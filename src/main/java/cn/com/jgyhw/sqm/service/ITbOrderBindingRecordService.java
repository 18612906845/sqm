package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.TbOrderBindingRecord;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/5/22 0022 14:05
 *
 * 淘宝订单绑定记录接口
 */
public interface ITbOrderBindingRecordService {

    /**
     * 保存淘宝订单绑定记录
     *
     * @param tbOrderBindingRecord 淘宝订单绑定记录对象
     */
    void saveTbOrderBindingRecord(TbOrderBindingRecord tbOrderBindingRecord);

    /**
     * 修改淘宝订单绑定记录
     *
     * @param tbOrderBindingRecord 淘宝订单绑定记录对象
     */
    void updateTbOrderBindingRecord(TbOrderBindingRecord tbOrderBindingRecord);

    /**
     * 根据淘宝订单编号查询淘宝订单绑定记录
     *
     * @param tbOrderId 淘宝订单编号
     * @return
     */
    TbOrderBindingRecord queryTbOrderBindingRecordByTbOrderId(String tbOrderId);

    /**
     * 根据用户标识和状态查询淘宝订单绑定记录集合（分页）
     *
     * @param wxUserId 用户标识
     * @param status 淘宝订单绑定记录状态
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<TbOrderBindingRecord> queryTbOrderBindingRecordListByWxUserIdAndStatusPage(Long wxUserId, Integer status, int pageNo, int pageSize);
}
