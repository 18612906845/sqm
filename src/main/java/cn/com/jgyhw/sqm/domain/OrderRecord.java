package cn.com.jgyhw.sqm.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单记录
 */
public class OrderRecord {
    /**
     * 标识
     */
    private String id;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 联盟佣金
     */
    private Double commission;

    /**
     * 返现金额
     */
    private Double returnMoney;

    /**
     * 订单平台：1：京东，2：拼多多，3：淘宝
     */
    private Integer platform;

    /**
     * 微信用户标识
     */
    private Long wxUserId;

    /**
     * 订单状态，1：待付款，2：已付款，3：已取消，4：已成团，5：已完成，6：已入账，7：无效
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    // TODO 以下是Pojo字段

    /**
     * 商品集合
     */
    private List<OrderGoods> orderGoodsList = new ArrayList<>();

    /**
     * 下单时间
     */
    private String orderTimeStr;

    /**
     * 完成时间
     */
    private String finishTimeStr;

    /**
     * 订单状态，1：待付款，2：已付款，3：已取消，4：已成团，5：已完成，6：已入账，7：无效
     */
    private String statusStr;

    /**
     * 是否多商品
     */
    private boolean isMultipartiteGoods;

    /**
     * 用户头像URL
     */
    private String headImgUrl;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 推荐人微信用户标识
     */
    private Long parentWxUserId;

    /**
     * 订单平台：1：京东，2：拼多多，3：淘宝
     */
    private String platformStr;

    /**
     * 创建时间（字符串）
     */
    private String createTimeStr;

    /**
     * 更新时间（字符串）
     */
    private String updateTimeStr;

    /**
     * 返现比例
     */
    private Integer returnScale;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Double returnMoney) {
        this.returnMoney = returnMoney;
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public List<OrderGoods> getOrderGoodsList() {
        return orderGoodsList;
    }

    public void setOrderGoodsList(List<OrderGoods> orderGoodsList) {
        this.orderGoodsList = orderGoodsList;
    }

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getFinishTimeStr() {
        return finishTimeStr;
    }

    public void setFinishTimeStr(String finishTimeStr) {
        this.finishTimeStr = finishTimeStr;
    }

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    public boolean isMultipartiteGoods() {
        return isMultipartiteGoods;
    }

    public void setMultipartiteGoods(boolean multipartiteGoods) {
        isMultipartiteGoods = multipartiteGoods;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getParentWxUserId() {
        return parentWxUserId;
    }

    public void setParentWxUserId(Long parentWxUserId) {
        this.parentWxUserId = parentWxUserId;
    }

    public String getPlatformStr() {
        return platformStr;
    }

    public void setPlatformStr(String platformStr) {
        this.platformStr = platformStr;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public Integer getReturnScale() {
        return returnScale;
    }

    public void setReturnScale(Integer returnScale) {
        this.returnScale = returnScale;
    }

    @Override
    public String toString() {
        return "OrderRecord{" +
                "id='" + id + '\'' +
                ", orderId='" + orderId + '\'' +
                ", orderTime=" + orderTime +
                ", finishTime=" + finishTime +
                ", commission=" + commission +
                ", returnMoney=" + returnMoney +
                ", platform=" + platform +
                ", wxUserId=" + wxUserId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderGoodsList=" + orderGoodsList +
                ", orderTimeStr='" + orderTimeStr + '\'' +
                ", finishTimeStr='" + finishTimeStr + '\'' +
                ", statusStr='" + statusStr + '\'' +
                ", isMultipartiteGoods=" + isMultipartiteGoods +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", nickName='" + nickName + '\'' +
                ", parentWxUserId=" + parentWxUserId +
                ", platformStr='" + platformStr + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                ", returnScale=" + returnScale +
                '}';
    }
}