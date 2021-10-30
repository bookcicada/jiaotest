package com.jiao.mapper;


import com.jiao.model.FinanceAccount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface FinanceAccountMapper {
    int insertAccount(FinanceAccount financeAccount);

    FinanceAccount selectByUid(@Param("uid") Integer uid);

    /**********查询上锁***************/
    FinanceAccount selectUidForUpdate(@Param("userId") Integer userId);

    /**********减少账户资金余额*************/
    Integer reduceAccountavailable(@Param("id") Integer id, @Param("bidMoney") BigDecimal bidMoney);

    int updateMoneyIncomeBack(@Param("uid") Integer uid, @Param("bidMoney") BigDecimal bidMoney, @Param("incomeMoney") BigDecimal incomeMoney);


    int updateMoneyRecharge(@Param("uid") Integer uid, @Param("rechargeMoney") BigDecimal rechargeMoney);

}
