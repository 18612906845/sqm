$.orderRecordList = $.orderRecordList || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var orderRecordTable = null;

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
        $("#orderRecordList").parent("dd").addClass("layui-this");
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        orderRecordTable = table.render({
            elem: '#orderRecordTable',
            url:context +'/xorXtglAuth/findOrderRecordListByConditionPage',
            method: 'post',
            request: {
                pageName: 'pageNo', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            where: {
                wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                orderId: $("#orderId").val(),
                platform: $('#platform option:selected').val(),
                status: $('#status option:selected').val(),
                orderField: $('#orderField option:selected').val()
            },
            cols: [[
                {field:'wxUserId', title: '用户ID', width: 80},
                {field:'headImgUrl', title: '头像', width: 70,templet: function(d){
                        if(d.headImgUrl == null || d.headImgUrl == undefined || d.headImgUrl == ''){
                            return "";
                        }else{
                            return '<img src="' + d.headImgUrl +'" class="layui-nav-img">';
                        }
                    }},
                {field:'nickName', title: '昵称', width: 150},
                {field:'platformStr', title: '订单平台', width: 90},
                {field:'orderId', title: '订单号', width: 180},
                {field:'statusStr', title: '订单状态', width: 90},
                {field:'orderTimeStr', title: '下单时间', width: 160},
                {field:'finishTimeStr', title: '完成时间', width: 160},
                {field:'commission', title: '联盟佣金', width: 90},
                {field:'returnScale', title: '返现比例', width: 90},
                {field:'returnMoney', title: '返利金额', width: 90},
                {field:'parentWxUserId', title: '推荐人标识', width: 100},
                {field:'createTimeStr', title: '创建时间', width: 160},
                {field:'updateTimeStr', title: '更新时间', width: 160}
            ]],
            page: true,
            limit:15,
            limits:[10, 15, 20, 25, 30, 50, 100, 500, 1000, 5000, 10000],
            done: function(res, curr, count){

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
        if(null != orderRecordTable && undefined != orderRecordTable){
            orderRecordTable.reload({
                page: {
                    curr: pageNo //重新从第 1 页开始
                },
                where: {
                    wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                    orderId: $("#orderId").val(),
                    platform: $('#platform option:selected').val(),
                    status: $('#status option:selected').val(),
                    orderField: $('#orderField option:selected').val()
                }
            });
        }
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.orderRecordList, {

    });
})(jQuery);