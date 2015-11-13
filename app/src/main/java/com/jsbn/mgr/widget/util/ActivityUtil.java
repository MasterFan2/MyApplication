package com.jsbn.mgr.widget.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.SystemBarTintManager;

/**
 * Created by 13510 on 2015/9/10.
 */
public class ActivityUtil {

    private static final int INVALID = -1;

    public static void injectFeature(Activity act){
        ActivityFeature feature = act.getClass().getAnnotation(ActivityFeature.class);
        if(feature == null)
            throw new IllegalArgumentException("not set resources");

//        if(feature.no_title()){
//            act.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        }

        int statusBarColor = feature.statusBarColor();

        //set status bar color
        if(statusBarColor != INVALID){
            //set statusBar color
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setTranslucentStatus(true, act);
            }

            SystemBarTintManager tintManager = new SystemBarTintManager(act);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(statusBarColor);
        }

        //
        act.setContentView(feature.layout());
    }

    private static void setTranslucentStatus(boolean on, Activity activity) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
