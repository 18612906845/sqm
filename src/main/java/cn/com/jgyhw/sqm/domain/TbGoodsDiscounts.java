package cn.com.jgyhw.sqm.domain;

/**
 * Created by WangLei on 2019/5/21 0021 00:23
 *
 * 淘宝优惠商品详情
 */
public class TbGoodsDiscounts extends GoodsDiscounts {

    /**
     * 商品推广连接
     */
    private String clickUrl;

    /**
     * 店铺信息-卖家类型，0表示集市，1表示商城
     */
    private Long userType;

    /**
     * 宝贝+券二合一页面链接
     */
    private String couponShareUrl;

    /**
     * 店铺信息-卖家类型，0表示集市，1表示商城
     */
    private String userTypeStr;

    public String getClickUrl() {
        return clickUrl;
    }

    public void setClickUrl(String clickUrl) {
        this.clickUrl = clickUrl;
    }

    public Long getUserType() {
        return userType;
    }

    public void setUserType(Long userType) {
        this.userType = userType;
    }

    public String getCouponShareUrl() {
        return couponShareUrl;
    }

    public void setCouponShareUrl(String couponShareUrl) {
        this.couponShareUrl = couponShareUrl;
    }

    public String getUserTypeStr() {
        return userTypeStr;
    }

    public void setUserTypeStr(String userTypeStr) {
        this.userTypeStr = userTypeStr;
    }

    @Override
    public String toString() {
        return "TbGoodsDiscounts{" +
                "clickUrl='" + clickUrl + '\'' +
                ", userType=" + userType +
                ", couponShareUrl='" + couponShareUrl + '\'' +
                ", userTypeStr='" + userTypeStr + '\'' +
                '}';
    }
}
