package cn.com.jgyhw.sqm.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("Image")
public class ImagePojo {

    /**
     * 图片永久素材MediaId
     */
    @XStreamAlias("MediaId")
    private String MediaId;

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}
