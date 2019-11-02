package io.github.seiferma.youtuberss.service.impl.youtubedto;

import java.util.HashMap;
import java.util.Map;

public class ChannelSnippet {
	private String title;
	private String description;
	private Map<String, Thumbnail> thumbnails;
	
	public ChannelSnippet() {
		thumbnails = new HashMap<>();
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


}