package com.jiao.util;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author 18067
 * @Date 2021/9/14 14:35
 */
public class YLBUtils {
    /**
     * @param productType 产品类型
     * @return检查产品类型
     */
    public static boolean checkProductType(Integer productType) {
        boolean flag = false;
        Set<Integer> types = new HashSet<>();
        types.add(0);
        types.add(1);
        types.add(2);
        if (productType != null) {
            if (types.contains(productType)) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * @param pageNo 起始页
     * @return 起始页
     */
    public static int defaultPageNo(Integer pageNo) {
        if (pageNo == null || pageNo < 1) {
            pageNo = 1;
        }
        return pageNo;
    }

    /**
     * @param pageSize 每页显示数量
     *                 检查是否合规
     * @return每页显示数量
     */
    public static int defaultPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }
        return pageSize;
    }

    /**
     * @param pageNo   起始页
     * @param pageSize 每页显示数量
     * @return
     */
    public static int offSet(Integer pageNo, Integer pageSize) {

        return (pageNo - 1) * pageSize;
    }

    public static boolean checkFormatPhone(String phone) {
        boolean flag = false;
        if (phone != null) {
            flag = Pattern.matches("^1[1-9]\\d{9}$", phone);
        }
        return flag;
    }
}
