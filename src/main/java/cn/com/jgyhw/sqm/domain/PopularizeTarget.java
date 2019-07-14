package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 线下推广主体
 */
public class PopularizeTarget {
    /**
     * 标识（自增）
     */
    private Long id;

    /**
     * 主体名称
     */
    private String targetName;

    /**
     * 主体负责人姓名，若需要邮寄为必填项
     */
    private String name;

    /**
     * 主体负责人联系电话，若需要邮寄为必填项
     */
    private String phone;

    /**
     * 推广主体地址，若需要邮寄为必填项
     */
    private String address;

    /**
     * 关联的推广者用户信息，此字段标识推广码是否已被推广者绑定
     */
    private Long wxUserId;

    /**
     * 推广二维码创建时间
     */
    private Date createTime;

    /**
     * 推广二维码与推广者绑定时间
     */
    private Date bindingTime;

    /**
     * 推广主体信息修改时间
     */
    private Date updateTime;

    // TODO 一下字段都是Pojo字段

    /**
     * 二维码URL地址
     */
    private String qrCodeUrl;

    /**
     * 推广二维码创建时间（字符串）
     */
    private String createTimeStr;

    /**
     * 推广二维码与推广者绑定时间（字符串）
     */
    private String bindingTimeStr;

    /**
     * 推广主体信息修改时间（字符串）
     */
    private String updateTimeStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName == null ? null : targetName.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getBindingTime() {
        return bindingTime;
    }

    public void setBindingTime(Date bindingTime) {
        this.bindingTime = bindingTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public String getCreateTimeStr() {
        return createTimeStr;
    }

    public void setCreateTimeStr(String createTimeStr) {
        this.createTimeStr = createTimeStr;
    }

    public String getBindingTimeStr() {
        return bindingTimeStr;
    }

    public void setBindingTimeStr(String bindingTimeStr) {
        this.bindingTimeStr = bindingTimeStr;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    @Override
    public String toString() {
        return "PopularizeTarget{" +
                "id=" + id +
                ", targetName='" + targetName + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", wxUserId=" + wxUserId +
                ", createTime=" + createTime +
                ", bindingTime=" + bindingTime +
                ", updateTime=" + updateTime +
                ", qrCodeUrl='" + qrCodeUrl + '\'' +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", bindingTimeStr='" + bindingTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                '}';
    }
}