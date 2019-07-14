package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 微信令牌
 */
public class WxToken {
    /**
     * 标识
     */
    private String id;

    /**
     * 微信应用ID
     */
    private String appId;

    /**
     * token值
     */
    private String token;

    /**
     * token类型，1：公众号服务Api接口Token，2：公众号JS Api接口Token，3：小程序服务Api接口Token
     */
    private Integer tokenType;

    /**
     * 更新时间
     */
    private Date updateTime;

    // TODO 以下是Pojo字段


    /**
     * 更新时间（字符串）
     */
    private String updateTimeStr;

    /**
     * token类型，1：公众号服务Api接口Token，2：公众号JS Api接口Token，3：小程序服务Api接口Token
     */
    private String tokenTypeStr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public Integer getTokenType() {
        return tokenType;
    }

    public void setTokenType(Integer tokenType) {
        this.tokenType = tokenType;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUpdateTimeStr() {
        return updateTimeStr;
    }

    public void setUpdateTimeStr(String updateTimeStr) {
        this.updateTimeStr = updateTimeStr;
    }

    public String getTokenTypeStr() {
        return tokenTypeStr;
    }

    public void setTokenTypeStr(String tokenTypeStr) {
        this.tokenTypeStr = tokenTypeStr;
    }

    @Override
    public String toString() {
        return "WxToken{" +
                "id='" + id + '\'' +
                ", appId='" + appId + '\'' +
                ", token='" + token + '\'' +
                ", tokenType=" + tokenType +
                ", updateTime=" + updateTime +
                ", updateTimeStr='" + updateTimeStr + '\'' +
                ", tokenTypeStr='" + tokenTypeStr + '\'' +
                '}';
    }
}