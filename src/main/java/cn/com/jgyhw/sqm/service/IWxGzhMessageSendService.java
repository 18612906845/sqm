package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.pojo.ArticlePojo;
import cn.com.jgyhw.sqm.pojo.TemplateMessagePojo;

import java.util.List;

/**
 * Created by WangLei on 2019/5/3 0003 16:07
 *
 * 微信公众号消息发送接口
 */
public interface IWxGzhMessageSendService {

    /**
     * 发送文本客服消息
     *
     * @param toUser
     * @param content
     */
    void sendTextMessage(String toUser, String content);

    /**
     * 发送图文客服消息
     *
     * @param toUser 接收者微信标识
     * @param articlePojoList 图文消息对象集合
     */
    void sendNewsMessage(String toUser, List<ArticlePojo> articlePojoList);

    /**
     * 发送模版消息到公众号
     *
     * @param templateMessagePojo 模版消息对象
     */
    void sendTemplateMessage(TemplateMessagePojo templateMessagePojo);
}
