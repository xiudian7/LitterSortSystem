package cn.litterSortSystem.xiudian7.member.service;


import cn.litterSortSystem.xiudian7.member.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

public interface IUserInfoService  extends IService<UserInfo> {
    boolean checkPhone(String username);
    Map<String, Object> login(String username, String password);

    void regist(String username, String password, String rpassword, int gender, String verifyCode);

    void sendVerifyCode(String phone);

    UserInfo getByToken(String token);
}
