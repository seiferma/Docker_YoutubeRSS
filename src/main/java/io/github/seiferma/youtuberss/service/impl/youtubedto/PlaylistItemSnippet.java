package io.github.seiferma.youtuberss.service.impl.youtubedto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PlaylistItemSnippet {
	private Date publishedAt;
	private String title;
	private String description;
	private Map<String, Thumbnail> thumbnails;
	private ResourceId resourceId;

	public PlaylistItemSnippet() {
		thumbnails = new HashMap<>();
	}

	public Date getPublishedAt() {
		return publishedAt;
	}

	public void setPublishedAt(Date publishedAt) {
		this.publishedAt = publishedAt;
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

	public Map<String, Thumbnail> getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(Map<String, Thumbnail> thumbnails) {
		this.thumbnails = thumbnails;
	}

	public ResourceId getResourceId() {
		return resourceId;
	}

	public void setResourceId(ResourceId resourceId) {
		this.resourceId = resourceId;
	}
}