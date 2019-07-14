package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.JdPosition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JdPositionMapper {

    int deleteByPrimaryKey(String id);

    int insert(JdPosition record);

    int insertSelective(JdPosition record);

    JdPosition selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(JdPosition record);

    int updateByPrimaryKey(JdPosition record);

    /**
     * 根据推广位ID查询京东推广位
     *
     * @param positionId 推广位ID
     * @return
     */
    JdPosition selectJdPositionByPositionId(@Param("positionId") Long positionId);

    /**
     * 根据微信用户标识查询京东推广位
     *
     * @param wxUserId 微信用户标识
     * @return
     */
    JdPosition selectJdPositionByWxUserId(@Param("wxUserId") Long wxUserId);

    /**
     * 查询更新时间最早的京东推广位
     *
     * @return
     */
    JdPosition selectJdPositionByOldFashioned();
}