package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.JdPosition;

/**
 * Created by WangLei on 2019/4/9 0009 20:02
 *
 * 京东推广位接口
 */
public interface IJdPositionService {

    /**
     * 保存京东推广位
     *
     * @param jdPosition 京东推广位对象
     */
    void saveJdPosition(JdPosition jdPosition);

    /**
     * 修改京东推广位
     *
     * @param jdPosition 京东推广位对象
     */
    void updateJdPosition(JdPosition jdPosition);

    /**
     * 根据推广位ID查询京东推广位
     *
     * @param positionId 推广位ID
     * @return
     */
    JdPosition queryJdPositionByPositionId(Long positionId);

    /**
     * 查询更新时间最早的京东推广位，并更新推广位与微信用户的关系
     *
     * @return
     */
    JdPosition queryJdPositionByOldFashionedAndUpdate(Long wxUserId);
}
