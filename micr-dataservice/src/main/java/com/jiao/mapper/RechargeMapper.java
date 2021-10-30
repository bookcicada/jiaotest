package com.jiao.mapper;


import com.jiao.model.Recharge;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RechargeMapper {

    List<Recharge> selectRecharge(@Param("uid") Integer uid, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    /************查询记录总数********************/
    int selectCountByUid(@Param("uid") Integer uid);

    int insertRecharge(Recharge recharge);

    /*********根据充值订单号,查询充值记录并上锁******************/
    Recharge selectRechargeNoLock(@Param("orderId") String orderId);

    /****************更新状态***************/
    int updateRechargeStatus(@Param("id") Integer id, @Param("rechargeStatusSucc") int rechargeStatusSucc);

    /*************通过充值订单号,更新状态****************/
    int updateRechargeByOrderID(@Param("orderId") String orderId, @Param("rechargeStatusSignFail") Integer rechargeStatusSignFail);
    /**************通过订单号,查询记录*****************/
    Recharge selectByRechargeNo(@Param("rechargeNo") String rechargeNo);
}
