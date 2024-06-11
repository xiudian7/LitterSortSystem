var vue = new Vue({
    el:"#app",
    data:{
    },
    methods:{
        //注册-手机检查
        phoneCheck:function (){
            var val = $('#inputPhone').val();
            //js 正则表达语法:
            //    / /g  : 正则表达式对象
            // ^1  以1开头
            //  \d 数字 0-9 数字中一个
            //  {10}  重复个数   \d{10} 表示10个数字
            // $  以xx结束
            //  [3456789]  代码 3 4 5 6 7 8 9 中一个数
            //   /^1[3456789]\d{9}$/
            if (/^1\d{10}$/g.test(val)) {
                $.get(getServiceUrl("member") + "/userInfos/checkPhone", {phone:val}, function (data) {
                    if(data.code == 200){
                        if(!data.data){
                            $('#inputPhone').next().text('').hide()
                            $('.login-box').hide()
                            $('.signup-box').show()
                            $("#phone").val(val);
                        }else{
                            $('#inputPhone').next().text('手机号码已注册.').show()
                        }
                    }
                })
            } else {
                $('#inputPhone').next().text('手机号码格式不正确').show()
            }
        },
        //注册-发送短信
        sendCode:function (event){
            var _this = $(event.target);//事件源
            if (_this.hasClass('disabled')) {
            } else {
                var self = _this;
                var count = 10;
                self.addClass('disabled')
                self.text(count + '秒后重新获取')
                var timer = setInterval(function () {
                    count--;
                    if (count > 0) {
                        self.text(count + '秒后重新获取');
                    } else {
                        clearInterval(timer)
                        self.text('重新获取验证码')
                        self.removeClass("disabled");
                    }
                }, 1000);

                var phone = $("#phone").val();
                $.get(getServiceUrl("member") + "/userInfos/sendVerifyCode", {phone:phone}, function (data) {
                    console.log(data);
                    if(data.code == 200){
                        popup("发送成功")
                    }else{
                        popup(data.msg);
                    }
                })
            }
        },
        //注册-完成注册
        regist:function (){
            $.post(getServiceUrl("member") + "/userInfos/regist", $("#editForm").serialize(), function (data) {
                if(data.code == 200){
                    //location.href = "/login.html";
                    popup("注册成功！即将跳转登入界面！");
                    setTimeout(function() {
                        location.href = "/login.html";
                    }, 3000); // 3000 milliseconds = 3 seconds
                }else{
                    popup(data.msg);
                }
            })
        }
    },
    mounted:function () {
    }
});
