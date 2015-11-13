package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/9/15.
 */
public class Version extends BaseEntity {
    private VersionInfo data;

    public VersionInfo getData() {
        return data;
    }

    public void setData(VersionInfo data) {
        this.data = data;
    }

    public Version(VersionInfo data) {

        this.data = data;
    }
}
