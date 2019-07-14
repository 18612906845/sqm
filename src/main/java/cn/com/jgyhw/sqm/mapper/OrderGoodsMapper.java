package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.OrderGoods;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGoodsMapper {

    int deleteByPrimaryKey(String id);

    int insert(OrderGoods record);

    int insertSelective(OrderGoods record);

    OrderGoods selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderGoods record);

    int updateByPrimaryKey(OrderGoods record);

    /**
     * 根据订单记录标识查询订单商品集合
     *
     * @param orderRecordId 订单记录标识
     * @return
     */
    List<OrderGoods> selectOrderGoodsListByOrderRecordId(@Param("orderRecordId") String orderRecordId);

    /**
     * 根据订单记录标识和商品编号查询订单商品
     *
     * @param orderRecordId 订单记录标识
     * @param code 商品编号
     * @return
     */
    OrderGoods selectOrderGoodsByOrderRecordIdAndCode(@Param("orderRecordId") String orderRecordId, @Param("code") String code);
}