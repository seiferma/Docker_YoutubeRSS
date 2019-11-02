package io.github.seiferma.youtuberss.web.impl;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.domain.YoutubeVideo;

@Component
public class YoutubeChannelRssViewFactoryImpl implements YoutubeChannelRssViewFactory {

	final private String videoUrlPattern;

	public YoutubeChannelRssViewFactoryImpl(@Value("${application.url-pattern}") String videoUrlPattern) {
		this.videoUrlPattern = videoUrlPattern;
	}
	
	@Override
	public YoutubeChannelRssView build(YoutubeChannel channel) {
		Function<YoutubeVideo, String> urlProvider = video -> String.format(videoUrlPattern, video.getId());
		return new YoutubeChannelRssView(channel, urlProvider);
	}

}
