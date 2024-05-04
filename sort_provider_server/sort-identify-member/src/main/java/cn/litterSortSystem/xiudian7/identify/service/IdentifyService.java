package cn.litterSortSystem.xiudian7.identify.service;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IdentifyService extends IService<ImageInfo> {
    public int identify(String imagePath);
}
