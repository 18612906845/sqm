$.wxTokenManage = $.wxTokenManage || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var wxTokenTable = null;

    $(document).ready(function() {

        //初始化layerUi控件
        layui.use(['form', 'table', 'layer', 'element'], function() {
            form = layui.form;
            table = layui.table;
            layer = layui.layer;
            element = layui.element;

            //监听工具条
            table.on('tool(wxTokenTable)', function(obj){
                var data = obj.data;
                if(obj.event === 'edit'){
                    refreshWxTokenByTokenType(data.tokenType);
                }
            });

            initTable(); //初始化Table
        });

        initPage(); //初始化页面
    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单组选中
        $("#weChatParamManage").addClass("layui-nav-itemed");
        //设置子菜单选中
        $("#wxTokenManage").parent("dd").addClass("layui-this");
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        wxTokenTable = table.render({
            title: '微信令牌表',
            elem: '#wxTokenTable',
            url:context +'/xwtXtglAuth/findWxTokenAll',
            method: 'post',
            cols: [[
                {field:'appId', title: '微信应用ID', width: 200},
                {field:'token', title: '令牌数值', minWidth: 500},
                {field:'tokenTypeStr', title: '令牌类型', width: 150},
                {field:'updateTimeStr', title: '更新时间', width: 180},
                {fixed:'right', align:'center', title: '操作', width: 80, templet: function(data){
                        var td = '<a class="layui-btn layui-btn-xs" lay-event="edit">更新</a>';
                        return td;
                    }
                }
            ]],
            page: false
        });
    }

    /**
     * 刷新表格
     */
    function updateTable(){
        if(null != wxTokenTable && undefined != wxTokenTable){
            wxTokenTable.reload({});
        }
    }

    /**
     * 根据令牌类型刷新令牌
     *
     * @param tokenType 令牌类型
     */
    function refreshWxTokenByTokenType(tokenType) {
        $.ajax({
            url: context +'/xwtXtglAuth/refreshWxTokenByTokenType',
            data: {
                tokenType : tokenType
            },
            type: "get",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    showOperationPop(1,'更新成功');
                }else{
                    var msg = successData.msg;
                    if($.util.isBlank(msg)){
                        showOperationPop(1,'更新失败');
                    }else{
                        showOperationPop(0,msg);
                    }
                }
                updateTable();
            },
            error: function (errorData) {
                showOperationPop(2,'未知错误');
            }
        });
    }

    /**
     * 显示操作状态提示窗
     *
     * @param icon 图标
     * @param msg 提示语
     */
    function showOperationPop(icon, msg){
        layer.msg(msg, {icon: icon, offset: 'auto'});
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.wxTokenManage, {

    });
})(jQuery);