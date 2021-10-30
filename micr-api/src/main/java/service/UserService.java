package service;

import com.jiao.model.User;

/**
 * @author 18067
 * @Date 2021/9/13 15:50
 */
public interface UserService {
    /**
     * @return 注册用户数量
     */
    Integer queryRegisterUsers();

    User queryUserByPhone(String phone);

    //注册
    User userRegister(String phone, String mima);

    /**
     * 根据手机号更新
     *
     * @param phone 手机号
     * @return 更新结果
     */
    boolean realName(String phone, String name, String idCard);

    /**
     * 用户登录
     *
     * @param phone         手机号
     * @param loginPassword 密码
     * @return 返回符合条件的user
     */
    User userLogin(String phone, String loginPassword);

    //查看登录错误次数
    Integer loginCount(String phone);

    /**
     * 记录登录错误次数
     */
    void loginCountIncr(String phone);
}
