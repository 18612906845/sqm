$.pc = $.pc || {};
$.pc.top = $.pc.top || {};

(function($){

    "use strict";

    $(document).ready(function() {

        /**
         * 二维码hover事件
         */
        $('.qrCodeImage').hover(function(){
            var qrCodeType = $(this).attr("qrCodeType");
            if(qrCodeType == "gzh"){
                $("#magnifyGzhQrCode").show();
            }else{
                $("#magnifyXcxQrCode").show();
            }
        },function(){
            $("#magnifyGzhQrCode").hide();
            $("#magnifyXcxQrCode").hide();
        });

        /**
         * 搜索框输入事件
         */
        $(document).on("keyup", "#keyword", function () {
            var keyword = $(this).val();
            if(keyword.length > 0){
                $("#clearKeyword").show();
            }else{
                $("#clearKeyword").hide();
            }
        });


        findSystemNotice();
    });

    /**
     * 查询系统公告
     */
    function findSystemNotice(){
        $.ajax({
            url:context +'/findSystemNotice',
            data:{},
            type:"get",
            // dataType:"json",
            success:function(successData){
                $("#notice").text("公告：" + successData);
            },
            error:function (errorData) {
                console.log("出错了");
                console.log(errorData);
            }
        });
    }


    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.pc.top, {

    });
})(jQuery);