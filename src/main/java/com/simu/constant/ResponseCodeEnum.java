package com.simu.constant;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public enum ResponseCodeEnum {
    BUCKET_NOT_EXIST(3000,"BUCKET_NOT_EXIST"),
    AUTH_SIGNATURE_EXPIRED(4000,"AUTH_SIGNATURE_EXPIRED"),
    AUTH_ACCESS_ID_NOT_MATCH(4001,"AUTH_ACCESS_ID_NOT_MATCH"),
    AUTH_SIGNATURE_NOT_MATCH(4002,"AUTH_SIGNATURE_NOT_MATCH");

    private final int code;
    private final String msg;
    ResponseCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode(){
        return this.code;
    }

    public String getMsg(){
        return this.msg;
    }
}
