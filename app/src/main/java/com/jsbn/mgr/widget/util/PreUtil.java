package com.jsbn.mgr.widget.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Fan on 2015-02-13.
 */
public class PreUtil {

    private final static String prefix = "jsbn_f4_";

    /**
     * 检查是否第一次运行
     * @param context
     * @return
     */
    public static boolean isFirstRun(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean flag = sp.getBoolean( prefix + "firstRun", false);
        return flag;
    }

    /**
     * 设置
     * @param context
     */
    public static void setRun(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(prefix + "firstRun", true);
        editor.commit();
    }

    /**
     * 是否登录
     * @return true: 已登录    false:未登录
     */
    public static boolean isLogin(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String account = sp.getString(prefix + "account", null);
        return account != null;
    }

    /**
     * 是否登录
     * @return true: 已登录    false:未登录
     */
    public static void clearLoginInfo(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(prefix + "account");
        editor.remove(prefix + "password");
        editor.commit();
    }

    /**
     * 保存登录成功的用户名头像
     *
     * @param context
     * @param account
     * @param headUrl
     */
    public static void saveLoginSuccessInfo(Context context, String account, String headUrl){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefix + "success_account", account);
        editor.putString(prefix + "success_head_url", headUrl);
        editor.commit();
    }

    /**
     * 获取登录成功的账户和头像
     *
     * @param context
     * @return
     */
    public static LoginSucessInfo getLoginSuccessInfo(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String account = sp.getString(prefix + "success_account", null);
        String headUrl = sp.getString(prefix + "success_head_url", null);
        if(account == null || headUrl == null) return null;
        return new LoginSucessInfo(account, headUrl);
    }

    /**
     * 保存用户信息
     *
     * @param context
     * @param account
     * @param pwd
     */
    public static void saveLoginInfo(Context context, String account, String pwd){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(prefix + "account", account);
        editor.putString(prefix + "password", pwd);
        editor.commit();
    }

    /**
     * 获取账户信息
     * @param context
     * @return
     */
    public static String getLoginAccount(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(prefix +"account", null);
    }

    /**
     * 获取账户密码
     * @param context
     * @return
     */
    public static String getLoginPwd(Context context){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString(prefix +"password", null);
    }


    public static class LoginSucessInfo {
        private String account;
        private String url;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public LoginSucessInfo(String account, String url) {

            this.account = account;
            this.url = url;
        }
    }

}
