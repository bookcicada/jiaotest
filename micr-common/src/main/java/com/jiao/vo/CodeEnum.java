package com.jiao.vo;

/**
 * @author 18067
 * @Date 2021/9/16 14:58
 */
public enum CodeEnum {
    RC_SUCC(0,"成功"),
    RC_FAIL(1,"未知错误"),
    RC_FORMAT_ERRCR(2,"格式错误"),
    RC_EXITS_PHONE(3,"请更换手机号,改手机号已经注册过了"),
    RC_FORMAT_PHONE_ERROR(4,"手机号格式不正确"),
    RC_AUTHCODE_ERRODR(5,"验证码不正确"),
    RC_REAL_NAME(6,"姓名不正确"),
    RC_PHONE_INVLIDATE(7,"认证手机号不可用"),
    RC_LOGIN_COUNT(8,"今日登录错误次数不足"),
    RC_NO_REALNAME(9,"必须要先实名认证"),
    RC_INVEST_MONEY_ERROR(10,"投资金额不满足"),
    RC_PRODUCT_NOT_SELLED(11,"不可售"),
    RC_NOT_PRODUCT(12,"无此产品"),
    RC_ACCOUNT_MONEY_ERROR(13,"余额不足"),
    RC_NONE_ACCOUNT(14,"没有此账户"),
    RC_NOT_EXISTS_RECHARGENO(15,"没有此充值记录"),
    RC_RECHARGE_FINISHED(16,"充值记录已经处理完成了"),
    RC_RECHARGE_MONEY_DIFFER(17,"充值金额不一致"),
    RC_RECHARGE_SUCCESS(18,"充值全部成功"),
    RC_RECHARGE_FAIL(19,"充值全部失败"),
    RC_RECHARGE_SIGN_FAIL(20,"验签失败"),
    ;



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    //错误码
    private int code;
    //错误信息
    private String msg;


    private CodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }






}
