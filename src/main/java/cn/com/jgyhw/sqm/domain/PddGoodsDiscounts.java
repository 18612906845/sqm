package cn.com.jgyhw.sqm.domain;

/**
 * 拼多多商品优惠
 */
public class PddGoodsDiscounts extends GoodsDiscounts {

    /**
     * 最小拼团价格
     */
    private Double minGroupPrice;

    /**
     * 最小单买价格
     */
    private Double minNormalPrice;

    /**
     * 是否有优惠券，1：是；2：否
     */
    private Integer hasCoupon;

    public Double getMinGroupPrice() {
        return minGroupPrice;
    }

    public void setMinGroupPrice(Double minGroupPrice) {
        this.minGroupPrice = minGroupPrice;
    }

    public Double getMinNormalPrice() {
        return minNormalPrice;
    }

    public void setMinNormalPrice(Double minNormalPrice) {
        this.minNormalPrice = minNormalPrice;
    }

    public Integer getHasCoupon() {
        return hasCoupon;
    }

    public void setHasCoupon(Integer hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    @Override
    public String toString() {
        return "PddGoodsDiscounts{" +
                "minGroupPrice=" + minGroupPrice +
                ", minNormalPrice=" + minNormalPrice +
                ", hasCoupon=" + hasCoupon +
                '}';
    }
}