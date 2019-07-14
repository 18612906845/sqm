package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import cn.com.jgyhw.sqm.domain.WithdrawCashRecord;
import cn.com.jgyhw.sqm.mapper.WithdrawCashRecordMapper;
import cn.com.jgyhw.sqm.service.IReturnMoneyChangeRecordService;
import cn.com.jgyhw.sqm.service.IWithdrawCashRecordService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Created by WangLei on 2019/4/14 0014 11:14
 */
@Service("withdrawCashRecordService")
public class WithdrawCashRecordServiceImpl extends CommonUtil implements IWithdrawCashRecordService {

    private static Logger LOGGER = LogManager.getLogger(WithdrawCashRecordServiceImpl.class);

    @Autowired
    private WithdrawCashRecordMapper withdrawCashRecordMapper;

    @Autowired
    private IReturnMoneyChangeRecordService returnMoneyChangeRecordService;

    /**
     * 根据标识查询提现记录
     *
     * @param id 提现记录标识
     * @return
     */
    @Override
    public WithdrawCashRecord queryWithdrawCashRecordById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        return withdrawCashRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存提现记录
     *
     * @param withdrawCashRecord 提现记录对象
     */
    @Transactional
    @Override
    public void saveWithdrawCashRecord(WithdrawCashRecord withdrawCashRecord) {
        if(withdrawCashRecord == null){
            return;
        }
        withdrawCashRecordMapper.insert(withdrawCashRecord);

        // 保存返现变更记录，并修改用户可提现余额
        ReturnMoneyChangeRecord rmcr = new ReturnMoneyChangeRecord();
        rmcr.setId(UUID.randomUUID().toString());
        rmcr.setWxUserId(withdrawCashRecord.getWxUserId());
        rmcr.setChangeType(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tx")));
        rmcr.setChangeTime(withdrawCashRecord.getApplyTime());
        rmcr.setChangeMoney(withdrawCashRecord.getApplyMoney());
        rmcr.setTargetId(withdrawCashRecord.getId());

        returnMoneyChangeRecordService.saveReturnMoneyChangeRecord(rmcr);
    }

    /**
     * 修改提现记录
     *
     * @param withdrawCashRecord 提现记录对象
     */
    @Override
    public void updateWithdrawCashRecord(WithdrawCashRecord withdrawCashRecord) {
        if(withdrawCashRecord == null){
            return;
        }
        withdrawCashRecordMapper.updateByPrimaryKey(withdrawCashRecord);
    }

    /**
     * 根据条件查询申请提现记录（分页）
     *
     * @param wxUserId 申请用户标识
     * @param status   支付状态
     * @param pageNo   页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @Override
    public Page<WithdrawCashRecord> queryWithdrawCashRecordAllByConditionPage(Long wxUserId, Integer status, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return withdrawCashRecordMapper.selectWithdrawCashRecordAllByConditionPage(wxUserId, status);
    }
}
