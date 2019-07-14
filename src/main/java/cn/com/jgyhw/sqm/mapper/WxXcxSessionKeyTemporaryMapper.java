package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.WxXcxSessionKeyTemporary;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WxXcxSessionKeyTemporaryMapper {

    int deleteByPrimaryKey(String id);

    int insert(WxXcxSessionKeyTemporary record);

    int insertSelective(WxXcxSessionKeyTemporary record);

    WxXcxSessionKeyTemporary selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WxXcxSessionKeyTemporary record);

    int updateByPrimaryKey(WxXcxSessionKeyTemporary record);

    /**
     * 根据小程序唯一标识查询会话密匙
     *
     * @param openId 小程序唯一标识
     * @return
     */
    WxXcxSessionKeyTemporary selectWxXcxSessionKeyTemporaryByOpenId(@Param("openId") String openId);
}