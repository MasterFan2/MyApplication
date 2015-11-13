package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/9/16.
 */
public class Schedule {
    private int scheduleId;
    private String scheduleDate;
    private int orderId;
    private int statusId;
    private String remark;

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Schedule(int scheduleId, String scheduleDate, int orderId, int statusId, String remark) {

        this.scheduleId = scheduleId;
        this.scheduleDate = scheduleDate;
        this.orderId = orderId;
        this.statusId = statusId;
        this.remark = remark;
    }
}
