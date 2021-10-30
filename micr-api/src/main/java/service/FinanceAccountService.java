package service;

import com.jiao.model.FinanceAccount;

/**
 * @author 18067
 * @Date 2021/9/22 18:28
 */
public interface FinanceAccountService {
    FinanceAccount queryAccount(Integer uid);
}
