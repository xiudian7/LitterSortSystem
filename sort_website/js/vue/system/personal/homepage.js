var user = getUserInfo();

var vue = new Vue({
    el:"#app",
    data:{
        //userId对应的用户--窝的主人
        owner:{id:"1", nickname:"dafei", headImgUrl: "/images/test/personal/df.jpg", level:100, city:"广州", info:"广州最靓的仔"},
        isFollow:false,  //是否关注， true已经关注  false 未关注
        follows:[ //关注列表前4个
            {id:"1", nickname:"lyf", headImgUrl: "/images/test/personal/lyf.jpg"},
            {id:"2", nickname:"qsz", headImgUrl: "/images/test/personal/qsz.jpg"},
            {id:"3", nickname:"zhy", headImgUrl: "/images/test/personal/zhy.jpg"},
            {id:"4", nickname:"dafei", headImgUrl: "/images/test/personal/df.jpg", level:100, city:"广州", info:"广州最靓的仔"}
        ],
        followNum:100
    },
    methods:{
        follow:function () {
            var param = getParams();
            var userId = param.userId;
            //关注与取消关注--接口实现

        }
    },
    filters:{

    },
    mounted:function () {
        var param = getParams();
        //userId对应的用户关注列表---follows

        //当前登录用户是否关注该用户了？--isFollow

        //该用户关注人数--followNum

    }
});

