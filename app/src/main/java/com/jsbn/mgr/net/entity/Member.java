package com.jsbn.mgr.net.entity;

import com.jsbn.mgr.utils.PinyinUtil;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Id;
import com.lidroid.xutils.db.annotation.Table;

import java.util.ArrayList;

/**
 * Created by 13510 on 2015/9/16.
 */
@Table(name = "member")
public class Member implements Comparable<Member> {

    @Id
    @Column(column = "empNo")
    private String empNo;

    @Column(column = "groupId")
    private int groupId;

    @Column(column = "groupLeaderId")
    private int groupLeaderId;

    @Column(column = "name")
    private String name;

    @Column(column = "personId")
    private int personId;

    @Column(column = "personType")
    private int personType;

    @Column(column = "headUrl")
    private String headUrl;

    @Column(column = "leaderId")
    private int leaderId;

    @Column(column = "gender")
    private String gender;

    @Column(column = "skillTypeId")
    private int skillTypeId;

    @Column(column = "skillTypeName")
    private String skillTypeName;

    private String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    private ArrayList<Member> persons;

    public ArrayList<Member> getPersons() {
        return persons;
    }

    public void setPersons(ArrayList<Member> persons) {
        this.persons = persons;
    }

    public String getEmpNo() {
        return empNo;
    }

    public void setEmpNo(String empNo) {
        this.empNo = empNo;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getGroupLeaderId() {
        return groupLeaderId;
    }

    public void setGroupLeaderId(int groupLeaderId) {
        this.groupLeaderId = groupLeaderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        this.pinyin = PinyinUtil.getPinyin(name);
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getSkillTypeId() {
        return skillTypeId;
    }

    public void setSkillTypeId(int skillTypeId) {
        this.skillTypeId = skillTypeId;
    }

    public String getSkillTypeName() {
        return skillTypeName;
    }

    public void setSkillTypeName(String skillTypeName) {
        this.skillTypeName = skillTypeName;
    }

    public Member(String empNo, int groupId, int groupLeaderId, String name, int personId, int personType, String headUrl, int leaderId, String gender, int skillTypeId, String skillTypeName) {

        this.empNo = empNo;
        this.groupId = groupId;
        this.groupLeaderId = groupLeaderId;
        this.name = name;
        this.personId = personId;
        this.personType = personType;
        this.headUrl = headUrl;
        this.leaderId = leaderId;
        this.gender = gender;
        this.skillTypeId = skillTypeId;
        this.skillTypeName = skillTypeName;
    }

    public Member() {
    }

    @Override
    public int compareTo(Member another) {
        return pinyin.compareTo(another.pinyin);
    }
}
