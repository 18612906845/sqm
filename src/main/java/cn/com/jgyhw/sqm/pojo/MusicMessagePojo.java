package cn.com.jgyhw.sqm.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 回复消息之音乐消息
 */
@XStreamAlias("MusicMessage")
public class MusicMessagePojo extends BaseMessagePojo {

    // 回复的消息内容
    @XStreamAlias("Content")
    private String Content;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
