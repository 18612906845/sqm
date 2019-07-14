package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 微信用户
 */
public class WxUser {
    /**
     * 标识（自增）
     */
    private Long id;

    /**
     * 用户在开放平台的唯一标识符
     */
    private String unionId;

    /**
     * 微信用户标识-公众号
     */
    private String openIdGzh;

    /**
     * 微信用户标识-小程序
     */
    private String openIdXcx;

    /**
     * 微信用户标识-PC网站
     */
    private String openIdPc;

    /**
     * 会话密钥-小程序
     */
    private String sessionKeyXcx;

    /**
     * 推荐人微信用户标识
     */
    private Long parentWxUserId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户性别，1：男性，2：女性，0：未知
     */
    private Integer sex;

    /**
     * 用户填写的省份
     */
    private String province;

    /**
     * 用户填写的城市
     */
    private String city;

    /**
     * 用户填写的国家
     */
    private String country;

    /**
     * 用户头像URL
     */
    private String headImgUrl;

    /**
     * 可提现余额
     */
    private Double remainingMoney;

    /**
     * 累计提现
     */
    private Double withdrawCashSum;

    /**
     * 返现比例（无券）
     */
    private Integer returnMoneyShareWq;

    /**
     * 返现比例（有券）
     */
    private Integer returnMoneyShareYq;

    /**
     * 提成比例
     */
    private Integer returnMoneyShareTc;

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
     * 创建时间（字符串）
     */
    private String createTimeStr = "";

    /**
     * 更新时间（字符串）
     */
    private String updateTimeStr = "";

    /**
     * 用户性别，1：男性，2：女性，0：未知
     */
    private String sexStr;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId == null ? null : unionId.trim();
    }

    public String getOpenIdGzh() {
        return openIdGzh;
    }

    public void setOpenIdGzh(String openIdGzh) {
        this.openIdGzh = openIdGzh == null ? null : openIdGzh.trim();
    }

    public String getOpenIdXcx() {
        return openIdXcx;
    }

    public void setOpenIdXcx(String openIdXcx) {
        this.openIdXcx = openIdXcx == null ? null : openIdXcx.trim();
    }

    public String getOpenIdPc() {
        return openIdPc;
    }

    public void setOpenIdPc(String openIdPc) {
        this.openIdPc = openIdPc == null ? null : openIdPc.trim();
    }

    public String getSessionKeyXcx() {
        return sessionKeyXcx;
    }

    public void setSessionKeyXcx(String sessionKeyXcx) {
        this.sessionKeyXcx = sessionKeyXcx == null ? null : sessionKeyXcx.trim();
    }

    public Long getParentWxUserId() {
        return parentWxUserId;
    }

    public void setParentWxUserId(Long parentWxUserId) {
        this.parentWxUserId = parentWxUserId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName == null ? null : nickName.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl == null ? null : headImgUrl.trim();
    }

    public Double getRemainingMoney() {
        return remainingMoney;
    }

    public void setRemainingMoney(Double remainingMoney) {
        this.remainingMoney = remainingMoney;
    }

    public Integer getReturnMoneyShareWq() {
        return returnMoneyShareWq;
    }

    public void setReturnMoneyShareWq(Integer returnMoneyShareWq) {
        this.returnMoneyShareWq = returnMoneyShareWq;
    }

    public Integer getReturnMoneyShareYq() {
        return returnMoneyShareYq;
    }

    public void setReturnMoneyShareYq(Integer returnMoneyShareYq) {
        this.returnMoneyShareYq = returnMoneyShareYq;
    }

    public Integer getReturnMoneyShareTc() {
        return returnMoneyShareTc;
    }

    public void setReturnMoneyShareTc(Integer returnMoneyShareTc) {
        this.returnMoneyShareTc = returnMoneyShareTc;
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

    public Double getWithdrawCashSum() {
        return withdrawCashSum;
    }

    public void setWithdrawCashSum(Double withdrawCashSum) {
        this.withdrawCashSum = withdrawCashSum;
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

    public String getSexStr() {
        return sexStr;
    }

    public void setSexStr(String sexStr) {
        this.sexStr = sexStr;
    }

    @Override
    public String toString() {
        return "WxUser{" +
                "id=" + id +
                ", unionId='" + unionId + '\'' +
                ", openIdGzh='" + openIdGzh + '\'' +
                ", openIdXcx='" + openIdXcx + '\'' +
                ", openIdPc='" + openIdPc + '\'' +
                ", sessionKeyXcx='" + sessionKeyXcx + '\'' +
                ", parentWxUserId=" + parentWxUserId +
                ", nickName='" + nickName + '\'' +
                ", sex=" + sex +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", headImgUrl='" + headImgUrl + '\'' +
                ", remainingMoney=" + remainingMoney +
                ", withdrawCashSum=" + withdrawCashSum +
                ", returnMoneyShareWq=" + returnMoneyShareWq +
                ", returnMoneyShareYq=" + returnMoneyShareYq +
                ", returnMoneyShareTc=" + returnMoneyShareTc +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", createTimeStr='" + createTimeStr + '\'' +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                ", sexStr='" + sexStr + '\'' +
                '}';
    }
}