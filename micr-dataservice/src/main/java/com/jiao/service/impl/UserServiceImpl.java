package com.jiao.service.impl;

import com.jiao.contants.YLBkeys;
import com.jiao.mapper.FinanceAccountMapper;
import com.jiao.mapper.UserMapper;
import com.jiao.model.FinanceAccount;
import com.jiao.model.User;
import com.jiao.util.YLBUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import service.UserService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author 18067
 * @Date 2021/9/13 15:52
 */
@DubboService(interfaceClass = UserService.class, version = "1.0")
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisTemplate redisTemplate;
    @Value("${mima.salt")
    private String salt;
    @Resource
    private FinanceAccountMapper financeAccountMapper;

    /**
     * @return 注册用户总数
     * 从redis中查
     */
    @Override
    public Integer queryRegisterUsers() {
        //1.从redis获取数据
        ValueOperations operations = redisTemplate.opsForValue();
        Integer registerUsers = (Integer) operations.get(YLBkeys.USER_REGISTER_AMOUNT);
        if (registerUsers == null) {
            synchronized (this) {
                if (operations.get(YLBkeys.USER_REGISTER_AMOUNT) == null) {
                    registerUsers = userMapper.selectRegisterUsers();
                    operations.set(YLBkeys.USER_REGISTER_AMOUNT, registerUsers, 30, TimeUnit.MINUTES);
                }
            }
        }

        return registerUsers;
    }

    @Override
    public User queryUserByPhone(String phone) {
        User user = null;
        if (YLBUtils.checkFormatPhone(phone)) {
            //调用数据库的查询
            user = userMapper.selectUserByPhone(phone);
        }
        return user;
    }

    /**
     * @param phone 手机号
     * @param mima  客户端传递过来的md5加密后的密码值
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public User userRegister(String phone, String mima) {
        //查询手机号是否注册
        User user = userMapper.selectUserByPhone(phone);
        if (user == null) {
            user = new User();
            user.setAddTime(new Date());
            user.setPhone(phone);
            mima = DigestUtils.md5Hex(mima + salt);
            user.setLoginPassword(mima);
            userMapper.insertUserReturnId(user);
            //添加financeAccountMapper
            FinanceAccount financeAccount = new FinanceAccount();
            financeAccount.setUid(user.getId());
            financeAccount.setAvailableMoney(new BigDecimal(888));
            financeAccountMapper.insertAccount(financeAccount);
        } else {
            //将user赋值为空,区分是否注册成功
            user = null;
        }
        return user;
    }

    @Override
    public boolean realName(String phone, String name, String idCard) {
        int rows = userMapper.updateUserByPhone(phone, idCard, name);
//如果rows>0,则true
        return rows > 0;
    }

    @Override
    public User userLogin(String phone, String loginPassword) {
        User user = null;
        if (YLBUtils.checkFormatPhone(phone) && loginPassword != null) {
            //调用数据库的操作
            String pwd = DigestUtils.md5Hex(loginPassword + salt);
            user = userMapper.selectUserLogin(phone, pwd);
            if (user != null) {
                //更新登录时间
                userMapper.updateLoginTime(user.getId(), new Date());
            }
        }
        return user;
    }


    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Integer loginCount(String phone) {
        ValueOperations ops = stringRedisTemplate.opsForValue();
        String count = (String) ops.get("loginCount_" + phone);
        if (count == null) {
            count = "0";
        }
        int anInt = Integer.parseInt(count);
        return anInt;
    }

    //次数加一
    @Override
    public void loginCountIncr(String phone) {
        ValueOperations ops = stringRedisTemplate.opsForValue();
        ops.increment("loginCount_" + phone);
        Date date = new Date();
        redisTemplate.expire("loginCount_" + phone,getNowToNextDaySeconds() , TimeUnit.SECONDS);
    }

    /**
     * 计算第二天凌晨与当前时间的时间差秒数
     * @param
     * @return java.lang.Long
     * @author shy
     * @date 2021/3/12 18:10
     */
    public static Long getNowToNextDaySeconds() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis() - System.currentTimeMillis()) / 1000;
    }


}
