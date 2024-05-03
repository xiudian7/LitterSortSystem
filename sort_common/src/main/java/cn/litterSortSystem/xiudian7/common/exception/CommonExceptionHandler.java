package cn.litterSortSystem.xiudian7.common.exception;


import cn.litterSortSystem.xiudian7.common.web.response.JsonResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;


//在trip -common 定义异常处理规范
//后续在每个服务中，定制异常处理具体实现

/**
 * 通用异常处理类
 *  RestControllerAdvice  controller类功能增强注解, 动态代理controller类实现一些额外功能
 *
 *  请求进入controller映射方法之前做功能增强: 经典用法:日期格式化
 *
 *  请求进入controller映射方法之后做功能增强: 经典用法:统一异常处理
 *
 *
 */

@RestControllerAdvice//统一异常处理
//Common包中很多时候都是做统一异常处理规范，抽象组件 是一个父类，不同微服务继承该父类，定制自己的异常处理
public class CommonExceptionHandler {
    //这个方法定义的跟映射方法操作一样
    @ExceptionHandler(LogicException.class)
    public JsonResult logicExp(Exception e, HttpServletResponse resp) {
        e.printStackTrace();
        resp.setContentType("application/json;charset=utf-8");
        return JsonResult.error(JsonResult.CODE_ERROR_PARAM, e.getMessage(), null);
    }

    @ExceptionHandler(RuntimeException.class)
    public JsonResult  runTimeExp(Exception e, HttpServletResponse resp) {
        e.printStackTrace();
        resp.setContentType("application/json;charset=utf-8");
        return JsonResult.defaultError();
    }
}
