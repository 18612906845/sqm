package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.TbGoodsDiscounts;
import cn.com.jgyhw.sqm.pojo.TbGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
import com.taobao.api.request.TbkTpwdCreateRequest;

/**
 * Created by WangLei on 2019/5/20 0020 16:14
 *
 * 淘宝商品查询接口
 */
public interface ITbSearchService {

    /**
     * 根据物料编号查询淘宝推荐商品（同联盟APP好券直播）
     *
     * @param req 查询条件
     * @return
     */
    TbGoodsDiscountsQueryResultPojo queryTbGoodsTicketByMaterialId(TbkDgOptimusMaterialRequest req);

    /**
     * 根据关键词查询淘宝推荐商品
     *
     * @param req 查询条件
     * @return
     */
    TbGoodsDiscountsQueryResultPojo queryTbGoodsTicketByKeyword(TbkDgMaterialOptionalRequest req);

    /**
     * 根据条件查询淘宝优惠商品淘口令
     *
     * @param req 查询条件
     * @return
     */
    String queryTbGoodsCommand(TbkTpwdCreateRequest req);

    /**
     * 根据商品编号查询淘宝优惠商品详情
     *
     * @param goodsId 商品编号
     * @return
     */
    TbGoodsDiscounts queryTbGoodsDiscountsByGoodsId(Long goodsId);

    /**
     * 更新淘宝订单信息
     *
     * @param queryTimeStr 订单查询开始时间，例如：2016-05-23 12:18:22
     * @param queryTimeType 订单查询类型，创建时间“create_time”，或结算时间“settle_time”。当查询渠道或会员运营订单时，建议入参创建时间“create_time”进行查询
     * @param span  订单查询时间范围，单位：秒，最小60，最大1200，如不填写，默认60。查询常规订单、三方订单、渠道，及会员订单时均需要设置此参数，直接通过设置page_size,page_no 翻页查询数据即可
     * @param pageNum      页码
     * @param pageSize     每页条数
     */
    TimingTaskQueryResultPojo updateTbOrderInfoByTime(String queryTimeStr, String queryTimeType, Long span, int pageNum, int pageSize);
}
