$.parameterSettingNew = $.parameterSettingNew || {};

(function($){

    "use strict";

    var form = null;
    var element = null;
    var layer = null;


    $(document).ready(function() {

        //初始化layerUi控件
        layui.use(['form', 'layer', 'element'], function() {
            form = layui.form;
            layer = layui.layer;
            element = layui.element;

            //监听提交
            form.on('submit(save)', function(data){
                newConfiguration(data);
                return false;
            });

        });

    });

    /**
     * 新建参数配置
     *
     * @param data 参数配置
     */
    function newConfiguration(data){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        $.ajax({
            url:context +'/xpsXtglAuth/newConfiguration',
            data: JSON.stringify(data.field),
            type: "post",
            dataType: "json",
            contentType: "application/json",
            success: function(successData){
                if(successData.status){
                    parent.$.parameterSettingList.showOperationPop(1, "新建成功");
                    parent.$.parameterSettingList.updateTable();
                    parent.layer.close(index);
                }else{
                    var msg = successData.msg;
                    if($.util.isBlank(msg)){
                        parent.$.parameterSettingList.showOperationPop(2, "新建失败");
                        parent.layer.close(index);
                    }else{
                        parent.$.parameterSettingList.showOperationPop(0, msg);
                    }
                }
            },
            error: function (errorData) {
                parent.$.parameterSettingList.showOperationPop(2, "未知错误");
                parent.layer.close(index);
            }
        });
    }


    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.parameterSettingNew, {

    });
})(jQuery);