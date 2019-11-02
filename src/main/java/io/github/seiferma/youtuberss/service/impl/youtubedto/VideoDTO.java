package io.github.seiferma.youtuberss.service.impl.youtubedto;

public class VideoDTO {

	private String id;

	private VideoContentDetails contentDetails;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public VideoContentDetails getContentDetails() {
		return contentDetails;
	}

	public void setContentDetails(VideoContentDetails contentDetails) {
		this.contentDetails = contentDetails;
	}

}
