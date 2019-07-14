package cn.com.jgyhw.sqm.pojo;

/**
 * Created by WangLei on 2019/4/11 0011 16:04
 *
 * 用户返现比例Pojo
 */
public class ReturnMoneyScalePojo {

    private Long wxUserId;// 用户信息

    private int returnScale;// 返现比例

    public Long getWxUserId() {
        return wxUserId;
    }

    public void setWxUserId(Long wxUserId) {
        this.wxUserId = wxUserId;
    }

    public int getReturnScale() {
        return returnScale;
    }

    public void setReturnScale(int returnScale) {
        this.returnScale = returnScale;
    }

    @Override
    public String toString() {
        return "ReturnMoneyScalePojo{" +
                "wxUserId=" + wxUserId +
                ", returnScale=" + returnScale +
                '}';
    }
}
