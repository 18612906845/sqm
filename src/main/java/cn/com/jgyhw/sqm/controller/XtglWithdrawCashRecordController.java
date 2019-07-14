package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.service.IWithdrawCashRecordService;
import cn.com.jgyhw.sqm.service.IWxPayService;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/5/16 0016 17:31
 *
 * 提现记录Controller
 */
@RequestMapping("/xwcrXtglAuth")
@Controller
public class XtglWithdrawCashRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglWithdrawCashRecordController.class);

    @Autowired
    private IWithdrawCashRecordService withdrawCashRecordService;

    @Autowired
    private IWxPayService wxPayService;

    @Autowired
    private IWxUserService wxUserService;

    /**
     * 打开提现记录页面
     * @return
     */
    @RequestMapping("/openWithdrawCashRecordListPage")
    public String openWithdrawCashRecordListPage(){
        return "xtgl/withdrawCashRecordList";
    }


    /**
     * 查询提现申请记录
     *
     * @param wxUserId 申请用户标识
     * @param status 支付状态
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findWithdrawCashRecordListByConditionPage")
    @ResponseBody
    public Map<String, Object> findWithdrawCashRecordListByConditionPage(Long wxUserId, Integer status, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "服务未知错误");
        Page<WithdrawCashRecord> wcrPage = withdrawCashRecordService.queryWithdrawCashRecordAllByConditionPage(wxUserId, status, pageNo, pageSize);

        List<WithdrawCashRecord> wcrList = wcrPage.getResult();

        if(wcrList != null && !wcrList.isEmpty()){
            for(WithdrawCashRecord wcr : wcrList){
                wcr = this.withdrawCashRecordToWithdrawCashRecordPojo(wcr);
            }
        }
        resultMap.put("data", wcrList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", wcrPage.getTotal());
        return resultMap;
    }

    /**
     * 支付失败重试
     *
     * @param id 提现申请记录标识
     * @return
     */
    @RequestMapping("/retryPay")
    @ResponseBody
    public Map<String, Object> retryPay(String id){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        WithdrawCashRecord wcr = withdrawCashRecordService.queryWithdrawCashRecordById(id);
        if(wcr == null){
            return resultMap;
        }

        String partnerTradeNo = this.getShopOrderString();
        //发起企业付款
        Double amountDouble = wcr.getApplyMoney() * 100 ;
        int amount = amountDouble.intValue();

        WxUser wu = wxUserService.queryWxUserById(wcr.getWxUserId());

        Map<String, String> payResultMap = wxPayService.enterprisePayToWxWallet(partnerTradeNo, wu.getOpenIdXcx(), amount, "京东购物返利提现");

        if(payResultMap == null){//解析XML错误
            if(wcr != null){
                wcr.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_zfsb")));
                wcr.setErrCodeDes("解析XML错误");
                withdrawCashRecordService.updateWithdrawCashRecord(wcr);
            }
        }else{
            if("SUCCESS".equals(payResultMap.get("result_code"))){//支付成功
                if(wcr != null){
                    wcr.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_yzf")));
                    wcr.setPayTime(new Date());
                    wcr.setPaymentNo(payResultMap.get("payment_no"));
                    withdrawCashRecordService.updateWithdrawCashRecord(wcr);
                }
            }else{//支付失败
                if(wcr != null){
                    wcr.setPayStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_zfsb")));
                    wcr.setErrCode(payResultMap.get("err_code"));
                    wcr.setErrCodeDes(payResultMap.get("err_code_des"));
                    withdrawCashRecordService.updateWithdrawCashRecord(wcr);
                }
            }
        }

        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 查询累计支付提现金额
     *
     * @return
     */
    @RequestMapping("/findWithdrawCashSum")
    @ResponseBody
    public Map<String, Object> findWithdrawCashSum(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        Double withdrawCashSum = wxUserService.queryWithdrawCashSum();
        resultMap.put("withdrawCashSum", this.formatDouble(withdrawCashSum));
        resultMap.put("status", true);
        return resultMap;
    }
}
