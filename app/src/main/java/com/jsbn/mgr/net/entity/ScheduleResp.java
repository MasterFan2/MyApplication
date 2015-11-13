package com.jsbn.mgr.net.entity;

import java.util.ArrayList;

/**
 * Created by 13510 on 2015/9/16.
 */
public class ScheduleResp  extends BaseEntity{
    private ArrayList<Schedule> data;

    public ArrayList<Schedule> getData() {
        return data;
    }

    public void setData(ArrayList<Schedule> data) {
        this.data = data;
    }

    public ScheduleResp(ArrayList<Schedule> data) {

        this.data = data;
    }
}
