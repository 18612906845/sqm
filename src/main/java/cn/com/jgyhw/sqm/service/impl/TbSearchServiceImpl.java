package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.*;
import cn.com.jgyhw.sqm.pojo.TbGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkItemInfoGetRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import com.taobao.api.response.TbkItemInfoGetResponse;
import com.taobao.api.response.TbkTpwdCreateResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by WangLei on 2019/5/20 0020 17:33
 */
@Service("tbSearchService")
public class TbSearchServiceImpl extends ObjectTransitionUtil implements ITbSearchService {

    private static Logger LOGGER = LogManager.getLogger(TbSearchServiceImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private IOrderRecordService orderRecordService;

    @Autowired
    private ITbOrderBindingRecordService tbOrderBindingRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    private TaobaoClient client;// 淘宝请求对象


    /**
     * 根据物料编号查询淘宝推荐商品（同联盟APP好券直播）
     *
     * @param req 查询条件
     * @return
     */
    @Override
    public TbGoodsDiscountsQueryResultPojo queryTbGoodsTicketByMaterialId(TbkDgOptimusMaterialRequest req) {
        this.getTaobaoClient();

        TbGoodsDiscountsQueryResultPojo tgdqrp = new TbGoodsDiscountsQueryResultPojo();

        tgdqrp.setStatus(false);

        // 根据页号和每页长度计算是否达到做大页
        if(req.getPageSize() * req.getPageNo() < tgdqrp.getTotal()){
            tgdqrp.setMore(true);
        }else{
            tgdqrp.setMore(false);
            tgdqrp.setStatus(true);
            return tgdqrp;
        }

        req.setAdzoneId(Long.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_default_adzone_id")));
        TbkDgOptimusMaterialResponse rsp = null;
        try {
            rsp = client.execute(req);

            if(StringUtils.isBlank(rsp.getErrorCode())){// 调用成功
                List<TbkDgOptimusMaterialResponse.MapData> resultMapList =  rsp.getResultList();
                if(resultMapList != null && !resultMapList.isEmpty()){
                    for (TbkDgOptimusMaterialResponse.MapData mapData : resultMapList){
                        TbGoodsDiscounts tgd = this.tbMapDataToTbGoodsDiscounts(mapData);
                        if(tgd == null){
                            continue;
                        }
                        tgd.setGoodsClassify(req.getMaterialId().toString());
                        tgdqrp.getTbGoodsDiscountsList().add(tgd);
                    }
                    tgdqrp.setStatus(true);
                }
            }

        } catch (ApiException e) {
            LOGGER.error("根据物料编号查询淘宝推荐商品异常：", e);
        }
        return tgdqrp;
    }

    /**
     * 根据关键词查询淘宝推荐商品
     *
     * @param req 查询条件
     * @return
     */
    @Override
    public TbGoodsDiscountsQueryResultPojo queryTbGoodsTicketByKeyword(TbkDgMaterialOptionalRequest req) {
        this.getTaobaoClient();

        TbGoodsDiscountsQueryResultPojo tgdqrp = new TbGoodsDiscountsQueryResultPojo();

        tgdqrp.setStatus(false);

        req.setAdzoneId(Long.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_default_adzone_id")));
        req.setMaterialId(6707L);
        TbkDgMaterialOptionalResponse rsp = null;
        try {
            rsp = client.execute(req);

            if(StringUtils.isBlank(rsp.getErrorCode())) {// 调用成功
                tgdqrp.setTotal(rsp.getTotalResults());

                if(req.getPageNo() * req.getPageSize() >= rsp.getTotalResults()){
                    tgdqrp.setMore(false);
                }
                List<TbkDgMaterialOptionalResponse.MapData> resultMapList =  rsp.getResultList();
                if(resultMapList != null && !resultMapList.isEmpty()) {
                    for(TbkDgMaterialOptionalResponse.MapData mapData : resultMapList){
                        TbGoodsDiscounts tgd = this.tbMapDataToTbGoodsDiscounts(mapData);
                        if(tgd == null){
                            continue;
                        }
                        tgd.setGoodsClassify(req.getMaterialId().toString());
                        tgdqrp.getTbGoodsDiscountsList().add(tgd);
                    }
                    tgdqrp.setStatus(true);
                }
            }
        } catch (ApiException e) {
            LOGGER.error("根据关键词查询淘宝推荐商品异常：", e);
        }
        return tgdqrp;
    }

    /**
     * 根据条件查询淘宝优惠商品淘口令
     *
     * @param req 查询条件
     * @return
     */
    @Override
    public String queryTbGoodsCommand(TbkTpwdCreateRequest req) {
        this.getTaobaoClient();

        String goodsCommand = null;

        TbkTpwdCreateResponse rsp = null;
        try {
            rsp = client.execute(req);

            if(StringUtils.isBlank(rsp.getErrorCode())) {// 调用成功
                TbkTpwdCreateResponse.MapData mapData = rsp.getData();
                goodsCommand = mapData.getModel();
            }
        } catch (ApiException e) {
            LOGGER.error("根据条件查询淘宝优惠商品淘口令异常：", e);
        }
        return goodsCommand;
    }

    /**
     * 根据商品编号查询淘宝优惠商品详情
     *
     * @param goodsId 商品编号
     * @return
     */
    @Override
    public TbGoodsDiscounts queryTbGoodsDiscountsByGoodsId(Long goodsId) {

        LOGGER.info("根据商品编号查询淘宝优惠商品详情，参数，商品编号：" + goodsId);

        TbGoodsDiscounts tgd = null;

        this.getTaobaoClient();

        // 查询商品页面地址
        String goodsPageUrl = null;
        TbkItemInfoGetRequest tiigReq = new TbkItemInfoGetRequest();
        tiigReq.setNumIids(String.valueOf(goodsId));

        try {
            TbkItemInfoGetResponse tiigRsp = client.execute(tiigReq);

            if(StringUtils.isBlank(tiigRsp.getErrorCode())) {// 调用成功
                List<TbkItemInfoGetResponse.NTbkItem> nTbkItemList = tiigRsp.getResults();
                if(nTbkItemList != null && !nTbkItemList.isEmpty()){
                    TbkItemInfoGetResponse.NTbkItem nTbkItem = nTbkItemList.get(0);
                    goodsPageUrl = nTbkItem.getItemUrl();
                }
            }
            LOGGER.info("根据商品编号查询淘宝优惠商品详情，简版信息商品页面地址：" + goodsPageUrl);
            if(StringUtils.isBlank(goodsPageUrl)){
                return tgd;
            }

            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
            req.setAdzoneId(Long.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_default_adzone_id")));
            req.setMaterialId(6707L);
            req.setQ(goodsPageUrl);

            TbkDgMaterialOptionalResponse rsp = client.execute(req);

            if(StringUtils.isBlank(rsp.getErrorCode())) {// 调用成功
                List<TbkDgMaterialOptionalResponse.MapData> mapDataList = rsp.getResultList();
                LOGGER.info("根据商品编号查询淘宝优惠商品详情，查询到" + rsp.getTotalResults() + "条数据");
                if(mapDataList != null && !mapDataList.isEmpty()){
                    tgd = this.tbMapDataToTbGoodsDiscounts(mapDataList.get(0));
                }
            }
            LOGGER.info("根据商品编号查询淘宝优惠商品详情，完整版商品信息：" + (tgd == null ? "未查询到" : "有查询到"));
        } catch (ApiException e) {
            LOGGER.error("根据商品编号查询淘宝优惠商品详情异常：", e);
        }
        return tgd;
    }

    /**
     * 更新淘宝订单信息
     *
     * @param queryTimeStr  订单查询开始时间，例如：2016-05-23 12:18:22
     * @param queryTimeType 订单查询类型，创建时间“create_time”，或结算时间“settle_time”。当查询渠道或会员运营订单时，建议入参创建时间“create_time”进行查询
     * @param span          订单查询时间范围，单位：秒，最小60，最大1200，如不填写，默认60。查询常规订单、三方订单、渠道，及会员订单时均需要设置此参数，直接通过设置page_size,page_no 翻页查询数据即可
     * @param pageNum       页码
     * @param pageSize      每页条数
     */
    @Transactional
    @Override
    public TimingTaskQueryResultPojo updateTbOrderInfoByTime(String queryTimeStr, String queryTimeType, Long span, int pageNum, int pageSize) {
        TimingTaskQueryResultPojo ttqrp = new TimingTaskQueryResultPojo();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_search_url_other");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("AP_KEY", configurationPropertiesSqm.getTB_OTHER_API_AP_KEY());
            try {
                String timeUrlEncode = URLEncoder.encode(queryTimeStr, "UTF-8");
                LOGGER.info("查询时间urlencode编码结果：" + timeUrlEncode);
                url = url.replaceAll("START_TIME", timeUrlEncode);
            } catch (UnsupportedEncodingException e) {
                LOGGER.info("更新淘宝订单信息，查询时间urlencode编码异常", e);
                ttqrp.setStatus(true);
                ttqrp.setMore(false);
                return ttqrp;
            }
            url = url.replaceAll("SPAN", span.toString());
            url = url.replaceAll("PAGE_NUM", pageNum + "");
            url = url.replaceAll("PAGE_SIZE", pageSize + "");
            url = url.replaceAll("TK_STATUS", "1");
            url = url.replaceAll("ORDER_TYPE", queryTimeType);
            url = url.replaceAll("TB_NAME", configurationPropertiesSqm.getTB_USER_NAME());
            url = url.replaceAll("INFO_EXT", "1");
        }
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;

        try {
            response = httpClient.execute(httpGet);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("请求更新淘宝订单信息结果--> " + respContent);
                JSONObject jsonObject = JSONObject.parseObject(respContent);

                Integer code = jsonObject.getInteger("code");
                if(code == 200){// 请求成功并且有值
                    JSONObject dataJson = jsonObject.getJSONObject("data");
                    Long total = dataJson.getLong("totalcounts");

                    JSONArray orderJsonArray = new JSONArray();
                    if(total == 1){
                        JSONObject orderJson = dataJson.getJSONObject("n_tbk_order");
                        orderJsonArray.add(orderJson);
                    }else if(total > 1){
                        orderJsonArray = dataJson.getJSONArray("n_tbk_order");
                    }
                    for(int i=0; i<orderJsonArray.size(); i++){
                        JSONObject orderJson = orderJsonArray.getJSONObject(i);
                        int status = orderJson.getInteger("tk_status");
                        if(status == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_status_yfk"))){// 订单已付款
                            finishPayOrderDispose(orderJson);
                        }else if(status == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_status_ysx"))){// 订单已失效
                            invalidOrderDispose(orderJson);
                        }else if(status == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_status_ycg"))){// 订单已成功
                            finishOrderDispose(orderJson);
                        }
                    }
                    if(pageNum * pageSize >= total){
                        ttqrp.setMore(false);
                    }else{
                        ttqrp.setMore(true);
                    }
                    ttqrp.setStatus(true);
                }else if(code == -1){// 请求成功，但是没有值
                    ttqrp.setStatus(true);
                    ttqrp.setMore(false);
                }
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("请求更新淘宝订单信息异常--> ", e);
            ttqrp.setStatus(false);
            ttqrp.setMore(true);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求更新淘宝订单信息异常--> ", e);
            }
        }
        return ttqrp;
    }


    /**
     * 无效订单处理
     *
     * @param orJson 订单JSON对象
     */
    private void invalidOrderDispose(JSONObject orJson){
        String orderId = orJson.getString("trade_id");
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        if(or == null){//新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")));
            createOrderRecordByOrderJson(or, orJson);
            LOGGER.info("淘宝无效-无效订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 更改状态
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")));
            updateOrderRecordByOrderJson(or, orJson);
            LOGGER.info("淘宝无效-无效订单处理--> 有订单记录，更新订单，编号：" + orderId);
        }
    }

    /**
     * 已付款订单处理
     *
     * @param orJson 订单JSON对象
     */
    private void finishPayOrderDispose(JSONObject orJson){
        String orderId = orJson.getString("trade_id");
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")));
            createOrderRecordByOrderJson(or, orJson);
            LOGGER.info("淘宝已支付订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }
    }

    /**
     * 已完成订单处理
     *
     * @param orJson 订单JSON对象
     */
    private void finishOrderDispose(JSONObject orJson){
        String orderId = orJson.getString("trade_id");
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(orderId, Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
            or.setFinishTime(new Date());
            createOrderRecordByOrderJson(or, orJson);
            LOGGER.info("淘宝已完成订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{
            Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
            Date finishTime = new Date();
            if(or.getFinishTime() == null){
                or.setFinishTime(finishTime);
            }
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
            updateOrderRecordByOrderJson(or, orJson);

            // 如果修改之前订单状态已经是已完成，则不发送消息
            if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc"))){
                // 判断用户是否关注公众号
                WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                    this.sendFinishOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), finishTime);
                }
            }
            LOGGER.info("淘宝已完成订单处理--> 有订单记录，更新订单，编号：" + orderId);

            // 判断完成时间是否已经有8天了
            Long finishTimeLong = or.getFinishTime().getTime();
            Long unfreezeTimeLong = new Date().getTime() - 1000*60*60*24*8;
            if(finishTimeLong >= unfreezeTimeLong){// 执行入账操作
                unfreezeOrder(or, orJson);
            }
        }
    }

    /**
     * 新建淘宝订单
     *
     * @param or 订单数据库对象
     * @param orJson 订单JSON对象
     */
    private void createOrderRecordByOrderJson(OrderRecord or, JSONObject orJson){
        String orderRecordId = UUID.randomUUID().toString();
        String orderId = orJson.getString("trade_id");

        or.setId(orderRecordId);
        or.setOrderId(orderId);
        String createTime = orJson.getString("create_time");
        or.setOrderTime(this.stringToDateByFormat(createTime, null));
        or.setPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));

        Integer returnScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"));
        // 获取微信用户标识
        TbOrderBindingRecord tobr = tbOrderBindingRecordService.queryTbOrderBindingRecordByTbOrderId(orderId);
        if(tobr != null && tobr.getStatus() == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_wbd"))){// 淘宝订单绑定记录申请不为空，且绑定记录为未绑定
            or.setWxUserId(tobr.getWxUserId());
            // 获取用户返现比例
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());

            if(wu != null && wu.getReturnMoneyShareWq() > 0){
                returnScale = wu.getReturnMoneyShareWq();
            }

            tobr.setStatus(Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_ybd")));
            tbOrderBindingRecordService.updateTbOrderBindingRecord(tobr);

            // 判断用户是否关注公众号, 发送订单确认通知
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已绑定");
            }
        }

        // 设置联盟佣金
        or.setCommission(orJson.getDouble("pub_share_pre_fee"));
        // 设置返现金额
        or.setReturnMoney(this.rebateCompute(or.getCommission(), returnScale));

        // 设置订单关联商品信息
        List<OrderGoods> ogList = new ArrayList<>();
        OrderGoods og = new OrderGoods();
        og.setId(UUID.randomUUID().toString());
        String goodsId = orJson.getString("num_iid");
        og.setCode(goodsId);
        og.setName(orJson.getString("item_title"));
        og.setImageUrl(orJson.getString("pict_url"));
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
     * 修改淘宝订单
     *
     * @param or 订单数据库对象
     * @param orJson 订单JSON对象
     */
    private void updateOrderRecordByOrderJson(OrderRecord or, JSONObject orJson){
        Integer returnScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
        // 查询当前订单商品集合
        List<OrderGoods> ogList = orderRecordService.queryOrderGoodsListByOrderId(or.getId());
        if(ogList != null && !ogList.isEmpty()){
            OrderGoods og = ogList.get(0);
            returnScale = og.getReturnScale();
        }
        // 设置联盟佣金
        or.setCommission(orJson.getDouble("pub_share_pre_fee"));
        // 设置返现金额
        or.setReturnMoney(this.rebateCompute(or.getCommission(), returnScale));
        or.setUpdateTime(new Date());
        orderRecordService.updateOrderRecord(or);
    }

    /**
     * 解冻订单
     *
     * @param or 订单数据库对象
     */
    private void unfreezeOrder(OrderRecord or,  JSONObject orJson){
        if(or == null){
            LOGGER.warn("淘宝订单结算，数据库无记录");
            return;
        }
        // 根据订单状态判断订单是否可以结算
        if(or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")) ||
                or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")) ||
                or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx"))){
            return;
        }
        // 更改淘宝订单状态为已入账
        or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")));

        // 更新订单到数据库
        updateOrderRecordByOrderJson(or, orJson);

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

    /**
     * 获取淘宝请求对象
     */
    private void getTaobaoClient(){
        if(client == null){
            client = new DefaultTaobaoClient(ApplicationParamConstant.TB_PARAM_MAP.get("tb_server_api_url"), configurationPropertiesSqm.getTB_APP_KEY(), configurationPropertiesSqm.getTB_APP_SECRET());
        }
    }
}
