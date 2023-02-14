package io.github.seiferma.youtuberss.web.impl;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.jdom2.Element;
import org.jdom2.Namespace;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Description;
import com.rometools.rome.feed.rss.Enclosure;
import com.rometools.rome.feed.rss.Guid;
import com.rometools.rome.feed.rss.Image;
import com.rometools.rome.feed.rss.Item;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.domain.YoutubeVideo;

public class YoutubeChannelRssView extends AbstractRssFeedView {

	private static final Namespace NS_ITUNES = Namespace.getNamespace("itunes", "http://www.itunes.com/dtds/podcast-1.0.dtd");
	private YoutubeChannel youtubeChannel;
	private Function<YoutubeVideo, String> videoUrlProvider;

	public YoutubeChannelRssView(YoutubeChannel youtubeChannel, Function<YoutubeVideo, String> videoUrlProvider) {
		this.youtubeChannel = youtubeChannel;
		this.videoUrlProvider = videoUrlProvider;
	}
	
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
		final String channelUrl = buildChannelUrl(youtubeChannel);
		feed.setTitle(youtubeChannel.getTitle());
		feed.setDescription(youtubeChannel.getDescription());
		Image feedImage = new Image();
		feedImage.setUrl(youtubeChannel.getThumbnailUrl());
		feedImage.setTitle(youtubeChannel.getTitle());
		feedImage.setHeight(youtubeChannel.getThumbnailHeight());
		feedImage.setWidth(youtubeChannel.getThumbnailWidth());
		feedImage.setLink(channelUrl);
		feed.setImage(feedImage);
		feed.setLink(channelUrl);
		
		List<Element> itunesElements = new ArrayList<>();
		itunesElements.add(buildItunesElement("author", youtubeChannel.getTitle()));
		itunesElements.add(buildItunesElement("subtitle", youtubeChannel.getTitle()));
		itunesElements.add(buildItunesElement("summary", youtubeChannel.getDescription()));
		itunesElements.add(buildItunesElementLink("image", youtubeChannel.getThumbnailUrl()));
		itunesElements.add(buildItunesElement("explicit", "no"));
		itunesElements.add(buildItunesElementText("category", "TV & Film"));
		feed.setForeignMarkup(itunesElements);
	}

	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		List<Item> items = new ArrayList<>();
		
		for (YoutubeVideo youtubevideo : youtubeChannel.getVideos()) {
			final String videoUrl = buildVideoUrl(youtubevideo);
			Item entry = new Item();
			Guid guid = new Guid();
			guid.setValue(videoUrl);
			guid.setPermaLink(true);
			entry.setGuid(guid);
			entry.setTitle(youtubevideo.getTitle());
			entry.setLink(buildDirectVideoUrl(youtubevideo));
			Description description = new Description();
			description.setType(MimeTypeUtils.TEXT_PLAIN_VALUE);
			description.setValue(youtubevideo.getDescription());
			entry.setPubDate(youtubevideo.getDate());
			Enclosure enclosure = new Enclosure();
			enclosure.setUrl(videoUrl);
			enclosure.setType("video/mp4");
			entry.setEnclosures(Arrays.asList(enclosure));
			
			List<Element> itunesElements = new ArrayList<>();
			itunesElements.add(buildItunesElement("author", youtubevideo.getChannel().getTitle()));
			itunesElements.add(buildItunesElement("subtitle", youtubevideo.getTitle()));
			itunesElements.add(buildItunesElement("summary", youtubevideo.getDescription()));
			itunesElements.add(buildItunesElementLink("image", youtubevideo.getThumbnail()));
			itunesElements.add(buildItunesElement("duration", formatDuration(youtubevideo.getDurationInSeconds())));
			itunesElements.add(buildItunesElement("explicit", "no"));
			itunesElements.add(buildItunesElement("order", Integer.toString(items.size())));
			entry.setForeignMarkup(itunesElements);
			
			items.add(entry);
		}

		return items;
	}
	
	private String formatDuration(long durationInSeconds) {
		Duration duration = Duration.of(durationInSeconds, ChronoUnit.SECONDS);
		String durationString = String.format("%02d:%02d", duration.toMinutes() % 60, duration.getSeconds() % 60);
		if (duration.toHours() > 0) {
			durationString = String.format("%02d:", duration.toHours()) + durationString;
		}
		return durationString;
	}

	private String buildVideoUrl(YoutubeVideo youtubevideo) {
		return videoUrlProvider.apply(youtubevideo);
	}
	
	private String buildChannelUrl(YoutubeChannel channel) {
		return "https://www.youtube.com/channel/" + channel.getId();
	}
	
	private String buildDirectVideoUrl(YoutubeVideo youtubevideo) {
		return "https://www.youtube.com/watch?v=" + youtubevideo.getId();
	}
	
	private Element buildItunesElement(String name, String value) {
		Element element = new Element(name, NS_ITUNES);
		element.setText(value);
		return element;
	}
	
	private Element buildItunesElementLink(String name, String link) {
		return buildItunesElementWithAttribute(name, "href", link);
	}
	
	private Element buildItunesElementText(String name, String value) {
		return buildItunesElementWithAttribute(name, "text", value);
	}
	
	private Element buildItunesElementWithAttribute(String name, String attributeName, String attributeValue) {
		Element element = new Element(name, NS_ITUNES);
		element.setAttribute(attributeName, attributeValue);
		return element;
	}

}
