package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.Configuration;
import cn.com.jgyhw.sqm.mapper.ConfigurationMapper;
import cn.com.jgyhw.sqm.service.IConfigurationService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by WangLei on 2019/1/26 0026 17:41
 */
@Service("configurationService")
public class ConfigurationServiceImpl implements IConfigurationService {

    private static Logger LOGGER = LogManager.getLogger(ConfigurationServiceImpl.class);

    @Autowired
    private ConfigurationMapper configurationMapper;

    /**
     * 根据参数名称和参数分组查询参数配置
     *
     * @param paramName  参数名称
     * @param paramGroup 参数分组
     * @return 参数配置对象
     */
    @Override
    public Configuration queryConfigurationByParamNameAndParamGroup(String paramName, String paramGroup) {
        LOGGER.info("查询参数配置，入参paramName：" + paramName + "；paramGroup：" + paramGroup);
        Configuration cf = this.configurationMapper.selectConfigurationByParamNameAndParamGroup(paramName, paramGroup);
        if(cf == null){
            LOGGER.info("未查询到参数配置");
        }else{
            LOGGER.info("查询参数配置：" + cf.toString());
        }
        return cf;
    }

    /**
     * 根据标识查询参数配置
     *
     * @param id 标识
     * @return
     */
    @Override
    public Configuration queryConfigurationById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        return this.configurationMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改参数配置
     *
     * @param configuration 参数配置对象
     */
    @Override
    public void modifyConfiguration(Configuration configuration) {
        if(configuration == null){
            return;
        }
        this.configurationMapper.updateByPrimaryKey(configuration);
        LOGGER.info("修改参数配置：" + configuration.toString());
    }

    /**
     * 保存参数配置
     *
     * @param configuration 参数配置对象
     */
    @Override
    public void saveConfiguration(Configuration configuration) {
        if(configuration == null){
            return;
        }
        this.configurationMapper.insert(configuration);
        LOGGER.info("保存参数配置：" + configuration.toString());
    }

    /**
     * 根据标识删除参数配置
     *
     * @param id 标识
     */
    @Override
    public void deleteConfigurationById(String id) {
        if(StringUtils.isBlank(id)){
            return;
        }
        this.configurationMapper.deleteByPrimaryKey(id);
        LOGGER.info("删除参数配置：" + id);
    }

    /**
     * 根据条件查询参数配置（分页）
     *
     * @param paramName  参数名称
     * @param paramGroup 参数分组
     * @param pageNo     页号
     * @param pageSize   每页显示记录数
     * @return 配置参数对象分页信息
     */
    @Override
    public Page<Configuration> queryConfigurationByConditionPage(String paramName, String paramGroup, int pageNo, int pageSize) {
        LOGGER.info("查询参数配置（分页），入参paramName：" + paramName + "；paramGroup：" + paramGroup + "；pageNo：" + pageNo + "；pageSize：" + pageSize);
        PageHelper.startPage(pageNo, pageSize);
        return this.configurationMapper.selectConfigurationByParamNameAndParamGroupPage(paramName, paramGroup);
    }
}
