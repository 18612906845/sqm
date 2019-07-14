package cn.com.jgyhw.sqm.service;


import cn.com.jgyhw.sqm.domain.WxGzhMenu;

import java.util.List;

/**
 * 微信公众号菜单管理Service
 */
public interface IWxGzhMenuService {

    /**
     * 新增微信公众号菜单
     *
     * @param wxGzhMenu 微信公众号菜单对象
     * @return
     */
    void saveWxGzhMenu(WxGzhMenu wxGzhMenu);

    /**
     * 根据ID删除微信公众号菜单
     *
     * @param id 微信公众号菜单ID
     * @return
     */
    void deleteWxGzhMenuById(String id);

    /**
     * 修改微信公众号菜单
     *
     * @param wxGzhMenu 微信公众号菜单对象
     * @return
     */
    void updateWxGzhMenu(WxGzhMenu wxGzhMenu);

    /**
     * 根据ID查询微信公众号菜单
     *
     * @param id
     * @return
     */
    WxGzhMenu queryWxGzhMenuById(String id);

    /**
     * 查询一级菜单集合
     *
     * @return
     */
    List<WxGzhMenu> queryFirstLevelWxGzhMenuList();

    /**
     * 根据父级ID查询二级菜单集合
     *
     * @param parentId 父菜单ID
     * @return
     */
    List<WxGzhMenu> querySecondLevelWxGzhMenuListByParentId(String parentId);
}
