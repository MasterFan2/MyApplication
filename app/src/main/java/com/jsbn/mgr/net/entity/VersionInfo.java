package com.jsbn.mgr.net.entity;

import java.io.Serializable;

/**
 * Created by MasterFan on 2015/7/23.
 * description:
 */
public class VersionInfo extends BaseEntity implements Serializable {

    private String appUrl;
    private String appVersion;
    private int    isForcedUpdate;//1.强制更新， 0.可选更新。
    private String appInfo;

    public String getAppInfo() {
        return appInfo;
    }

    public void setAppInfo(String appInfo) {
        this.appInfo = appInfo;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getIsForcedUpdate() {
        return isForcedUpdate;
    }

    public void setIsForcedUpdate(int isForcedUpdate) {
        this.isForcedUpdate = isForcedUpdate;
    }

    public VersionInfo(String appUrl, String appVersion, int isForcedUpdate) {

        this.appUrl = appUrl;
        this.appVersion = appVersion;
        this.isForcedUpdate = isForcedUpdate;
    }
}
