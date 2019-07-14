package cn.com.jgyhw.sqm.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 回复消息之视频消息
 */
@XStreamAlias("VideoMessage")
public class VideoMessagePojo extends BaseMessagePojo {

    @XStreamAlias("Video")
    private VideoPojo Video;

    public VideoPojo getVideo() {
        return Video;
    }

    public void setVideo(VideoPojo video) {
        Video = video;
    }
}
