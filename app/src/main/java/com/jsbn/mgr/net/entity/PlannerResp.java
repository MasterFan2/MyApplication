package com.jsbn.mgr.net.entity;

import java.util.List;

/**
 * 统筹师返回数据
 * Created by 13510 on 2015/11/11.
 */
public class PlannerResp {

    private List<Planner> data;
    private int code;

    public List<Planner> getData() {
        return data;
    }

    public void setData(List<Planner> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public PlannerResp(List<Planner> data, int code) {

        this.data = data;
        this.code = code;
    }
}
