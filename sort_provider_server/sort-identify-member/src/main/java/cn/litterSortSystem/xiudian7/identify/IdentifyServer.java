package cn.litterSortSystem.xiudian7.identify;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "cn.litterSortSystem.xiudian7")
@MapperScan("cn.litterSortSystem.xiudian7.identify.mapper")
@EnableDiscoveryClient
public class IdentifyServer {
    public static void main(String[] args) {
        SpringApplication.run(IdentifyServer.class,args);
    }
}
