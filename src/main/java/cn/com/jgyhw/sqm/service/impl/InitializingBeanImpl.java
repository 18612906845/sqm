package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.Configuration;
import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import cn.com.jgyhw.sqm.domain.WxToken;
import cn.com.jgyhw.sqm.mapper.ConfigurationMapper;
import cn.com.jgyhw.sqm.mapper.DiscountsGoodsClassifyMapper;
import cn.com.jgyhw.sqm.service.IWxTokenService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import cn.com.jgyhw.sqm.util.UpdateApplicationParamUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class InitializingBeanImpl implements InitializingBean {

    private static Logger LOGGER = LogManager.getLogger(InitializingBeanImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private UpdateApplicationParamUtil updateApplicationParamUtil;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Autowired
    private DiscountsGoodsClassifyMapper discountsGoodsClassifyMapper;

    @Autowired
    private IWxTokenService wxTokenService;

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.info("项目启动完成--> 执行初始化应用参数方法");
        //初始化系统参数
        LOGGER.info("执行系统参数初始化--> 开始");
        String systemGroup = configurationPropertiesSqm.getCONFIGURATION_GROUP_XT();
        List<Configuration> systemConfigurations = this.configurationMapper.selectConfigurationListByParamGroup(systemGroup);
        if(systemConfigurations == null || systemConfigurations.isEmpty()){
            LOGGER.warn("执行系统参数初始化--> 警告：configuration表" + systemGroup  +"分组无数据");
        }else{
            updateApplicationParamUtil.initSystemParamMap(systemConfigurations);
        }
        LOGGER.info("执行系统参数初始化--> 结束");

        //初始化京东参数
        LOGGER.info("执行京东参数初始化--> 开始");
        String jdGroup = configurationPropertiesSqm.getCONFIGURATION_GROUP_JD();
        List<Configuration> jdConfigurations = this.configurationMapper.selectConfigurationListByParamGroup(jdGroup);
        if(jdConfigurations == null || jdConfigurations.isEmpty()){
            LOGGER.warn("执行京东参数初始化--> 警告：configuration表" + jdGroup  +"分组无数据");
        }else{
            updateApplicationParamUtil.initJdParamMap(jdConfigurations);
        }
        LOGGER.info("执行京东参数初始化--> 结束");

        //初始化微信参数
        LOGGER.info("执行微信参数初始化--> 开始");
        String wxGroup = configurationPropertiesSqm.getCONFIGURATION_GROUP_WX();
        List<Configuration> wxConfigurations = this.configurationMapper.selectConfigurationListByParamGroup(wxGroup);
        if(wxConfigurations == null || wxConfigurations.isEmpty()){
            LOGGER.warn("执行微信参数初始化--> 警告：configuration表" + wxGroup  +"分组无数据");
        }else{
            updateApplicationParamUtil.initWxParamMap(wxConfigurations);
        }
        LOGGER.info("执行微信参数初始化--> 结束");

        //初始化拼多多参数
        LOGGER.info("执行拼多多参数初始化--> 开始");
        String pddGroup = configurationPropertiesSqm.getCONFIGURATION_GROUP_PDD();
        List<Configuration> pddConfigurations = this.configurationMapper.selectConfigurationListByParamGroup(pddGroup);
        if(pddConfigurations == null || pddConfigurations.isEmpty()){
            LOGGER.warn("执行拼多多参数初始化--> 警告：configuration表" + pddGroup  +"分组无数据");
        }else{
            updateApplicationParamUtil.initPddParamMap(pddConfigurations);
        }
        LOGGER.info("执行拼多多参数初始化--> 结束");

        //初始化淘宝参数
        LOGGER.info("执行淘宝参数初始化--> 开始");
        String tbGroup = configurationPropertiesSqm.getCONFIGURATION_GROUP_TB();
        List<Configuration> tbConfigurations = this.configurationMapper.selectConfigurationListByParamGroup(tbGroup);
        if(tbConfigurations == null || tbConfigurations.isEmpty()){
            LOGGER.warn("执行淘宝参数初始化--> 警告：configuration表" + tbGroup  +"分组无数据");
        }else{
            updateApplicationParamUtil.initTbParamMap(tbConfigurations);
        }
        LOGGER.info("执行淘宝参数初始化--> 结束");

        //初始化商品类型缓存
        LOGGER.info("执行商品类型缓存初始化--> 开始");
        Map<String, List<DiscountsGoodsClassify>> dgcMap = new TreeMap<>();
        List<DiscountsGoodsClassify> dgcJd = this.discountsGoodsClassifyMapper.selectDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        dgcMap.put("dgcJd", dgcJd);
        List<DiscountsGoodsClassify> dgcTb = this.discountsGoodsClassifyMapper.selectDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        dgcMap.put("dgcTb", dgcTb);
        List<DiscountsGoodsClassify> dgcPdd = this.discountsGoodsClassifyMapper.selectDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        dgcMap.put("dgcPdd", dgcPdd);
        ApplicationParamConstant.GOODS_CLASSIFY_MAP = dgcMap;
        LOGGER.info("执行商品类型缓存初始化--> 结束");

        //初始化微信Token数据
        LOGGER.info("微信Token初始化--> 开始");
        List<WxToken> wxTokenList = wxTokenService.queryWxTokenAll();
        if(wxTokenList != null && !wxTokenList.isEmpty()){
            for(WxToken wt : wxTokenList){
                if (Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_gzh_server_api")) == wt.getTokenType()){// 公众号服务Api接口Token
                    configurationPropertiesSqm.setWX_GZH_SERVER_API_TOKEN(wt.getToken());
                }else if(Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_xcx_server_api")) == wt.getTokenType()){// 小程序服务Api接口Token
                    configurationPropertiesSqm.setWX_XCX_SERVER_API_TOKEN(wt.getToken());
                }
            }
        }
        LOGGER.info("微信Token初始化--> 结束");
    }
}
