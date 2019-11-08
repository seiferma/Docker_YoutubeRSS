package io.github.seiferma.youtuberss.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import io.github.seiferma.youtuberss.service.YoutubeVideoService;

@RestController
@RequestMapping("/video")
@EnableWebMvc
public class YoutubeStreamResource implements WebMvcConfigurer {

	private static final Logger LOGGER = LoggerFactory.getLogger(YoutubeStreamResource.class);
	private static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
	private static final String HEADER_CONTENT_TYPE = "video/mp4";
	private final YoutubeVideoService videoService;

	public YoutubeStreamResource(YoutubeVideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/{videoFilename:[a-zA-Z0-9-_]+[.][a-z0-9]+}")
	public ResponseEntity<StreamingResponseBody> getVideoStream(final HttpServletResponse response,
			@PathVariable("videoFilename") String videoFilename) throws IOException {
		response.setContentType(HEADER_CONTENT_TYPE);
		response.setHeader(HEADER_CONTENT_DISPOSITION, String.format("attachment;filename=%s", videoFilename));

		final String videoId = videoFilename.replaceFirst("([^.]+)([.][^.]+)?", "$1");

		StreamingResponseBody stream = out -> {
			LOGGER.info("Starting streaming of {}", videoId);
			try (InputStream bis = videoService.getVideoInputStream(videoId)) {
				IOUtils.copy(bis, out);
			}
			catch (IOException e) {
				if (isBrokenPipeException(e)) {
					LOGGER.warn("Broken pipe during streaming of {}. Most probably, client left.", videoId);
					return;
				}
				throw e;
			}
		};

		return new ResponseEntity<StreamingResponseBody>(stream, HttpStatus.OK);
	}

	@Override
	public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
		configurer.setDefaultTimeout(6*60*60*1000);
		configurer.setTaskExecutor(new ConcurrentTaskExecutor(Executors.newFixedThreadPool(6)));
		WebMvcConfigurer.super.configureAsyncSupport(configurer);
	}

	@ExceptionHandler(IOException.class)
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	public Object handleBrokenPipeIOException(IOException ex, HttpServletRequest request) {
	    if (isBrokenPipeException(ex)) {
	        return null;
	    } else {
	        return new HttpEntity<>(ex.getMessage());
	    }
	}

	private static boolean isBrokenPipeException(IOException ex) {
		boolean isBrokenPipe = Optional.ofNullable(ExceptionUtils.getRootCauseMessage(ex)).map(String::toLowerCase)
				.map(s -> s.contains("broken pipe")).orElse(false);
		return isBrokenPipe;
	}
	
}
