package cn.litterSortSystem.xiudian7.identify.service;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

public interface IdentifyService extends IService<ImageInfo> {
    public String identify(MultipartFile file);
}