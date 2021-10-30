package com.jiao.contants;

/**
 * @author 18067
 * @Date 2021/9/14 17:58
 */
public class YLBcontants {
    /*******************产品类型**********************/
    //新手宝
    public static final int PRODUCT_TYPE_XINSHOUBAO=0;
    //优选
    public static final int PRODUCT_TYPE_YOUXUAN=1;
    //散标
    public static final int PRODUCT_TYPE_SANBIAO=2;
    //注册时,短信验证时间
    public static final int SMS_REG_CODE_TIME = 3;
    /**************产品状态**************************/
    //未满标
    public static final int PRODUCT_STATUS_SAIL=0;
    //产品满标
    public static final int PRODUCT_STATUS_SAILED=1;
    //满标生成收益计划
    public static final int PRODUCT_STATUS_INCOME=2;
    /*****************投资表状态************************/
    //投资成功
    public static final int BID_STATUS_SUCC=1;
    //投资失败
    public static final int BID_STATUS_FAIL=2;
    /************收益表的状态*************************/
    //收益未返还
    public static final int INCOME_STAUTS_PLAN=0;
    //收益返还
    public static final int INCOME_STAUTS_BACK=1;
    /************充值表状态*******************/
    //充值中 0
    public static final int RECHARGE_STATUS_PROCESSING=0;
    //成功1
    public static final int RECHARGE_STATUS_SUCC=1;
    //失败2
    public static final int RECHARGE_STATUS_FAIL=2;
    //签名错误,,验签失败
    public static final Integer RECHARGE_STATUS_SIGN_FAIL=3;
}
