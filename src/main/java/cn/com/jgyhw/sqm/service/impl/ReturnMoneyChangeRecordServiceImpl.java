package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.mapper.ReturnMoneyChangeRecordMapper;
import cn.com.jgyhw.sqm.mapper.WxUserMapper;
import cn.com.jgyhw.sqm.service.IReturnMoneyChangeRecordService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by WangLei on 2019/4/11 0011 17:51
 */
@Service("returnMoneyChangeRecordService")
public class ReturnMoneyChangeRecordServiceImpl extends CommonUtil implements IReturnMoneyChangeRecordService {

    private static Logger LOGGER = LogManager.getLogger(ReturnMoneyChangeRecordServiceImpl.class);

    @Autowired
    private WxUserMapper wxUserMapper;

    @Autowired
    private ReturnMoneyChangeRecordMapper returnMoneyChangeRecordMapper;

    /**
     * 保存返现变更记录，同时变更用户表可取现金额
     *
     * @param returnMoneyChangeRecord 返现金额变更记录对象
     */
    @Transactional
    @Override
    public synchronized void saveReturnMoneyChangeRecord(ReturnMoneyChangeRecord returnMoneyChangeRecord) {
        if(returnMoneyChangeRecord == null){
            return;
        }
        // 根据微信用户标识查询可提现金额
        WxUser wu = wxUserMapper.selectByPrimaryKey(returnMoneyChangeRecord.getWxUserId());
        if(wu != null){
            double remainingMoney = wu.getRemainingMoney();// 用户可提现金额
            // 根据变更类型，对余额做相信操作
            double newRemainingMoney = wu.getRemainingMoney();
            if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_rz")) == returnMoneyChangeRecord.getChangeType()){// 订单解冻入账
                newRemainingMoney = remainingMoney + returnMoneyChangeRecord.getChangeMoney();
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tx")) == returnMoneyChangeRecord.getChangeType()){// 用户提现
                newRemainingMoney = remainingMoney - returnMoneyChangeRecord.getChangeMoney();
                // 计算累计提现
                double withdrawCashSum = wu.getWithdrawCashSum() + returnMoneyChangeRecord.getChangeMoney();
                wu.setWithdrawCashSum(this.formatDouble(withdrawCashSum));
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_tc")) == returnMoneyChangeRecord.getChangeType()){// 邀请提成
                newRemainingMoney = remainingMoney + returnMoneyChangeRecord.getChangeMoney();
            }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("return_money_change_type_yx")) == returnMoneyChangeRecord.getChangeType()){// 邀新奖励
                newRemainingMoney = remainingMoney + returnMoneyChangeRecord.getChangeMoney();
            }
            wu.setRemainingMoney(this.formatDouble(newRemainingMoney));
            wxUserMapper.updateByPrimaryKey(wu);
        }
        returnMoneyChangeRecordMapper.insert(returnMoneyChangeRecord);
    }

    /**
     * 根据微信用户标识、变更类型、主体id、订单编号查询返现变更记录
     *
     * @param wxUserId   微信用户标识
     * @param changeType 变更类型
     * @param targetId   主体id
     * @param orderId    订单编号
     * @return
     */
    @Override
    public ReturnMoneyChangeRecord queryReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(Long wxUserId, int changeType, String targetId, String orderId) {
        return returnMoneyChangeRecordMapper.selectReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(wxUserId, changeType, targetId, orderId);
    }

    /**
     * 根据微信用户标识查询返现变更记录集合（分页）
     *
     * @param wxUserId   微信用户标识
     * @param changeType 变更类型
     * @param pageNo     页号
     * @param pageSize   每页显示记录数
     * @return
     */
    @Override
    public Page<ReturnMoneyChangeRecord> queryReturnMoneyChangeRecordListByWxUserIdAndChangTypePage(Long wxUserId, int changeType, int pageNo, int pageSize) {
        if(wxUserId == null){
            return null;
        }
        PageHelper.startPage(pageNo, pageSize);
        return returnMoneyChangeRecordMapper.selectReturnMoneyChangeRecordListByWxUserIdAndChangeTypePage(wxUserId, changeType);
    }
}
