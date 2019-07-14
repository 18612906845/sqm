package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.TbOrderBindingRecord;
import cn.com.jgyhw.sqm.service.ITbOrderBindingRecordService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import com.github.pagehelper.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by WangLei on 2019/5/22 0022 14:26
 *
 * 淘宝订单绑定记录Controller
 */
@RequestMapping("/tbOrderBindingRecord")
@Controller
public class TbOrderBindingRecordController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(TbOrderBindingRecordController.class);

    @Autowired
    private ITbOrderBindingRecordService tbOrderBindingRecordService;

    /**
     * 添加淘宝订单绑定记录
     *
     * @param loginKey 登陆标识
     * @param tbOrderId 淘宝订单编号
     * @return
     */
    @RequestMapping("/addTbOrderBindingRecord")
    @ResponseBody
    public Map<String, Object> addTbOrderBindingRecord(Long loginKey, String tbOrderId){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        if(loginKey == null || loginKey == 0){
            resultMap.put("msg", "请先登陆");
            return resultMap;
        }
        if(StringUtils.isBlank(tbOrderId)){
            resultMap.put("msg", "订单编号有误");
            return resultMap;
        }
        // 查询是否已有订单
        TbOrderBindingRecord tobr = tbOrderBindingRecordService.queryTbOrderBindingRecordByTbOrderId(tbOrderId);
        if(tobr == null){// 不存在，新建
            tobr = new TbOrderBindingRecord();
            tobr.setId(UUID.randomUUID().toString());
            tobr.setTbOrderId(tbOrderId);
            tobr.setWxUserId(loginKey);
            tobr.setStatus(Integer.valueOf(ApplicationParamConstant.TB_PARAM_MAP.get("tb_order_binding_status_wbd")));
            tobr.setCreateTime(new Date());
            tobr.setUpdateTime(new Date());
            tbOrderBindingRecordService.saveTbOrderBindingRecord(tobr);resultMap.put("status", true);
            resultMap.put("msg", "");
            return resultMap;
        }else{
            resultMap.put("msg", "订单已存在");
            return resultMap;
        }
    }

    /**
     * 根据用户标识查询淘宝订单绑定记录（分页）
     *
     * @param loginKey 登陆标识
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findTbOrderBindingRecordListByWxUserIdPage")
    @ResponseBody
    public Map<String, Object> findTbOrderBindingRecordListByWxUserIdPage(Long loginKey, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        resultMap.put("isMore", true);
        if(loginKey == null || loginKey == 0){
            resultMap.put("msg", "请先登陆");
            return resultMap;
        }
        // 查询是否已有订单
        Page<TbOrderBindingRecord> tobrPage = tbOrderBindingRecordService.queryTbOrderBindingRecordListByWxUserIdAndStatusPage(loginKey, 0, pageNo, pageSize);

        List<TbOrderBindingRecord> tbOrderBindingRecordList = new ArrayList<>();

        if(tobrPage != null && tobrPage.getTotal() > 0){

            resultMap.put("total", tobrPage.getTotal());

            for(TbOrderBindingRecord tobr : tobrPage.getResult()){
                TbOrderBindingRecord tbOrderBindingRecord = this.tbOrderBindingRecordToPojo(tobr);
                if(tbOrderBindingRecord == null){
                    continue;
                }
                tbOrderBindingRecordList.add(tbOrderBindingRecord);
            }
            // 判断是否还有更多
            long count = pageNo * pageSize;
            if(count >= tobrPage.getTotal()){
                resultMap.put("isMore", false);
            }
            resultMap.put("status", true);
            resultMap.put("msg", "");
        }else{
            resultMap.put("isMore", false);
        }
        resultMap.put("list", tbOrderBindingRecordList);
        return resultMap;
    }
}
