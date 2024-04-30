package cn.litterSortSystem.xiudian7.member.service;


import cn.litterSortSystem.xiudian7.member.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Map;

public interface IUserInfoService  extends IService<UserInfo> {
    Map<String, Object> login(String username, String password);
}
