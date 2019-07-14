$.wxUserList = $.wxUserList || {};

(function($){

    "use strict";

    var form = null;
    var table = null;
    var element = null;
    var layer = null;

    var wxUserListTable = null;
    var wxUserListTableThisPageNo = 1;

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
                findAwaitPaySumNum();
                return false;
            });

            //监听单元格编辑
            table.on('edit(wxUserListTable)', function(obj){
                var value = obj.value; //得到修改后的值
                var data = obj.data; //得到所在行所有键值
                var field = obj.field; //得到字段
                updateWxUserById(data.id, field, value);
            });

            initTable();
        });

        /**
         * 重置
         */
        $(document).on("click", "#reset", function () {
            setTimeout(function () {
                updateTable();
            },1000);
        });

        initPage();
        findAwaitPaySumNum();
    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单选中
        $("#businessData").addClass("layui-nav-itemed");
        $("#wxUserList").parent("dd").addClass("layui-this");
    }


    /**
     * 查询总计待支付金额
     */
    function findAwaitPaySumNum() {
        $.ajax({
            url:context +'/xwuXtglAuth/findAwaitPaySumNum',
            data:{},
            type:"post",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    var awaitPaySum = successData.awaitPaySum;
                    $("#awaitPaySum").text(awaitPaySum);
                }else{
                    $("#awaitPaySum").text("0.00");
                }
            },
            error:function (errorData) {
                $("#awaitPaySum").text("0.00");
            }
        });
    }

    /**
     * 初始化表格数据
     */
    function initTable() {
        wxUserListTable = table.render({
            elem: '#wxUserListTable',
            url:context +'/xwuXtglAuth/findWxUserListByConditionPage',
            method: 'post',
            request: {
                pageName: 'pageNo', //页码的参数名称，默认：page
                limitName: 'pageSize' //每页数据量的参数名，默认：limit
            },
            where: {
                id : $("#id").val() == "" ? 0 : $("#id").val(),
                nickName : $("#nickName").val(),
                orderField : $('#orderField option:selected').val()
            },
            cols: [[
                {field:'id', title: 'ID', width: 60},
                {field:'headImgUrl', title: '头像', width: 70,templet: function(d){
                        if(d.headImgUrl == null || d.headImgUrl == undefined || d.headImgUrl == ''){
                            return "";
                        }else{
                            return '<img src="' + d.headImgUrl +'" class="layui-nav-img">';
                        }
                    }},
                {field:'nickName', title: '昵称', width: 120},
                {field:'unionId', title: 'Union标识', width: 200},
                {field:'openIdGzh', title: '公众号标识', width: 200},
                {field:'openIdXcx', title: '小程序标识', width: 200},
                {field:'parentWxUserId', title: '推荐人ID', width: 90},
                {field:'remainingMoney', title: '可提余额', width: 90},
                {field:'withdrawCashSum', title: '累计提现', width: 90},
                {field:'returnMoneyShareWq', title: '无券返现比例', width: 110, edit: 'text'},
                {field:'returnMoneyShareYq', title: '有券返现比例', width: 110, edit: 'text'},
                {field:'returnMoneyShareTc', title: '提成比例', width: 90, edit: 'text'},
                {field:'createTimeStr', title: '创建时间', width: 160},
                {field:'updateTimeStr', title: '更新时间', width: 160},
                {field:'sessionKeyXcx', title: '小程序密匙', width: 200},
                {field:'openIdPc', title: '网站标识', width: 200},
                {field:'sexStr', title: '性别', width: 60},
                {field:'city', title: '城市', width: 80},
                {field:'province', title: '省份', width: 80},
                {field:'country', title: '国家', width: 80}
            ]],
            page: true,
            limit:15,
            limits:[10, 15, 20, 25, 30, 50, 100, 500, 1000, 5000, 10000],
            done: function(res, curr, count){
                wxUserListTableThisPageNo = curr;
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
        if(null != wxUserListTable && undefined != wxUserListTable){
            wxUserListTable.reload({
                page: {
                    curr: pageNo //重新从第 1 页开始
                },
                where: {
                    id : $("#id").val() == "" ? 0 : $("#id").val(),
                    nickName : $("#nickName").val(),
                    orderField : $('#orderField option:selected').val()
                }
            });
        }
    }

    /**
     * 根据标识修改用户信息
     *
     * @param id 标识
     * @param field 修改字段
     * @param value 修改值
     */
    function updateWxUserById(id, field, value) {
        $.ajax({
            url: context +'/xwuXtglAuth/updateWxUserById',
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
                updateTable(wxUserListTableThisPageNo);
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
    jQuery.extend($.wxUserList, {

    });
})(jQuery);