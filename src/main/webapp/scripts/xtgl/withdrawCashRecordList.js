$.withdrawCashRecordList = $.withdrawCashRecordList || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var withdrawCashRecordTable = null;
    var withdrawCashRecordTableThisPageNo = 1;

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
                findWithdrawCashSum();
                return false;
            });

            //监听工具条
            table.on('tool(withdrawCashRecordTable)', function(obj){
                var data = obj.data;
                if(obj.event === 'retry'){
                    retryPay(data.id);
                }
            });

            initTable(); //初始化Table
        });

        initPage(); //初始化页面
        findWithdrawCashSum();

        /**
         * 重置
         */
        $(document).on("click", "#reset", function () {
            setTimeout(function () {
                updateTable();
                findWithdrawCashSum();
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
        $("#withdrawCashRecord").parent("dd").addClass("layui-this");
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        withdrawCashRecordTable = table.render({
            elem: '#withdrawCashRecordTable',
            url:context +'/xwcrXtglAuth/findWithdrawCashRecordListByConditionPage',
            method: 'post',
            request: {
                pageName: 'pageNo', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            where: {
                wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                status: $('#status option:selected').val()
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
                {field:'applyTimeStr', title: '申请时间', width: 160},
                {field:'applyMoney', title: '申请金额', width: 100},
                {field:'payStatusStr', title: '支付状态', width: 100},
                {field:'payTimeStr', title: '支付时间', width: 160},
                {field:'partnerTradeNo', title: '商户单号', width: 280},
                {field:'paymentNo', title: '付款单号', width: 280},
                {field:'errCode', title: '错误代码', width: 280},
                {field:'errCodeDes', title: '错误描述', minWidth: 280},
                {fixed:'right', align:'center', title: '操作', width: 80, templet: function(data){
                        var td = "";
                        if(data.payStatus == 3){
                            td = '<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="retry">重试</a>';
                        }
                        return td;
                    }
                }
            ]],
            page: true,
            limit:15,
            limits:[10, 15, 20, 25, 30, 50, 100, 500, 1000, 5000, 10000],
            done: function(res, curr, count){
                withdrawCashRecordTableThisPageNo = curr;
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
        if(null != withdrawCashRecordTable && undefined != withdrawCashRecordTable){
            withdrawCashRecordTable.reload({
                page: {
                    curr: pageNo //重新从第 1 页开始
                },
                where: {
                    wxUserId: $("#wxUserId").val() == "" ? 0 : $("#wxUserId").val(),
                    status: $('#status option:selected').val()
                }
            });
        }
    }

    /**
     * 查询累计支付提现申请金额
     */
    function findWithdrawCashSum() {
        $.ajax({
            url: context +'/xwcrXtglAuth/findWithdrawCashSum',
            data: {},
            type: "get",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    $("#withdrawCashSum").text(successData.withdrawCashSum);
                }else{
                    $("#withdrawCashSum").text(0.00);
                }
                updateTable(withdrawCashRecordTableThisPageNo);
            },
            error: function (errorData) {
                $("#withdrawCashSum").text(0.00);
            }
        });
    }

    /**
     * 重新支付
     *
     * @param id 提现申请标识
     */
    function retryPay(id) {
        $.ajax({
            url: context +'/xwcrXtglAuth/retryPay',
            data: {
                id : id
            },
            type: "get",
            dataType: "json",
            success: function(successData){
                if(successData.status){
                    showOperationPop(1,'执行成功');
                }else{
                    var msg = successData.msg;
                    if($.util.isBlank(msg)){
                        showOperationPop(1,'执行失败');
                    }else{
                        showOperationPop(0,msg);
                    }
                }
                updateTable(withdrawCashRecordTableThisPageNo);
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
    jQuery.extend($.withdrawCashRecordList, {

    });
})(jQuery);