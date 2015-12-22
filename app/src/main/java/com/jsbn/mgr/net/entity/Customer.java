package com.jsbn.mgr.net.entity;

import com.jsbn.mgr.utils.PinyinUtil;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Transient;

import java.io.Serializable;

/**
 * 客户资料
 * Created by 13510 on 2015/11/21.
 */
@Table(name = "Customer")
public class Customer implements Serializable,Comparable<Customer>  {

    private int id;

    @Column(column = "bridegroomName")
    private String bridegroomName;//新郎

    @Column(column = "bridegroomPhone")
    private String bridegroomPhone;

    @Column(column = "brideName")
    private String brideName;//新娘

    @Column(column = "bridePhone")
    private String bridePhone;//

    @Column(column = "date")
    private String date;//婚期

    @Column(column = "hotel")
    private String hotel;//酒店

    @Transient
    private String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBridegroomName() {
        return bridegroomName;
    }

    public void setBridegroomName(String bridegroomName) {
        this.bridegroomName = bridegroomName;
        this.pinyin = PinyinUtil.getPinyin(bridegroomName);
    }

    public String getBridegroomPhone() {
        return bridegroomPhone;
    }

    public void setBridegroomPhone(String bridegroomPhone) {
        this.bridegroomPhone = bridegroomPhone;
    }

    public String getBrideName() {
        return brideName;
    }

    public void setBrideName(String brideName) {
        this.brideName = brideName;
    }

    public String getBridePhone() {
        return bridePhone;
    }

    public void setBridePhone(String bridePhone) {
        this.bridePhone = bridePhone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHotel() {
        return hotel;
    }

    public void setHotel(String hotel) {
        this.hotel = hotel;
    }

    public Customer() {}

    public Customer(int id, String bridegroomName, String bridegroomPhone, String brideName, String bridePhone, String date, String hotel) {
        this.id = id;
        this.bridegroomName = bridegroomName;
        this.bridegroomPhone = bridegroomPhone;
        this.brideName = brideName;
        this.bridePhone = bridePhone;
        this.date = date;
        this.hotel = hotel;
    }

    @Override
    public int compareTo(Customer another) {
        return pinyin.compareTo(another.pinyin);
    }
}
