package com.jsbn.mgr;

import android.app.Application;

import com.jsbn.mgr.net.HttpClient;

/**
 * Created by 13510 on 2015/9/12.
 */
public class MgrApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HttpClient.getInstance().initialize(getApplicationContext());
    }
}
