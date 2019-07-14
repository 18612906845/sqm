package cn.com.jgyhw.sqm.mapper;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountsGoodsClassifyMapper {

    int deleteByPrimaryKey(String id);

    int insert(DiscountsGoodsClassify record);

    int insertSelective(DiscountsGoodsClassify record);

    DiscountsGoodsClassify selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(DiscountsGoodsClassify record);

    int updateByPrimaryKey(DiscountsGoodsClassify record);

    /**
     * 根据平台标识查询优惠商品类型集合
     *
     * @param platform 平台标识
     * @return
     */
    List<DiscountsGoodsClassify> selectDiscountsGoodsClassifyListByPlatform(@Param("platform") Integer platform);
}