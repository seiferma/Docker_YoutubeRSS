package io.github.seiferma.youtuberss.persistance;

import org.springframework.data.repository.CrudRepository;

import io.github.seiferma.youtuberss.domain.YoutubeVideo;

public interface YoutubeVideoRepository extends CrudRepository<YoutubeVideo, String> {

}
