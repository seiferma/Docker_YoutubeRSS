package io.github.seiferma.youtuberss.service.impl;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.domain.YoutubeVideo;
import io.github.seiferma.youtuberss.service.YoutubeDataAPIService;
import io.github.seiferma.youtuberss.service.impl.youtubedto.ChannelDTO;
import io.github.seiferma.youtuberss.service.impl.youtubedto.ChannelResponseDTO;
import io.github.seiferma.youtuberss.service.impl.youtubedto.PlaylistItemDTO;
import io.github.seiferma.youtuberss.service.impl.youtubedto.PlaylistItemsResponseDTO;
import io.github.seiferma.youtuberss.service.impl.youtubedto.Thumbnail;
import io.github.seiferma.youtuberss.service.impl.youtubedto.VideoContentDetails;
import io.github.seiferma.youtuberss.service.impl.youtubedto.VideoDTO;
import io.github.seiferma.youtuberss.service.impl.youtubedto.VideoResponseDTO;

@Component
public class YoutubeDataAPIServiceImpl implements YoutubeDataAPIService {

	private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeDataAPIServiceImpl.class);
	private final RestTemplate restTemplate;
	private final String apiKey;

	@Autowired
	public YoutubeDataAPIServiceImpl(RestTemplate restTemplate, @Value("${application.youtube.apikey}") String apiKey) {
		this.restTemplate = restTemplate;
		this.apiKey = apiKey;
	}

	@Override
	public Optional<YoutubeChannel> getChannelById(String channelId) {
		LOGGER.info("Receiving channel {} from youtube.", channelId);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("part", "id,snippet,contentDetails");
		parameters.put("id", channelId);
		parameters.put("apiKey", apiKey);
		ResponseEntity<ChannelResponseDTO> dtoResponse = restTemplate.getForEntity(
				"https://www.googleapis.com/youtube/v3/channels?part={part}&id={id}&key={apiKey}",
				ChannelResponseDTO.class, parameters);
		return getChannel(dtoResponse);
	}

	@Override
	public Optional<YoutubeChannel> getChannelByUsername(String username) {
		LOGGER.info("Receiving channel for user {} from youtube.", username);
		Map<String, String> parameters = new HashMap<>();
		parameters.put("part", "id,snippet,contentDetails");
		parameters.put("username", username);
		parameters.put("apiKey", apiKey);
		ResponseEntity<ChannelResponseDTO> dtoResponse = restTemplate.getForEntity(
				"https://www.googleapis.com/youtube/v3/channels?part={part}&forUsername={username}&key={apiKey}",
				ChannelResponseDTO.class, parameters);
		Optional.ofNullable(dtoResponse.getBody())
				.ifPresent(dto -> dto.getItems().stream().findFirst().ifPresent(i -> i.setUsername(username)));
		return getChannel(dtoResponse);
	}

	protected Optional<YoutubeChannel> getChannel(ResponseEntity<ChannelResponseDTO> dtoResponse) {
		if (!dtoResponse.getStatusCode().is2xxSuccessful()) {
			return Optional.empty();
		}
		Optional<ChannelDTO> itemDto = dtoResponse.getBody().getItems().stream().findFirst();
		if (!itemDto.isPresent()) {
			return Optional.empty();
		}
		ChannelDTO dto = itemDto.get();

		YoutubeChannel entity = new YoutubeChannel();
		entity.setId(dto.getId());
		entity.setDescription(dto.getSnippet().getDescription());
		entity.setTitle(dto.getSnippet().getTitle());
		entity.setPlaylistId(dto.getContentDetails().getRelatedPlaylists().getUploads());
		Optional<Thumbnail> thumbnail = getLargestThumbnail(dto.getSnippet().getThumbnails().values());
		if (thumbnail.isPresent()) {
			entity.setThumbnailHeight(thumbnail.get().getHeight());
			entity.setThumbnailWidth(thumbnail.get().getWidth());
			entity.setThumbnailUrl(thumbnail.get().getUrl());
		}
		entity.setVideos(getVideos(entity.getPlaylistId()));
		entity.setUsername(dto.getUsername());
		entity.setLastUpdate(Date.from(Instant.now()));

		return Optional.of(entity);
	}

	@Override
	public List<YoutubeVideo> getVideos(String playlistId) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("part", "id,snippet");
		parameters.put("apiKey", apiKey);
		parameters.put("playlistId", playlistId);
		parameters.put("maxResults", "50");
		ResponseEntity<PlaylistItemsResponseDTO> dtoResponse = restTemplate.getForEntity(
				"https://www.googleapis.com/youtube/v3/playlistItems?part={part}&playlistId={playlistId}&maxResults={maxResults}&key={apiKey}",
				PlaylistItemsResponseDTO.class, parameters);

		if (!dtoResponse.getStatusCode().is2xxSuccessful()) {
			return Collections.emptyList();
		}
		List<PlaylistItemDTO> dtos = dtoResponse.getBody().getItems();

		List<YoutubeVideo> results = dtos.stream().map(this::createYoutubeVideo).collect(Collectors.toList());
		setDurationsInVideos(results);
		return results;
	}

	protected void setDurationsInVideos(Collection<YoutubeVideo> youtubeVideos) {
		String idList = youtubeVideos.stream().map(YoutubeVideo::getId).collect(Collectors.joining(","));
		Map<String, String> parameters = new HashMap<>();
		parameters.put("part", "id,contentDetails");
		parameters.put("apiKey", apiKey);
		parameters.put("id", idList);

		ResponseEntity<VideoResponseDTO> dtoResponse = restTemplate.getForEntity(
				"https://www.googleapis.com/youtube/v3/videos?part={part}&id={id}&key={apiKey}", VideoResponseDTO.class,
				parameters);

		if (!dtoResponse.getStatusCode().is2xxSuccessful()) {
			return;
		}

		List<VideoDTO> dtos = dtoResponse.getBody().getItems();
		Map<String, Long> idToDuration = dtos.stream()
				.collect(Collectors.toMap(VideoDTO::getId, YoutubeDataAPIServiceImpl::getDurationInSeconds));
		youtubeVideos.forEach(v -> v.setDurationInSeconds(idToDuration.getOrDefault(v.getId(), 0l)));
	}

	protected YoutubeVideo createYoutubeVideo(PlaylistItemDTO dto) {
		YoutubeVideo entity = new YoutubeVideo();

		entity.setId(dto.getSnippet().getResourceId().getVideoId());
		entity.setTitle(dto.getSnippet().getTitle());
		entity.setDescription(dto.getSnippet().getDescription());
		entity.setDate(dto.getSnippet().getPublishedAt());
		getLargestThumbnail(dto.getSnippet().getThumbnails().values()).map(Thumbnail::getUrl).ifPresent(entity::setThumbnail);

		return entity;
	}

	private Optional<Thumbnail> getLargestThumbnail(Collection<Thumbnail> thumbnails) {
		return thumbnails.stream().sorted((e1, e2) -> e2.getWidth() - e1.getWidth()).findFirst();
	}
	
	private static long getDurationInSeconds(VideoDTO video) {
		return Optional.ofNullable(video).map(VideoDTO::getContentDetails).map(VideoContentDetails::getDuration)
				.map(Duration::parse).map(d -> d.get(ChronoUnit.SECONDS)).orElse(0l);
	}

}
