package cn.com.jgyhw.sqm.util;


import cn.com.jgyhw.sqm.domain.Configuration;
import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 更新应用参数常量
 */
@Component
public class UpdateApplicationParamUtil {

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    /**
     * 初始化系统参数
     *
     * @param configurations 系统参数集合
     */
    public void initSystemParamMap(List<Configuration> configurations){
        Map<String, String> map = configurationsToMap(configurations);
        if(map != null){
            ApplicationParamConstant.XT_PARAM_MAP = map;
        }
    }

    /**
     * 初始化京东参数
     *
     * @param configurations 京东参数对象
     */
    public void initJdParamMap(List<Configuration> configurations){
        Map<String, String> map = configurationsToMap(configurations);
        if(map != null){
            ApplicationParamConstant.JD_PARAM_MAP = map;
        }
    }

    /**
     * 初始化微信公众平台参数
     *
     * @param configurations 微信公众平台参数对象
     */
    public void initWxParamMap(List<Configuration> configurations){
        Map<String, String> map = configurationsToMap(configurations);
        if(map != null){
            ApplicationParamConstant.WX_PARAM_MAP = map;
        }
    }

    /**
     * 初始化拼多多参数
     *
     * @param configurations 拼多多参数对象
     */
    public void initPddParamMap(List<Configuration> configurations){
        Map<String, String> map = configurationsToMap(configurations);
        if(map != null){
            ApplicationParamConstant.PDD_PARAM_MAP = map;
        }
    }

    /**
     * 初始化淘宝参数
     *
     * @param configurations 淘宝参数对象
     */
    public void initTbParamMap(List<Configuration> configurations){
        Map<String, String> map = configurationsToMap(configurations);
        if(map != null){
            ApplicationParamConstant.TB_PARAM_MAP = map;
        }
    }

    /**
     * 增加或修改参数配置缓存
     *
     * @param configuration 参数对象
     */
    public void addOrUpdateParamMap(Configuration configuration){
        if(configuration == null){
            return;
        }
        if(configurationPropertiesSqm.getCONFIGURATION_GROUP_XT().equals(configuration.getParamGroup())){// 系统参数
            ApplicationParamConstant.XT_PARAM_MAP.put(configuration.getParamName(), configuration.getParamValue());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_WX().equals(configuration.getParamGroup())){// 微信参数
            ApplicationParamConstant.WX_PARAM_MAP.put(configuration.getParamName(), configuration.getParamValue());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_JD().equals(configuration.getParamGroup())){// 京东参数
            ApplicationParamConstant.JD_PARAM_MAP.put(configuration.getParamName(), configuration.getParamValue());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_PDD().equals(configuration.getParamGroup())){// 拼多多参数
            ApplicationParamConstant.PDD_PARAM_MAP.put(configuration.getParamName(), configuration.getParamValue());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_TB().equals(configuration.getParamGroup())){// 淘宝参数
            ApplicationParamConstant.TB_PARAM_MAP.put(configuration.getParamName(), configuration.getParamValue());
        }
    }

    /**
     * 删除参数配置缓存
     *
     * @param configuration 参数对象
     */
    public void deleteParamMap(Configuration configuration){
        if(configuration == null){
            return;
        }
        if(configurationPropertiesSqm.getCONFIGURATION_GROUP_XT().equals(configuration.getParamGroup())){// 系统参数
            ApplicationParamConstant.XT_PARAM_MAP.remove(configuration.getParamName());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_WX().equals(configuration.getParamGroup())){// 微信参数
            ApplicationParamConstant.WX_PARAM_MAP.remove(configuration.getParamName());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_JD().equals(configuration.getParamGroup())){// 京东参数
            ApplicationParamConstant.JD_PARAM_MAP.remove(configuration.getParamName());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_PDD().equals(configuration.getParamGroup())){// 拼多多参数
            ApplicationParamConstant.PDD_PARAM_MAP.remove(configuration.getParamName());
        }else if(configurationPropertiesSqm.getCONFIGURATION_GROUP_TB().equals(configuration.getParamGroup())){// 淘宝参数
            ApplicationParamConstant.TB_PARAM_MAP.remove(configuration.getParamName());
        }
    }

    /**
     * 修改商品分类缓存
     *
     * @param key 商品分类key
     * @param dgcList 商品分类集合
     */
    public void updateGoodsClassifyMap(String key, List<DiscountsGoodsClassify> dgcList){
        if(StringUtils.isBlank(key)){
            return;
        }
        ApplicationParamConstant.GOODS_CLASSIFY_MAP.put(key, dgcList);
    }

    private Map<String, String> configurationsToMap(List<Configuration> configurations){
        if(configurations == null || configurations.isEmpty()){
            return null;
        }
        Map<String, String> map = new TreeMap<>();
        for(Configuration configuration : configurations){
            map.put(configuration.getParamName(), configuration.getParamValue());
        }
        return map;
    }

}
