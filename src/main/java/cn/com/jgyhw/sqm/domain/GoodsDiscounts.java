package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * Created by WangLei on 2019/5/21 0021 00:08
 *
 * 商品优惠信息基类
 */
public class GoodsDiscounts {

    /**
     * 标识
     */
    private String id;
    
    /**
     * 商品编号
     */
    private Long goodsId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品主图地址
     */
    private String goodsImgUrl;

    /**
     * 销售数量
     */
    private Long salesVolume;

    /**
     * 价格（原价）
     */
    private Double price;

    /**
     * 券面额
     */
    private Double discount;

    /**
     * 券限额
     */
    private Double discountQuota;

    /**
     * 联盟佣金
     */
    private Double commission;

    /**
     * 联盟佣金比例
     */
    private Double commissionRate;

    /**
     * 返现金额
     */
    private Double returnMoney;

    /**
     * 最低价格
     */
    private Double lowestPrice;

    /**
     * 最低价格名称
     */
    private String lowestPriceName;

    /**
     * 商品分类
     */
    private String goodsClassify;

    /**
     * 更新时间
     */
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsImgUrl() {
        return goodsImgUrl;
    }

    public void setGoodsImgUrl(String goodsImgUrl) {
        this.goodsImgUrl = goodsImgUrl;
    }

    public Long getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(Long salesVolume) {
        this.salesVolume = salesVolume;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getDiscountQuota() {
        return discountQuota;
    }

    public void setDiscountQuota(Double discountQuota) {
        this.discountQuota = discountQuota;
    }

    public Double getCommission() {
        return commission;
    }

    public void setCommission(Double commission) {
        this.commission = commission;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public Double getReturnMoney() {
        return returnMoney;
    }

    public void setReturnMoney(Double returnMoney) {
        this.returnMoney = returnMoney;
    }

    public Double getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(Double lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public String getLowestPriceName() {
        return lowestPriceName;
    }

    public void setLowestPriceName(String lowestPriceName) {
        this.lowestPriceName = lowestPriceName;
    }

    public String getGoodsClassify() {
        return goodsClassify;
    }

    public void setGoodsClassify(String goodsClassify) {
        this.goodsClassify = goodsClassify;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "GoodsDiscounts{" +
                "id='" + id + '\'' +
                ", goodsId=" + goodsId +
                ", goodsName='" + goodsName + '\'' +
                ", goodsImgUrl='" + goodsImgUrl + '\'' +
                ", salesVolume=" + salesVolume +
                ", price=" + price +
                ", discount=" + discount +
                ", discountQuota=" + discountQuota +
                ", commission=" + commission +
                ", commissionRate=" + commissionRate +
                ", returnMoney=" + returnMoney +
                ", lowestPrice=" + lowestPrice +
                ", lowestPriceName='" + lowestPriceName + '\'' +
                ", goodsClassify='" + goodsClassify + '\'' +
                ", updateTime=" + updateTime +
                '}';
    }
}
