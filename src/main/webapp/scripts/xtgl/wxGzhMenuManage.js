$.wxGzhMenuManage = $.wxGzhMenuManage || {};

(function($){

    "use strict";

    var form = null;
    var element = null;
    var layer = null;

    var diyMenuId = null;
    var firstSize = null;
    var diyMenuObj = null;

    var menuNameMaxLength = 16;//菜单名字最大字节

    $(document).ready(function() {

        //初始化layerUi控件
        layui.use(['form', 'layer', 'element'], function(){
            form = layui.form;
            form.render();
            layer = layui.layer;
            element = layui.element;

            form.verify({
                menuNameVerify : function (value, item) {
                    var bytesCount = 0;
                    for (var i = 0; i < value.length; i++){
                        var c = value.charAt(i);
                        if (/^[\u0000-\u00ff]$/.test(c)){
                            bytesCount += 1;
                        }else{
                            bytesCount += 2;
                        }
                    }
                    if(bytesCount > menuNameMaxLength){
                        return "菜单名称不可超出1-" + (menuNameMaxLength/2) + "个纯汉字或1-" + menuNameMaxLength +  "个纯字母数字";
                    }
                },
                menuKeyVerify : function (value, item) {
                    var bytesCount = 0;
                    for (var i = 0; i < value.length; i++){
                        var c = value.charAt(i);
                        if (/^[\u0000-\u00ff]$/.test(c)){
                            bytesCount += 1;
                        }else{
                            bytesCount += 2;
                        }
                    }
                    if(bytesCount > 128){
                        return "菜单KEY值不可超出1-64个纯汉字或1-128个纯字母数字";
                    }
                }
            });

            /**
             * 菜单层级选择事件
             */
            form.on('select(levelNum)', function(data){
                var levelNum = data.value;
                if(levelNum == "" || levelNum == 1){
                    menuNameMaxLength = 16;
                    form.val("diyMenuForm", {
                        "parentId": "",
                        "orderNum": ""
                    });
                    $("select[name='parentId']").attr("disabled", true);
                    $("select[name='parentId']").removeAttr("lay-verify");
                    $("#parentIdRed").hide();

                    //设置排序可选值
                    var options = $("select[name='orderNum']").find("option");
                    $.each(options, function(o, option){
                        $(option).attr("disabled", true);
                    });
                    if(diyMenuId == null){//新增
                        $("select[name='orderNum']").removeAttr("disabled");
                        $("select[name='orderNum']").attr("lay-verify", "required");
                        $("#orderNumRed").show();
                        $.each(options, function(o, option){
                            if($(option).val() == (firstSize + 1) || $(option).val() == ""){
                                $(option).removeAttr("disabled");
                            }
                        });
                    }else{
                        $("select[name='orderNum']").attr("disabled", true);
                        $("select[name='orderNum']").removeAttr("lay-verify");
                        $("#orderNumRed").hide();
                    }
                }else{
                    menuNameMaxLength = 60;
                    $("select[name='parentId']").removeAttr("disabled");
                    $("select[name='parentId']").attr("lay-verify", "required");
                    $("#parentIdRed").show();
                    //排序号禁用
                    form.val("diyMenuForm", {
                        "orderNum": ""
                    });
                    $("select[name='orderNum']").attr("disabled", true);
                    $("select[name='orderNum']").attr("lay-verify", "required");
                    $("#orderNumRed").show();
                }
                form.render('select', 'diyMenuForm');
            });

            /**
             * 响应动作类型选择事件
             */
            form.on('select(actionType)', function(data){
                var actionType = data.value;
                updateVerifyByActionType(actionType);
            });

            /**
             * 父级菜单选择事件
             */
            form.on('select(parentId)', function(data){
                var pId = data.value;
                form.val("diyMenuForm", {
                    "orderNum": ""
                });
                if(pId == ""){
                    $("select[name='orderNum']").attr("disabled", true);
                }else{
                    //查询当前父id下有几个子菜单
                    var treeObj = $.fn.zTree.getZTreeObj("treeDiyMenu");
                    var nodes = treeObj.getNodesByParam("pId", pId, null);
                    var size = nodes.length;
                    //设置可选排序号
                    var options = $("select[name='orderNum']").find("option");
                    $.each(options, function(o, option){
                        $(option).attr("disabled", true);
                    });
                    $.each(options, function(o, option){
                        if($(option).val() == (size + 1) || $(option).val() == ""){
                            $(option).removeAttr("disabled");
                        }
                    });
                    $("select[name='orderNum']").removeAttr("disabled");
                    $("select[name='orderNum']").attr("lay-verify", "required");
                    $("#orderNumRed").show();
                }
                form.render('select', 'diyMenuForm');
            });

            /**
             * 新增，表单提交事件
             */
            form.on('submit(diyMenuFormSaveSubmit)', function(data){
                $.ajax({
                    url:context +'/xwgmtXtglAuth/saveWebManageWxMenu',
                    data:JSON.stringify(data.field),
                    type:"post",
                    contentType: "application/json; charset=utf-8",
                    dataType:"json",
                    success:function(successData){
                        if(successData.status){
                            disabledFormAll();
                            layer.msg('新建成功', {icon: 6, time: 2000}, function(){
                                window.location.reload();
                            });
                        }else{
                            layer.msg('新建失败', {icon: 5, time: 2000});
                        }
                    },
                    error:function (errorData) {
                        layer.msg('未知错误，新建失败', {icon: 5, time: 2000});
                    }
                });
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });

            /**
             * 修改，表单提交事件
             */
            form.on('submit(diyMenuFormUpdateSubmit)', function(data){
                data.field.id = diyMenuId;
                $.ajax({
                    url:context +'/xwgmtXtglAuth/updateWebManageWxMenuById',
                    data:JSON.stringify(data.field),
                    type:"post",
                    contentType: "application/json; charset=utf-8",
                    dataType:"json",
                    success:function(successData){
                        if(successData.status){
                            disabledFormAll();
                            layer.msg('修改成功', {icon: 6, time: 2000}, function(){
                                window.location.reload();
                            });
                        }else{
                            layer.msg('修改失败', {icon: 5, time: 2000});
                        }
                    },
                    error:function (errorData) {
                        layer.msg('未知错误，修改失败', {icon: 5, time: 2000});
                    }
                });
                return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
            });
        });

        /**
         * 新增
         */
        $(document).on("click", "#newBut", function () {
            //显示必填项
            $(".requiredSpanRed").show();
            //菜单id置空
            diyMenuId = null;
            //显示表单
            $("#diyMenuForm").show();
            $("#update").hide();
            $("#save").show();
            //清空表单
            clearWeChatDiyMenu();
            //取消树选择状态
            var treeObj = $.fn.zTree.getZTreeObj("treeDiyMenu");
            treeObj.cancelSelectedNode();
            //启用表单填写
            enabledFormAll();
            //判断是否可以新建一级菜单
            if(firstSize == 0){//只能创建一级
                //设置菜单级别只可选择一级
                var options = $("select[name='levelNum']").find("option");
                $.each(options, function(o, option){
                    $(option).removeAttr("disabled");
                });
                $.each(options, function(o, option){
                    if($(option).val() == 2){
                        $(option).attr("disabled", true);
                    }
                });
            }else if(firstSize > 0 && firstSize < 3){
                //设置菜单级别一级、二级都可选择
                var options = $("select[name='levelNum']").find("option");
                $.each(options, function(o, option){
                    $(option).removeAttr("disabled");
                });
            }else{
                //设置菜单级别只可选择二级
                var options = $("select[name='levelNum']").find("option");
                $.each(options, function(o, option){
                    $(option).removeAttr("disabled");
                });
                $.each(options, function(o, option){
                    if($(option).val() == 1){
                        $(option).attr("disabled", true);
                    }
                });
            }
            form.render(null, "diyMenuForm");
        });

        /**
         * 修改按钮
         */
        $(document).on("click", "#updateBut", function () {
            //显示必填项
            $(".requiredSpanRed").show();
            //设置菜单层级不可改
            $("select[name='levelNum']").attr("disabled", true);
            //切换保存按钮
            $("#save").hide();
            $("#update").show();
            //启用表单填写
            enabledFormAll();
            //根据当前修改的菜单级别判断是否需要填写父级菜单
            var treeObj = $.fn.zTree.getZTreeObj("treeDiyMenu");
            var nodes = treeObj.getSelectedNodes();
            if(nodes.length < 0){
                return ;
            }
            var diyMenu = nodes[0].diy.diyMenu;
            if(diyMenu.levelNum == 1){
                $("select[name='parentId']").attr("disabled", true);
                $("select[name='parentId']").removeAttr("lay-verify");
                $("#parentIdRed").hide();
            }else{
                $("select[name='parentId']").removeAttr("disabled");
                $("select[name='parentId']").attr("lay-verify", "required");
                $("#parentIdRed").show();
            }
            updateVerifyByActionType(diyMenuObj.actionType);
            form.render(null, "diyMenuForm");
        });

        /**
         * 删除按钮
         */
        $(document).on("click", "#deleteBut", function () {
            layer.confirm('删除后无法恢复，确定删除吗？', {icon: 3, title:'删除提示'}, function(index){
                $.ajax({
                    url:context +'/xwgmtXtglAuth/deleteWebManageWxMenuById',
                    data:{"id" : diyMenuId},
                    type:"post",
                    dataType:"json",
                    success:function(successData){
                        if(successData.status){
                            layer.msg('删除成功', {icon: 6, time: 2000}, function(){
                                window.location.reload();
                            });
                        }else{
                            layer.msg(successData.msg + '删除失败', {icon: 5, time: 2000});
                        }
                    },
                    error:function (errorData) {
                        layer.msg('未知错误，删除失败', {icon: 5, time: 2000});
                    }
                });
                layer.close(index);
            });
        });

        /**
         * 重置按钮
         */
        $(document).on("click", "#resetBut", function () {
            $(".spanRed").hide();
            $("#parentIdRed").hide();
            $("#orderNumRed").hide();
        });

        /**
         * 取消
         */
        $(document).on("click", "#cancelBut", function () {
            setTimeout(function () {
                window.location.reload();
            }, 100);
            return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
        });

        /**
         * 同步菜单按钮
         */
        $(document).on("click", "#syncWeChatServerBut", function () {
            layer.confirm('同步后即可生效，确定同步吗？', {icon: 3, title:'同步提示'}, function(index){
                $.ajax({
                    url:context +'/xwgmtXtglAuth/setWebManageWxMenu',
                    data:{},
                    type:"post",
                    contentType: "application/json; charset=utf-8",
                    dataType:"json",
                    success:function(successData){
                        if(successData.status){
                            if(successData.errcode == "0"){
                                layer.msg('同步成功', {icon: 6, time: 2000});
                            }else{
                                layer.alert('同步失败，错误码：' + successData.errcode + "；错误信息：" + successData.errmsg, {icon: 5});
                            }
                        }else{
                            layer.msg('同步失败', {icon: 5, time: 2000});
                        }
                    },
                    error:function (errorData) {
                        layer.msg('同步失败', {icon: 5, time: 2000});
                    }
                });
                layer.close(index);
            });
        });

        /**
         * 删除微信服务器菜单按钮
         */
        $(document).on("click", "#deleteWeChatServerBut", function () {
            layer.confirm('删除后无法恢复，确定删除吗？', {icon: 3, title:'删除提示'}, function(index){
                $.ajax({
                    url:context +'/xwgmtXtglAuth/deleteWebManageWxMenu',
                    data:{},
                    type:"post",
                    contentType: "application/json; charset=utf-8",
                    dataType:"json",
                    success:function(successData){
                        if(successData.status){
                            if(successData.errcode == "0"){
                                layer.msg('删除成功', {icon: 6, time: 2000});
                            }else{
                                layer.alert('删除失败，错误码：' + successData.errcode + "；错误信息：" + successData.errmsg, {icon: 5});
                            }
                        }else{
                            layer.msg('删除失败', {icon: 5, time: 2000});
                        }
                    },
                    error:function (errorData) {
                        layer.msg('删除失败', {icon: 5, time: 2000});
                    }
                });
                layer.close(index);
            });
        });

        initPage();
        initPageData();

    });

    /**
     * 初始化页面
     */
    function initPage() {
        //设置菜单选中
        $("#weChatParamManage").addClass("layui-nav-itemed");
        $("#wxGzhMenuManage").parent("dd").addClass("layui-this");
    }


    var setting = {
        edit: {
            enable: true,
            showRemoveBtn: false,
            showRenameBtn: false
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeDrag: beforeDrag,
            beforeDrop: beforeDrop,

            onClick: function (event, treeId, treeNode) {
                $("#updateBut").show();
                var treeObj = $.fn.zTree.getZTreeObj("treeDiyMenu");
                var subNodes = treeObj.getNodesByParam("pId", treeNode.id, null);
                if(subNodes.length > 0){
                    $("#deleteBut").hide();
                }else{
                    $("#deleteBut").show();
                }
                var diyMenu = treeNode.diy.diyMenu;
                setWeChatDiyMenu(diyMenu);
                //禁用form表单字段,隐藏保存、重置按钮
                disabledFormAll();
                //查看模式取消必填项标识
                $("#diyMenuForm .layui-badge-dot").hide();
            }
        }
    };

    function beforeDrag(treeId, treeNodes) {
        for (var i=0,l=treeNodes.length; i<l; i++) {
            if (treeNodes[i].drag === false) {
                return false;
            }
        }
        return true;
    }
    function beforeDrop(treeId, treeNodes, targetNode, moveType) {
        return targetNode ? targetNode.drop !== false : true;
    }

    /**
     * 初始化页面数据
     */
    function initPageData() {
        $.ajax({
            url:context +'/xwgmtXtglAuth/findAllWebManageWxMenu',
            data:{},
            type:"post",
            contentType: "application/json; charset=utf-8",
            dataType:"json",
            success:function(successData){
                if(successData.status){
                    $.fn.zTree.init($("#treeDiyMenu"), setting, successData.zNodes);
                    initParentMenu();
                    firstSize = successData.firstSize;
                    if(firstSize > 0){
                        $("#syncWeChatServerBut").show();
                    }else{
                        $("#syncWeChatServerBut").hide();
                    }
                }else{
                    layer.msg('自定义菜单查询失败', {icon: 5, time: 2000});
                }
            },
            error:function (errorData) {
                layer.msg('自定义菜单查询失败', {icon: 5, time: 2000});
            }
        });
    }

    /**
     * 初始化父级菜单可选项
     */
    function initParentMenu(){
        var treeObj = $.fn.zTree.getZTreeObj("treeDiyMenu");
        var nodes = treeObj.getNodesByParam("level", 0, null);
        var options = "<option value=''></option>";
        //设置父级可选项
        $.each(nodes, function (i, val) {
            var subNodes = treeObj.getNodesByParam("pId", val.id, null);
            if(subNodes.length < 5){
                options += "<option value='" + val.id + "'>" + val.name + "</option>"
            }
        });
        $("select[name='parentId']").html(options);
    }

    /**
     * 设置自定义菜单详情
     *
     * @param data 数据
     */
    function setWeChatDiyMenu(data) {
        clearWeChatDiyMenu();
        if(data == null || data == undefined){
            return ;
        }
        $.each(data, function (i, val) {
            if(val == null){
                data[i] = "";
            }
        });
        diyMenuId = data.id;
        diyMenuObj = data;
        $("#diyMenuForm").show();
        form.val("diyMenuForm", data);
    }

    /**
     * 清除页面数据
     *
     * @param data 数据
     */
    function clearWeChatDiyMenu() {
        $("#diyMenuForm")[0].reset()
    }

    /**
     * 所有字段不可更改
     */
    function disabledFormAll(){
        $("select[name='levelNum']").attr("disabled", true);
        $("input[name='menuName']").attr("readonly", true);
        $("select[name='actionType']").attr("disabled", true);
        $("input[name='menuKey']").attr("readonly", true);
        $("input[name='url']").attr("readonly", true);
        $("input[name='mediaId']").attr("readonly", true);
        $("input[name='appId']").attr("readonly", true);
        $("input[name='pagePath']").attr("readonly", true);
        $("select[name='parentId']").attr("disabled", true);
        $("select[name='orderNum']").attr("disabled", true);
        $("#operationDiv").hide();
    }

    /**
     * 所有字段可更改
     */
    function enabledFormAll(){
        $("select[name='levelNum']").removeAttr("disabled");
        $("input[name='menuName']").removeAttr("readonly");
        $("select[name='actionType']").removeAttr("disabled");
        $("input[name='menuKey']").removeAttr("readonly");
        $("input[name='url']").removeAttr("readonly");
        $("input[name='mediaId']").removeAttr("readonly");
        $("input[name='appId']").removeAttr("readonly");
        $("input[name='pagePath']").removeAttr("readonly");
        $("#operationDiv").show();
    }

    /**
     * 根据响应动作类型变化更新验证
     *
     * @param actionType 响应动作类型值
     */
    function updateVerifyByActionType(actionType){
        //清空部分验证
        $("input[name='menuKey']").removeAttr("lay-verify");
        $("input[name='url']").removeAttr("lay-verify");
        $("input[name='mediaId']").removeAttr("lay-verify");
        $("input[name='appId']").removeAttr("lay-verify");
        $("input[name='pagePath']").removeAttr("lay-verify");
        $(".spanRed").hide();
        //根据选择的值判断谁必填
        switch (actionType){
            case "click" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "view" :
                $("input[name='url']").attr("lay-verify", "required");
                $("#urlRed").show();
                break;
            case "scancode_push" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "scancode_waitmsg" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "pic_sysphoto" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "pic_photo_or_album" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "pic_weixin" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "location_select" :
                $("input[name='menuKey']").attr("lay-verify", "required|menuKeyVerify");
                $("#menuKeyRed").show();
                break;
            case "media_id" :
                $("input[name='mediaId']").attr("lay-verify", "required");
                $("#mediaIdRed").show();
                break;
            case "view_limited" :
                $("input[name='mediaId']").attr("lay-verify", "required");
                $("#mediaIdRed").show();
                break;
            case "miniprogram" :
                $("input[name='url']").attr("lay-verify", "required");
                $("input[name='appId']").attr("lay-verify", "required");
                $("input[name='pagePath']").attr("lay-verify", "required");
                $("#urlRed").show();
                $("#appIdRed").show();
                $("#pagePathRed").show();
                break;
        }
        form.render('select', 'diyMenuForm');
    }

    /**
     * 暴露本js方法，让其它js可调用
     */
    jQuery.extend($.wxGzhMenuManage, {

    });
})(jQuery);