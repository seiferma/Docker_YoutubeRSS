package io.github.seiferma.youtuberss.service.impl.youtubedto;

import java.util.ArrayList;
import java.util.List;

public class ChannelResponseDTO {
	private List<ChannelDTO> items;

	public ChannelResponseDTO() {
		items = new ArrayList<>();
	}

	public List<ChannelDTO> getItems() {
		return items;
	}

	public void setItems(List<ChannelDTO> items) {
		this.items = items;
	}

}