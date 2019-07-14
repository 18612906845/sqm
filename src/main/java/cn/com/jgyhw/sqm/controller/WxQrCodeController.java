package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.service.IWxQrCodeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by WangLei on 2019/5/7 0007 13:45
 *
 * 微信二维码Controller
 */
@RequestMapping("/wxQrCode")
@Controller
public class WxQrCodeController {

    private static Logger LOGGER = LogManager.getLogger(WxQrCodeController.class);

    @Autowired
    private IWxQrCodeService wxQrCodeService;

    /**
     * 查询商品分享微信参数二维码
     *
     * @param loginKey 登陆标识
     * @param goodsId 商品编号
     * @param pagePath 小程序页面路径
     * @param isHyaline 是否透明背景
     * @param width 小程序尺寸
     * @return
     */
    @RequestMapping("/findWxXcxInfinitudeQrCodeByGoodsShare")
    @ResponseBody
    public Map<String, Object> findWxXcxInfinitudeQrCodeByGoodsShare(Long loginKey, Long goodsId, String pagePath, boolean isHyaline, int width){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        String scene = "";
        if(goodsId != null && goodsId != 0){
            scene += "g=" + goodsId;
        }

        if(loginKey != null && loginKey != 0){
            if(StringUtils.isBlank(scene)){
                scene += "u=" + loginKey;
            }else{
                scene += "&u=" + loginKey;
            }
        }

        String qrCodeBase64 = wxQrCodeService.createWxXcxInfinitudeQrCodeByShare(scene, pagePath, isHyaline, width);
        if(StringUtils.isBlank(qrCodeBase64)){
            resultMap.put("msg", "未获取到二维码数据");
            return resultMap;
        }
        resultMap.put("qrCodeBase64", qrCodeBase64);
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }
}
