package cn.com.jgyhw.sqm.domain;

import java.util.Date;

public class DiscountsGoodsClassify {
    /**
     * 标识
     */
    private String id;

    /**
     * 类型名称
     */
    private String classifyName;

    /**
     * 类型ID
     */
    private String classifyId;

    /**
     * 平台
     */
    private Integer platform;

    /**
     * 类型排序号
     */
    private Integer orderNum;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    // TODO 以下字段是Pojo字段

    /**
     * 平台
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getClassifyName() {
        return classifyName;
    }

    public void setClassifyName(String classifyName) {
        this.classifyName = classifyName == null ? null : classifyName.trim();
    }

    public String getClassifyId() {
        return classifyId;
    }

    public void setClassifyId(String classifyId) {
        this.classifyId = classifyId == null ? null : classifyId.trim();
    }

    public Integer getPlatform() {
        return platform;
    }

    public void setPlatform(Integer platform) {
        this.platform = platform;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
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

    @Override
    public String toString() {
        return "DiscountsGoodsClassify{" +
                "id='" + id + '\'' +
                ", classifyName='" + classifyName + '\'' +
                ", classifyId='" + classifyId + '\'' +
                ", platform=" + platform +
                ", orderNum=" + orderNum +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", platformStr='" + platformStr + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                '}';
    }
}