package com.jiao.mapper;

import com.jiao.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface UserMapper {
    /**********查看多少用户***************/
    Integer selectRegisterUsers();

    /**********根据手机号查找yser*******************/
    User selectUserByPhone(@Param("phone") String phone);

    /*************注册用户,并获取主键id值******************/
    int insertUserReturnId(User user);

    /*****************更新实名认证信息**************/
    int updateUserByPhone(@Param("phone") String phone, @Param("idCard") String idCard, @Param("name") String name);

    /***********登录***************************/
    User selectUserLogin(@Param("phone") String phone, @Param("pwd") String pwd);

    /***********更新登录时间***************************/
    int updateLoginTime(@Param("id") Integer id, @Param("loginDate") Date date);
}
