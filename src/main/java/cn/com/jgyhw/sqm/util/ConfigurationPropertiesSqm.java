package cn.com.jgyhw.sqm.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangLei on 2019/1/12 0012 18:14
 *
 * 配置文件
 */
@Component
@PropertySource("classpath:configuration.properties")
public class ConfigurationPropertiesSqm implements WebMvcConfigurer {

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 参数配置分组-系统
     */
    @Value("${CONFIGURATION_GROUP_XT}")
    private String CONFIGURATION_GROUP_XT;

    /**
     * 参数配置分组-京东
     */
    @Value("${CONFIGURATION_GROUP_JD}")
    private String CONFIGURATION_GROUP_JD;

    /**
     * 参数配置分组-微信
     */
    @Value("${CONFIGURATION_GROUP_WX}")
    private String CONFIGURATION_GROUP_WX;

    /**
     * 参数配置分组-拼多多
     */
    @Value("${CONFIGURATION_GROUP_PDD}")
    private String CONFIGURATION_GROUP_PDD;

    /**
     * 参数配置分组-淘宝
     */
    @Value("${CONFIGURATION_GROUP_TB}")
    private String CONFIGURATION_GROUP_TB;

    /**
     * 微信支付Key
     */
    @Value("${WX_PAY_BY_KEY}")
    private String WX_PAY_BY_KEY;

    /**
     * 微信小程序AppKey
     */
    @Value("${WX_XCX_APP_ID}")
    private String WX_XCX_APP_ID;

    /**
     * 微信小程序AppSecret
     */
    @Value("${WX_XCX_APP_SECRET}")
    private String WX_XCX_APP_SECRET;

    /**
     * 微信公众号AppKey
     */
    @Value("${WX_GZH_APP_ID}")
    private String WX_GZH_APP_ID;

    /**
     * 微信公众号AppSecret
     */
    @Value("${WX_GZH_APP_SECRET}")
    private String WX_GZH_APP_SECRET;

    /**
     * 京东联盟AppKey
     */
    @Value("${JD_APP_KEY}")
    private String JD_APP_KEY;

    /**
     * 京东联盟AppSecret
     */
    @Value("${JD_APP_SECRET}")
    private String JD_APP_SECRET;

    /**
     * 拼多多开放平台ClientId
     */
    @Value("${PDD_CLIENT_ID}")
    private String PDD_CLIENT_ID;

    /**
     * 拼多多开放平台ClientSecret
     */
    @Value("${PDD_CLIENT_SECRET}")
    private String PDD_CLIENT_SECRET;

    /**
     * 淘宝应用KEY
     */
    @Value("${TB_APP_KEY}")
    private String TB_APP_KEY;

    /**
     * 淘宝应用KEY密匙
     */
    @Value("${TB_APP_SECRET}")
    private String TB_APP_SECRET;

    /**
     * 微信公众号服务Api令牌
     */
    private String WX_GZH_SERVER_API_TOKEN;

    /**
     * 微信小程序服务Api令牌
     */
    private String WX_XCX_SERVER_API_TOKEN;


    @Value("${POPULARIZE_QR_CODE_PATH}")
    private String POPULARIZE_QR_CODE_PATH;

    /**
     * 京东接口（第三方）API调用KEY
     */
    @Value("${APITH_SECRET_ID}")
    private String APITH_SECRET_ID;

    /**
     * 京东接口（第三方）API调用KEY密匙
     */
    @Value("${APITH_SECRET_KEY}")
    private String APITH_SECRET_KEY;

    /**
     * 淘宝接口（第三方）API调用KEY密匙
     */
    @Value("${TB_OTHER_API_AP_KEY}")
    private String TB_OTHER_API_AP_KEY;

    /**
     * 淘宝推广账户用户名
     */
    @Value("${TB_USER_NAME}")
    private String TB_USER_NAME;

    /**
     * 设置此处配置，页面可以拿到数据
     * @param registry
     */
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        if (thymeleafViewResolver != null) {
            Map<String, Object> vars = new HashMap<>();
            vars.put("CONFIGURATION_GROUP_XT", CONFIGURATION_GROUP_XT);
            vars.put("CONFIGURATION_GROUP_JD", CONFIGURATION_GROUP_JD);
            vars.put("CONFIGURATION_GROUP_WX", CONFIGURATION_GROUP_WX);
            vars.put("CONFIGURATION_GROUP_PDD", CONFIGURATION_GROUP_PDD);
            vars.put("CONFIGURATION_GROUP_TB", CONFIGURATION_GROUP_TB);
            thymeleafViewResolver.setStaticVariables(vars);
        }
    }

    public String getCONFIGURATION_GROUP_XT() {
        return CONFIGURATION_GROUP_XT;
    }

    public String getCONFIGURATION_GROUP_JD() {
        return CONFIGURATION_GROUP_JD;
    }

    public String getCONFIGURATION_GROUP_WX() {
        return CONFIGURATION_GROUP_WX;
    }

    public String getCONFIGURATION_GROUP_PDD() {
        return CONFIGURATION_GROUP_PDD;
    }

    public String getCONFIGURATION_GROUP_TB() {
        return CONFIGURATION_GROUP_TB;
    }

    public String getWX_GZH_APP_ID() {
        return WX_GZH_APP_ID;
    }

    public String getWX_GZH_APP_SECRET() {
        return WX_GZH_APP_SECRET;
    }

    public String getWX_PAY_BY_KEY() {
        return WX_PAY_BY_KEY;
    }

    public String getWX_XCX_APP_ID() {
        return WX_XCX_APP_ID;
    }

    public String getWX_XCX_APP_SECRET() {
        return WX_XCX_APP_SECRET;
    }

    public String getJD_APP_KEY() {
        return JD_APP_KEY;
    }

    public String getJD_APP_SECRET() {
        return JD_APP_SECRET;
    }

    public String getPDD_CLIENT_ID() {
        return PDD_CLIENT_ID;
    }

    public String getPDD_CLIENT_SECRET() {
        return PDD_CLIENT_SECRET;
    }

    public String getTB_APP_KEY() {
        return TB_APP_KEY;
    }

    public String getTB_APP_SECRET() {
        return TB_APP_SECRET;
    }

    public String getWX_GZH_SERVER_API_TOKEN() {
        return WX_GZH_SERVER_API_TOKEN;
    }

    public void setWX_GZH_SERVER_API_TOKEN(String WX_GZH_SERVER_API_TOKEN) {
        this.WX_GZH_SERVER_API_TOKEN = WX_GZH_SERVER_API_TOKEN;
    }

    public String getWX_XCX_SERVER_API_TOKEN() {
        return WX_XCX_SERVER_API_TOKEN;
    }

    public void setWX_XCX_SERVER_API_TOKEN(String WX_XCX_SERVER_API_TOKEN) {
        this.WX_XCX_SERVER_API_TOKEN = WX_XCX_SERVER_API_TOKEN;
    }

    public String getPOPULARIZE_QR_CODE_PATH() {
        return POPULARIZE_QR_CODE_PATH;
    }

    public String getAPITH_SECRET_ID() {
        return APITH_SECRET_ID;
    }

    public String getAPITH_SECRET_KEY() {
        return APITH_SECRET_KEY;
    }

    public String getTB_OTHER_API_AP_KEY() {
        return TB_OTHER_API_AP_KEY;
    }

    public String getTB_USER_NAME() {
        return TB_USER_NAME;
    }
}
