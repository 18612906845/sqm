package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.OrderRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRecordMapper {

    int deleteByPrimaryKey(String id);

    int insert(OrderRecord record);

    int insertSelective(OrderRecord record);

    OrderRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(OrderRecord record);

    int updateByPrimaryKey(OrderRecord record);

    /**
     * 根据订单编号和订单平台查询订单记录
     *
     * @param orderId 订单编号
     * @param platform 订单平台
     */
    OrderRecord selectOrderRecordByOrderIdAndPlatform(@Param("orderId") String orderId, @Param("platform") int platform);

    /**
     * 根据微信用户标识和订单状态查询订单记录总数
     *
     * @param wxUserId 微信用户标识
     * @param status 订单状态
     * @return
     */
    int selectOrderRecordSumByWxUserIdAndStatus(@Param("wxUserId") Long wxUserId, @Param("status") int status);

    /**
     * 查询已确认订单集合
     *
     * @param wxUserId 微信用户标识
     * @param status1 订单状态
     * @param platform1 订单平台
     * @param status2 订单状态
     * @param platform2 订单平台
     * @param status3 订单状态
     * @param platform3 订单平台
     * @return
     */
    List<OrderRecord> selectConfirmedOrderList(@Param("wxUserId") Long wxUserId, @Param("status1") int status1, @Param("platform1") int platform1, @Param("status2") int status2, @Param("platform2") int platform2, @Param("status3") int status3, @Param("platform3") int platform3);

    /**
     * 根据微信用户标识和订单状态查询订单记录集合
     *
     * @param wxUserId 微信用户标识
     * @param status 订单状态
     * @return
     */
    List<OrderRecord> selectOrderRecordListByWxUserIdAndStatus(@Param("wxUserId") Long wxUserId, @Param("status") int status);

    /**
     * 根据微信用户标识和订单状态数组查询订单记录集合
     *
     * @param wxUserId 微信用户标识
     * @param statusList 订单状态数组
     * @return
     */
    List<OrderRecord> selectOrderRecordListByWxUserIdAndMultiStatus(@Param("wxUserId") Long wxUserId, @Param("statusList") List<Integer> statusList);

    /**
     * 根据微信用户标识查询预估返现总和
     *
     * @param wxUserId 微信用户标识
     * @param status1 订单状态
     * @param platform1 订单平台
     * @param status2 订单状态
     * @param platform2 订单平台
     * @param status3 订单状态
     * @param platform3 订单平台
     * @return
     */
    Double selectEstimateReturnMoneySum(Long wxUserId, @Param("status1") int status1, @Param("platform1") int platform1, @Param("status2") int status2, @Param("platform2") int platform2, @Param("status3") int status3, @Param("platform3") int platform3);

    /**
     * 根据微信用户标识和订单状态查询返现总和
     *
     * @param wxUserId 微信用户标识
     * @param status 订单状态
     * @return
     */
    Double selectReturnMoneySumByWxUserIdAndStatus(@Param("wxUserId") Long wxUserId, @Param("status") int status);

    /**
     * 根据条件查询推广订单记录（分页）
     *
     * @param wxUserId 微信用户标识
     * @param orderId 订单编号
     * @param platform 订单平台
     * @param status 订单状态
     * @param orderField 排序字段
     * @return
     */
    Page<OrderRecord> selectOrderRecordListByConditionPage(@Param("wxUserId") Long wxUserId, @Param("orderId") String orderId, @Param("platform") Integer platform, @Param("status") Integer status, @Param("orderField") String orderField);


    /**
     * 根据订单状态、订单平台查询完成时间小于或等于传入时间的订单集合
     *
     * @param finishTimeStr 完成时间
     * @param status 订单状态
     * @param platform 订单平台
     * @return
     */
    List<OrderRecord> selectOrderRecordListByFinishTimeAndStatusAndPlatform(@Param("finishTimeStr") String finishTimeStr, @Param("status") Integer status, @Param("platform") Integer platform);
}