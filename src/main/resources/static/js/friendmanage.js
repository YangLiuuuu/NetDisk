$(function () {

    loadFriendRequest();//加载好友请求
    loadFriendList();//加载好友列表


    function reloadFriendList(){
        $('#friend-list-ul').empty();
        $('#friend-request-list').empty();
        loadFriendList();
        loadFriendRequest();
    }


        /**
         * 接受好友请求按钮
         */
    $(document).on('click','.acceptbtn',function () {
        var msgid = $(this).attr('msg-id');
        var context = $(this).parent().parent();
        $.ajax({
            url:'/relation/addfriend',
            type:'post',
            data:{msgid:msgid},
            success:function (data) {
                if (data.status===0){
                    alert("添加成功")
                    var divid = 'request-div-id-'+msgid;
                    $('#'+divid).children('a').remove();
                    var p = $("<p>你已接受对方的请求</p>");
                    context.append(p);
                }
            }
        })
    })


        /**
         * 加载好友请求列表
         */
    function loadFriendRequest(){
        $.ajax({
            url:'/relation/queryfriendrequest',
            type:'post',
            success:function (data) {
                if (data.status===0){
                    var ul = $('#friend-request-list');
                    var userdata = data.data;
                    for (var i=0;i<userdata.length;i++){
                        // console.log('加载'+userdata[i].user.nickname)
                        var li = $("<li class='friend-list-item'></li>")
                        var div = $("<div style=''></div>")
                        var nickname_title;
                        if (userdata[i].isSender===0){//如果自己是接收者
                            if (userdata[i].message.accepted===0){//未答复
                                nickname_title = $("<strong>"+userdata[i].fromUser.nickname+"</strong>");
                                div.append(nickname_title).append($("<a type='button' style='float: right'  msg-id="+userdata[i].message.msgid+" class='btn btn-info btn-xs acceptbtn'>接受</a>"))
                                    .append($("<a type='button' style='float: right' msg-id="+userdata[i].message.msgid+" class='btn btn-info btn-xs rejectbtn '>拒绝</a>"));
                            }else if (userdata[i].message.accepted===-1){//已拒绝
                                nickname_title = $("<strong>"+userdata[i].fromUser.nickname+"</strong>");
                                div.append(nickname_title).append($("<p>你已拒绝对方请求</p>"))
                            }  else if (userdata[i].message.accepted===1){//已接受
                                nickname_title = $("<strong>"+userdata[i].fromUser.nickname+"</strong>");
                                div.append(nickname_title).append($("<p>你已同意对方请求</p>"))
                            }
                        }else{
                            if (userdata[i].isSender===1){//自己是发送者
                                nickname_title = $("<strong>"+userdata[i].toUser.nickname+"</strong>")
                                if (userdata[i].message.accepted===1){//对方已接受
                                    div.append(nickname_title).append($("<p>对方已接受你的好友请求</p>"))
                                } else if (userdata[i].message.accepted===-1){//对方已拒绝
                                    div.append(nickname_title).append($("<p>对方已拒绝你的好友请求</p>"));
                                }else if (userdata[i].message.accepted===0){//对方还未答复
                                    div.append(nickname_title).append($("<p>已发送好友请求</p>"));
                                }
                            }
                        }
                        div.attr('id','request-div-id-'+userdata[i].message.msgid);
                        li.append(div);
                        ul.append(li);
                    }
                }
            }
        })
    }

        /**
         * 加载好友列表
         */
        function loadFriendList(){
            $.ajax({
                url:'/relation/queryfriendlist',
                type:'post',
                success:function (data) {

                    var ul = $('#friend-list-ul');
                    var frienddata = data.data;
                    frienddata.sort();
                    for (var i=0;i<frienddata.length;i++){
                        var li = $("<li class='friend-list-item' friend-uid="+frienddata[i].uid+"></li>");
                        var div = $("<div></div>")
                        var namep = $("<p>"+frienddata[i].nickname+"</p>")
                        var accountp = $("<p>账号:"+frienddata[i].username+"</p>")
                        div.append(namep).append(accountp);
                        li.append(div);
                        ul.append(li);
                    }
                }
            })
        }

        /**
         * 关闭模态框，清理内容
         */
    $('#closefriendmodal').click(function () {
        $('#search-result-wrap').css('display','none');
        $('#add-friend-btn').removeClass('active').addClass('disabled');
        $('#query-input').val('');
    })

        /**
         * 查找好友
         */
    $('#query_btn').click(function () {
        console.log('查询');
        var input_account = $('#query-input').val();
        $.ajax({
            url:'/relation/queryfriend',
            type:'post',
            data:{account:input_account},
            success:function (data) {
                console.log(data)
                if (data.status===0){
                    // console.log(data.user);
                    $('#search-result-wrap').css('display','inline');
                    if (data.data.friend==true){
                        $('#add-friend-btn').addClass("disabled").text('已是好友');
                    }else {
                        $('#add-friend-btn').addClass("active").text('加为好友');
                    }
                    $('#search-result-nick').html('昵称:'+data.data.user.nickname)
                    $('#search-result-account').html('账号:'+data.data.user.username);
                }else{
                    alert(data.msg);
                }
            }
        })
    })

        /**
         * 发送加好友请求
         */
    $('#add-friend-btn').click(function () {
        console.log('click')
        if (!$('#add-friend-btn').hasClass('active')){
            return;//如果按钮没有激活直接返回
        }
        var str = $('#search-result-account').text();
        // console.log(str);
        var account = str.substring(str.indexOf(':')+1);
        // console.log(account);
        $.ajax({
            url:'/relation/addfriendrequest',
            type:'post',
            data:{'account':account},
            success:function (data) {
                if (data.status===0){
                    alert('请求已发送');
                    reloadFriendList();
                    $('#closefriendmodal').click();
                }else{
                    alert(data.msg);
                }
            }
        })
    })

        /**
         * 好友条目点击，刷新右侧内容页
         */
    $("#friend-list-ul").on('click','.friend-list-item',function () {
        var nick = $(this).find("p:first").text();//昵称
        var account = $(this).find("p:last").text();//账号
        $("#user-detail-nick").text(nick);
        $("#user-detail-account").text(account.substring(account.indexOf(":")+1));
        $("#user-detail-uid").text($(this).attr("friend-uid"));
        $("#init-content").css("display","none");
        $("#profile_content").css("display","inline");
    })

    //分享文件按钮点击
    $(document).on("click","#share-btn",function () {
        if (!$.fn.DataTable.isDataTable('#filetable'))//不必每次点击都加载，如果已经加载了就不再请求后端加载了
        {
            $('#filetable').DataTable({
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
                        return data + "MB"
                    }
                },{
                    "data":"levels",
                    "target":"levels",
                    "title":"下载等级"
                }],
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
        $('.share-check').prop('checked',false);
        $("#share-btn-hide").click();

    });

    /**
     * 分享 checkbox单选
     */
    $(document).on('click','.share-check',function () {
         $('.share-check').prop('checked',false);
         $(this).prop('checked',true);
    })

    /**
     * 分享确定按钮
     */
    $('#share-confirm-btn').click(function () {
        var shareUid = $("#user-detail-uid").text();
        var shareUfid = $('.share-check:checked').attr('ufid');
        $.ajax({
            url:'/share/addShare',
            type:'post',
            data:{'shareUid':shareUid,'shareUfid':shareUfid,"type":1},
            success:function (data) {
                if (data.status===0){
                    alert('分享成功');
                    $('#closesharemodal').click();
                }else{
                    alert(data.msg);
                }
            }
        })
    })
    }
)