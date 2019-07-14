package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/5/2 0002 11:58
 *
 * 拼多多商品优惠接口
 */
public interface IPddGoodsDiscountsService {

    /**
     * 保存拼多多商品优惠
     *
     * @param pddGoodsDiscounts 拼多多商品优惠对象
     */
    void savePddGoodsDiscounts(PddGoodsDiscounts pddGoodsDiscounts);

    /**
     * 根据商品id查询拼多多商品优惠
     *
     * @param goodsId 商品id
     * @return
     */
    PddGoodsDiscounts queryPddGoodsDiscountsByGoodsId(Long goodsId);

    /**
     * 更新拼多多商品优惠
     *
     * @param pddGoodsDiscounts 拼多多商品优惠对象
     */
    void updatePddGoodsDiscounts(PddGoodsDiscounts pddGoodsDiscounts);

    /**
     * 根据条件分页查询拼多多商品优惠（分页）
     *
     * @param goodsClassify 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<PddGoodsDiscounts> queryPddGoodsDiscountsByConditionPage(String goodsClassify, int pageNo, int pageSize);

    /**
     * 根据商品类型和更新时间删除拼多多商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime 更新时间
     */
    void deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(String goodsClassify, String updateTime);
}
