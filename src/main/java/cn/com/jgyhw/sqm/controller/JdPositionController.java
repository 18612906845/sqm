package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.JdPosition;
import cn.com.jgyhw.sqm.service.IJdPositionService;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Created by WangLei on 2019/4/9 0009 20:24
 *
 * 京东推广位Controller
 */
@RequestMapping("/jdPosition")
@Controller
@Deprecated
public class JdPositionController {

    private static Logger LOGGER = LogManager.getLogger(JdPositionController.class);

    @Autowired
    private IJdPositionService jdPositionService;

    /**
     * 创建京东推广位
     * @return
     */
    @RequestMapping("/createPosition")
    @ResponseBody
    public String createPosition(){
        long index = 9301;
        for(int i=0; i<140; i++){
            String positionNameStr = "";
            for(int j=0; j<50; j++){
                String positionName = String.valueOf(index);
                if(j > 0){
                    positionNameStr += "," + positionName;
                }else{
                    positionNameStr += positionName;
                }
                index ++;
            }
            System.out.println(positionNameStr);CloseableHttpClient httpClient = HttpClients.createDefault();

            String url = "https://jd.open.apith.cn/unionv2/createPosition?unionId=1000455975&key=e4209eac5db9f8e2991aff46d30286846b51ef0faaf4aa75946d6460c00973671ae62d79f09d7b0a&unionType=1&type=1&siteId=1537716146&spaceNameList=" + positionNameStr;

            HttpGet httpGet = new HttpGet(url);

            CloseableHttpResponse response = null;
            Map<String, Object> map = null;
            try {
                response = httpClient.execute(httpGet);
                HttpEntity entity = null;
                if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    entity = response.getEntity();
                    String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));
                    LOGGER.info("创建京东推广位结果--> " + respContent);
                    JSONObject jsonObject = JSONObject.parseObject(respContent);
                    String code = jsonObject.getString("code");
                    if("1".equals(code)){
                        JSONObject dataJsonObj = jsonObject.getJSONObject("data");
                        JSONObject resultListObj = dataJsonObj.getJSONObject("resultList");
                        map = resultListObj.getInnerMap();
                        if(map != null){
                            for(String key : map.keySet()){
                                JdPosition jp = new JdPosition();
                                jp.setId(UUID.randomUUID().toString());
                                jp.setPositionId(Long.valueOf(map.get(key).toString()));
                                jp.setPositionName(key);
                                jp.setCreateTime(new Date());
                                jp.setUpdateTime(new Date());
                                jdPositionService.saveJdPosition(jp);
                            }
                        }
                    }
                }
                if(entity != null){
                    EntityUtils.consume(entity);
                }
            } catch (IOException e) {
                LOGGER.error("创建京东推广位异常--> ", e);
                return "error";
            } finally {
                try {
                    if(response != null){
                        response.close();
                    }
                    if(httpClient != null){
                        httpClient.close();
                    }
                } catch (IOException e) {
                    LOGGER.error("关闭创建京东推广位异常--> ", e);
                }
            }

//            try {
//                Thread.sleep(1000 * 10);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
        return "success";
    }

    /**
     * 查询更新时间最早的京东推广位（测试多线程是否安全）
     * @return
     */
    @RequestMapping("/findOldPosition")
    @ResponseBody
    public String findOldPosition(){
        //创建启动3个线程
        for(int i=0; i<10; i++){

            Thread t1 = new Thread(){
                @Override
                public void run() {
                    JdPosition jp = jdPositionService.queryJdPositionByOldFashionedAndUpdate(1L);

                    System.out.println("线程" + Thread.currentThread().getName() + "的查询结果：" + jp.toString());
                }
            };
            //线程启动
            t1.start();
            //确保创建的线程的优先级一样
            t1.setPriority(Thread.NORM_PRIORITY);
        }
        return "success";
    }
}
