var user = getUserInfo();
var vue = new Vue({
    el:"#app",
    data:{
        param:{},
        strategy:{},
        page:{},
        hash:{},
        content:{}
    },
    methods:{
        //查看明细
        queryDetail:function (){
            ajaxGet("article", "/strategies/detail", {id:this.param.id}, function (data) {
                vue.strategy = data.data;
                vue.content = data.data.content;
                //统计数据
                ajaxPost("article","/strategies/veiwnumIncr",{sid:vue.param.id}, function (data) {
                    vue.hash = data.data;
                })

                //用户收藏攻略id集合
                if(user){
                    vue.queryUserFavor(vue.param.id,user.id);
                }
            })
        },
        //攻略点赞
        strategyThumbup:function(){
            ajaxPost("article", "/strategies/thumbnumIncr",{sid:vue.strategy.id}, function (data) {
                var map = data.data;
                if(map.result){
                    popup("顶成功啦");
                }else{
                    popup("今天你已经顶过了");
                }
                vue.hash =map;
            })
        },
        //攻略收藏
        favor:function(){
            ajaxPost("article", "/strategies/favornumIncr",{sid:vue.strategy.id}, function (data) {
                var map = data.data;
                if(map.result){
                    popup("收藏成功");
                }else{
                    popup("已取消收藏");
                }
                vue.hash =map;
                if(user){
                    vue.queryUserFavor(vue.strategy.id,user.id);
                }

            })
        },
        //光标定位
        contentFocus:function(){
            $("#content").focus();
        },
        //评论点赞
        commentThumb:function(commentId){
            var page = $("#pagination").find("a.active").html()||1;
            ajaxPost("comment","/strategyComments/thumb",{cid:commentId}, function (data) {
                vue.commentPage(page,getParams().id);
            })
        },
        //鼠标移上
        mouseover:function(even){
            $(even.currentTarget).find(".rep-del").css("display", "block");
        },
        //鼠标移出
        mouseout:function(even){
            $(even.currentTarget).find(".rep-del").css("display", "none");
        },
        //评论分页
        commentPage:function (page,strategyId) {//分页
            strategyId = strategyId || vue.strategy.id;
            ajaxGet("comment","/strategyComments/query", {currentPage:page, strategyId:strategyId}, function(data){
                vue.page = data.data;
                buildPage(vue.page.current, vue.page.pages,vue.commentPage);
            })
        },
        //评论添加
        commentAdd:function(){ //添加评论
            var param = {}
            param.strategyId = vue.strategy.id;
            param.strategyTitle = vue.strategy.title;

            var content = $("#content").val();
            if(!content){
                popup("评论内容必填");
                return;
            }
            param.content = content;
            $("#content").val('');

            ajaxPost("comment", "/strategyComments/save",param, function (data) {
                vue.commentPage(1,param.strategyId);

                //评论数+1
                ajaxPost("article","/strategies/replynumIncr",{sid:param.strategyId}, function (data) {
                    vue.hash = data.data;
                })
            })
        },
        //用户收藏
        queryUserFavor:function (sid,userId) {
            ajaxGet("article","/strategies/isUserFavor",{sid:vue.strategy.id, uid:userId}, function (data) {
                if(data.data){
                    $(".btn-collect i").addClass('on-i02');
                }else{
                    $(".btn-collect i").removeClass('on-i02');
                }
            })
        },
        //点赞小手
        thumbEcho :function (thumbuplist) {
            if(user){
                var id = parseInt(user.id)
                return $.inArray(id,thumbuplist ) != -1;
            }
        }
    },
    filters:{
        dateFormat:function(date){
            return dateFormat(date, "YYYY-MM-DD HH:mm:ss")
        }
    },
    mounted:function () {
        this.param = getParams();
        //查明细
        this.queryDetail();
        //攻略评论分页
        this.commentPage(1, this.param.id);

    }
});

