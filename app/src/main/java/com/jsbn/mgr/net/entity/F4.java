package com.jsbn.mgr.net.entity;

import com.jsbn.mgr.utils.PinyinUtil;

/**
 * Created by 13510 on 2015/11/10.
 */
public class F4 implements Comparable<F4> {

    private String name;
    private String pinyin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public F4(String name) {
        this.name = name;
        this.pinyin = PinyinUtil.getPinyin(name);
    }

    @Override
    public int compareTo(F4 another) {
        return pinyin.compareTo(another.pinyin);
    }
}
