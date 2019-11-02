package io.github.seiferma.youtuberss.service;

import java.io.IOException;
import java.io.InputStream;

public interface YoutubeVideoService {

	InputStream getVideoInputStream(String videoId) throws IOException;
	
}
