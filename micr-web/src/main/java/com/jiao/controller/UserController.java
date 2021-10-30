package com.jiao.controller;

import com.jiao.contants.YLBkeys;
import com.jiao.model.FinanceAccount;
import com.jiao.model.Recharge;
import com.jiao.model.User;
import com.jiao.model.exit.ProudctNameBid;
import com.jiao.model.exit.UserIncome;
import com.jiao.util.YLBUtils;
import com.jiao.vo.CodeEnum;
import com.jiao.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/15 16:26
 */
@Controller
public class UserController extends BaseController {
    //注册页面
    @GetMapping("/user/page/regist")
    public String pageUserRegist() {
        return "register";
    }

    //手机号是否注册
    @GetMapping("/user/regist/phone")
    @ResponseBody
    public Result hasRegistPhone(String phone) {
        Result result = Result.fail();
        if (YLBUtils.checkFormatPhone(phone)) {
            User user = userService.queryUserByPhone(phone);
            if (user == null) {
                result = Result.ok();
            } else {
                result.setEnum(CodeEnum.RC_EXITS_PHONE);
            }
        }
        return result;
    }


    //用户注册
    @PostMapping("/user/register")
    @ResponseBody
    public Result userRegister(@RequestParam("phone") String phone, @RequestParam("mima") String mima, @RequestParam("code") String code, HttpSession session) {
        Result ro = Result.fail();
        if (StringUtils.isAnyEmpty(phone, mima, code)) {
            ro.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        } else if (!YLBUtils.checkFormatPhone(phone)) {
            ro.setEnum(CodeEnum.RC_FORMAT_PHONE_ERROR);
        } else if (mima.length() != 32) {
            ro.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        } else if (!smsService.checkCode(phone, code)) {
            ro.setEnum(CodeEnum.RC_AUTHCODE_ERRODR);
        } else {
            //注册业务逻辑
            User user = userService.userRegister(phone, mima);
            if (user != null) {
                //注册成功
                ro = Result.ok();
                //把user存放到session
                session.setAttribute(YLBkeys.YLB_SESSION_USER, user);
                //删除验证码
                smsService.deleteKey(YLBkeys.SMS_REG_CODE + phone);
            } else {
                ro.setEnum(CodeEnum.RC_EXITS_PHONE);
            }
        }
        return ro;
    }

    //实名认证页面跳转
    @GetMapping("/user/page/realname")
    public String pageRealName(Model model, HttpSession session) {
        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        model.addAttribute("phone", user.getPhone());
        return "realName";
    }

    //实名认证
    @PostMapping("/user/realname")
    @ResponseBody
    public Result realname(@RequestParam("name") String name, @RequestParam("idcard") String idCard, @RequestParam("phone") String phone, HttpSession session) {

        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        Result result = Result.fail();
        if (StringUtils.isAnyEmpty(phone, idCard, name)) {
            result.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        } else if (!YLBUtils.checkFormatPhone(phone)) {
            result.setEnum(CodeEnum.RC_FORMAT_PHONE_ERROR);
        } else if (name.length() < 2) {
            result.setEnum(CodeEnum.RC_REAL_NAME);
        } else if (!phone.equals(user.getPhone())) {
            result.setEnum(CodeEnum.RC_PHONE_INVLIDATE);
        } else {


            boolean isOK = userService.realName(phone, name, idCard);
            if (isOK) {
                //给session中用户增加name信息
                user.setName(name);
                result = Result.ok();
            }
        }
        return result;
    }


    /**
     * 跳转登录界面
     */
    @GetMapping("/user/page/login")
    public String pageUserLogin(String returnUrl, Model model, HttpServletRequest request) {
        if (StringUtils.isEmpty(returnUrl)) {
            //协议+ip+端口号
            returnUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        }
        model.addAttribute("returnUrl", returnUrl);
        //注册用户数
        Integer registerUsers = userService.queryRegisterUsers();
        model.addAttribute("registerUsers", registerUsers);
        //成交金额
        BigDecimal sumInvestMoney = investService.querySumInvestMoney();
        model.addAttribute("sumInvestMoney", sumInvestMoney);
        //收益率平均值
        BigDecimal avgRate = productService.queryAvgRate();
        model.addAttribute("avgProductRate", avgRate);
        return "login";
    }

    //登录
    @PostMapping("/user/login")
    @ResponseBody
    public Result userLogin(@RequestParam("phone") String phone, @RequestParam("mima") String loginPassword, HttpSession session) {
        Result result = Result.fail();
        if (StringUtils.isAnyEmpty(phone, loginPassword)) {
            result.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        } else if (!YLBUtils.checkFormatPhone(phone)) {
            result.setEnum(CodeEnum.RC_FORMAT_PHONE_ERROR);
        } else if (loginPassword.length() != 32) {
            result.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        } else if (userService.loginCount(phone) > 2) {
            result.setEnum(CodeEnum.RC_LOGIN_COUNT);
        } else {
            //参数正确,调用数据库
            User user = userService.userLogin(phone, loginPassword);
            if (user != null) {
                //把登录成功的用户存到session
                session.setAttribute(YLBkeys.YLB_SESSION_USER, user);
                result = Result.ok();
            } else {
                //登陆失败锁定
                userService.loginCountIncr(phone);
            }
        }
        return result;
    }

    //用户中心
    @GetMapping("/user/page/mycenter")
    public String myCenter(Model model, HttpSession session) {
        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        FinanceAccount account = financeAccountService.queryAccount(user.getId());
        if (account != null) {
            model.addAttribute("accountMoney", account.getAvailableMoney());
        }
        //投资记录
        List<ProudctNameBid> bidList = investService.queryProudctNameBid(user.getId(), 1, 5);
        model.addAttribute("bidList", bidList);
        //充值记录
        List<Recharge> rechargeList = rechargeService.queryListByUid(user.getId(), 1, 5);
        model.addAttribute("rechargeList", rechargeList);
        //收益记录
        List<UserIncome> incomeList = incomeService.queryIncomeListUid(user.getId(), 1, 5);
        model.addAttribute("incomeList", incomeList);

        return "myCenter";
    }

    //查询资金
    @GetMapping("/user/account")
    @ResponseBody
    public Result getAccount(HttpSession session) {
        Result result = Result.fail();
        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        FinanceAccount account = financeAccountService.queryAccount(user.getId());
        if (account != null) {
            result = Result.ok();
            result.setData(account.getAvailableMoney().toString());
        }
        return result;
    }

    //退出
    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/index";
        //return "index";
    }
}
