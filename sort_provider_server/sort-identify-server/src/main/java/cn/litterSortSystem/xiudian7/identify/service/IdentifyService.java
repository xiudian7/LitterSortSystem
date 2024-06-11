package cn.litterSortSystem.xiudian7.identify.service;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.python.antlr.ast.Str;

import java.io.File;

public interface IdentifyService extends IService<ImageInfo> {
    public String identify();

    public String generateImage(String file ,String imagePath);

    //调用树莓派拍摄图片
    public void takePhoto(String filePath);

    public int sendIdentifyMsg(String path);
}