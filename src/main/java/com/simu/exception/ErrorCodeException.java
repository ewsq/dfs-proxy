package com.simu.exception;

import com.simu.constant.ResponseCodeEnum;

/**
 * @author DengrongGuan
 * @create 2018-02-09
 **/
public class ErrorCodeException extends RuntimeException{
    private int code;

    public ErrorCodeException(){
        super();
    }

    public ErrorCodeException(ResponseCodeEnum responseCodeEnum){
        super(responseCodeEnum.getMsg());
        this.code = responseCodeEnum.getCode();
    }

    public ErrorCodeException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
