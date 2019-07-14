package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.Configuration;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/1/26 0026 17:35
 *
 * 参数配置Service
 */
public interface IConfigurationService {

    /**
     * 根据参数名称和参数分组查询参数配置
     *
     * @param paramName 参数名称
     * @param paramGroup 参数分组
     * @return 参数配置对象
     */
    Configuration queryConfigurationByParamNameAndParamGroup(String paramName, String paramGroup);

    /**
     * 根据标识查询参数配置
     *
     * @param id 标识
     * @return
     */
    Configuration queryConfigurationById(String id);

    /**
     * 修改参数配置
     *
     * @param configuration 参数配置对象
     */
    void modifyConfiguration(Configuration configuration);

    /**
     * 保存参数配置
     *
     * @param configuration 参数配置对象
     */
    void saveConfiguration(Configuration configuration);

    /**
     * 根据标识删除参数配置
     *
     * @param id 标识
     */
    void deleteConfigurationById(String id);

    /**
     * 根据条件查询参数配置（分页）
     *
     * @param paramName 参数名称
     * @param paramGroup 参数分组
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return 配置参数对象分页信息
     */
    Page<Configuration> queryConfigurationByConditionPage(String paramName, String paramGroup, int pageNo, int pageSize);

}
