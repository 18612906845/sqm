package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/4/14 0014 11:12
 *
 * 提现记录接口
 */
public interface IWithdrawCashRecordService {

    /**
     * 根据标识查询提现记录
     *
     * @param id 提现记录标识
     * @return
     */
    WithdrawCashRecord queryWithdrawCashRecordById(String id);

    /**
     * 保存提现记录
     *
     * @param withdrawCashRecord 提现记录对象
     */
    void saveWithdrawCashRecord(WithdrawCashRecord withdrawCashRecord);

    /**
     * 修改提现记录
     *
     * @param withdrawCashRecord 提现记录对象
     */
    void updateWithdrawCashRecord(WithdrawCashRecord withdrawCashRecord);

    /**
     * 根据条件查询申请提现记录（分页）
     *
     * @param wxUserId 申请用户标识
     * @param status 支付状态
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<WithdrawCashRecord> queryWithdrawCashRecordAllByConditionPage(Long wxUserId, Integer status, int pageNo, int pageSize);
}
