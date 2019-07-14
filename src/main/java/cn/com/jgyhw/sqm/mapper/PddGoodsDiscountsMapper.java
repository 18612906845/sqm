package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PddGoodsDiscountsMapper {

    int deleteByPrimaryKey(String id);

    int insert(PddGoodsDiscounts record);

    int insertSelective(PddGoodsDiscounts record);

    PddGoodsDiscounts selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(PddGoodsDiscounts record);

    int updateByPrimaryKey(PddGoodsDiscounts record);

    /**
     * 根据商品id查询拼多多商品优惠
     *
     * @param goodsId 商品id
     * @return
     */
    PddGoodsDiscounts selectPddGoodsDiscountsByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 根据条件分页查询拼多多商品优惠（分页）
     *
     * @param goodsClassify 商品类型
     * @return
     */
    Page<PddGoodsDiscounts> selectPddGoodsDiscountsByGoodsClassifyPage(@Param("goodsClassify") String goodsClassify);

    /**
     * 根据商品类型和更新时间删除拼多多商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime 更新时间
     */
    void deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(@Param("goodsClassify") String goodsClassify, @Param("updateTime") String updateTime);
}