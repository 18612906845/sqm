package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.OrderRecord;
import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.IOrderRecordService;
import cn.com.jgyhw.sqm.service.IReturnMoneyChangeRecordService;
import cn.com.jgyhw.sqm.service.IWithdrawCashRecordService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/4/14 0014 09:41
 *
 * 返现变更记录Controller
 */
@RequestMapping("/returnMoneyChangeRecord")
@Controller
public class ReturnMoneyChangeRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(ReturnMoneyChangeRecordController.class);

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IWithdrawCashRecordService withdrawCashRecordService;

    @Autowired
    private IOrderRecordService orderRecordService;

    /**
     * 根据登陆标识查询返现变更记录
     *
     * @param loginKey 登陆标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findReturnMoneyChangeRecordListByWxUserIdPage")
    @ResponseBody
    public Map<String, Object> findReturnMoneyChangeRecordListByWxUserIdPage(Long loginKey, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", false);
        resultMap.put("msg", "未知错误");

        Page<ReturnMoneyChangeRecord> rmcrPage = returnMoneyChangeRecordService.queryReturnMoneyChangeRecordListByWxUserIdAndChangTypePage(loginKey, 0, pageNo, pageSize);

        List<ReturnMoneyChangeRecord> rmcrList = rmcrPage.getResult();

        long count = pageNo * pageSize;
        if(count >= rmcrPage.getTotal()){
            resultMap.put("isMore", false);
        }else{
            resultMap.put("isMore", true);
        }

        if(rmcrList == null){
            resultMap.put("recordList", new ArrayList<>());
            resultMap.put("status", true);
            resultMap.put("msg", "");
            return resultMap;
        }
        for(ReturnMoneyChangeRecord rmcr : rmcrList){
            this.returnMoneyChangeRecordToReturnMoneyChangeRecordPojo(rmcr);
            if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tc")) == rmcr.getChangeType()){// 提成
                rmcr.setTargetName("未知用户昵称");
                WxUser wu = wxUserService.queryWxUserById(Long.valueOf(rmcr.getTargetId()));
                if(wu != null && !StringUtils.isBlank(wu.getNickName())){
                    rmcr.setTargetName(wu.getNickName());
                    rmcr.setTargetImageName(wu.getHeadImgUrl());
                }
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tx")) == rmcr.getChangeType()){// 提现
                WithdrawCashRecord wcr = withdrawCashRecordService.queryWithdrawCashRecordById(rmcr.getTargetId());
                if(wcr != null){
                    wcr = this.withdrawCashRecordToWithdrawCashRecordPojo(wcr);
                    rmcr.setTargetName(wcr.getPayStatusStr());
                    rmcr.setPayTimeStr(wcr.getPayTimeStr());
                    rmcr.setPayStatus(wcr.getPayStatus());
                    rmcr.setErrCodeDes(wcr.getErrCodeDes());
                }
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_rz")) == rmcr.getChangeType()) {// 入账
                OrderRecord or = orderRecordService.queryOrderRecordById(rmcr.getTargetId());
                if(or == null){
                    continue;
                }
                if(or.getPlatform() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd"))){// 京东订单
                    rmcr.setTargetImageName("icon_rzjl_jd.jpg");
                }else if(or.getPlatform() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd"))){// 拼多多订单
                    rmcr.setTargetImageName("icon_rzjl_pdd.jpg");
                }else if(or.getPlatform() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb"))){// 淘宝订单
                    rmcr.setTargetImageName("icon_rzjl_al.jpg");
                }
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_yx")) == rmcr.getChangeType()){// 邀新
                rmcr.setTargetName("未知用户昵称");
                WxUser wu = wxUserService.queryWxUserById(Long.valueOf(rmcr.getTargetId()));
                if(wu != null && !StringUtils.isBlank(wu.getNickName())){
                    rmcr.setTargetName(wu.getNickName());
                    rmcr.setTargetImageName(wu.getHeadImgUrl());
                }
            }
        }
        resultMap.put("recordList", rmcrList);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

}
