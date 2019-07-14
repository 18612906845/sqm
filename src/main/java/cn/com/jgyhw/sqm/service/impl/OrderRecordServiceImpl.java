package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.OrderGoods;
import cn.com.jgyhw.sqm.domain.OrderRecord;
import cn.com.jgyhw.sqm.mapper.OrderGoodsMapper;
import cn.com.jgyhw.sqm.mapper.OrderRecordMapper;
import cn.com.jgyhw.sqm.service.IOrderRecordService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLei on 2019/4/10 0010 22:22
 */
@Service("orderRecordService")
public class OrderRecordServiceImpl implements IOrderRecordService {

    private static Logger LOGGER = LogManager.getLogger(OrderRecordServiceImpl.class);

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    /**
     * 保存订单记录
     *
     * @param orderRecord 订单记录对象
     */
    @Override
    public void saveOrderRecord(OrderRecord orderRecord) {
        if(orderRecord == null){
            return;
        }
        // 保存订单信息
        orderRecordMapper.insert(orderRecord);
        // 保存商品信息
        List<OrderGoods> ogList = orderRecord.getOrderGoodsList();
        if(ogList == null || ogList.isEmpty()){
            return;
        }
        for(OrderGoods og : ogList){
            orderGoodsMapper.insert(og);
        }
    }

    /**
     * 保存商品记录
     *
     * @param orderGoods 商品记录对象
     */
    @Override
    public void saveOrderGoods(OrderGoods orderGoods) {
        if(orderGoods == null){
            return;
        }
        orderGoodsMapper.insert(orderGoods);
    }

    /**
     * 修改订单记录
     *
     * @param orderRecord 订单记录对象
     */
    @Override
    public void updateOrderRecord(OrderRecord orderRecord) {
        if(orderRecord == null){
            return;
        }
        orderRecordMapper.updateByPrimaryKey(orderRecord);
    }

    /**
     * 根据订单编号和订单平台删除订单记录
     *
     * @param orderId  订单编号
     * @param platform 订单平台
     */
    @Override
    public void deleteOrderRecordByOrderIdAndPlatform(String orderId, int platform) {
        OrderRecord or = orderRecordMapper.selectOrderRecordByOrderIdAndPlatform(orderId, platform);
        if(or != null){
            List<OrderGoods> ogList = orderGoodsMapper.selectOrderGoodsListByOrderRecordId(or.getId());
            if(ogList != null && !ogList.isEmpty()){
                for(OrderGoods og: ogList){
                    orderGoodsMapper.deleteByPrimaryKey(og.getId());
                }
            }
            orderRecordMapper.deleteByPrimaryKey(or.getId());
        }
    }

    /**
     * 根据订单编号和订单平台查询订单记录
     *
     * @param orderId  订单编号
     * @param platform 订单平台
     */
    @Override
    public OrderRecord queryOrderRecordByOrderIdAndPlatform(String orderId, int platform) {
        return orderRecordMapper.selectOrderRecordByOrderIdAndPlatform(orderId, platform);
    }

    /**
     * 根据微信用户标识和订单状态查询订单记录总数
     *
     * @param wxUserId 微信用户标识
     * @param status  订单状态
     * @return
     */
    @Override
    public int queryOrderRecordSumByWxUserIdAndStatus(Long wxUserId, int status) {
        return orderRecordMapper.selectOrderRecordSumByWxUserIdAndStatus(wxUserId, status);
    }

    /**
     * 根据微信用户标识和订单状态查询订单记录集合
     *
     * @param wxUserId 微信用户标识
     * @param status  订单类型
     * @return
     */
    @Override
    public List<OrderRecord> queryOrderRecordListByWxUserIdAndStatus(Long wxUserId, String status) {
        List<OrderRecord> orList = null;
        if(StringUtils.isBlank(status)){// 查询全部订单
            orList = orderRecordMapper.selectOrderRecordListByWxUserIdAndStatus(wxUserId, 0);
        }else if("dfk".equals(status)){// 待付款订单
            orList = orderRecordMapper.selectOrderRecordListByWxUserIdAndStatus(wxUserId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk")));
        }else if("yqr".equals(status)){// 已确认订单
            orList = orderRecordMapper.selectConfirmedOrderList(wxUserId,
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")),
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")),
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct")),
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")),
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")),
                    Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        }else if("drz".equals(status) || "ywc".equals(status)){// 待入账/已完成订单
            orList = orderRecordMapper.selectOrderRecordListByWxUserIdAndStatus(wxUserId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
        }else if("yrz".equals(status)){// 已入账订单
            orList = orderRecordMapper.selectOrderRecordListByWxUserIdAndStatus(wxUserId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")));
        }else if("wx".equals(status)){// 无效订单
            List<Integer> statusList = new ArrayList<>();
            statusList.add(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")));
            statusList.add(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")));
            orList = orderRecordMapper.selectOrderRecordListByWxUserIdAndMultiStatus(wxUserId, statusList);
        }

        if(orList != null && !orList.isEmpty()){
            for(OrderRecord or : orList){
                List<OrderGoods> ogList = orderGoodsMapper.selectOrderGoodsListByOrderRecordId(or.getId());
                or.setOrderGoodsList(ogList);
            }
        }
        return orList;
    }

    /**
     * 根据微信用户标识查询预估返现总和
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    @Override
    public double queryEstimateReturnMoneySumByWxUserId(Long wxUserId) {
        Double sum = orderRecordMapper.selectEstimateReturnMoneySum(wxUserId,
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")),
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")),
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct")),
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")),
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")),
                Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        if(sum == null){
            return 0.0;
        }
        return sum;
    }

    /**
     * 根据微信用户标识和订单状态查询返现总和
     *
     * @param wxUserId 微信用户标识
     * @param status   订单状态
     * @return
     */
    @Override
    public double queryReturnMoneySumByWxUserIdAndStatus(Long wxUserId, int status) {
        Double sum = orderRecordMapper.selectReturnMoneySumByWxUserIdAndStatus(wxUserId, status);
        if(sum == null){
            return 0.0;
        }
        return sum;
    }

    /**
     * 根据订单记录标识查询订单关联的商品集合
     *
     * @param orderRecordId 订单记录标识
     * @return
     */
    @Override
    public List<OrderGoods> queryOrderGoodsListByOrderId(String orderRecordId) {
        if(StringUtils.isBlank(orderRecordId)){
            return null;
        }
        return orderGoodsMapper.selectOrderGoodsListByOrderRecordId(orderRecordId);
    }

    /**
     * 根据订单记录标识查询订单记录
     *
     * @param id 订单记录标识
     */
    @Override
    public OrderRecord queryOrderRecordById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        return orderRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据条件查询推广订单记录（分页）
     *
     * @param wxUserId   微信用户标识
     * @param orderId    订单编号
     * @param platform   订单平台
     * @param status     订单状态
     * @param orderField 排序字段
     * @param pageNo     页号
     * @param pageSize   每页显示记录数
     * @return
     */
    @Override
    public Page<OrderRecord> queryOrderRecordListByConditionPage(Long wxUserId, String orderId, Integer platform, Integer status, String orderField, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return orderRecordMapper.selectOrderRecordListByConditionPage(wxUserId, orderId, platform, status, orderField);
    }

    /**
     * 根据订单状态、订单平台查询完成时间小于或等于传入时间的订单集合
     *
     * @param finishTimeStr 完成时间
     * @param status        订单状态
     * @param platform      订单平台
     * @return
     */
    @Override
    public List<OrderRecord> queryOrderRecordListByFinishTimeAndStatusAndPlatform(String finishTimeStr, Integer status, Integer platform) {
        return orderRecordMapper.selectOrderRecordListByFinishTimeAndStatusAndPlatform(finishTimeStr, status, platform);
    }
}
