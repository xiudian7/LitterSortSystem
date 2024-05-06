package cn.litterSortSystem.xiudian7.identify.service.impl;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import cn.litterSortSystem.xiudian7.identify.mapper.IdentifyMapper;
import cn.litterSortSystem.xiudian7.identify.service.IdentifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;

@Service
@Transactional
public class IdentifyServiceImpl extends ServiceImpl<IdentifyMapper,ImageInfo>implements IdentifyService {

    @Override
    public String identify(MultipartFile file) {
        String substring = UUID.randomUUID().toString().substring(0, 6);
        String imagePath=substring;
        try{
            FileOutputStream fos=new FileOutputStream("D:\\CodePractice\\litter_sort_system\\model_code\\images\\"+imagePath+".jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int i = identifyResult(imagePath);
        System.out.println(i);
        return null;
    }
    public static int identifyResult(String imagePath){
        System.out.println("当前 Java 程序的工作目录：" + System.getProperty("user.dir"));
        Process proc;
        StringBuilder pythonOutput = new StringBuilder(); // 用于存储Python脚本的输出结果
        try {
            String command = "D:\\CodePractice\\anaconda\\envs\\pytorch\\python.exe model_code/main.py " + imagePath;
            proc = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            // 处理标准输出流
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                pythonOutput.append(line).append("\n");
            }

            // 处理错误输出流
            while ((line = err.readLine()) != null) {
                System.err.println(line);
            }

            in.close();
            err.close();

            proc.waitFor();

            // 输出Python脚本的输出结果到控制台
            System.out.println("Python脚本的输出结果：");
            System.out.println(pythonOutput.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}