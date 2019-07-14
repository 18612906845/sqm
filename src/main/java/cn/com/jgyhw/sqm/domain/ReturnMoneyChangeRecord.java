package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 返现变更记录
 */
public class ReturnMoneyChangeRecord {
    /**
     * 标识
     */
    private String id;

    /**
     * 微信用户标识
     */
    private Long wxUserId;

    /**
     * 变更类型，1：入账；2：提现；3：提成；4：邀新
     */
    private Integer changeType;

    /**
     * 变更时间
     */
    private Date changeTime;

    /**
     * 变更金额
     */
    private Double changeMoney;

    /**
     * 关联主体ID，订单号、提现记录ID、（提成）邀请用户标识
     */
    private String targetId;

    /**
     * 订单编号
     */
    private String orderId;

    // TODO 以下是Pojo字段

    /**
     * 变更类型，1：入账；2：提现；3：提成；4：邀新
     */
    private String changeTypeStr;

    /**
     * 变更时间
     */
    private String changeTimeStr;

    /**
     * 支付时间
     */
    private String payTimeStr;

    /**
     * 主体图片名称
     */
    private String targetImageName;

    /**
     * 主体名称
     */
    private String targetName;

    /**
     * 支付状态，1：待支付；2：已支付；3：支付失败
     */
    private Integer payStatus;

    /**
     * 错误代码描述
     */
    private String errCodeDes;


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

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Double getChangeMoney() {
        return changeMoney;
    }

    public void setChangeMoney(Double changeMoney) {
        this.changeMoney = changeMoney;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId == null ? null : targetId.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public String getChangeTypeStr() {
        return changeTypeStr;
    }

    public void setChangeTypeStr(String changeTypeStr) {
        this.changeTypeStr = changeTypeStr;
    }

    public String getChangeTimeStr() {
        return changeTimeStr;
    }

    public void setChangeTimeStr(String changeTimeStr) {
        this.changeTimeStr = changeTimeStr;
    }

    public String getTargetImageName() {
        return targetImageName;
    }

    public void setTargetImageName(String targetImageName) {
        this.targetImageName = targetImageName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getPayTimeStr() {
        return payTimeStr;
    }

    public void setPayTimeStr(String payTimeStr) {
        this.payTimeStr = payTimeStr;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }


    @Override
    public String toString() {
        return "ReturnMoneyChangeRecord{" +
                "id='" + id + '\'' +
                ", wxUserId='" + wxUserId + '\'' +
                ", changeType=" + changeType +
                ", changeTime=" + changeTime +
                ", changeMoney=" + changeMoney +
                ", targetId='" + targetId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", changeTypeStr='" + changeTypeStr + '\'' +
                ", changeTimeStr='" + changeTimeStr + '\'' +
                ", targetImageName='" + targetImageName + '\'' +
                ", targetName='" + targetName + '\'' +
                '}';
    }
}