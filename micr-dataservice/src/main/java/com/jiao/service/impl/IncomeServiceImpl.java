package com.jiao.service.impl;

import com.jiao.contants.YLBcontants;
import com.jiao.mapper.BidMapper;
import com.jiao.mapper.FinanceAccountMapper;
import com.jiao.mapper.IncomeMapper;
import com.jiao.mapper.ProductMapper;
import com.jiao.model.Bid;
import com.jiao.model.Income;
import com.jiao.model.Product;
import com.jiao.model.exit.UserIncome;
import com.jiao.util.YLBUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;
import service.IncomeService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/23 19:46
 */
@DubboService(interfaceClass = IncomeService.class, version = "1.0")
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private IncomeMapper incomeMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private BidMapper bidMapper;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    /**
     * @param uid      用户
     * @param pageNo   页数
     * @param pageSize 每页显示
     * @return 收益记录
     */
    @Override
    public List<UserIncome> queryIncomeListUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<UserIncome> incomeList = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = YLBUtils.defaultPageNo(pageNo);
            pageSize = YLBUtils.defaultPageSize(pageSize);
            int offSet = YLBUtils.offSet(pageNo, pageSize);
            incomeList = incomeMapper.selectIncomeByUID(uid, offSet, pageSize);
        }
        return incomeList;

    }

    /**
     * @param uid 用户id
     * @return 收益记录总数
     */
    @Override
    public int queryIncomeCount(Integer uid) {
        int counts = 0;
        if (uid != null && uid > 0) {
            counts = incomeMapper.selectIncomeCount(uid);
        }
        return counts;
    }

    //计算满标产品的收益计划
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void generateIncomePlan() {
        int rows = 0;
        Date curDate = new Date();//当前时间
        Date begin = DateUtils.truncate(DateUtils.addDays(curDate, -1), Calendar.DATE);
        Date end = DateUtils.truncate(curDate, Calendar.DATE);
        //查询昨天的满标产品,计算时间
        List<Product> productList = productMapper.selectBeforeManBiaoProduct(begin, end);
        //每个满标产品查询他的投资记录
        Date incomeDate = null;//到期时间
        BigDecimal dateRate = null;//日利率
        BigDecimal incomeMoney = null;// 利息
        BigDecimal cycle = null;//周期
        for (Product product : productList) {
            List<Bid> bidList = bidMapper.selectBidListProductId(product.getId());

            //计算日利率
            dateRate = product.getRate().divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP).divide(new BigDecimal("365"), 10, RoundingMode.HALF_UP);
            //计算周期
            cycle = new BigDecimal(product.getCycle());

            //遍历记录,计算收益
            for (Bid bid : bidList) {
                Date computerBeginDate = DateUtils.addDays(product.getProductFullTime(), 1);//满标时间
                //计算利息(投资金额*周期*利率)和到期时间(满标的第二天+周期)
                if (product.getProductType() == YLBcontants.PRODUCT_TYPE_XINSHOUBAO) {
                    incomeDate = DateUtils.addDays(computerBeginDate, product.getCycle());//到期时间
                    incomeMoney = bid.getBidMoney().multiply(cycle).multiply(dateRate);
                } else {
                    incomeDate = DateUtils.addMonths(computerBeginDate, product.getCycle());//到期时间
                    incomeMoney = bid.getBidMoney().multiply(cycle.multiply(new BigDecimal("30").multiply(dateRate)));
                }
                //创建收益记录
                Income income = new Income();
                income.setBidId(bid.getId());
                income.setBidMoney(bid.getBidMoney());
                income.setIncomeDate(incomeDate);//到期时间
                income.setIncomeMoney(incomeMoney);//利息
                income.setLoanId(product.getId());
                income.setUid(bid.getUid());
                income.setIncomeStatus(YLBcontants.INCOME_STAUTS_PLAN);
                incomeMapper.insertIncome(income);
            }
            //更新产品的状态为 满标已经生成收益计划
            rows = productMapper.updateStatus(product.getId(), YLBcontants.PRODUCT_STATUS_INCOME);
            if (rows < 1) {
                throw new RuntimeException("生成收益计划,更新产品状态失败");
            }

        }
    }


    /**
     * 满标产品的收益返还
     */
    @Override
    public void generateIncomeBack() {
        int rows;
        //查询符合条件的记录,昨天到期的并且没有返还的
        List<Income> incomeList = incomeMapper.selectExipreIncome();
        //返回收益
        for (Income income : incomeList) {
            //更新产品资金
           rows= financeAccountMapper.updateMoneyIncomeBack(income.getUid(),income.getBidMoney(),income.getIncomeMoney());
           if (rows<1){
               throw new RuntimeException(("定时任务,收益返还,更新资金表失败"));
           }
           //更新此收益记录为1(已返还)
          rows=  incomeMapper.updateStatusBack(income.getId());
           if (rows<1){
               throw new RuntimeException("定时任务,收益返还,更新收益记录状态异常");
           }
        }
    }
}
