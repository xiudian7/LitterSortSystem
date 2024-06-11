package cn.litterSortSystem.xiudian7.identify.service.impl;

import cn.litterSortSystem.xiudian7.common.exception.LogicException;
import cn.litterSortSystem.xiudian7.identify.ImageInfo;
import cn.litterSortSystem.xiudian7.identify.mapper.IdentifyMapper;
import cn.litterSortSystem.xiudian7.identify.service.IdentifyService;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

@Service
@Transactional
public class IdentifyServiceImpl extends ServiceImpl<IdentifyMapper,ImageInfo>implements IdentifyService {

    @Override
    public String identify() {
        String substring = UUID.randomUUID().toString().substring(0, 6);
        String imagePath="D:\\CodePractice\\litter_sort_system\\model_code\\images\\"+substring+".jpg";
        takePhoto(imagePath);
        int res=sendIdentifyMsg("images/"+substring+".jpg");
        //int i = identifyResult(substring+".jpg");
        //数字标识：class_labels = ["glass", "paper", "cardboard", "plastic", "metal", "trash"]
        if(res==-1)
        throw new LogicException("未能识别出垃圾!");
        String []information={"玻璃","纸张","纸皮","塑料","金属","废料"};
        System.out.println(information[res]);
        return information[res];
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


//将树莓派拍摄图片所存放的地址传进来，进行获取图片
    @Override
    public void takePhoto(String filePath) {
        // 配置连接参数
        final String ip = "192.168.139.193";
        final int port = 4567;
        // 获取当前工作目录
        String workingDirectory = System.getProperty("user.dir");
        // 打印工作目录
        System.out.println("当前工作目录: " + workingDirectory);
        // 创建文件并加载流
        File file = new File(filePath);
        if(!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("文件已创建: " + filePath);
                } else {
                    System.out.println("文件创建失败: " + filePath);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try(Socket socket = new Socket(ip, port);
            FileOutputStream fos=new FileOutputStream(filePath)
        ) {
            System.out.println("已连接到服务器端口，准备接收图片...");

            InputStream inputStream = socket.getInputStream();
            // 定义缓存数组，每次只读1024字节
            byte[] buf = new byte[1024];
            int len = 0;
            while((len = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //使用http请求来发送验证消息,提升效率 1s左右完成
    @Override
    public int sendIdentifyMsg(String path) {
        int prediction = -1;
        try {

            // 创建 URL 对象
            URL url = new URL("http://localhost:5000");

            // 打开连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 设置请求方法为 POST
            connection.setRequestMethod("POST");

            // 设置请求头
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            connection.setRequestProperty("Accept", "application/json");

            // 允许写入
            connection.setDoOutput(true);

            // 创建 JSON 请求体
            String jsonInputString = String.format("{\"imagePath\": \"%s\"}", path);

            // 将请求体写入到连接的输出流中
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 获取响应码
            int code = connection.getResponseCode();
            System.out.println("Response Code: " + code);

            // 读取响应内容
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
            }

            // 解析 JSON 响应
            JSONObject jsonResponse = JSON.parseObject(response.toString());
            prediction = jsonResponse.getIntValue("prediction");
            System.out.println(prediction);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return prediction;
    }


    public static int identifyResult(String imagePath){
        //System.out.println(imagePath);
        //System.out.println("当前 Java 程序的工作目录：" + System.getProperty("user.dir"));
        Process proc;
        StringBuilder pythonOutput = new StringBuilder(); // 用于存储Python脚本的输出结果
        try {
            String command = "D:\\CodePractice\\anaconda\\envs\\pytorch\\python.exe model_code/produce.py " + imagePath;
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