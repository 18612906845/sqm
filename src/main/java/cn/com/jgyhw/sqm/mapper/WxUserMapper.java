package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.WxUser;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(WxUser record);

    int insertSelective(WxUser record);

    WxUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WxUser record);

    int updateByPrimaryKey(WxUser record);

    /**
     * 根据微信开放平台标识查询微信用户
     *
     * @param unionId 微信开放平台标识
     * @return
     */
    WxUser selectWxUserByUnionId(@Param("unionId") String unionId);

    /**
     * 根据微信公众号标识查询微信用户
     *
     * @param openIdGzh 微信公众号标识
     * @return
     */
    WxUser selectWxUserByOpenIdGzh(@Param("openIdGzh") String openIdGzh);

    /**
     * 根据推荐人微信用户标识查询微信用户总数
     *
     * @return
     */
    int selectWxUserSumByParentWxUserId(@Param("parentWxUserId") Long parentWxUserId);

    /**
     * 根据推荐人微信用户标识查询微信用户集合
     *
     * @return
     */
    Page<WxUser> selectWxUserListByParentWxUserIdPage(@Param("parentWxUserId") Long parentWxUserId);

    /**
     * 根据条件查询用户列表（分页）
     *
     * @param id 微信用户标识
     * @param nickName 微信用户昵称
     * @param orderField 排序字段
     * @return
     */
    Page<WxUser> selectWxUserListByConditionPage(@Param("id") Long id, @Param("nickName") String nickName, @Param("orderField") String orderField);


    /**
     * 查询可提现余额总计
     *
     * @return
     */
    Double selectRemainingMoneySum();


    /**
     * 查询累计支出提现申请金额
     * @return
     */
    Double selectWithdrawCashSum();
}