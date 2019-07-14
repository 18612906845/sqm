package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.WxToken;

import java.util.List;

/**
 * Created by WangLei on 2019/4/14 0014 21:24
 *
 * 微信令牌接口
 */
public interface IWxTokenService {

    /**
     * 定时获取微信公众号服务Api令牌
     */
    void timingGetWxGzhServiceApiToken();

    /**
     * 定时获取微信小程序服务Api令牌
     */
    void timingGetWxXcxServiceApiToken();

    /**
     * 查询所有微信令牌
     *
     * @return
     */
    List<WxToken> queryWxTokenAll();
}
