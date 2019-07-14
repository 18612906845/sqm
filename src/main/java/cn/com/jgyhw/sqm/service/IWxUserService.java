package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.WxUser;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/4/9 0009 13:28
 *
 * 微信用户接口
 */
public interface IWxUserService {

    /**
     * 保存微信用户
     *
     * @param wxUser 微信用户对象
     * @return
     */
    void saveWxUser(WxUser wxUser);

    /**
     * 修改微信用户
     *
     * @param wxUser 微信用户对象
     */
    void updateWxUser(WxUser wxUser);

    /**
     * 根据标识查询微信用户
     *
     * @param id 标识
     * @return
     */
    WxUser queryWxUserById(Long id);

    /**
     * 根据微信开放平台标识查询微信用户
     *
     * @param unionId 微信开放平台标识
     * @return
     */
    WxUser queryWxUserByUnionId(String unionId);

    /**
     * 根据微信公众号标识查询微信用户
     *
     * @param openIdGzh 微信公众号标识
     * @return
     */
    WxUser queryWxUserByOpenIdGzh(String openIdGzh);

    /**
     * 根据推荐人微信用户标识查询微信用户总数
     *
     * @return
     */
    int queryWxUserSumByParentWxUserId(Long parentWxUserId);

    /**
     * 根据推荐人微信用户标识查询微信用户集合（分页）
     *
     * @param parentWxUserId 推荐人用户标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<WxUser> queryWxUserListByParentWxUserIdPage(Long parentWxUserId, int pageNo, int pageSize);

    /**
     * 根据条件查询用户列表（分页）
     *
     * @param id 微信用户标识
     * @param nickName 微信用户昵称
     * @param orderField 排序字段
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<WxUser> queryWxUserListByConditionPage(Long id, String nickName, String orderField, int pageNo, int pageSize);

    /**
     * 查询可提现余额总计
     *
     * @return
     */
    double queryRemainingMoneySum();

    /**
     * 查询累计支出提现申请金额
     * @return
     */
    double queryWithdrawCashSum();

}
