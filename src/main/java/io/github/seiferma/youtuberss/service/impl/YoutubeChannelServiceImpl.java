package io.github.seiferma.youtuberss.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;
import io.github.seiferma.youtuberss.persistance.YoutubeChannelRepository;
import io.github.seiferma.youtuberss.service.YoutubeChannelService;
import io.github.seiferma.youtuberss.service.YoutubeDataAPIService;

@Service
public class YoutubeChannelServiceImpl implements YoutubeChannelService {

	private final YoutubeDataAPIService dataApi;
	private final YoutubeChannelRepository repo;
	private final int minimumUpdateIntervalInMinutes;

	@Autowired
	public YoutubeChannelServiceImpl(YoutubeChannelRepository repo, YoutubeDataAPIService dataApi,
			@Value("${application.minimum-update-minutes}") int minimumUpdateMinutes) {
		this.repo = repo;
		this.dataApi = dataApi;
		this.minimumUpdateIntervalInMinutes = minimumUpdateMinutes;
	}

	@Override
	public Optional<YoutubeChannel> getByUsername(String username) {
		return get(() -> repo.findByUsername(username), () -> dataApi.getChannelByUsername(username));
	}

	@Override
	public Optional<YoutubeChannel> getByChannelId(String channelId) {
		return get(() -> repo.findById(channelId), () -> dataApi.getChannelById(channelId));
	}

	protected Optional<YoutubeChannel> get(Supplier<Optional<YoutubeChannel>> repoQuery,
			Supplier<Optional<YoutubeChannel>> apiQuery) {
		Optional<YoutubeChannel> youtubeChannel = repoQuery.get();
		if (!youtubeChannel.isPresent() || isOutdated(youtubeChannel.get())) {
			youtubeChannel = initYoutubeChannel(apiQuery);
		}
		return youtubeChannel;
	}

	private Optional<YoutubeChannel> initYoutubeChannel(Supplier<Optional<YoutubeChannel>> apiQuery) {
		Optional<YoutubeChannel> channel = apiQuery.get();
		channel.ifPresent(repo::save);
		return channel;
	}

	private boolean isOutdated(YoutubeChannel channel) {
		Date oldestPossibleUpdateDate = Date
				.from(Instant.now().minus(minimumUpdateIntervalInMinutes, ChronoUnit.MINUTES));
		Date actualUpdateDate = channel.getLastUpdate();
		return actualUpdateDate.before(oldestPossibleUpdateDate);
	}

}
