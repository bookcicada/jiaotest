package com.jiao.controller;

import com.jiao.contants.YLBcontants;
import com.jiao.model.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.math.BigDecimal;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/13 15:34
 */
@Controller
public class IndexController extends BaseController {


    /******项目首页index*********/
    @GetMapping({"/index", "/", ""})
    public String index(Model model) {
        //调用远程服务
        Integer registerUsers = userService.queryRegisterUsers();
        model.addAttribute("registerUsers", registerUsers);
        //调用投资服务
        BigDecimal suminvestMoney = investService.querySumInvestMoney();
        model.addAttribute("suminvestMoney", suminvestMoney);
//调用收益率
        BigDecimal avgRate = productService.queryAvgRate();
        model.addAttribute("avgRate", avgRate);
        //新手宝产品
        List<Product> xinShouBaoList = productService.queryProductyPage(YLBcontants.PRODUCT_TYPE_XINSHOUBAO, 1, 1);
        model.addAttribute("xinShouBaoList", xinShouBaoList);
        //优选
        List<Product> youxuanBaoList = productService.queryProductyPage(YLBcontants.PRODUCT_TYPE_YOUXUAN, 1, 4);
        model.addAttribute("youxuanBaoList", youxuanBaoList);
        //散标
        List<Product> sanbiaoBaoList = productService.queryProductyPage(YLBcontants.PRODUCT_TYPE_SANBIAO, 1, 8);
        model.addAttribute("sanbiaoBaoList", sanbiaoBaoList);
        return "index";
    }


}
