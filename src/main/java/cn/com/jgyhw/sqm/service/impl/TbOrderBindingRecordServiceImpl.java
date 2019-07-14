package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.OrderRecord;
import cn.com.jgyhw.sqm.domain.TbOrderBindingRecord;
import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.mapper.OrderRecordMapper;
import cn.com.jgyhw.sqm.mapper.TbOrderBindingRecordMapper;
import cn.com.jgyhw.sqm.service.ITbOrderBindingRecordService;
import cn.com.jgyhw.sqm.service.IWxGzhMessageSendService;
import cn.com.jgyhw.sqm.service.IWxUserService;
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

/**
 * Created by WangLei on 2019/5/22 0022 14:15
 */
@Service("tbOrderBindingRecordService")
public class TbOrderBindingRecordServiceImpl extends CommonUtil implements ITbOrderBindingRecordService {

    private static Logger LOGGER = LogManager.getLogger(TbOrderBindingRecordServiceImpl.class);

    @Autowired
    private TbOrderBindingRecordMapper tbOrderBindingRecordMapper;

    @Autowired
    private OrderRecordMapper orderRecordMapper;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    /**
     * 保存淘宝订单绑定记录
     *
     * @param tbOrderBindingRecord 淘宝订单绑定记录对象
     */
    @Transactional
    @Override
    public void saveTbOrderBindingRecord(TbOrderBindingRecord tbOrderBindingRecord) {
        if(tbOrderBindingRecord == null){
            return;
        }
        // 先查询一下订单记录里是否有本订单，切未绑定用户
        OrderRecord or = orderRecordMapper.selectOrderRecordByOrderIdAndPlatform(tbOrderBindingRecord.getTbOrderId(), Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")));
        if(or != null && or.getWxUserId() == null){
            or.setWxUserId(tbOrderBindingRecord.getWxUserId());
            tbOrderBindingRecord.setStatus(Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_ybd")));
            orderRecordMapper.updateByPrimaryKey(or);
            // 判断用户是否关注公众号, 发送订单确认通知
            WxUser wu = wxUserService.queryWxUserById(or.getWxUserId());
            if(wu != null && !StringUtils.isBlank(wu.getOpenIdGzh())){
                this.sendAffirmOrderWxMessage(wxGzhMessageSendService, wu.getOpenIdGzh(), or.getOrderId(), "已绑定");
            }
        }
        tbOrderBindingRecordMapper.insert(tbOrderBindingRecord);
    }

    /**
     * 修改淘宝订单绑定记录
     *
     * @param tbOrderBindingRecord 淘宝订单绑定记录对象
     */
    @Override
    public void updateTbOrderBindingRecord(TbOrderBindingRecord tbOrderBindingRecord) {
        if(tbOrderBindingRecord == null){
            return;
        }
        tbOrderBindingRecordMapper.updateByPrimaryKey(tbOrderBindingRecord);
    }

    /**
     * 根据淘宝订单编号查询淘宝订单绑定记录
     *
     * @param tbOrderId 淘宝订单编号
     * @return
     */
    @Override
    public TbOrderBindingRecord queryTbOrderBindingRecordByTbOrderId(String tbOrderId) {
        if(StringUtils.isBlank(tbOrderId)){
            return null;
        }
        return tbOrderBindingRecordMapper.selectTbOrderBindingRecordByTbOrderId(tbOrderId);
    }

    /**
     * 根据用户标识和状态查询淘宝订单绑定记录集合（分页）
     *
     * @param wxUserId 用户标识
     * @param status   淘宝订单绑定记录状态
     * @param pageNo   页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @Override
    public Page<TbOrderBindingRecord> queryTbOrderBindingRecordListByWxUserIdAndStatusPage(Long wxUserId, Integer status, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return tbOrderBindingRecordMapper.selectTbOrderBindingRecordListByWxUserIdAndStatusPage(wxUserId, status);
    }


}
