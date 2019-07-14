package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 淘宝订单绑定记录
 */
public class TbOrderBindingRecord {
    /**
     * 标识
     */
    private String id;

    /**
     * 淘宝订单编号
     */
    private String tbOrderId;

    /**
     * 绑定用户标识
     */
    private Long wxUserId;

    /**
     * 绑定状态，1：未绑定，2：已绑定，3：无效
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

    // TODO 以下字段是Pojo

    /**
     * 绑定状态，1：未绑定，2：已绑定，3：无效
     */
    private String statusStr;

    /**
     * 创建时间（字符串）
     */
    private String createTimeStr;

    /**
     * 更新时间（字符串）
     */
    private String updateTimeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getTbOrderId() {
        return tbOrderId;
    }

    public void setTbOrderId(String tbOrderId) {
        this.tbOrderId = tbOrderId == null ? null : tbOrderId.trim();
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

    public String getStatusStr() {
        return statusStr;
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
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

    @Override
    public String toString() {
        return "TbOrderBindingRecord{" +
                "id='" + id + '\'' +
                ", tbOrderId='" + tbOrderId + '\'' +
                ", wxUserId=" + wxUserId +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", statusStr='" + statusStr + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                '}';
    }
}