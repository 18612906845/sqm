package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.IWxUserService;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.github.pagehelper.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/5/16 0016 13:22
 *
 * 微信用户管理
 */
@RequestMapping("/xwuXtglAuth")
@Controller
public class XtglWxUserController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglWxUserController.class);

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 打开微信令牌管理页面
     * @return
     */
    @RequestMapping("/openWxUserListPage")
    public String openWxUserListPage(){
        return "xtgl/wxUserList";
    }

    /**
     * 根据条件查询微信用户信息（分页）
     *
     * @param id 微信用户标识
     * @param nickName 微信用户昵称
     * @param orderField 排序字段
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findWxUserListByConditionPage")
    @ResponseBody
    public Map<String, Object> findWxUserListByConditionPage(Long id, String nickName, String orderField, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "服务未知错误");
        Page<WxUser> wuPage = wxUserService.queryWxUserListByConditionPage(id, nickName, orderField, pageNo, pageSize);

        List<WxUser> wuList = wuPage.getResult();

        if(wuList != null && !wuList.isEmpty()){
            for(WxUser wu : wuList){
                wu = this.wxUserToWxUserPojo(wu);
            }
        }
        resultMap.put("data", wuList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", wuPage.getTotal());
        return resultMap;
    }

    /**
     * 查询等待支付总金额
     *
     * @return
     */
    @RequestMapping("/findAwaitPaySumNum")
    @ResponseBody
    public Map<String, Object> findAwaitPaySumNum(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        Double awaitPaySum = wxUserService.queryRemainingMoneySum();
        resultMap.put("awaitPaySum", this.formatDouble(awaitPaySum));
        resultMap.put("status", true);
        return resultMap;
    }


    /**
     * 根据微信用户标识修改微信用户信息
     *
     * @param id 微信用户标识
     * @param field 修改的字段名
     * @param value 修改的字段值
     * @return
     */
    @RequestMapping("/updateWxUserById")
    @ResponseBody
    public Map<String, Object> updateWxUserById(Long id, String field, String value){
        LOGGER.info("根据微信用户标识修改微信用户信息，参数ID：" + id + "；字段名：" + field + "；字段值：" + value);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        WxUser wu = wxUserService.queryWxUserById(id);
        if(wu == null){
            return resultMap;
        }
        try {
            Field f = wu.getClass().getDeclaredField(field);
            f.setAccessible(true);
            if("returnMoneyShareWq".equals(field) || "returnMoneyShareYq".equals(field) || "returnMoneyShareTc".equals(field)){
                f.set(wu, Integer.valueOf(value));
            }else{
                f.set(wu, value);
            }
            wxUserService.updateWxUser(wu);
            resultMap.put("status", true);
        } catch (Exception e) {
            LOGGER.error("Java反射动态赋值异常", e);
        }
        return resultMap;
    }
}
