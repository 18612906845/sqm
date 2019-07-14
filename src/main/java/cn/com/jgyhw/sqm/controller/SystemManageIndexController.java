package cn.com.jgyhw.sqm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 系统管理首页
 */
@RequestMapping("/smiXtglAuth")
@Controller
public class SystemManageIndexController {

    /**
     * 打开系统管理首页
     * @return
     */
    @RequestMapping("/openIndexPage")
    public String openIndexPage(){
        return "xtgl/index";
    }

}