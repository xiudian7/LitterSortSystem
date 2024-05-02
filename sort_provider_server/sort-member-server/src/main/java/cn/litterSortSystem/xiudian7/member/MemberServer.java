package cn.litterSortSystem.xiudian7.member;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication(scanBasePackages = "cn.litterSortSystem.xiudian7")//解决扫描不到RedisService问题
@MapperScan("cn.litterSortSystem.xiudian7.member.mapper")
@EnableDiscoveryClient
public class MemberServer {

        public static void main(String[] args) {
            SpringApplication.run(MemberServer.class,args);
        }

}
