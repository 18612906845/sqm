package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PopularizeTargetMapper {

    int deleteByPrimaryKey(Long id);

    int insert(PopularizeTarget record);

    int insertSelective(PopularizeTarget record);

    PopularizeTarget selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PopularizeTarget record);

    int updateByPrimaryKey(PopularizeTarget record);

    /**
     * 根据微信用户标识查询线下推广主体
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    PopularizeTarget selectPopularizeTargetByWxUserId(@Param("wxUserId") Long wxUserId);

    /**
     * 根据绑定用户标识和关键词查询推广主体列表（分页）
     *
     * @param wxUserId 绑定微信用户标识
     * @param keyword 关键词
     * @return
     */
    Page<PopularizeTarget> selectPopularizeTargetByWxUserIdAndKeywordPage(@Param("wxUserId") Long wxUserId, @Param("keyword") String keyword);
}