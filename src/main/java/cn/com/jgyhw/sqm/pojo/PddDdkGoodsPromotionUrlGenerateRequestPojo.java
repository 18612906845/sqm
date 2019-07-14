package cn.com.jgyhw.sqm.pojo;

/**
 * Created by WangLei on 2019/5/2 0002 21:17
 */
public class PddDdkGoodsPromotionUrlGenerateRequestPojo {

    /**
     * 微信用户状态
     */
    private String loginKey;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 是否生成短链接，true-是，false-否
     */
    private Boolean generateShortUrl = true;

    /**
     * true--生成多人团推广链接 false--生成单人团推广链接（默认false）1、单人团推广链接：用户访问单人团推广链接，可直接购买商品无需拼团。2、多人团推广链接：用户访问双人团推广链接开团，若用户分享给他人参团，则开团者和参团者的佣金均结算给推手
     */
    private Boolean multiGroup = true;

    /**
     * 自定义参数
     */
    private String customParameters;

    /**
     * 是否生成唤起微信客户端链接，true-是，false-否，默认false
     */
    private Boolean generateWeappWebview = false;

    /**
     * 招商多多客ID
     */
    private Long zsDuoId;

    /**
     * 是否生成小程序推广
     */
    private Boolean generateWeApp = true;

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Boolean getGenerateShortUrl() {
        return generateShortUrl;
    }

    public void setGenerateShortUrl(Boolean generateShortUrl) {
        this.generateShortUrl = generateShortUrl;
    }

    public Boolean getMultiGroup() {
        return multiGroup;
    }

    public void setMultiGroup(Boolean multiGroup) {
        this.multiGroup = multiGroup;
    }

    public String getCustomParameters() {
        return customParameters;
    }

    public void setCustomParameters(String customParameters) {
        this.customParameters = customParameters;
    }

    public Boolean getGenerateWeappWebview() {
        return generateWeappWebview;
    }

    public void setGenerateWeappWebview(Boolean generateWeappWebview) {
        this.generateWeappWebview = generateWeappWebview;
    }

    public Long getZsDuoId() {
        return zsDuoId;
    }

    public void setZsDuoId(Long zsDuoId) {
        this.zsDuoId = zsDuoId;
    }

    public Boolean getGenerateWeApp() {
        return generateWeApp;
    }

    public void setGenerateWeApp(Boolean generateWeApp) {
        this.generateWeApp = generateWeApp;
    }
}
