package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WxGzhMenu;
import cn.com.jgyhw.sqm.pojo.ZtreePojo;
import cn.com.jgyhw.sqm.service.IWxGzhMenuService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 微信公众号菜单管理controller
 */
@RequestMapping("/xwgmtXtglAuth")
@Controller
public class XtglWxGzhMenuController {

    private static Logger LOGGER = LogManager.getLogger(XtglWxGzhMenuController.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private IWxGzhMenuService wxGzhMenuService;

    /**
     * 打开微信自定义菜单管理页面
     * @return
     */
    @RequestMapping("/openWxGzhMenuManagePage")
    public String openWxGzhMenuManagePage(){
        return "xtgl/wxGzhMenuManage";
    }

    /**
     * 删除自定义菜单
     *
     * @param id 自定义菜单对象id
     * @return
     */
    @RequestMapping("/deleteWebManageWxMenuById")
    @ResponseBody
    public Map<String, Object> deleteWebManageWxMenuById(String id){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "Server未知错误。");
        List<WxGzhMenu> wgmList = this.wxGzhMenuService.querySecondLevelWxGzhMenuListByParentId(id);
        if(wgmList == null || wgmList.isEmpty()){
            this.wxGzhMenuService.deleteWxGzhMenuById(id);
            resultMap.put("status", true);
            resultMap.put("msg", "删除成功。");
        }else{
            resultMap.put("status", false);
            resultMap.put("msg", "请先删除该菜单下二级子菜单。");
        }
        return resultMap;
    }

    /**
     * 修改自定义菜单
     *
     * @param webManageWxMenu 自定义菜单对象
     * @return
     */
    @RequestMapping("/updateWebManageWxMenuById")
    @ResponseBody
    public Map<String, Object> updateWebManageWxMenuById(@RequestBody WxGzhMenu webManageWxMenu){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        if(webManageWxMenu.getParentId() != null){
            webManageWxMenu.setParentId(webManageWxMenu.getParentId());
        }
        this.wxGzhMenuService.updateWxGzhMenu(webManageWxMenu);
        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 新增自定义菜单
     *
     * @param webManageWxMenu 自定义菜单对象
     * @return
     */
    @RequestMapping("/saveWebManageWxMenu")
    @ResponseBody
    public Map<String, Object> saveWebManageWxMenu(@RequestBody WxGzhMenu webManageWxMenu){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        webManageWxMenu.setId(UUID.randomUUID().toString());
        if(webManageWxMenu.getParentId() != null){
            webManageWxMenu.setParentId(webManageWxMenu.getParentId());
        }
        this.wxGzhMenuService.saveWxGzhMenu(webManageWxMenu);
        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 查询所有微信自定义菜单
     * @return
     */
    @RequestMapping("/findAllWebManageWxMenu")
    @ResponseBody
    public Map<String, Object> findAllWebManageWxMenu(){
        Map<String, Object> resultMap = new HashMap<>();
        List<ZtreePojo> zpList = new ArrayList<>();
        resultMap.put("status", false);
        resultMap.put("zNodes", zpList);
        resultMap.put("firstSize", 0);
        //查询出所有的一级菜单
        List<WxGzhMenu> systemWxDiyMenuListYj = this.wxGzhMenuService.queryFirstLevelWxGzhMenuList();
        if(systemWxDiyMenuListYj == null || systemWxDiyMenuListYj.isEmpty()){
            resultMap.put("status", true);
            return resultMap;
        }
        for(WxGzhMenu wgmYj : systemWxDiyMenuListYj){
            ZtreePojo zpYj = new ZtreePojo();
            zpYj.setId(String.valueOf(wgmYj.getId()));
            zpYj.setpId(null);
            zpYj.setName(wgmYj.getMenuName());
            Map<String, Object> diyMapYj = new HashMap<>();
            diyMapYj.put("diyMenu", wgmYj);
            zpYj.setDiy(diyMapYj);
            //查询一级菜单下是否还有二级菜单
            List<WxGzhMenu> systemWxDiyMenuListEj = this.wxGzhMenuService.querySecondLevelWxGzhMenuListByParentId(wgmYj.getId());
            if(systemWxDiyMenuListEj == null || systemWxDiyMenuListEj.isEmpty()){
                zpYj.setParent(false);
                zpYj.setOpen(false);
            }else{
                zpYj.setParent(true);
                zpYj.setOpen(true);
                zpYj.setSubMenuNum(systemWxDiyMenuListEj.size());
                for(WxGzhMenu wgmEj : systemWxDiyMenuListEj){
                    ZtreePojo zpEj = new ZtreePojo();
                    zpEj.setId(String.valueOf(wgmEj.getId()));
                    zpEj.setName(wgmEj.getMenuName());
                    zpEj.setpId(String.valueOf(wgmYj.getId()));
                    zpEj.setpName(wgmYj.getMenuName());
                    zpEj.setParent(false);
                    zpEj.setOpen(false);
                    Map<String, Object> mapEj = new HashMap<>();
                    mapEj.put("diyMenu", wgmEj);
                    zpEj.setDiy(mapEj);
                    zpList.add(zpEj);
                }
            }
            zpList.add(zpYj);
        }
        resultMap.put("status", true);
        resultMap.put("firstSize", systemWxDiyMenuListYj.size());
        return resultMap;
    }

    /**
     * 设置微信自定义菜单
     * @return
     */
    @RequestMapping("/setWebManageWxMenu")
    @ResponseBody
    public Map<String, Object> setWebManageWxMenu(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);

        String requestDataJsonStr = this.getCreateMenuRequestDataJsonStr();
        if(StringUtils.isBlank(requestDataJsonStr)){
            return resultMap;
        }
        LOGGER.info("设置微信自定义菜单--> 开始：" + requestDataJsonStr);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_create_diy_menu_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_GZH_SERVER_API_TOKEN());
        }
        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        try {
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(requestDataJsonStr, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("请求微信设置自定义菜单结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);
                resultMap = jsonObject.getInnerMap();
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("请求微信设置自定义菜单异常--> ", e);
            return null;
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求微信设置自定义菜单异常--> ", e);
            }
            resultMap.put("status", true);
            return resultMap;
        }
    }

    /**
     * 删除微信自定义菜单
     * @return
     */
    @RequestMapping("/deleteWebManageWxMenu")
    @ResponseBody
    public Map<String, Object> deleteWebManageWxMenu(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_delete_diy_menu_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_GZH_SERVER_API_TOKEN());
        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("请求微信删除自定义菜单结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);
                resultMap = jsonObject.getInnerMap();
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("请求微信删除自定义菜单异常--> ", e);
            return null;
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求微信删除自定义菜单异常--> ", e);
            }
            resultMap.put("status", true);
            return resultMap;
        }
    }

    /**
     * 获取自定义菜单请求参数Json字符串
     *
     * @return
     */
    private String getCreateMenuRequestDataJsonStr(){
        //查询出所有的一级菜单
        List<WxGzhMenu> systemWxDiyMenuListYj = this.wxGzhMenuService.queryFirstLevelWxGzhMenuList();
        if(systemWxDiyMenuListYj == null || systemWxDiyMenuListYj.isEmpty()){
            return null;
        }
        Map<String, Object> dataMap = new HashMap<>();
        List<Map<String, Object>> buttonList = new ArrayList<>();
        dataMap.put("button", buttonList);

        for(WxGzhMenu wgmYj : systemWxDiyMenuListYj){
            Map<String, Object> mapYj = new HashMap<>();
            mapYj.put("name", wgmYj.getMenuName());
            //查询一级菜单下是否还有二级菜单
            List<WxGzhMenu> systemWxDiyMenuListEj = this.wxGzhMenuService.querySecondLevelWxGzhMenuListByParentId(wgmYj.getId());
            if(systemWxDiyMenuListEj == null || systemWxDiyMenuListEj.isEmpty()){
                if(!StringUtils.isBlank(wgmYj.getActionType())){
                    mapYj.put("type", wgmYj.getActionType());
                }
                if(!StringUtils.isBlank(wgmYj.getMenuKey())){
                    mapYj.put("key", wgmYj.getMenuKey());
                }
                if(!StringUtils.isBlank(wgmYj.getUrl())){
                    mapYj.put("url", wgmYj.getUrl());
                }
                if(!StringUtils.isBlank(wgmYj.getMediaId())){
                    mapYj.put("media_id", wgmYj.getMediaId());
                }
                if(!StringUtils.isBlank(wgmYj.getAppId())){
                    mapYj.put("appid", wgmYj.getAppId());
                }
                if(!StringUtils.isBlank(wgmYj.getPagePath())){
                    mapYj.put("pagepath", wgmYj.getPagePath());
                }
            }else{
                List<Map<String, String>> ejMenuList = new ArrayList<>();
                for(WxGzhMenu wgmEj : systemWxDiyMenuListEj){
                    Map<String, String> mapEj = new HashMap<>();
                    mapEj.put("name", wgmEj.getMenuName());
                    if(!StringUtils.isBlank(wgmEj.getActionType())){
                        mapEj.put("type", wgmEj.getActionType());
                    }
                    if(!StringUtils.isBlank(wgmEj.getMenuKey())){
                        mapEj.put("key", wgmEj.getMenuKey());
                    }
                    if(!StringUtils.isBlank(wgmEj.getUrl())){
                        mapEj.put("url", wgmEj.getUrl());
                    }
                    if(!StringUtils.isBlank(wgmEj.getMediaId())){
                        mapEj.put("media_id", wgmEj.getMediaId());
                    }
                    if(!StringUtils.isBlank(wgmEj.getAppId())){
                        mapEj.put("appid", wgmEj.getAppId());
                    }
                    if(!StringUtils.isBlank(wgmEj.getPagePath())){
                        mapEj.put("pagepath", wgmEj.getPagePath());
                    }
                    ejMenuList.add(mapEj);
                }
                mapYj.put("sub_button", ejMenuList);
            }
            buttonList.add(mapYj);
        }
        JSONObject json = new JSONObject(dataMap);
        return json.toJSONString();
    }

}
