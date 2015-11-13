package com.jsbn.mgr.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by MasterFan on 2015/4/24.
 * description:
 */
public class T {
    public static void s(Context context , String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void sLong(Context context , String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
