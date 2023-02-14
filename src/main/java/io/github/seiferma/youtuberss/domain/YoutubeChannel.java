package io.github.seiferma.youtuberss.domain;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

@Entity
public class YoutubeChannel {

	@Id
	private String id;
	private String username;
	private String title;
	@Lob
	private String description;
	private String thumbnailUrl;
	private int thumbnailWidth;
	private int thumbnailHeight;
	private String playlistId;
	@OneToMany(mappedBy = "channel", orphanRemoval = true, fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
	@OrderBy("date DESC")
	private List<YoutubeVideo> videos;
	private Date lastUpdate;

	public YoutubeChannel() {
		// intentionally left blank
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlaylistId() {
		return playlistId;
	}

	public void setPlaylistId(String playlistId) {
		this.playlistId = playlistId;
	}

	public List<YoutubeVideo> getVideos() {
		return videos;
	}

	public void setVideos(List<YoutubeVideo> videos) {
		this.videos = videos;
		videos.stream().forEach(v -> v.setChannel(this));
	}

	public void addVideo(YoutubeVideo video) {
		this.videos.add(video);
		video.setChannel(this);
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public void setThumbnailUrl(String thumbnail_url) {
		this.thumbnailUrl = thumbnail_url;
	}

	public int getThumbnailWidth() {
		return thumbnailWidth;
	}

	public void setThumbnailWidth(int thumbnail_width) {
		this.thumbnailWidth = thumbnail_width;
	}

	public int getThumbnailHeight() {
		return thumbnailHeight;
	}

	public void setThumbnailHeight(int thumbnail_height) {
		this.thumbnailHeight = thumbnail_height;
	}

}
