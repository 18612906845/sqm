package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.apith.SignAndSend;
import cn.com.jgyhw.sqm.domain.*;
import cn.com.jgyhw.sqm.mapper.OrderGoodsMapper;
import cn.com.jgyhw.sqm.pojo.*;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
import jd.union.open.goods.jingfen.query.request.UnionOpenGoodsJingfenQueryRequest;
import jd.union.open.goods.jingfen.query.response.*;
import jd.union.open.goods.promotiongoodsinfo.query.request.UnionOpenGoodsPromotiongoodsinfoQueryRequest;
import jd.union.open.goods.promotiongoodsinfo.query.response.PromotionGoodsResp;
import jd.union.open.goods.promotiongoodsinfo.query.response.UnionOpenGoodsPromotiongoodsinfoQueryResponse;
import jd.union.open.order.query.request.OrderReq;
import jd.union.open.order.query.request.UnionOpenOrderQueryRequest;
import jd.union.open.order.query.response.OrderResp;
import jd.union.open.order.query.response.SkuInfo;
import jd.union.open.order.query.response.UnionOpenOrderQueryResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by WangLei on 2019/2/23 0023 11:01
 */
@Service("jdSearchService")
public class JdSearchServiceImpl extends CommonUtil implements IJdSearchService {

    private static Logger LOGGER = LogManager.getLogger(JdSearchServiceImpl.class);

    @Autowired
    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    @Autowired
    private IJdPositionService jdPositionService;

    @Autowired
    private IOrderRecordService orderRecordService;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    /**
     * 关键词/商品编号查询京东商品优惠
     *
     * @param jdGoodsDiscountsQueryParamPojo 查询参数
     * @return
     */
    @Override
    public JdGoodsDiscountsQueryResultPojo queryJdGoodsDiscounts(JdGoodsDiscountsQueryParamPojo jdGoodsDiscountsQueryParamPojo) {
        JdGoodsDiscountsQueryResultPojo jgdqrp = new JdGoodsDiscountsQueryResultPojo();

        int pageNo = jdGoodsDiscountsQueryParamPojo.getPageIndex() < 1 ? 1 :jdGoodsDiscountsQueryParamPojo.getPageIndex();
        int pageSize = jdGoodsDiscountsQueryParamPojo.getPageSize() < 1 ? 30 :jdGoodsDiscountsQueryParamPojo.getPageSize();

        StringBuffer sb = new StringBuffer(ApplicationParamConstant.JD_PARAM_MAP.get("jd_keyword_search_url_other"));
        if(sb != null){
            if(jdGoodsDiscountsQueryParamPojo.getCid1() != null){
                sb.append("cid1=" + jdGoodsDiscountsQueryParamPojo.getCid1() + "&");
            }
            if(jdGoodsDiscountsQueryParamPojo.getCid2() != null){
                sb.append("cid2=" + jdGoodsDiscountsQueryParamPojo.getCid2() + "&");
            }
            if(jdGoodsDiscountsQueryParamPojo.getCid3() != null){
                sb.append("cid3=" + jdGoodsDiscountsQueryParamPojo.getCid3() + "&");
            }
            sb.append("pageIndex=" + pageNo + "&");
            sb.append("pageSize=" + pageSize + "&");
            if(!StringUtils.isBlank(jdGoodsDiscountsQueryParamPojo.getSkuIds())){
                sb.append("skuIds=" + jdGoodsDiscountsQueryParamPojo.getSkuIds() + "&");
            }
            if(!StringUtils.isBlank(jdGoodsDiscountsQueryParamPojo.getKeyword())){
                sb.append("keyword=" + jdGoodsDiscountsQueryParamPojo.getKeyword() + "&");
            }
            if(!StringUtils.isBlank(jdGoodsDiscountsQueryParamPojo.getOwner())){
                sb.append("owner=" + jdGoodsDiscountsQueryParamPojo.getOwner() + "&");
            }
            if(!StringUtils.isBlank(jdGoodsDiscountsQueryParamPojo.getSortName())){
                sb.append("sortName=" + jdGoodsDiscountsQueryParamPojo.getSortName() + "&");
            }
            if(!StringUtils.isBlank(jdGoodsDiscountsQueryParamPojo.getSort())){
                sb.append("sort=" + jdGoodsDiscountsQueryParamPojo.getSort() + "&");
            }
            if(jdGoodsDiscountsQueryParamPojo.getIsCoupon() != 0){
                sb.append("isCoupon=" + jdGoodsDiscountsQueryParamPojo.getIsCoupon() + "&");
            }
            if(jdGoodsDiscountsQueryParamPojo.getIsPG() != 0){
                sb.append("isPG=" + jdGoodsDiscountsQueryParamPojo.getIsPG() + "&");
            }
            if(jdGoodsDiscountsQueryParamPojo.getIsHot() != 0){
                sb.append("isHot=" + jdGoodsDiscountsQueryParamPojo.getIsHot() + "&");
            }
        }
        LOGGER.info("关键词/商品编号查询京东商品优惠--> url：" + sb.toString());

        String url = sb.toString();
        String secretId = configurationPropertiesSqm.getAPITH_SECRET_ID();
        String secretKey = configurationPropertiesSqm.getAPITH_SECRET_KEY();

        String respContent = SignAndSend.sendGet(url, secretId, secretKey);
        LOGGER.info("关键词/商品编号查询京东商品优惠结果--开始");
        LOGGER.info(respContent);
        LOGGER.info("关键词/商品编号查询京东商品优惠结果--结束");

        if(StringUtils.isBlank(respContent)){// 请求异常
            jgdqrp.setStatus(false);
        }else{
            JSONObject jsonObject = JSONObject.parseObject(respContent);

            jgdqrp.setJdGoodsDiscountsList(jsonObjectToJdGoodsDiscounts(jsonObject));

            if(jsonObject.getInteger("code") == 1){
                // 判断是否还有更多
                long count = pageNo * pageSize;
                long totalCount = jsonObject.getLong("totalCount");
                if(count >= totalCount){
                    jgdqrp.setMore(false);
                }
                jgdqrp.setTotalCount(totalCount);
            }else{
                jgdqrp.setMore(false);
            }
        }

        return jgdqrp;
    }


    /**
     * 同步京粉商品优惠
     *
     * @param jfGoodsReq 查询条件
     * @return
     */
    @Override
    public JdGoodsDiscountsQueryResultPojo syncJdJingFenGoodsDiscounts(JFGoodsReq jfGoodsReq) {

        JdGoodsDiscountsQueryResultPojo jgdqrp = new JdGoodsDiscountsQueryResultPojo();

        String SERVER_URL = ApplicationParamConstant.JD_PARAM_MAP.get("jd_server_api_url");
        String appKey = configurationPropertiesSqm.getJD_APP_KEY();
        String appSecret = configurationPropertiesSqm.getJD_APP_SECRET();
        String accessToken = "";
        JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);

        UnionOpenGoodsJingfenQueryRequest request = new UnionOpenGoodsJingfenQueryRequest();

        request.setGoodsReq(jfGoodsReq);
        try {
            UnionOpenGoodsJingfenQueryResponse response = client.execute(request);
            LOGGER.info("同步京粉商品优惠--> 查询到" + response.getTotalCount() + "条商品");

            if(response.getCode() == 200){
                jgdqrp.setTotalCount(response.getTotalCount());
                jgdqrp.setJdGoodsDiscountsList(unionOpenGoodsJingfenQueryResponseToJdGoodsDiscounts(response, String.valueOf(jfGoodsReq.getEliteId())));
                // 判断是否还有更多
                int pageNo = jfGoodsReq.getPageIndex() < 1 ? 1 :jfGoodsReq.getPageIndex();
                int pageSize = jfGoodsReq.getPageSize() < 1 ? 20 :jfGoodsReq.getPageSize();
                long count = pageNo * pageSize;
                long totalCount = response.getTotalCount();
                if(count >= totalCount){
                    jgdqrp.setMore(false);
                }
                jgdqrp.setStatus(true);
            }else{
                jgdqrp.setStatus(false);
                LOGGER.warn("同步京粉商品优惠--> 错误码" + response.getCode() + "；错误描述：" + response.getMessage());
                return jgdqrp;
            }
        } catch (JdException e) {
            LOGGER.error("同步京粉商品优惠异常--> ", e);
            jgdqrp.setStatus(false);
            return jgdqrp;
        }
        return jgdqrp;
    }

    /**
     * 根据参数查询京东推广链接
     *
     * @param promotionCodeReqPojo 推广链接查询参数
     * @return
     */
    @Override
    public String queryJdCpsUrl(PromotionCodeReqPojo promotionCodeReqPojo) {
        String url = ApplicationParamConstant.JD_PARAM_MAP.get("jd_transition_cps_url_other");
        if (StringUtils.isBlank(url)) {
            return null;
        }
        url = url.replaceAll("MATERIAL_ID", promotionCodeReqPojo.getMaterialId());
        url = url.replaceAll("UNION_ID", ApplicationParamConstant.JD_PARAM_MAP.get("jd_union_id"));

        // 获取推广位id
        JdPosition jp = jdPositionService.queryJdPositionByOldFashionedAndUpdate(promotionCodeReqPojo.getLoginKey());
        if (jp == null) {
            return null;
        }
        String positionId = String.valueOf(jp.getPositionId());

        url = url.replaceAll("POSITION_ID", positionId);

        String couponUrl = promotionCodeReqPojo.getCouponUrl();
        if (!StringUtils.isBlank(couponUrl) && !"null".equals(couponUrl)) {
            url += "&couponUrl=" + couponUrl;
        }

        LOGGER.info("根据参数查询京东推广链接--> url：" + url);

        String secretId = configurationPropertiesSqm.getAPITH_SECRET_ID();
        String secretKey = configurationPropertiesSqm.getAPITH_SECRET_KEY();

        String respContent = SignAndSend.sendGet(url, secretId, secretKey);
        LOGGER.info("根据参数查询京东推广链接结果--开始");
        LOGGER.info(respContent);
        LOGGER.info("根据参数查询京东推广链接结果--结束");

        String cpsUrl = null;

        if (StringUtils.isBlank(respContent)) {// 请求异常
            LOGGER.error("根据参数查询京东推广链接异常");
            return cpsUrl;
        } else {
            JSONObject jsonObject = JSONObject.parseObject(respContent);
            String code = jsonObject.getString("code");
            if ("1".equals(code)) {
                JSONObject dataJsonObj = jsonObject.getJSONObject("data");
                cpsUrl = dataJsonObj.getString("shortURL");
            }
        }
        return cpsUrl;
    }

    /**
     * 更新京东订单信息
     *
     * @param queryTimeStr 查询时间字符串，查询时间,输入格式必须为yyyyMMddHHmm,yyyyMMddHHmmss或者yyyyMMddHH格式之一
     * @param queryTimeType 查询时间类型，1：下单时间，2：完成时间，3：更新时间
     * @param isUnfreeze   是否解冻
     * @param pageNum      页码
     * @param pageSize     每页条数
     * @return hasMore true：还有下一页，false：无下一页
     */
    @Transactional
    @Override
    public synchronized TimingTaskQueryResultPojo updateJdOrderInfoByTime(String queryTimeStr, int queryTimeType, boolean isUnfreeze, int pageNum, int pageSize) {
        LOGGER.info("更新京东订单信息Service--> 开始，参数：查询时间：" + queryTimeStr + "；查询时间类型：" + queryTimeType +  "；是否解冻：" + isUnfreeze + " ；页数：" + pageNum + "；每页条数：" + pageSize);
        TimingTaskQueryResultPojo ttqrp = new TimingTaskQueryResultPojo();

        String SERVER_URL = ApplicationParamConstant.JD_PARAM_MAP.get("jd_server_api_url");
        String appKey = configurationPropertiesSqm.getJD_APP_KEY();
        String appSecret = configurationPropertiesSqm.getJD_APP_SECRET();
        String accessToken = "";

        JdClient client=new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);

        UnionOpenOrderQueryRequest unionOpenOrderQueryRequest = new UnionOpenOrderQueryRequest();
        OrderReq orderReq = new OrderReq();
        orderReq.setPageNo(pageNum);
        orderReq.setPageSize(pageSize);
        orderReq.setType(queryTimeType);
        orderReq.setTime(queryTimeStr);
        unionOpenOrderQueryRequest.setOrderReq(orderReq);
        try {
            UnionOpenOrderQueryResponse response = client.execute(unionOpenOrderQueryRequest);
            if(response.getCode() == 200){
                if(response.getHasMore() == null){
                    LOGGER.info("更新京东订单信息Service--> 无数据");
                    ttqrp.setMore(false);
                    ttqrp.setStatus(true);
                    return ttqrp;
                }
                ttqrp.setMore(response.getHasMore());
                OrderResp[] orArray = response.getData();
                if(orArray == null || orArray.length < 1){
                    LOGGER.info("更新京东订单信息Service--> 查询到" + 0 + "条订单记录");
                    ttqrp.setStatus(true);
                    ttqrp.setMore(false);
                    return ttqrp;
                }
                LOGGER.info("更新京东订单信息Service--> 查询到" + orArray.length + "条订单记录");
                for(OrderResp or : orArray){
                    int validCode = or.getValidCode();
                    Long orderTimeLong = or.getOrderTime();
                    if(orderTimeLong < Long.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_time_restrict"))){
                        LOGGER.info("更新京东订单信息Service--> 下单时间：" + orderTimeLong + "，小于可入库历史订单时间");
                        continue;
                    }
                    if(Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_valid_code_wxcd")) == validCode){//无效-拆单
                        invalidSeparateOrderDispose(or);
                    }else if(Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_valid_code_wxqx")) == validCode){//无效-取消
                        invalidCancelOrderDispose(or);
                    }else if(Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_valid_code_dfk")) == validCode){//待付款
                        awaitPayOrderDispose(or);
                    }else if(Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_valid_code_yfk")) == validCode){//已付款
                        finishPayOrderDispose(or);
                    }else if(Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_valid_code_ywc")) == validCode){//已完成
                        finishOrderDispose(or, isUnfreeze);
                    }
                }
                LOGGER.info("更新京东订单信息Service--> 处理完成");
                ttqrp.setStatus(true);
            }else{
                LOGGER.info("更新京东订单信息Service--> 返回结果，Code：" + response.getCode() + "；message：" + response.getMessage());
            }
        }catch (Exception e){
            ttqrp.setStatus(false);
            ttqrp.setMore(true);
            LOGGER.error("更新京东订单信息Service--> 异常", e);
        }finally {
            LOGGER.info("更新京东订单信息Service--> 结束");
            return ttqrp;
        }
    }

    /**
     * 京粉商品优惠 转换 京东商品优惠集合
     *
     * @param uogjqr 京粉商品优惠
     * @param goodsClassify 商品分类
     * @return
     */
    private List<JdGoodsDiscounts> unionOpenGoodsJingfenQueryResponseToJdGoodsDiscounts(UnionOpenGoodsJingfenQueryResponse uogjqr, String goodsClassify){
        JFGoodsResp[] dataArray = uogjqr.getData();
        if(dataArray == null || dataArray.length < 1){
            return null;
        }
        List<JdGoodsDiscounts> jgdList = new ArrayList<>();
        for(int i=0; i<dataArray.length; i++){
            boolean isNew = false;
            JFGoodsResp jfgr = dataArray[i];
            if(jfgr == null){
                continue;
            }

            JdGoodsDiscounts jgd = null;
            // 查询数据库是否已存在该商品信息
            LOGGER.info("商品编号：" + jfgr.getSkuId());
            jgd = jdGoodsDiscountsService.queryJdGoodsDiscountsByGoodsId(jfgr.getSkuId());
            if(jgd == null){
                jgd = new JdGoodsDiscounts();
                isNew = true;
                jgd.setId(UUID.randomUUID().toString());
                jgd.setGoodsId(jfgr.getSkuId());
            }
            jgd.setGoodsName(jfgr.getSkuName());
            // 设置图片
            ImageInfo[] imageInfo = jfgr.getImageInfo();
            if(imageInfo != null && imageInfo.length > 0){
                UrlInfo[] imageList = imageInfo[0].getImageList();
                if(imageList != null && imageList.length > 0){
                    UrlInfo urlInfo = imageList[0];
                    String imageUrl = urlInfo.getUrl();
                    if(StringUtils.isBlank(imageUrl)){
                        jgd.setGoodsImgUrl("");
                    }else{
                        imageUrl = imageUrl.replaceAll("http://", "https://");
                        jgd.setGoodsImgUrl(imageUrl);
                    }
                }
            }
            jgd.setMaterialUrl(jfgr.getMaterialUrl());
            jgd.setOwner(jfgr.getOwner());
            jgd.setSalesVolume(jfgr.getInOrderCount30Days());
            // 设置原价
            PriceInfo[] priceInfo = jfgr.getPriceInfo();
            if(priceInfo != null && priceInfo.length > 0){
                jgd.setPrice(priceInfo[0].getPrice());
            }
            // 设置佣金/佣金比例
            CommissionInfo[] commissionInfo = jfgr.getCommissionInfo();
            if(commissionInfo != null){
                jgd.setCommission(commissionInfo[0].getCommission());
                jgd.setCommissionRate(commissionInfo[0].getCommissionShare());
            }
            // 设置拼购价格
            PinGouInfo[] pinGouInfoArray = jfgr.getPinGouInfo();
            if(pinGouInfoArray != null && pinGouInfoArray.length > 0){
                for(int p=0; p<pinGouInfoArray.length; p++){
                    PinGouInfo pgi = pinGouInfoArray[p];
                    if(pgi.getPingouTmCount() != null && pgi.getPingouTmCount() < 3){
                        jgd.setPinGouPrice(pgi.getPingouPrice());
                    }
                }
            }
            // 设置优惠券
            CouponInfo[] couponInfo = jfgr.getCouponInfo();
            if(couponInfo != null && couponInfo.length > 0){
                Coupon[] couponList = couponInfo[0].getCouponList();
                if(couponList != null && couponList.length > 0){
                    double flagPrice = jgd.getPrice();
                    if(jgd.getPinGouPrice() != null && jgd.getPinGouPrice() > 0){
                        flagPrice = jgd.getPinGouPrice();
                    }
                    for(int c=0; c<couponList.length; c++){
                        Coupon coupon = couponList[c];
                        double quota = coupon.getQuota();
                        if(flagPrice >= quota){
                            jgd.setDiscount(coupon.getDiscount());
                            jgd.setLink(coupon.getLink());
                            jgd.setDiscountQuota(quota);
                        }
                    }
                }
            }
            // 设置返现金额
            this.setReturnMoney(jgd);
            // 保存或更新京东商品优惠
            if(isNew){
                jgd.setGoodsClassify(goodsClassify);
                jgd.setUpdateTime(new Date());
                jdGoodsDiscountsService.saveJdGoodsDiscounts(jgd);
            }else{
                if(ApplicationParamConstant.JD_PARAM_MAP.get("query_type_ptss").equals(jgd.getGoodsClassify())){
                    jgd.setUpdateTime(new Date());
                }
                jdGoodsDiscountsService.updateJdGoodsDiscounts(jgd);
            }
            jgdList.add(jgd);
        }
        return jgdList;
    }

    /**
     * Json结果 转换 京东商品优惠集合
     *
     * @param jsonObject Json结果
     * @return
     */
    private List<JdGoodsDiscounts> jsonObjectToJdGoodsDiscounts(JSONObject jsonObject){
        if(jsonObject.getInteger("code") == 1){
            JSONArray dataArray = jsonObject.getJSONArray("data");
            if(dataArray == null){
                return null;
            }
            List<JdGoodsDiscounts> jgdList = new ArrayList<>();
            for(int i=0; i<dataArray.size(); i++){
                boolean isNew = false;
                JSONObject jsonObj = dataArray.getJSONObject(i);
                if(jsonObj == null){
                    continue;
                }

                JdGoodsDiscounts jgd = null;
                // 查询数据库是否已存在该商品信息
                jgd = jdGoodsDiscountsService.queryJdGoodsDiscountsByGoodsId(jsonObj.getLong("skuId"));
                if(jgd == null){
                    jgd = new JdGoodsDiscounts();
                    isNew = true;
                    jgd.setId(UUID.randomUUID().toString());
                    jgd.setGoodsId(jsonObj.getLong("skuId"));
                }
                jgd.setGoodsName(jsonObj.getString("skuName"));
                // 设置图片
                JSONObject imageInfoObj = jsonObj.getJSONObject("imageInfo");
                if(imageInfoObj != null){
                    JSONArray imageList = imageInfoObj.getJSONArray("imageList");
                    if(imageList != null && !imageList.isEmpty()){
                        JSONObject urlInfo = imageList.getJSONObject(0);
                        String imageUrl = urlInfo.getString("url");
                        if(StringUtils.isBlank(imageUrl)){
                            jgd.setGoodsImgUrl("");
                        }else{
                            imageUrl = imageUrl.replaceAll("http://", "https://");
                            jgd.setGoodsImgUrl(imageUrl);
                        }
                    }
                }
                jgd.setMaterialUrl(jsonObj.getString("materialUrl"));
                jgd.setOwner(jsonObj.getString("owner"));
                jgd.setSalesVolume(jsonObj.getLong("inOrderCount30Days"));
                // 设置原价
                JSONObject priceInfo = jsonObj.getJSONObject("priceInfo");
                if(priceInfo != null){
                    jgd.setPrice(priceInfo.getDouble("price"));
                }
                // 设置佣金/佣金比例
                JSONObject commissionInfo = jsonObj.getJSONObject("commissionInfo");
                if(commissionInfo != null){
                    jgd.setCommission(commissionInfo.getDouble("commission"));
                    jgd.setCommissionRate(commissionInfo.getDouble("commissionShare"));
                }
                // 设置拼购价格
                if(jsonObj.get("pinGouInfo") instanceof JSONArray){// 是个数组
                    JSONArray pinGouInfoList = jsonObj.getJSONArray("pinGouInfo");
                    if(pinGouInfoList != null && !pinGouInfoList.isEmpty()){
                        for(int p=0; p<pinGouInfoList.size(); p++){
                            JSONObject pinGouInfo = pinGouInfoList.getJSONObject(p);
                            Long pingouTmCount = pinGouInfo.getLong("pingouTmCount");
                            if(pingouTmCount != null && pingouTmCount < 3){
                                jgd.setPinGouPrice(pinGouInfo.getDouble("pingouPrice"));
                            }
                        }
                    }
                }
                if(jsonObj.get("pinGouInfo") instanceof JSONObject){// 是个对象
                    JSONObject pinGouInfo = jsonObj.getJSONObject("pinGouInfo");
                    if(pinGouInfo != null){
                        Long pingouTmCount = pinGouInfo.getLong("pingouTmCount");
                        if(pingouTmCount != null && pingouTmCount < 3){
                            jgd.setPinGouPrice(pinGouInfo.getDouble("pingouPrice"));
                        }
                    }
                }
                // 设置优惠券
                JSONObject couponInfo = jsonObj.getJSONObject("couponInfo");
                if(couponInfo != null){
                    JSONArray couponList = couponInfo.getJSONArray("couponList");
                    if(couponList != null && !couponList.isEmpty()){
                        double flagPrice = jgd.getPrice();
                        if(jgd.getPinGouPrice() != null && jgd.getPinGouPrice() > 0){
                            flagPrice = jgd.getPinGouPrice();
                        }
                        for(int c=0; c< couponList.size(); c++){
                            JSONObject couponJson = couponList.getJSONObject(c);
                            double quota = couponJson.getDouble("quota");
                            if(flagPrice >= quota){
                                jgd.setDiscount(couponJson.getDouble("discount"));
                                jgd.setLink(couponJson.getString("link"));
                                jgd.setDiscountQuota(quota);
                            }
                        }
                    }
                }
                // 设置返现金额
                this.setReturnMoney(jgd);
                // 保存或更新京东商品优惠
                if(isNew){
                    jgd.setGoodsClassify(ApplicationParamConstant.JD_PARAM_MAP.get("query_type_ptss"));
                    jgd.setUpdateTime(new Date());
                    jdGoodsDiscountsService.saveJdGoodsDiscounts(jgd);
                }else{
                    if(ApplicationParamConstant.JD_PARAM_MAP.get("query_type_ptss").equals(jgd.getGoodsClassify())){
                        jgd.setUpdateTime(new Date());
                    }
                    jdGoodsDiscountsService.updateJdGoodsDiscounts(jgd);
                }
                jgdList.add(jgd);
            }
            return jgdList;
        }else{
            return null;
        }
    }

    /**
     * 设置返现金额
     *
     * @param jgd
     */
    private void setReturnMoney(JdGoodsDiscounts jgd){
        // 设置返现金额
        if(jgd.getPinGouPrice() == null || jgd.getPinGouPrice() == 0){// 不可以拼购
            if(StringUtils.isBlank(jgd.getLink())){// 无券
                jgd.setReturnMoney(this.rebateCompute(jgd.getPrice(), jgd.getCommissionRate(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"))));
                jgd.setLowestPrice(jgd.getPrice());
                jgd.setLowestPriceName("京东价");
            }else{// 有券
                double actualPrice = jgd.getPrice() - jgd.getDiscount();// 券后实际价格
                jgd.setReturnMoney(this.rebateCompute(actualPrice, jgd.getCommissionRate(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"))));
                jgd.setLowestPrice(this.formatDouble(actualPrice));
                jgd.setLowestPriceName("券后价");
            }
        }else{// 可以拼购
            if(StringUtils.isBlank(jgd.getLink())){// 无券
                jgd.setReturnMoney(this.rebateCompute(jgd.getPinGouPrice(), jgd.getCommissionRate(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"))));
                jgd.setLowestPrice(jgd.getPinGouPrice());
                jgd.setLowestPriceName("拼购价");
            }else{// 有券
                double actualPrice = jgd.getPinGouPrice() - jgd.getDiscount();// 拼购券后实际价格
                jgd.setReturnMoney(this.rebateCompute(actualPrice, jgd.getCommissionRate(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"))));
                jgd.setLowestPrice(this.formatDouble(actualPrice));
                jgd.setLowestPriceName("拼购券后价");
            }
        }
    }

    /**
     * 无效-拆单订单处理
     *
     * @param orderResp 订单对象
     */
    private void invalidSeparateOrderDispose(OrderResp orderResp){
        Long orderId = orderResp.getOrderId();
        // 删除订单及订单商品信息
        orderRecordService.deleteOrderRecordByOrderIdAndPlatform(String.valueOf(orderId), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        LOGGER.info("京东无效-拆单订单处理--> 删除订单记录，编号：" + orderId + "，平台：京东");
    }

    /**
     * 无效-取消订单处理
     *
     * @param orderResp 订单对象
     */
    private void invalidCancelOrderDispose(OrderResp orderResp){
        Long orderId = orderResp.getOrderId();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(String.valueOf(orderId), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        if(or == null){//新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")));
            createOrderRecordByOrderResp(or, orderResp);
            LOGGER.info("京东无效-取消订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 更改状态
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")));
            updateOrderRecordByOrderResp(or, orderResp, false);
            LOGGER.info("京东无效-取消订单处理--> 有订单记录，更新订单，编号：" + orderId);
        }
    }

    /**
     * 待支付订单处理
     *
     * @param orderResp 订单对象
     */
    private void awaitPayOrderDispose(OrderResp orderResp){
        Long orderId = orderResp.getOrderId();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(String.valueOf(orderId), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk")));
            createOrderRecordByOrderResp(or, orderResp);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "待付款");
            }
            LOGGER.info("京东待支付订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }
    }

    /**
     * 已支付订单处理
     *
     * @param orderResp 订单对象
     */
    private void finishPayOrderDispose(OrderResp orderResp){
        Long orderId = orderResp.getOrderId();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(String.valueOf(orderId), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")));
            createOrderRecordByOrderResp(or, orderResp);
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已付款");
            }
            LOGGER.info("京东已支付订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{
            if(or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk"))){// 若是待付款订单，则修改为已付款
                Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
                or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")));
                updateOrderRecordByOrderResp(or, orderResp, false);
                // 如果修改之前订单状态已经是已付款，则不发送消息
                if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk"))) {
                    // 判断用户是否关注公众号
                    WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已付款");
                    }
                }
                LOGGER.info("京东已支付订单处理--> 有订单记录，更新订单，编号：" + orderId);
            }
        }
    }

    /**
     * 完成订单处理
     *
     * @param orderResp 订单对象
     * @param isUnfreeze 是否解冻
     */
    private void finishOrderDispose(OrderResp orderResp, boolean isUnfreeze){
        Long orderId = orderResp.getOrderId();
        // 查询订单是否存在
        OrderRecord or = orderRecordService.queryOrderRecordByOrderIdAndPlatform(String.valueOf(orderId), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));
        if(or == null){// 新建订单
            or = new OrderRecord();
            or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
            createOrderRecordByOrderResp(or, orderResp);
            or.setFinishTime(new Date(orderResp.getFinishTime()));
            // 判断用户是否关注公众号
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendFinishOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), or.getFinishTime());
            }
            LOGGER.info("京东完成订单处理--> 无订单记录，新建订单，编号：" + orderId);
        }else{// 更改状态
            or.setFinishTime(new Date(orderResp.getFinishTime()));
            if(or.getStatus() != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")) &&
                    or.getStatus() != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")) &&
                    or.getStatus() != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz"))){// 跳过取消/无效/已入账订单
                Integer updateBeforeStatus = or.getStatus(); // 修改之前订单状态
                or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")));
                updateOrderRecordByOrderResp(or, orderResp, true);
                // 如果修改之前订单状态已经是已完成，则不发送消息
                if(updateBeforeStatus != Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc"))){
                    // 判断用户是否关注公众号
                    WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
                    if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                        this.sendFinishOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), or.getFinishTime());
                    }
                }
                LOGGER.info("京东完成订单处理--> 有订单记录，更新订单，编号：" + orderId);
            }
        }
        if(isUnfreeze){
            unfreezeOrder(or, orderResp);
        }
    }

    /**
     * 订单解冻
     *
     * @param orderResp 订单对象
     */
    private void unfreezeOrder(OrderRecord or, OrderResp orderResp){
        if(or == null){
            LOGGER.warn("京东订单结算，数据库无记录");
            return;
        }
        // 根据订单状态判断订单是否可以结算
        if(or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")) ||
                or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")) ||
                or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")) ||
                or.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz"))){
            return;
        }
        // 更改京东订单状态为已入账
        or.setStatus(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")));

        // 更新订单到数据库
        updateOrderRecordByOrderResp(or, orderResp, true);

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
            if(pRmcr.getChangeMoney() > 0){// 提成大于0，保存记录并发送通知
                returnMoneyChangeRecordService.saveReturnMoneyChangeRecord(pRmcr);
                // 判断邀请人是否关注公众号
                if(pwu != null && !StringUtils.isBlank(pwu.getOpenIdGzh())){
                    this.sendRebateWxMessage(wxGzhMessageSendService, pwu.getOpenIdGzh(), "您邀请的 " + wu.getNickName() + " 完成订单结算，提成奖励已入账", pRmcr.getChangeMoney());
                }
            }
        }
    }

    /**
     * 新建京东订单
     *
     * @param orderRecord 订单数据库对象
     * @param orderResp 订单对象
     */
    private void createOrderRecordByOrderResp(OrderRecord orderRecord, OrderResp orderResp){
        //保存商品
        SkuInfo[] skuArray = orderResp.getSkuList();
        if(skuArray == null || skuArray.length < 1){
            return;
        }

        Long orderId = orderResp.getOrderId();
        String orderRecordId = UUID.randomUUID().toString();

        Double orderEstimateFeeCount = 0d;
        Double orderReturnMoneyCount = 0d;

        ReturnMoneyScalePojo rmsp = null;
        // 获取用户标识和返现比例
        Long positionId = 0L;
        for(SkuInfo si : skuArray){
            if(si.getPositionId() > 0){
                positionId = si.getPositionId();
                break;
            }
        }
        List<OrderGoods> ogList = new ArrayList<>();
        int skuArrayLength = skuArray.length;
        Long[] skuIdArray = new Long[skuArrayLength];
        for(int i=0; i<skuArrayLength; i++){
            SkuInfo si = skuArray[i];
            // 预防同订单多个同商品信息
            boolean exist = Arrays.asList(skuIdArray).contains(si.getSkuId());
            skuIdArray[i] = si.getSkuId();
            OrderGoods og = null;
            // 已存在，查询库里信息
            if(exist == false){
                og = orderGoodsMapper.selectOrderGoodsByOrderRecordIdAndCode(orderRecord.getId(), si.getSkuId().toString());
            }
            // 不存在
            if(og == null){
                og = new OrderGoods();
                og.setId(UUID.randomUUID().toString());
                og.setCode(si.getSkuId().toString());
                og.setName(si.getSkuName());
                og.setOrderRecordId(orderRecordId);
                og.setImageUrl(getGoodsImageUrlBySkuId(si.getSkuId()));
            }

            // 获取返利比例
            rmsp = getReturnMoneyScaleBySkuInfo(positionId, si.getPrice()* si.getSkuNum(), si.getEstimateCosPrice());
            og.setReturnScale(rmsp.getReturnScale());

            double estimateFee = si.getEstimateFee();
            orderEstimateFeeCount += estimateFee;
            // 计算返现
            Double returnMoney = this.rebateCompute(estimateFee, rmsp.getReturnScale());
            orderReturnMoneyCount += returnMoney;

            // 将商品添加到订单集合
            ogList.add(og);
        }
        // 设置订单
        orderRecord.setId(orderRecordId);
        orderRecord.setOrderId(orderId.toString());
        orderRecord.setOrderTime(new Date(orderResp.getOrderTime()));
        if(orderResp.getFinishTime() != 0){
            orderRecord.setFinishTime(new Date(orderResp.getFinishTime()));
        }
        orderRecord.setCommission(this.formatDouble(orderEstimateFeeCount));
        orderRecord.setReturnMoney(this.formatDouble(orderReturnMoneyCount));
        orderRecord.setPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));

        // 获取微信用户标识
        if(rmsp == null){
            orderRecord.setWxUserId(Long.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_wx_user_id_default")));
        }else{
            orderRecord.setWxUserId(rmsp.getWxUserId());
        }
        // 设置订单商品集合
        orderRecord.setOrderGoodsList(ogList);

        // 设置订单时间
        orderRecord.setCreateTime(new Date());
        orderRecord.setUpdateTime(new Date());

        // 保存订单
        orderRecordService.saveOrderRecord(orderRecord);
    }

    /**
     * 更新京东订单
     *
     * @param orderRecord 订单数据库对象
     * @param orderResp 订单对象
     * @param isActualFee 是否实际佣金计算
     */
    private void updateOrderRecordByOrderResp(OrderRecord orderRecord, OrderResp orderResp, boolean isActualFee){
        //更新商品
        SkuInfo[] skuInfoArray = orderResp.getSkuList();
        if(skuInfoArray == null || skuInfoArray.length < 1){
            return;
        }
        Double orderEstimateFeeCount = 0d;// 联盟佣金总和
        Double orderReturnMoneyCount = 0d;// 返现总和
        boolean isReturnGoods = false;// 是否退货

        int skuInfoArrayLength = skuInfoArray.length;
        Long[] skuIdArray = new Long[skuInfoArrayLength];
        for(int i=0; i<skuInfoArrayLength; i++){
            SkuInfo si = skuInfoArray[i];
            // 判断是否有退货
            if(si.getSkuReturnNum() > 0){
                isReturnGoods = true;
            }

            // 判断商品是否已计算过，是为了避免同一个商品购买多件，会出现重复计算的问题
            boolean exist = Arrays.asList(skuIdArray).contains(si.getSkuId());
            skuIdArray[i] = si.getSkuId();
            if(exist){
                continue;
            }

            // 计算联盟佣金总和
            Double estimateFee = si.getEstimateFee();
            if(isActualFee){
                estimateFee = si.getActualFee();
            }
            orderEstimateFeeCount += estimateFee;

            // 根据订单记录标识和商品编号查询订单商品返现比例
            int goodsReturnScale = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"));
            OrderGoods og = orderGoodsMapper.selectOrderGoodsByOrderRecordIdAndCode(orderRecord.getId(), si.getSkuId().toString());
            if(og != null){
                goodsReturnScale = og.getReturnScale();
            }
            // 计算返现总和
            Double returnMoney = this.rebateCompute(estimateFee, goodsReturnScale);
            orderReturnMoneyCount += returnMoney;

        }
        // 更新订单
        orderRecord.setCommission(this.formatDouble(orderEstimateFeeCount));
        orderRecord.setReturnMoney(this.formatDouble(orderReturnMoneyCount));
        orderRecord.setUpdateTime(new Date());

        // 订单入账并返现金额小于等于0
        if(orderRecord.getStatus() == Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")) && orderRecord.getReturnMoney() <= 0 && isReturnGoods){
            orderRecord.setStatus(Integer.valueOf(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx"))));// 设置订单状态为无效
        }

        orderRecordService.updateOrderRecord(orderRecord);
    }


    /**
     * 根据商品信息获取用户返现比例
     *
     * @param positionId 推广位id
     * @param price 商品单价
     * @param estimateFee 实际计佣金额
     * @return
     */
    private ReturnMoneyScalePojo getReturnMoneyScaleBySkuInfo(Long positionId, double price, double estimateFee){
        LOGGER.info("根据商品信息获取用户返现比例，参数，推广位：" + positionId + "；商品单价：" + price + "；实际佣金额度：" + estimateFee);
        ReturnMoneyScalePojo rmsp = new ReturnMoneyScalePojo();
        rmsp.setReturnScale(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq")));// 设置返现比例默认值
        rmsp.setWxUserId(Long.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_wx_user_id_default")));// 设置用户标识默认值

        // 根据订单推广位信息解析出用户信息
        JdPosition jp = jdPositionService.queryJdPositionByPositionId(positionId);
        // 查询用户信息
        WxUser wu = null;
        if(jp != null){
            rmsp.setWxUserId(jp.getWxUserId());
            wu = wxUserService.queryWxUserById(jp.getWxUserId());
        }

        // 根据商品单价和计佣金额确定返现比例
        if(estimateFee < price){// 有券
            if(wu != null && wu.getReturnMoneyShareYq() > 0){
                rmsp.setReturnScale(wu.getReturnMoneyShareYq());
            }else{
                rmsp.setReturnScale(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq")));
            }
        }else{// 无券
            if(wu != null && wu.getReturnMoneyShareWq() > 0){
                rmsp.setReturnScale(wu.getReturnMoneyShareWq());
            }
        }
        LOGGER.info("根据商品信息获取用户返现比例，结果：" + rmsp.toString());
        return rmsp;
    }

    /**
     * 根据商品编号查询商品图片
     *
     * @param skuId 商品编号
     * @return
     */
    private String getGoodsImageUrlBySkuId(Long skuId){
        LOGGER.info("根据商品编号查询商品图片--> 商品编号：" + skuId);
        String imageUrl = "";
        JdGoodsDiscounts jgd = jdGoodsDiscountsService.queryJdGoodsDiscountsByGoodsId(skuId);
        if(jgd != null){
            imageUrl = jgd.getGoodsImgUrl();
        }else{// 联网查询图片
            String SERVER_URL = ApplicationParamConstant.JD_PARAM_MAP.get("jd_server_api_url");
            String appKey = configurationPropertiesSqm.getJD_APP_KEY();
            String appSecret = configurationPropertiesSqm.getJD_APP_SECRET();
            String accessToken = "";

            JdClient client = new DefaultJdClient(SERVER_URL, accessToken, appKey, appSecret);

            UnionOpenGoodsPromotiongoodsinfoQueryRequest request = new UnionOpenGoodsPromotiongoodsinfoQueryRequest();
            request.setSkuIds(skuId.toString());
            try {
                UnionOpenGoodsPromotiongoodsinfoQueryResponse response = client.execute(request);
                if(response.getCode() == 200) {
                    PromotionGoodsResp[] pgrArray = response.getData();
                    if(pgrArray != null && pgrArray.length > 0){
                        LOGGER.info("根据商品编号查询商品图片--> 查询到的商品数量：" + pgrArray.length);
                        imageUrl = pgrArray[0].getImgUrl();
                    }
                }
            }catch (Exception e){
                LOGGER.error("根据商品编号查询商品图片--> 异常", e);
            }
        }
        if(!StringUtils.isBlank(imageUrl)){
            imageUrl = imageUrl.replaceAll("http://", "https://");
        }
        LOGGER.info("根据商品编号查询商品图片--> 最终结果：" + imageUrl);
        return imageUrl;
    }

}
