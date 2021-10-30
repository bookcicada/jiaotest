package com.jiao;

import com.jiao.task.TaskList;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author 18067
 * @Date 2021/9/27 14:54
 */
@EnableScheduling
@SpringBootApplication
@EnableDubbo
public class TaskListApp {
    public static void main(String[] args) {
        ApplicationContext cfl = SpringApplication.run(TaskListApp.class, args);
       TaskList taskList= (TaskList) cfl.getBean("taskList");
       //taskList.invokeGenerateIncomePlan();
       //taskList.invokeGenerateIncomeBack();
    }
}
