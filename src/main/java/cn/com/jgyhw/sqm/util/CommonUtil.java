package cn.com.jgyhw.sqm.util;

import cn.com.jgyhw.sqm.pojo.TemplateMessagePojo;
import cn.com.jgyhw.sqm.service.IWxGzhMessageSendService;
import com.jd.open.api.sdk.internal.util.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 公共工具方法
 */
public class CommonUtil {

    private static Logger LOGGER = LogManager.getLogger(CommonUtil.class);

    protected static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化成字符串
     *
     * @param date 日期
     * @param format 格式
     * @return
     */
    protected String dateToStringByFormat(Date date, String format){
        String dateStr = "";
        if(date == null){
            return dateStr;
        }
        if(StringUtils.isBlank(format)){
            format = YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        dateStr = sdf.format(date);
        return dateStr;
    }
    /**
     * 日期字符串格式化成日期对象
     *
     * @param dateStr 日期
     * @param format 格式
     * @return
     */
    protected Date stringToDateByFormat(String dateStr, String format){
        if(StringUtils.isBlank(dateStr)){
            return new Date();
        }
        if(StringUtils.isBlank(format)){
            format = YYYY_MM_DD_HH_MM_SS;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date date = sdf.parse(dateStr);
            return date;
        } catch (ParseException e) {
            LOGGER.error("日期字符串格式化为日子对象异常", e);
            return new Date();
        }
    }

    /**
     * 对象转String
     *
     * @param obj 对象
     * @return
     */
    protected String objectToString(Object obj){
        if(obj == null){
            return null;
        }
        return obj.toString();
    }

    /**
     * 对象转int
     *
     * @param obj 对象
     * @return
     */
    protected int objectToInt(Object obj){
        if(obj == null){
            return 0;
        }
        String objStr = obj.toString();
        Integer objInteger = Integer.valueOf(objStr);
        if(objInteger == null){
            return 0;
        }
        return objInteger;
    }

    /**
     * 计算返利
     *
     * @param unitPrice 商品单价
     * @param commisionRatio 佣金比例
     * @param rebateScale 返利比例
     * @return
     */
    protected Double rebateCompute(Double unitPrice, Double commisionRatio, int rebateScale){
        if(unitPrice == null || commisionRatio == null){
            return 0.0;
        }
        Double rebateDouble = (commisionRatio / 100.00) * unitPrice * (rebateScale / 100.00);
        //保留2位小数
        DecimalFormat df = new DecimalFormat("#.00");
        Double rebate = Double.valueOf(df.format(rebateDouble));
        return rebate;
    }

    /**
     * 计算返利
     *
     * @param commision 佣金
     * @param rebateScale 返利比例
     * @return
     */
    protected Double rebateCompute(Double commision, int rebateScale){
        if(commision == null){
            return 0.0;
        }
        Double rebateDouble = commision * (rebateScale / 100.00);
        //保留2位小数
        DecimalFormat df = new DecimalFormat("#.00");
        Double rebate = Double.valueOf(df.format(rebateDouble));
        return rebate;
    }

    /**
     * 格式化双精度数据
     *
     * @param source 元数据
     * @return
     */
    protected Double formatDouble(Double source){
        if(source == null){
            source = 0.00;
        }
        //保留2位小数
        DecimalFormat df = new DecimalFormat("#.00");
        return Double.valueOf(df.format(source));
    }

    /**
     * 分转元
     *
     * @param fen 分
     * @return
     */
    protected Double fenToYuan(Long fen){
        if(fen == null){
            return 0.0;
        }
        Double yuan = Double.valueOf(fen) / 100;
        return this.formatDouble(yuan);
    }

    /**
     * 千分比转百分比
     *
     * @param qianFenBi 千分比
     * @return
     */
    protected Double qianFenBiToBaiFenBi(Long qianFenBi){
        if(qianFenBi == null){
            return 0.0;
        }
        Double baiFeiBi = Double.valueOf(qianFenBi) / 10;
        return this.formatDouble(baiFeiBi);
    }

    /**
     * 获取随机数字
     *
     * @param min 最小值
     * @param max 最大值
     * @return
     */
    protected int getRandomNumber(int min, int max) {
        Random rand = new Random();
        int randomNumber = min + (rand.nextInt((max - min)) + 1);
        return randomNumber;
    }

    /**
     * 获取随机字符串
     * @return
     */
    protected String getRandomString() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        String randomStr = machineId + String.format("%05d", hashCodeV);
        String timeStr = String.valueOf(new Date().getTime());
        timeStr = timeStr.substring(3, timeStr.length());
        randomStr += timeStr;
        return randomStr;
    }


    /**
     * 获取商户订单号
     * @return
     */
    protected String getShopOrderString() {
        int machineId = 1;//最大支持1-9个集群机器部署
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        if(hashCodeV < 0) {//有可能是负数
            hashCodeV = - hashCodeV;
        }
        String randomStr = "JGYHWDH" + machineId + String.format("%05d", hashCodeV);
        String timeStr = String.valueOf(new Date().getTime());
        timeStr = timeStr.substring(3, timeStr.length());
        randomStr += timeStr;
        return randomStr;
    }

    /**
     * map转Xml字符串
     *
     * @param map map元数据
     * @return
     */
    protected String mapToXml(Map<String, Object> map, boolean isCDATA, String sign){
        StringBuilder sb = new StringBuilder();
        if(map == null){
            return null;
        }
        sb.append("<xml>");
        for (Map.Entry entry : map.entrySet()) {
            String key = String.valueOf(entry.getKey());
            String value = String.valueOf(entry.getValue());
            //检测参数是否为空
            if (StringUtil.areNotEmpty(new String[]{key, value})) {
                if(isCDATA){
                    sb.append("<").append(key).append("><![CDATA[").append(value).append("]]></").append(key).append(">");
                }else{
                    sb.append("<").append(key).append(">").append(value).append("</").append(key).append(">");
                }
            }
        }
        if(isCDATA){
            sb.append("<sign><![CDATA[" + sign + "]]></sign>");
        }else{
            sb.append("<sign>" + sign + "</sign>");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * xml字符串转Map
     *
     * @param xmlStr xml字符串
     * @return
     */
    protected Map<String, String> xmlToMap(String xmlStr){
        LOGGER.info("微信支付结果XmlToMap--> 开始，参数XmlStr：" + xmlStr);
        if(StringUtils.isBlank(xmlStr)){
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            Document document = DocumentHelper.parseText(xmlStr);
            Element resultElem = document.getRootElement();
            Iterator it = resultElem.elementIterator();
            while (it.hasNext()) {
                Element element = (Element) it.next();
                map.put(element.getName(), element.getStringValue());
            }
        } catch (Exception e) {
            LOGGER.error("微信支付结果XmlToMap--> 异常", e);
        } finally {
            LOGGER.info("微信支付结果XmlToMap--> 结束，结果：" + map.toString());
            return map;
        }
    }

    /**
     * 正则验证字符串是否全是数字
     *
     * @param str 被验证字符串
     * @return
     */
    protected boolean stringIsAllNumber(String str){
        String reg="^\\d+$";
        return str.matches(reg);
    }


    /**
     * 发送确认订单微信消息
     *
     * @param wxGzhMessageSendService 微信消息发送接口
     * @param openIdGzh 用户公众号唯一标识
     * @param orderId 订单编号
     * @param orderStatus 订单状态
     */
    protected void sendAffirmOrderWxMessage(IWxGzhMessageSendService wxGzhMessageSendService, String openIdGzh, String orderId, String orderStatus){
        //发送确认订单模版消息
        TemplateMessagePojo tmp = new TemplateMessagePojo();
        tmp.setTouser(openIdGzh);
        tmp.setTemplate_id(ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_template_message_id_ddqr"));
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> firstMap = new HashMap<>();
        firstMap.put("value", "您好，已确认新订单");
        dataMap.put("first", firstMap);

        Map<String, Object> keyword1Map = new HashMap<>();
        keyword1Map.put("value", orderId);
        dataMap.put("keyword1", keyword1Map);

        Map<String, Object> keyword2Map = new HashMap<>();
        keyword2Map.put("value", orderStatus);
        keyword2Map.put("color", "#008000");
        dataMap.put("keyword2", keyword2Map);

        Map<String, Object> keyword3Map = new HashMap<>();
        keyword3Map.put("value", this.dateToStringByFormat(new Date(), "yyyy-MM-dd HH:mm:ss"));
        dataMap.put("keyword3", keyword3Map);

        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("value", "请持续关注后续完成订单通知");
        dataMap.put("remark", remarkMap);
        tmp.setData(dataMap);
        wxGzhMessageSendService.sendTemplateMessage(tmp);
    }

    /**
     * 发送完成订单微信消息
     *
     * @param wxGzhMessageSendService 微信消息发送接口
     * @param openIdGzh 用户公众号唯一标识
     * @param orderId 订单编号
     * @param orderFinishTime 订单完成时间
     */
    protected void sendFinishOrderWxMessage(IWxGzhMessageSendService wxGzhMessageSendService, String openIdGzh, String orderId, Date orderFinishTime){
        //发送完成订单模版消息
        TemplateMessagePojo tmp = new TemplateMessagePojo();
        tmp.setTouser(openIdGzh);
        tmp.setTemplate_id(ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_template_message_id_ddwc"));
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> firstMap = new HashMap<>();
        firstMap.put("value", "您好，订单已完成");
        dataMap.put("first", firstMap);

        Map<String, Object> keyword1Map = new HashMap<>();
        keyword1Map.put("value", orderId);
        dataMap.put("keyword1", keyword1Map);

        Map<String, Object> keyword2Map = new HashMap<>();
        keyword2Map.put("value", this.dateToStringByFormat(orderFinishTime, "yyyy-MM-dd HH:mm:ss"));
        dataMap.put("keyword2", keyword2Map);

        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("value", "请持续关注后续解冻返现入账通知");
        dataMap.put("remark", remarkMap);
        tmp.setData(dataMap);
        wxGzhMessageSendService.sendTemplateMessage(tmp);
    }

    /**
     * 发送获得返现微信消息
     *
     * @param wxGzhMessageSendService 微信消息发送接口
     * @param openIdGzh 用户公众号唯一标识
     * @param returnMoneyCause 返现原因
     * @param returnMoney 返现金额
     */
    protected void sendRebateWxMessage(IWxGzhMessageSendService wxGzhMessageSendService, String openIdGzh, String returnMoneyCause, double returnMoney){
        //发送获得返现模版消息
        TemplateMessagePojo tmp = new TemplateMessagePojo();
        tmp.setTouser(openIdGzh);
        tmp.setTemplate_id(ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_template_message_id_hdfx"));
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> firstMap = new HashMap<>();
        firstMap.put("value", "您好，新返现已入账");
        dataMap.put("first", firstMap);

        Map<String, Object> keyword1Map = new HashMap<>();
        keyword1Map.put("value", returnMoneyCause);
        dataMap.put("keyword1", keyword1Map);

        Map<String, Object> keyword2Map = new HashMap<>();
        keyword2Map.put("value", this.formatDouble(returnMoney) + "元");
        keyword2Map.put("color", "#008000");
        dataMap.put("keyword2", keyword2Map);

        Map<String, Object> keyword3Map = new HashMap<>();
        keyword3Map.put("value", "入账到个人中心，可取现金额");
        dataMap.put("keyword3", keyword3Map);

        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("value", "进入个人中心->自助提现申请，提现到微信零钱，欢迎再次光顾");
        dataMap.put("remark", remarkMap);
        tmp.setData(dataMap);
        wxGzhMessageSendService.sendTemplateMessage(tmp);
    }


    /**
     * 发送支付成功消息
     *
     * @param wxGzhMessageSendService 微信消息发送接口
     * @param openIdGzh 用户公众号唯一标识
     * @param payMoney 支付金额
     * @param payTime 支付时间
     */
    protected void sendPaySuccessWxMessage(IWxGzhMessageSendService wxGzhMessageSendService, String openIdGzh, Double payMoney, Date payTime){
        //发送提现成功模版消息
        TemplateMessagePojo tmp = new TemplateMessagePojo();
        tmp.setTouser(openIdGzh);
        tmp.setTemplate_id(ApplicationParamConstant.WX_PARAM_MAP.get("wx_gzh_template_message_id_txcg"));
        Map<String, Object> dataMap = new HashMap<>();
        Map<String, Object> firstMap = new HashMap<>();
        firstMap.put("value", "恭喜您，提现成功");
        dataMap.put("first", firstMap);

        Map<String, Object> keyword1Map = new HashMap<>();
        keyword1Map.put("value", payMoney + "元");
        keyword1Map.put("color", "#e4393c");
        dataMap.put("keyword1", keyword1Map);

        Map<String, Object> keyword2Map = new HashMap<>();
        keyword2Map.put("value", this.dateToStringByFormat(payTime, "yyyy-MM-dd HH:mm:ss"));
        dataMap.put("keyword2", keyword2Map);

        Map<String, Object> remarkMap = new HashMap<>();
        remarkMap.put("value", "提现金额已到账微信钱包，请及时查收");
        dataMap.put("remark", remarkMap);
        tmp.setData(dataMap);
        wxGzhMessageSendService.sendTemplateMessage(tmp);
    }

}
