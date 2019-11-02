package io.github.seiferma.youtuberss.service.impl.youtubedto;

import java.util.ArrayList;
import java.util.List;

public class PlaylistItemsResponseDTO {
	private List<PlaylistItemDTO> items;
	
	public PlaylistItemsResponseDTO() {
		items = new ArrayList<>();
	}

	public List<PlaylistItemDTO> getItems() {
		return items;
	}

	public void setItems(List<PlaylistItemDTO> items) {
		this.items = items;
	}
}