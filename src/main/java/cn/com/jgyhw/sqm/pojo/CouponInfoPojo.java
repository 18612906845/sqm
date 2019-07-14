package cn.com.jgyhw.sqm.pojo;

/**
 * Created by WangLei on 2019/2/23 0023 12:11
 *
 * 优惠券信息Pojo
 */
public class CouponInfoPojo {

    /**
     * 券领取结束时间(时间戳，毫秒)
     */
    private Long takeEndTime;

    /**
     * 券领取开始时间(时间戳，毫秒)
     */
    private Long takeBeginTime;

    /**
     * 券剩余张数
     */
    private Long remainNum;

    /**
     * 券有效状态
     */
    private String yn;

    /**
     * 券总张数
     */
    private Long num;

    /**
     * 券消费限额
     */
    private Double quota;

    /**
     * 券链接
     */
    private String link;

    /**
     * 券面额
     */
    private Double discount;

    /**
     * 券有效使用开始时间(时间戳，毫秒)
     */
    private Long beginTime;

    /**
     * 券有效使用结束时间(时间戳，毫秒)
     */
    private Long endTime;

    /**
     * 券使用平台
     */
    private String platform;

    public Long getTakeEndTime() {
        return takeEndTime;
    }

    public void setTakeEndTime(Long takeEndTime) {
        this.takeEndTime = takeEndTime;
    }

    public Long getTakeBeginTime() {
        return takeBeginTime;
    }

    public void setTakeBeginTime(Long takeBeginTime) {
        this.takeBeginTime = takeBeginTime;
    }

    public Long getRemainNum() {
        return remainNum;
    }

    public void setRemainNum(Long remainNum) {
        this.remainNum = remainNum;
    }

    public String getYn() {
        return yn;
    }

    public void setYn(String yn) {
        this.yn = yn;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Long getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "CouponInfoPojo{" +
                "takeEndTime=" + takeEndTime +
                ", takeBeginTime=" + takeBeginTime +
                ", remainNum=" + remainNum +
                ", yn='" + yn + '\'' +
                ", num=" + num +
                ", quota=" + quota +
                ", link='" + link + '\'' +
                ", discount=" + discount +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", platform='" + platform + '\'' +
                '}';
    }
}
