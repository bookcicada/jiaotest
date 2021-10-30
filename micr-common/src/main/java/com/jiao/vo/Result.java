package com.jiao.vo;

import java.io.Serializable;

/**
 * @author 18067
 * @Date 2021/9/16 14:51
 * ajax请求返回结果类
 */
public class Result<T> implements Serializable {
    //请求是否成功
    private boolean success;
    //错误码
    private int code;
    //错误消息
    private String msg;
    //数据
    private T data;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public void setEnum(CodeEnum codeEnum) {
        this.code = codeEnum.getCode();
        this.msg = codeEnum.getMsg();
    }

    public static Result ok() {

        Result<String> result = new Result<>();
        result.setSuccess(true);
        result.setEnum(CodeEnum.RC_SUCC);
        result.setData("");
        return result;
    }

    public static Result fail() {

        Result<String> result = new Result<>();
        result.setSuccess(false);
        result.setEnum(CodeEnum.RC_FAIL);
        result.setData("");
        return result;
    }
}
