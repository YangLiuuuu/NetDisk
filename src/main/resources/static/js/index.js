$(function () {
    $('#switch').click(function () {
        var isSignin = $(this).attr("class");
        if(isSignin === "signin"){
            $(this).attr("class","signup");
            $('#signuptitle').show();
            $('#signupform').show();
            $('#signinform').hide();

        }else{
            $(this).attr('class',"signin");
            $('#signuptitle').hide();
            $('#signupform').hide();
            $('#signinform').show();
        }
        if($(this).attr('class')==='signin'){
            $(this).text("返回登陆");
        }else{
            $(this).text("创建一个账号");
        }
    })

    //登陆请求
    $('#sigininbtn').click(function () {
        var username=$('#signusername').val();
        var password=$('#siginpass').val();
        if(username==null||username===''||password==null||password===''){
            alert("请输入用户名和密码");
            return false;
        }
        $.ajax({
            url:'/user/login',
            data:{
                username:username,
                password:password
            },
            type:'post',
            dataType:'json',
            success:function (data) {
                // console.log();
                if(data.status===0){
                    alert("登陆成功");
                    window.location.href='/main'
                    return false;
                }else{
                    alert("登陆失败")
                    return false;
                }
            }
        })
        return  false;
    })

    //注册请求
    $('#signupbtn').click(function () {
        var username=$('#signupusername').val();
        var pass = $('#signuppass').val();
        var confirmpass = $('#confirmpass').val();
        var nickname=$('#nickname').val();

        if (username.length<=8 || username.length>=12) {
            alert("账号为9-11位字母或数字");
            return false;
        }
        if (pass.length<=5 || pass.length >= 12){
            alert("密码为6-12位字母或数字");
            return false;
        }

        if (nickname.length<=0 || nickname >= 10){
            alert("昵称不规范");
            return false;
        }

        if (pass !== confirmpass){
            alert("两次输入密码不一致");
            return false;
        }
        $.ajax({
            url:'/user/signup',
            data:{
                username:username,
                password:pass,
                nickname:nickname
            },
            type:'post',
            dataType:'json',
            success:function (data) {
                // console.log();
                if(data.status===0){
                    alert("注册成功");
                    window.location.href='/'
                    return false;
                }else{
                    alert("注册失败")
                    return false;
                }
            }
        })
        return  false;
    })

    $("#signupusername").bind("input propertychange",function(event){
        var username = $('#signupusername').val();
        $.ajax({
            url:'/user/isUsernameValid'+'?username='+username,
            type:'get',
            success:function (data) {
                if(data.status===0){
                    $('#tips').hide();
                }else{
                    $('#tips').show();
                }
            }
        })
    });


})

