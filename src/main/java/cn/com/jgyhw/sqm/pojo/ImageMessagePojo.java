package cn.com.jgyhw.sqm.pojo;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * 回复消息之图片消息
 */
@XStreamAlias("ImageMessage")
public class ImageMessagePojo extends BaseMessagePojo {

    @XStreamAlias("Image")
    private ImagePojo Image;

    public ImagePojo getImage() {
        return Image;
    }

    public void setImage(ImagePojo image) {
        Image = image;
    }
}
