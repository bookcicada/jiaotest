package com.jiao.util;

import java.math.BigDecimal;

/**
 * @author 18067
 * @Date 2021/9/24 19:22
 */
public class DecimalUtils {
    public static boolean ge(BigDecimal n1, BigDecimal n2) {
        boolean flag = false;
        if (n1 == null || n2 == null) {
            throw new RuntimeException("参数为null");
        }
        if (n1.compareTo(n2) >= 0) {
            flag = true;
        }
        return flag;
    }
}
