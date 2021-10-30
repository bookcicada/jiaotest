package com.jiao;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author 18067
 * @Date 2021/9/16 14:38
 */
public class MyTest {
    @Test
    public void phoneText() {
        String phone = "13645648966";
        boolean flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        System.out.println(flag);
        boolean fa = phone.matches("^1[1-9]\\d{9}$");
        System.out.println(fa);
    }
    @Test
    public void test02(){
        String str="[盈利宝]您的验证码是:%s,请勿泄露,检验码是%s,数字是%d";
        String ma = String.format(str, "456213","asfw",55);
        System.out.println(ma);
    }
    @Test
    public void test03(){
        String a="";
        System.out.println(a.equals(""));
        System.out.println(a=="");
        System.out.println(!a.equals(""));
    }
}
