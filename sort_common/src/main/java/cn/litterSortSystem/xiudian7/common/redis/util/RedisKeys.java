package cn.wolfcode.wolf2w.common.redis.util;


import cn.wolfcode.wolf2w.common.util.Consts;
import lombok.Getter;

/*
Redis keys 管理枚举类
约定：一个redis key对应一个枚举类
 */
@Getter
public enum RedisKeys {
    //注册验证码key的实例
    VERIFY_CODE("verify_code",Consts.VERIFY_CODE_VAI_TIME * 60L),
    //用户登录token key 的实例
    USER_LOGIN_TOKEN("user_login_token",Consts.USER_INFO_TOKEN_VAI_TIME*60L);

    private String prefix; //key前缀
    private Long time; //key有效时间，默认单位为s

    private RedisKeys(String prefix,Long time){
        this.prefix=prefix;
        this.time=time;
    }
    //用来拼接出真实的key
    public String join(String value){
        return this.prefix+ Consts.splitStr()+value;
    }

}
