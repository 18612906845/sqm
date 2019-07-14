package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.WxToken;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WxTokenMapper {

    int deleteByPrimaryKey(String id);

    int insert(WxToken record);

    int insertSelective(WxToken record);

    WxToken selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(WxToken record);

    int updateByPrimaryKey(WxToken record);

    /**
     * 根据微信应用Id和令牌类型查询微信令牌
     *
     * @param appId 微信应用id
     * @param tokenType 令牌类型
     * @return
     */
    WxToken selectWxTokenByAppIdAndTokenType(@Param("appId") String appId, @Param("tokenType") int tokenType);

    /**
     * 查询所有微信令牌
     *
     * @return
     */
    List<WxToken> selectWxTokenAll();
}