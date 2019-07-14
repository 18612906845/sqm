package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.Configuration;
import cn.com.jgyhw.sqm.pojo.ConfigurationPojo;
import cn.com.jgyhw.sqm.service.IConfigurationService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import cn.com.jgyhw.sqm.util.UpdateApplicationParamUtil;
import com.github.pagehelper.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by WangLei on 2019/2/18 0018 21:02
 *
 * 系统设置-参数配置
 */
@RequestMapping("/xpsXtglAuth")
@Controller
public class XtglParameterSettingController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(XtglParameterSettingController.class);

    @Autowired
    private IConfigurationService configurationService;

    @Autowired
    private UpdateApplicationParamUtil updateApplicationParamUtil;

    /**
     * 打开参数配置页面
     * @return
     */
    @RequestMapping("/openParameterSettingListPage")
    public String openParameterSettingListPage(){
        return "xtgl/parameterSettingList";
    }

    /**
     * 新建参数配置页面
     * @return
     */
    @RequestMapping("/openParameterSettingNewPage")
    public String openParameterSettingNewPage(){
        return "xtgl/parameterSettingNew";
    }

    /**
     * 根据条件查询参数配置（分页）
     *
     * @param paramName 参数名
     * @param paramGroup 参数分组
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findConfigurationByConditionPage")
    @ResponseBody
    public Map<String, Object> findConfigurationByConditionPage(String paramName, String paramGroup, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("code", 5000);
        resultMap.put("msg", "服务未知错误");
        Page<Configuration> configurationPage = this.configurationService.queryConfigurationByConditionPage(paramName, paramGroup, pageNo, pageSize);

        List<Configuration> cList = configurationPage.getResult();
        List<ConfigurationPojo> cpList = new ArrayList<>();

        if(cList != null && !cList.isEmpty()){
            for(Configuration c : cList){
                ConfigurationPojo cp = this.configurationToConfigurationPojo(c);
                if(cp == null){
                    continue;
                }
                cpList.add(cp);
            }
        }
        resultMap.put("data", cpList);
        resultMap.put("code", 0);
        resultMap.put("msg", "");
        resultMap.put("count", configurationPage.getTotal());
        return resultMap;
    }

    /**
     * 根据标识修改参数配置
     *
     * @param id 标识
     * @param field 修改字段
     * @param value 修改值
     * @return
     */
    @RequestMapping("/updateConfigurationById")
    @ResponseBody
    public Map<String, Object> updateConfigurationById(String id, String field, String value){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        Configuration config = this.configurationService.queryConfigurationById(id);
        if(config == null){
            return resultMap;
        }
        // 判断是否修改参数名称
        if("paramName".equals(field)){
            Configuration configFlag = this.configurationService.queryConfigurationByParamNameAndParamGroup(value, config.getParamGroup());
            if(configFlag != null){
                resultMap.put("msg", "同分组下参数名称重复");
                return resultMap;
            }
        }
        config.setUpdateTime(new Date());
        try {
            Field f = config.getClass().getDeclaredField(field);
            f.setAccessible(true);
            f.set(config, value);
            this.configurationService.modifyConfiguration(config);
            // 修改运行内存中变量
            this.updateApplicationParamUtil.addOrUpdateParamMap(config);
            resultMap.put("status", true);
        } catch (Exception e) {
            LOGGER.error("Java反射动态赋值异常", e);
        }
        return resultMap;
    }

    /**
     * 根据标识删除参数配置
     *
     * @param id 标识
     * @return
     */
    @RequestMapping("/deleteConfigurationById")
    @ResponseBody
    public Map<String, Object> deleteConfigurationById(String id){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        // 根据ID查询配置参数
        Configuration config = this.configurationService.queryConfigurationById(id);
        this.configurationService.deleteConfigurationById(id);
        if(config != null){
            this.updateApplicationParamUtil.deleteParamMap(config);
        }
        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 新建参数配置
     *
     * @param configuration 参数配置对象
     * @return
     */
    @RequestMapping("/newConfiguration")
    @ResponseBody
    public Map<String, Object> newConfiguration(@RequestBody Configuration configuration){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        if(configuration == null){
            return resultMap;
        }
        // 判断同分组是否有相同参数存在
        Configuration configFlage = this.configurationService.queryConfigurationByParamNameAndParamGroup(configuration.getParamName(), configuration.getParamGroup());
        if(configFlage != null){
            resultMap.put("msg", "同分组下参数名称重复");
            return resultMap;
        }
        configuration.setId(UUID.randomUUID().toString());
        configuration.setUpdateTime(new Date());
        this.configurationService.saveConfiguration(configuration);
        // 添加运行内存中变量
        this.updateApplicationParamUtil.addOrUpdateParamMap(configuration);
        resultMap.put("status", true);
        return resultMap;
    }

    @RequestMapping("/findParamMap")
    @ResponseBody
    public Map<String, Object> findParamMap(){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("XT", ApplicationParamConstant.XT_PARAM_MAP);
        resultMap.put("JD", ApplicationParamConstant.JD_PARAM_MAP);
        resultMap.put("WX", ApplicationParamConstant.WX_PARAM_MAP);
        return resultMap;
    }
}
