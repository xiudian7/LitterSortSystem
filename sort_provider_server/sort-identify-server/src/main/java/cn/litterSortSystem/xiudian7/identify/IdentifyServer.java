package cn.litterSortSystem.xiudian7.identify;


import org.mybatis.spring.annotation.MapperScan;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.io.File;
import java.net.URISyntaxException;

@SpringBootApplication(scanBasePackages = "cn.litterSortSystem.xiudian7")
@MapperScan("cn.litterSortSystem.xiudian7.identify.mapper")
@EnableDiscoveryClient
public class IdentifyServer {
    public static void main(String[] args) {
        // 获取 Jython 的 jar 文件路径
        String jythonJarPath = getJythonJarPath();

        // 如果找到了 Jython 的 jar 文件路径，则执行 Python 代码
        if (jythonJarPath != null) {
            // 设置 Python 脚本路径
            String pythonScriptPath = "D:/CodePractice/litter_sort_system/model_code/main.py";
            // 设置图片路径
            String imagePath = "./paper162.jpg";

            // 创建 Python 解释器
            PythonInterpreter interpreter = new PythonInterpreter();

            // 将图片地址传递给 Python 程序
            interpreter.set("imagePath", new PyString(imagePath));

            // 执行 Python 代码
            interpreter.execfile(pythonScriptPath);
        }

        SpringApplication.run(IdentifyServer.class,args);
    }

    private static String getJythonJarPath() {
        try {
            File jarFile = new File(IdentifyServer.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            return jarFile.getParentFile().getAbsolutePath() + File.separator + "jython-standalone-2.7.0.jar";
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
