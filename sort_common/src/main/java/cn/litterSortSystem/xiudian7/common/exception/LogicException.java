package cn.litterSortSystem.xiudian7.common.exception;

/**
 * 自定义的异常
 * 1:用于区分系统异常与主动抛出的一样
 */
public class LogicException extends RuntimeException{

    public LogicException(String msg){
        super(msg);
    }
}