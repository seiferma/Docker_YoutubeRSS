package io.github.seiferma.youtuberss.service.impl.youtubedto;

import java.util.ArrayList;
import java.util.List;

public class VideoResponseDTO {

	private List<VideoDTO> items;

	public VideoResponseDTO() {
		items = new ArrayList<>();
	}

	public List<VideoDTO> getItems() {
		return items;
	}

	public void setItems(List<VideoDTO> items) {
		this.items = items;
	}

}
