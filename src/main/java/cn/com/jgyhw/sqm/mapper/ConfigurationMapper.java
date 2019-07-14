package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.Configuration;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfigurationMapper {

    int deleteByPrimaryKey(String id);

    int insert(Configuration record);

    int insertSelective(Configuration record);

    Configuration selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Configuration record);

    int updateByPrimaryKey(Configuration record);

    /**
     * 根据分组查询所有参数配置
     *
     * @param paramGroup 参数分组
     * @return
     */
    List<Configuration> selectConfigurationListByParamGroup(@Param("paramGroup") String paramGroup);

    /**
     * 根据参数名称和参数分组查询参数配置
     *
     * @param paramName 参数名称
     * @param paramGroup 参数分组
     * @return 参数配置对象
     */
    Configuration selectConfigurationByParamNameAndParamGroup(@Param("paramName")String paramName, @Param("paramGroup")String paramGroup);

    /**
     * 根据参数名称和参数分组查询参数配置（分页）
     *
     * @param paramName 参数名称
     * @param paramGroup 参数分组
     * @return 参数配置对象分页
     */
    Page<Configuration> selectConfigurationByParamNameAndParamGroupPage(@Param("paramName")String paramName, @Param("paramGroup")String paramGroup);
}