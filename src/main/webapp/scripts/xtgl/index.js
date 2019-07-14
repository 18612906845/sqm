$.index = $.index || {};

(function($){

    "use strict";

    var form = null;
    var element = null;
    var layer = null;

    $(document).ready(function() {

        //初始化layerUi控件
        layui.use(['form', 'layer', 'element'], function() {
            form = layui.form;
            form.render();
            layer = layui.layer;
            element = layui.element;
        });

        initPage();
    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单选中
        $("#systemIndex").addClass("layui-this");
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.index, {

    });
})(jQuery);