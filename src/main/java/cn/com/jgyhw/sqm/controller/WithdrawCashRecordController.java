package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.*;
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
 * Created by WangLei on 2019/4/14 0014 16:19
 *
 * 提现记录Controller
 */
@RequestMapping("/withdrawCashRecord")
@Controller
public class WithdrawCashRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(WithdrawCashRecordController.class);

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IWithdrawCashRecordService withdrawCashRecordService;

    @Autowired
    private IWxPayService wxPayService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    /**
     * 根据登陆标识查询提现记录
     *
     * @param loginKey 登陆标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findWithdrawCashRecordListByWxUserIdPage")
    @ResponseBody
    public Map<String, Object> findWithdrawCashRecordListByWxUserIdPage(Long loginKey, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", false);
        resultMap.put("msg", "未知错误");

        Page<ReturnMoneyChangeRecord> rmcrPage = returnMoneyChangeRecordService.queryReturnMoneyChangeRecordListByWxUserIdAndChangTypePage(loginKey, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tx")), pageNo, pageSize);

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
            // 查询提现状态
            WithdrawCashRecord wcr = withdrawCashRecordService.queryWithdrawCashRecordById(rmcr.getTargetId());
            if(wcr != null){
                wcr = this.withdrawCashRecordToWithdrawCashRecordPojo(wcr);
                rmcr.setTargetName(wcr.getPayStatusStr());
                rmcr.setPayTimeStr(wcr.getPayTimeStr());
                rmcr.setPayStatus(wcr.getPayStatus());
                rmcr.setErrCodeDes(wcr.getErrCodeDes());
            }
        }
        resultMap.put("recordList", rmcrList);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 申请提现
     *
     * @param money 申请金额
     * @return
     */
    @RequestMapping("/applyWithdrawCash")
    @ResponseBody
    public Map<String, Object> applyWithdrawCash(Long loginKey, Double money){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        //判断取现金额是否正确
        if(money == null || money < 1){
            resultMap.put("msg", "1元起提");
            return resultMap;
        }
        //验证金额是否够
        Double remainingMoney = 0D;
        WxUser wu = wxUserService.queryWxUserById(loginKey);
        if(wu != null){
            remainingMoney = wu.getRemainingMoney();
        }
        if(money > remainingMoney){
            resultMap.put("msg", "余额不足");
            return resultMap;
        }

        WithdrawCashRecord wcr = new WithdrawCashRecord();
        wcr.setId(UUID.randomUUID().toString());
        wcr.setWxUserId(loginKey);
        wcr.setApplyTime(new Date());
        wcr.setApplyMoney(money);
        wcr.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_dzf")));
        String partnerTradeNo = this.getShopOrderString();
        wcr.setPartnerTradeNo(partnerTradeNo);

        withdrawCashRecordService.saveWithdrawCashRecord(wcr);

        //发起企业付款
        Double amountDouble = money * 100 ;
        int amount = amountDouble.intValue();

        Map<String, String> payResultMap = wxPayService.enterprisePayToWxWallet(partnerTradeNo, wu.getOpenIdXcx(), amount, "京东购物返利提现");

        WithdrawCashRecord wcrOld = withdrawCashRecordService.queryWithdrawCashRecordById(wcr.getId());
        if(payResultMap == null){//解析XML错误
            if(wcrOld != null){
                wcrOld.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_zfsb")));
                wcrOld.setErrCodeDes("解析XML错误");
                withdrawCashRecordService.updateWithdrawCashRecord(wcrOld);
            }
        }else{
            if("SUCCESS".equals(payResultMap.get("result_code"))){//支付成功
                if(wcrOld != null){
                    wcrOld.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_yzf")));
                    wcrOld.setPayTime(new Date());
                    wcrOld.setPaymentNo(payResultMap.get("payment_no"));
                    withdrawCashRecordService.updateWithdrawCashRecord(wcrOld);
                    // 判断用户是否关注公众号
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendPaySuccessWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), wcrOld.getApplyMoney(), wcrOld.getPayTime());
                    }
                }
            }else{//支付失败
                if(wcrOld != null){
                    wcrOld.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_zfsb")));
                    wcrOld.setErrCode(payResultMap.get("err_code"));
                    wcrOld.setErrCodeDes(payResultMap.get("err_code_des"));
                    withdrawCashRecordService.updateWithdrawCashRecord(wcrOld);
                }
            }
        }
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;

    }
}
