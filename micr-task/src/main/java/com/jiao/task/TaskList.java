package com.jiao.task;

import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import service.IncomeService;

/**
 * @author 18067
 * @Date 2021/9/27 14:53
 */
@Component("taskList")
public class TaskList {
    @DubboReference(interfaceClass = IncomeService.class, version = "1.0")
    private IncomeService incomeService;

    /**
     * 调用dubbo生成收益计划
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void invokeGenerateIncomePlan() {
        System.out.println("开始执行收益计划方法调用");
        incomeService.generateIncomePlan();
        System.out.println("调用完毕");
    }

    /**
     * 调用dubbo生成收益计划
     */
    @Scheduled(cron = "0 0 1 * * * ?")
    public void invokeGenerateIncomeBack() {
        System.out.println("开始执行收益计划方法调用");
        incomeService.generateIncomeBack();
        System.out.println("调用完毕");
    }
}
