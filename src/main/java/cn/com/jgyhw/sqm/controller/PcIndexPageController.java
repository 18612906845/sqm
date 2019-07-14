package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangLei on 2018/12/28 0028 20:47
 */
@Controller
public class PcIndexPageController {

    @RequestMapping("/")
    public String openIndexToPcPage(){
        return "pc/index" ;
    }

    /**
     * 查询系统公告
     *
     * @return
     */
    @RequestMapping("/findSystemNotice")
    @ResponseBody
    public String findSystemNotice(){
        return ApplicationParamConstant.XT_PARAM_MAP.get("notice");
    }

}
