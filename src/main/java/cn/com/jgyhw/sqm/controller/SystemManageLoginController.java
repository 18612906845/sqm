package cn.com.jgyhw.sqm.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 系统管理员登陆
 */
@Controller
public class SystemManageLoginController {

    private static Logger LOGGER = LogManager.getLogger(SystemManageLoginController.class);

    private String u = "18612906845";

    private String p = "f89f018b57912e5962b3277de1613756";

    /**
     * 系统管理登陆页面
     */
    @RequestMapping("/manage")
    public String manage() {
        return "xtgl/login";
    }

    /**
     * 登陆验证
     *
     * @param userName 用户名
     * @param password 密码
     * @return
     */
    @RequestMapping("/loginVerify")
    public String loginVerify(HttpSession session, Model model, @RequestParam("loginname")String userName, @RequestParam("loginpass")String password){
        if(u.equals(userName) && p.equals(password)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LOGGER.info("系统管理用户登陆，用户名：" + userName + "；登陆时间：" + sdf.format(new Date()));
            session.setAttribute("xtglUserName", userName);
            return "redirect:/smiXtglAuth/openIndexPage";
        }else{
            model.addAttribute("msg", "用户名或密码错误");
            return "xtgl/login";
        }
    }

    /**
     * 登出
     * @return
     */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        session.setAttribute("xtglUserName", "");
        return "xtgl/login";
    }
}
