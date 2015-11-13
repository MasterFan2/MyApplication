package com.jsbn.mgr.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by 13510 on 2015/9/17.
 */
public class InputMethodUtil {
    public static void hideInputMethod(Activity context) {
        InputMethodManager im = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(context.getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}