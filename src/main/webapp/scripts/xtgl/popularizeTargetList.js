$.popularizeTargetList = $.popularizeTargetList || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var popularizeTargetTable = null;
    var popularizeTargetTableThisPageNo = 1;

    $(document).ready(function() {

        //初始化layerUi控件
        layui.use(['form', 'table', 'layer', 'element'], function() {
            form = layui.form;
            table = layui.table;
            layer = layui.layer;
            element = layui.element;

            //监听提交
            form.on('submit(search)', function(data){
                updateTable();
                return false;
            });

            //监听头工具栏事件
            table.on('toolbar(popularizeTargetTable)', function(obj){
                if(obj.event === "add"){
                    layer.open({
                        title: "批量创建线下推广码",
                        type: 2,
                        area: ['700px', '400px'],
                        fixed: false, //不固定
                        content: context + '/xptXtglAuth/openPopularizeTargetNewPage'
                    });
                }
            });

            //监听工具条
            table.on('tool(popularizeTargetTable)', function(obj){
                var data = obj.data;
                if(obj.event === 'download'){
                    window.open(data.qrCodeUrl);
                }
            });

            //监听单元格编辑
            table.on('edit(popularizeTargetTable)', function(obj){
                var value = obj.value; //得到修改后的值
                var data = obj.data; //得到所在行所有键值
                var field = obj.field; //得到字段
                updatePopularizeTargetById(data.id, field, value);
            });

            initTable(); //初始化Table
        });

        initPage(); //初始化页面

        /**
         * 重置
         */
        $(document).on("click", "#reset", function () {
            setTimeout(function () {
                updateTable();
            },1000);
        });

    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单组选中
        $("#businessData").addClass("layui-nav-itemed");
        //设置子菜单选中
        $("#popularizeTarget").parent("dd").addClass("layui-this");
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        popularizeTargetTable = table.render({
            title: '推广主体记录',
            elem: '#popularizeTargetTable',
            url:context +'/xptXtglAuth/findPopularizeTargetByPage',
            toolbar: '#toolbarDemo',
            method: 'post',
            request: {
                pageName: 'pageNo', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            where: {
                wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                keyword: $('#keyword') .val()
            },
            cols: [[
                {field:'id', title: '主体标识', width: 90},
                {field:'targetName', title: '主体名称', width: 300, edit: 'text'},
                {field:'name', title: '负责人姓名', width: 100, edit: 'text'},
                {field:'phone', title: '负责人电话', width: 120, edit: 'text'},
                {field:'address', title: '主体地址', minWidth: 200, edit: 'text'},
                {field:'wxUserId', title: '推广者标识', width: 100},
                {field:'qrCodeUrl', title: '二维码地址', minWidth: 250},
                {field:'createTimeStr', title: '创建时间', width: 160},
                {field:'bindingTimeStr', title: '绑定时间', width: 160},
                {field:'updateTimeStr', title: '更新时间', width: 160},
                {fixed:'right', align:'center', title: '操作', width: 80, templet: function(data){
                        var td = '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="download">下载</a>';
                        return td;
                    }
                }
            ]],
            page: true,
            limit:15,
            limits:[10, 15, 20, 25, 30, 50, 100, 500, 1000, 5000, 10000],
            done: function(res, curr, count){// 渲染数据完成回调
                popularizeTargetTableThisPageNo = curr;
            }
        });
    }

    /**
     * 刷新表格
     *
     * @param pageNo 刷新第几页
     */
    function updateTable(pageNo){
        if($.util.isBlank(pageNo) || pageNo == 0){
            pageNo = 1;
        }
        if(null != popularizeTargetTable && undefined != popularizeTargetTable){
            popularizeTargetTable.reload({
                page: {
                    curr: pageNo //重新从第 1 页开始
                },
                where: {
                    wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                    keyword: $('#keyword') .val()
                }
            });
        }
    }

    /**
     * 根据标识修改推广主体
     *
     * @param id 标识
     * @param field 修改字段
     * @param value 修改值
     */
    function updatePopularizeTargetById(id, field, value) {
        $.ajax({
            url: context +'/xptXtglAuth/updatePopularizeTargetById',
            data: {
                id : id ,
                field : field ,
                value : value
            },
            type: "post",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    showOperationPop(1,'修改成功');
                }else{
                    var msg = successData.msg;
                    if($.util.isBlank(msg)){
                        showOperationPop(1,'修改失败');
                    }else{
                        showOperationPop(0,msg);
                    }
                }
                updateTable(popularizeTargetTableThisPageNo);
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
    jQuery.extend($.popularizeTargetList, {
        updateTable: updateTable,
        showOperationPop: showOperationPop
    });
})(jQuery);