package cn.com.jgyhw.sqm.service.impl;

import cn.com.jgyhw.sqm.service.IWxPayService;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CommonUtil;
import cn.com.jgyhw.sqm.util.ConfigurationPropertiesSqm;
import com.jd.open.api.sdk.internal.util.CodecUtil;
import com.jd.open.api.sdk.internal.util.StringUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by WangLei on 2019/4/14 0014 17:19
 */
@Service("wxPayService")
public class WxPayServiceImpl extends CommonUtil implements IWxPayService {

    private static Logger LOGGER = LogManager.getLogger(WxPayServiceImpl.class);

    @Autowired
    private ConfigurationPropertiesSqm configurationPropertiesSqm;

    /**
     * 企业支付到微信零钱
     *
     * @param partnerTradeNo 商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
     * @param openid         收款用户标识
     * @param amount         企业付款金额，单位为分
     * @param desc           企业付款备注，必填
     * @return
     */
    @Override
    public Map<String, String> enterprisePayToWxWallet(String partnerTradeNo, String openid, int amount, String desc) {
        LOGGER.info("企业支付到微信零钱Service--> 开始，参数，商户订单号：" + partnerTradeNo + "；收款用户标识：" + openid + "；企业付款金额：" + amount + "；企业付款备注：" + desc);
        CloseableHttpClient httpClient = getSSLHttpclient();
        String url = ApplicationParamConstant.WX_PARAM_MAP.get("wx_pay_by_api_url");

        HttpPost httpPost = new HttpPost(url);
        CloseableHttpResponse response = null;

        String check_name = "NO_CHECK";
        String nonce_str = this.getRandomString();
        String mch_appid = configurationPropertiesSqm.getWX_XCX_APP_ID();

        Map<String, Object> map = new TreeMap<>();
        map.put("mch_appid", mch_appid);
        map.put("mchid", ApplicationParamConstant.WX_PARAM_MAP.get("wx_pay_by_mchid"));
        map.put("nonce_str", nonce_str);
        map.put("partner_trade_no", partnerTradeNo);
        map.put("openid", openid);
        map.put("check_name", check_name);
        map.put("amount", amount);
        map.put("desc", desc);
        map.put("spbill_create_ip", ApplicationParamConstant.WX_PARAM_MAP.get("wx_pay_server_ip"));

        String sign = createWxPaySign(map, configurationPropertiesSqm.getWX_PAY_BY_KEY());
        String requestParamXml = this.mapToXml(map, false, sign);
        LOGGER.info("企业支付到微信零钱Service--> Xml参数：" + requestParamXml);

        Map<String, String> resultMap = new HashMap<>();
        try {
            httpPost.addHeader("Content-Type", "text/xml");
            httpPost.setEntity(new StringEntity(requestParamXml, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = null;
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                entity = response.getEntity();
                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                LOGGER.info("企业支付到微信零钱Service--> 结果：" + respContent);
                resultMap = this.xmlToMap(respContent);
            }
            if(entity != null){
                EntityUtils.consume(entity);
            }
        } catch (IOException e) {
            LOGGER.error("企业支付到微信零钱Service--> 异常", e);
        } finally {
            try {
                if(response != null){
                    response.close();
                }
                if(httpClient != null){
                    httpClient.close();
                }
            } catch (IOException e) {
                LOGGER.error("关闭请求企业支付到微信零钱Service--> 异常", e);
            }
            return resultMap;
        }
    }

    /**
     * 生成微信支付签名
     *
     * @param map </br>
     *          .mch_appid 商户账号appid
     *          .mchid 商户号
     *          .nonce_str 随机字符串
     *          .sign 签名
     *          .partner_trade_no 商户订单号
     *          .openid 微信用户标识
     *          .check_name 校验用户姓名选项
     *          .amount 支付金额
     *          .desc 企业付款备注
     *          .spbill_create_ip Ip地址
     * @param wxPaykey 支付密匙
     * @return
     * @throws Exception
     */
    private String createWxPaySign(Map<String, Object> map, String wxPaykey){
        LOGGER.info("生成微信支付Sign--> 开始，参数：" + map.toString());
        StringBuilder sb = new StringBuilder();
        //按照规则拼成字符串
        for (Map.Entry entry : map.entrySet()) {
            String name = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            //检测参数是否为空
            if (StringUtil.areNotEmpty(new String[]{name, value})) {
                if(sb.length() < 1){
                    sb.append(name).append("=").append(value);
                }else{
                    sb.append("&").append(name).append("=").append(value);
                }
            }
        }
        String sign = null;
        sb.append("&").append("key").append("=").append(wxPaykey);
        try {
            //计算MD5
            LOGGER.error("生成微信支付Sign--> 排序字符串：" + sb.toString());
            sign = CodecUtil.md5(sb.toString());
        }catch (Exception e){
            LOGGER.error("生成微信支付Sign--> 异常", e);
        }finally {
            LOGGER.info("生成微信支付Sign--> 结束，结果：" + sign);
            return sign;
        }
    }

    /**
     * 获取带证书SSLHttpclient
     *
     * @return
     */
    private CloseableHttpClient getSSLHttpclient(){
        LOGGER.info("获取带证书SSLHttpclient--> 开始");
        CloseableHttpClient httpclient = null;

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String certFilePath = request.getSession().getServletContext().getRealPath("/WEB-INF/views/cert/apiclient_cert.p12");
        try {
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            FileInputStream instream = new FileInputStream(new File(certFilePath));
            keyStore.load(instream, ApplicationParamConstant.WX_PARAM_MAP.get("wx_pay_by_mchid").toCharArray());
            SSLContext sslcontext = SSLContexts.custom().loadKeyMaterial(keyStore, ApplicationParamConstant.WX_PARAM_MAP.get("wx_pay_by_mchid").toCharArray()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,new String[] { "TLSv1" },null,SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("获取带证书SSLHttpclient--> 异常", e);
        }finally {
            LOGGER.info("获取带证书SSLHttpclient--> 结束");
            return httpclient;
        }
    }
}
