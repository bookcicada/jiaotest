package com.jiao.controller;

import com.jiao.contants.YLBkeys;
import com.jiao.model.FinanceAccount;
import com.jiao.model.Product;
import com.jiao.model.User;
import com.jiao.model.exit.PhoneBid;
import com.jiao.util.YLBUtils;
import com.jiao.vo.InvestTop;
import com.jiao.vo.PageInfo;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestParam;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author 18067
 * @Date 2021/9/14 20:17
 */
@Controller
public class ProductController extends BaseController {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    /************产品详情***********************/
    @GetMapping("/product/info")
    public String productInfo(Integer pid, Model model, HttpSession session) {
        if (pid != null && pid > 0) {
            //查询用户的资金,从session中获取登录的user,在获取userid
            User user = (User) session.getAttribute(YLBkeys.YLB_SESSION_USER);
            if (user != null) {
                FinanceAccount account = financeAccountService.queryAccount(user.getId());
                if (account != null) {
                    model.addAttribute("accountMoney", account.getAvailableMoney());
                } else {
                    model.addAttribute("accountMoney", 0);
                }
            }
            //获得产品详情
            Product product = productService.queryProductById(pid);
            model.addAttribute("product", product);
            //获得投资记录
            List<PhoneBid> bidList = bidService.queryBidByLoanId(pid);
            model.addAttribute("bidList", bidList);
            return "productInfo";
        } else {
            model.addAttribute("msg", "无此产品");
            return "err";
        }

    }


    /*********分类查询产品***************/
    @GetMapping("/product/list")
    public String productList(Model model,
                              @RequestParam("ptype") Integer ptype,
                              @RequestParam(value = "pageNo", required = false, defaultValue = "1") Integer pageNo,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "9") Integer pageSize) {
        List<Product> productList = new ArrayList<>();
        if (YLBUtils.checkProductType(ptype)) {
            pageNo = YLBUtils.defaultPageNo(pageNo);
            pageSize = YLBUtils.defaultPageSize(pageSize);
            //获取产品数据
            productList = productService.queryProductyPage(ptype, pageNo, pageSize);
            //某类型的总记录数量
            Integer totalRecords = productService.queryTotalRecordProductType(ptype);
            PageInfo pageInfo = new PageInfo(pageNo, pageSize, totalRecords);
            //@todo 投资排行榜
            List<InvestTop> investTops = new ArrayList<>();
            Set<ZSetOperations.TypedTuple<String>> scores = stringRedisTemplate.opsForZSet().reverseRangeWithScores(YLBkeys.INVSET_TOP_LIST, 0, 5);
            scores.forEach(s -> {
                investTops.add(new InvestTop(s.getValue(), s.getScore()));
            });
            model.addAttribute("investTops", investTops);
            model.addAttribute("productList", productList);
            model.addAttribute("productType", ptype);
            model.addAttribute("pageInfo", pageInfo);
        } else {
            model.addAttribute("msg", "无此类型的理财产品");
            return "err";
        }
        return "productList";
    }
}
