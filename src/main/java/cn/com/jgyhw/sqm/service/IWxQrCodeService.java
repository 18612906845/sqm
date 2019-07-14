package cn.com.jgyhw.sqm.service;

import cn.com.jgyhw.sqm.domain.PopularizeTarget;

/**
 * Created by WangLei on 2019/5/7 0007 13:07
 *
 * 微信生成二维码接口
 */
public interface IWxQrCodeService {

    /**
     * 创建用户分享微信小程序无限量带参数二维码（官方方案B）
     *
     * @param scene 最大32个可见字符，只支持数字，大小写英文以及部分特殊字符：!#$&'()*+,/:;=?@-._~，其它字符请自行编码为合法字符（因不支持%，中文无法使用 urlencode 处理，请使用其他编码方式）
     * @param page 必须是已经发布的小程序存在的页面（否则报错），例如 pages/index/index, 根路径前不要填加 /,不能携带参数（参数请放在scene字段里），如果不填写这个字段，默认跳主页面
     * @param isHyaline 是否需要透明底色，为 true 时，生成透明底色的小程序
     * @param width 二维码的宽度，单位 px。最小 280px，最大 1280px，默认430
     * @return
     */
    String createWxXcxInfinitudeQrCodeByShare(String scene, String page, boolean isHyaline, int width);

    /**
     * 创建线下推广主体微信小程序无限量带参数二维码（官方方案B）
     *
     * @param popularizeTarget 线下推广主体
     * @return
     */
    String createWxXcxInfinitudeQrCodeByPopularize(PopularizeTarget popularizeTarget);
}
