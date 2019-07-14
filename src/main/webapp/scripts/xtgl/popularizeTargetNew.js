$.popularizeTargetNew = $.popularizeTargetNew || {};

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
                batchCreatePopularizeTargetBySize(data);
                return false;
            });

        });

    });

    /**
     * 新建推广主体
     *
     * @param data 推广主体
     */
    function batchCreatePopularizeTargetBySize(data){
        var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        $.ajax({
            url:context +'/xptXtglAuth/batchCreatePopularizeTargetBySize',
            data: {
                size: data.field.createSize
            },
            type: "get",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    parent.$.popularizeTargetList.showOperationPop(1, "批量创建成功");
                    parent.$.popularizeTargetList.updateTable();
                    parent.layer.close(index);
                }else{
                    var msg = successData.msg;
                    if($.util.isBlank(msg)){
                        parent.$.popularizeTargetList.showOperationPop(2, "批量创建失败");
                        parent.layer.close(index);
                    }else{
                        parent.$.popularizeTargetList.showOperationPop(0, msg);
                    }
                }
            },
            error: function (errorData) {
                parent.$.popularizeTargetList.showOperationPop(2, "未知错误");
                parent.layer.close(index);
            }
        });
    }


    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.popularizeTargetNew, {

    });
})(jQuery);