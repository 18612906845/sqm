package cn.com.jgyhw.sqm.pojo;

import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLei on 2019/5/2 0002 12:45
 *
 * 拼多多商品优惠查询结果Pojo
 */
public class PddGoodsDiscountsQueryResultPojo {

    private List<PddGoodsDiscounts> pddGoodsDiscountsList = new ArrayList<>();// 拼多多商品优惠集合

    private boolean isMore = true;// 是否还有更多

    private long totalCount;// 有效商品总数量

    private boolean status;// 查询状态，ture：正常，false：失败

    public List<PddGoodsDiscounts> getPddGoodsDiscountsList() {
        return pddGoodsDiscountsList;
    }

    public void setPddGoodsDiscountsList(List<PddGoodsDiscounts> pddGoodsDiscountsList) {
        this.pddGoodsDiscountsList = pddGoodsDiscountsList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "PddGoodsDiscountsQueryResultPojo{" +
                "pddGoodsDiscountsList=" + pddGoodsDiscountsList +
                ", isMore=" + isMore +
                ", totalCount=" + totalCount +
                ", status=" + status +
                '}';
    }
}
