package cn.com.jgyhw.sqm.pojo;

import jd.union.open.promotion.byunionid.get.request.PromotionCodeReq;

/**
 * Created by WangLei on 2019/4/9 0009 17:31
 *
 * 获取推广链接接口参数
 */
public class PromotionCodeReqPojo extends PromotionCodeReq {

    private Long loginKey;// 微信用户状态

    public Long getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(Long loginKey) {
        this.loginKey = loginKey;
    }
}
