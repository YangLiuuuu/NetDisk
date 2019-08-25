$(function () {

    loadFriendRequest();//加载好友请求


        /**
         * 接受好友请求按钮
         */
    $(document).on('click','.acceptbtn',function () {
        var msgid = $(this).attr('msg-id');
        $.ajax({
            url:'/relation/addfriend',
            type:'post',
            data:{msgid:msgid},
            success:function (data) {
                if (data===0){
                    var parent = $(this).parent();//找到父节点
                    $(parent).children('a').remove();
                    var p = $("<p>你已接受对方的请求</p>");
                    parent.append(p);
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
                        var div = $("<div style='float:left '></div>")
                        var nickname_title;
                        if (userdata[i].message.accepted===0&&userdata[i].isSender===0){//如果自己是接收者
                            if (userdata[i].message.accepted===0){//未答复
                                nickname_title = $("<strong>"+userdata[i].fromUser.nickname+"</strong>");
                                div.append(nickname_title).append($("<a type='button' msg-id="+userdata[i].message.msgid+" class='btn btn-info acceptbtn'>接受</a>"))
                                    .append($("<a type='button' msg-id="+userdata[i].message.msgid+" class='btn btn-info rejectbrn'>拒绝</a>"));
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
                        li.append(div);
                        ul.append(li);
                    }
                }
            }
        })
    }

        /**
         * 关闭模态框，清理内容
         */
    $('#closemodal').click(function () {
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
                    if (data.friend==true){
                        $('#add-friend-btn').addClass("disabled");
                    }else {
                        $('#add-friend-btn').addClass("active");
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
         * 加好友请求
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
                if (data.status==0){
                    alert('请求已发送');
                    $('#closemodal').click();
                }else{
                    alert(data.msg);
                }
            }
        })
    })
}
)