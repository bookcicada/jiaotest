package com.jiao;

import org.junit.jupiter.api.Test;

/**
 * @author 18067
 * @Date 2021/10/16 8:53
 */
public class canTest {
    ZI zi=new ZI();
    Fu fu=new Fu();
    Fu fu1=new ZI();
    @Test
    public void canshuTest(){
        getsa(zi);
        getsa(fu);
        zi.say();
        fu.say();
        System.out.println(fu1.a);
        fu1.say();
    }




    public static void getsa(Fu fu){

        System.out.println("可以");
    }
}
