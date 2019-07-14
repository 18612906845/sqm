package cn.com.jgyhw.sqm.controller;

import cn.com.jgyhw.sqm.domain.DiscountsGoodsClassify;
import cn.com.jgyhw.sqm.domain.JdGoodsDiscounts;
import cn.com.jgyhw.sqm.domain.PddGoodsDiscounts;
import cn.com.jgyhw.sqm.domain.TbGoodsDiscounts;
import cn.com.jgyhw.sqm.pojo.*;
import cn.com.jgyhw.sqm.service.*;
import cn.com.jgyhw.sqm.util.ApplicationParamConstant;
import cn.com.jgyhw.sqm.util.ObjectTransitionUtil;
import cn.com.jgyhw.sqm.util.PatternsUtil;
import com.github.pagehelper.Page;
import com.pdd.pop.sdk.http.api.response.PddDdkGoodsPromotionUrlGenerateResponse;
import com.taobao.api.request.TbkDgMaterialOptionalRequest;
import com.taobao.api.request.TbkDgOptimusMaterialRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by WangLei on 2019/2/23 0023 10:04
 *
 * 商品优惠查询
 */
@RequestMapping("/goodsDiscountsSearch")
@Controller
public class GoodsDiscountsSearchController extends ObjectTransitionUtil {

    private static Logger LOGGER = LogManager.getLogger(GoodsDiscountsSearchController.class);

    @Autowired
    private IJdGoodsDiscountsService jdGoodsDiscountsService;

    @Autowired
    private IJdSearchService jdSearchService;

    @Autowired
    private IPddGoodsDiscountsService pddGoodsDiscountsService;

    @Autowired
    private IPddSearchService pddSearchService;

    @Autowired
    private IDiscountsGoodsClassifyService discountsGoodsClassifyService;

    @Autowired
    private ITbSearchService tbSearchService;

    /**
     * 关键词/商品编号查询京东商品优惠
     *
     * @param jgdqpp 查询参数Pojo
     * @return
     */
    @RequestMapping("/findJdGoodsDiscountsByKeywordPage")
    @ResponseBody
    public Map<String, Object> findJdGoodsDiscountsByKeywordPage(@RequestBody JdGoodsDiscountsQueryParamPojo jgdqpp){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        String keyword = jgdqpp.getKeyword();
        if(StringUtils.isBlank(keyword)){
            resultMap.put("msg", "查询关键词为空");
        }

        //处理查询参数，验证文字是否是全部数字
        Pattern numberPattern = Pattern.compile(ApplicationParamConstant.XT_PARAM_MAP.get("regexp_all_number"));
        if(numberPattern.matcher(keyword).matches()){
            jgdqpp.setSkuIds(keyword);
            jgdqpp.setKeyword(null);
        }
        //处理查询参数，验证文字是否是网址
        Pattern urlPattern = Pattern.compile(ApplicationParamConstant.XT_PARAM_MAP.get("regexp_verify_url"));
        if(urlPattern.matcher(keyword).matches()){
            //解析网址，解析出商品编号
            Pattern pattern = Pattern.compile(ApplicationParamConstant.JD_PARAM_MAP.get("regexp_extract_url_jd_goods_id"));// 匹配商品ID的模式
            Matcher m = pattern.matcher(keyword);
            String skuId = "";
            while(m.find()){
                skuId += m.group(1);
            }
            jgdqpp.setSkuIds(skuId);
            jgdqpp.setKeyword(null);
        }

        JdGoodsDiscountsQueryResultPojo jgdqrp = jdSearchService.queryJdGoodsDiscounts(jgdqpp);

        resultMap.put("isMore", jgdqrp.isMore());

        resultMap.put("total", jgdqrp.getTotalCount());

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_jd_title_default"));

        disposeReturnDataByJd(jgdqrp.getJdGoodsDiscountsList(), resultMap);

        return resultMap;
    }

    /**
     * 分页查询京粉商品优惠
     *
     * @param classifyId 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findJdGoodsDiscountsByClassifyIdPage")
    @ResponseBody
    public Map<String, Object> findJdGoodsDiscountsByClassifyIdPage(String classifyId, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        Page<JdGoodsDiscounts> jgdPage = jdGoodsDiscountsService.queryJdGoodsDiscountsByConditionPage(classifyId, pageNo, pageSize);
        List<JdGoodsDiscounts> jgdList = jgdPage.getResult();

        long count = pageNo * pageSize;
        if(count >= jgdPage.getTotal()){
            resultMap.put("isMore", false);
        }

        resultMap.put("total", jgdPage.getTotal());

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_jd_title_default"));

        disposeReturnDataByJd(jgdList, resultMap);

        return resultMap;
    }

    /**
     * 根据参数查询京东推广链接
     *
     * @param promotionCodeReqPojo 推广链接查询参数
     * @return
     */
    @RequestMapping("/findJdCpsUrl")
    @ResponseBody
    public Map<String, Object> findJdCpsUrl(@RequestBody PromotionCodeReqPojo promotionCodeReqPojo){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        String cpsUrl = jdSearchService.queryJdCpsUrl(promotionCodeReqPojo);
        if(StringUtils.isBlank(cpsUrl)){
            // 判断是否有优惠券连接，若有，去掉优惠券连接重试
            if(!StringUtils.isBlank(promotionCodeReqPojo.getCouponUrl())){
                promotionCodeReqPojo.setCouponUrl(null);
                cpsUrl = jdSearchService.queryJdCpsUrl(promotionCodeReqPojo);
                if(StringUtils.isBlank(cpsUrl)){
                    resultMap.put("msg", "未查询到推广链接");
                    return resultMap;
                }
            }
        }
        resultMap.put("cpsUrl", cpsUrl);
        resultMap.put("status", true);
        return resultMap;
    }

    /**
     * 根据商品编号查询京东优惠商品信息
     *
     * @param goodsId 商品编号
     * @return
     */
    @RequestMapping("/findJdGoodsDiscountsByGoodsId")
    @ResponseBody
    public Map<String, Object> findJdGoodsDiscountsByGoodsId(Long goodsId){
        LOGGER.error("根据商品编号查询京东优惠商品信息，查询参数：" + goodsId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        if(goodsId == 0){
            LOGGER.error("根据商品编号查询京东优惠商品信息，参数商品编号错误");
            resultMap.put("msg", "商品编号错误");
            return resultMap;
        }
        JdGoodsDiscounts jgd = jdGoodsDiscountsService.queryJdGoodsDiscountsByGoodsId(goodsId);
        if(jgd == null){
            JdGoodsDiscountsQueryParamPojo jgdqpp = new JdGoodsDiscountsQueryParamPojo();
            jgdqpp.setSkuIds(String.valueOf(goodsId));
            JdGoodsDiscountsQueryResultPojo jgdqrp = jdSearchService.queryJdGoodsDiscounts(jgdqpp);
            if(jgdqrp != null && jgdqrp.getJdGoodsDiscountsList() != null && jgdqrp.getJdGoodsDiscountsList().size() > 0){
                jgd = jgdqrp.getJdGoodsDiscountsList().get(0);
            }else{
                LOGGER.warn("根据商品编号查询京东优惠商品信息，无查询结果");
                resultMap.put("msg", "无查询结果");
                return resultMap;
            }
        }
        resultMap.put("jdGoodsDiscounts", jgd);
        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_jd_title"));
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 处理查询京东商品优惠的返回数据
     *
     * @param jgdList 京东商品优惠集合
     * @param resultMap 返回参数
     */
    private void disposeReturnDataByJd(List<JdGoodsDiscounts> jgdList, Map<String, Object> resultMap){
        if(jgdList == null || jgdList.isEmpty()){
            resultMap.put("status", true);
            resultMap.put("msg", "未查询到京东商品优惠");
            resultMap.put("list", new ArrayList<>());
            return ;
        }

        resultMap.put("status", true);
        resultMap.put("msg", "共计查询到" + jgdList.size() + "条京东商品优惠");
        resultMap.put("list", jgdList);
    }


    /**
     * 分页查询拼多多商品优惠
     *
     * @param classifyId 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findPddGoodsDiscountsByClassifyIdPage")
    @ResponseBody
    public Map<String, Object> findPddGoodsDiscountsByClassifyIdPage(String classifyId, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        Page<PddGoodsDiscounts> pgdPage = pddGoodsDiscountsService.queryPddGoodsDiscountsByConditionPage(classifyId, pageNo, pageSize);
        List<PddGoodsDiscounts> pgdList = pgdPage.getResult();

        long count = pageNo * pageSize;
        if(count >= pgdPage.getTotal()){
            resultMap.put("isMore", false);
        }

        resultMap.put("total", pgdPage.getTotal());

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_pdd_title_default"));

        disposeReturnDataByPdd(pgdList, resultMap);

        return resultMap;
    }


    /**
     * 根据参数查询拼多多商品推广链接
     *
     * @param pddDdkGoodsPromotionUrlGenerateRequestPojo 推广链接查询参数
     * @return
     */
    @RequestMapping("/findPddCpsUrl")
    @ResponseBody
    public Map<String, Object> findPddCpsUrl(@RequestBody PddDdkGoodsPromotionUrlGenerateRequestPojo pddDdkGoodsPromotionUrlGenerateRequestPojo){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        pddDdkGoodsPromotionUrlGenerateRequestPojo.setCustomParameters(pddDdkGoodsPromotionUrlGenerateRequestPojo.getLoginKey());
        PddDdkGoodsPromotionUrlGenerateResponse pdgpugr = pddSearchService.queryPddCpsUrl(pddDdkGoodsPromotionUrlGenerateRequestPojo);

        if(pdgpugr == null){
            resultMap.put("msg", "未查询到推广链接");
            return resultMap;
        }else if(pdgpugr.getErrorResponse() != null){
            resultMap.put("msg", "查询推广链接错误");
            LOGGER.error("根据参数查询拼多多商品推广链接错误，错误码：" + pdgpugr.getErrorResponse().getErrorCode() + "；错误描述：" + pdgpugr.getErrorResponse().getErrorMsg());
            return resultMap;
        }else{
            List<PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItem> itemList = pdgpugr.getGoodsPromotionUrlGenerateResponse().getGoodsPromotionUrlList();
            if(itemList != null && !itemList.isEmpty()){
                PddDdkGoodsPromotionUrlGenerateResponse.GoodsPromotionUrlGenerateResponseGoodsPromotionUrlListItemWeAppInfo weAppInfo = itemList.get(0).getWeAppInfo();
                if(weAppInfo != null){
                    resultMap.put("pagePath", weAppInfo.getPagePath());
                    resultMap.put("appId", weAppInfo.getAppId());
                    resultMap.put("msg", "");
                    resultMap.put("status", true);
                }
                resultMap.put("weiboAppWebViewUrl", itemList.get(0).getWeAppWebViewUrl());
            }
        }
        return resultMap;
    }


    /**
     * 关键词/商品编号查询拼多多商品优惠
     *
     * @param pdgsrp 查询参数Pojo
     * @return
     */
    @RequestMapping("/findPddGoodsDiscountsByKeywordPage")
    @ResponseBody
    public Map<String, Object> findPddGoodsDiscountsByKeywordPage(@RequestBody PddDdkGoodsSearchRequestPojo pdgsrp){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        String keyword = pdgsrp.getKeyword();
        if(StringUtils.isBlank(keyword)){
            resultMap.put("msg", "查询关键词为空");
        }
        if(pdgsrp.getPageSize() == null || pdgsrp.getPageSize() < 1){
            pdgsrp.setPageSize(30);
        }

        //处理查询参数，验证文字是否是全部数字
        Pattern numberPattern = Pattern.compile(ApplicationParamConstant.XT_PARAM_MAP.get("regexp_all_number"));
        if(numberPattern.matcher(keyword).matches()){
            pdgsrp.getGoodsIdList().add(Long.valueOf(keyword));
            pdgsrp.setKeyword(null);
        }

        PddGoodsDiscountsQueryResultPojo pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, true);

        resultMap.put("isMore", pgdqrp.isMore());

        resultMap.put("total", pgdqrp.getTotalCount());

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_pdd_title_default"));

        disposeReturnDataByPdd(pgdqrp.getPddGoodsDiscountsList(), resultMap);

        return resultMap;
    }

    /**
     * 根据商品编号查询拼多多优惠商品信息
     *
     * @param goodsId 商品编号
     * @return
     */
    @RequestMapping("/findPddGoodsDiscountsByGoodsId")
    @ResponseBody
    public Map<String, Object> findPddGoodsDiscountsByGoodsId(Long goodsId){
        LOGGER.error("根据商品编号查询拼多多优惠商品信息，查询参数：" + goodsId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        if(goodsId == 0){
            LOGGER.error("根据商品编号查询拼多多优惠商品信息，参数商品编号错误");
            resultMap.put("msg", "商品编号错误");
            return resultMap;
        }
        PddGoodsDiscounts pgd = pddGoodsDiscountsService.queryPddGoodsDiscountsByGoodsId(goodsId);
        if(pgd == null){
            PddDdkGoodsSearchRequestPojo pdgsrp = new PddDdkGoodsSearchRequestPojo();
            List<Long> goodsIdList = new ArrayList<>();
            goodsIdList.add(goodsId);
            pdgsrp.setGoodsIdList(goodsIdList);
            PddGoodsDiscountsQueryResultPojo pgdqrp = pddSearchService.queryPddGoodsDiscounts(pdgsrp, true);
            if(pgdqrp != null && pgdqrp.getPddGoodsDiscountsList() != null && pgdqrp.getPddGoodsDiscountsList().size() > 0){
                pgd = pgdqrp.getPddGoodsDiscountsList().get(0);
            }else{
                LOGGER.warn("根据商品编号查询拼多多优惠商品信息，无查询结果");
                resultMap.put("msg", "无查询结果");
                return resultMap;
            }
        }
        resultMap.put("pddGoodsDiscounts", pgd);
        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_pdd_title"));
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 处理查询拼多多商品优惠的返回数据
     *
     * @param pgdList 拼多多商品优惠集合
     * @param resultMap 返回参数
     */
    private void disposeReturnDataByPdd(List<PddGoodsDiscounts> pgdList, Map<String, Object> resultMap){
        if(pgdList == null || pgdList.isEmpty()){
            resultMap.put("status", true);
            resultMap.put("msg", "未查询到拼多多商品优惠");
            resultMap.put("list", new ArrayList<>());
            return ;
        }

        resultMap.put("status", true);
        resultMap.put("msg", "共计查询到" + pgdList.size() + "条拼多多商品优惠");
        resultMap.put("list", pgdList);
    }

    /**
     * 根据优惠商品类型分页查询淘宝优惠商品集合
     *
     * @param classifyId 优惠商品类型ID
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findTbGoodsDiscountsByClassifyIdPage")
    @ResponseBody
    public Map<String, Object> findTbGoodsDiscountsByClassifyIdPage(Long classifyId, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        TbkDgOptimusMaterialRequest req = new TbkDgOptimusMaterialRequest();
        req.setMaterialId(classifyId);
        req.setPageNo(Long.valueOf(pageNo));
        req.setPageSize(Long.valueOf(pageSize));

        TbGoodsDiscountsQueryResultPojo tgdqrp = tbSearchService.queryTbGoodsTicketByMaterialId(req);

        List<TbGoodsDiscounts> tgdList = new ArrayList<>();
        if(tgdqrp.isStatus()){
            tgdList = tgdqrp.getTbGoodsDiscountsList();
            resultMap.put("isMore", tgdqrp.isMore());
            resultMap.put("total", tgdqrp.getTotal());
        }else{// 查询异常，重试一次
            tgdqrp = tbSearchService.queryTbGoodsTicketByMaterialId(req);
            if(tgdqrp.isStatus()) {
                tgdList = tgdqrp.getTbGoodsDiscountsList();
                resultMap.put("isMore", tgdqrp.isMore());
                resultMap.put("total", tgdqrp.getTotal());
            }else{
                resultMap.put("isMore", false);
                resultMap.put("total", pageNo * pageSize);
            }
        }

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_tb_title_default"));

        disposeReturnDataByTb(tgdList, resultMap);

        return resultMap;
    }

    /**
     * 根据关键词分页查询淘宝优惠商品集合
     *
     * @param keyword 关键词
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    @RequestMapping("/findTbGoodsDiscountsByKeywordPage")
    @ResponseBody
    public Map<String, Object> findTbGoodsDiscountsByKeywordPage(String keyword, int pageNo, int pageSize){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("isMore", true);
        resultMap.put("msg", "未知错误");

        if(StringUtils.isBlank(keyword)){
            resultMap.put("isMore", false);
            return resultMap;
        }

        List<TbGoodsDiscounts> tgdList = new ArrayList<>();

        //处理查询参数，验证文字是否是全部数字
        Pattern numberPattern = Pattern.compile(ApplicationParamConstant.XT_PARAM_MAP.get("regexp_all_number"));
        if(numberPattern.matcher(keyword).matches()){
            TbGoodsDiscounts tgd = tbSearchService.queryTbGoodsDiscountsByGoodsId(Long.valueOf(keyword));
            if(tgd != null){
                tgdList.add(tgd);
                resultMap.put("total", 1);
            }
            resultMap.put("total", 0);
            resultMap.put("isMore", false);
        }else{
            TbkDgMaterialOptionalRequest req = new TbkDgMaterialOptionalRequest();
            req.setPageNo(Long.valueOf(pageNo));
            req.setPageSize(Long.valueOf(pageSize));
            req.setQ(keyword);
            req.setSort(ApplicationParamConstant.TB_PARAM_MAP.get("tb_keyword_query_order"));

            TbGoodsDiscountsQueryResultPojo tgdqrp = tbSearchService.queryTbGoodsTicketByKeyword(req);

            if(tgdqrp.isStatus()){
                tgdList = tgdqrp.getTbGoodsDiscountsList();
                resultMap.put("isMore", tgdqrp.isMore());
                resultMap.put("total", tgdqrp.getTotal());
            }else{// 查询异常，重试一次
                tgdqrp = tbSearchService.queryTbGoodsTicketByKeyword(req);
                if(tgdqrp.isStatus()) {
                    tgdList = tgdqrp.getTbGoodsDiscountsList();
                    resultMap.put("isMore", tgdqrp.isMore());
                    resultMap.put("total", tgdqrp.getTotal());
                }else{
                    resultMap.put("isMore", false);
                    resultMap.put("total", pageNo * pageSize);
                }
            }
        }

        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_tb_title_default"));

        disposeReturnDataByTb(tgdList, resultMap);

        return resultMap;
    }


    /**
     * 根据参数查询淘宝优惠商品淘口令
     *
     * @param text 口令弹框文字内容
     * @param url 口令跳转目标页
     * @param logo 口令弹框图片地址
     * @return
     */
    @RequestMapping("/findTbGoodsCommand")
    @ResponseBody
    public Map<String, Object> findTbGoodsCommand(String text, String url, String logo){
        LOGGER.info("生成淘口令参数，text：" + text + "；URL：" + url + "；logo：" + logo);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        TbkTpwdCreateRequest ttcr = new TbkTpwdCreateRequest();
        ttcr.setText(text);
        ttcr.setUrl(url);
        ttcr.setLogo(logo);

        String goodsCommand = tbSearchService.queryTbGoodsCommand(ttcr);
        LOGGER.info("生成淘口令：" + goodsCommand);
        resultMap.put("goodsCommand", goodsCommand);
        resultMap.put("status", true);
        return resultMap;
    }


    /**
     * 根据商品编号查询淘宝优惠商品信息
     *
     * @param goodsId 商品编号
     * @return
     */
    @RequestMapping("/findTbGoodsDiscountsByGoodsId")
    @ResponseBody
    public Map<String, Object> findTbGoodsDiscountsByGoodsId(Long goodsId){
        LOGGER.error("根据商品编号查询淘宝优惠商品信息，查询参数：" + goodsId);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        if(goodsId == 0){
            LOGGER.error("根据商品编号查询淘宝优惠商品信息，参数商品编号错误");
            resultMap.put("msg", "商品编号错误");
            return resultMap;
        }
        TbGoodsDiscounts tgd = tbSearchService.queryTbGoodsDiscountsByGoodsId(goodsId);

        if(tgd == null){
            resultMap.put("msg", "未查询到商品信息");
            return resultMap;
        }

        resultMap.put("tbGoodsDiscounts", tgd);
        resultMap.put("shareTitle", ApplicationParamConstant.WX_PARAM_MAP.get("wx_xcx_share_tb_title"));
        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

    /**
     * 处理查询淘宝优惠商品的返回数据
     *
     * @param tgdList 淘宝优惠商品集合
     * @param resultMap 返回参数
     */
    private void disposeReturnDataByTb(List<TbGoodsDiscounts> tgdList, Map<String, Object> resultMap){
        if(tgdList == null || tgdList.isEmpty()){
            resultMap.put("status", true);
            resultMap.put("msg", "未查询到淘宝优惠商品");
            resultMap.put("list", new ArrayList<>());
            return ;
        }
        resultMap.put("status", true);
        resultMap.put("msg", "共计查询到" + tgdList.size() + "条淘宝优惠商品");
        resultMap.put("list", tgdList);
    }

    /**
     * 根据平台标识查询优惠商品类型
     *
     * @param platform 平台标识
     * @return
     */
    @RequestMapping("/findDiscountsGoodsClassifyListByPlatform")
    @ResponseBody
    public Map<String, Object> findDiscountsGoodsClassifyListByPlatform(Integer platform){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        List<DiscountsGoodsClassify> dgcList = null;
        if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_jd")) == platform){
            dgcList = ApplicationParamConstant.GOODS_CLASSIFY_MAP.get("dgcJd");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_pdd")) == platform){
            dgcList = ApplicationParamConstant.GOODS_CLASSIFY_MAP.get("dgcPdd");
        }else if(Integer.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("order_platform_tb")) == platform){
            dgcList = ApplicationParamConstant.GOODS_CLASSIFY_MAP.get("dgcTb");
        }
        if(dgcList == null){
            dgcList = discountsGoodsClassifyService.queryDiscountsGoodsClassifyListByPlatform(platform);
        }
        if(dgcList == null || dgcList.isEmpty()){
            return resultMap;
        }
        resultMap.put("status", true);
        resultMap.put("list", dgcList);
        resultMap.put("msg", "");
        return resultMap;
    }


    /**
     * 根据剪贴板内容跳转对应页面
     *
     * @param scrapbook 剪贴板内容
     * @return
     */
    @RequestMapping("/disposeScrapbookContent")
    @ResponseBody
    public Map<String, Object> disposeScrapbookContent(String scrapbook){
        LOGGER.info("根据剪贴板内容跳转对应页面--> 源参数：" + scrapbook);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        // 剪贴板无内容
        if(StringUtils.isBlank(scrapbook)){
            resultMap.put("msg", "剪贴板无内容");
            return resultMap;
        }
        scrapbook.trim();
        try {
            scrapbook = URLDecoder.decode(scrapbook, "utf-8");
            LOGGER.info("根据剪贴板内容跳转对应页面--> 转码参数：" + scrapbook);

            // 验证剪贴板是否包含连接
            if(scrapbook.indexOf("https://") != -1){ // 包含网址
                // 判断网址平台类型
                if(scrapbook.indexOf("jd.com") != -1){// 京东网址
                    // 解析商品编号
                    Pattern pattern = Pattern.compile(ApplicationParamConstant.JD_PARAM_MAP.get("regexp_extract_url_jd_goods_id"));// 匹配商品ID的模式
                    Matcher m = pattern.matcher(scrapbook);
                    String goodsId = "";
                    while(m.find()){
                        goodsId += m.group(1);
                    }
                    if(!StringUtils.isBlank(goodsId)){
                        resultMap.put("status", true);
                        resultMap.put("page", "/pages/jd/jd?goodsId=" + goodsId);
                    }
                    LOGGER.info("根据剪贴板内容跳转对应页面--> 京东解析商品ID结果：" + goodsId);
                    return resultMap;
                }else if(scrapbook.indexOf("yangkeduo.com") != -1){ // 拼多多
                    // 解析商品编号
                    Pattern pattern = Pattern.compile(ApplicationParamConstant.PDD_PARAM_MAP.get("regexp_extract_url_pdd_goods_id"));// 匹配商品ID的模式
                    Matcher m = pattern.matcher(scrapbook);
                    String goodsId = "";
                    while(m.find()){
                        goodsId += m.group(1);
                    }
                    if(!StringUtils.isBlank(goodsId)){
                        resultMap.put("status", true);
                        resultMap.put("page", "/pages/pdd/pdd?goodsId=" + goodsId);
                    }
                    LOGGER.info("根据剪贴板内容跳转对应页面--> 拼多多解析商品ID结果：" + goodsId);
                    return resultMap;
                }else if(scrapbook.indexOf("tb.cn") != -1){ // 淘宝
                    // 解析出链接
                    String tbUrl = null;
                    Matcher matcher = PatternsUtil.WEB_URL.matcher(scrapbook);
                    if (matcher.find()){
                        tbUrl = matcher.group();
                    }
                    String goodsId = "";
                    if(StringUtils.isBlank(tbUrl)){ // 未解析出地址
                        resultMap.put("msg", "未从文本中解析出淘宝推广地址");
                        LOGGER.info("根据剪贴板内容跳转对应页面--> 未从文本中解析出淘宝推广地址");
                        return resultMap;
                    }else{
                        CloseableHttpClient httpClient = HttpClients.createDefault();
                        HttpGet httpGet = new HttpGet(tbUrl);
                        CloseableHttpResponse response = null;
                        try {
                            response = httpClient.execute(httpGet);
                            HttpEntity entity = null;
                            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                                entity = response.getEntity();
                                String respContent = EntityUtils.toString(entity, Charset.forName("UTF-8"));

                                // 通过返回结果解析商品编号
                                Pattern pattern = Pattern.compile(ApplicationParamConstant.TB_PARAM_MAP.get("regexp_extract_url_tb_goods_id"));// 匹配商品ID的模式
                                Matcher m = pattern.matcher(respContent);

                                while(m.find()){
                                    goodsId += m.group(1);
                                }
                                if(!StringUtils.isBlank(goodsId)){
                                    resultMap.put("status", true);
                                    resultMap.put("skipType", "al");
                                    resultMap.put("page", "/pages/al/al?goodsId=" + goodsId);
                                }
                            }
                            if(entity != null){
                                EntityUtils.consume(entity);
                            }
                        } catch (IOException e) {
                            LOGGER.error("请求淘宝推广地址异常--> ");
                            e.printStackTrace();
                        } finally {
                            try {
                                if(response != null){
                                    response.close();
                                }
                                if(httpClient != null){
                                    httpClient.close();
                                }
                            } catch (IOException e) {
                                LOGGER.error("关闭淘宝推广地址请求异常--> ");
                                e.printStackTrace();
                            }
                        }
                    }
                    LOGGER.info("根据剪贴板内容跳转对应页面--> 淘宝解析商品ID结果：" + goodsId);
                    return resultMap;
                }else{ // 未识别，什么都不是
                    resultMap.put("msg", "无法识别链接");
                    LOGGER.info("根据剪贴板内容跳转对应页面--> 无法识别链接");
                    return resultMap;
                }
            }else{ // 其他不包含连接信息
                resultMap.put("msg", "其他不包含连接信息");
                LOGGER.info("根据剪贴板内容跳转对应页面--> 剪贴板无链接");
                return resultMap;
            }

        } catch (UnsupportedEncodingException e) {
            LOGGER.error("根据剪贴板内容跳转对应页面--> URLDecoder出错", e);
            resultMap.put("msg", "URLDecoder出错");
            return resultMap;
        }
    }

    /**
     * 查询动态显示/隐藏项目
     *
     * @param version 小程序版本号
     * @return
     */
    @RequestMapping("/findDynamicShowHiddenItem")
    @ResponseBody
    public Map<String, Object> findDynamicShowHiddenItem(String version){
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("status", false);
        resultMap.put("msg", "未知错误");

        if(ApplicationParamConstant.XT_PARAM_MAP.get("xcx_release_version_no").equals(version)){// 发布版本号
            resultMap.put("isHiddenSearch", Boolean.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("is_hidden_search")));
            resultMap.put("isHiddenShare", Boolean.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("is_hidden_share")));
            resultMap.put("isHiddenTmMenu", Boolean.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("is_hidden_tm_menu")));
            if(Boolean.valueOf(ApplicationParamConstant.XT_PARAM_MAP.get("is_hidden_tm_menu"))){
                resultMap.put("tmKeyword", "");
            }else{
                resultMap.put("tmKeyword", "天猫");
            }
        }else{// 其他版本
            resultMap.put("isHiddenSearch", true);
            resultMap.put("isHiddenShare", true);
            resultMap.put("isHiddenTmMenu", true);
            resultMap.put("tmKeyword", "");
        }

        resultMap.put("status", true);
        resultMap.put("msg", "");
        return resultMap;
    }

}
