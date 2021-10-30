package com.jiao.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户资金表
 */
public class FinanceAccount implements Serializable {
    private static final long serialVersionUID = -2876142388306991958L;
    private Integer id;

    private Integer uid;

    private BigDecimal availableMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public BigDecimal getAvailableMoney() {
        return availableMoney;
    }

    public void setAvailableMoney(BigDecimal availableMoney) {
        this.availableMoney = availableMoney;
    }
}
