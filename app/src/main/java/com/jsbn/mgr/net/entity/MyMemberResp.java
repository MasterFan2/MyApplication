package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/11/12.
 */
public class MyMemberResp {
    private Member data;
    private int code;

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MyMemberResp(Member data, int code) {

        this.data = data;
        this.code = code;
    }
}
