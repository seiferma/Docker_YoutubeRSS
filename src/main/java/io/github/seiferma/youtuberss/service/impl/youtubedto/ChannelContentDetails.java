package io.github.seiferma.youtuberss.service.impl.youtubedto;

public class ChannelContentDetails {
	private ChannelRelatedPlaylists relatedPlaylists;

	public ChannelContentDetails() {

	}

	public ChannelRelatedPlaylists getRelatedPlaylists() {
		return relatedPlaylists;
	}

	public void setRelatedPlaylists(ChannelRelatedPlaylists relatedPlaylists) {
		this.relatedPlaylists = relatedPlaylists;
	}
}