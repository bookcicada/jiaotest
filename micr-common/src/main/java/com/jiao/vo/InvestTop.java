package com.jiao.vo;

import java.io.Serializable;

/**
 * @author 18067
 * @Date 2021/9/26 16:29
 */
public class InvestTop implements Serializable {
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public InvestTop(String phone, double money) {
        this.phone = phone;
        this.money = money;
    }

    private double money;



    public InvestTop() {
    }
}
