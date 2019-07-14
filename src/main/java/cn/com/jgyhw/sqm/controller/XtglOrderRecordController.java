package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.OrderGoods;
import cn.com.jgyhw.sqm.domain.OrderRecord;
import cn.com.jgyhw.sqm.service.IOrderRecordService;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.github.pagehelper.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/5/16 0016 19:35
 *
 * 订单记录Controller
 */
@RequestMapping("/xorXtglAuth")
@Controller
public class XtglOrderRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglOrderRecordController.class);

    @Autowired
    private IOrderRecordService orderRecordService;

    /**
     * 打开订单记录页面
     * @return
     */
    @RequestMapping("/openOrderRecordListPage")
    public String openOrderRecordListPage(){
        return "xtgl/orderRecordList";
    }

    /**
     * 根据条件查询推广订单记录（分页）
     *
     * @param wxUserId 微信用户标识
     * @param orderId 订单编号
     * @param platform 订单平台
     * @param status 订单状态
     * @param orderField 排序字段
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findOrderRecordListByConditionPage")
    @ResponseBody
    public Map<String, Object> findOrderRecordListByConditionPage(Long wxUserId, String orderId, Integer platform, Integer status, String orderField, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "服务未知错误");
        Page<OrderRecord> orPage = orderRecordService.queryOrderRecordListByConditionPage(wxUserId, orderId, platform, status, orderField, pageNo, pageSize);

        List<OrderRecord> orList = orPage.getResult();

        if(orList != null && !orList.isEmpty()){
            for(OrderRecord or : orList){
                or = this.orderRecordToOrderRecordPojo(or);
                List<OrderGoods> ogList = orderRecordService.queryOrderGoodsListByOrderId(or.getId());
                if(ogList != null && !ogList.isEmpty()){
                    or.setReturnScale(ogList.get(0).getReturnScale());
                }
            }
        }
        resultMap.put("data", orList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", orPage.getTotal());
        return resultMap;
    }

}
