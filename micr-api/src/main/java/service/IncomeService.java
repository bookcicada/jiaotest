package service;

import com.jiao.model.exit.UserIncome;

import java.util.List;

/**
 * 收益服务
 * @author 18067
 * @Date 2021/9/23 19:30
 */

public interface IncomeService {
    //获取用户的所有收益记录
    List<UserIncome> queryIncomeListUid(Integer uid,Integer pageNo,Integer pageSize);
    //获取所有用户的收益记录数
    int queryIncomeCount(Integer uid);
    //计算满标产品的收益计划
    void generateIncomePlan();
    //计算收益的返还
    void generateIncomeBack();
}
