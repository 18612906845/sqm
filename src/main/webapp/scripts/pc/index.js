$.pc = $.pc || {};
$.pc.index = $.pc.index || {};

(function($){

    "use strict";

    var scrollToptimer = null;

    var pageSize = 12;// 每页条数
    var loginKey = 1;// 登陆标识

    $(document).ready(function() {
        /**
         * 初始化分页-京东
         */
        $.custom.util.initPaginator("#paginationJd", {
            size:"large",
            onPageChanged: function(e, oldPageNo, newPageNo){
                var keyword = $("#keyword").val();
                if(keyword.length > 1){
                    findJdGoodsDiscountsByKeyword(keyword, newPageNo, pageSize);
                }else{
                    findJdGoodsDiscountsByClassifyIdPage(1, newPageNo, pageSize);
                }
            }
        });
        /**
         * 初始化分页-拼多多
         */
        $.custom.util.initPaginator("#paginationPdd", {
            size:"large",
            onPageChanged: function(e,oldPageNo, newPageNo){
                var keyword = $("#keyword").val();
                if(keyword.length > 1){
                    findPddGoodsDiscountsByKeyword(keyword, newPageNo, pageSize);
                }else{
                    findPddGoodsDiscountsByClassifyIdPage(1, newPageNo, pageSize);
                }
            }
        });


        /**
         * 返回顶部
         */
        $(document).on("click", "#goTop", function () {
            scrollToptimer = setInterval(function () {
                var top = document.body.scrollTop || document.documentElement.scrollTop;
                var speed = top / 4;
                if (document.body.scrollTop!=0) {
                    document.body.scrollTop -= speed;
                }else {
                    document.documentElement.scrollTop -= speed;
                }
                if (top == 0) {
                    clearInterval(scrollToptimer);
                }
            }, 30);
        })
        
        $(document).on("click", ".wuyou-login", function () {
            $("#loginPage").modal('show');
            $(".wuyou-waiter").hide();
        })

        $('#loginPage').on('hidden.bs.modal', function () {
            $(".wuyou-waiter").show();
        })

        /**
         * 京东下单点击事件
         */
        $(document).on("click", ".jdOrderBut", function () {
            var materialUrl = $(this).attr("materialUrl");
            var link = $(this).attr("link");
            findJdCpsUrl(materialUrl, link);
        });

        /**
         * 拼多多下单点击事件
         */
        $(document).on("click", ".pddOrderBut", function () {
            var goodsId = $(this).attr("goodsId");
            findPddCpsUrl(goodsId);
        });

        /**
         * 搜索点击事件
         */
        $(document).on("click", "#searchBut", function () {
            var keyword = $("#keyword").val();
            if(keyword.length < 1){
                alert("请输入关键词");
                return;
            }
            findJdGoodsDiscountsByKeyword(keyword, 1, pageSize);
            findPddGoodsDiscountsByKeyword(keyword, 1, pageSize);
            $.custom.util.setCurrentPage("#paginationJd", 1);
            $.custom.util.setCurrentPage("#paginationPdd", 1);
        });

        /**
         * 清除点击事件
         */
        $(document).on("click", "#clearKeyword", function () {
            $("#keyword").val("");
            findJdGoodsDiscountsByClassifyIdPage(1, 1, pageSize);
            findPddGoodsDiscountsByClassifyIdPage(1, 1, pageSize);
            $.custom.util.setCurrentPage("#paginationJd", 1);
            $.custom.util.setCurrentPage("#paginationPdd", 1);
            $(this).hide();
        });

        /**
         * 轮播图点击事件
         */
        $(document).on("click", ".carousel-image", function () {
            var url = $(this).attr("url");
            window.open(url);
        });


        // 初始化页面数据
        findJdGoodsDiscountsByClassifyIdPage(1, 1, pageSize);
        findPddGoodsDiscountsByClassifyIdPage(1, 1, pageSize);

    });

    /**
     * 获取京东商品推广连接
     *
     * @param materialUrl 商品落地页
     * @param link 优惠券连接
     */
    function findJdCpsUrl(materialUrl, link) {
        var data = {
            materialId: materialUrl,
            couponUrl: link,
            loginKey: loginKey
        }
        $.ajax({
            url:context +'/goodsDiscountsSearch/findJdCpsUrl',
            data: JSON.stringify(data),
            type:"post",
            contentType: "application/json",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    // 跳转京东页面
                    window.open(successData.cpsUrl);
                }else{
                    // 提示错误
                    alert(successData.msg);
                }
            },
            error:function (errorData) {
                console.log("出错了");
                console.log(errorData);
            }
        });
    }

    /**
     * 查询京东推荐商品详情（分页）
     *
     * @param classifyId 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     */
    function findJdGoodsDiscountsByClassifyIdPage(classifyId, pageNo, pageSize){
        var jdPageSizeSum = 1;
        $.ajax({
            url:context +'/goodsDiscountsSearch/findJdGoodsDiscountsByClassifyIdPage',
            data:{
                classifyId : classifyId,
                pageNo : pageNo,
                pageSize : pageSize
            },
            type:"get",
            dataType:"json",
            success:function(successData){

                if(successData.status){
                    // 计算京东共计有多少页
                    var total = successData.total;
                    if(total % pageSize > 0){
                        jdPageSizeSum = parseInt(total / pageSize) + 1;
                    }else{
                        jdPageSizeSum = parseInt(total / pageSize);
                    }
                    $("#jdNoData").hide();
                    // 设置京东产品列表
                    setJdGoodsDiscountsPageList(successData.list);
                }else{
                    setJdGoodsDiscountsPageList([]);
                    $("#jdNoData").show();
                }
                $.custom.util.updateTotalPages("#paginationJd", jdPageSizeSum);
            },
            error:function (errorData) {
                console.log("出错了");
                console.log(errorData);
            }
        });
    }

    /**
     * 设置京东商品列表
     *
     * @param dataArray 列表数据数组
     */
    function setJdGoodsDiscountsPageList(dataArray) {
        // 清空页面数据
        $("#jdGoodsUl").html("");
        // 循环添加数据
        if(dataArray != undefined && dataArray != null && dataArray.length > 0){
            $.each(dataArray, function (i, val) {
                var html = getJdGoodsDiscountsHtmlByIndexAndData(i+1, val);
                $("#jdGoodsUl").append(html);
            });
        }
    }

    /**
     * 获取京东产品页面HTML
     *
     * @param index 第几个产品
     * @param data 产品数据
     */
    function getJdGoodsDiscountsHtmlByIndexAndData(index, data){
        // data.goodsName.substring(0, 15)
        var liClass = "";
        if(index % 4 == 0){
            liClass = "no-right";
        }
        var ownerName = data.owner  == "g" ? "自营" : "商家";
        var html =
            '<li class="' + liClass +'">' +
                '<img src="' + data.goodsImgUrl + '">' +
                '<div class="goods-info">' +
                    '<p class="goods-name-p">' +
                        '<span class="label label-danger">' + ownerName + '</span>' +
                        '<span class="goods-name" title="' + data.goodsName + '">' + data.goodsName + '</span>' +
                    '</p>' +
                    '<div class="row price-sales-num">' +
                        '<div class="col-xs-6">' + data.lowestPriceName + ' ：¥' + data.lowestPrice + '</div>' +
                        '<div class="col-xs-6 ">月售 ' + data.salesVolume + '</div>' +
                    '</div>' +
                    '<div class="row discount-purchase">' +
                        '<div class="col-xs-6">' +
                            '<span>¥' +
                                '<em style="font-size: 26px;">' + data.returnMoney + '</em><i></i>' +
                            '</span>' +
                        '</div>' +
                        '<div class="col-xs-6 ">' +
                            '<button type="button" class="btn btn-danger jdOrderBut" materialUrl="' + data.materialUrl + '" link="' + data.link + '">京东下单</button>' +
                        '</div>' +
                    '</div>' +
                '</div>' +
            '</li>';
        return html;
    }

    /**
     * 获取拼多多商品推广连接
     *
     * @param goodsId 商品编号
     */
    function findPddCpsUrl(goodsId) {
        var data = {
            goodsId: goodsId,
            loginKey: loginKey
        }
        $.ajax({
            url:context +'/goodsDiscountsSearch/findPddCpsUrl',
            data: JSON.stringify(data),
            type:"post",
            contentType: "application/json",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    // 跳转拼多多页面
                    window.open(successData.weiboAppWebViewUrl);
                }else{
                    // 提示错误
                    alert(successData.msg);
                }
            },
            error:function (errorData) {
                console.log("出错了");
                console.log(errorData);
            }
        });
    }

    /**
     * 查询拼多多推荐商品详情（分页）
     *
     * @param classifyId 商品类型
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     */
    function findPddGoodsDiscountsByClassifyIdPage(classifyId, pageNo, pageSize){
        var jdPageSizeSum = 1;
        $.ajax({
            url:context +'/goodsDiscountsSearch/findPddGoodsDiscountsByClassifyIdPage',
            data:{
                classifyId : classifyId,
                pageNo : pageNo,
                pageSize : pageSize
            },
            type:"get",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    // 计算拼多多共计有多少页
                    var total = successData.total;
                    if(total % pageSize > 0){
                        jdPageSizeSum = parseInt(total / pageSize) + 1;
                    }else{
                        jdPageSizeSum = parseInt(total / pageSize);
                    }
                    $("#pddNoData").hide();
                    // 设置拼多多产品列表
                    setPddGoodsDiscountsPageList(successData.list);
                }else{
                    setPddGoodsDiscountsPageList([]);
                    $("#pddNoData").show();
                }
                $.custom.util.updateTotalPages("#paginationPdd", jdPageSizeSum);
            },
            error:function (errorData) {
                console.log("出错了");
                console.log(errorData);
            }
        });
    }

    /**
     * 设置京东商品列表
     *
     * @param dataArray 列表数据数组
     */
    function setPddGoodsDiscountsPageList(dataArray) {
        // 清空页面数据
        $("#pddGoodsUl").html("");
        // 循环添加数据
        if(dataArray != undefined && dataArray != null && dataArray.length > 0){
            $.each(dataArray, function (i, val) {
                var html = getPddGoodsDiscountsHtmlByIndexAndData(i+1, val);
                $("#pddGoodsUl").append(html);
            });
        }
    }


    /**
     * 获取拼多多产品页面HTML
     *
     * @param index 第几个产品
     * @param data 产品数据
     */
    function getPddGoodsDiscountsHtmlByIndexAndData(index, data){
        var liClass = "";
        if(index % 4 == 0){
            liClass = "no-right";
        }
        var html =
            '<li class="' + liClass +'">' +
                '<img src="' + data.goodsImgUrl + '">' +
                '<div class="goods-info pdd-info">' +
                    '<p style="word-break: break-all;display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 1;overflow: hidden;text-overflow: ellipsis;">' +
                        '<span class="label label-danger">拼多多</span>' +
                        '<span title="' + data.goodsName + '">' + data.goodsName + '</span>' +
                    '</p>' +
                    '<div class="row price-sales-num">' +
                        '<div class="col-xs-6">' + data.lowestPriceName + '：¥' + data.lowestPrice + '</div>' +
                        '<div class="col-xs-6 ">月售 ' + data.salesVolume + '</div>' +
                    '</div>' +
                    '<div class="row discount-purchase">' +
                        '<div class="col-xs-6">' +
                            '<span>¥' +
                                '<em style="font-size: 26px;">' + data.returnMoney + '</em><i></i>' +
                            '</span>' +
                        '</div>' +
                    '<div class="col-xs-6 ">' +
                        '<button type="button" class="btn btn-danger pddOrderBut" goodsId="' + data.goodsId + '">拼多多下单</button>' +
                    '</div>' +
                '</div>' +
            '</div>' +
        '</li>';
        return html;
    }

    /**
     * 根据关键词查询京东商品
     *
     * @param keyword 关键词
     * @param pageIndex 页号
     * @param pageSize 每页条数
     */
    function findJdGoodsDiscountsByKeyword(keyword, pageIndex, pageSize){
        var jdPageSizeSum = 1;
        var data = {
            keyword: keyword,
            pageIndex: pageIndex,
            pageSize: pageSize
        }
        $.ajax({
            url:context +'/goodsDiscountsSearch/findJdGoodsDiscountsByKeywordPage',
            data: JSON.stringify(data),
            type:"post",
            contentType: "application/json",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    // 计算京东共计有多少页
                    var total = successData.total;
                    if(total % pageSize > 0){
                        jdPageSizeSum = parseInt(total / pageSize) + 1;
                    }else{
                        jdPageSizeSum = parseInt(total / pageSize);
                    }
                    if(jdPageSizeSum > 100){
                        jdPageSizeSum = 100;
                    }
                    var dataArray = successData.list;
                    if(dataArray.length > 0){
                        $("#jdNoData").hide();
                        setJdGoodsDiscountsPageList(dataArray);
                    }else{
                        $("#jdNoData").show();
                        setJdGoodsDiscountsPageList([]);
                    }
                }else{
                    $("#jdNoData").show();
                    setJdGoodsDiscountsPageList([]);
                }
                $.custom.util.updateTotalPages("#paginationJd", jdPageSizeSum);
            },
            error:function (errorData) {
                $("#jdNoData").show();
                setJdGoodsDiscountsPageList([]);
            }
        });
    }

    /**
     * 根据关键词查询拼多多商品
     *
     * @param keyword 关键词
     * @param page 页号
     * @param pageSize 每页条数
     */
    function findPddGoodsDiscountsByKeyword(keyword, page, pageSize){
        var jdPageSizeSum = 1;
        var data = {
            keyword: keyword,
            page: page,
            pageSize: pageSize
        }
        $.ajax({
            url:context +'/goodsDiscountsSearch/findPddGoodsDiscountsByKeywordPage',
            data: JSON.stringify(data),
            type:"post",
            contentType: "application/json",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    // 计算拼多多共计有多少页
                    var total = successData.total;
                    if(total % pageSize > 0){
                        jdPageSizeSum = parseInt(total / pageSize) + 1;
                    }else{
                        jdPageSizeSum = parseInt(total / pageSize);
                    }
                    if(jdPageSizeSum > 100){
                        jdPageSizeSum = 100;
                    }
                    var dataArray = successData.list;
                    if(dataArray.length > 0){
                        $("#pddNoData").hide();
                        setPddGoodsDiscountsPageList(dataArray);
                    }else{
                        $("#pddNoData").show();
                        setPddGoodsDiscountsPageList([]);
                    }
                }else{
                    $("#pddNoData").show();
                    setPddGoodsDiscountsPageList([]);
                }
                $.custom.util.updateTotalPages("#paginationPdd", jdPageSizeSum);
            },
            error:function (errorData) {
                $("#pddNoData").show();
                setPddGoodsDiscountsPageList([]);
            }
        });
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.pc.index, {

    });

})(jQuery);