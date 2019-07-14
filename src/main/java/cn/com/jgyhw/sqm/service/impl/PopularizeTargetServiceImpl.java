package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import cn.com.jgyhw.sqm.mapper.PopularizeTargetMapper;
import cn.com.jgyhw.sqm.service.IPopularizeTargetService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by WangLei on 2019/5/11 0011 20:45
 */
@Service("popularizeTargetService")
public class PopularizeTargetServiceImpl implements IPopularizeTargetService {

    private static Logger LOGGER = LogManager.getLogger(PopularizeTargetServiceImpl.class);

    @Autowired
    private PopularizeTargetMapper popularizeTargetMapper;

    /**
     * 保存新下推广主体
     *
     * @param popularizeTarget 线下推广主体对象
     */
    @Override
    public void savePopularizeTarget(PopularizeTarget popularizeTarget) {
        if(popularizeTarget == null){
            return;
        }
        popularizeTargetMapper.insert(popularizeTarget);
    }

    /**
     * 修改新下推广主体
     *
     * @param popularizeTarget 线下推广主体对象
     */
    @Override
    public void updatePopularizeTarget(PopularizeTarget popularizeTarget) {
        if(popularizeTarget == null){
            return;
        }
        popularizeTargetMapper.updateByPrimaryKey(popularizeTarget);
    }

    /**
     * 根据线下推广主体标识查询
     *
     * @param id 线下推广主体标识
     * @return
     */
    @Override
    public PopularizeTarget queryPopularizeTargetById(Long id) {
        if(id == 0){
            return null;
        }
        return popularizeTargetMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据微信用户标识查询线下推广主体
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    @Override
    public PopularizeTarget queryPopularizeTargetByWxUserId(Long wxUserId) {
        if(wxUserId == 0){
            return null;
        }
        return popularizeTargetMapper.selectPopularizeTargetByWxUserId(wxUserId);
    }

    /**
     * 根据绑定用户标识和关键词查询推广主体列表（分页）
     *
     * @param wxUserId 绑定微信用户标识
     * @param keyword  关键词
     * @param pageNo   页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @Override
    public Page<PopularizeTarget> queryPopularizeTargetByWxUserIdAndKeywordPage(Long wxUserId, String keyword, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return popularizeTargetMapper.selectPopularizeTargetByWxUserIdAndKeywordPage(wxUserId, keyword);
    }
}
