package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.OrderGoods;
import cn.com.jgyhw.sqm.domain.OrderRecord;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by WangLei on 2019/4/10 0010 22:15
 *
 * 订单记录接口
 */
public interface IOrderRecordService {

    /**
     * 保存订单记录
     *
     * @param orderRecord 订单记录对象
     */
    void saveOrderRecord(OrderRecord orderRecord);

    /**
     * 保存商品记录
     *
     * @param orderGoods 商品记录对象
     */
    void saveOrderGoods(OrderGoods orderGoods);

    /**
     * 修改订单记录
     *
     * @param orderRecord 订单记录对象
     */
    void updateOrderRecord(OrderRecord orderRecord);

    /**
     * 根据订单编号和订单平台删除订单记录
     *
     * @param orderId 订单编号
     * @param platform 订单平台
     */
    void deleteOrderRecordByOrderIdAndPlatform(String orderId, int platform);

    /**
     * 根据订单编号和订单平台查询订单记录
     *
     * @param orderId 订单编号
     * @param platform 订单平台
     */
    OrderRecord queryOrderRecordByOrderIdAndPlatform(String orderId, int platform);

    /**
     * 根据微信用户标识和订单状态查询订单记录总数
     *
     * @param wxUserId 微信用户标识
     * @param status 订单状态
     * @return
     */
    int queryOrderRecordSumByWxUserIdAndStatus(Long wxUserId, int status);

    /**
     * 根据微信用户标识和订单状态查询订单记录集合
     *
     * @param wxUserId 微信用户标识
     * @param status 订单类型
     * @return
     */
    List<OrderRecord> queryOrderRecordListByWxUserIdAndStatus(Long wxUserId, String status);

    /**
     * 根据微信用户标识查询预估返现总和
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    double queryEstimateReturnMoneySumByWxUserId(Long wxUserId);

    /**
     * 根据微信用户标识和订单状态查询返现总和
     *
     * @param wxUserId 微信用户标识
     * @param status 订单状态
     * @return
     */
    double queryReturnMoneySumByWxUserIdAndStatus(Long wxUserId, int status);

    /**
     * 根据订单记录标识查询订单关联的商品集合
     *
     * @param orderRecordId 订单记录标识
     * @return
     */
    List<OrderGoods> queryOrderGoodsListByOrderId(String orderRecordId);

    /**
     * 根据订单记录标识查询订单记录
     *
     * @param id 订单记录标识
     */
    OrderRecord queryOrderRecordById(String id);

    /**
     * 根据条件查询推广订单记录（分页）
     *
     * @param wxUserId 微信用户标识
     * @param orderId 订单编号
     * @param platform 订单平台
     * @param status 订单状态
     * @param orderField 排序字段
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<OrderRecord> queryOrderRecordListByConditionPage(Long wxUserId, String orderId, Integer platform, Integer status, String orderField, int pageNo, int pageSize);

    /**
     * 根据订单状态、订单平台查询完成时间小于或等于传入时间的订单集合
     *
     * @param finishTimeStr 完成时间
     * @param status 订单状态
     * @param platform 订单平台
     * @return
     */
    List<OrderRecord> queryOrderRecordListByFinishTimeAndStatusAndPlatform(String finishTimeStr, Integer status, Integer platform);

}
