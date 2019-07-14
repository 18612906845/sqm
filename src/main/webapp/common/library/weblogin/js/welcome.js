$.welcome = $.welcome || {};

(function($){

    "use strict";

    $(document).ready(function() {

        /**
         * 登陆点击事件
         */
        $(document).on('click', '#loginBut', function () {
            login();
        });

        /**
         * icp备案号点击事件
         */
        $(document).on('click', '#icpba', function () {
            window.open("http://www.miitbeian.gov.cn");
        });

        /**
         * 回车事件绑定
         */
        $(document).on('keyup', function(event) {
            if (event.keyCode == "13") {
                login();
            }
        });
    });

    /**
     * 登陆
     */
    function login() {
        var logpass = hex_md5($("#logpass").val());
        $("#loginpass").val(logpass);

        console.log($("#loginForm").serialize());
        $("#loginForm").submit();
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.welcome, {

    });
})(jQuery);