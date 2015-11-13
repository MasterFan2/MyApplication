package com.jsbn.mgr.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by MasterFan on 2015/7/8.
 * description:
 */
public class MatcherUtil {
    public static boolean isPhone(String phone) {
        String check = "^(13[4,5,6,7,8,9]|15[0,8,9,1,7]|18[7,8,9]|177)\\d{8}$"; //手机验证
        Pattern p = Pattern.compile(check);
        Matcher m = p.matcher(phone);
        return m.find();
    }
}
