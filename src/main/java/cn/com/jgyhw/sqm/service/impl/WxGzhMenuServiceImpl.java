package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.WxGzhMenu;
import cn.com.jgyhw.sqm.mapper.WxGzhMenuMapper;
import cn.com.jgyhw.sqm.service.IWxGzhMenuService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxGzhMenuServiceImpl implements IWxGzhMenuService {

    private static Logger LOGGER = LogManager.getLogger(WxGzhMenuServiceImpl.class);

    @Autowired
    private WxGzhMenuMapper wxGzhMenuMapper;

    /**
     * 新增微信公众号菜单
     *
     * @param wxGzhMenu 微信公众号菜单对象
     * @return
     */
    @Override
    public void saveWxGzhMenu(WxGzhMenu wxGzhMenu) {
        if(wxGzhMenu == null){
            return;
        }
        wxGzhMenuMapper.insert(wxGzhMenu);
    }

    /**
     * 根据ID删除微信公众号菜单
     *
     * @param id 微信公众号菜单ID
     * @return
     */
    @Override
    public void deleteWxGzhMenuById(String id) {
        LOGGER.info("根据ID删除微信公众号菜单参数，id：" + id);
        WxGzhMenu wgm = this.queryWxGzhMenuById(id);
        wxGzhMenuMapper.deleteByPrimaryKey(id);
        //删除后重新排序
        if(wgm.getLevelNum() == 1){//一级菜单
            List<WxGzhMenu> wgmList = this.queryFirstLevelWxGzhMenuList();
            this.anewOrderByDiyMenuList(wgmList);
        }else{//二级往后子菜单
            List<WxGzhMenu> wgmList = this.querySecondLevelWxGzhMenuListByParentId(wgm.getParentId());
            this.anewOrderByDiyMenuList(wgmList);
        }
    }

    /**
     * 修改微信公众号菜单
     *
     * @param wxGzhMenu 微信公众号菜单对象
     * @return
     */
    @Override
    public void updateWxGzhMenu(WxGzhMenu wxGzhMenu) {
        if(wxGzhMenu == null){
            return ;
        }
        wxGzhMenuMapper.updateByPrimaryKey(wxGzhMenu);
    }

    /**
     * 根据ID查询微信公众号菜单
     *
     * @param id
     * @return
     */
    @Override
    public WxGzhMenu queryWxGzhMenuById(String id) {
        if(id == null){
            return null;
        }
        return wxGzhMenuMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询一级菜单集合
     *
     * @return
     */
    @Override
    public List<WxGzhMenu> queryFirstLevelWxGzhMenuList() {
        return wxGzhMenuMapper.selectFirstLevelWxGzhMenuList();
    }

    /**
     * 根据父级ID查询二级菜单集合
     *
     * @param parentId 父菜单ID
     * @return
     */
    @Override
    public List<WxGzhMenu> querySecondLevelWxGzhMenuListByParentId(String parentId) {
        return wxGzhMenuMapper.selectSecondLevelWxGzhMenuListByParentId(parentId);
    }

    private void anewOrderByDiyMenuList(List<WxGzhMenu> diyMenuPojoList){
        if(diyMenuPojoList == null || diyMenuPojoList.isEmpty()){
            return ;
        }else{
            int index = 1;
            for(WxGzhMenu subWgm : diyMenuPojoList){
                subWgm.setOrderNum(index);
                this.updateWxGzhMenu(subWgm);
                index ++;
            }
        }
    }
}
