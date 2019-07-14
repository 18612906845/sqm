package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.pojo.PddDdkGoodsPromotionUrlGenerateRequestPojo;
import cn.com.jgyhw.sqm.pojo.PddDdkGoodsSearchRequestPojo;
import cn.com.jgyhw.sqm.pojo.PddGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPromotionUrlGenerateResponse;

/**
 * Created by WangLei on 2019/5/2 0002 12:44
 *
 * 拼多多查询接口
 */
public interface IPddSearchService {

    /**
     * 关键词/商品编号/商品分类查询拼多多商品优惠
     *
     * @param pddDdkGoodsSearchRequestPojo 查询参数
     * @param isSearch 是否是人为搜索
     * @return
     */
    PddGoodsDiscountsQueryResultPojo queryPddGoodsDiscounts(PddDdkGoodsSearchRequestPojo pddDdkGoodsSearchRequestPojo, boolean isSearch);

    /**
     * 根据参数查询拼多多商品推广链接
     *
     * @param pddDdkGoodsPromotionUrlGenerateRequestPojo 查询参数
     * @return
     */
    PddDdkGoodsPromotionUrlGenerateResponse queryPddCpsUrl(PddDdkGoodsPromotionUrlGenerateRequestPojo pddDdkGoodsPromotionUrlGenerateRequestPojo);

    /**
     * 更新拼多多订单信息
     *
     * @param startUpdateTime 查询时间开始，毫秒值
     * @param endUpdateTime 查询时间结束，相差不能大于24小时
     * @param pageNum      页码
     * @param pageSize     每页条数
     * @return hasMore true：还有下一页，false：无下一页
     */
    TimingTaskQueryResultPojo updatePddOrderInfoByTime(Long startUpdateTime, Long endUpdateTime, int pageNum, int pageSize);

    /**
     * 根据完成时间解冻订单
     *
     * @param finishTimeStr 完成时间，小于等于传入时间，即可解冻
     */
    void unfreezePddOrderByFinishTimeStr(String finishTimeStr);
}
