package io.github.seiferma.youtuberss.persistance;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;

public interface YoutubeChannelRepository extends CrudRepository<YoutubeChannel, String> {

	Optional<YoutubeChannel> findByUsername(String username);
	
}
