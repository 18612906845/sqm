package cn.com.jgyhw.sqm.thread;

import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;
import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;
import cn.com.jgyhw.sqm.domain.TbGoodsDiscounts;
import cn.com.jgyhw.sqm.pojo.*;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.PatternsUtil;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.taobao.api.request.TbkTpwdCreateRequest;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WangLei on 2019/6/2 0002 10:31
 *
 * 微信公众号文字消息处理线程
 */
public class WxGzhTextMessageDisposeThread implements Runnable {

    private static Logger LOGGER = LogManager.getLogger(WxGzhTextMessageDisposeThread.class);

    /**
     * 收到的文字消息内容
     */
    private String content;

    /**
     * 接收者微信公众号OpenId
     */
    private String receiveGzhOpenId;

    /**
     * 当前登陆人标识
     */
    private Long loginKey;

    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    private IJdSearchService jdSearchService;

    private IWxGzhMessageSendService wxGzhMessageSendService;

    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    private IPddSearchService pddSearchService;

    private ITbSearchService tbSearchService;

    /**
     * 线程执行方法
     */
    @Override
    public void run() {
        try {
            content = URLDecoder.decode(content, "utf-8");
            LOGGER.info("根据剪贴板内容跳转对应页面--> 转码参数：" + content);

            String noDiscountsInfo = "很抱歉，未找到该商品优惠信息";
            String urlErrorInfo = "商品链接不正确，无法解析";

            // 验证消息内容是否包含连接
            if (content.indexOf("https://") != -1) { // 包含网址
                // 判断网址平台类型
                if (content.indexOf("jd.com") != -1) {// 京东网址
                    // 解析商品编号
                    Pattern pattern = Pattern.compile(ApplicationParamConstant.JD_PARAM_MAP.get("regexp_extract_url_jd_goods_id"));// 匹配商品ID的模式
                    Matcher m = pattern.matcher(content);
                    String goodsId = "";
                    while (m.find()) {
                        goodsId += m.group(1);
                    }
                    if (!StringUtils.isBlank(goodsId)) {
                        // 根据ID查询对应的推广连接
                        JdGoodsDiscounts jgd = jdGoodsDiscountsService.queryJdGoodsDiscountsByGoodsId(Long.valueOf(goodsId));
                        if (jgd == null) {
                            JdGoodsDiscountsQueryParamPojo jgdqpp = new JdGoodsDiscountsQueryParamPojo();
                            jgdqpp.setSkuIds(goodsId);
                            JdGoodsDiscountsQueryResultPojo jgdqrp = jdSearchService.queryJdGoodsDiscounts(jgdqpp);
                            if (jgdqrp != null && jgdqrp.getJdGoodsDiscountsList() != null && jgdqrp.getJdGoodsDiscountsList().size() > 0) {
                                jgd = jgdqrp.getJdGoodsDiscountsList().get(0);
                            }
                        }

                        if(jgd == null){
                            // 发送无优惠文本消息
                            wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                        }else{
                            // 查询推广链接
                            PromotionCodeReqPojo promotionCodeReqPojo = new PromotionCodeReqPojo();
                            promotionCodeReqPojo.setLoginKey(loginKey);
                            if (!StringUtils.isBlank(jgd.getLink())) {
                                promotionCodeReqPojo.setCouponUrl(URLEncoder.encode(jgd.getLink(), "utf-8"));
                            }
                            promotionCodeReqPojo.setMaterialId(URLEncoder.encode(jgd.getMaterialUrl(), "utf-8"));
                            String cpsUrl = jdSearchService.queryJdCpsUrl(promotionCodeReqPojo);
                            if (StringUtils.isBlank(cpsUrl)) {
                                // 判断是否有优惠券连接，若有，去掉优惠券连接重试
                                if (!StringUtils.isBlank(promotionCodeReqPojo.getCouponUrl())) {
                                    promotionCodeReqPojo.setCouponUrl(null);
                                    cpsUrl = jdSearchService.queryJdCpsUrl(promotionCodeReqPojo);
                                }
                            }
                            if (StringUtils.isBlank(cpsUrl)) {
                                // 发送无优惠文本消息
                                wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                            } else {
                                // 发送优惠图文消息
                                List<ArticlePojo> aps = new ArrayList<>();
                                ArticlePojo ap = new ArticlePojo();
                                ap.setTitle("约返现:" + jgd.getReturnMoney() + "元" + ";编号:" + jgd.getGoodsId());
                                ap.setDescription(jgd.getGoodsName());
                                ap.setPicUrl(jgd.getGoodsImgUrl());
                                ap.setUrl(cpsUrl);
                                aps.add(ap);
                                wxGzhMessageSendService.sendNewsMessage(receiveGzhOpenId, aps);
                            }
                        }
                    }else{
                        wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, urlErrorInfo);
                    }
                } else if (content.indexOf("yangkeduo.com") != -1) { // 拼多多
                    // 解析商品编号
                    Pattern pattern = Pattern.compile(ApplicationParamConstant.PDD_PARAM_MAP.get("regexp_extract_url_pdd_goods_id"));// 匹配商品ID的模式
                    Matcher m = pattern.matcher(content);
                    String goodsId = "";
                    while (m.find()) {
                        goodsId += m.group(1);
                    }
                    if (!StringUtils.isBlank(goodsId)) {
                        PddGoodsDiscounts pgd = pddGoodsDiscountsService.queryPddGoodsDiscountsByGoodsId(Long.valueOf(goodsId));
                        if(pgd == null){
                            PddDdkGoodsSearchRequestPojo pdgsrp = new PddDdkGoodsSearchRequestPojo();
                            List<Long> goodsIdList = new ArrayList<>();
                            goodsIdList.add(Long.valueOf(goodsId));
                            pdgsrp.setGoodsIdList(goodsIdList);
                            PddGoodsDiscountsQueryResultPojo pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, true);
                            if(pgdqrp != null && pgdqrp.getPddGoodsDiscountsList() != null && pgdqrp.getPddGoodsDiscountsList().size() > 0){
                                pgd = pgdqrp.getPddGoodsDiscountsList().get(0);
                            }
                        }

                        if(pgd == null){
                            // 发送无优惠文本消息
                            wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                        }else{
                            // 查询推广链接
                            PddDdkGoodsPromotionUrlGenerateRequestPojo req = new PddDdkGoodsPromotionUrlGenerateRequestPojo();
                            req.setCustomParameters(loginKey.toString());
                            req.setGoodsId(Long.valueOf(goodsId));
                            PddDdkGoodsPromotionUrlGenerateResponse pdgpugr = pddSearchService.queryPddCpsUrl(req);

                            String cpsUrl = null;
                            if(pdgpugr == null || pdgpugr.getErrorResponse() != null){
                                // 发送无优惠文本消息
                                wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                            }else{
                                List<PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItem> itemList = pdgpugr.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList();
                                if(itemList != null && !itemList.isEmpty()){
                                    cpsUrl = itemList.get(0).getWeAppWebViewUrl();
                                }
                            }
                            if(StringUtils.isBlank(cpsUrl)){
                                // 发送无优惠文本消息
                                wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                            }else{
                                // 发送优惠图文消息
                                List<ArticlePojo> aps = new ArrayList<>();
                                ArticlePojo ap = new ArticlePojo();
                                ap.setTitle("约返现:" + pgd.getReturnMoney() + "元" + ";编号:" + pgd.getGoodsId());
                                ap.setDescription(pgd.getGoodsName());
                                ap.setPicUrl(pgd.getGoodsImgUrl());
                                ap.setUrl(cpsUrl);
                                aps.add(ap);
                                wxGzhMessageSendService.sendNewsMessage(receiveGzhOpenId, aps);
                            }
                        }
                    }else{
                        wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, urlErrorInfo);
                    }
                } else if (content.indexOf("tb.cn") != -1) { // 淘宝
                    // 解析出链接
                    String tbUrl = null;
                    Matcher matcherTb = PatternsUtil.WEB_URL.matcher(content);
                    if (matcherTb.find()) {
                        tbUrl = matcherTb.group();
                    }
                    String goodsId = "";
                    if (StringUtils.isBlank(tbUrl)) { // 未解析出地址
                        wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, urlErrorInfo);
                    } else {
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet(tbUrl);
                        CloseableHttpResponse response = null;
                        try {
                            response = httpClient.execute(httpGet);
                            HttpEntity entity = null;
                            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                                entity = response.getEntity();
                                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));

                                // 通过返回结果解析商品编号
                                Pattern pattern = Pattern.compile(ApplicationParamConstant.TB_PARAM_MAP.get("regexp_extract_url_tb_goods_id"));// 匹配商品ID的模式
                                Matcher m = pattern.matcher(respContent);

                                while (m.find()) {
                                    goodsId += m.group(1);
                                }
                                if (!StringUtils.isBlank(goodsId)) {
                                    TbGoodsDiscounts tgd = tbSearchService.queryTbGoodsDiscountsByGoodsId(Long.valueOf(goodsId));
                                    if(tgd == null){
                                        // 发送无优惠文本消息
                                        wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                                    }else{
                                        TbkTpwdCreateRequest ttcr = new TbkTpwdCreateRequest();
                                        ttcr.setText(tgd.getGoodsName());
                                        String url = tgd.getCouponShareUrl();
                                        if(StringUtils.isBlank(url)){
                                            url = tgd.getClickUrl();
                                        }
                                        ttcr.setUrl(url);
                                        ttcr.setLogo(tgd.getGoodsImgUrl());
                                        String goodsCommand = tbSearchService.queryTbGoodsCommand(ttcr);

                                        if(StringUtils.isBlank(goodsCommand)){
                                            // 发送无优惠文本消息
                                            wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, noDiscountsInfo);
                                        }else{
                                            // 发送优惠图文消息
                                            List<ArticlePojo> aps = new ArrayList<>();
                                            ArticlePojo ap = new ArticlePojo();
                                            ap.setTitle("约返现:" + tgd.getReturnMoney() + "元" + ";复制口令到淘宝APP下单");
                                            ap.setDescription(tgd.getGoodsName());
                                            ap.setPicUrl(tgd.getGoodsImgUrl());
                                            aps.add(ap);
                                            wxGzhMessageSendService.sendNewsMessage(receiveGzhOpenId, aps);
                                            // 发送口令
                                            wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, goodsCommand);
                                        }
                                    }
                                }
                            }
                            if (entity != null) {
                                EntityUtils.consume(entity);
                            }
                        } catch (IOException e) {
                            LOGGER.error("请求淘宝推广地址异常--> ");
                            e.printStackTrace();
                        } finally {
                            try {
                                if (response != null) {
                                    response.close();
                                }
                                if (httpClient != null) {
                                    httpClient.close();
                                }
                            } catch (IOException e) {
                                LOGGER.error("关闭淘宝推广地址请求异常--> ");
                                e.printStackTrace();
                            }
                        }
                    }
                } else { // 未识别，什么都不是
                    LOGGER.info("消息内容无法识别链接");
                    wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, urlErrorInfo);
                }
            } else { // 其他不包含连接信息
                LOGGER.info("消息内容无链接");
                wxGzhMessageSendService.sendTextMessage(receiveGzhOpenId, urlErrorInfo);
            }

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("根据剪贴板内容跳转对应页面--> URLDecoder出错", e);
        }
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReceiveGzhOpenId() {
        return receiveGzhOpenId;
    }

    public void setReceiveGzhOpenId(String receiveGzhOpenId) {
        this.receiveGzhOpenId = receiveGzhOpenId;
    }

    public Long getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(Long loginKey) {
        this.loginKey = loginKey;
    }

    public IJdGoodsDiscountsService getJdGoodsDiscountsService() {
        return jdGoodsDiscountsService;
    }

    public void setJdGoodsDiscountsService(IJdGoodsDiscountsService jdGoodsDiscountsService) {
        this.jdGoodsDiscountsService = jdGoodsDiscountsService;
    }

    public IJdSearchService getJdSearchService() {
        return jdSearchService;
    }

    public void setJdSearchService(IJdSearchService jdSearchService) {
        this.jdSearchService = jdSearchService;
    }

    public IWxGzhMessageSendService getWxGzhMessageSendService() {
        return wxGzhMessageSendService;
    }

    public void setWxGzhMessageSendService(IWxGzhMessageSendService wxGzhMessageSendService) {
        this.wxGzhMessageSendService = wxGzhMessageSendService;
    }

    public IPddGoodsDiscountsService getPddGoodsDiscountsService() {
        return pddGoodsDiscountsService;
    }

    public void setPddGoodsDiscountsService(IPddGoodsDiscountsService pddGoodsDiscountsService) {
        this.pddGoodsDiscountsService = pddGoodsDiscountsService;
    }

    public IPddSearchService getPddSearchService() {
        return pddSearchService;
    }

    public void setPddSearchService(IPddSearchService pddSearchService) {
        this.pddSearchService = pddSearchService;
    }

    public ITbSearchService getTbSearchService() {
        return tbSearchService;
    }

    public void setTbSearchService(ITbSearchService tbSearchService) {
        this.tbSearchService = tbSearchService;
    }
}
