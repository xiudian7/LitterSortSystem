package cn.litterSortSystem.xiudian7.common.util;



import cn.litterSortSystem.xiudian7.common.exception.LogicException;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AssertUtil {

    private static final String PHONE_NUMBER_PATTERN = "^1[3-9]\\d{9}$";

    public AssertUtil(){}

    public static void hasText(String text,String message){
        if(!StringUtils.hasText(text)){
            throw new LogicException(message);
        }
    }

    public static void isEquals(String v1, String v2, String msg) {
        if(v1 == null || v2 == null)
            throw new LogicException("参数不能为空！");
        if(!v1.equals(v2)){
            throw new LogicException(msg);
        }
    }
   //判断手机格式是否正确
    public static boolean validPhone(String phone){
        Pattern pattern = Pattern.compile(PHONE_NUMBER_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
