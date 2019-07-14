package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * Created by WangLei on 2019/4/11 0011 17:44
 *
 * 返现金额变更记录接口
 */
public interface IReturnMoneyChangeRecordService {

    /**
     * 保存返现变更记录，同时变更用户表可取现金额
     *
     * @param returnMoneyChangeRecord 返现金额变更记录对象
     */
    void saveReturnMoneyChangeRecord(ReturnMoneyChangeRecord returnMoneyChangeRecord);

    /**
     * 根据微信用户标识、变更类型、主体id、订单编号查询返现变更记录
     *
     * @param wxUserId 微信用户标识
     * @param changeType 变更类型
     * @param targetId 主体id
     * @param orderId 订单编号
     * @return
     */
    ReturnMoneyChangeRecord queryReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(Long wxUserId, int changeType, String targetId, String orderId);

    /**
     * 根据微信用户标识查询返现变更记录集合（分页）
     *
     * @param wxUserId 微信用户标识
     * @param changeType 变更类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<ReturnMoneyChangeRecord> queryReturnMoneyChangeRecordListByWxUserIdAndChangTypePage(Long wxUserId, int changeType, int pageNo, int pageSize);
}
