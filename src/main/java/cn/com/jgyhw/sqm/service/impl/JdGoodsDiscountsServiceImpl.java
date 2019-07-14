package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;
import cn.com.jgyhw.sqm.mapper.JdGoodsDiscountsMapper;
import cn.com.jgyhw.sqm.service.IJdGoodsDiscountsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by WangLei on 2019/4/8 0008 16:09
 */
@Service("jdGoodsDiscountsService")
public class JdGoodsDiscountsServiceImpl implements IJdGoodsDiscountsService {

    @Autowired
    private JdGoodsDiscountsMapper jdGoodsDiscountsMapper;

    /**
     * 保存京东商品优惠
     *
     * @param jdGoodsDiscounts 京东商品优惠对象
     */
    @Override
    public void saveJdGoodsDiscounts(JdGoodsDiscounts jdGoodsDiscounts) {
        if(jdGoodsDiscounts == null){
            return;
        }
        jdGoodsDiscountsMapper.insert(jdGoodsDiscounts);
    }

    /**
     * 根据标识查询京东商品优惠
     *
     * @param id 标识
     * @return
     */
    @Override
    public JdGoodsDiscounts queryJdGoodsDiscountsById(String id) {
        if(StringUtils.isBlank(id)){
            return null;
        }
        return jdGoodsDiscountsMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据商品编号查询京东商品优惠
     *
     * @param goodsId 商品编号
     * @return
     */
    @Override
    public JdGoodsDiscounts queryJdGoodsDiscountsByGoodsId(Long goodsId) {
        if(goodsId == null){
            return null;
        }
        return jdGoodsDiscountsMapper.selectJdGoodsDiscountsByGoodsId(goodsId);
    }

    /**
     * 更新京东商品优惠
     *
     * @param jdGoodsDiscounts 京东商品优惠对象
     */
    @Override
    public void updateJdGoodsDiscounts(JdGoodsDiscounts jdGoodsDiscounts) {
        if(jdGoodsDiscounts == null){
            return;
        }
        jdGoodsDiscountsMapper.updateByPrimaryKey(jdGoodsDiscounts);
    }

    /**
     * 根据条件分页查询京东商品优惠
     *
     * @param goodsClassify 商品类型
     * @param pageNo        页号
     * @param pageSize      每页显示记录数
     * @return
     */
    @Override
    public Page<JdGoodsDiscounts> queryJdGoodsDiscountsByConditionPage(String goodsClassify, int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return jdGoodsDiscountsMapper.selectJdGoodsDiscountsByGoodsClassifyPage(goodsClassify);
    }

    /**
     * 根据查询类型和更新时间删除京东商品优惠
     *
     * @param goodsClassify 商品类型
     * @param updateTime    更新时间
     */
    @Override
    public void deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(String goodsClassify, String updateTime) {
        jdGoodsDiscountsMapper.deleteJdGoodsDiscountsByGoodsClassifyAndUpdateTime(goodsClassify, updateTime);
    }

}
