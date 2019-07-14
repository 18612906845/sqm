$.parameterSettingList = $.parameterSettingList || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var parameterSettingTable = null;
    var parameterSettingTableThisPageNo = 1;

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
            table.on('toolbar(parameterSettingTable)', function(obj){
                if(obj.event === "add"){
                    layer.open({
                        title: "新建参数配置",
                        type: 2,
                        area: ['700px', '450px'],
                        fixed: false, //不固定
                        content: context + '/xpsXtglAuth/openParameterSettingNewPage'
                    });
                }
            });

            //监听工具条
            table.on('tool(parameterSettingTable)', function(obj){
                var data = obj.data;
                if(obj.event === 'del'){
                    layer.confirm('确定要删除参数：' + data.paramName + ' 吗？', {icon: 3}, function(index){
                        layer.close(index);
                        //向服务端发送删除指令
                        deleteConfigurationById(data.id);
                    });
                }
            });

            //监听单元格编辑
            table.on('edit(parameterSettingTable)', function(obj){
                var value = obj.value; //得到修改后的值
                var data = obj.data; //得到所在行所有键值
                var field = obj.field; //得到字段
                updateConfigurationById(data.id, field, value);
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

        /**
         * 查看内存参数
         */
        $(document).on("click", "#lookParamMap", function () {
            findParamMap();
            return false;
        });

    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单组选中
        $("#systemSettings").addClass("layui-nav-itemed");
        //设置子菜单选中
        $("#parameterSetting").parent("dd").addClass("layui-this");
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        parameterSettingTable = table.render({
            title: '参数配置表',
            elem: '#parameterSettingTable',
            url:context +'/xpsXtglAuth/findConfigurationByConditionPage',
            toolbar: '#toolbarDemo',
            method: 'post',
            request: {
                pageName: 'pageNo', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            where: {
                paramName: $("#parameterName").val(),
                paramGroup: $('#parameterType option:selected') .val()
            },
            cols: [[
                {field:'paramName', title: '参数名称', width: 300, edit: 'text'},
                {field:'paramValue', title: '参数数值', minWidth: 300, edit: 'text'},
                {field:'remark', title: '参数说明', minWidth: 300, edit: 'text'},
                {field:'paramGroup', align:'center', title: '参数分组', width: 100},
                {field:'updateTimeLong', title: '更新时间', width: 180, templet: function (data) {
                        return $.date.timeToStr(data.updateTimeLong, "yyyy-MM-dd HH:mm:ss");
                    }
                },
                {fixed:'right', align:'center', title: '操作', width: 80, templet: function(data){
                    var td = '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>';
                        return td;
                    }
                }
            ]],
            page: true,
            limit:15,
            limits:[10, 15, 20, 25, 30, 50, 100, 500, 1000, 5000, 10000],
            done: function(res, curr, count){// 渲染数据完成回调
                parameterSettingTableThisPageNo = curr;
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
        if(null != parameterSettingTable && undefined != parameterSettingTable){
            parameterSettingTable.reload({
                page: {
                    curr: pageNo //重新从第 1 页开始
                },
                where: {
                    paramName: $("#parameterName").val(),
                    paramGroup: $('#parameterType option:selected') .val()
                }
            });
        }
    }

    /**
     * 根据标识修改参数配置
     *
     * @param id 标识
     * @param field 修改字段
     * @param value 修改值
     */
    function updateConfigurationById(id, field, value) {
        $.ajax({
            url: context +'/xpsXtglAuth/updateConfigurationById',
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
                updateTable(parameterSettingTableThisPageNo);
            },
            error: function (errorData) {
                showOperationPop(2,'未知错误');
            }
        });
    }

    /**
     * 根据标识删除参数配置
     *
     * @param id 标识
     */
    function deleteConfigurationById(id){
        $.ajax({
            url: context +'/xpsXtglAuth/deleteConfigurationById',
            data: {
                id : id
            },
            type: "post",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    showOperationPop(1,'删除成功');
                    updateTable(parameterSettingTableThisPageNo);
                }else{
                    showOperationPop(1,'删除失败');
                }
            },
            error: function (errorData) {
                showOperationPop(2,'未知错误');
            }
        });
    }

    /**
     * 查看内存中参数配置
     *
     */
    function findParamMap(){
        $.ajax({
            url: context +'/xpsXtglAuth/findParamMap',
            data: {},
            type: "post",
            dataType: "json",
            success: function(successData){
                layer.open({
                    type: 1,
                    title: false,
                    closeBtn: false,
                    area: '1000px',
                    shade: 0.8,
                    id: 'LAY_layuipro',
                    btn: ['关闭'],
                    btnAlign: 'c',
                    moveType: 1,
                    content: '<div style="width: 1000px; line-height: 22px; background-color: #393D49; color: #fff; font-weight: 300; word-break: break-all;"><p style="padding: 10px;">' + JSON.stringify(successData) + '</p></div>',
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                });
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
    jQuery.extend($.parameterSettingList, {
        updateTable: updateTable,
        showOperationPop: showOperationPop
    });
})(jQuery);