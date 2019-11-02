package io.github.seiferma.youtuberss.service;

import java.util.Collection;
import java.util.Optional;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.domain.YoutubeVideo;

public interface YoutubeDataAPIService {

	Optional<YoutubeChannel> getChannelByUsername(String username);

	Optional<YoutubeChannel> getChannelById(String channelId);

	Collection<YoutubeVideo> getVideos(String playlistId);

}
