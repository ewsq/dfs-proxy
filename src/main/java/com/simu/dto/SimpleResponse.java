package com.simu.dto;

import java.beans.Transient;
import java.util.function.Supplier;

/**
 * @author DengrongGuan
 * @create 2018-02-12
 **/
public class SimpleResponse {
    private final int code;
    private final Object data;
    private final String exception;

    private SimpleResponse() {
        this.code = 0;
        this.data = "Success";
        this.exception = null;
    }

    private SimpleResponse(boolean success) {
        this.code = success ? 0 : -1;
        this.data = null;
        this.exception = null;
    }

    public SimpleResponse(int code, Object data) {
        this(code, data, (String)null);
    }

    public SimpleResponse(int code, Object data, String exception) {
        this.code = code;
        this.data = data;
        this.exception = exception;
    }

    public SimpleResponse(int code) {
        this.code = code;
        this.data = null;
        this.exception = null;
    }

    public static SimpleResponse checkNull(Object data) {
        return data == null ? new SimpleResponse(-3, (Object)null) : ok(data);
    }

    public static SimpleResponse ifTrue(boolean result, Supplier<?> data) {
        return result ? ok(data.get()) : new SimpleResponse(false);
    }

    public static SimpleResponse exception(Exception exception) {
        return new SimpleResponse(2500, exception.getMessage());
    }

    public static SimpleResponse error(Object data) {
        return new SimpleResponse(-1, data);
    }

    public static SimpleResponse ok(Object data) {
        return new SimpleResponse(0, data);
    }

    public static SimpleResponse checkBoolean(boolean result) {
        return new SimpleResponse(result);
    }

    public Object getData() {
        return this.data;
    }

    public int getCode() {
        return this.code;
    }

    @Transient
    public String getException() {
        return this.exception;
    }
}
