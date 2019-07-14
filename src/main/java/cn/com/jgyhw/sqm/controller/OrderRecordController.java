package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.OrderRecord;
import cn.com.jgyhw.sqm.service.IOrderRecordService;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.jd.open.api.sdk.internal.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by WangLei on 2019/4/13 0013 20:45
 *
 * 订单记录Controller
 */
@RequestMapping("/orderRecord")
@Controller
public class OrderRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(OrderRecordController.class);

    @Autowired
    private IOrderRecordService orderRecordService;

    /**
     * 根据登陆标识和类型查询订单集合
     *
     * @param loginKey 登陆标识
     * @param type 订单状态
     * @return
     */
    @RequestMapping("/findOrderRecordListByLoginKeyAndType")
    @ResponseBody
    public Map<String, Object> findOrderRecordListByLoginKeyAndType(Long loginKey, String type){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        if(loginKey == null){
            resultMap.put("orderList", new ArrayList<>());
            resultMap.put("status", true);
            resultMap.put("msg", "");
            return resultMap;
        }
        List<OrderRecord> orderList = orderRecordService.queryOrderRecordListByWxUserIdAndStatus(loginKey, type);
        if(orderList != null && !orderList.isEmpty()){
            for(OrderRecord or : orderList){
                this.orderRecordToOrderRecordPojo(or);
            }
        }
        resultMap.put("orderList", orderList);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

}
