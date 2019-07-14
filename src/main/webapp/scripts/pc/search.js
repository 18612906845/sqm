$.pc = $.pc || {};
$.pc.search = $.pc.search || {};

(function($){

    "use strict";

    var scrollToptimer = null;

    $(document).ready(function() {
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
    });



    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.pc.search, {

    });
})(jQuery);