package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.mapper.WxUserMapper;
import cn.com.jgyhw.sqm.service.IWxUserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by WangLei on 2019/4/9 0009 13:40
 */
@Service("wxUserService")
public class WxUserServiceImpl implements IWxUserService {

    private static Logger LOGGER = LogManager.getLogger(WxUserServiceImpl.class);

    @Autowired
    private WxUserMapper wxUserMapper;

    /**
     * 保存微信用户
     *
     * @param wxUser 微信用户对象
     * @return
     */
    @Override
    public void saveWxUser(WxUser wxUser) {
        if(wxUser == null){
            return;
        }
        wxUserMapper.insert(wxUser);
    }

    /**
     * 修改微信用户
     *
     * @param wxUser 微信用户对象
     */
    @Override
    public void updateWxUser(WxUser wxUser) {
        if(wxUser == null){
            return;
        }
        wxUserMapper.updateByPrimaryKey(wxUser);
    }

    /**
     * 根据标识查询微信用户
     *
     * @param id 标识
     * @return
     */
    @Override
    public WxUser queryWxUserById(Long id) {
        if(id == null){
            return null;
        }
        return wxUserMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据微信开放平台标识查询微信用户
     *
     * @param unionId 微信开放平台标识
     * @return
     */
    @Override
    public WxUser queryWxUserByUnionId(String unionId) {
        if(StringUtils.isBlank(unionId)){
            return null;
        }
        return wxUserMapper.selectWxUserByUnionId(unionId);
    }

    /**
     * 根据微信公众号标识查询微信用户
     *
     * @param openIdGzh 微信公众号标识
     * @return
     */
    @Override
    public WxUser queryWxUserByOpenIdGzh(String openIdGzh) {
        if(StringUtils.isBlank(openIdGzh)){
            return null;
        }
        return wxUserMapper.selectWxUserByOpenIdGzh(openIdGzh);
    }

    /**
     * 根据推荐人微信用户标识查询微信用户总数
     *
     * @param parentWxUserId
     * @return
     */
    @Override
    public int queryWxUserSumByParentWxUserId(Long parentWxUserId) {
        if(parentWxUserId == null){
            return 0;
        }else {
            return wxUserMapper.selectWxUserSumByParentWxUserId(parentWxUserId);
        }
    }

    /**
     * 根据推荐人微信用户标识查询微信用户集合（分页）
     *
     * @param parentWxUserId 推荐人用户标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @Override
    public Page<WxUser> queryWxUserListByParentWxUserIdPage(Long parentWxUserId, int pageNo, int pageSize) {
        if(parentWxUserId == null){
            return null;
        }else {
            PageHelper.startPage(pageNo, pageSize);
            return wxUserMapper.selectWxUserListByParentWxUserIdPage(parentWxUserId);
        }
    }

    /**
     * 根据条件查询用户列表（分页）
     *
     * @param id         微信用户标识
     * @param nickName   微信用户昵称
     * @param orderField 排序字段
     * @param pageNo     页号
     * @param pageSize   每页显示记录数
     * @return
     */
    @Override
    public Page<WxUser> queryWxUserListByConditionPage(Long id, String nickName, String orderField, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return wxUserMapper.selectWxUserListByConditionPage(id, nickName, orderField);
    }

    /**
     * 查询可提现余额总计
     *
     * @return
     */
    @Override
    public double queryRemainingMoneySum() {
        Double sum = wxUserMapper.selectRemainingMoneySum();
        return sum == null ? 0 : sum;
    }

    /**
     * 查询累计支出提现申请金额
     *
     * @return
     */
    @Override
    public double queryWithdrawCashSum() {
        Double sum = wxUserMapper.selectWithdrawCashSum();
        return sum == null ? 0 : sum;
    }
}
