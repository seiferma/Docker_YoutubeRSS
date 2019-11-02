package io.github.seiferma.youtuberss.service;

import java.util.Optional;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;

public interface YoutubeChannelService {

	Optional<YoutubeChannel> getByUsername(String username);

	Optional<YoutubeChannel> getByChannelId(String channelId);
	
}
