package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.IOrderRecordService;
import cn.com.jgyhw.sqm.service.IWxUserService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/4/13 0013 15:09
 *
 * 用户中心Controller
 */
@RequestMapping("/userCentre")
@Controller
public class UserCentreController extends CommonUtil {

    private static Logger LOGGER = LogManager.getLogger(UserCentreController.class);

    @Autowired
    private IOrderRecordService orderRecordService;

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 查询最新公告
     *
     * @return
     */
    @RequestMapping("/findNewOpenNotice")
    @ResponseBody
    public Map<String, Object> findNewOpenNotice(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        String notice = ApplicationParamConstant.XT_PARAM_MAP.get("notice");
        resultMap.put("notice", notice);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询等待付款订单总数
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findAwaitPayOrderSum")
    @ResponseBody
    public Map<String, Object> findAwaitPayOrderSum(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        int orderSum = orderRecordService.queryOrderRecordSumByWxUserIdAndStatus(loginKey, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk")));
        resultMap.put("awaitPayOrderSum", orderSum);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询等待入账订单总数
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findAwaitRzOrderSum")
    @ResponseBody
    public Map<String, Object> findAwaitRzOrderSum(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        int orderSum = orderRecordService.queryOrderRecordSumByWxUserIdAndStatus(loginKey, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
        resultMap.put("awaitRzOrderSum", orderSum);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询我的邀请总数
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findMyInviteSum")
    @ResponseBody
    public Map<String, Object> findMyInviteSum(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        int userSum = wxUserService.queryWxUserSumByParentWxUserId(loginKey);
        resultMap.put("myInviteSum", userSum);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询我的返现信息
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findMyReturnMoney")
    @ResponseBody
    public Map<String, Object> findMyReturnMoney(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        // 查询预估返现
        double estimate = orderRecordService.queryEstimateReturnMoneySumByWxUserId(loginKey);
        resultMap.put("estimate", this.formatDouble(estimate));
        // 查询待解冻
        double unfreeze = orderRecordService.queryReturnMoneySumByWxUserIdAndStatus(loginKey, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
        resultMap.put("unfreeze", this.formatDouble(unfreeze));
        // 查询可取现余额
        WxUser user = wxUserService.queryWxUserById(loginKey);
        resultMap.put("remainingMoney", 0.0);
        if(user != null){
            resultMap.put("remainingMoney", this.formatDouble(user.getRemainingMoney()));
        }
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询使用手册页面数据
     *
     * @return
     */
    @RequestMapping("/findEnchiridionPageData")
    @ResponseBody
    public Map<String, Object> findEnchiridionPageData(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        int imageSize = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("enchiridion_image_size"));
        List<String> imageUrlList = new ArrayList<>();
        for(int i=0; i<imageSize; i++){
            String url = ApplicationParamConstant.XT_PARAM_MAP.get("enchiridion_image_url_prefix") + "/" + i + ".jpg";
            imageUrlList.add(url);
        }
        resultMap.put("list", imageUrlList);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
