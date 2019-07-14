package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.domain.WxXcxSessionKeyTemporary;
import cn.com.jgyhw.sqm.mapper.WxXcxSessionKeyTemporaryMapper;
import cn.com.jgyhw.sqm.service.IWxUserService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import cn.com.jgyhw.sqm.util.WxUtil;
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
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 获取授权，取得用户信息Controller
 */
@RequestMapping("/getAuthorization")
@Controller
public class GetAuthorizationController {

    private static Logger LOGGER = LogManager.getLogger(GetAuthorizationController.class);

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private WxXcxSessionKeyTemporaryMapper wxXcxSessionKeyTemporaryMapper;

    /**
     * 通过临时凭证Code获取微信小程序OpenId和SessionKey
     *
     * @param code 临时凭证
     * @return
     */
    @RequestMapping("/getWxUserOpenIdAndSessionKeyByCode")
    @ResponseBody
    public Map<String, Object> getWxUserOpenIdAndSessionKeyByCode(String code){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_get_openid_request_url");
        if(StringUtils.isBlank(url)){
            resultMap.put("msg", "服务请求地址为空");
            return resultMap;
        }

        url = url.replaceAll("APPID", configurationPropertiesSqm.getWX_XCX_APP_ID());
        url = url.replaceAll("SECRET", configurationPropertiesSqm.getWX_XCX_APP_SECRET());
        url = url.replaceAll("JSCODE", code);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("通过临时凭证Code获取微信小程序唯一标识结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);

                String openId = jsonObject.getString("openid");
                String sessionKey = jsonObject.getString("session_key");
                if(StringUtils.isBlank(openId) || StringUtils.isBlank(sessionKey)){
                    resultMap.put("msg", "未获取到OpenIdAndSessionKey");
                    return resultMap;
                }

                WxXcxSessionKeyTemporary wxskt = wxXcxSessionKeyTemporaryMapper.selectWxXcxSessionKeyTemporaryByOpenId(openId);
                if(wxskt == null){
                    wxskt = new WxXcxSessionKeyTemporary();
                    wxskt.setId(UUID.randomUUID().toString());
                    wxskt.setOpenId(openId);
                    wxskt.setSessionKey(sessionKey);
                    wxskt.setUpdateTime(new Date());
                    wxXcxSessionKeyTemporaryMapper.insert(wxskt);
                }else{
                    wxskt.setSessionKey(sessionKey);
                    wxskt.setUpdateTime(new Date());
                    wxXcxSessionKeyTemporaryMapper.updateByPrimaryKey(wxskt);
                }

                if(wxskt == null){
                    resultMap.put("msg", "未获取到OpenIdAndSessionKey临时表记录");
                    return resultMap;
                }
                resultMap.put("tempId", wxskt.getId());
                resultMap.put("msg", "");
                resultMap.put("status", true);
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("通过临时凭证Code获取微信小程序唯一标识异常--> ", e);
            return resultMap;
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭通过临时凭证Code获取微信小程序唯一标识异常--> ", e);
            }
            return resultMap;
        }
    }

    /**
     * 获取微信用户UnionId
     *
     * @param encryptedData 加密数据字符串
     * @param tempId 会话密匙临时表id
     * @param iv 自定义对称解密算法初始向量
     * @return
     */
    @RequestMapping("/getWxUserUnionId")
    @ResponseBody
    public Map<String, Object> getWxUserUnionId(String tempId, String encryptedData, String iv){

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        WxXcxSessionKeyTemporary wxskt = wxXcxSessionKeyTemporaryMapper.selectByPrimaryKey(tempId);
        if(wxskt == null){
            LOGGER.error("会话密匙数据不存在，会话密匙临时表id：" + tempId);
            resultMap.put("msg", "未查询到用户SessionKey");
            return resultMap;
        }
        if(StringUtils.isBlank(wxskt.getSessionKey())){
            LOGGER.error("会话密匙为空");
            resultMap.put("msg", "未查询到用户SessionKey");
            return resultMap;
        }

        String encryptedDataResult = WxUtil.decryptData(encryptedData, wxskt.getSessionKey(), iv);

        if(StringUtils.isBlank(encryptedDataResult)){
            resultMap.put("msg", "解析失败");
            return resultMap;
        }

        LOGGER.info(encryptedDataResult);
        JSONObject jsonObj = JSONObject.parseObject(encryptedDataResult);

        String unionId = jsonObj.getString("unionId");
        WxUser wu = wxUserService.queryWxUserByUnionId(unionId);
        if(wu == null){// 新建用户
            wu = new WxUser();
            wu.setOpenIdXcx(jsonObj.getString("openId"));
            wu.setSessionKeyXcx(wxskt.getSessionKey());
            wu.setUnionId(unionId);
            wu.setNickName(jsonObj.getString("nickName"));
            wu.setSex(jsonObj.getInteger("gender"));
            wu.setProvince(jsonObj.getString("province"));
            wu.setCity(jsonObj.getString("city"));
            wu.setCountry(jsonObj.getString("country"));
            wu.setHeadImgUrl(jsonObj.getString("avatarUrl"));
            wu.setRemainingMoney(0D);
            wu.setWithdrawCashSum(0D);
            wu.setReturnMoneyShareWq(0);
            wu.setReturnMoneyShareYq(0);
            wu.setReturnMoneyShareTc(0);
            wu.setCreateTime(new Date());
            wu.setUpdateTime(new Date());

            wxUserService.saveWxUser(wu);
        }else{// 更新用户
            wu.setOpenIdXcx(jsonObj.getString("openId"));
            wu.setSessionKeyXcx(wxskt.getSessionKey());
            wu.setNickName(jsonObj.getString("nickName"));
            wu.setSex(jsonObj.getInteger("gender"));
            wu.setProvince(jsonObj.getString("province"));
            wu.setCity(jsonObj.getString("city"));
            wu.setCountry(jsonObj.getString("country"));
            wu.setHeadImgUrl(jsonObj.getString("avatarUrl"));
            wu.setUpdateTime(new Date());

            wxUserService.updateWxUser(wu);
        }
        if(wu == null){
            resultMap.put("msg", "未获解析出用户信息");
            return resultMap;
        }

        resultMap.put("status", true);
        resultMap.put("loginKey", wu.getId());
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 同步微信用户公共信息
     *
     * @param wxUser 微信用户信息
     * @return
     */
    @RequestMapping("/syncWxUserCommonInfo")
    @ResponseBody
    public Map<String, Object> syncWxUserCommonInfo(@RequestBody WxUser wxUser){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        // 查询用户信息
        WxUser oldWxUser = wxUserService.queryWxUserById(wxUser.getId());
        if(oldWxUser != null){
            oldWxUser.setNickName(wxUser.getNickName());
            oldWxUser.setSex(wxUser.getSex());
            oldWxUser.setProvince(wxUser.getProvince());
            oldWxUser.setCity(wxUser.getCity());
            oldWxUser.setCountry(wxUser.getCountry());
            oldWxUser.setHeadImgUrl(wxUser.getHeadImgUrl());
            oldWxUser.setUpdateTime(new Date());
            wxUserService.updateWxUser(oldWxUser);
        }
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }


    /**
     * 通过临时凭证Code获取微信公众号用户信息
     *
     * @param request
     * @return
     */
    @RequestMapping("/getWxGzhUserInfoByCode")
    public String getWxGzhUserInfoByCode(HttpServletRequest request){

        String code = request.getParameter("code");

        Map<String, String> accessTokenOpenIdMap = getAccessTokenAndOpenIdByCode(code);

        String resultPageUrl = "redirect:/wxGzhMessageSubscribe/openMessageSubscribeWarnPage";

        if(accessTokenOpenIdMap != null){
            String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_get_user_info_request_url");
            if(StringUtils.isBlank(url)){
                return resultPageUrl;
            }
            url = url.replaceAll("ACCESS_TOKEN", accessTokenOpenIdMap.get("accessToken"));
            url = url.replaceAll("OPENID", accessTokenOpenIdMap.get("openId"));

            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = null;
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    entity = response.getEntity();
                    String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                    LOGGER.info("通过临时凭证Code获取微信公众号用户信息结果--> " + respContent);
                    JSONObject jsonObject = JSONObject.parseObject(respContent);

                    String errCode = jsonObject.getString("errcode");

                    if(StringUtils.isBlank(errCode)){
                        String unionId = jsonObject.getString("unionid");
                        WxUser wu = null;
                        if(!StringUtils.isBlank(unionId)){
                            // 查询库里用户数据
                            wu = wxUserService.queryWxUserByUnionId(unionId);
                            if(wu == null){
                                wu = new WxUser();
                                wu.setUnionId(unionId);
                                wu.setOpenIdGzh(jsonObject.getString("openid"));
                                wu.setNickName(jsonObject.getString("nickname"));
                                wu.setSex(Integer.valueOf(jsonObject.getString("sex")));
                                wu.setProvince(jsonObject.getString("province"));
                                wu.setCity(jsonObject.getString("city"));
                                wu.setHeadImgUrl(jsonObject.getString("headimgurl"));
                                wu.setRemainingMoney(0D);
                                wu.setWithdrawCashSum(0D);
                                wu.setReturnMoneyShareWq(0);
                                wu.setReturnMoneyShareYq(0);
                                wu.setReturnMoneyShareTc(0);
                                wu.setCreateTime(new Date());
                                wu.setUpdateTime(new Date());
                                wxUserService.saveWxUser(wu);
                            }else{
                                wu.setOpenIdGzh(jsonObject.getString("openid"));
                                wu.setNickName(jsonObject.getString("nickname"));
                                wu.setSex(Integer.valueOf(jsonObject.getString("sex")));
                                wu.setProvince(jsonObject.getString("province"));
                                wu.setCity(jsonObject.getString("city"));
                                wu.setHeadImgUrl(jsonObject.getString("headimgurl"));
                                wu.setUpdateTime(new Date());
                                wxUserService.updateWxUser(wu);
                            }
                        }
                        if(wu != null){
                            resultPageUrl = "redirect:/wxGzhMessageSubscribe/openMessageSubscribeSuccessPage";
                            return resultPageUrl;
                        }
                    }
                }
                if(entity != null){
                    EntityUtils.consume(entity);
                }
            } catch (IOException e) {
                LOGGER.error("通过临时凭证Code获取微信公众号用户信息异常--> ", e);
                return resultPageUrl;
            } finally {
                try {
                    if(response != null){
                        response.close();
                    }
                    if(httpClient != null){
                        httpClient.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("关闭通过临时凭证Code获取微信公众号用户信息异常--> ", e);
                }
                return resultPageUrl;
            }
        }else{
            return resultPageUrl;
        }
    }

    /**
     * 根据临时凭证Code获取微信公众号access_token和openid
     *
     * @param code 临时凭证
     * @return
     */
    private Map<String, String> getAccessTokenAndOpenIdByCode(String code){

        Map<String, String> resultMap = null;

        if(StringUtils.isBlank(code)){
            return resultMap;
        }

        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_get_access_token_open_id_request_url");
        if(StringUtils.isBlank(url)){
            return resultMap;
        }
        url = url.replaceAll("APPID", configurationPropertiesSqm.getWX_GZH_APP_ID());
        url = url.replaceAll("SECRET", configurationPropertiesSqm.getWX_GZH_APP_SECRET());
        url = url.replaceAll("CODE", code);

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpGet httpGet = new HttpGet(url);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("根据临时凭证Code获取微信公众号access_token和openid结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);

                String accessToken = jsonObject.getString("access_token");
                String openId =jsonObject.getString("openid");

                if(!StringUtils.isBlank(accessToken) && !StringUtils.isBlank(openId)){
                    resultMap = new HashMap<>();
                    resultMap.put("accessToken", accessToken);
                    resultMap.put("openId", openId);
                }
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("根据临时凭证Code获取微信公众号access_token和openid异常--> ", e);
            return resultMap;
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭根据临时凭证Code获取微信公众号access_token和openid异常--> ", e);
            }
            return resultMap;
        }
    }

}
