$(function () {
    var table;
    loadshareList();

    $(document).on("click","#add-share-btn",function () {
        if (!$.fn.DataTable.isDataTable('#sharefiletable'))//不必每次点击都加载，如果已经加载了就不再请求后端加载了
        {
            $('#sharefiletable').DataTable({
                "processing": true,//显示等待信息
                "searching": true,//是否支持页内搜索
                "serverSide": false,//是否服务器端分页
                "paging":true,//是否分页
                "lengthChange":false,//选择下拉框调整每页显示数量
                'iDisplayLength': 7, //每页初始显示7条记录
                'pagingType':'full',
                "ajax": {
                    "url": "/file/filelist/default"
                },
                "columns":[{
                    "data":null,
                    "target":0,
                    "title":"<input type='checkbox' id='share-check-all'/>",
                    "render":function (data, type, row, meta) {
                        return '<input type="checkbox" class="share-check" ufid='+row.ufid+'>'
                    }
                },{
                    "data":"fileName",
                    "target":"fileName",
                    "title":"文件名"
                },{
                    "data":"saveDate",
                    "target":"saveDate",
                    "title":"保存日期",
                    "render":function  formateDate(time) {
                        var date = new Date(time);
                        return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+'-'+date.getHours()+':'+date.getMinutes()
                    }
                },{
                    "data":"size",
                    "target":"size",
                    "title":"大小",
                    "render":function (data) {
                        var result=0;
                        if(data>1024){
                            result = data/1024;
                            return result.toFixed(2) + "GB"
                        }else if (data<1024) {
                            result = data*1024;
                            return result.toFixed(2) + "KB"
                        }else {
                            return data.toFixed(2) + "MB"
                        }
                    }
                }],
                language:{
                    processing:'加载中',
                    info:'显示第_START_到第_END_条记录，共_TOTAL_条',
                    paginate:{
                        "sFirst" : " 首页 ",
                        'sPrevious':'<button type="button" class="btn btn-default btn-sm my-button"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>上一页</button>',
                        'sNext':'<button type="button" class="btn btn-default btn-sm my-button"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>下一页</button>',
                        "sLast" : " 末页 "},
                    infoEmpty:'没有数据'

                }
            })
        }

        $('.share-check').prop('checked',false);
        $("#share-modal-btn").click();
    });


    $("#share-confirm-btn").click(function () {
        var shareUfid = $('.share-check:checked').attr('ufid');
        var form = $("<form action='/share/addShare' type='post'><label for='ranks'>等级<select name='ranks'><option>1</option><option>2</option><option>3</option><option>4</option><option>5</option>" +
            "</select><input name='shareUfid' value='"+shareUfid+"' style='display: none'/><input name='type' value='0' style='display: none'/></form>")
        var d = dialog({
            title: '选择下载等级',
            content: form,
            okValue: '确定',
            ok: function () {
                form.ajaxSubmit();
                alert("分享完成");
                return true;
            },
            cancelValue: '取消',
            cancel: function () {}
        });
        $('#closesharemodal').click();
        d.show();
    })


    function  loadshareList(){
        var table = $('#sharetable').DataTable({
            "processing": true,//显示等待信息
            "searching": false,//是否支持页内搜索
            "serverSide": false,//是否服务器端分页
            "paging":true,//是否分页
            "lengthChange":false,//选择下拉框调整每页显示数量
            'iDisplayLength': 9, //每页初始显示7条记录
            'pagingType':'full',
            "destroy":true,
            "ajax": {
                "url": "/share/queryShareMessageList",
                "type":'post'
            },
            "columns":[{
                "data":null,
                "target":0,
                "render":function (data, type, row, meta) {
                    return '<input type="checkbox" class="share-lib-check" sid='+row.sid+'>'
                }
            },{
                "data":"fileName",
                "target":"fileName",
                "title":"文件名",
                "render":function (data, type, row, meta) {
                    return '<a class="common-share-item" href="#" sid='+row.sid+'>'+row.fileName+'</a>'
                }
            },{
                "data":"shareUser",
                "target":"shareUser",
                "title":"分享人"
            },{
                "data":"ranks",
                "target":"ranks",
                "title":"浏览等级"
            },{
                "data":"shareDate",
                "target":"shareDate",
                "title":"分享时间",
                "render":function  formateDate(time) {
                    var date = new Date(time);
                    return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()
                    //return date;
                }
            },{
                "data":"likes",
                "target":"likes",
                "title":"点赞",
                "render":function (data, type, row, meta) {
                    var likeimg;
                    likeimg = $("<img width='20' height='20'><img/>");
                    if (row.status===0){
                        likeimg = "<img width='20' height='20' class='zan disable' sid="+row.sid+" src='/img/like.png'><img/>"
                    } else {
                        likeimg = "<img width='20' height='20' class='zan active' sid="+row.sid+" src='/img/liked.png'><img/>"
                    }
                    return "<div><div >"+likeimg+"<p id='like-"+row.sid+"'>"+row.likes+"</p></div></div>";
                }
            }],
            language:{
                processing:'加载中',
                info:'显示第_START_到第_END_条记录，共_TOTAL_条',
                paginate:{
                    "sFirst" : " 首页 ",
                    'sPrevious':'<button type="button" class="btn btn-default btn-sm my-button"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>上一页</button>',
                    'sNext':'<button type="button" class="btn btn-default btn-sm my-button"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>下一页</button>',
                    "sLast" : " 末页 "},
                infoEmpty:'没有数据'
            }
        })
    }

    //表格选择某行变色
    $('#sharetable tbody').on('click','tr',function () {
        $('#sharetable tbody tr').css('background','#fff').removeClass("row_selected");
        $(this).css('background','#FFFACD').addClass('row_selected')
    })

    $(document).on('click','#report-share-btn',function () {
        var sid = $('.row_selected a').attr("sid");
        if (sid==null){
            alert("请选择举报对象");
            return;
        }
        $("#sid-hidden").val(sid);
        $("#report-reason").val("");
        $("#report-btn").click();
    })
    
    $("#report-confirm-btn").click(function () {
        var sid = $("#sid-hidden").val();
        console.log(sid);
        var reason = $("#report-reason").val();
        $.ajax({
            url:"/report/addReport",
            type:"post",
            data:{sid:sid,reason:reason},
            success:function (data) {
                alert(data.msg);
                $("#closereportmodal").click();
            }
        })
    })

    $(document).on('click','.common-share-item',function () {
        var sid=$(this).attr('sid');
        var d = dialog({
            content:"确定保存该分享吗?",
            align:'left',
            cancelValue: '取消',
            cancel: function () {
                return true;
            },
            okValue: '确定',
            ok: function () {
                this.title('提交中…');
                $.ajax({
                    url:"/share/saveShare",
                    type:"post",
                    data:{sid:sid},
                    success:function (data) {
                        alert(data.msg);
                    }
                })
                return true;
            }
        });
        d.show();
        return false;
    })

    $(document).on("click",".zan",function () {
        var context = $(this);
        var sid = $(this).attr("sid");
        var type;
        if ($(this).hasClass("disable")){
            type=1;//进行点赞
        }else{
            type=0;//取消点赞
        }
        var likenode =$("#like-"+sid);
        console.log("点赞:"+likenode.text());
        var count = likenode.text();
        $.ajax({
            url:"/share/zan",
            type:"post",
            data:{sid:sid,type:type},
            success:function (data) {
                if (type===1){
                    context.attr("src","/img/liked.png");
                    context.removeClass("disable").addClass("active");
                    likenode.text(++count);
                }else{
                    context.attr("src","/img/like.png").removeClass("active").addClass("disable");
                    likenode.text(--count);
                }
            }
        })
    });

})