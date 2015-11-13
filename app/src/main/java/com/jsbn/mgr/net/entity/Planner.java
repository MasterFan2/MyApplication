package com.jsbn.mgr.net.entity;

/**
 * 统筹师
 * Created by 13510 on 2015/11/11.
 */
public class Planner {
    private String lockDateFrom;
    private int tcsPersonId;
    private int scheduleId;
    private String tcsPrepareDate;
    private String remark;
    private String empNo;
    private String lockDate;
    private int statusId;
    private String groupId;
    private String tcsPersonName;
    private String name;
    private int personId;
    private String gender;
    private String skillTypeName;
    private String headUrl;

    public String getLockDateFrom() {
        return lockDateFrom;
    }

    public void setLockDateFrom(String lockDateFrom) {
        this.lockDateFrom = lockDateFrom;
    }

    public int getTcsPersonId() {
        return tcsPersonId;
    }

    public void setTcsPersonId(int tcsPersonId) {
        this.tcsPersonId = tcsPersonId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTcsPrepareDate() {
        return tcsPrepareDate;
    }

    public void setTcsPrepareDate(String tcsPrepareDate) {
        this.tcsPrepareDate = tcsPrepareDate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public String getLockDate() {
        return lockDate;
    }

    public void setLockDate(String lockDate) {
        this.lockDate = lockDate;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTcsPersonName() {
        return tcsPersonName;
    }

    public void setTcsPersonName(String tcsPersonName) {
        this.tcsPersonName = tcsPersonName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSkillTypeName() {
        return skillTypeName;
    }

    public void setSkillTypeName(String skillTypeName) {
        this.skillTypeName = skillTypeName;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public Planner(String lockDateFrom, int tcsPersonId, int scheduleId, String tcsPrepareDate, String remark, String empNo, String lockDate, int statusId, String groupId, String tcsPersonName, String name, int personId, String gender, String skillTypeName, String headUrl) {

        this.lockDateFrom = lockDateFrom;
        this.tcsPersonId = tcsPersonId;
        this.scheduleId = scheduleId;
        this.tcsPrepareDate = tcsPrepareDate;
        this.remark = remark;
        this.empNo = empNo;
        this.lockDate = lockDate;
        this.statusId = statusId;
        this.groupId = groupId;
        this.tcsPersonName = tcsPersonName;
        this.name = name;
        this.personId = personId;
        this.gender = gender;
        this.skillTypeName = skillTypeName;
        this.headUrl = headUrl;
    }
}
