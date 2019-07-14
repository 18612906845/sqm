//定义date类
$.util = $.util || {};
(function($){
    "use strict";

    jQuery.extend($.util, {
        isBlank:function(val){
            /**
             * 判断某字符串是否为空或未定义或长度为0或由空白符(whitespace) 构成
             */
            if(val!=undefined && val!=null&&!($.trim(val)==="")){
                return false ;
            }else{
                return true ;
            }
        },
        isEmpty:function(val){
            /**
             * 为空的标准是 val==null 或val==undefined 或 str.length()==0
             */
            if(val!=undefined && val!=null&&val.length>0){
                return false ;
            }else{
                return true ;
            }
        },
        exist:function(val){
            /**
             * 判断对象是否存在
             */
            if(val!=undefined && val!=null){
                return true ;
            }else{
                return false ;
            }
        }
    });

})(jQuery);

//定义date类
$.date = $.date || {};
(function($){
    "use strict";

    jQuery.extend($.date, {
        dateToStr : function(date, fmt){
            var o = {
                "M+" : date.getMonth()+1, //月份
                "d+" : date.getDate(), //日
                "h+" : date.getHours()%12 == 0 ? 12 : date.getHours()%12, //小时
                "H+" : date.getHours(), //小时
                "m+" : date.getMinutes(), //分
                "s+" : date.getSeconds(), //秒
                "q+" : Math.floor((date.getMonth()+3)/3), //季度
                "S" : date.getMilliseconds() //毫秒
            };
            var week = {
                "0" : "/u65e5",
                "1" : "/u4e00",
                "2" : "/u4e8c",
                "3" : "/u4e09",
                "4" : "/u56db",
                "5" : "/u4e94",
                "6" : "/u516d"
            };
            if(/(y+)/.test(fmt)){
                fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));
            }
            if(/(E+)/.test(fmt)){
                fmt=fmt.replace(RegExp.$1, ((RegExp.$1.length>1) ? (RegExp.$1.length>2 ? "/u661f/u671f" : "/u5468") : "")+week[date.getDay()+""]);
            }
            for(var k in o){
                if(new RegExp("("+ k +")").test(fmt)){
                    fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
                }
            }

            return fmt;
        },

        timeToDate:function(time){
            return new Date(time) ;
        },

        strToTime:function(str, fmt){
            if($.util.isBlank(fmt)){
                fmt = "yyyy-MM-dd HH:mm:ss" ;
            }

            var date = $.date.strToDate(str, fmt) ;

            return date.getTime() ;
        },

        timeToStr:function(time, fmt){
            if(!$.util.exist(fmt)){
                fmt = "yyyy-MM-dd HH:mm:ss" ;
            }

            return $.date.dateToStr($.date.timeToDate(time), fmt) ;
        },

        strToDate : function(str, fmt){

            var o = {
                "Y+" : "Y+", //年份
                "y+" : "y+", //年份
                "M+" : "M+", //月份
                "d+" : "d+", //日
                "h+" : "h+", //小时
                "H+" : "H+", //小时
                "m+" : "m+", //分
                "s+" : "s+", //秒
                "q+" : "q+", //季度
                "S" : "S" //毫秒
            };

            var year = "" ;
            var month = "" ;
            var day = "" ;
            var hour = "" ;
            var minute = "" ;
            var second = "" ;



            for(var k in o){
                var reg = new RegExp("("+ k +")") ;
                if(reg.test(fmt)){
                    var re = RegExp.$1 ;
                    var start = fmt.indexOf(re) ;
                    var length = re.length ;
                    var result = str.substr(start, length) ;
                    if(k=="y+" || k=="Y+"){
                        year = result ;
                    }

                    if(k=="M+"){
                        month = result ;
                    }

                    if(k=="d+"){
                        day = result ;
                    }

                    if(k=="h+" || k=="H+"){
                        hour = result ;
                    }

                    if(k=="m+"){
                        minute = result ;
                    }

                    if(k=="s+"){
                        second = result ;
                    }

                }
            }

            if(month.length==0){
                month = "01" ;
            }else if(month.length==1){
                month = "0" + month ;
            }

            if(day.length==0){
                day = "01" ;
            }else if(day.length==1){
                day = "0" + day ;
            }

            if(hour.length==0){
                hour = "00" ;
            }else if(hour.length==1){
                hour = "0" + hour ;
            }

            if(minute.length==0){
                minute = "00" ;
            }else if(minute.length==1){
                minute = "0" + minute ;
            }

            if(second.length==0){
                second = "00" ;
            }else if(second.length==1){
                second = "0" + second ;
            }
            return new Date(year+"/"+month+"/"+day+" "+hour+":"+minute+":"+second) ;

        },

        strFmt:function(str, fmtFrom, fmtTo){

            if($.util.isBlank(fmtTo)){
                fmtTo = "yyyy-MM-dd HH:mm:ss" ;
            }

            if($.util.isBlank(str)){
                return "" ;
            }
            var date = $.date.strToDate(str, fmtFrom) ;
            return $.date.dateToStr(date, fmtTo) ;
        },

        endRange:function(str, fmt){
            if($.util.isBlank(fmt)){
                fmt = "yyyy-MM-dd HH:mm:ss" ;
            }
            if($.util.isBlank(str)){
                return "" ;
            }

            var range = getfmtType(fmt) ;
            var date = $.date.strToDate(str, fmt) ;

            if(range==="s"){

            }else if(range==="m"){
                date.setMinutes(date.getMinutes()+1) ;
            }else if(range==="H"){
                date.setHours(date.getHours() + 1) ;
            }else if(range==="d"){
                date.setDate(date.getDate() + 1) ;
            }else if(range==="M"){
                date.setMonth(date.getMonth() + 1) ;
            }else if(range==="y"){
                date.setFullYear(date.getFullYear() + 1) ;
            }

            return $.date.dateToStr(date, "yyyy-MM-dd HH:mm:ss") ;
        },

        endRangeByTime:function(time, fmt){
            if($.util.isBlank(fmt)){
                fmt = "yyyy-MM-dd HH:mm:ss" ;
            }
            if($.util.isBlank(time)){
                return null ;
            }

            var range = getfmtType(fmt) ;
            var date = new Date(time) ;

            if(range==="s"){

            }else if(range==="m"){
                date.setMinutes(date.getMinutes()+1) ;
            }else if(range==="H"){
                date.setHours(date.getHours() + 1) ;
            }else if(range==="d"){
                date.setDate(date.getDate() + 1) ;
            }else if(range==="M"){
                date.setMonth(date.getMonth() + 1) ;
            }else if(range==="y"){
                date.setFullYear(date.getFullYear() + 1) ;
            }

            return date.getTime() ;
        },

        compareStr:function(str1, str2, fmt){
            var d1 = $.date.strToDate(str1, fmt) ;
            var d2 = $.date.strToDate(str2, fmt) ;

            var t1 = d1.getTime() ;
            var t2 = d2.getTime() ;
            if(t1>t2){
                return 2 ;
            }else if(t1==t2){
                return 1 ;
            }else{
                return -1 ;
            }
        },
        compareDate:function(date1, date2, fmt){
            var t1 = date1.getTime() ;
            var t2 = date2.getTime() ;
            if(t1>t2){
                return 2 ;
            }else if(t1==t2){
                return 1 ;
            }else{
                return -1 ;
            }
        }

    });


    function getfmtType(fmt){
        var o = {
            "Y+" : "Y+", //年份
            "y+" : "y+", //年份
            "M+" : "M+", //月份
            "d+" : "d+", //日
            "h+" : "h+", //小时
            "H+" : "H+", //小时
            "m+" : "m+", //分
            "s+" : "s+", //秒
            "q+" : "q+", //季度
            "S" : "S" //毫秒
        };

        var year = false ;
        var month = false ;
        var day = false ;
        var hour = false ;
        var minute = false ;
        var second = false ;

        for(var k in o){
            var reg = new RegExp("("+ k +")") ;
            if(reg.test(fmt)){

                if(k=="y+" || k=="Y+"){
                    year = true ;
                }

                if(k=="M+"){
                    month = true ;
                }

                if(k=="d+"){
                    day = true ;
                }

                if(k=="h+" || k=="H+"){
                    hour = true ;
                }

                if(k=="m+"){
                    minute = true ;
                }

                if(k=="s+"){
                    second = true ;
                }

            }
        }

        if(second){
            return "s" ;
        }else if(minute){
            return "m" ;
        }else if(hour){
            return "H" ;
        }else if(day){
            return "d" ;
        }else if(month){
            return "M" ;
        }else if(year){
            return "y" ;
        }
    }
})(jQuery);