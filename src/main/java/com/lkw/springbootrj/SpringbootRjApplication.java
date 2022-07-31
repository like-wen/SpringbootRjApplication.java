package com.lkw.springbootrj;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@ServletComponentScan
@SpringBootApplication
public class SpringbootRjApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootRjApplication.class, args);
        log.info("启动成功");
    }

}
