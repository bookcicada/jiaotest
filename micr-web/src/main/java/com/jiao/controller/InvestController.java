package com.jiao.controller;

import com.jiao.contants.YLBkeys;
import com.jiao.model.User;
import com.jiao.model.exit.ProudctNameBid;
import com.jiao.util.YLBUtils;
import com.jiao.vo.CodeEnum;
import com.jiao.vo.PageInfo;
import com.jiao.vo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.List;

/**
 * 投资
 *
 * @author 18067
 * @Date 2021/9/23 15:57
 */
@Controller
public class InvestController extends BaseController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //查询所有投资记录
    @GetMapping("/invest/all")
    public String moreInvest(@RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize,
                             HttpSession session, Model model) {

        User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);


        pageNo = YLBUtils.defaultPageNo(pageNo);
        pageSize = YLBUtils.defaultPageSize(pageSize);
        List<ProudctNameBid> bidList = investService.queryProudctNameBid(user.getId(), pageNo, pageSize);
        //获取总记录数量,当前页,显示条数,总条数
        PageInfo pageInfo = new PageInfo(pageNo, pageSize, investService.queryBidCountUid(user.getId()));
        model.addAttribute("page", pageInfo);
        model.addAttribute("bidList", bidList);
        return "myInvest";
    }

    @PostMapping("/user/invest")
    @ResponseBody
    public Result userInvest(@RequestParam("productId") Integer productId, @RequestParam("bidMoney") BigDecimal bidMoney, HttpSession session) {
        Result result = Result.fail();
        //接收参数基本校验
        if (productId != null && productId > 0 && bidMoney != null && bidMoney.intValue() % 100 == 0 && bidMoney.intValue() >= 100) {
            User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
            if (StringUtils.isEmpty(user.getName())) {
                result.setEnum(CodeEnum.RC_NO_REALNAME);
            } else {
                result = investService.userInvest(user.getId(), productId, bidMoney);
                //redis排行榜
                if (result.isSuccess()){
stringRedisTemplate.opsForZSet().incrementScore(YLBkeys.INVSET_TOP_LIST, user.getPhone(), bidMoney.doubleValue());
                }
            }
        } else {
            result.setEnum(CodeEnum.RC_FORMAT_ERRCR);
        }
        return result;
    }
}
