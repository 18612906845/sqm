package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.JdPosition;
import cn.com.jgyhw.sqm.mapper.JdPositionMapper;
import cn.com.jgyhw.sqm.service.IJdPositionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by WangLei on 2019/4/9 0009 20:05
 */
@Service("jdPosition")
public class JdPositionServiceImpl implements IJdPositionService {

    private static Logger LOGGER = LogManager.getLogger(JdPositionServiceImpl.class);

    @Autowired
    private JdPositionMapper jdPositionMapper;

    /**
     * 保存京东推广位
     *
     * @param jdPosition 京东推广位对象
     */
    @Override
    public void saveJdPosition(JdPosition jdPosition) {
        if(jdPosition == null){
            return;
        }
        jdPositionMapper.insert(jdPosition);
    }

    /**
     * 修改京东推广位
     *
     * @param jdPosition 京东推广位对象
     */
    @Override
    public void updateJdPosition(JdPosition jdPosition) {
        if(jdPosition == null){
            return;
        }
        jdPositionMapper.updateByPrimaryKey(jdPosition);
    }

    /**
     * 根据推广位ID查询京东推广位
     *
     * @param positionId 推广位ID
     * @return
     */
    @Override
    public JdPosition queryJdPositionByPositionId(Long positionId) {
        if(positionId == null){
            return null;
        }
        return jdPositionMapper.selectJdPositionByPositionId(positionId);
    }

    /**
     * 查询更新时间最早的京东推广位，并更新推广位与微信用户的关系
     *
     * @param wxUserId
     * @return
     */
    @Override
    public synchronized JdPosition queryJdPositionByOldFashionedAndUpdate(Long wxUserId) {
        LOGGER.info("登陆人：" + wxUserId + "查询京东推广位");
        // 查询是否有对应记录
        JdPosition jp = jdPositionMapper.selectJdPositionByWxUserId(wxUserId);
        if(jp == null){// 没有
            jp = jdPositionMapper.selectJdPositionByOldFashioned();
            if(jp == null){
                return null;
            }
        }
        jp.setWxUserId(wxUserId);
        jp.setUpdateTime(new Date());
        jdPositionMapper.updateByPrimaryKey(jp);
        LOGGER.info("查询京东推广位结果：" + jp.toString());
        return jp;
    }


}
