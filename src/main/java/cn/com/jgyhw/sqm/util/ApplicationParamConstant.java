package cn.com.jgyhw.sqm.util;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by WangLei on 2019/1/12 0012 22:09
 *
 * 参数常量类
 */
public class ApplicationParamConstant {

    /**
     * 系统配置参数
     */
    public static Map<String, String> XT_PARAM_MAP = new TreeMap<>();

    /**
     * 京东配置参数
     */
    public static Map<String, String> JD_PARAM_MAP = new TreeMap<>();

    /**
     * 微信配置参数
     */
    public static Map<String, String> WX_PARAM_MAP = new TreeMap<>();

    /**
     * 拼多多配置参数
     */
    public static Map<String, String> PDD_PARAM_MAP = new TreeMap<>();

    /**
     * 淘宝配置参数
     */
    public static Map<String, String> TB_PARAM_MAP = new TreeMap<>();

    /**
     * 商品类型缓存
     */
    public static Map<String, List<DiscountsGoodsClassify>> GOODS_CLASSIFY_MAP = new TreeMap<>();
}
