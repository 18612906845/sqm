package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WxToken;
import cn.com.jgyhw.sqm.service.IWxTokenService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/4/14 0014 22:05
 *
 * 微信令牌Controller
 */
@RequestMapping("/xwtXtglAuth")
@Controller
public class XtglWxTokenController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglWxTokenController.class);

    @Autowired
    private IWxTokenService wxTokenService;

    /**
     * 打开微信令牌管理页面
     * @return
     */
    @RequestMapping("/openWxTokenManagePage")
    public String openWxTokenManagePage(){
        return "xtgl/wxTokenManage";
    }


    /**
     * 查询所有的微信令牌
     *
     * @return
     */
    @RequestMapping("findWxTokenAll")
    @ResponseBody
    public Map<String, Object> findWxTokenAll(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "未知错误");

        List<WxToken> wtList = wxTokenService.queryWxTokenAll();
        if(wtList == null){
            wtList = new ArrayList<>();
        }else{
            for(WxToken wt : wtList){
                this.wxTokenToWxTokenPojo(wt);
            }
        }
        resultMap.put("data", wtList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", wtList.size());
        return resultMap;
    }

    /**
     * 根据令牌类型刷新令牌
     *
     * @param tokenType 令牌类型
     * @return
     */
    @RequestMapping("/refreshWxTokenByTokenType")
    @ResponseBody
    public Map<String, Object> refreshWxTokenByTokenType(Integer tokenType){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        if(Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_gzh_server_api")) == tokenType){
            wxTokenService.timingGetWxGzhServiceApiToken();
        }else if(Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_xcx_server_api")) == tokenType){
            wxTokenService.timingGetWxXcxServiceApiToken();
        }
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

}
