package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.IReturnMoneyChangeRecordService;
import cn.com.jgyhw.sqm.service.IWxGzhMessageSendService;
import cn.com.jgyhw.sqm.service.IWxUserService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by WangLei on 2019/4/14 0014 09:41
 *
 * 微信用户Controller
 */
@RequestMapping("/wxUser")
@Controller
public class WxUserController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(WxUserController.class);

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    /**
     * 根据登陆标识查询我的邀请列表
     *
     * @param loginKey 登陆标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findMyInviteWxUserListByLoginKeyPage")
    @ResponseBody
    public Map<String, Object> findMyInviteWxUserListByLoginKeyPage(Long loginKey, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", false);
        resultMap.put("msg", "未知错误");

        Page<WxUser> wuPage = wxUserService.queryWxUserListByParentWxUserIdPage(loginKey, pageNo, pageSize);

        List<WxUser> wuList = wuPage.getResult();

        long count = pageNo * pageSize;
        if(count >= wuPage.getTotal()){
            resultMap.put("isMore", false);
        }else{
            resultMap.put("isMore", true);
        }

        if(wuList == null){
            resultMap.put("wxUserList", new ArrayList<>());
            resultMap.put("status", true);
            resultMap.put("msg", "");
            return resultMap;
        }
        for(WxUser wu : wuList){
            this.wxUserToWxUserPojo(wu);
        }
        resultMap.put("wxUserList", wuList);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 根据登陆标识查询可提现余额
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findRemainingMoneyByLoginKey")
    @ResponseBody
    public Map<String, Object> findRemainingMoneyByLoginKey(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        WxUser wu = wxUserService.queryWxUserById(loginKey);
        if(wu == null){
            resultMap.put("remainingMoney", 0.0);
            resultMap.put("status", true);
            resultMap.put("msg", "");
            return resultMap;
        }
        resultMap.put("remainingMoney", wu.getRemainingMoney());
        resultMap.put("withdrawCashSum", wu.getWithdrawCashSum());
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 根据登陆标识查询用户并绑定推荐人标识
     *
     * @param loginKey 登陆标识
     * @param parentWxUserId 推荐人标识
     * @return
     */
    @RequestMapping("/bindingParentWxUserId")
    @ResponseBody
    public Map<String, Object> bindingParentWxUserId(Long loginKey, Long parentWxUserId){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        if(loginKey == parentWxUserId){
            resultMap.put("msg", "推荐人和被推荐人相同");
            return resultMap;
        }

        WxUser wu = wxUserService.queryWxUserById(loginKey);
        if(wu == null){
            resultMap.put("status", false);
            resultMap.put("msg", "未查询到用户");
            return resultMap;
        }
        if(wu.getParentWxUserId() == null){
            wu.setParentWxUserId(parentWxUserId);
            wu.setUpdateTime(new Date());
            wxUserService.updateWxUser(wu);
            // 为推荐者存入推荐返现
            WxUser pwu = wxUserService.queryWxUserById(parentWxUserId);
            if(pwu != null){
                Double userRecommendPrize = Double.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("user_recommend_prize"));
                // 记录返现变更记录
                ReturnMoneyChangeRecord rmcr = new ReturnMoneyChangeRecord();
                rmcr.setId(UUID.randomUUID().toString());
                rmcr.setWxUserId(parentWxUserId);
                rmcr.setChangeType(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_yx")));
                rmcr.setTargetId(String.valueOf(loginKey));
                rmcr.setChangeTime(new Date());
                rmcr.setChangeMoney(userRecommendPrize);
                returnMoneyChangeRecordService.saveReturnMoneyChangeRecord(rmcr);
                // 发送获得返现消息，判断用户是否关注公众号
                if(!StringUtils.isBlank(pwu.getOpenIdGzh())){
                    this.sendRebateWxMessage(wxGzhMessageSendService, pwu.getOpenIdGzh(), "您邀请的 " + wu.getNickName() + " 完成绑定，邀新奖励已入账", rmcr.getChangeMoney());
                }
            }
            resultMap.put("status", true);
            resultMap.put("msg", "恭喜您已成功被邀请");
        }else{
            resultMap.put("status", false);
            resultMap.put("msg", "您已经绑定过其他推荐人");
            return resultMap;
        }
        return resultMap;
    }
}
