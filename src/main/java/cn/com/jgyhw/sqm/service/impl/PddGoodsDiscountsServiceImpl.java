package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;
import cn.com.jgyhw.sqm.mapper.PddGoodsDiscountsMapper;
import cn.com.jgyhw.sqm.service.IPddGoodsDiscountsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by WangLei on 2019/5/2 0002 12:03
 */
@Service("pddGoodsDiscountsService")
public class PddGoodsDiscountsServiceImpl implements IPddGoodsDiscountsService {

    @Autowired
    private PddGoodsDiscountsMapper pddGoodsDiscountsMapper;

    /**
     * 保存拼多多商品优惠
     *
     * @param pddGoodsDiscounts 拼多多商品优惠对象
     */
    @Override
    public void savePddGoodsDiscounts(PddGoodsDiscounts pddGoodsDiscounts) {
        if(pddGoodsDiscounts == null){
            return;
        }
        pddGoodsDiscountsMapper.insert(pddGoodsDiscounts);
    }

    /**
     * 根据商品id查询拼多多商品优惠
     *
     * @param goodsId 商品id
     * @return
     */
    @Override
    public PddGoodsDiscounts queryPddGoodsDiscountsByGoodsId(Long goodsId) {
        if(goodsId == null){
            return null;
        }
        return pddGoodsDiscountsMapper.selectPddGoodsDiscountsByGoodsId(goodsId);
    }

    /**
     * 更新拼多多商品优惠
     *
     * @param pddGoodsDiscounts 拼多多商品优惠对象
     */
    @Override
    public void updatePddGoodsDiscounts(PddGoodsDiscounts pddGoodsDiscounts) {
        if(pddGoodsDiscounts == null){
            return;
        }
        pddGoodsDiscountsMapper.updateByPrimaryKey(pddGoodsDiscounts);
    }

    /**
     * 根据条件分页查询拼多多商品优惠（分页）
     *
     * @param goodsClassify 商品类型
     * @param pageNo        页号
     * @param pageSize      每页显示记录数
     * @return
     */
    @Override
    public Page<PddGoodsDiscounts> queryPddGoodsDiscountsByConditionPage(String goodsClassify, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return pddGoodsDiscountsMapper.selectPddGoodsDiscountsByGoodsClassifyPage(goodsClassify);
    }

    /**
     * 根据商品类型和更新时间删除拼多多商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime    更新时间
     */
    @Override
    public void deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(String goodsClassify, String updateTime) {
        pddGoodsDiscountsMapper.deletePddGoodsDiscountsByGoodsClassifyAndUpdateTime(goodsClassify, updateTime);
    }
}
