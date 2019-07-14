package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.WxToken;
import cn.com.jgyhw.sqm.mapper.WxTokenMapper;
import cn.com.jgyhw.sqm.service.IWxTokenService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by WangLei on 2019/4/14 0014 21:38
 */
@Service("wxTokenService")
public class WxTokenServiceImpl implements IWxTokenService {

    private static Logger LOGGER = LogManager.getLogger(WxTokenServiceImpl.class);

    @Autowired
    private WxTokenMapper wxTokenMapper;

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    /**
     * 定时获取微信公众号服务Api令牌
     */
    @Override
    public void timingGetWxGzhServiceApiToken() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_get_service_api_token_request_url");
        String appId = configurationPropertiesSqm.getWX_GZH_APP_ID();
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("APPID", appId);
            url = url.replaceAll("APPSECRET", configurationPropertiesSqm.getWX_GZH_APP_SECRET());
        }

        LOGGER.info("定时获取微信公众号服务Api令牌，参数：URL--> " + url);

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("定时获取微信公众号服务Api令牌结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);

                String accessToken = jsonObject.getString("access_token");
                if(!StringUtils.isBlank(accessToken)){
                    // 查询库里现有令牌
                    int tokenType = Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_gzh_server_api"));
                    WxToken wt = wxTokenMapper.selectWxTokenByAppIdAndTokenType(appId, tokenType);
                    if(wt == null){
                        wt = new WxToken();
                        wt.setId(UUID.randomUUID().toString());
                        wt.setAppId(appId);
                        wt.setToken(accessToken);
                        wt.setTokenType(tokenType);
                        wt.setUpdateTime(new Date());
                        wxTokenMapper.insert(wt);
                    }else{
                        wt.setToken(accessToken);
                        wt.setUpdateTime(new Date());
                        wxTokenMapper.updateByPrimaryKey(wt);
                    }
                    configurationPropertiesSqm.setWX_GZH_SERVER_API_TOKEN(accessToken);
                }
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("定时获取微信公众号服务Api令牌异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭定时获取微信公众号服务Api令牌异常--> ", e);
            }
        }
    }

    /**
     * 定时获取微信小程序服务Api令牌
     */
    @Override
    public void timingGetWxXcxServiceApiToken() {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_get_service_api_token_request_url");
        String appId = configurationPropertiesSqm.getWX_XCX_APP_ID();
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("APPID", appId);
            url = url.replaceAll("APPSECRET", configurationPropertiesSqm.getWX_XCX_APP_SECRET());
        }

        LOGGER.info("定时获取微信小程序服务Api令牌，参数：URL--> " + url);

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("定时获取微信小程序服务Api令牌结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);

                String accessToken = jsonObject.getString("access_token");
                if(!StringUtils.isBlank(accessToken)){
                    // 查询库里现有令牌
                    int tokenType = Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_xcx_server_api"));
                    WxToken wt = wxTokenMapper.selectWxTokenByAppIdAndTokenType(appId, tokenType);
                    if(wt == null){
                        wt = new WxToken();
                        wt.setId(UUID.randomUUID().toString());
                        wt.setAppId(appId);
                        wt.setToken(accessToken);
                        wt.setTokenType(tokenType);
                        wt.setUpdateTime(new Date());
                        wxTokenMapper.insert(wt);
                    }else{
                        wt.setToken(accessToken);
                        wt.setUpdateTime(new Date());
                        wxTokenMapper.updateByPrimaryKey(wt);
                    }
                    configurationPropertiesSqm.setWX_XCX_SERVER_API_TOKEN(accessToken);;
                }
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("定时获取微信小程序服务Api令牌异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭定时获取微信小程序服务Api令牌异常--> ", e);
            }
        }
    }

    /**
     * 查询所有微信令牌
     *
     * @return
     */
    @Override
    public List<WxToken> queryWxTokenAll() {
        return wxTokenMapper.selectWxTokenAll();
    }
}
