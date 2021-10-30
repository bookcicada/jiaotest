package com.jiao.service.impl;

import com.jiao.mapper.FinanceAccountMapper;
import com.jiao.model.FinanceAccount;
import org.apache.dubbo.config.annotation.DubboService;
import service.FinanceAccountService;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;

/**
 * @author 18067
 * @Date 2021/9/22 18:29
 */
@DubboService(interfaceClass = FinanceAccountService.class, version = "1.0")
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Resource
    FinanceAccountMapper financeAccountMapper;

    /**
     * @param uid 用户id
     * @return 账户资金
     */
    @Override
    public FinanceAccount queryAccount(Integer uid) {
        FinanceAccount account = null;
        if (uid != null && uid > 0) {
            account = financeAccountMapper.selectByUid(uid);
        }
        return account;
    }
}
