package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.pojo.ArticlePojo;
import cn.com.jgyhw.sqm.pojo.TemplateMessagePojo;
import cn.com.jgyhw.sqm.service.IWxGzhMessageSendService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import cn.com.jgyhw.sqm.util.WxGzhMessageUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Created by WangLei on 2019/5/3 0003 16:11
 */
@Service("wxGzhMessageSendService")
public class WxGzhMessageSendServiceImpl implements IWxGzhMessageSendService {

    private static Logger LOGGER = LogManager.getLogger(WxGzhMessageSendServiceImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    /**
     * 发送文本客服消息
     *
     * @param toUser
     * @param content
     */
    @Override
    public void sendTextMessage(String toUser, String content) {
        if(StringUtils.isBlank(toUser) || StringUtils.isBlank(content)){
            return ;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_send_custom_message_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_GZH_SERVER_API_TOKEN());
        }
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        JSONObject textJsonObj = new JSONObject();
        textJsonObj.put("content", content);

        JSONObject jsonData = new JSONObject();
        jsonData.put("touser", toUser);
        jsonData.put("msgtype", WxGzhMessageUtil.ANSWER_MSG_TYPE_TEXT);
        jsonData.put("text", textJsonObj);

        try {
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonData.toJSONString(), "UTF-8"));
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                LOGGER.info("发送文本客服消息结果--> 成功");
            }
        } catch (IOException e) {
            LOGGER.error("发送文本客服消息异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求发送文本客服消息异常--> ", e);
            }
        }
    }

    /**
     * 发送图文客服消息
     *
     * @param toUser          接收者微信标识
     * @param articlePojoList 图文消息对象集合
     */
    @Override
    public void sendNewsMessage(String toUser, List<ArticlePojo> articlePojoList) {
        if(StringUtils.isBlank(toUser) || articlePojoList == null || articlePojoList.isEmpty()){
            return ;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_send_custom_message_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_GZH_SERVER_API_TOKEN());
        }
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        JSONArray articlesJsonArray = new JSONArray();
        for(ArticlePojo ap : articlePojoList){
            JSONObject itemJsonObj = new JSONObject();
            itemJsonObj.put("title", ap.getTitle());
            itemJsonObj.put("description", ap.getDescription());
            itemJsonObj.put("url", ap.getUrl());
            itemJsonObj.put("picurl", ap.getPicUrl());
            articlesJsonArray.add(itemJsonObj);
        }

        JSONObject newsJsonObj = new JSONObject();
        newsJsonObj.put("articles", articlesJsonArray);

        JSONObject jsonData = new JSONObject();
        jsonData.put("touser", toUser);
        jsonData.put("msgtype", WxGzhMessageUtil.ANSWER_MSG_TYPE_NEWS);
        jsonData.put("news", newsJsonObj);

        try {
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonData.toJSONString(), "UTF-8"));
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                LOGGER.info("发送图文客服消息结果--> 成功");
            }
        } catch (IOException e) {
            LOGGER.error("发送图文客服消息异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求发送图文客服消息异常--> ", e);
            }
        }
    }

    /**
     * 发送模版消息到公众号
     *
     * @param templateMessagePojo 模版消息对象
     */
    @Override
    public void sendTemplateMessage(TemplateMessagePojo templateMessagePojo) {
        if(templateMessagePojo == null){
            return ;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_send_template_message_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_GZH_SERVER_API_TOKEN());
        }
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;
        String dataJsonStr = JSONObject.toJSONString(templateMessagePojo);
        LOGGER.info("发送模版消息内容--> " + dataJsonStr);
        try {
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(dataJsonStr, "UTF-8"));
            response = httpClient.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                LOGGER.info("发送模版消息结果--> 成功");
            }
        } catch (IOException e) {
            LOGGER.error("发送模版消息异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求发送模版消息异常--> ", e);
            }
        }
    }
}
