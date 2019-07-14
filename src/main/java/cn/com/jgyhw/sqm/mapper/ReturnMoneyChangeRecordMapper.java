package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.ReturnMoneyChangeRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReturnMoneyChangeRecordMapper {

    int deleteByPrimaryKey(String id);

    int insert(ReturnMoneyChangeRecord record);

    int insertSelective(ReturnMoneyChangeRecord record);

    ReturnMoneyChangeRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(ReturnMoneyChangeRecord record);

    int updateByPrimaryKey(ReturnMoneyChangeRecord record);

    /**
     * 根据微信用户标识、变更类型、主体id、订单编号查询返现变更记录
     *
     * @param wxUserId 微信用户标识
     * @param changeType 变更类型
     * @param targetId 主体id
     * @param targetId 主体id
     * @param orderId 订单编号
     * @return
     */
    ReturnMoneyChangeRecord selectReturnMoneyChangeRecordByWxUserIdAndChangeTypeAndTargetIdAndOrderId(@Param("wxUserId") Long wxUserId, @Param("changeType") int changeType, @Param("targetId") String targetId, @Param("orderId") String orderId);

    /**
     * 根据微信用户标识和变更类型查询返现变更记录集合
     *
     * @param wxUserId 微信用户标识
     * @param changeType 变更类型
     * @return
     */
    Page<ReturnMoneyChangeRecord> selectReturnMoneyChangeRecordListByWxUserIdAndChangeTypePage(@Param("wxUserId") Long wxUserId, @Param("changeType") int changeType);
}