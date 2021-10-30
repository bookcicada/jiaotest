package com.jiao.model.exit;

import com.jiao.model.Income;

/**
 * @author 18067
 * @Date 2021/9/23 19:31
 */
public class UserIncome extends Income {
    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
