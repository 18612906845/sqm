package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.TbOrderBindingRecord;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TbOrderBindingRecordMapper {

    int deleteByPrimaryKey(String id);

    int insert(TbOrderBindingRecord record);

    int insertSelective(TbOrderBindingRecord record);

    TbOrderBindingRecord selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(TbOrderBindingRecord record);

    int updateByPrimaryKey(TbOrderBindingRecord record);

    /**
     * 根据淘宝订单编号查询淘宝订单绑定记录
     *
     * @param tbOrderId 淘宝订单编号
     * @return
     */
    TbOrderBindingRecord selectTbOrderBindingRecordByTbOrderId(@Param("tbOrderId") String tbOrderId);

    /**
     * 根据用户标识和状态查询淘宝订单绑定记录集合（分页）
     *
     * @param wxUserId 用户标识
     * @param status 淘宝订单绑定记录状态
     * @return
     */
    Page<TbOrderBindingRecord> selectTbOrderBindingRecordListByWxUserIdAndStatusPage(@Param("wxUserId") Long wxUserId, @Param("status") Integer status);
}