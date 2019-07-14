package cn.com.jgyhw.sqm.pojo;

import java.util.Map;

/**
 * 模版消息Pojo
 */
public class TemplateMessagePojo {

    /**
     * 接收者openid
     */
    private String touser;

    /**
     * 模板ID
     */
    private String template_id;

    /**
     * 消息点击跳转URL
     */
    private String url;

    /**
     * 跳小程序所需数据，不需跳小程序可不用传该数据
     */
    private TemplateMessageMiniprogramPojo miniprogram;

    /**
     * 模板数据，数据格式如下
     * "data":{
     *      "first": {
     *          "value":"恭喜你购买成功！",
     *          "color":"#173177"
     *      },
     *      "keyword1":{
     *          "value":"巧克力",
     *          "color":"#173177"
     *      }
     * }
     */
    private Map<String, Object> data;

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TemplateMessageMiniprogramPojo getMiniprogram() {
        return miniprogram;
    }

    public void setMiniprogram(TemplateMessageMiniprogramPojo miniprogram) {
        this.miniprogram = miniprogram;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "TemplateMessagePojo{" +
                "touser='" + touser + '\'' +
                ", template_id='" + template_id + '\'' +
                ", url='" + url + '\'' +
                ", miniprogram=" + miniprogram +
                ", data=" + data +
                '}';
    }
}
