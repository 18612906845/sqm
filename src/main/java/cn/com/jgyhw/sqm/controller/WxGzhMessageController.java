package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.WxUser;
import cn.com.jgyhw.sqm.pojo.TextMessagePojo;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.thread.WxGzhTextMessageDisposeThread;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.CheckoutUtil;
import cn.com.jgyhw.sqm.util.CommonUtil;
import cn.com.jgyhw.sqm.util.WxGzhMessageUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

/**
 * 微信消息Controller
 */
@Controller
@RequestMapping("/wxGzhMessage")
public class WxGzhMessageController extends CommonUtil {

    private static Logger LOGGER = LogManager.getLogger(WxGzhMessageController.class);

    @Autowired
    private CheckoutUtil checkoutUtil;

    @Autowired
    private IWxUserService wxUserService;

    @Autowired
    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    @Autowired
    private IJdSearchService jdSearchService;

    @Autowired
    private IWxGzhMessageSendService wxGzhMessageSendService;

    @Autowired
    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    @Autowired
    private IPddSearchService pddSearchService;

    @Autowired
    private ITbSearchService tbSearchService;

    /**
     * 接收微信公众号消息
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/receiveMessage")
    public void receiveWxMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LOGGER.info("接收微信公众号消息开始--> 开始时间毫秒：" + System.currentTimeMillis());
        boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print;

        if (isGet) {
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            LOGGER.info("微信公众号Get请求-->signature：" + signature + "；timestamp：" + timestamp + "；nonce：" + "；echostr：" + echostr);
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && this.checkoutUtil.checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.info("微信消息验证IO异常：", e);
                }
            }
        } else {
            LOGGER.info("微信公众号Post请求");
            print = response.getWriter();
            // 接收消息并返回消息，调用核心服务类接收处理请求
            String respXml = new String(this.processRequest(request).getBytes("UTF-8"), "ISO-8859-1");
            print.print(respXml);
            print.flush();
            print.close();
        }
        LOGGER.info("接收微信公众号消息结束--> 结束时间毫秒：" + System.currentTimeMillis());
    }

    /**
     * 处理消息分发
     *
     * @param request 请求参数
     * @return
     */
    private String processRequest(HttpServletRequest request) {
        // xml格式的消息数据
        String respXml = null;
        try {
            // 调用parseXml方法解析请求消息
            Map requestMap = WxGzhMessageUtil.parseXml(request);
            // 发送方帐号
            String fromUserName = (String) requestMap.get("FromUserName");
            // 开发者微信号
            String toUserName = (String) requestMap.get("ToUserName");
            // 消息类型
            String msgType = (String) requestMap.get("MsgType");

            LOGGER.info("FromUserName：" + fromUserName + "；ToUserName：" + toUserName + "；MsgType：" + msgType);

            // 文本消息
            TextMessagePojo textMessagePojo = new TextMessagePojo();
            textMessagePojo.setToUserName(fromUserName);
            textMessagePojo.setFromUserName(toUserName);
            textMessagePojo.setCreateTime(new Date().getTime());
            textMessagePojo.setMsgType(WxGzhMessageUtil.RESP_MESSAGE_TYPE_TEXT);

            if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_TEXT)) {// 文字消息
                String content = (String) requestMap.get("Content");
                content.trim();
                LOGGER.info("开始处理文字消息--> 文字内容：" + content);
                // 判断用户是否登陆
                WxUser wu = wxUserService.queryWxUserByOpenIdGzh(fromUserName);
                if(wu == null || StringUtils.isBlank(wu.getUnionId())){// 用户不存在或者没有开放平台唯一标识
                    // 发送登陆文本消息
                    String messageText = "您好，请先点击公众号菜单【消息订阅】登陆后，重新发送商品链接获取优惠券/返现信息";
                    textMessagePojo.setContent(messageText);
                    // 将文本消息对象转换成xml
                    respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                    return respXml;
                }
                // 判断消息是否是含有京东、淘宝、拼多多网址的信息
                if(content.indexOf("jd.com") == -1 && content.indexOf("tb.cn") == -1 && content.indexOf("yangkeduo.com") == -1){
                    String messageText = ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_auto_reply_text_message");
                    textMessagePojo.setContent(messageText);
                    // 将文本消息对象转换成xml
                    respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                    return respXml;
                }else{
                    WxGzhTextMessageDisposeThread wgtmdt = new WxGzhTextMessageDisposeThread();
                    wgtmdt.setContent(content);
                    wgtmdt.setReceiveGzhOpenId(fromUserName);
                    wgtmdt.setLoginKey(wu.getId());
                    wgtmdt.setJdGoodsDiscountsService(jdGoodsDiscountsService);
                    wgtmdt.setJdSearchService(jdSearchService);
                    wgtmdt.setWxGzhMessageSendService(wxGzhMessageSendService);
                    wgtmdt.setPddGoodsDiscountsService(pddGoodsDiscountsService);
                    wgtmdt.setPddSearchService(pddSearchService);
                    wgtmdt.setTbSearchService(tbSearchService);
                    new Thread(wgtmdt, "微信公众号商品链接处理线程" + new Date().getTime()).start();
                    return "success";
                }
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_IMAGE)) {// 图片消息
                textMessagePojo.setContent("敢发张自拍照让我看看嘛/::P");
                // 将文本消息对象转换成xml
                respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                return respXml;
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_VOICE)) {// 语音消息
                textMessagePojo.setContent("哎呀，普通话还不如我呢[Smart]");
                // 将文本消息对象转换成xml
                respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                return respXml;
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_VIDEO)) {// 视频消息
                textMessagePojo.setContent("好厉害的样子，可是我看不懂/::O");
                // 将文本消息对象转换成xml
                respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                return respXml;
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_LOCATION)) {// 地理位置消息
                textMessagePojo.setContent("你就不怕我过去打劫嘛\uE14C");
                // 将文本消息对象转换成xml
                respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                return respXml;
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_LINK)) {// 链接消息
                textMessagePojo.setContent("我不喝鸡汤，谢谢/:share");
                // 将文本消息对象转换成xml
                respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                return respXml;
            }else if (msgType.equals(WxGzhMessageUtil.REQ_MESSAGE_TYPE_EVENT)) {// 事件推送
                // 事件类型
                String eventType = (String) requestMap.get("Event");
                if (eventType.equals(WxGzhMessageUtil.EVENT_TYPE_SUBSCRIBE)) {// 关注
                    String[] contentArray = ApplicationParamConstant.WX_PARAM_MAP.get("attention_gzh_greeting").split("；");
                    String contentStr = "";
                    for(int i=0; i<contentArray.length; i++){
                        contentStr += contentArray[i] + "\n";
                        if(i < contentArray.length - 1 && !StringUtils.isBlank(contentStr)){
                            contentStr += "\n";
                        }
                    }
                    textMessagePojo.setContent(contentStr);
                    // 将文本消息对象转换成xml
                    respXml = WxGzhMessageUtil.textMessagePojoToXml(textMessagePojo);
                    return respXml;
                } else if (eventType.equals(WxGzhMessageUtil.EVENT_TYPE_UNSUBSCRIBE)) {// 取消关注

                } else if (eventType.equals(WxGzhMessageUtil.EVENT_TYPE_SCAN)) {// 扫描带参数二维码
                    String eventKey = (String) requestMap.get("EventKey");
                    LOGGER.info("eventKey：" + eventKey);
                } else if (eventType.equals(WxGzhMessageUtil.EVENT_TYPE_LOCATION)) {// 上报地理位置

                } else if (eventType.equals(WxGzhMessageUtil.EVENT_TYPE_CLICK)) {// 自定义菜单
                    String eventKey = (String) requestMap.get("EventKey");
                }
            }
            return "success";
        } catch (Exception e) {
            LOGGER.error(e);
            return "success";
        }
    }
}

