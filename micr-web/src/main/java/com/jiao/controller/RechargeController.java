package com.jiao.controller;

import com.alibaba.fastjson.JSONObject;
import com.jiao.contants.YLBcontants;
import com.jiao.contants.YLBkeys;
import com.jiao.model.Recharge;
import com.jiao.model.User;
import com.jiao.util.HttpClientUtils;
import com.jiao.util.YLBUtils;
import com.jiao.vo.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 18067
 * @Date 2021/9/23 17:01
 */
@Controller
public class RechargeController extends BaseController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${micrPay.kqUrl}")
    private String micrPayKwUrl;
    @Value("${micrPay.kqOrderUrl}")
    private String micrPayKQOrderQueryUrl;

    //进入充值界面
    @GetMapping("/recharge/page/toRecharge")
    public String toRecharge(Model model) {
        //生成充值的订单号,时间戳+唯一自增值
        String orderId = generateNewOrderId();
        model.addAttribute("rechargeNo", orderId);
        //存放到redis
        stringRedisTemplate.opsForValue().set(YLBkeys.RECHARGE_ORDERID_SEQ + orderId, orderId);
        model.addAttribute("kqUrl", micrPayKwUrl);
        return "toRecharge";
    }

    private String generateNewOrderId() {
        return DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS") + stringRedisTemplate.opsForValue().increment(YLBkeys.RECHARGE_ORDERID_SEQ);
    }

    //查询充值结果
    @GetMapping("/recharge/result")
    public String rechargeReult(Model model, String rechargeNo, String kqUrl) {
        if (StringUtils.isAnyEmpty(rechargeNo, kqUrl)) {
            model.addAttribute("trade_msg", "充值数据有误");
            return "toRechargeBack.html";
        } else {
            Recharge recharge = rechargeService.queryForRechargeNo(rechargeNo);
            if (recharge != null) {
                if (recharge.getRechargeStatus() == YLBcontants.RECHARGE_STATUS_PROCESSING) {
                    //没有充值结果,调用快钱的查询接口
                    String url = micrPayKQOrderQueryUrl;
                    Map<String, String> map = new HashMap<>();
                    map.put("rechargeNo", rechargeNo);
                    try {
                        String result = HttpClientUtils.doGet(url, map);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getBooleanValue("success")) {
                            //充值处理完毕
                            return "forward:/user/page/mycenter";
                        } else {
                            //充值处理失败
                            model.addAttribute("trade_msg", jsonObject.getString("msg"));
                            return "toRechargeBack";
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            //重新充值
            model.addAttribute("rechargeNo", rechargeNo);
            model.addAttribute("kqUrl", kqUrl);
        }

        //暂时到首页吧
        return "toRecharge";
    }


    @GetMapping("/recharge/all")
    public String returnRecharge(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "6") Integer pageSize,
                                 HttpSession session,
                                 Model model) {
        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
        pageNo = YLBUtils.defaultPageNo(pageNo);
        pageSize = YLBUtils.defaultPageSize(pageSize);
        List<Recharge> rechargeList = rechargeService.queryListByUid(user.getId(), pageNo, pageSize);
        model.addAttribute("rechargeList", rechargeList);
        //记录数量
        int rows = rechargeService.queryCountUid(user.getId());
        PageInfo pageInfo = new PageInfo(pageNo, pageSize, rows);
        model.addAttribute("page", pageInfo);
        return "myRecharge";
    }

}
