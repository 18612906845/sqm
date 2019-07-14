package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import com.github.pagehelper.Page;

/**
 * Created by WangLei on 2019/5/11 0011 20:40
 *
 * 线下推广主体接口
 */
public interface IPopularizeTargetService {

    /**
     * 保存新下推广主体
     *
     * @param popularizeTarget 线下推广主体对象
     */
    void savePopularizeTarget(PopularizeTarget popularizeTarget);

    /**
     * 修改新下推广主体
     *
     * @param popularizeTarget 线下推广主体对象
     */
    void updatePopularizeTarget(PopularizeTarget popularizeTarget);

    /**
     * 根据线下推广主体标识查询
     *
     * @param id 线下推广主体标识
     * @return
     */
    PopularizeTarget queryPopularizeTargetById(Long id);

    /**
     * 根据微信用户标识查询线下推广主体
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    PopularizeTarget queryPopularizeTargetByWxUserId(Long wxUserId);

    /**
     * 根据绑定用户标识和关键词查询推广主体列表（分页）
     *
     * @param wxUserId 绑定微信用户标识
     * @param keyword 关键词
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<PopularizeTarget> queryPopularizeTargetByWxUserIdAndKeywordPage(Long wxUserId, String keyword, int pageNo, int pageSize);

}
