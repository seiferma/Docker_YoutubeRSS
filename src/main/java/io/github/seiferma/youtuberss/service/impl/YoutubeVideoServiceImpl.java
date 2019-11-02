package io.github.seiferma.youtuberss.service.impl;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ProcessBuilder.Redirect;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.seiferma.youtuberss.service.YoutubeVideoService;

@Component
public class YoutubeVideoServiceImpl implements YoutubeVideoService {

	private static final String URL_PATTERN = "https://www.youtube.com/watch?v=%s";
	private final String executablePath;

	@Autowired
	public YoutubeVideoServiceImpl(@Value("${application.youtube.executable}") String executablePath) {
		this.executablePath = executablePath;
	}

	@Override
	public InputStream getVideoInputStream(String videoId) throws IOException {
		Process process = new ProcessBuilder(executablePath, "-q", "-o", "-",
				String.format(URL_PATTERN, videoId)).redirectError(Redirect.INHERIT).start();
		return new BufferedInputStream(process.getInputStream());
	}
	
}
