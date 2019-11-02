package io.github.seiferma.youtuberss.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class YoutubeVideo {

	@Id
	private String id;
	private String title;
	@Lob
	private String description;
	private String thumbnail;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "channel_id")
	private YoutubeChannel channel;
	private Date date;
	private long durationInSeconds;

	public YoutubeVideo() {
		// intentionally left blank
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public YoutubeChannel getChannel() {
		return channel;
	}

	public void setChannel(YoutubeChannel channel) {
		this.channel = channel;
	}

	public long getDurationInSeconds() {
		return durationInSeconds;
	}

	public void setDurationInSeconds(long durationInSeconds) {
		this.durationInSeconds = durationInSeconds;
	}

}
