package io.github.seiferma.youtuberss.service.impl.youtubedto;

public class Thumbnail {
	private String url;
	private int width;
	private int height;

	public Thumbnail() {

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}