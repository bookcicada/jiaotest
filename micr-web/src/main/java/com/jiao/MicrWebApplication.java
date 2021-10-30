package com.jiao;

import com.jiao.controller.BaseController;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class  MicrWebApplication extends BaseController {


    public static void main(String[] args) {
        SpringApplication.run(MicrWebApplication.class, args);
    }

}
