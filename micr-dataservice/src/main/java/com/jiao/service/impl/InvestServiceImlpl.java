package com.jiao.service.impl;

import com.jiao.contants.YLBcontants;
import com.jiao.contants.YLBkeys;
import com.jiao.mapper.BidMapper;
import com.jiao.mapper.FinanceAccountMapper;
import com.jiao.mapper.ProductMapper;
import com.jiao.model.Bid;
import com.jiao.model.FinanceAccount;
import com.jiao.model.Product;
import com.jiao.model.exit.ProudctNameBid;
import com.jiao.util.DecimalUtils;
import com.jiao.util.YLBUtils;
import com.jiao.vo.CodeEnum;
import com.jiao.vo.Result;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import service.BidService;
import service.InvestService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 投资
 *
 * @author 18067
 * @Date 2021/9/13 20:35
 */
@DubboService(interfaceClass = InvestService.class, version = "1.0")
public class InvestServiceImlpl implements InvestService {

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private BidMapper bidMapper;

    /**
     * 投资
     *
     * @param id        用户id
     * @param productId 产品id
     * @param bidMoney  投资金额
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result userInvest(Integer id, Integer productId, BigDecimal bidMoney) {
        Result result = Result.fail();
        Integer rows;
        //先查询金额,锁住
        FinanceAccount account = financeAccountMapper.selectUidForUpdate(id);
        if (account != null) {
            //比较金额
            if (DecimalUtils.ge(account.getAvailableMoney(), bidMoney)) {
                //判断产品金额
                Product product = productMapper.selectProductById(productId);
                if (product != null) {
                    //判断min,max,leftmoney,state
                    if (product.getProductStatus() == YLBcontants.PRODUCT_STATUS_SAIL) {
                        if (DecimalUtils.ge(bidMoney, product.getBidMinLimit()) && DecimalUtils.ge(product.getBidMaxLimit(), bidMoney) && DecimalUtils.ge(product.getLeftProductMoney(), bidMoney) && bidMoney.intValue() % 100 == 0) {
                            //1.扣除资金余额
                            rows = financeAccountMapper.reduceAccountavailable(id, bidMoney);
                            if (rows < 1) {
                                throw new RuntimeException("投资,更新资金余额失败");
                            }
                            //2.扣除产品投资金额
                            rows = productMapper.updateLeftMoney(productId, bidMoney);
                            if (rows < 1) {
                                throw new RuntimeException("投资.更新产品剩余可投资金额失败");
                            }
                            //3.生成投资记录
                            Bid bid = new Bid();
                            bid.setUid(id);
                            bid.setBidMoney(bidMoney);
                            bid.setLoanId(product.getId());
                            bid.setBidStatus(YLBcontants.BID_STATUS_SUCC);
                            bid.setBidTime(new Date());
                            rows = bidMapper.insertBid(bid);
                            if (rows < 1) {
                                throw new RuntimeException("投资,生成投资记录失败");
                            }
                            //4.判断产品是否满标
                            product = productMapper.selectProductById(productId);
                            if (product.getLeftProductMoney().intValue() == 0) {
                                //修改标志
                                rows = productMapper.updateStatusAndFullTime(product.getId());
                                if (rows < 1) {
                                    throw new RuntimeException("投资,更改满标标记失败");
                                }

                            }
                            result = Result.ok();

                        } else {
                            result.setEnum(CodeEnum.RC_INVEST_MONEY_ERROR);
                        }

                    } else {
                        result.setEnum(CodeEnum.RC_PRODUCT_NOT_SELLED);
                        //不可售
                    }
                } else {
                    result.setEnum(CodeEnum.RC_NOT_PRODUCT);
                    //无此产品
                }
            } else {
                result.setEnum(CodeEnum.RC_ACCOUNT_MONEY_ERROR);
                //余额不足
            }
        } else {
            result.setEnum(CodeEnum.RC_NONE_ACCOUNT);
            //没有此账户
        }
        return result;
    }


    /**
     * @return 总计的投资金额
     */
    @Override
    public BigDecimal querySumInvestMoney() {
        ValueOperations operations = redisTemplate.opsForValue();
        BigDecimal sumInvestMoney = (BigDecimal) operations.get(YLBkeys.INVEST_MONEY_SUM);
        if (sumInvestMoney == null) {
            synchronized (this) {
                if (operations.get(YLBkeys.INVEST_MONEY_SUM) == null) {
                    sumInvestMoney = bidMapper.selectSumInvestMone();
                    operations.set(YLBkeys.INVEST_MONEY_SUM, sumInvestMoney, 30, TimeUnit.MINUTES);
                }
            }
        }

        return sumInvestMoney;
    }

    @Override
    public List<ProudctNameBid> queryProudctNameBid(Integer uid, Integer pageNo, Integer pageSize) {
        List<ProudctNameBid> list = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = YLBUtils.defaultPageNo(pageNo);
            pageSize = YLBUtils.defaultPageSize(pageSize);
            int offSet = YLBUtils.offSet(pageNo, pageSize);
            list = bidMapper.selectBidByUid(uid, offSet, pageSize);
        }

        return list;
    }

    @Override
    public int queryBidCountUid(Integer uid) {
        return bidMapper.selectBidCountByUID(uid);
    }
}
