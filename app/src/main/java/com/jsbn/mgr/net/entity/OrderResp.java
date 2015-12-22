package com.jsbn.mgr.net.entity;

import java.util.List;

/**
 * Created by 13510 on 2015/11/24.
 */
public class OrderResp {
    private List<Order> data;
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }

    public OrderResp(int code, List<Order> data) {

        this.code = code;
        this.data = data;
    }
}
