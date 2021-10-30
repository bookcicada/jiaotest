package service;

import com.jiao.model.Recharge;
import com.jiao.vo.Result;

import java.util.List;

/**
 * 充值服务
 * @author 18067
 * @Date 2021/9/23 17:06
 */
public interface RechargeService {
    //用户的充值记录
    List<Recharge> queryListByUid(Integer uid,Integer pageNo,Integer pageSize);
    //记录总数
    int queryCountUid(Integer uid);

    int addRecharge(Recharge recharge);

    /**
     * 充值的异步通知
     * @param orderId 商家订单号
     * @param payResult 充值结果 10成功,11失败
     * @param payAmount 充值金额 分为单位
     * @return
     */
    Result handlerRechargeNotify(String orderId, String payResult, String payAmount);

    boolean modifyRechargeStatus(String orderId, Integer rechargeStatusSignFail);
    //查询充值记录
    Recharge queryForRechargeNo(String rechargeNo);

}
