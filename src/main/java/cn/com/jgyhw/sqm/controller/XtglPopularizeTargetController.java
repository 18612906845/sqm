package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import cn.com.jgyhw.sqm.service.IPopularizeTargetService;
import cn.com.jgyhw.sqm.service.IWxQrCodeService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import com.github.pagehelper.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by WangLei on 2019/5/15 0015 21:56
 *
 * 线下推广码Controller
 */
@RequestMapping("/xptXtglAuth")
@Controller
public class XtglPopularizeTargetController extends CommonUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglPopularizeTargetController.class);

    @Autowired
    private IPopularizeTargetService popularizeTargetService;

    @Autowired
    private IWxQrCodeService wxQrCodeService;

    /**
     * 打开线下推广码页面
     * @return
     */
    @RequestMapping("/openPopularizeTargetPage")
    public String openPopularizeTargetPage(){
        return "xtgl/popularizeTargetList";
    }

    /**
     * 打开线下推广码批量创建页面
     * @return
     */
    @RequestMapping("/openPopularizeTargetNewPage")
    public String openPopularizeTargetNewPage(){
        return "xtgl/popularizeTargetNew";
    }

    /**
     * 根据绑定用户标识和关键字查询推广主体列表（分页）
     *
     * @param wxUserId 绑定微信用户标识
     * @param keyword 关键词
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findPopularizeTargetByPage")
    @ResponseBody
    public Map<String, Object> findPopularizeTargetByPage(Long wxUserId, String keyword, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "服务未知错误");
        Page<PopularizeTarget> ptPage = popularizeTargetService.queryPopularizeTargetByWxUserIdAndKeywordPage(wxUserId, keyword, pageNo, pageSize);

        List<PopularizeTarget> ptList = ptPage.getResult();

        if(ptList != null && !ptList.isEmpty()){
            for(PopularizeTarget pt : ptList){
                pt.setQrCodeUrl(ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix") + "/" + pt.getId() + ".jpg");
                if(pt.getCreateTime() != null){
                    pt.setCreateTimeStr(this.dateToStringByFormat(pt.getCreateTime(), null));
                }
                if(pt.getBindingTime() != null){
                    pt.setBindingTimeStr(this.dateToStringByFormat(pt.getBindingTime(), null));
                }
                if(pt.getUpdateTime() != null){
                    pt.setUpdateTimeStr(this.dateToStringByFormat(pt.getUpdateTime(), null));
                }
            }
        }
        resultMap.put("data", ptList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", ptPage.getTotal());
        return resultMap;
    }

    /**
     * 根据推广主体标识修改推广主体
     *
     * @param id 推广主体标识
     * @param field 修改的字段名
     * @param value 修改的字段值
     * @return
     */
    @RequestMapping("/updatePopularizeTargetById")
    @ResponseBody
    public Map<String, Object> updatePopularizeTargetById(Long id, String field, String value){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetById(id);
        if(pt == null){
            return resultMap;
        }
        try {
            Field f = pt.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(pt, value);
            popularizeTargetService.updatePopularizeTarget(pt);
            resultMap.put("status", true);
        } catch (Exception e) {
            LOGGER.error("Java反射动态赋值异常", e);
        }
        return resultMap;
    }


    /**
     * 批量创建线下推广码
     *
     * @param size 批量创建个数
     * @return
     */
    @RequestMapping("/batchCreatePopularizeTargetBySize")
    @ResponseBody
    public Map<String, Object> batchCreatePopularizeTargetBySize(Integer size){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);

        if(size > 0){
            for(int i=0; i<size; i++){
                PopularizeTarget pt = new PopularizeTarget();
                pt.setCreateTime(new Date());
                pt.setUpdateTime(new Date());
                String qrCodeUrl = wxQrCodeService.createWxXcxInfinitudeQrCodeByPopularize(pt);

                pt.setQrCodeUrl(qrCodeUrl);
            }
            resultMap.put("status", true);
        }
        return resultMap;
    }

}
