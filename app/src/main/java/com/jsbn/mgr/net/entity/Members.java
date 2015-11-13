package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/9/16.
 */
public class Members extends BaseEntity {

    private Member data;

    public Members(Member data) {
        this.data = data;
    }

    public Member getData() {
        return data;
    }

    public void setData(Member data) {
        this.data = data;
    }
}
