package cn.litterSortSystem.xiudian7.identify.service.impl;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import cn.litterSortSystem.xiudian7.identify.mapper.IdentifyMapper;
import cn.litterSortSystem.xiudian7.identify.service.IdentifyService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IdentifyServiceImpl extends ServiceImpl<IdentifyMapper,ImageInfo>implements IdentifyService {

    @Override
    public int identify(String imagePath) {
        //创建Python解释器
        PythonInterpreter interpreter=new PythonInterpreter();
        //将图片地址传递给Python程序
        interpreter.set("imagePath",new PyString(imagePath));
        //执行Python代码
        interpreter.execfile("D:/CodePractice/litter_sort_system/model_code/main.py");
        return 0;
    }
}