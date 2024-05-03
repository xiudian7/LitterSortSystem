var vue = new Vue({
    el:"#app",
    data:{
    },
    methods:{
        login:function (){
            $.post(getServiceUrl('member') + "/userInfos/login", $("#_j_login_form").serialize(), function (data){
                console.log(data);
                /**
                 * JsonReslut   --- data
                 * {
                 *    code:200,
                 *    msg:xxx,
                 *    data:{
                 *        token:xx
                 *        user:{...}
                 *    }
                 * }
                 *
                 */
                if(data.code == 200) {
                    popup("登录成功！");

                    var map = data.data;
                    var token = map.token;
                    var user = map.user;

                    Cookies.set('user', JSON.stringify(user), { expires: 1/48,path:'/'});
                    Cookies.set('token', token, { expires: 1/48,path:'/'});

                    var url = document.referrer ? document.referrer : "/";
                    if(url.indexOf("regist.html") > -1 || url.indexOf("login.html") > -1){
                        url = "/";
                    }

                    // Pause execution for 3 seconds before redirecting
                    setTimeout(function() {
                        window.location.href = url;
                    }, 3000); // 3000 milliseconds = 3 seconds
                } else {
                    popup(data.msg);
                }
            })
        }
    },
    mounted:function () {
    }
});
