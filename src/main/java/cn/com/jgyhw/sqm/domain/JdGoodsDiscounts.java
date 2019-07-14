package cn.com.jgyhw.sqm.domain;

/**
 * 京东商品优惠
 */
public class JdGoodsDiscounts extends GoodsDiscounts {

    /**
     * 商品落地页URL
     */
    private String materialUrl;

    /**
     * g=自营，p=pop
     */
    private String owner;

    /**
     * 券链接
     */
    private String link;

    /**
     * 拼购价格
     */
    private Double pinGouPrice;

    public String getMaterialUrl() {
        return materialUrl;
    }

    public void setMaterialUrl(String materialUrl) {
        this.materialUrl = materialUrl;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Double getPinGouPrice() {
        return pinGouPrice;
    }

    public void setPinGouPrice(Double pinGouPrice) {
        this.pinGouPrice = pinGouPrice;
    }

    @Override
    public String toString() {
        return "JdGoodsDiscounts{" +
                "materialUrl='" + materialUrl + '\'' +
                ", owner='" + owner + '\'' +
                ", link='" + link + '\'' +
                ", pinGouPrice=" + pinGouPrice +
                '}';
    }
}