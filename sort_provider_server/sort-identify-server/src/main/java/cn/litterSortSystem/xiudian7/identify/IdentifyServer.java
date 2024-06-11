package cn.litterSortSystem.xiudian7.identify;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@SpringBootApplication(scanBasePackages = "cn.litterSortSystem.xiudian7")
@MapperScan("cn.litterSortSystem.xiudian7.identify.mapper")
@EnableDiscoveryClient
public class IdentifyServer {
    public static void main(String[] args) {
       /* System.out.println("当前 Java 程序的工作目录：" + System.getProperty("user.dir"));
        Process proc;
        StringBuilder pythonOutput = new StringBuilder(); // 用于存储Python脚本的输出结果
        try {
            proc = Runtime.getRuntime().exec("D:\\CodePractice\\anaconda\\envs\\pytorch\\python.exe model_code/main.py");

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
        }*/

        SpringApplication.run(IdentifyServer.class,args);
    }

}
