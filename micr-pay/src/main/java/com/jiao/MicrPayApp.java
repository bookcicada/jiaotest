package com.jiao;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author 18067
 * @Date 2021/9/28 18:12
 */
@SpringBootApplication
@EnableDubbo
public class MicrPayApp {
    public static void main(String[] args) {
        SpringApplication.run(MicrPayApp.class, args);
    }
}
