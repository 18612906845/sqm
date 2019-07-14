package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;

import java.util.List;

/**
 * Created by WangLei on 2019/5/20 0020 19:49
 *
 * 优惠商品类型接口
 */
public interface IDiscountsGoodsClassifyService {

    /**
     * 根据平台标识查询优惠商品类型集合
     *
     * @param platform 平台标识
     * @return
     */
    List<DiscountsGoodsClassify> queryDiscountsGoodsClassifyListByPlatform(Integer platform);
}
