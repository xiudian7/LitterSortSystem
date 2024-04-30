package cn.litterSortSystem.xiudian7.member.web.controller;



import cn.litterSortSystem.xiudian7.common.web.response.JsonResult;
import cn.litterSortSystem.xiudian7.member.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("userInfos")
public class UserInfoController {

    @Autowired
    private IUserInfoService userInfoService;


    @PostMapping("/login")
    public JsonResult login(String username, String password){
        Map<String,Object> map=userInfoService.login(username,password);
        return JsonResult.success();
    }
}
