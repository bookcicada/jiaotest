package com.jiao.mapper;


import com.jiao.model.Income;
import com.jiao.model.exit.UserIncome;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IncomeMapper {
    /*****************查看收益****************/
    List<UserIncome> selectIncomeByUID(@Param("uid") Integer uid, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);

    int selectIncomeCount(@Param("uid") Integer uid);

    /***********添加收益记录*************/
    int insertIncome(Income income);

    /************执行收益返回记录************************/
    List<Income> selectExipreIncome();

    /****************更新收益返回记录状态为已返还******************/
    int updateStatusBack(@Param("id") Integer id);
}
