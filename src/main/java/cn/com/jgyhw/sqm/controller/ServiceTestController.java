package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import cn.com.jgyhw.sqm.pojo.JdGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.PddDdkGoodsSearchRequestPojo;
import cn.com.jgyhw.sqm.pojo.PddGoodsDiscountsQueryResultPojo;
import cn.com.jgyhw.sqm.pojo.TimingTaskQueryResultPojo;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import jd.union.open.goods.jingfen.query.request.JFGoodsReq;
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
 * Created by WangLei on 2019/4/11 0011 20:41
 *
 * 服务测试
 */
@RequestMapping("/serviceTest")
@Controller
public class ServiceTestController {

    private static Logger LOGGER = LogManager.getLogger(ServiceTestController.class);

    @Autowired
    private IJdSearchService jdSearchService;

    @Autowired
    private IPddSearchService pddSearchService;

    @Autowired
    private ITbSearchService tbSearchService;

    @Autowired
    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    @Autowired
    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    @Autowired
    private IDiscountsGoodsClassifyService discountsGoodsClassifyService;

    /**
     * 更新京东订单
     *
     * @param queryTimeStr 查询时间
     * @param queryTimeType 查询时间类型
     * @return
     */
    @RequestMapping("/updateJdOrderInfoByTimeTest")
    @ResponseBody
    public Map<String, Object> updateJdOrderInfoByTimeTest(String queryTimeStr, int queryTimeType, boolean isUnfreeze){
        Map<String, Object> resultMap = new HashMap<>();

        jdSearchService.updateJdOrderInfoByTime(queryTimeStr, queryTimeType, isUnfreeze, 1, 100);

        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 更新京东订单（测试多线程是否安全）
     * @return
     */
    @RequestMapping("/updateJdOrderInfoByTimeThreadTest")
    @ResponseBody
    public String updateJdOrderInfoByTimeThreadTest(String queryTimeStr, int queryTimeType){
        //创建启动10个线程
        for(int i=0; i<10; i++){

            Thread t1 = new Thread(){
                @Override
                public void run() {
                    TimingTaskQueryResultPojo ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, queryTimeType,false, 1, 100);

                    System.out.println("线程" + Thread.currentThread().getName() + "的查询结果，是否还有更多：" + ttqrp.isMore());
                }
            };
            //线程启动
            t1.start();
            //确保创建的线程的优先级一样
            t1.setPriority(Thread.NORM_PRIORITY);
        }
        return "success";
    }

    /**
     * 更新拼多多订单
     *
     * @param startTime 查询时间开始，更新时间
     * @param endTime 查询时间结束
     * @return
     */
    @RequestMapping("/updatePddOrderInfoByTimeTest")
    @ResponseBody
    public Map<String, Object> updatePddOrderInfoByTimeTest(Long startTime, Long endTime){
        Map<String, Object> resultMap = new HashMap<>();

        pddSearchService.updatePddOrderInfoByTime(startTime, endTime, 1, 100);

        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 更新淘宝订单
     *
     * @param queryTimeStr 订单查询开始时间，例如：2016-05-23 12:18:22
     * @param queryTimeType 订单查询类型，创建时间“create_time”，或结算时间“settle_time”。
     * @return
     */
    @RequestMapping("/updateTbOrderInfoByTimeTest")
    @ResponseBody
    public Map<String, Object> updateTbOrderInfoByTimeTest(String queryTimeStr, String queryTimeType){
        Map<String, Object> resultMap = new HashMap<>();

        tbSearchService.updateTbOrderInfoByTime(queryTimeStr, queryTimeType, 1200L, 1,100);

        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 循环查询所有类型拼多多商品优惠并存库
     *
     * @return
     */
    @RequestMapping("/searchPddGoodsDiscountsTest")
    @ResponseBody
    public Map<String, Object> searchPddGoodsDiscountsTest(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);

        List<DiscountsGoodsClassify> dgcList = discountsGoodsClassifyService.queryDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));

        if(dgcList == null || dgcList.isEmpty()){
            resultMap.put("status", false);
            resultMap.put("msg", "无可用商品类型");
            return resultMap;
        }

        for(DiscountsGoodsClassify dgc : dgcList){
            pddGoodsDiscountsService.deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(dgc.getClassifyId(), null);

            PddDdkGoodsSearchRequestPojo pdgsrp = new PddDdkGoodsSearchRequestPojo();
            pdgsrp.setOptId(Long.valueOf(dgc.getClassifyId()));

            boolean flag = true;
            int index = 1;
            PddGoodsDiscountsQueryResultPojo pgdqrp = null;
            do {
                LOGGER.info("查询拼多多" + dgc.getClassifyId() + "（ " + dgc.getClassifyName() + " ）类型商品，第" + index + "页");
                pdgsrp.setPage(index);
                pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, false);
                if(pgdqrp == null){
                    flag = false;
                }else{
                    flag = pgdqrp.isMore();
                }
                if(index >= 10){
                    flag = false;
                }
                index ++;
            } while (flag);
        }


        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 循环查询所有类型京东商品优惠并存库
     *
     * @return
     */
    @RequestMapping("/searchJdGoodsDiscountsTest")
    @ResponseBody
    public Map<String, Object> searchJdGoodsDiscountsTest(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);

        List<DiscountsGoodsClassify> dgcList = discountsGoodsClassifyService.queryDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));

        if(dgcList == null || dgcList.isEmpty()){
            resultMap.put("status", false);
            resultMap.put("msg", "无可用商品类型");
            return resultMap;
        }

        for(DiscountsGoodsClassify dgc : dgcList){

            jdGoodsDiscountsService.deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(dgc.getClassifyId(), null);

            JFGoodsReq jfgr = new JFGoodsReq();
            jfgr.setEliteId(Integer.valueOf(dgc.getClassifyId()));
            jfgr.setPageSize(50);

            boolean flag = true;
            int index = 1;
            JdGoodsDiscountsQueryResultPojo jgdqrp = null;
            do {
                LOGGER.info("查询京粉" + dgc.getClassifyId() + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页");
                jfgr.setPageIndex(index);
                jgdqrp = jdSearchService.syncJdJingFenGoodsDiscounts(jfgr);
                if(jgdqrp == null){
                    flag = false;
                }else{
                    flag = jgdqrp.isMore();
                }
                if(index >= 10){
                    flag = false;
                }
                index ++;
            } while (flag);
        }
        resultMap.put("status", true);
        return resultMap;
    }

}
