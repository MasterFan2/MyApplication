package com.jsbn.mgr.ui.fragment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Schedule;
import com.jsbn.mgr.net.entity.ScheduleResp;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.utils.InputMethodUtil;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.common.EditText;
import com.jsbn.mgr.widget.datepicker.bizs.calendars.DPCManager;
import com.jsbn.mgr.widget.datepicker.bizs.decors.DPDecor;
import com.jsbn.mgr.widget.datepicker.views.DatePicker;
import com.jsbn.mgr.widget.datepicker.views.MonthView;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.util.PreUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by 13510 on 2015/9/14.
 */
@FragmentFeature(layout = R.layout.fragment_calendar)
public class CalendarFragment extends BaseFragment implements MonthView.OnDaySelected, DatePicker.OnMonthChange, OnClickListener{

    @Bind(R.id.operation_layout)
    LinearLayout opetationLayout;

    @Bind(R.id.pickerView)
    DatePicker picker;

    @Bind(R.id.choose_date_txt)
    TextView selectTxt;

    @Bind(R.id.self_used_btn)
    Button usedBtn;

    @Bind(R.id.remark_btn)
    Button remarkBtn;

    @Bind(R.id.self_release_btn)
    Button releaseBtn;

    @Bind(R.id.desc_txt)
    TextView descTxt;

    private TextView addDialogDescTxt;

    private TextView addDialogHeadTxt;

    private EditText addDialogDescEdit;

    private String selectedDay ;//选择的日期

    private String desc = "";    //备注

    private ArrayList<Schedule> lists = null;

    private MTDialog addDescDialog;

    private boolean isAddDesc = true;//默认为添加备注

    int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int currentYear  = Calendar.getInstance().get(Calendar.YEAR);

    private AlertDialog dialog;


    //---------------记录有减少时清除元素------------------
    private ArrayList<String> selfArrs      = null;//new ArrayList<>();
    private ArrayList<String> jsbnUsedArrs  = null;//new ArrayList<>();
    private ArrayList<String> jsbnOrderArrs = null;//new ArrayList<>();

    /**
     * create new.
     * @param param1
     * @return
     */
    public static CalendarFragment newInstance(String param1) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * dialog show return true else false;
     * @return
     */
    public boolean dialogShow(){
        if(addDescDialog == null) return false;
        if(addDescDialog.isShowing()) {
            addDescDialog.dismiss();
            return true;
        }
        return false;
    }

    private void getAllSchedules(){
        dialog.show();
        picker.getSelfUsed().clear();
        picker.getJsbnOrder().clear();
        picker.getJsbnUsed().clear();
        HttpClient.getInstance().getSchedules(Config.members.getPersonId(), cb);
    }

    @OnClick(R.id.remark_btn)
    public void remark(View view){
        isAddDesc = false;
        if(!TextUtils.isEmpty(selectedDay)){
            addDialogHeadTxt.setText("修改档期备注");
            addDialogDescTxt.setText("日期：" + selectedDay);
            addDialogDescEdit.setText(desc);
            addDescDialog.show();
        }
    }

    /**
     * release
     * @param view
     */
    @OnClick(R.id.self_release_btn)
    public void selfRelease(View view){
//        if(!TextUtils.isEmpty(selectedDay))
//            picker.selfUnChecked(selectedDay);
        view.setEnabled(false);
        HttpClient.getInstance().releaseSchedule(Config.members.getPersonId(), selectedDay, releaseScheduleCallback);
    }

    private Callback<BaseEntity> releaseScheduleCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {

            if(baseEntity.getCode() == 200){
                T.s(getActivity(), "操作成功!");

                //查询当前档期
                getAllSchedules();
                usedBtn.setEnabled(true);
                remarkBtn.setEnabled(false);
                picker.selfUnChecked(selectedDay);
                descTxt.setText("备注：");

            }else {
                T.s(getActivity(), "操作失败!");
                releaseBtn.setEnabled(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "操作失败!");
            releaseBtn.setEnabled(true);
        }
    };

    @OnClick(R.id.self_used_btn)
    public void selfUsed(View view){
        isAddDesc = true;
        addDialogDescTxt.setText("日期：" + selectedDay);
        addDialogHeadTxt.setText("占用档期");
        addDescDialog.show();
        view.setEnabled(false);
    }

    private Callback<BaseEntity> usedScheduleCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {

            if(baseEntity.getCode() == 200){
                if(addDescDialog != null && addDescDialog.isShowing()) addDescDialog.dismiss();
                T.s(getActivity(), "操作成功!");
                descTxt.setText("备注：" + addDialogDescEdit.getText().toString());
                addDialogDescEdit.setText("");
                remarkBtn.setEnabled(true);
                releaseBtn.setEnabled(true);
                //查询当前档期
                getAllSchedules();
            }else if(baseEntity.getCode() == 3001017) {
                if(addDescDialog != null && addDescDialog.isShowing()) addDescDialog.dismiss();
                addDialogDescEdit.setText("");
                T.s(getActivity(), "该档期已被占用!");
                getAllSchedules();
            }else {
                T.s(getActivity(), "操作失败!");
                usedBtn.setEnabled(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "操作失败!");
            usedBtn.setEnabled(true);
        }
    };

    @Override
    public void initialize() {

        //如果是永久屏蔽, 则隐藏操作按钮
        if(Config.members.getIsScheduleScreen() == 1){
            opetationLayout.setVisibility(View.INVISIBLE);
        }else {
            opetationLayout.setVisibility(View.VISIBLE);
        }

        //刷新
        picker.setOnRefreshClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        picker.refresh();
                        picker.invalidate();
                        getAllSchedules();
                    }
                }.sendEmptyMessage(0);
            }
        });

        //
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(getActivity()).setView(dialogView).setCancelable(false).create();

        usedBtn.setEnabled(false);
        releaseBtn.setEnabled(false);
        remarkBtn.setEnabled(false);

        //init description dialog
        View view     = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_description_layout, null);
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_head_layout, null);
        addDialogHeadTxt = (TextView) headView.findViewById(R.id.dialog_head_txt);
        addDialogDescTxt = (TextView) view.findViewById(R.id.add_desc_date_txt);
        addDialogDescEdit= (EditText) view.findViewById(R.id.add_desc_edit);

        ViewHolder holder = new ViewHolder(view);
        addDescDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setHeader(headView)
                .setFooter(R.layout.dialog_foot_layout)
                .setCancelable(false)
                .setOnClickListener(this)
                .setGravity(MTDialog.Gravity.TOP)
                .create();

        //
        usedBtn.setEnabled(false);
        releaseBtn.setEnabled(false);
        remarkBtn.setEnabled(false);

        picker.setOnDaySelected(this);
        picker.setOnMonthChange(this);
        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(List<String> date) {
//                unReleaseAll();
                getAllSchedules();
            }
        });

        Calendar calendar = Calendar.getInstance();
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        //today
        List<String> tmpTL = new ArrayList<>();
        tmpTL.add(year + "-" + month + "-" + day);
        DPCManager.getInstance().setDecorTR(tmpTL);

        picker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(getResources().getColor(R.color.pink));
                canvas.drawRect(rect, paint);
            }

            @Override
            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(getResources().getColor(R.color.red_error));
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
            }
        });

        picker.setDate(year, month);

        //查询当前档期
//        getAllSchedules();
    }

    private Callback<ScheduleResp> cb = new Callback<ScheduleResp>() {
        @Override
        public void success(ScheduleResp scheduleResp, Response response) {
            dialog.dismiss();
            if(scheduleResp.getCode() == 200){

               lists = scheduleResp.getData();
                if(lists != null && lists.size() > 0){
                    for (int i = 0; i < lists.size(); i++) {
                        Schedule schedule = lists.get(i);
                        String date = schedule.getScheduleDate().split(" ")[0];

                        int year = Integer.parseInt(date.substring(0, date.indexOf("-")));
                        int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
                        int day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));

                        if(currentMonth == month && currentYear == year){
                            date = year + "-" + month + "-" + day;
                            if(schedule.getStatusId() == 4) {

//                                for (picker.getSelfUsed().keySet())
                                picker.selfChecked(date);
                            }
                            if(schedule.getStatusId() == 1) {
//                                if(jsbnUsedArrs == null){
//                                    jsbnUsedArrs = new ArrayList<>();
//                                }else {
//
//                                }
                                picker.jsbnUsedCheck(date);
                            }
                            if(schedule.getStatusId() == 2) {
//                                if(jsbnOrderArrs == null){
//                                    jsbnOrderArrs = new ArrayList<>();
//                                }else {
//
//                                }
                                picker.jsbnOrderCheck(date);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            dialog.dismiss();
            T.s(getActivity(), "ERR");
        }
    };

    private int compareDate(String today, String compare){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateToday = sdf.parse(today);
            Date compDate  = sdf.parse(compare);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

    /**
     * 选中日期
     *
     * @param dd
     */
    @Override
    public void onDaySelected(String dd) {
        releaseBtn.setEnabled(false);
        remarkBtn.setEnabled(false);
        selectedDay = dd;
        if(TextUtils.isEmpty(dd)) {
            usedBtn.setEnabled(false);
            releaseBtn.setEnabled(false);
            remarkBtn.setEnabled(false);
            selectTxt.setText("");
            descTxt.setText("备注：");
            return;
        }

        selectTxt.setText(dd);

        if(usedBtn.isEnabled() == false){
            usedBtn.setEnabled(true);
        }

        //显示备注信息
        final int size =lists == null ? 0 : lists.size();
        boolean finded = false;
        for (int i = 0; i < size; i++) {
            Schedule schedule = lists.get(i);
            String date = schedule.getScheduleDate().split(" ")[0];

            int year  = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            int day   = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));

            date = year + "-" + month + "-" + day;
            if (date.equals(dd)){//如果选择的日期在占用日期里面
                desc =  schedule.getRemark();
                descTxt.setText("备注：" + schedule.getRemark().replace("<br />", "\n"));
                usedBtn.setEnabled(false);
                if(schedule.getStatusId() == 4) {//只有自己占用的日期才能释放
                    releaseBtn.setEnabled(true);
                    remarkBtn.setEnabled(true);
                }else {
                    remarkBtn.setEnabled(false);
                }
                finded = true;
                break;
            }else{
                desc = "";
            }
        }
        if(!finded) descTxt.setText("备注：-" );
    }

    @Override
    public void onMonthChange(int month) {
        currentMonth = month;
        picker.refresh();
        getAllSchedules();
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_cancel_Btn:
                InputMethodUtil.hideInputMethod(getActivity());
                usedBtn.setEnabled(true);
                addDialogDescEdit.clearError();
                addDialogDescEdit.setText("");
                dialog.dismiss();
                break;
            case  R.id.dialog_confrim_btn:
                String desc = addDialogDescEdit.getText().toString();
                if(TextUtils.isEmpty(desc)){
                    addDialogDescEdit.setError("请输入备注信息");
                    return;
                }
                addDialogDescEdit.clearError();
                InputMethodUtil.hideInputMethod(getActivity());
                if(!TextUtils.isEmpty(selectedDay)){
                    String account = PreUtil.getLoginAccount(getActivity());
                    if(!TextUtils.isEmpty(account)){
                        if(isAddDesc) HttpClient.getInstance().usedSchedule(selectedDay, selectedDay, Config.members.getPersonId(), desc, usedScheduleCallback);
                        else          HttpClient.getInstance().remarkSchedule(Config.members.getPersonId(), selectedDay, desc, usedScheduleCallback);

                    }else {
                        T.s(getActivity(), "登录过期， 请重新登录");
                        view.setEnabled(true);
                    }
                }
                break;
        }
    }
}
