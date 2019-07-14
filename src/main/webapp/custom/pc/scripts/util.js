$.custom = $.custom || {};
$.custom.util = $.custom.util || {};

(function($){

    "use strict";

    $(document).ready(function() {
        /**
         * 初始化轮播
         */
        $('.carousel').carousel({
            interval: 2000
        });



    });

    /**
     * 初始化分页控件
     *
     * @param select 选择器
     * @param option 自定义参数
     */
    var initPaginator = function (select, option) {
        var settings = {
            bootstrapMajorVersion:3,
            currentPage: 1,
            numberOfPages: 5,
            totalPages: 1,
            itemTexts: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "上一页";
                    case "next":
                        return "下一页";
                    case "last":
                        return "尾页";
                    case "page":
                        return page;
                }
            },
            tooltipTitles: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "跳转到第一页";
                    case "prev":
                        return "跳转到上一页";
                    case "next":
                        return "跳转到下一页";
                    case "last":
                        return "跳转到最后一页";
                    case "page":
                        return "跳转到底 " + page + "页";
                }
            },
            onPageChanged: function(e,oldPage,newPage){
                console.log("Current page changed, old: "+oldPage+" new: "+newPage);
            }
        };
        if(option != null && option != undefined){
            $.each(option, function (key, val) {
                settings[key] = val;
            });
        }
        if(select == "" || select == null || select == undefined){
            return $(".pagination").bootstrapPaginator(settings);
        }else{
            return $("" + select).bootstrapPaginator(settings);
        }
    }

    /**
     * 重新设置总页码
     *
     * @param select 选择器
     * @param totalPages 页码总数
     */
    var updateTotalPages = function(select, totalPages){

        if(select == "" || select == null || select == undefined){
            return $(".pagination").bootstrapPaginator("setOptions", {
                totalPages: totalPages
            });
        }else{
            return $("" + select).bootstrapPaginator("setOptions", {
                totalPages: totalPages
            });
        }
    }

    /**
     * 设置页码
     *
     * @param select 选择器
     * @param currentPage 页码值
     */
    var setCurrentPage = function(select, currentPage){
        if(select == "" || select == null || select == undefined){
            return $(".pagination").bootstrapPaginator("setOptions", {
                currentPage: currentPage
            });
        }else{
            return $("" + select).bootstrapPaginator("setOptions", {
                currentPage: currentPage
            });
        }
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.custom.util, {
        initPaginator : initPaginator ,
        updateTotalPages: updateTotalPages ,
        setCurrentPage: setCurrentPage
    });
})(jQuery);