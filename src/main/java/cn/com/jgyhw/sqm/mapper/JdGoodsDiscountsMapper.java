package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JdGoodsDiscountsMapper {

    int deleteByPrimaryKey(String id);

    int insert(JdGoodsDiscounts record);

    int insertSelective(JdGoodsDiscounts record);

    JdGoodsDiscounts selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JdGoodsDiscounts record);

    int updateByPrimaryKey(JdGoodsDiscounts record);

    /**
     * 根据商品编号查询京东商品优惠
     *
     * @param goodsId 商品编号
     * @return
     */
    JdGoodsDiscounts selectJdGoodsDiscountsByGoodsId(@Param("goodsId") Long goodsId);

    /**
     * 根据条件分页查询京东商品优惠（分页）
     *
     * @param goodsClassify 商品类型
     * @return
     */
    Page<JdGoodsDiscounts> selectJdGoodsDiscountsByGoodsClassifyPage(@Param("goodsClassify") String goodsClassify);


    /**
     * 根据商品类型和更新时间删除京东商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime 更新时间
     */
    void deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(@Param("goodsClassify") String goodsClassify, @Param("updateTime") String updateTime);
}