package service;

import com.jiao.model.exit.ProudctNameBid;
import com.jiao.vo.Result;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 18067
 * @Date 2021/9/13 20:34
 * 投资
 */
public interface InvestService {
    /**
     * @return 总计投资的金额
     */
    BigDecimal querySumInvestMoney();

    /**
     * @param uid      用户id
     * @param pageNo   页数
     * @param pageSize 每页显示
     * @return 用户的投资记录
     */
    List<ProudctNameBid> queryProudctNameBid(Integer uid, Integer pageNo, Integer pageSize);

    /**
     * 查询用户总计的投资数
     */
    int queryBidCountUid(Integer uid);

    Result userInvest(Integer id, Integer productId, BigDecimal bidMoney);
}
