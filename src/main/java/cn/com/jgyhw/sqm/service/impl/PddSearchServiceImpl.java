package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.*;
import cn.com.jgyhw.sqm.pojo.PddDdkGoodsPromotionUrlGenerateRequestPojo;
import cn.com.jgyhw.sqm.pojo.PddDdkGoodsSearchRequestPojo;
import cn.com.jgyhw.sqm.pojo.PddGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.pdd.pop.sdk.http.PopClient;
import com.pdd.pop.sdk.http.PopHttpClient;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkOrderDetailGetRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkOrderListIncrementGetRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsSearchResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkOrderDetailGetResponse;
import com.pdd.pop.sdk.http.api.response.PddDdkOrderListIncrementGetResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by WangLei on 2019/5/2 0002 13:08
 */
@Service("pddSearchService")
public class PddSearchServiceImpl extends ObjectTransitionUtil implements IPddSearchService {

    private static Logger LOGGER = LogManager.getLogger(PddSearchServiceImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    @Autowired
    private IOrderRecordService orderRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    /**
     * 关键词/商品编号/商品分类查询拼多多商品优惠
     *
     * @param pddDdkGoodsSearchRequestPojo 查询参数
     * @param isSearch                     是否是人为搜索
     * @return
     */
    @Override
    public PddGoodsDiscountsQueryResultPojo queryPddGoodsDiscounts(PddDdkGoodsSearchRequestPojo pddDdkGoodsSearchRequestPojo, boolean isSearch) {
        LOGGER.info("关键词/商品编号/商品分类查询拼多多商品优惠，关键词：" + pddDdkGoodsSearchRequestPojo.getKeyword());

        String clientId = configurationPropertiesSqm.getPDD_CLIENT_ID();
        String clientSecret = configurationPropertiesSqm.getPDD_CLIENT_SECRET();
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddDdkGoodsSearchRequest pdgsr = this.pddDdkGoodsSearchRequestPojoToPddDdkGoodsSearchRequest(pddDdkGoodsSearchRequestPojo);

        PddGoodsDiscountsQueryResultPojo pgdqrp = null;

        if(pdgsr == null){
            return pgdqrp;
        }

        try {
            PddDdkGoodsSearchResponse response = client.syncInvoke(pdgsr);
            if(response != null && response.getErrorResponse() == null){
                Integer totalCount = response.getGoodsSearchResponse().getTotalCount();
                LOGGER.info("关键词/商品编号/商品分类查询拼多多商品优惠，商品总数：" + totalCount);
                List<PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem> goodsList =  response.getGoodsSearchResponse().getGoodsList();
                List<PddGoodsDiscounts> pgdList = new ArrayList<>();
                for(PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item : goodsList){

                    PddGoodsDiscounts pgd = this.goodsSearchResponseGoodsListItemToPddGoodsDiscounts(item);
                    if(pgd == null){
                        continue;
                    }
                    // 保存拼多多商品优惠对象
                    PddGoodsDiscounts pgdOld = pddGoodsDiscountsService.queryPddGoodsDiscountsByGoodsId(pgd.getGoodsId());
                    if(pgdOld == null){// 新增
                        pgd.setId(UUID.randomUUID().toString());
                        pgd.setUpdateTime(new Date());

                        // 根据查询商品类目编号，设置查询类型
                        Long optId = pddDdkGoodsSearchRequestPojo.getOptId();

                        if(isSearch){// 人为搜索
                            pgd.setGoodsClassify(ApplicationParamConstant.PDD_PARAM_MAP.get("query_type_ptss"));
                        }else {
                            pgd.setGoodsClassify(String.valueOf(optId));
                        }

                        pddGoodsDiscountsService.savePddGoodsDiscounts(pgd);
                    }else{// 修改
                        String id = pgdOld.getId();
                        String goodsClassify = pgdOld.getGoodsClassify();
                        BeanCopier copier = BeanCopier.create(PddGoodsDiscounts.class, PddGoodsDiscounts.class, false);
                        copier.copy(pgd, pgdOld, null);
                        pgdOld.setId(id);
                        pgdOld.setGoodsClassify(goodsClassify);
                        pgdOld.setUpdateTime(new Date());
                        pddGoodsDiscountsService.updatePddGoodsDiscounts(pgdOld);
                    }
                    pgdList.add(pgd);
                }
                pgdqrp = new PddGoodsDiscountsQueryResultPojo();
                pgdqrp.setPddGoodsDiscountsList(pgdList);
                pgdqrp.setTotalCount(totalCount);

                // 判断是否还有更多
                long count = pddDdkGoodsSearchRequestPojo.getPage() * pddDdkGoodsSearchRequestPojo.getPageSize();
                if(count >= totalCount){
                    pgdqrp.setMore(false);
                }
                pgdqrp.setStatus(true);
            }else if(response.getErrorResponse() != null){
                if(pgdqrp == null){
                    pgdqrp = new PddGoodsDiscountsQueryResultPojo();
                }
                pgdqrp.setStatus(false);
                LOGGER.error("关键词/商品编号/商品分类查询拼多多商品优惠错误，错误编号：" + response.getErrorResponse().getErrorCode() + "；错误描述：" + response.getErrorResponse().getErrorMsg());
            }

        } catch (Exception e) {
            if(pgdqrp == null){
                pgdqrp = new PddGoodsDiscountsQueryResultPojo();
            }
            pgdqrp.setStatus(false);
            LOGGER.error("关键词/商品编号/商品分类查询拼多多商品优惠异常", e);
        }
        return pgdqrp;
    }

    /**
     * 根据参数查询拼多多商品推广链接
     *
     * @param pddDdkGoodsPromotionUrlGenerateRequestPojo 查询参数
     * @return
     */
    @Override
    public PddDdkGoodsPromotionUrlGenerateResponse queryPddCpsUrl(PddDdkGoodsPromotionUrlGenerateRequestPojo pddDdkGoodsPromotionUrlGenerateRequestPojo) {
        String clientId = configurationPropertiesSqm.getPDD_CLIENT_ID();
        String clientSecret = configurationPropertiesSqm.getPDD_CLIENT_SECRET();
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddDdkGoodsPromotionUrlGenerateRequest request = this.pddDdkGoodsPromotionUrlGenerateRequestPojoToPddDdkGoodsPromotionUrlGenerateRequest(pddDdkGoodsPromotionUrlGenerateRequestPojo);

        PddDdkGoodsPromotionUrlGenerateResponse response = null;
        try {
            response = client.syncInvoke(request);
            return response;
        } catch (Exception e) {
            LOGGER.error("根据参数查询拼多多商品推广链接异常", e);
        }
        return response;
    }

    /**
     * 更新拼多多订单信息
     *
     * @param startUpdateTime 查询时间开始，毫秒值
     * @param endUpdateTime   查询时间结束，相差不能大于24小时
     * @param pageNum         页码
     * @param pageSize        每页条数
     * @return hasMore true：还有下一页，false：无下一页
     */
    @Transactional
    @Override
    public TimingTaskQueryResultPojo updatePddOrderInfoByTime(Long startUpdateTime, Long endUpdateTime, int pageNum, int pageSize) {
        TimingTaskQueryResultPojo ttqrp = new TimingTaskQueryResultPojo();

        String clientId = configurationPropertiesSqm.getPDD_CLIENT_ID();
        String clientSecret = configurationPropertiesSqm.getPDD_CLIENT_SECRET();
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddDdkOrderListIncrementGetRequest request = new PddDdkOrderListIncrementGetRequest();
        request.setStartUpdateTime(startUpdateTime);
        request.setEndUpdateTime(endUpdateTime);
        request.setPageSize(pageSize);
        request.setPage(pageNum);
        request.setReturnCount(true);

        try {
            PddDdkOrderListIncrementGetResponse response = client.syncInvoke(request);

            if(response.getErrorResponse() == null){
                PddDdkOrderListIncrementGetResponse.OrderListGetResponse orderListGetResponse =  response.getOrderListGetResponse();

                List<PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem> itemList = orderListGetResponse.getOrderList();
                if(itemList != null && !itemList.isEmpty()){
                    for(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item : itemList){
                        Integer orderStatus = item.getOrderStatus();

                        if(orderStatus == Integer.valueOf(ApplicationParamConstant.PDD_PARAM_MAP.get("pdd_order_status_dfk"))){// 待付款
                            awaitPayOrderDispose(item);
                        }else if(orderStatus == Integer.valueOf(ApplicationParamConstant.PDD_PARAM_MAP.get("pdd_order_status_yfk"))){// 已付款
                            finishPayOrderDispose(item);
                        }else if(orderStatus == Integer.valueOf(ApplicationParamConstant.PDD_PARAM_MAP.get("pdd_order_status_yct"))){// 已成团
                            orderGroupSuccessDispose(item);
                        }else if(orderStatus == Integer.valueOf(ApplicationParamConstant.PDD_PARAM_MAP.get("pdd_order_status_qrsh"))){// 确认收货
                            finishOrderDispose(item);
                        }
                    }
                }

                Long totalCount = orderListGetResponse.getTotalCount();
                if((pageNum * pageSize) < totalCount){
                    ttqrp.setMore(true);
                }else{
                    ttqrp.setMore(false);
                }
                ttqrp.setStatus(true);
            }else{
                LOGGER.error("更新拼多多订单信息错误，错误编号：" + response.getErrorResponse().getErrorCode() + "；错误描述：" + response.getErrorResponse().getErrorMsg());
                ttqrp.setMore(true);
                ttqrp.setStatus(false);
            }
        } catch (Exception e) {
            ttqrp.setMore(true);
            ttqrp.setStatus(false);
            LOGGER.error("更新拼多多订单信息异常", e);
        }
        return ttqrp;
    }

    /**
     * 根据完成时间解冻订单
     *
     * @param finishTimeStr 完成时间，小于等于传入时间，即可解冻
     */
    @Transactional
    @Override
    public void unfreezePddOrderByFinishTimeStr(String finishTimeStr) {
        List<OrderRecord> orList = orderRecordService.queryOrderRecordListByFinishTimeAndStatusAndPlatform(finishTimeStr, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        if(orList == null || orList.isEmpty()){
            return;
        }

        // 循环可解冻订单，解冻
        for(OrderRecord or : orList){
            unfreezeOrder(or);
        }
    }

    /**
     * 待付款订单处理
     *
     * @param item 订单对象
     */
    private void awaitPayOrderDispose(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        String orderId = item.getOrderSn();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk")));
            createOrderRecordByOrderListGetResponseOrderListItem(or, item);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "待付款");
            }
            LOGGER.info("拼多多待支付订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }
    }

    /**
     * 已付款订单处理
     *
     * @param item 订单对象
     */
    private void finishPayOrderDispose(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        String orderId = item.getOrderSn();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")));
            createOrderRecordByOrderListGetResponseOrderListItem(or, item);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已付款");
            }
            LOGGER.info("拼多多已付款订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 修改订单
            if(or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk"))){// 若是待付款订单，则修改为已付款
                Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
                or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")));
                updateOrderRecordByOrderListGetResponseOrderListItem(or, item.getPromotionAmount());
                // 如果修改之前订单状态已经是已付款，则不发送消息
                if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk"))){
                    // 判断用户是否关注公众号
                    WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已付款");
                    }
                }
                LOGGER.info("拼多多已付款订单处理--> 有订单记录，更新订单，编号：" + orderId);
            }
        }
    }

    /**
     * 已成团订单处理
     *
     * @param item 订单对象
     */
    private void orderGroupSuccessDispose(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        String orderId = item.getOrderSn();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct")));
            createOrderRecordByOrderListGetResponseOrderListItem(or, item);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已成团");
            }
            LOGGER.info("拼多多已成团订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 修改订单
            if(or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk"))){// 若是已付款订单，则修改为已成团
                Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
                or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct")));
                updateOrderRecordByOrderListGetResponseOrderListItem(or, item.getPromotionAmount());
                // 如果修改之前订单状态已经是已成团，则不发送消息
                if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct"))){
                    // 判断用户是否关注公众号
                    WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已成团");
                    }
                }
                LOGGER.info("拼多多已成团订单处理--> 有订单记录，更新订单，编号：" + orderId);
            }
        }
    }

    /**
     * 已收货订单处理
     *
     * @param item 订单对象
     */
    private void finishOrderDispose(PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        String orderId = item.getOrderSn();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
            or.setFinishTime(new Date());
            createOrderRecordByOrderListGetResponseOrderListItem(or, item);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendFinishOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), or.getFinishTime());
            }
            LOGGER.info("拼多多已收货订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 修改订单
            Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
            Date finishTime = new Date();
            if(or.getFinishTime() == null){
                or.setFinishTime(finishTime);
            }
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
            updateOrderRecordByOrderListGetResponseOrderListItem(or, item.getPromotionAmount());
            // 如果修改之前订单状态已经是已完成，则不发送消息
            if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc"))){
                // 判断用户是否关注公众号
                WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                    this.sendFinishOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), finishTime);
                }
            }
            LOGGER.info("拼多多已收货订单处理--> 有订单记录，更新订单，编号：" + orderId);
        }
    }

    /**
     * 解冻订单
     *
     * @param or 订单数据库对象
     */
    private void unfreezeOrder(OrderRecord or){
        if(or == null){
            LOGGER.warn("拼多多订单结算，数据库无记录");
            return;
        }
        String clientId = configurationPropertiesSqm.getPDD_CLIENT_ID();
        String clientSecret = configurationPropertiesSqm.getPDD_CLIENT_SECRET();
        PopClient client = new PopHttpClient(clientId, clientSecret);

        PddDdkOrderDetailGetRequest request = new PddDdkOrderDetailGetRequest();
        request.setOrderSn(or.getOrderId());
        try {
            PddDdkOrderDetailGetResponse response = client.syncInvoke(request);
            if(response.getErrorResponse() == null){
                PddDdkOrderDetailGetResponse.OrderDetailResponse odr = response.getOrderDetailResponse();
                Integer returnStatus = odr.getReturnStatus();
                if(returnStatus != 0 && (Integer.valueOf(odr.getOrderStatusDesc()) == 8 || Integer.valueOf(odr.getOrderStatusDesc()) == 10)){
                    return;
                }
                // 更改京东订单状态为已入账
                or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")));

                // 更新订单到数据库
                updateOrderRecordByOrderListGetResponseOrderListItem(or, odr.getPromotionAmount());

                // 判断返现金额是否大于零
                if(or.getReturnMoney() <= 0){
                    return;
                }
                // 保存购买人入账信息
                Long wxUserId = or.getWxUserId();
                WxUser wu = wxUserService.queryWxUserById(wxUserId);
                Integer changeType = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_rz"));
                ReturnMoneyChangeRecord rmcr = returnMoneyChangeRecordService.queryReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(wxUserId, changeType, or.getId(), or.getOrderId());
                if(rmcr == null){
                    rmcr = new ReturnMoneyChangeRecord();
                    rmcr.setId(UUID.randomUUID().toString());
                    rmcr.setWxUserId(wxUserId);
                    rmcr.setChangeType(changeType);
                    rmcr.setTargetId(or.getId());
                    rmcr.setChangeTime(new Date());
                    rmcr.setChangeMoney(or.getReturnMoney());
                    rmcr.setOrderId(or.getOrderId());
                    returnMoneyChangeRecordService.saveReturnMoneyChangeRecord(rmcr);
                    // 发送获得返现消息，判断用户是否关注公众号
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendRebateWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), "订单（" + or.getOrderId() + "）完成审核，并已入账", rmcr.getChangeMoney());
                    }
                }
                // 保存推荐人提成入账信息
                if(wu == null){
                    return;
                }
                // 查询推荐人用户信息
                Long parentWxUserId = wu.getParentWxUserId();
                if(parentWxUserId == null){
                    return;
                }
                WxUser pwu = wxUserService.queryWxUserById(parentWxUserId);
                if(pwu == null){
                    return;
                }
                // 保存推荐人入账信息
                Integer pChangeType = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tc"));
                ReturnMoneyChangeRecord pRmcr = returnMoneyChangeRecordService.queryReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(pwu.getId(), pChangeType, String.valueOf(wxUserId), or.getOrderId());
                if(pRmcr == null){
                    pRmcr = new ReturnMoneyChangeRecord();
                    pRmcr.setId(UUID.randomUUID().toString());
                    pRmcr.setWxUserId(pwu.getId());
                    pRmcr.setChangeType(pChangeType);
                    pRmcr.setTargetId(String.valueOf(wxUserId));
                    pRmcr.setChangeTime(new Date());
                    // 计算提成金额
                    int recommendTcScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("recommend_tc_scale"));
                    if(pwu.getReturnMoneyShareTc() > 0){
                        recommendTcScale = pwu.getReturnMoneyShareTc();
                    }
                    pRmcr.setChangeMoney(this.rebateCompute(or.getCommission(), recommendTcScale));
                    pRmcr.setOrderId(or.getOrderId());
                    if(pRmcr.getChangeMoney() > 0) {// 提成大于0，保存记录并发送通知
                        returnMoneyChangeRecordService.saveReturnMoneyChangeRecord(pRmcr);
                        if(pwu != null && !StringUtils.isBlank(pwu.getOpenIdGzh())){
                            this.sendRebateWxMessage(wxGzhMessageSendService, pwu.getOpenIdGzh(), "您邀请的 " + wu.getNickName() + " 完成订单结算，提成奖励已入账", pRmcr.getChangeMoney());
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.info("拼多多解冻订单处理--> 根据订单号查询订单详情异常", e);
        }
    }

    /**
     * 创建订单记录
     *
     * @param or 订单记录
     * @param item 拼多多订单对象
     */
    private void createOrderRecordByOrderListGetResponseOrderListItem(OrderRecord or, PddDdkOrderListIncrementGetResponse.OrderListGetResponseOrderListItem item){
        String orderRecordId = UUID.randomUUID().toString();

        or.setId(orderRecordId);
        or.setOrderId(item.getOrderSn());
        or.setOrderTime(new Date(item.getOrderCreateTime()*1000));
        or.setPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));
        // 获取微信用户标识
        Long wxUserId = Long.valueOf(item.getCustomParameters());
        if(wxUserId == null){
            or.setWxUserId(Long.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_wx_user_id_default")));
        }else{
            or.setWxUserId(wxUserId);
        }
        // 获取用户返现比例
        WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());

        Integer returnScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
        if(wu != null && wu.getReturnMoneyShareYq() > 0){
            returnScale = wu.getReturnMoneyShareYq();
        }
        // 设置联盟佣金
        or.setCommission(this.fenToYuan(item.getPromotionAmount()));
        // 设置返现金额
        or.setReturnMoney(this.rebateCompute(or.getCommission(), returnScale));

        // 设置订单关联商品信息
        List<OrderGoods> ogList = new ArrayList<>();
        OrderGoods og = new OrderGoods();
        og.setId(UUID.randomUUID().toString());
        og.setCode(String.valueOf(item.getGoodsId()));
        og.setName(item.getGoodsName());
        og.setImageUrl(item.getGoodsThumbnailUrl());
        og.setOrderRecordId(orderRecordId);

        og.setReturnScale(returnScale);
        og.setCreateTime(new Date());
        ogList.add(og);

        or.setOrderGoodsList(ogList);

        or.setCreateTime(new Date());
        or.setUpdateTime(new Date());

        orderRecordService.saveOrderRecord(or);
    }

    /**
     * 修改订单记录
     *
     * @param or 订单记录
     * @param promotionAmount 拼多多订单联盟佣金
     */
    private void updateOrderRecordByOrderListGetResponseOrderListItem(OrderRecord or, Long promotionAmount){
        Integer returnScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
        // 查询当前订单商品集合
        List<OrderGoods> ogList = orderRecordService.queryOrderGoodsListByOrderId(or.getId());
        if(ogList != null && !ogList.isEmpty()){
            returnScale = ogList.get(0).getReturnScale();
        }
        // 设置联盟佣金
        or.setCommission(this.fenToYuan(promotionAmount));
        // 设置返现金额
        or.setReturnMoney(this.rebateCompute(or.getCommission(), returnScale));
        or.setUpdateTime(new Date());
        orderRecordService.updateOrderRecord(or);
    }
}
