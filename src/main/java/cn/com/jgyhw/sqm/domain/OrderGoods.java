package cn.com.jgyhw.sqm.domain;

import java.util.Date;

/**
 * 订单商品
 */
public class OrderGoods {
    /**
     * 标识
     */
    private String id;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品图片地址
     */
    private String imageUrl;

    /**
     * 订单记录标识
     */
    private String orderRecordId;

    /**
     * 返现比例
     */
    private Integer returnScale;

    /**
     * 创建时间
     */
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl == null ? null : imageUrl.trim();
    }

    public String getOrderRecordId() {
        return orderRecordId;
    }

    public void setOrderRecordId(String orderRecordId) {
        this.orderRecordId = orderRecordId == null ? null : orderRecordId.trim();
    }

    public Integer getReturnScale() {
        return returnScale;
    }

    public void setReturnScale(Integer returnScale) {
        this.returnScale = returnScale;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", code=").append(code);
        sb.append(", name=").append(name);
        sb.append(", imageUrl=").append(imageUrl);
        sb.append(", orderRecordId=").append(orderRecordId);
        sb.append(", returnScale=").append(returnScale);
        sb.append(", createTime=").append(createTime);
        sb.append("]");
        return sb.toString();
    }
}