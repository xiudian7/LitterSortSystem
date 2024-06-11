package cn.litterSortSystem.xiudian7.common.sercurity.interceptor;

import cn.litterSortSystem.xiudian7.common.redis.service.RedisService;
import cn.litterSortSystem.xiudian7.common.redis.util.RedisKeys;
import cn.litterSortSystem.xiudian7.common.web.response.JsonResult;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CheckLoginInterceptor implements HandlerInterceptor {


    @Autowired
    private RedisService redisService;
    //判断是否登录
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)){
            return true;
        }
        String token = request.getHeader("token");
        System.out.println(token);
        String key = RedisKeys.USER_LOGIN_TOKEN.join(token);
        if(!redisService.hasKey(key)){
            //没有登录
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(JsonResult.noLogin()));
            return false;
        }

        return true;
    }
}
