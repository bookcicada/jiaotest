package com.jiao.controller;

import com.jiao.contants.YLBkeys;
import com.jiao.model.User;
import com.jiao.model.exit.UserIncome;
import com.jiao.vo.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/23 19:25
 */
@Controller
public class IncomeController extends BaseController {

    @GetMapping("/income/all")
    public String myIncome(Model model, HttpSession session,
                           @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "6") Integer pageSize) {
        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        List<UserIncome> incomeList = incomeService.queryIncomeListUid(user.getId(), pageNo, pageSize);
        model.addAttribute("incomeList", incomeList);
        int counts = incomeService.queryIncomeCount(user.getId());
        PageInfo pageInfo = new PageInfo(pageNo, pageSize, counts);
        model.addAttribute("page", pageInfo);
        return "myIncome";
    }
}
