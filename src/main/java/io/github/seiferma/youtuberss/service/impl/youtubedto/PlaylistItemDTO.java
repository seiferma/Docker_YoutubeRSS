package io.github.seiferma.youtuberss.service.impl.youtubedto;

public class PlaylistItemDTO {
	private PlaylistItemSnippet snippet;

	public PlaylistItemDTO() {

	}

	public PlaylistItemSnippet getSnippet() {
		return snippet;
	}

	public void setSnippet(PlaylistItemSnippet snippet) {
		this.snippet = snippet;
	}

}