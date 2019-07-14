package cn.com.jgyhw.sqm.task;

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
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by WangLei on 2019/4/14 0014 23:24
 * <p>
 * 定时任务
 */
@Component
@EnableScheduling   // 1.开启定时任务
@EnableAsync        // 2.开启多线程
public class MultithreadScheduleTask {

    private static Logger LOGGER = LogManager.getLogger(MultithreadScheduleTask.class);

    private static final SimpleDateFormat SDF_YYYYMMDDHH = new SimpleDateFormat("yyyyMMddHH");
    private static final SimpleDateFormat SDF_YYYY_MM_DD_HH_HH_MM_SS = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Autowired
    private IWxTokenService wxTokenService;

    @Autowired
    private IJdSearchService jdSearchService;

    @Autowired
    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    @Autowired
    private IPddSearchService pddSearchService;

    @Autowired
    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    @Autowired
    private IDiscountsGoodsClassifyService discountsGoodsClassifyService;

    @Autowired
    private ITbSearchService tbSearchService;

    /**
     * 一分钟任务
     */
    @Async
    @Scheduled(cron = "0 * * * * ?")
    public void oneMinuteTask() {
        LOGGER.info("一分钟定时任务开始");
        updateJdOrder();
        updatePddOrder();
        updateTbOrder();
        LOGGER.info("一分钟定时任务结束");
    }

    /**
     * 更新京东订单
     */
    private void updateJdOrder() {
        //获取当前时间
        Calendar cl = Calendar.getInstance();
        //查询京东当前小时订单
        String queryTimeStr = SDF_YYYYMMDDHH.format(cl.getTime());
        LOGGER.info("更新京东订单信息--间隔1分钟定时任务--> 时间条件：更新时间，时间值：" + queryTimeStr);
        int pageNumber = 1;
        boolean hasMore = false;
        do {
            LOGGER.info("更新京东订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber);
            TimingTaskQueryResultPojo ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_gxsj")), false, pageNumber, 100);
            if (ttqrp.isStatus() == false) {
                LOGGER.info("更新京东订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_gxsj")), false, pageNumber, 100);
                LOGGER.info("更新京东订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
            }
            hasMore = ttqrp.isMore();
            pageNumber++;
        } while (hasMore);

        // 订单解冻
        pageNumber = 1;
        hasMore = false;
        cl.add(Calendar.DATE, -8);
        cl.add(Calendar.HOUR, -1);
        queryTimeStr = SDF_YYYYMMDDHH.format(cl.getTime());
        LOGGER.info("解冻京东订单--间隔1分钟定时任务--> 时间条件：完成时间，时间值：" + queryTimeStr);
        do {
            LOGGER.info("解冻京东订单--间隔1分钟定时任务--> 查询页数：" + pageNumber);
            TimingTaskQueryResultPojo ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_wcsj")), true, pageNumber, 100);
            if (ttqrp.isStatus() == false) {
                LOGGER.info("解冻京东订单--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_wcsj")), true, pageNumber, 100);
                LOGGER.info("解冻京东订单--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
            }
            hasMore = ttqrp.isMore();
            pageNumber++;
        } while (hasMore);
    }

    /**
     * 更新拼多多订单
     */
    private void updatePddOrder() {
        // 查询拼多多过去24小时内有更新的订单
        Date queryDate = new Date();
        Long queryEndTime = queryDate.getTime() / 1000;// 秒值
        Long queryStartTime = queryEndTime - (60 * 60 * 24);
        LOGGER.info("更新拼多多订单信息--间隔1分钟定时任务--> 时间条件：更新时间，时间值开始：" + queryStartTime + "；时间值结束：" + queryEndTime);
        int pageNumber = 1;
        boolean hasMore = false;
        do {
            LOGGER.info("更新拼多多订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber);
            TimingTaskQueryResultPojo ttqrp = pddSearchService.updatePddOrderInfoByTime(queryStartTime, queryEndTime, pageNumber, 50);
            if (ttqrp.isStatus() == false) {
                LOGGER.info("更新拼多多订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                ttqrp = pddSearchService.updatePddOrderInfoByTime(queryStartTime, queryEndTime, pageNumber, 50);
                LOGGER.info("更新拼多多订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
            }
            hasMore = ttqrp.isMore();
            pageNumber++;
        } while (hasMore);

        // 订单解冻
        Date finishTimeDate = new Date(new Date().getTime() - 1000 * 60 * 60 * 24 * 8);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        pddSearchService.unfreezePddOrderByFinishTimeStr(sdf.format(finishTimeDate));
    }

    /**
     * 更新淘宝订单
     */
    private void updateTbOrder() {
        // 查询淘宝今天和之前25天当前时间点下单的订单
        Calendar cl = Calendar.getInstance();//获取当前时间
        int fwMinute = 10;// 查询时间范围（分钟）
        cl.add(Calendar.MINUTE, -fwMinute);
        for (int i = 0; i < 26; i++) {
            if (i > 0) {
                cl.add(Calendar.DATE, -1);
            }
            String queryTimeStr = SDF_YYYY_MM_DD_HH_HH_MM_SS.format(cl.getTime());
            LOGGER.info("更新淘宝订单信息--间隔1分钟定时任务--> 时间条件：下单时间，时间值：" + queryTimeStr);
            int pageNumber = 1;
            boolean hasMore = false;
            do {
                LOGGER.info("更新淘宝订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber);
                TimingTaskQueryResultPojo ttqrp = tbSearchService.updateTbOrderInfoByTime(queryTimeStr, "create_time", Long.valueOf(60 * fwMinute), pageNumber, 100);
                if (ttqrp.isStatus() == false) {
                    LOGGER.info("更新淘宝订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                    //重试一次
                    ttqrp = tbSearchService.updateTbOrderInfoByTime(queryTimeStr, "create_time", Long.valueOf(60 * fwMinute), pageNumber, 100);
                    LOGGER.info("更新淘宝订单信息--间隔1分钟定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
                }
                hasMore = ttqrp.isMore();
                pageNumber++;
            } while (hasMore);
        }
    }

    /**
     * 一小时任务
     */
    @Async
    @Scheduled(cron = "0 1 * * * ?")
    public void oneHourTask() {
        LOGGER.info("一小时定时任务开始");

        // 定时获取微信公众号服务Api令牌
        wxTokenService.timingGetWxGzhServiceApiToken();

        // 定时获取微信小程序服务Api令牌
        wxTokenService.timingGetWxXcxServiceApiToken();

        // 获取当前时间
        Calendar cl = Calendar.getInstance();
        // 查询京东过去一小时订单
        cl.add(Calendar.HOUR, -1);
        String queryTimeStr = SDF_YYYYMMDDHH.format(cl.getTime());
        LOGGER.info("更新京东订单信息--间隔1分钟定时任务--> 时间条件：更新时间，时间值（上一小时）：" + queryTimeStr);
        int pageNumber = 1;
        boolean hasMore = false;
        do {
            LOGGER.info("更新京东订单信息--间隔1小时定时任务--> 查询页数：" + pageNumber);
            TimingTaskQueryResultPojo ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_gxsj")), false, pageNumber, 100);
            if (ttqrp.isStatus() == false) {
                LOGGER.info("更新京东订单信息--间隔1小时定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_gxsj")), false, pageNumber, 100);
                LOGGER.info("更新京东订单信息--间隔1小时定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
            }
            hasMore = ttqrp.isMore();
            pageNumber++;
        } while (hasMore);

        LOGGER.info("一小时定时任务结束");
    }

    /**
     * 一小时五分任务
     */
    @Async
    @Scheduled(cron = "0 5 * * * ?")
    public void oneHourFiveMinuteTask() {
        LOGGER.info("一小时五分定时任务开始");

        // 验证京东订单是否遗漏定时任务方法
        int pageNumber = 1;
        boolean hasMore = false;
        //计算查询时间
        Calendar cl = Calendar.getInstance();
        cl.add(Calendar.DATE, -1);
        cl.add(Calendar.HOUR, -1);
        String queryTimeStr = SDF_YYYYMMDDHH.format(cl.getTime());
        do {
            LOGGER.info("验证京东订单是否有遗漏--每小时5分钟定时任务--> 查询页数：" + pageNumber);
            TimingTaskQueryResultPojo ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_xdsj")), false, pageNumber, 100);
            if (ttqrp.isStatus() == false) {
                LOGGER.info("验证京东订单是否有遗漏--每小时5分钟定时任务--> 查询页数：" + pageNumber + "；异常，重试一次");
                ttqrp = jdSearchService.updateJdOrderInfoByTime(queryTimeStr, Integer.valueOf(ApplicationParamConstant.JD_PARAM_MAP.get("jd_order_query_time_type_xdsj")), false, pageNumber, 100);
                LOGGER.info("验证京东订单是否有遗漏--每小时5分钟定时任务--> 查询页数：" + pageNumber + "；重试一次，结果：" + ttqrp.isStatus());
            }
            hasMore = ttqrp.isMore();
            pageNumber++;
        } while (hasMore);

        LOGGER.info("一小时五分定时任务结束");
    }

    /**
     * 二小时十分任务（京粉推荐定时更新）
     */
    @Async
    @Scheduled(cron = "0 10 0/2 * * ?")
    public void twoHourTenMinuteTask() {
        LOGGER.info("二小时十分定时任务开始");

        List<DiscountsGoodsClassify> dgcList = discountsGoodsClassifyService.queryDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")));

        if (dgcList == null || dgcList.isEmpty()) {
            LOGGER.info("无可用京东商品类型");
            return;
        }

        for (DiscountsGoodsClassify dgc : dgcList) {
            String classifyId = dgc.getClassifyId();

            jdGoodsDiscountsService.deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(classifyId, null);

            JFGoodsReq jfgr = new JFGoodsReq();
            jfgr.setEliteId(Integer.valueOf(classifyId));
            jfgr.setPageSize(50);

            boolean flag = true;
            int index = 1;
            JdGoodsDiscountsQueryResultPojo jgdqrp = null;
            do {
                LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页");
                jfgr.setPageIndex(index);
                jgdqrp = jdSearchService.syncJdJingFenGoodsDiscounts(jfgr);
                if (jgdqrp.isStatus()) {
                    flag = jgdqrp.isMore();
                    if (index >= 10) {
                        flag = false;
                    }
                    index++;
                    LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，查询第1次成功");
                } else {
                    LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，查询第1次失败，重试第1次");
                    jgdqrp = jdSearchService.syncJdJingFenGoodsDiscounts(jfgr);
                    if (jgdqrp.isStatus()) {
                        flag = jgdqrp.isMore();
                        if (index >= 10) {
                            flag = false;
                        }
                        index++;
                        LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第1次成功");
                    } else {
                        LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第1次失败，重试第2次");
                        jgdqrp = jdSearchService.syncJdJingFenGoodsDiscounts(jfgr);
                        if (jgdqrp.isStatus()) {
                            flag = jgdqrp.isMore();
                            if (index >= 10) {
                                flag = false;
                            }
                            index++;
                            LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第2次成功");
                        } else {
                            LOGGER.info("查询京粉" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第2次失败，查询下一页");
                        }
                    }
                }
            } while (flag);
        }

        LOGGER.info("二小时十分定时任务结束");
    }


    /**
     * 二小时二十分任务（拼多多推荐定时更新）
     */
    @Async
    @Scheduled(cron = "0 20 0/2 * * ?")
    public void twoHourTwentyMinuteTask() {
        LOGGER.info("二小时二十分定时任务开始");

        List<DiscountsGoodsClassify> dgcList = discountsGoodsClassifyService.queryDiscountsGoodsClassifyListByPlatform(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")));

        if (dgcList == null || dgcList.isEmpty()) {
            LOGGER.info("无可用拼多多商品类型");
            return;
        }

        for (DiscountsGoodsClassify dgc : dgcList) {
            String classifyId = dgc.getClassifyId();

            pddGoodsDiscountsService.deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(classifyId, null);

            Long optId = Long.valueOf(classifyId);

            PddDdkGoodsSearchRequestPojo pdgsrp = new PddDdkGoodsSearchRequestPojo();
            pdgsrp.setOptId(optId);

            boolean flag = true;
            int index = 1;
            PddGoodsDiscountsQueryResultPojo pgdqrp = null;
            do {
                LOGGER.info("查询拼多多" + classifyId + "类型商品，第" + index + "页");
                pdgsrp.setPage(index);
                pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, false);
                if (pgdqrp.isStatus()) { // 查询第一次，成功
                    flag = pgdqrp.isMore();
                    if (index >= 10) {
                        flag = false;
                    }
                    index++;
                    LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，查询第1次成功");
                } else {// 查询第一次，失败
                    LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，查询第1次失败，重试第1次");
                    pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, false);
                    if (pgdqrp.isStatus()) {
                        LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第1次成功");
                        flag = pgdqrp.isMore();
                        if (index >= 10) {
                            flag = false;
                        }
                        index++;
                    } else {
                        LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第1次失败，重试第2次");
                        pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, false);
                        if (pgdqrp.isStatus()) {
                            LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第2次成功");
                            flag = pgdqrp.isMore();
                            if (index >= 10) {
                                flag = false;
                            }
                            index++;
                        } else {
                            LOGGER.info("查询拼多多" + classifyId + "（" + dgc.getClassifyName() + "）类型商品，第" + index + "页，重试第2次失败，查询下一页");
                        }
                    }
                }
            } while (flag);
        }

        LOGGER.info("二小时二十分定时任务结束");
    }

    /**
     * 凌晨三点任务（删除当天查询过的商品）
     */
    @Async
    @Scheduled(cron = "0 0 3 * * ?")
    public void threeInTheMorningTask() {
        LOGGER.info("凌晨三点任务开始");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String updateTimeStr = sdf.format(new Date());

        jdGoodsDiscountsService.deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime("0", updateTimeStr);
        pddGoodsDiscountsService.deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime("0", updateTimeStr);

        LOGGER.info("凌晨三点任务结束");
    }

}
