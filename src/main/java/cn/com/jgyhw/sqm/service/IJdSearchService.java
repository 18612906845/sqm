package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.pojo.JdGoodsDiscountsQueryParamPojo;
import cn.com.jgyhw.sqm.pojo.JdGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.PromotionCodeReqPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;

/**
 * Created by WangLei on 2019/2/23 0023 10:52
 *
 * 京东查询接口
 */
public interface IJdSearchService {

    /**
     * 关键词/商品编号查询京东商品优惠
     *
     * @param jdGoodsDiscountsQueryParamPojo 查询参数
     * @return
     */
    JdGoodsDiscountsQueryResultPojo queryJdGoodsDiscounts(JdGoodsDiscountsQueryParamPojo jdGoodsDiscountsQueryParamPojo);

    /**
     * 同步京粉商品优惠
     *
     * @param jfGoodsReq 查询条件
     * @return
     */
    JdGoodsDiscountsQueryResultPojo syncJdJingFenGoodsDiscounts(JFGoodsReq jfGoodsReq);

    /**
     * 根据参数查询京东推广链接
     *
     * @param promotionCodeReqPojo 推广链接查询参数
     * @return
     */
    String queryJdCpsUrl(PromotionCodeReqPojo promotionCodeReqPojo);

    /**
     * 更新京东订单信息
     *
     * @param queryTimeStr 查询时间字符串，查询时间,输入格式必须为yyyyMMddHHmm,yyyyMMddHHmmss或者yyyyMMddHH格式之一
     * @param queryTimeType 查询时间类型，1：下单时间，2：完成时间，3：更新时间
     * @param isUnfreeze    是否解冻
     * @param pageNum      页码
     * @param pageSize     每页条数
     */
    TimingTaskQueryResultPojo updateJdOrderInfoByTime(String queryTimeStr, int queryTimeType, boolean isUnfreeze, int pageNum, int pageSize);

}
