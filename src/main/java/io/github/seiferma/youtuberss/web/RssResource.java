package io.github.seiferma.youtuberss.web;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.View;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.service.YoutubeChannelService;
import io.github.seiferma.youtuberss.web.impl.YoutubeChannelRssViewFactory;

@RestController
@RequestMapping("/rss")
public class RssResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(RssResource.class);
	private final YoutubeChannelService channelService;
	private final YoutubeChannelRssViewFactory rssViewFactory;

	public RssResource(YoutubeChannelService channelService, YoutubeChannelRssViewFactory rssViewFactory) {
		this.channelService = channelService;
		this.rssViewFactory = rssViewFactory;
	}

	@GetMapping("/username/{username:[a-zA-Z0-9-_]+}")
	public View getFeedForUser(@PathVariable String username) {
		LOGGER.info("Received RSS request by username {}", username);
		YoutubeChannel channel = channelService.getByUsername(username)
				.orElseThrow(() -> new NoSuchElementException("There is no channel for user " + username));
		return rssViewFactory.build(channel);
	}
	
	@GetMapping("/channelid/{channelId:[a-zA-Z0-9-_]}")
	public View getFeedForChannel(@PathVariable String channelId) {
		LOGGER.info("Received RSS request by channel id {}", channelId);
		YoutubeChannel channel = channelService.getByChannelId(channelId)
				.orElseThrow(() -> new NoSuchElementException("There is no channel " + channelId));
		return rssViewFactory.build(channel);
	}

}
