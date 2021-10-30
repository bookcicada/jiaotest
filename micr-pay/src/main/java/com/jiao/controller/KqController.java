package com.jiao.controller;

import com.jiao.service.KqService;
import com.jiao.vo.Result;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;


/**
 * @author 18067
 * @Date 2021/9/28 17:13
 */
@Controller
public class KqController {

    @Resource
    private KqService kqService;


    @RequestMapping("/kq/recvKqRequestForWeb")
    public String RecekqMicrWeb(String rechargeMoney, String rechargeNo, Integer userId, Model model) {
        //数据检查
        if (rechargeMoney != null && rechargeMoney != "" && rechargeNo != null && rechargeNo != "" && userId != null &&
                userId > 0) {
            //生成map数据
            Map<String, String> param = kqService.generateKqForData(userId, rechargeNo, rechargeMoney);
            //添加参数
            model.addAllAttributes(param);
            return "kqForm";
        }
        return "err";
    }

    @GetMapping("/kq/notifty")
    public void kqNotify(HttpServletRequest request, HttpServletResponse response) {
        try {
            StringBuffer buffer = new StringBuffer();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String name = parameterNames.nextElement();
                String value = request.getParameter(name);
                buffer.append(name).append("=").append(value).append("&");
            }
            System.out.println("接收的参数集合" + buffer.toString());
            //快钱异步通知处理方法
            kqService.handlerKQNotify(request);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            PrintWriter out = null;
            try {
                response.setContentType("text/html;charset=utf-8");
                out = response.getWriter();
                out.println("<result>1</result><redirecturl>http://localhost:8000/ylb/user/page/mycenter</redirecturl>");
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @GetMapping("/kq/queryOrderForWeb")
    @ResponseBody
    public Result queryOrderForWeb(String rechargeNo) {
        Result result = kqService.invokeKQOrderQuery(rechargeNo);
        return result;
    }
}
