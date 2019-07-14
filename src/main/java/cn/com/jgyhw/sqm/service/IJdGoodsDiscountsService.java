package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/4/8 0008 15:58
 *
 * 京东商品优惠接口
 */
public interface IJdGoodsDiscountsService {

    /**
     * 保存京东商品优惠
     *
     * @param jdGoodsDiscounts 京东商品优惠对象
     */
    void saveJdGoodsDiscounts(JdGoodsDiscounts jdGoodsDiscounts);

    /**
     * 根据标识查询京东商品优惠
     *
     * @param id 标识
     * @return
     */
    JdGoodsDiscounts queryJdGoodsDiscountsById(String id);

    /**
     * 根据商品编号查询京东商品优惠
     *
     * @param goodsId 商品编号
     * @return
     */
    JdGoodsDiscounts queryJdGoodsDiscountsByGoodsId(Long goodsId);

    /**
     * 更新京东商品优惠
     *
     * @param jdGoodsDiscounts 京东商品优惠对象
     */
    void updateJdGoodsDiscounts(JdGoodsDiscounts jdGoodsDiscounts);

    /**
     * 根据条件分页查询京东商品优惠
     *
     * @param goodsClassify 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<JdGoodsDiscounts> queryJdGoodsDiscountsByConditionPage(String goodsClassify, int pageNo, int pageSize);

    /**
     * 根据商品类型和更新时间删除京东商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime 更新时间
     */
    void deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(String goodsClassify, String updateTime);

}
