package cn.litterSortSystem.xiudian7.identify.service.impl;

import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import cn.litterSortSystem.xiudian7.identify.mapper.IdentifyMapper;
import cn.litterSortSystem.xiudian7.identify.service.IdentifyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class IdentifyServiceImpl extends ServiceImpl<IdentifyMapper,ImageInfo>implements IdentifyService {

    @Override
    public String identify(String file) {
        String substring = UUID.randomUUID().toString().substring(0, 6);
        //System.out.println(substring);
        String imagePath="D:\\CodePractice\\litter_sort_system\\model_code\\images\\"+substring+".jpg";
        String s = generateImage(file, imagePath);
       //System.out.println(s);
        /*try{
            FileOutputStream fos=new FileOutputStream("D:\\CodePractice\\litter_sort_system\\model_code\\images\\"+imagePath+".jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }*/
        int i = identifyResult(substring+".jpg");
        //数字标识：class_labels = ["glass", "paper", "cardboard", "plastic", "metal", "trash"]
        String []information={"玻璃","纸张","纸皮","塑料","金属","废料"};
        System.out.println(information[i]);
        return information[i];
    }

    @Override
    public  String generateImage(String file, String imagePath) {
        // 解密
        try {
            // 去掉base64前缀 data:image/jpeg;base64,
            file = file.substring(file.indexOf(",", 1) + 1);
            // 解密，解密的结果是一个byte数组
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] imgbytes = decoder.decode(file);
            for (int i = 0; i < imgbytes.length; ++i) {
                if (imgbytes[i] < 0) {
                    imgbytes[i] += 256;
                }
            }

            // 保存图片
            OutputStream out = new FileOutputStream(imagePath);
            out.write(imgbytes);
            out.flush();
            out.close();
            // 返回图片的相对路径 = 图片分类路径+图片名+图片后缀
            return imagePath;
        } catch (IOException e) {
            return null;
        }
    }


    public static int identifyResult(String imagePath){
        //System.out.println(imagePath);
        //System.out.println("当前 Java 程序的工作目录：" + System.getProperty("user.dir"));
        Process proc;
        StringBuilder pythonOutput = new StringBuilder(); // 用于存储Python脚本的输出结果
        try {
            String command = "D:\\CodePractice\\anaconda\\envs\\pytorch\\python.exe model_code/main.py " + imagePath;
            proc = Runtime.getRuntime().exec(command);

            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader err = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
            // 处理标准输出流
            String line = null;
            String ret=null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
                ret=line;
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
            /*System.out.println("Python脚本的输出结果：");
            System.out.println(pythonOutput.toString());*/
            return Integer.parseInt(ret);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}