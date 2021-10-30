package service;

/**
 * @author 18067
 * @Date 2021/9/17 10:07
 */

public interface SmsService {


    /**
     * @param phone 手机号
     * @param cmd 注册或登录
     * @return 成功
     */
    boolean handlerSmsService(String phone, String cmd);

    boolean checkCode(String phone, String code);

    /**
     * 删除redis中的验证码
     * @param key
     */
    public void deleteKey(String key);
}
