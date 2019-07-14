package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import cn.com.jgyhw.sqm.service.IPopularizeTargetService;
import cn.com.jgyhw.sqm.service.IReturnMoneyChangeRecordService;
import cn.com.jgyhw.sqm.service.IWxQrCodeService;
import cn.com.jgyhw.sqm.service.IWxUserService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangLei on 2019/5/12 0012 14:56
 *
 * 线下推广主体Controller
 */
@RequestMapping("/popularizeTarget")
@Controller
public class PopularizeTargetController {

    private static Logger LOGGER = LogManager.getLogger(PopularizeTargetController.class);

    @Autowired
    private IPopularizeTargetService popularizeTargetService;

    @Autowired
    private IWxQrCodeService wxQrCodeService;

    /**
     * 根据登陆标识查询线下推广主体
     *
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findPopularizeByLoginKey")
    @ResponseBody
    public Map<String, Object> findPopularizeByLoginKey(Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetByWxUserId(loginKey);
        if(pt == null){
            resultMap.put("msg", "");
        }else{// 返回线下推广主体信息+二维码地址
            pt.setQrCodeUrl(ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix") + "/" + pt.getId() + ".jpg");
            resultMap.put("popularizeTarget", pt);
            resultMap.put("status", true);
            resultMap.put("msg", "");
        }
        return resultMap;
    }

    /**
     * 根据登陆标识及其他参数创建或更新线下推广主体
     *
     * @param loginKey 登陆标识
     * @param targetName 推广主体名称
     * @param name 推广主体负责人姓名
     * @param phone 推广主体负责人电话
     * @param address 推广主体地址
     * @return
     */
    @RequestMapping("/saveOrUpdatePopularize")
    @ResponseBody
    public Map<String, Object> saveOrUpdatePopularize(Long loginKey, String targetName, String name, String phone, String address){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetByWxUserId(loginKey);
        if(pt == null) {
            pt = new PopularizeTarget();
            pt.setTargetName(targetName);
            pt.setName(name);
            pt.setPhone(phone);
            pt.setAddress(address);
            pt.setWxUserId(loginKey);
            pt.setCreateTime(new Date());
            pt.setBindingTime(new Date());
            pt.setUpdateTime(new Date());

            String qrCodeUrl = wxQrCodeService.createWxXcxInfinitudeQrCodeByPopularize(pt);

            LOGGER.info("生成成功，URL访问地址：" + qrCodeUrl);
            pt.setQrCodeUrl(qrCodeUrl);
        }else {
            pt.setTargetName(targetName);
            pt.setName(name);
            pt.setPhone(phone);
            pt.setAddress(address);
            pt.setUpdateTime(new Date());
            popularizeTargetService.updatePopularizeTarget(pt);

            String qrCodeUrl = ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix") + "/" + pt.getId() + ".jpg";
            pt.setQrCodeUrl(qrCodeUrl);
        }

        resultMap.put("status", true);
        resultMap.put("popularizeTarget", pt);
        resultMap.put("msg", "");

        return resultMap;
    }

    /**
     * 根据推广主体标识和登陆标识查询线下推广主体
     *
     * @param id 推广主体标识
     * @param loginKey 登陆标识
     * @return
     */
    @RequestMapping("/findPopularizeByIdAndLoginKey")
    @ResponseBody
    public Map<String, Object> findPopularizeByIdAndLoginKey(Long id, Long loginKey){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        // 判断登陆主体是否已经有绑定推广主体
        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetByWxUserId(loginKey);
        if(pt != null){
            resultMap.put("code", "100");
            resultMap.put("msg", "当前登陆人已绑定过推广主体");
            return resultMap;
        }
        pt = popularizeTargetService.queryPopularizeTargetById(id);
        if(pt == null){
            resultMap.put("msg", "");
        }else{// 返回线下推广主体信息+二维码地址
            pt.setQrCodeUrl(ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix") + "/" + pt.getId() + ".jpg");
            resultMap.put("popularizeTarget", pt);
            resultMap.put("status", true);
            resultMap.put("msg", "");
        }
        return resultMap;
    }

    /**
     * 根据推广主体标识查询线下推广主体
     *
     * @param id 推广主体标识
     * @return
     */
    @RequestMapping("/findPopularizeById")
    @ResponseBody
    public Map<String, Object> findPopularizeById(Long id){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetById(id);
        if(pt == null){
            resultMap.put("msg", "");
        }else{// 返回线下推广主体信息+二维码地址
            pt.setQrCodeUrl(ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix") + "/" + pt.getId() + ".jpg");
            resultMap.put("popularizeTarget", pt);
            resultMap.put("status", true);
            resultMap.put("msg", "");
        }
        return resultMap;
    }

    /**
     * 根据推广主体标识绑定线下推广主体
     *
     * @param id 推广主体标识
     * @param loginKey 登陆用户标识
     * @param targetName 推广主体名称
     * @param name 推广主体负责人姓名
     * @param phone 推广主体负责人电话
     * @param address 推广主体地址
     * @return
     */
    @RequestMapping("/bindingPopularizeById")
    @ResponseBody
    public Map<String, Object> bindingPopularizeById(Long id, Long loginKey, String targetName, String name, String phone, String address){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");
        PopularizeTarget pt = popularizeTargetService.queryPopularizeTargetById(id);
        if(pt != null){
            pt.setWxUserId(loginKey);
            pt.setBindingTime(new Date());
            pt.setTargetName(targetName);
            pt.setName(name);
            pt.setPhone(phone);
            pt.setAddress(address);
            popularizeTargetService.updatePopularizeTarget(pt);
            resultMap.put("status", true);
            resultMap.put("msg", "");
        }
        return resultMap;
    }

}
