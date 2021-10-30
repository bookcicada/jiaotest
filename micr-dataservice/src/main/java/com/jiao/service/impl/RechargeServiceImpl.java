package com.jiao.service.impl;

import com.jiao.contants.YLBcontants;
import com.jiao.mapper.FinanceAccountMapper;
import com.jiao.mapper.RechargeMapper;
import com.jiao.model.Recharge;
import com.jiao.util.YLBUtils;
import com.jiao.vo.CodeEnum;
import com.jiao.vo.Result;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.transaction.annotation.Transactional;
import service.RechargeService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/23 17:06
 */
@DubboService(interfaceClass = RechargeService.class, version = "1.0")
public class RechargeServiceImpl implements RechargeService {
    @Resource
    RechargeMapper rechargeMapper;
    @Resource
    FinanceAccountMapper financeAccountMapper;

    @Override
    public List<Recharge> queryListByUid(Integer uid, Integer pageNo, Integer pageSize) {
        List<Recharge> rechargeList = new ArrayList<>();
        if (uid != null && uid > 0) {
            pageNo = YLBUtils.defaultPageNo(pageNo);
            pageSize = YLBUtils.defaultPageSize(pageSize);
            int offSet = YLBUtils.offSet(pageNo, pageSize);
            rechargeList = rechargeMapper.selectRecharge(uid, offSet, pageSize);
        }

        return rechargeList;
    }

    @Override
    public int queryCountUid(Integer uid) {
        int rows = 0;
        if (uid != null && uid > 0) {
            rows = rechargeMapper.selectCountByUid(uid);
        }
        return rows;
    }

    /**
     * @param recharge
     * @return 创建充值记录
     */
    @Override
    public int addRecharge(Recharge recharge) {
        return rechargeMapper.insertRecharge(recharge);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized Result handlerRechargeNotify(String orderId, String payResult, String payAmount) {
        Result result = Result.fail();
        int rows = 0;
        //1.查询订单记录,判断订单是否处理过
        Recharge recharge = rechargeMapper.selectRechargeNoLock(orderId);
        if (recharge != null) {
            //2.判断是否处理过
            if (YLBcontants.PRODUCT_STATUS_SAIL == recharge.getRechargeStatus()) {
                //3.判断金额是否一致
                String fen = recharge.getRechargeMoney().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString();
                if (payAmount.equals(fen)) {
                    if ("10".equals(payResult)) {
                        //4.金额是一致的,给资金增加金额
                        rows = financeAccountMapper.updateMoneyRecharge(recharge.getUid(), recharge.getRechargeMoney());
                        if (rows < 1) {
                            throw new RuntimeException("充值异步通知,更新战虎资金失败");
                        }
                        //5.修改充值记录的状态
                        rows = rechargeMapper.updateRechargeStatus(recharge.getId(), YLBcontants.RECHARGE_STATUS_SUCC);
                        if (rows < 1) {
                            throw new RuntimeException("充值异步通知,更新充值记录表状态为成功失败");
                        }
                        result = Result.ok();
                        result.setEnum(CodeEnum.RC_RECHARGE_SUCCESS);
                    } else {
                        //充值失败
                        rows = rechargeMapper.updateRechargeStatus(recharge.getId(), YLBcontants.RECHARGE_STATUS_FAIL);
                        if (rows < 1) {
                            throw new RuntimeException("充值异步通知,更新充值记录表状态为失败失败");
                        }
                        result = Result.ok();
                        result.setEnum(CodeEnum.RC_RECHARGE_FAIL);
                    }


                } else {
                    //金额不一致
                    result.setEnum(CodeEnum.RC_RECHARGE_MONEY_DIFFER);
                }
            } else {
                result.setEnum(CodeEnum.RC_RECHARGE_FINISHED);
            }
        } else {
            //没有订单
            result.setEnum(CodeEnum.RC_NOT_EXISTS_RECHARGENO);
        }
        return result;
    }

    @Override
    public boolean modifyRechargeStatus(String orderId, Integer rechargeStatusSignFail) {
        int rows = rechargeMapper.updateRechargeByOrderID(orderId, rechargeStatusSignFail);

        return rows > 1;
    }

    @Override
    public Recharge queryForRechargeNo(String rechargeNo) {

        return rechargeMapper.selectByRechargeNo(rechargeNo);
    }
}
