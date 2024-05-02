package cn.litterSortSystem.xiudian7.member.web.controller;



import cn.litterSortSystem.xiudian7.common.web.response.JsonResult;
import cn.litterSortSystem.xiudian7.member.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
        return JsonResult.success(map);
    }
    @GetMapping("/checkPhone")
    public JsonResult checkPhone(String phone){
        boolean ret =userInfoService.checkPhone(phone);
        return JsonResult.success(ret);
    }
    @GetMapping("/sendVerifyCode")
    public JsonResult sendVerifyCode(String phone){
        userInfoService.sendVerifyCode(phone);
        return JsonResult.success();
    }
    @PostMapping("/regist")
    public JsonResult regist(String phone,String password,String rpassword,int gender,String verifyCode){
        userInfoService.regist(phone,password,rpassword,gender,verifyCode);
        return JsonResult.success();
    }
}
