package com.jsbn.mgr.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 13510 on 2015/11/19.
 */
public class DateUtil {

    /**
     * 今天以前
     * @return  false: 不能预定       true:今天或之后， 可以预定
     */
    public static boolean isAfterToday(String strDate) {

        if(TextUtils.isEmpty(strDate)) return false;
        Calendar calendar = Calendar.getInstance();
        long today = calendar.getTime().getTime();
        String strNow = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);

        if(strNow.equals(strDate)) return true;//是当天
        try {
            long into = new SimpleDateFormat("yyyy-MM-dd").parse(strDate).getTime();
            S.o("::::::::::::::" + into);
            if(into >= today) return true;
            else              return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }
}
