package com.jiao.controller;

import org.apache.dubbo.config.annotation.DubboReference;
import service.*;

/**
 * @author 18067
 * @Date 2021/9/13 21:03
 */
public class BaseController {
    //注入用户服务
    @DubboReference(interfaceClass = UserService.class, version = "1.0")
    protected UserService userService;
    //引用投资服务
    @DubboReference(interfaceClass = InvestService.class, version = "1.0")
    protected InvestService investService;
    //收益率吧平均数
    @DubboReference(interfaceClass = ProductService.class, version = "1.0")
    protected ProductService productService;
    @DubboReference(interfaceClass = BidService.class, version = "1.0")
    protected BidService bidService;
    @DubboReference(interfaceClass = SmsService.class, version = "1.0")
    protected SmsService smsService;
    @DubboReference(interfaceClass = FinanceAccountService.class, version = "1.0")
    protected FinanceAccountService financeAccountService;
    //充值记录
    @DubboReference(interfaceClass = RechargeService.class, version = "1.0")
    protected RechargeService rechargeService;
    //收益服务
    @DubboReference(interfaceClass = IncomeService.class,version = "1.0")
    protected IncomeService incomeService;
}
