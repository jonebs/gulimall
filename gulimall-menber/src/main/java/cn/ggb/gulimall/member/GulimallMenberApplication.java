package cn.ggb.gulimall.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients(basePackages = "cn.ggb.gulimall.member.feign")
@SpringBootApplication
public class GulimallMenberApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallMenberApplication.class, args);
    }

}
