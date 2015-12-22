package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/11/24.
 */
public class Order {
    private String zcrPrepareDate;
    private String zcrEmpName;
    private int zcrSkillPrice;
    private int zcrSkillOriginalPrice;
    private int zcrStatus;
    private int zcrScheduleId;
    private String hzsPrepareDate;
    private String hzsEmpName;
    private int hzsSkillPrice;
    private int hzsSkillOriginalPrice;
    private int hzsStatus;
    private int hzsScheduleId;
    private String sysPrepareDate;
    private String sysEmpName;
    private int sysSkillPrice;
    private int sysSkillOriginalPrice;
    private int sysStatus;
    private int sysScheduleId;
    private String sxsPrepareDate;
    private String sxsEmpName;
    private int sxsSkillPrice;
    private int sxsSkillOriginalPrice;
    private int sxsStatus;
    private int sxsScheduleId;

    public Order(String hzsEmpName, String hzsPrepareDate, int hzsScheduleId, int hzsSkillOriginalPrice, int hzsSkillPrice, int hzsStatus, String sxsEmpName, String sxsPrepareDate, int sxsScheduleId, int sxsSkillOriginalPrice, int sxsSkillPrice, int sxsStatus, String sysEmpName, String sysPrepareDate, int sysScheduleId, int sysSkillOriginalPrice, int sysSkillPrice, int sysStatus, String zcrEmpName, String zcrPrepareDate, int zcrScheduleId, int zcrSkillOriginalPrice, int zcrSkillPrice, int zcrStatus) {
        this.hzsEmpName = hzsEmpName;
        this.hzsPrepareDate = hzsPrepareDate;
        this.hzsScheduleId = hzsScheduleId;
        this.hzsSkillOriginalPrice = hzsSkillOriginalPrice;
        this.hzsSkillPrice = hzsSkillPrice;
        this.hzsStatus = hzsStatus;
        this.sxsEmpName = sxsEmpName;
        this.sxsPrepareDate = sxsPrepareDate;
        this.sxsScheduleId = sxsScheduleId;
        this.sxsSkillOriginalPrice = sxsSkillOriginalPrice;
        this.sxsSkillPrice = sxsSkillPrice;
        this.sxsStatus = sxsStatus;
        this.sysEmpName = sysEmpName;
        this.sysPrepareDate = sysPrepareDate;
        this.sysScheduleId = sysScheduleId;
        this.sysSkillOriginalPrice = sysSkillOriginalPrice;
        this.sysSkillPrice = sysSkillPrice;
        this.sysStatus = sysStatus;
        this.zcrEmpName = zcrEmpName;
        this.zcrPrepareDate = zcrPrepareDate;
        this.zcrScheduleId = zcrScheduleId;
        this.zcrSkillOriginalPrice = zcrSkillOriginalPrice;
        this.zcrSkillPrice = zcrSkillPrice;
        this.zcrStatus = zcrStatus;
    }

    public int getHzsScheduleId() {

        return hzsScheduleId;
    }

    public void setHzsScheduleId(int hzsScheduleId) {
        this.hzsScheduleId = hzsScheduleId;
    }

    public int getSxsScheduleId() {
        return sxsScheduleId;
    }

    public void setSxsScheduleId(int sxsScheduleId) {
        this.sxsScheduleId = sxsScheduleId;
    }

    public int getSysScheduleId() {
        return sysScheduleId;
    }

    public void setSysScheduleId(int sysScheduleId) {
        this.sysScheduleId = sysScheduleId;
    }

    public int getZcrScheduleId() {
        return zcrScheduleId;
    }

    public void setZcrScheduleId(int zcrScheduleId) {
        this.zcrScheduleId = zcrScheduleId;
    }

    public String getHzsEmpName() {
        return hzsEmpName;
    }

    public void setHzsEmpName(String hzsEmpName) {
        this.hzsEmpName = hzsEmpName;
    }

    public String getHzsPrepareDate() {
        return hzsPrepareDate;
    }

    public void setHzsPrepareDate(String hzsPrepareDate) {
        this.hzsPrepareDate = hzsPrepareDate;
    }

    public int getHzsSkillOriginalPrice() {
        return hzsSkillOriginalPrice;
    }

    public void setHzsSkillOriginalPrice(int hzsSkillOriginalPrice) {
        this.hzsSkillOriginalPrice = hzsSkillOriginalPrice;
    }

    public int getHzsSkillPrice() {
        return hzsSkillPrice;
    }

    public void setHzsSkillPrice(int hzsSkillPrice) {
        this.hzsSkillPrice = hzsSkillPrice;
    }

    public int getHzsStatus() {
        return hzsStatus;
    }

    public void setHzsStatus(int hzsStatus) {
        this.hzsStatus = hzsStatus;
    }

    public String getSxsEmpName() {
        return sxsEmpName;
    }

    public void setSxsEmpName(String sxsEmpName) {
        this.sxsEmpName = sxsEmpName;
    }

    public String getSxsPrepareDate() {
        return sxsPrepareDate;
    }

    public void setSxsPrepareDate(String sxsPrepareDate) {
        this.sxsPrepareDate = sxsPrepareDate;
    }

    public int getSxsSkillOriginalPrice() {
        return sxsSkillOriginalPrice;
    }

    public void setSxsSkillOriginalPrice(int sxsSkillOriginalPrice) {
        this.sxsSkillOriginalPrice = sxsSkillOriginalPrice;
    }

    public int getSxsSkillPrice() {
        return sxsSkillPrice;
    }

    public void setSxsSkillPrice(int sxsSkillPrice) {
        this.sxsSkillPrice = sxsSkillPrice;
    }

    public int getSxsStatus() {
        return sxsStatus;
    }

    public void setSxsStatus(int sxsStatus) {
        this.sxsStatus = sxsStatus;
    }

    public String getSysEmpName() {
        return sysEmpName;
    }

    public void setSysEmpName(String sysEmpName) {
        this.sysEmpName = sysEmpName;
    }

    public String getSysPrepareDate() {
        return sysPrepareDate;
    }

    public void setSysPrepareDate(String sysPrepareDate) {
        this.sysPrepareDate = sysPrepareDate;
    }

    public int getSysSkillOriginalPrice() {
        return sysSkillOriginalPrice;
    }

    public void setSysSkillOriginalPrice(int sysSkillOriginalPrice) {
        this.sysSkillOriginalPrice = sysSkillOriginalPrice;
    }

    public int getSysSkillPrice() {
        return sysSkillPrice;
    }

    public void setSysSkillPrice(int sysSkillPrice) {
        this.sysSkillPrice = sysSkillPrice;
    }

    public int getSysStatus() {
        return sysStatus;
    }

    public void setSysStatus(int sysStatus) {
        this.sysStatus = sysStatus;
    }

    public String getZcrEmpName() {
        return zcrEmpName;
    }

    public void setZcrEmpName(String zcrEmpName) {
        this.zcrEmpName = zcrEmpName;
    }

    public String getZcrPrepareDate() {
        return zcrPrepareDate;
    }

    public void setZcrPrepareDate(String zcrPrepareDate) {
        this.zcrPrepareDate = zcrPrepareDate;
    }

    public int getZcrSkillOriginalPrice() {
        return zcrSkillOriginalPrice;
    }

    public void setZcrSkillOriginalPrice(int zcrSkillOriginalPrice) {
        this.zcrSkillOriginalPrice = zcrSkillOriginalPrice;
    }

    public int getZcrSkillPrice() {
        return zcrSkillPrice;
    }

    public void setZcrSkillPrice(int zcrSkillPrice) {
        this.zcrSkillPrice = zcrSkillPrice;
    }

    public int getZcrStatus() {
        return zcrStatus;
    }

    public void setZcrStatus(int zcrStatus) {
        this.zcrStatus = zcrStatus;
    }

    public Order() {
    }
}
