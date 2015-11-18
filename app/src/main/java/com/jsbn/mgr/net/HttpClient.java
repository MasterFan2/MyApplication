package com.jsbn.mgr.net;

import android.content.Context;
import android.text.TextUtils;

import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.MemberResp;
import com.jsbn.mgr.net.entity.MyMemberResp;
import com.jsbn.mgr.net.entity.PlannerResp;
import com.jsbn.mgr.net.entity.ScheduleResp;
import com.jsbn.mgr.net.entity.Version;
import com.squareup.okhttp.OkHttpClient;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by MasterFan on 2015/7/3.
 * description:
 */
public class HttpClient {

    private static final String PREFIX = "http://";
    private static final String END = "/api";

    //private static final String URL     = "bfjs.f3322.net";//本地服务器
    //private static final String URL     = "cd.jsbn.com";//开发服务器
    private static final String URL = "jsbn4djg.jsbn.com";

    //服务器地址信息
    //private static final String URL = "app.jsbn.love";//正式服务器
    private static final String BASE_URL = PREFIX + URL + END;

    private Context mContext;
    private RestAdapter restAdapter = null;
    private NetInterface netInterface = null;

    private static HttpClient instanse;

    public HttpClient() {
    }

    public static HttpClient getInstance() {
        if (instanse == null) {
            instanse = new HttpClient();
        }
        return instanse;
    }

    /**
     * @param var1 上下文
     * @return false:初期化失败 true:初期化成功
     */
    public boolean initialize(Context var1) {

        mContext = var1;
        // 创建适配器
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setClient(new OkClient(new OkHttpClient().setCookieHandler(new CustomCookieManager(mContext))))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {

                        String temp = PreferenceUtils.getValue(mContext, PreferenceUtils.PREFERENCE_B_TOKEN, PreferenceUtils.DataType.STRING);
                        if (!TextUtils.isEmpty(temp)) {
                            // 设置JSESSIONID
                            request.addHeader("btoken", temp);
                        }
                    }
                })
                .build();

        if (restAdapter == null) {
            return false;
        }
        netInterface = restAdapter.create(NetInterface.class);
        return netInterface != null;
    }

    //接口
    interface NetInterface {

        //a2.登录 type:0-web      1-app       2-3D
        @FormUrlEncoded
        @POST("/business/person/login")
        void login(@Field("username") String uname, @Field("password") String pwd, Callback<MemberResp> cb);

        //修改密码
        @FormUrlEncoded
        @POST("/business/person/updatePassWord")
        void updatePassword(@Field("newPassword") String uname, @Field("oldPassword") String pwd, Callback<BaseEntity> cb);

        //检查更新
        @GET("/app/upgrade")
        void upgradeCheck(@Query("appType") int appType, @Query("appVersion") String appVersion, @Query("applicableUsers") int applicableUsers, Callback<Version> cb);

        //占用档期
        @FormUrlEncoded
        @POST("/schedule/{personId}/update")
        void usedSchedule(@Field("startScreenDate") String startScreenDate, @Field("endScreenDate") String endScreenDate, @Path("personId") int personId, @Field("remark") String remark, Callback<BaseEntity> cb);

        //释放档期
        @FormUrlEncoded
        @POST("/schedule/{personId}/relase")
        void releaseSchedule(@Path("personId") int personId, @Field("lockDate") String lockDate, Callback<BaseEntity> cb);

        //档期查询
        @GET("/schedule/{personId}")
        void getSchedules(@Path("personId") int personId, Callback<ScheduleResp> cb);

        //修改备注
        @FormUrlEncoded
        @POST("/schedule/{personId}/remark")
        void remarkSchedule(@Path("personId") int personId, @Field("lockDate") String lockDate, @Field("remark") String remark, Callback<BaseEntity> cb);

        //收银员获取所有档期
        @GET("/schedule/allPreparedList")
        void getAllPreparedList(Callback<PlannerResp> cb);

        //收银员确认档期
        @GET("/schedule/change")
        void collectMoneyConfirm(@Query("scheduleId") int scheduleId, Callback<BaseEntity> cb);

        //根据用户名查询所有管辖人员
        @GET("/schedule/{username}/personListByRole")
        void getPersonByPlannerName(@Path("username") String username, @Query("username") String name, Callback<MyMemberResp> cb);

        //根据用户名查询所有管辖人员
        @GET("/schedule/{username}/personListByRole")
        void getPersonByPlannerNameDate(@Path("username") String username, @Query("username") String name, @Query("lockDate") String loadDate, Callback<MyMemberResp> cb);

        //统筹师预订四大金刚
        @GET("/schedule/screenFourKing")
        void plannerScheduleF4(@Query("tcsPersonId") int tcsPersonId, @Query("personId") int personId, @Query("lockDate") String lockDate, @Query("remark") String remark, Callback<BaseEntity> cb);

        //统筹师释放档期
        @GET("/schedule/cancelPreparedFKPerson")
        void cancelPlannerSchedule(@Query("scheduleId") int scheduleId, Callback<BaseEntity> cb);
    }


    //-----------------------------------------------------
    // 登录/注册
    //-----------------------------------------------------

    /**
     * 登录
     *
     * @param uname
     * @param pwd
     */
    public void login(String uname, String pwd, Callback<MemberResp> cb) {
        netInterface.login(uname, pwd, cb);
    }

    /**
     * 修改密码
     *
     * @param newPassword
     * @param oldPassword
     * @param cb
     */
    public void updatePassword(@Field("newPassword") String newPassword, @Field("oldPassword") String oldPassword, Callback<BaseEntity> cb) {
        netInterface.updatePassword(newPassword, oldPassword, cb);
    }

    /**
     * 检查更新
     *
     * @param appType
     * @param appVersion
     * @param applicableUsers
     * @param cb
     */
    public void upgradeCheck(int appType, String appVersion, int applicableUsers, Callback<Version> cb) {
        netInterface.upgradeCheck(appType, appVersion, applicableUsers, cb);
    }

    /**
     * 占用档期
     *
     * @param startScreenDate
     * @param endScreenDate
     * @param personId
     * @param remark
     * @param cb
     */
    public void usedSchedule(String startScreenDate, String endScreenDate, int personId, String remark, Callback<BaseEntity> cb) {
        netInterface.usedSchedule(startScreenDate, endScreenDate, personId, remark, cb);
    }

    /**
     * 释放档期
     *
     * @param personId
     * @param lockDate
     * @param cb
     */
    public void releaseSchedule(int personId, String lockDate, Callback<BaseEntity> cb) {
        netInterface.releaseSchedule(personId, lockDate, cb);
    }

    /**
     * 档期查询
     *
     * @param personId
     * @param cb
     */
    public void getSchedules(int personId, Callback<ScheduleResp> cb) {
        netInterface.getSchedules(personId, cb);
    }

    /**
     * 修改备注
     *
     * @param personId
     * @param lockDate
     * @param remark
     * @param cb
     */
    public void remarkSchedule(int personId, String lockDate, String remark, Callback<BaseEntity> cb) {
        netInterface.remarkSchedule(personId, lockDate, remark, cb);
    }

    /**
     * 收银员获取所有档期
     *
     * @param cb
     */
    public void getAllPreparedList(Callback<PlannerResp> cb) {
        netInterface.getAllPreparedList(cb);
    }

    /**
     * 收银员确认档期
     *
     * @param scheduleId
     * @param cb
     */
    public void collectMoneyConfirm(@Query("scheduleId") int scheduleId, Callback<BaseEntity> cb) {
        netInterface.collectMoneyConfirm(scheduleId, cb);
    }

    /**
     * 根据用户名查询所有管辖人员
     *
     * @param username
     * @param cb
     */
    public void getPersonByPlannerName(String username, Callback<MyMemberResp> cb) {
        netInterface.getPersonByPlannerName(username, username, cb);
    }


    /**
     * 根据用户名查询所有管辖人员
     */
    public void getPersonByPlannerNameDate(String name, String loadDate, Callback<MyMemberResp> cb) {
        netInterface.getPersonByPlannerNameDate(name, name, loadDate, cb);
    }


    /**
     * 统筹师预订四大金刚
     *
     * @param tcsPersonId
     * @param personId
     * @param lockDate
     * @param remark
     * @param cb
     */
    public void plannerScheduleF4(@Query("tcsPersonId") int tcsPersonId, @Query("personId") int personId, @Query("lockDate") String lockDate, @Query("remark") String remark, Callback<BaseEntity> cb) {
        netInterface.plannerScheduleF4(tcsPersonId, personId, lockDate, remark, cb);
    }

    /**
     * 统筹师释放档期
     *
     * @param scheduleId
     * @param cb
     */
    public void cancelPlannerSchedule(@Query("scheduleId") int scheduleId, Callback<BaseEntity> cb) {
        netInterface.cancelPlannerSchedule(scheduleId, cb);
    }
}
