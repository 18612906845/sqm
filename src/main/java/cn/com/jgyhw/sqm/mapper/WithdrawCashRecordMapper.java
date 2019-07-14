package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WithdrawCashRecordMapper {

    int deleteByPrimaryKey(String id);

    int insert(WithdrawCashRecord record);

    int insertSelective(WithdrawCashRecord record);

    WithdrawCashRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WithdrawCashRecord record);

    int updateByPrimaryKey(WithdrawCashRecord record);

    /**
     * 根据条件查询申请提现记录（分页）
     *
     * @param wxUserId 申请用户标识
     * @param status 支付状态
     * @return
     */
    Page<WithdrawCashRecord> selectWithdrawCashRecordAllByConditionPage(@Param("wxUserId") Long wxUserId, @Param("status") Integer status);
}