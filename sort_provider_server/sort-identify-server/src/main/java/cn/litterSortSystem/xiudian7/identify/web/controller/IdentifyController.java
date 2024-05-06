package cn.litterSortSystem.xiudian7.identify.web.controller;


import cn.litterSortSystem.xiudian7.common.web.response.JsonResult;
import cn.litterSortSystem.xiudian7.identify.service.IdentifyService;
import cn.litterSortSystem.xiudian7.member.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("functions")
public class IdentifyController {
    @Autowired
    private IdentifyService identifyService;
    @PostMapping("/identify")
    public JsonResult identify(@RequestParam("file")MultipartFile file){
        System.out.println(file);
        String identify = identifyService.identify(file);
        return JsonResult.success(identify);
    }

}