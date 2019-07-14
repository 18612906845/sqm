package cn.com.jgyhw.sqm.pojo;

import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLei on 2019/4/8 0008 23:37
 *
 * 京东商品优惠查询结果Pojo
 */
public class JdGoodsDiscountsQueryResultPojo {

    private List<JdGoodsDiscounts> jdGoodsDiscountsList = new ArrayList<>();// 京东商品优惠集合

    private boolean isMore = true;// 是否还有更多

    private Long totalCount;// 有效商品总数量

    private boolean status;// 查询状态，ture：正常，false：失败

    public List<JdGoodsDiscounts> getJdGoodsDiscountsList() {
        return jdGoodsDiscountsList;
    }

    public void setJdGoodsDiscountsList(List<JdGoodsDiscounts> jdGoodsDiscountsList) {
        this.jdGoodsDiscountsList = jdGoodsDiscountsList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
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
        return "JdGoodsDiscountsQueryResultPojo{" +
                "jdGoodsDiscountsList=" + jdGoodsDiscountsList +
                ", isMore=" + isMore +
                ", totalCount=" + totalCount +
                ", status=" + status +
                '}';
    }
}
