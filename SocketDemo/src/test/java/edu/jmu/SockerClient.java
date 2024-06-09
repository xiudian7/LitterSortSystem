package edu.jmu;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author: lrui1
 * @description
 * @date: 2024/6/7
 */
public class SockerClient {
    public static void main(String[] args) {
        // 配置连接参数
        final String ip = "192.168.79.193";
        final int port = 4567;
        // 获取当前工作目录
        String workingDirectory = System.getProperty("user.dir");
        // 打印工作目录
        System.out.println("当前工作目录: " + workingDirectory);
        // 创建文件并加载流
        String filePath = "photos/2.jpg";
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
}
