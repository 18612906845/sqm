package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 提现记录
 */
public class WithdrawCashRecord {
    /**
     * 标识
     */
    private String id;

    /**
     * 微信用户标识
     */
    private Long wxUserId;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 申请金额
     */
    private Double applyMoney;

    /**
     * 支付状态，1：待支付；2：已支付；3：支付失败
     */
    private Integer payStatus;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 商户订单号
     */
    private String partnerTradeNo;

    /**
     * 微信付款单号
     */
    private String paymentNo;

    /**
     * 错误代码
     */
    private String errCode;

    /**
     * 错误代码描述
     */
    private String errCodeDes;

    // TODO 以下是Pojo字段

    /**
     * 支付状态，1：待支付；2：已支付；3：支付失败
     */
    private String payStatusStr;

    /**
     * 申请时间（字符串）
     */
    private String applyTimeStr;

    /**
     * 支付时间（字符串）
     */
    private String payTimeStr;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像URL
     */
    private String headImgUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public Double getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(Double applyMoney) {
        this.applyMoney = applyMoney;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPartnerTradeNo() {
        return partnerTradeNo;
    }

    public void setPartnerTradeNo(String partnerTradeNo) {
        this.partnerTradeNo = partnerTradeNo == null ? null : partnerTradeNo.trim();
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo == null ? null : paymentNo.trim();
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode == null ? null : errCode.trim();
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes == null ? null : errCodeDes.trim();
    }

    public String getPayStatusStr() {
        return payStatusStr;
    }

    public void setPayStatusStr(String payStatusStr) {
        this.payStatusStr = payStatusStr;
    }

    public String getApplyTimeStr() {
        return applyTimeStr;
    }

    public void setApplyTimeStr(String applyTimeStr) {
        this.applyTimeStr = applyTimeStr;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    @Override
    public String toString() {
        return "WithdrawCashRecord{" +
                "id='" + id + '\'' +
                ", wxUserId=" + wxUserId +
                ", applyTime=" + applyTime +
                ", applyMoney=" + applyMoney +
                ", payStatus=" + payStatus +
                ", payTime=" + payTime +
                ", partnerTradeNo='" + partnerTradeNo + '\'' +
                ", paymentNo='" + paymentNo + '\'' +
                ", errCode='" + errCode + '\'' +
                ", errCodeDes='" + errCodeDes + '\'' +
                ", payStatusStr='" + payStatusStr + '\'' +
                ", applyTimeStr='" + applyTimeStr + '\'' +
                ", payTimeStr='" + payTimeStr + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                '}';
    }
}