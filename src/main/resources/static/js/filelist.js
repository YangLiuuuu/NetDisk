$(function () {
    var table;
    var fileType = $('#filetypeinput').val();//按文件种类显示
    var tableUrl = '/file/filelist';
    tableUrl+="/"+fileType;
    initTable();//表格初始化

    /**
     * 初始化表格加载
     */
    function initTable() {
        table = $('#filetable').DataTable({
            "processing": true,//显示等待信息
            "searching": false,//是否支持页内搜索
            "serverSide": false,//是否服务器端分页
            "paging":true,//是否分页
            "lengthChange":false,//选择下拉框调整每页显示数量
            'iDisplayLength': 7, //每页初始显示7条记录
            'pagingType':'full',
            "ajax": {
                "url": tableUrl
            },
            "columns":[{
                "data":null,
                "target":0,
                "render":function (data, type, row, meta) {
                    return '<input type="checkbox" ufid='+row.ufid+'>'
                }
            },{
                "data":"fileName",
                "target":"fileName",
                "title":"文件名",
                "render":function (data, type, row, meta) {
                    return "<a class='downloadbtn' href='#'  ufid="+row.ufid+">"+row.fileName+"</a>"//href='/file/download?ufid="+row.ufid+"'
                }
            },/*{
                "data":"null",
                "target":"0",
                "title":"编辑",
                "render":function (data, type, row, meta) {
                    return '<input type="button" class="btn-app modifybtn" value="修改" ufid='+row.ufid+'>'
                }
            },*/{
                "data":"saveDate",
                "target":"saveDate",
                "title":"保存日期",
                "render":function  formateDate(time) {
                    var date = new Date(time);
                    return date.getFullYear()+'-'+(date.getMonth()+1)+'-'+date.getDate()+'-'+date.getHours()+':'+date.getMinutes()
                    //return date;
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
            },{
                "target":6,
                "title":"操作",
                "data":null,
                "orderable":false,
                "defaultContent":"",
                "width":"10%",
                "render":function (data, type, row, meta) {
                    return data =  '<button class="btn btn-danger btn-sm deletefilebtn' +
                        '" data-id=' + row.ufid + '><i class="fa fa-trash-o"></i>删除</button>'
                        +'<button class="btn btn-primary btn-sm sharefile" data-id=' + row.ufid + '><i class="fa icon-share-alt"></i>分享</button>';
                }
            }
            ],
            language:{
                processing:'加载中',
                info:'显示第_START_到第_END_条记录，共_TOTAL_条',
                paginate:{
                    "sFirst" : " 首页 ",
                    'sPrevious':'<button type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-arrow-left" aria-hidden="true"></span>上一页</button>',
                    'sNext':'<button type="button" class="btn btn-default btn-sm"><span class="glyphicon glyphicon-arrow-right" aria-hidden="true"></span>下一页</button>',
                    "sLast" : " 末页 "},
                infoEmpty:'没有数据'

            }
        })

    }

    /**
     * 文件删除按钮
     */
    $(document).on('click','.deletefilebtn',function () {
        var ufid = $(this).attr('data-id');
        var d = dialog({
            title: '提示',
            content: '你确定要删除该文件吗?',
            okValue: '确定',
            ok: function () {
                $.ajax({
                    url:'/file/deleteFile',
                    method:'post',
                    data:{ufid:ufid},
                    success:function (data) {
                        if (data.status===1)
                            alert("删除失败");
                        window.location.reload();
                    }
                })
                return true;
            },
            cancelValue: '取消',
            cancel: function () {}
        })
        d.show();
    })

    $(document).on('click','.downloadbtn',function () {
        /*alert($(this).attr("ufid"))*/
        var ufid = $(this).attr("ufid")
        $.ajax({
            url: '/file/isCapableDownload?ufid=' + ufid,
            type: 'post',
            success: function (data) {
                if (data.status === 0) {
                    window.location.href = '/file/download?ufid=' + ufid;
                } else {
                    alert(data.msg);
                }
            }
        })
    })

    //表格选择某行变色
    $('#filetable tbody').on('click','tr',function () {
        $('#filetable tbody tr').css('background','#fff').removeClass("row_selected");
        $(this).css('background','#FFFACD').addClass('row_selected')
    })

    /**
     * 编辑按钮响应
     */
    $(document).on('click','#editbtn',function () {
        var ufid=$('.row_selected a').attr('ufid');
        var filename = $('.row_selected a').text();
       // var content = '<label for="filename">名称</label><input type="text" id="diafilename" value='+filename+'>'
        var form = $('<form action="/file/modifyFileName" method="post"><label for="filename">名称</label><input name="filename"' +
                ' value='+filename+'><input type="hidden" name="ufid" value='+ufid+'></form>');
        var d = dialog({
            content:form,
            align:'left',
            cancelValue: '取消',
            cancel: function () {
                return true;
            },
            okValue: '确定',
            ok: function () {
                this.title('提交中…');
                form.ajaxSubmit();
                window.location.reload();
                return true;
            }
        });
        d.show();
    })


    /**
     * 上传按钮
     */
    $('#uploadbtn').click(function () {
        var formData = new FormData();
        formData.append('file',$('#fileinput')[0].files[0]);
        $.ajax({
            url:'/file/upload',
            type:'post',
            contentType:false,
            processData:false,
            data:formData,
            success:function (data) {
                if (data.status===0){
                    alert("上传成功");
                    $("#closemodal").click();
                    table.ajax.reload();
                }else{
                    alert(data.msg)
                }
            }
        })
        return false;
    })
})