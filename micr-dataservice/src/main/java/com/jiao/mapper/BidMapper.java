package com.jiao.mapper;


import com.jiao.model.Bid;
import com.jiao.model.exit.PhoneBid;
import com.jiao.model.exit.ProudctNameBid;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BidMapper {
    //总计的投资金额
    BigDecimal selectSumInvestMone();
    //查询每个理财产品对应的投资
    List<PhoneBid> selectBidByLoanId(Integer loanId,Integer rows);
    //某个用户所有的投资记录
    List<ProudctNameBid> selectBidByUid(@Param("uid") Integer uid, @Param("offSet") Integer offSet, @Param("rows") Integer rows);
    //查询用户投资记录总数(分页)
    int selectBidCountByUID(@Param("uid") Integer uid);
    //插入投资记录
    int insertBid(Bid bid);

    List<Bid> selectBidListProductId(@Param("productId") Integer productId);
}
