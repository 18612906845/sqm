package cn.com.jgyhw.sqm.pojo;

import cn.com.jgyhw.sqm.domain.TbGoodsDiscounts;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WangLei on 2019/5/20 0020 17:11
 *
 * 淘宝商品优惠查询结果Pojo
 */
public class TbGoodsDiscountsQueryResultPojo {

    private List<TbGoodsDiscounts> tbGoodsDiscountsList = new ArrayList<>();// 淘宝优惠商品集合

    private boolean isMore = true;// 是否还有更多

    private long total = 200;// 有效商品总数量

    private boolean status;// 查询状态，ture：正常，false：失败

    public List<TbGoodsDiscounts> getTbGoodsDiscountsList() {
        return tbGoodsDiscountsList;
    }

    public void setTbGoodsDiscountsList(List<TbGoodsDiscounts> tbGoodsDiscountsList) {
        this.tbGoodsDiscountsList = tbGoodsDiscountsList;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "TbGoodsDiscountsQueryResultPojo{" +
                "tbGoodsDiscountsList=" + tbGoodsDiscountsList +
                ", isMore=" + isMore +
                ", total=" + total +
                ", status=" + status +
                '}';
    }
}
