package cn.com.jgyhw.sqm.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by WangLei on 2019/5/1 0001 22:09
 *
 * 微信公众号消息订阅Controller
 */
@RequestMapping("/wxGzhMessageSubscribe")
@Controller
public class WxGzhMessageSubscribeController {

    private static Logger LOGGER = LogManager.getLogger(WxGzhMessageSubscribeController.class);

    /**
     * 打开微信公众号消息订阅页面
     * @return
     */
    @RequestMapping("/openMessageSubscribeSuccessPage")
    public String openMessageSubscribeSuccessPage(){
        return "gzh/messageSubscribe/success";
    }

    /**
     * 打开微信公众号消息订阅页面
     * @return
     */
    @RequestMapping("/openMessageSubscribeWarnPage")
    public String openMessageSubscribeWarnPage(){
        return "gzh/messageSubscribe/warn";
    }
}
