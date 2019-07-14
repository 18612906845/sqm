package cn.com.jgyhw.sqm.util;

import cn.com.jgyhw.sqm.domain.*;
import cn.com.jgyhw.sqm.pojo.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsPromotionUrlGenerateRequest;
import com.pdd.pop.sdk.http.api.request.PddDdkGoodsSearchRequest;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsSearchResponse;
import com.taobao.api.response.TbkDgMaterialOptionalResponse;
import com.taobao.api.response.TbkDgOptimusMaterialResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cglib.beans.BeanCopier;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by WangLei on 2019/1/12 0012 21:14
 *
 * 对象转换工具
 */
public class ObjectTransitionUtil extends CommonUtil {

    /**
     * 配置参数Model To Pojo
     *
     * @param configuration 配置参数Model
     * @return
     */
    protected ConfigurationPojo configurationToConfigurationPojo(Configuration configuration){
        if(configuration == null){
            return null;
        }
        ConfigurationPojo cp = new ConfigurationPojo();
        BeanCopier copier = BeanCopier.create(Configuration.class, ConfigurationPojo.class, false);
        copier.copy(configuration, cp, null);
        cp.setUpdateTimeLong(cp.getUpdateTime().getTime());
        return cp;
    }

    /**
     * 订单记录Model To Pojo
     *
     * @param orderRecord 订单记录Model
     * @return
     */
    protected OrderRecord orderRecordToOrderRecordPojo(OrderRecord orderRecord){
        if(orderRecord.getOrderTime() != null){
            orderRecord.setOrderTimeStr(this.dateToStringByFormat(orderRecord.getOrderTime(), null));
        }
        if(orderRecord.getFinishTime() != null){
            orderRecord.setFinishTimeStr(this.dateToStringByFormat(orderRecord.getFinishTime(), null));
        }
        if(orderRecord.getCreateTime() != null){
            orderRecord.setCreateTimeStr(this.dateToStringByFormat(orderRecord.getCreateTime(), null));
        }
        if(orderRecord.getUpdateTime() != null){
            orderRecord.setUpdateTimeStr(this.dateToStringByFormat(orderRecord.getUpdateTime(), null));
        }

        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_dfk")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("待付款");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yfk")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("已付款");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yqx")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("已取消");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yct")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("已成团");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_ywc")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("已完成");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_yrz")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("已入账");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_status_wx")) == orderRecord.getStatus()){
            orderRecord.setStatusStr("无效");
        }
        List<OrderGoods> ogList = orderRecord.getOrderGoodsList();
        if(ogList != null && ogList.size() > 1){
            orderRecord.setMultipartiteGoods(true);
        }else{
            orderRecord.setMultipartiteGoods(false);
        }

        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")) == orderRecord.getPlatform()){
            orderRecord.setPlatformStr("京东");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")) == orderRecord.getPlatform()){
            orderRecord.setPlatformStr("拼多多");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")) == orderRecord.getPlatform()){
            orderRecord.setPlatformStr("淘宝");
        }

        return orderRecord;
    }

    /**
     * 返现变更记录 Model To Pojo
     *
     * @param returnMoneyChangeRecord 返现变更记录Model
     * @return
     */
    protected ReturnMoneyChangeRecord returnMoneyChangeRecordToReturnMoneyChangeRecordPojo(ReturnMoneyChangeRecord returnMoneyChangeRecord){
        if(returnMoneyChangeRecord == null){
            return null;
        }
        if(returnMoneyChangeRecord.getChangeTime() != null){
            returnMoneyChangeRecord.setChangeTimeStr(this.dateToStringByFormat(returnMoneyChangeRecord.getChangeTime(), null));
        }
        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_rz")) == returnMoneyChangeRecord.getChangeType()){// 入账
            returnMoneyChangeRecord.setChangeTypeStr("订单入账");
            returnMoneyChangeRecord.setTargetName(returnMoneyChangeRecord.getOrderId());
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tc")) == returnMoneyChangeRecord.getChangeType()){// 提成
            returnMoneyChangeRecord.setChangeTypeStr("提成入账");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tx")) == returnMoneyChangeRecord.getChangeType()){// 提现
            returnMoneyChangeRecord.setChangeTypeStr("提现出账");
            returnMoneyChangeRecord.setTargetImageName("icon_txjl.jpg");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_yx")) == returnMoneyChangeRecord.getChangeType()){// 邀新
            returnMoneyChangeRecord.setChangeTypeStr("邀新入账");
        }
        return returnMoneyChangeRecord;
    }

    /**
     * 提现记录 Model To Pojo
     *
     * @param withdrawCashRecord 提现记录Model
     * @return
     */
    protected WithdrawCashRecord withdrawCashRecordToWithdrawCashRecordPojo(WithdrawCashRecord withdrawCashRecord){
        if(withdrawCashRecord == null){
            return null;
        }
        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_dzf")) == withdrawCashRecord.getPayStatus()){
            withdrawCashRecord.setPayStatusStr("待支付");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_yzf")) == withdrawCashRecord.getPayStatus()){
            withdrawCashRecord.setPayStatusStr("已支付");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("withdraw_cash_pay_status_zfsb")) == withdrawCashRecord.getPayStatus()){
            withdrawCashRecord.setPayStatusStr("支付失败");
        }
        if(withdrawCashRecord.getPayTime() != null){
            withdrawCashRecord.setPayTimeStr(this.dateToStringByFormat(withdrawCashRecord.getPayTime(), ""));
        }else{
            withdrawCashRecord.setPayTimeStr("");
        }
        if(withdrawCashRecord.getApplyTime() != null){
            withdrawCashRecord.setApplyTimeStr(this.dateToStringByFormat(withdrawCashRecord.getApplyTime(), ""));
        }else{
            withdrawCashRecord.setApplyTimeStr("");
        }

        return withdrawCashRecord;
    }

    /**
     * 微信用户 Model To Pojo
     *
     * @param wxUser 微信用户Model
     * @return
     */
    protected WxUser wxUserToWxUserPojo(WxUser wxUser){
        if(wxUser == null){
            return null;
        }
        if(wxUser.getCreateTime() != null){
            wxUser.setCreateTimeStr(this.dateToStringByFormat(wxUser.getCreateTime(), null));
        }
        if(wxUser.getUpdateTime() != null){
            wxUser.setUpdateTimeStr(this.dateToStringByFormat(wxUser.getUpdateTime(), null));
        }
        if(wxUser.getSex() != null){
            if(wxUser.getSex() == 1){
                wxUser.setSexStr("男性");
            }else if(wxUser.getSex() == 2){
                wxUser.setSexStr("女性");
            }else{
                wxUser.setSexStr("未知");
            }
        }else{
            wxUser.setSexStr("未知");
        }

        return wxUser;
    }

    /**
     * 微信令牌 Model To Pojo
     *
     * @param wxToken 微信令牌Model
     * @return
     */
    protected WxToken wxTokenToWxTokenPojo(WxToken wxToken){
        if(wxToken.getUpdateTime() != null){
            wxToken.setUpdateTimeStr(this.dateToStringByFormat(wxToken.getUpdateTime(), null));
        }else{
            wxToken.setUpdateTimeStr("");
        }
        if(Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_gzh_server_api")) == wxToken.getTokenType()){
            wxToken.setTokenTypeStr("公众号服务Api令牌");
        }else if(Integer.valueOf(ApplicationParamConstant.WX_PARAM_MAP.get("wx_token_type_xcx_server_api")) == wxToken.getTokenType()){
            wxToken.setTokenTypeStr("小程序服务Api令牌");
        }
        return wxToken;
    }

    /**
     * 拼多多商品优惠Model To Pojo
     *
     * @param item 拼多多商品优惠Model
     * @return
     */
    protected PddGoodsDiscounts goodsSearchResponseGoodsListItemToPddGoodsDiscounts(PddDdkGoodsSearchResponse.GoodsSearchResponseGoodsListItem item){
        if(item == null){
            return null;
        }
        PddGoodsDiscounts pgd = new PddGoodsDiscounts();
        pgd.setGoodsId(item.getGoodsId());
        pgd.setGoodsName(item.getGoodsName());
        pgd.setGoodsImgUrl(item.getGoodsThumbnailUrl());
        pgd.setSalesVolume(item.getSoldQuantity());
        pgd.setMinGroupPrice(this.fenToYuan(item.getMinGroupPrice()));
        pgd.setMinNormalPrice(this.fenToYuan(item.getMinNormalPrice()));
        pgd.setHasCoupon(item.getHasCoupon() ? 1 : 2);
        pgd.setDiscountQuota(this.fenToYuan(item.getCouponMinOrderAmount()));
        pgd.setDiscount(this.fenToYuan(item.getCouponDiscount()));
        pgd.setCommissionRate(this.qianFenBiToBaiFenBi(item.getPromotionRate()));
        // 设置最低价格
        if(item.getMinGroupPrice() != null && item.getMinGroupPrice() > 0){
            if(pgd.getMinGroupPrice() > pgd.getDiscountQuota()){// 最低拼购价格大于优惠券限额，可使用优惠券
                double practicalPrice = pgd.getMinGroupPrice() - pgd.getDiscount();
                pgd.setLowestPrice(this.formatDouble(practicalPrice));
                pgd.setLowestPriceName("券后拼购价");
            }else{
                pgd.setLowestPrice(pgd.getMinGroupPrice());
                pgd.setLowestPriceName("拼购价");
            }
            pgd.setPrice(pgd.getMinGroupPrice());
        }else{
            if(pgd.getMinNormalPrice() > pgd.getDiscountQuota()){// 最低单购价格大于优惠券限额，可使用优惠券
                double practicalPrice = pgd.getMinNormalPrice() - pgd.getDiscount();
                pgd.setLowestPrice(this.formatDouble(practicalPrice));
                pgd.setLowestPriceName("券后单购价");
            }else{
                pgd.setLowestPrice(pgd.getMinNormalPrice());
                pgd.setLowestPriceName("单购价");
            }
            pgd.setPrice(pgd.getMinNormalPrice());
        }

        // 设置联盟佣金，最低价格*佣金比例
        Double promotion = pgd.getLowestPrice() * (pgd.getCommissionRate() / 100);
        pgd.setCommission(this.formatDouble(promotion));

        // 设置返现金额
        Double returnMoney = this.rebateCompute(pgd.getLowestPrice(), pgd.getCommissionRate(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq")));
        pgd.setReturnMoney(this.formatDouble(returnMoney));
        return pgd;
    }

    /**
     * 拼多多商品优惠查询条件Pojo To Model
     *
     * @param pddDdkGoodsSearchRequestPojo 拼多多商品优惠查询条件Pojo
     * @return
     */
    protected PddDdkGoodsSearchRequest pddDdkGoodsSearchRequestPojoToPddDdkGoodsSearchRequest(PddDdkGoodsSearchRequestPojo pddDdkGoodsSearchRequestPojo){
        if(pddDdkGoodsSearchRequestPojo == null){
            return null;
        }
        PddDdkGoodsSearchRequest pdgsr = new PddDdkGoodsSearchRequest();
        pdgsr.setKeyword(pddDdkGoodsSearchRequestPojo.getKeyword());
        pdgsr.setOptId(pddDdkGoodsSearchRequestPojo.getOptId());
        pdgsr.setPage(pddDdkGoodsSearchRequestPojo.getPage());
        pdgsr.setPageSize(pddDdkGoodsSearchRequestPojo.getPageSize());
        pdgsr.setSortType(pddDdkGoodsSearchRequestPojo.getSortType());
        pdgsr.setWithCoupon(pddDdkGoodsSearchRequestPojo.getWithCoupon());
        pdgsr.setRangeList(pddDdkGoodsSearchRequestPojo.getRangeList());
        pdgsr.setCatId(pddDdkGoodsSearchRequestPojo.getCatId());
        pdgsr.setGoodsIdList(pddDdkGoodsSearchRequestPojo.getGoodsIdList());
        pdgsr.setMerchantType(pddDdkGoodsSearchRequestPojo.getMerchantType());
        pdgsr.setPid(pddDdkGoodsSearchRequestPojo.getPid());
        pdgsr.setCustomParameters(pddDdkGoodsSearchRequestPojo.getCustomParameters());

        return pdgsr;
    }

    /**
     * 拼多多商品推广链接查询条件Pojo To Model
     *
     * @param pddDdkGoodsPromotionUrlGenerateRequestPojo 拼多多商品推广链接查询条件Pojo
     * @return
     */
    protected PddDdkGoodsPromotionUrlGenerateRequest pddDdkGoodsPromotionUrlGenerateRequestPojoToPddDdkGoodsPromotionUrlGenerateRequest(PddDdkGoodsPromotionUrlGenerateRequestPojo pddDdkGoodsPromotionUrlGenerateRequestPojo){
        if(pddDdkGoodsPromotionUrlGenerateRequestPojo == null){
            return null;
        }
        PddDdkGoodsPromotionUrlGenerateRequest pdgpugr = new PddDdkGoodsPromotionUrlGenerateRequest();
        pdgpugr.setPId(ApplicationParamConstant.PDD_PARAM_MAP.get("pdd_default_pId"));
        List<Long> goodsIdList = new ArrayList<>();
        goodsIdList.add(pddDdkGoodsPromotionUrlGenerateRequestPojo.getGoodsId());
        pdgpugr.setGoodsIdList(goodsIdList);
        pdgpugr.setGenerateShortUrl(pddDdkGoodsPromotionUrlGenerateRequestPojo.getGenerateShortUrl());
        pdgpugr.setMultiGroup(pddDdkGoodsPromotionUrlGenerateRequestPojo.getMultiGroup());
        pdgpugr.setCustomParameters(pddDdkGoodsPromotionUrlGenerateRequestPojo.getCustomParameters());
        pdgpugr.setGenerateWeappWebview(pddDdkGoodsPromotionUrlGenerateRequestPojo.getGenerateWeappWebview());
        pdgpugr.setZsDuoId(pddDdkGoodsPromotionUrlGenerateRequestPojo.getZsDuoId());
        pdgpugr.setGenerateWeApp(pddDdkGoodsPromotionUrlGenerateRequestPojo.getGenerateWeApp());

        return pdgpugr;
    }

    /**
     * 淘宝优惠商品信息 To Pojo
     *
     * @param mapData 淘宝优惠商品信息
     * @return
     */
    protected TbGoodsDiscounts tbMapDataToTbGoodsDiscounts(TbkDgOptimusMaterialResponse.MapData mapData){
        if(mapData == null){
            return null;
        }
        TbGoodsDiscounts tgd = new TbGoodsDiscounts();

        tgd.setGoodsId(mapData.getItemId());
        tgd.setGoodsName(mapData.getTitle());
        tgd.setGoodsImgUrl("https:" + mapData.getPictUrl());
        tgd.setSalesVolume(mapData.getVolume());

        // 设置优惠券
        tgd.setDiscount(Double.valueOf(mapData.getCouponAmount()));
        tgd.setDiscountQuota(StringUtils.isBlank(mapData.getCouponStartFee()) ? 0D : Double.valueOf(mapData.getCouponStartFee()));

        // 一口价
        Double reservePrice = StringUtils.isBlank(mapData.getReservePrice()) ? 0D : Double.valueOf(mapData.getReservePrice());
        // 折扣价
        Double zkFinalPrice = StringUtils.isBlank(mapData.getZkFinalPrice()) ? 0D : Double.valueOf(mapData.getZkFinalPrice());
        // 设置返现比例
        Integer returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"));
        // 设置原价
        if(reservePrice > 0){
            tgd.setPrice(reservePrice);
            if(tgd.getDiscountQuota() > 0 && tgd.getDiscountQuota() <= reservePrice){// 可使用优惠券
                tgd.setLowestPrice(this.formatDouble(reservePrice - tgd.getDiscount()));
                tgd.setLowestPriceName("券后价");
                returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
            }else{
                tgd.setLowestPrice(reservePrice);
                tgd.setLowestPriceName("一口价");
            }
        }
        if(zkFinalPrice > 0){
            tgd.setPrice(zkFinalPrice);
            if(tgd.getDiscountQuota() > 0 && tgd.getDiscountQuota() <= zkFinalPrice){// 可使用优惠券
                tgd.setLowestPrice(this.formatDouble(zkFinalPrice - tgd.getDiscount()));
                tgd.setLowestPriceName("券后价");
                returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
            }else{
                tgd.setLowestPrice(zkFinalPrice);
                tgd.setLowestPriceName("现价");
            }
        }

        // 设置佣金信息
        if(StringUtils.isBlank(mapData.getCommissionRate())){// 无佣金比例
            tgd.setCommission(0D);
            tgd.setReturnMoney(0D);
        }else{
            tgd.setCommissionRate(Double.valueOf(mapData.getCommissionRate()));

            // 设置联盟佣金，最低价格*佣金比例
            Double commission = tgd.getLowestPrice() * (tgd.getCommissionRate() / 100);
            tgd.setCommission(this.formatDouble(commission));

            // 设置返现金额
            Double returnMoney = this.rebateCompute(tgd.getLowestPrice(), tgd.getCommissionRate(), returnMoneyRate);
            tgd.setReturnMoney(this.formatDouble(returnMoney));
        }

        tgd.setUpdateTime(new Date());
        tgd.setClickUrl("https:" + mapData.getClickUrl());
        tgd.setCouponShareUrl(StringUtils.isBlank(mapData.getCouponShareUrl()) ? "" : "https:" + mapData.getCouponShareUrl());
        // 设置店铺类型
        tgd.setUserType(mapData.getUserType());
        if(mapData.getUserType() == 1){
            tgd.setUserTypeStr("天猫");
        }else{
            tgd.setUserTypeStr("淘宝");
        }
        return tgd;
    }

    /**
     * 淘宝优惠商品信息 To Pojo
     *
     * @param mapData 淘宝优惠商品信息
     * @return
     */
    protected TbGoodsDiscounts tbMapDataToTbGoodsDiscounts(TbkDgMaterialOptionalResponse.MapData mapData){
        if(mapData == null){
            return null;
        }
        TbGoodsDiscounts tgd = new TbGoodsDiscounts();

        tgd.setGoodsId(mapData.getItemId());
        tgd.setGoodsName(mapData.getTitle());
        tgd.setGoodsImgUrl(mapData.getPictUrl());
        tgd.setSalesVolume(mapData.getVolume());

        // 设置优惠券
        tgd.setDiscount(StringUtils.isBlank(mapData.getCouponAmount()) ? 0D : Double.valueOf(mapData.getCouponAmount()));
        tgd.setDiscountQuota(StringUtils.isBlank(mapData.getCouponStartFee()) ? 0D : Double.valueOf(mapData.getCouponStartFee()));

        // 一口价
        Double reservePrice = StringUtils.isBlank(mapData.getReservePrice()) ? 0D : Double.valueOf(mapData.getReservePrice());
        // 折扣价
        Double zkFinalPrice = StringUtils.isBlank(mapData.getZkFinalPrice()) ? 0D : Double.valueOf(mapData.getZkFinalPrice());
        // 设置返现比例
        Integer returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_wq"));
        // 设置原价
        if(reservePrice > 0){
            tgd.setPrice(reservePrice);
            if(tgd.getDiscountQuota() > 0 && tgd.getDiscountQuota() <= reservePrice){// 可使用优惠券
                tgd.setLowestPrice(this.formatDouble(reservePrice - tgd.getDiscount()));
                tgd.setLowestPriceName("券后价");
                returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
            }else{
                tgd.setLowestPrice(reservePrice);
                tgd.setLowestPriceName("一口价");
            }
        }
        if(zkFinalPrice > 0){
            tgd.setPrice(zkFinalPrice);
            if(tgd.getDiscountQuota() > 0 && tgd.getDiscountQuota() <= zkFinalPrice){// 可使用优惠券
                tgd.setLowestPrice(this.formatDouble(zkFinalPrice - tgd.getDiscount()));
                tgd.setLowestPriceName("券后价");
                returnMoneyRate = Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_share_yq"));
            }else{
                tgd.setLowestPrice(zkFinalPrice);
                tgd.setLowestPriceName("现价");
            }
        }

        // 设置佣金信息
        if(StringUtils.isBlank(mapData.getCommissionRate())){// 无佣金比例
            tgd.setCommission(0D);
            tgd.setReturnMoney(0D);
        }else{
            tgd.setCommissionRate(Double.valueOf(mapData.getCommissionRate()) / 100);

            // 设置联盟佣金，最低价格*佣金比例
            Double commission = tgd.getLowestPrice() * (tgd.getCommissionRate() / 100);
            tgd.setCommission(this.formatDouble(commission));

            // 设置返现金额
            Double returnMoney = this.rebateCompute(tgd.getLowestPrice(), tgd.getCommissionRate(), returnMoneyRate);
            tgd.setReturnMoney(this.formatDouble(returnMoney));
        }

        tgd.setUpdateTime(new Date());
        tgd.setClickUrl("https:" + mapData.getUrl());
        tgd.setCouponShareUrl(StringUtils.isBlank(mapData.getCouponShareUrl()) ? "" : "https:" + mapData.getCouponShareUrl());
        // 设置店铺类型
        tgd.setUserType(mapData.getUserType());
        if(mapData.getUserType() == 1){
            tgd.setUserTypeStr("天猫");
        }else{
            tgd.setUserTypeStr("淘宝");
        }
        return tgd;
    }

    /**
     * 优惠商品类型Model To Pojo
     *
     * @param dgc 优惠商品类型Model
     * @return
     */
    protected DiscountsGoodsClassify discountsGoodsClassifyToPojo(DiscountsGoodsClassify dgc){
        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")) == dgc.getPlatform()){
            dgc.setPlatformStr("京东");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")) == dgc.getPlatform()){
            dgc.setPlatformStr("拼多多");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")) == dgc.getPlatform()){
            dgc.setPlatformStr("淘宝");
        }

        if(dgc.getCreateTime() != null){
            dgc.setCreateTimeStr(this.dateToStringByFormat(dgc.getCreateTime(), null));
        }
        if(dgc.getUpdateTime() != null){
            dgc.setUpdateTimeStr(this.dateToStringByFormat(dgc.getUpdateTime(), null));
        }
        return dgc;
    }

    /**
     * 淘宝订单绑定记录Model To Pojo
     *
     * @param tobr 淘宝订单绑定记录Model
     * @return
     */
    protected TbOrderBindingRecord tbOrderBindingRecordToPojo(TbOrderBindingRecord tobr){
        if(tobr == null){
            return null;
        }

        if(tobr.getCreateTime() != null){
            tobr.setCreateTimeStr(this.dateToStringByFormat(tobr.getCreateTime(), null));
        }
        if(tobr.getUpdateTime() != null){
            tobr.setUpdateTimeStr(this.dateToStringByFormat(tobr.getUpdateTime(), null));
        }
        if(tobr.getStatus() == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_wbd"))){
            tobr.setStatusStr("未绑定");
        }else if(tobr.getStatus() == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_ybd"))){
            tobr.setStatusStr("已绑定");
        }if(tobr.getStatus() == Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_wx"))){
            tobr.setStatusStr("无效");
        }
        return tobr;
    }
}
