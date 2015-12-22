package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/12/22.
 */
public class Base<T> {
    private T data;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Base(int code, T data) {

        this.code = code;
        this.data = data;
    }
}
