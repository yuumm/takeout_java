package com.lxz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;


// @Slf4j注解可以用来输出日志
@Slf4j
@SpringBootApplication
// 下方的注解会去扫描WebFilter这类注解，从而创建filter，才能正常使用
@ServletComponentScan
// 因为调用了Transactional注解，因此需要下述注解才能生效
@EnableTransactionManagement
public class ReggieTakeOutApplication {

    public static void main(String[] args) {

        SpringApplication.run(ReggieTakeOutApplication.class, args);
        log.info("项目启动成功...");
    }

}
