package io.github.seiferma.youtuberss.web.impl;

import io.github.seiferma.youtuberss.domain.YoutubeChannel;

public interface YoutubeChannelRssViewFactory {

	YoutubeChannelRssView build(YoutubeChannel channel);
	
}
