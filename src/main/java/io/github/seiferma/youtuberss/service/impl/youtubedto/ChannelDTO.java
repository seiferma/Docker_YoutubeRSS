package io.github.seiferma.youtuberss.service.impl.youtubedto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class ChannelDTO {
	private String id;
	private ChannelSnippet snippet;
	private ChannelContentDetails contentDetails;
	@JsonIgnore
	private String username;

	public ChannelDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ChannelSnippet getSnippet() {
		return snippet;
	}

	public void setSnippet(ChannelSnippet snippet) {
		this.snippet = snippet;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public ChannelContentDetails getContentDetails() {
		return contentDetails;
	}

	public void setContentDetails(ChannelContentDetails contentDetails) {
		this.contentDetails = contentDetails;
	}
}