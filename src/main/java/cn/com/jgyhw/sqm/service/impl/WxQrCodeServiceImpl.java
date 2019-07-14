package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;
import cn.com.jgyhw.sqm.service.IPopularizeTargetService;
import cn.com.jgyhw.sqm.service.IWxQrCodeService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

/**
 * Created by WangLei on 2019/5/7 0007 13:22
 */
@Service("wxQrCodeService")
public class WxQrCodeServiceImpl implements IWxQrCodeService {

    private static Logger LOGGER = LogManager.getLogger(WxPayServiceImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    @Autowired
    private IPopularizeTargetService popularizeTargetService;

    /**
     * 查询微信小程序无限量参数二维码（官方方案B）
     *
     * @param scene     最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     * @param page      必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param isHyaline 是否需要透明底色，为 true 时，生成透明底色的小程序
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px，默认430
     * @return
     */
    @Override
    public String createWxXcxInfinitudeQrCodeByShare(String scene, String page, boolean isHyaline, int width) {

        String qrCodeBase64Str = this.requestWxApiGetWxXcxInfinitudeQrCode(scene, page, isHyaline, width, null);

        return qrCodeBase64Str;
    }

    /**
     * 创建线下推广主体微信小程序无限量带参数二维码（官方方案B）
     *
     * @param popularizeTarget 线下推广主体
     * @return
     */
    @Transactional
    @Override
    public String createWxXcxInfinitudeQrCodeByPopularize(PopularizeTarget popularizeTarget) {
        String qrCodeUrl = ApplicationParamConstant.XT_PARAM_MAP.get("popularize_qr_code_url_prefix");
        // 保存线下主体信息
        popularizeTargetService.savePopularizeTarget(popularizeTarget);

        Long ptId = popularizeTarget.getId();
        String fileName = ptId + ".jpg";

        String scene = "t=" + ptId;

        String localPath = this.requestWxApiGetWxXcxInfinitudeQrCode(scene, "pages/popularize/welcome", false, 530, fileName);

        if(StringUtils.isBlank(localPath)){
            // 回滚事物，不保存推广主体信息
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return null;
        }

        LOGGER.info("创建线下推广主体微信小程序无限量带参数二维码，图片服务器保存物理路径：" + localPath);

        qrCodeUrl += "/" + fileName;

        return qrCodeUrl;
    }


    /**
     * 请求微信Api获取微信小程序无限量带参数二维码（官方方案B）
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     * @param page 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param isHyaline 是否需要透明底色，为 true 时，生成透明底色的小程序
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px，默认430
     * @param fileName 二维码的文件名，带文件后缀，若是不存储二维码到物理磁盘不需要传值
     * @return
     */
    private String requestWxApiGetWxXcxInfinitudeQrCode(String scene, String page, boolean isHyaline, int width, String fileName){

        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_get_infinitude_qrcode_request_url");
        if(!StringUtils.isBlank(url)){
            url = url.replaceAll("ACCESS_TOKEN", configurationPropertiesSqm.getWX_XCX_SERVER_API_TOKEN());
        }

        LOGGER.debug("请求微信Api获取微信小程序无限量带参数二维码（官方方案B），参数：URL--> " + url);

        HttpPost httpPost = new HttpPost(url);

        JSONObject reqJsonObj = new JSONObject();
        reqJsonObj.put("scene", scene);
        reqJsonObj.put("page", page);
        if(width == 0){
            width = 480;
        }
        reqJsonObj.put("width", width);
        reqJsonObj.put("is_hyaline", isHyaline);

        CloseableHttpResponse response = null;

        InputStream is = null;

        String resultStr = null;

        try {
            httpPost.addHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(reqJsonObj.toJSONString(), "UTF-8"));
            response = httpClient.execute(httpPost);

            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){

                String contentTypeValue = response.getEntity().getContentType().getValue();

                LOGGER.debug("contentTypeValue：" + contentTypeValue);

                if(contentTypeValue.indexOf("application/json;") != -1){
                    return  null;
                }

                is = response.getEntity().getContent();

                if(StringUtils.isBlank(fileName)){// 获取图片Base64编码字符串
                    // 将获取流转为base64格式
                    byte[] data = null;
                    ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                    byte[] buff = new byte[100];
                    int rc = 0;
                    while ((rc = is.read(buff, 0, 100)) > 0) {
                        swapStream.write(buff, 0, rc);
                    }
                    data = swapStream.toByteArray();

                    resultStr = new String(Base64.getEncoder().encode(data));
                }else{// 保存服务器磁盘
                    // 加载模版图片
                    String templateLocalPath = configurationPropertiesSqm.getPOPULARIZE_QR_CODE_PATH() + File.separator + "popularizeQrCode" + File.separator + "template-hd.jpg";
                    BufferedImage templateImage = ImageIO.read(new File(templateLocalPath));
                    // 加载推广二维码
                    BufferedImage popularizeQrCode = ImageIO.read(is);
                    // 以模版图片为画布
                    Graphics2D g = templateImage.createGraphics();
                    // 在模板上添加用户二维码(地址,左边距,上边距,图片宽度,图片高度,未知)
                    g.drawImage(popularizeQrCode, 325, 632, 530, 530, null);
                    // 完成模板修改
                    g.dispose();
                    // 获取新文件的地址
                    File outputfile = new File(configurationPropertiesSqm.getPOPULARIZE_QR_CODE_PATH() + File.separator + "popularizeQrCode" + File.separator + fileName);
                    // 生成新的合成过的用户二维码并写入新图片
                    String fileSuffix = "jpg";
                    String[] fileNameArray = fileName.split(".");
                    if(fileNameArray.length >= 2){
                        fileSuffix = fileNameArray[1];
                    }
                    ImageIO.write(templateImage, fileSuffix, outputfile);

                    resultStr = outputfile.getPath();
                }
            }
        } catch (IOException e) {
            LOGGER.error("请求微信Api获取微信小程序无限量带参数二维码（官方方案B）异常--> ", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
                if(is != null){
                    is.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求微信Api获取微信小程序无限量带参数二维码（官方方案B）异常--> ", e);
            }
        }
        return resultStr;
    }
}
