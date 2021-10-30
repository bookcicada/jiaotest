package com.jiao.controller;

import com.jiao.util.YLBUtils;
import com.jiao.vo.CodeEnum;
import com.jiao.vo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author 18067
 * @Date 2021/9/17 10:06
 */
@RestController
public class SmSController extends BaseController {

    //发送短信
    @PostMapping("/sms/authcode")
    public Result<String> sendSms(String phone, String cmd) {
        Result<String> ro = Result.fail();
        if (YLBUtils.checkFormatPhone(phone)) {
            //发送短信
            boolean result = smsService.handlerSmsService(phone, cmd);
            if (result){
                ro=Result.ok();
            }else {
                ro.setEnum(CodeEnum.RC_FORMAT_ERRCR);
            }
        }
        return ro;
    }
}
