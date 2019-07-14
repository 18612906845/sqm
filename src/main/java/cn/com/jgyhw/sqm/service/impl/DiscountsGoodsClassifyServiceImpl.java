package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import cn.com.jgyhw.sqm.mapper.DiscountsGoodsClassifyMapper;
import cn.com.jgyhw.sqm.service.IDiscountsGoodsClassifyService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by WangLei on 2019/5/20 0020 19:51
 */
@Service("discountsGoodsClassifyService")
public class DiscountsGoodsClassifyServiceImpl implements IDiscountsGoodsClassifyService {

    private static Logger LOGGER = LogManager.getLogger(DiscountsGoodsClassifyServiceImpl.class);

    @Autowired
    private DiscountsGoodsClassifyMapper discountsGoodsClassifyMapper;

    /**
     * 根据平台标识查询优惠商品查询类型集合
     *
     * @param platform 平台标识
     * @return
     */
    @Override
    public List<DiscountsGoodsClassify> queryDiscountsGoodsClassifyListByPlatform(Integer platform) {
        return discountsGoodsClassifyMapper.selectDiscountsGoodsClassifyListByPlatform(platform);
    }
}
