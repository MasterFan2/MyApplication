package com.jsbn.mgr.net.entity;

import java.util.ArrayList;

/**
 * Created by 13510 on 2015/11/10.
 */
public class MemberResp {
    private ArrayList<Member> data;
    private int code;

    public ArrayList<Member> getData() {
        return data;
    }

    public void setData(ArrayList<Member> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MemberResp(ArrayList<Member> data, int code) {

        this.data = data;
        this.code = code;
    }
}
