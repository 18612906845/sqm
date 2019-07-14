package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.WxGzhMenu;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxGzhMenuMapper {

    int deleteByPrimaryKey(String id);

    int insert(WxGzhMenu record);

    int insertSelective(WxGzhMenu record);

    WxGzhMenu selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WxGzhMenu record);

    int updateByPrimaryKey(WxGzhMenu record);

    /**
     * 查询一级菜单集合
     *
     * @return
     */
    List<WxGzhMenu> selectFirstLevelWxGzhMenuList();

    /**
     * 根据父级ID查询二级菜单集合
     *
     * @param parentId 父菜单ID
     * @return
     */
    List<WxGzhMenu> selectSecondLevelWxGzhMenuListByParentId(String parentId);
}