package com.jsbn.mgr.ui.member;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Schedule;
import com.jsbn.mgr.net.entity.ScheduleResp;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_member_schedule)
public class MemberScheduleActivity extends BaseActivity implements MonthView.OnDaySelected, DatePicker.OnMonthChange, OnClickListener {

    @Bind(R.id.member_schedule_pickerView)
    DatePicker picker;

    @Bind(R.id.member_schedule_desc_txt)
    TextView descTxt;

    @Bind(R.id.choose_date_txt)
    TextView selectTxt;

    @Bind(R.id.self_used_btn)
    Button usedBtn;

    @Bind(R.id.remark_btn)
    Button remarkBtn;

    @Bind(R.id.self_release_btn)
    Button releaseBtn;

    private TextView addDialogDescTxt;

    private TextView addDialogHeadTxt;

    private EditText addDialogDescEdit;

    private String selectedDay ;//选择的日期

    private String desc = "";    //备注

    private ArrayList<Schedule> lists;

    private String personId;

    private MTDialog addDescDialog;

    private boolean isAddDesc = true;//默认为添加备注

    private AlertDialog dialog;

    int currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
    int currentYear  = Calendar.getInstance().get(Calendar.YEAR);

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
        HttpClient.getInstance().releaseSchedule(Integer.parseInt(personId), selectedDay, releaseScheduleCallback);
    }

    @Override
    public void initialize() {

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(context).setView(dialogView).setCancelable(false).create();

        usedBtn.setEnabled(false);
        releaseBtn.setEnabled(false);
        remarkBtn.setEnabled(false);

        personId = getIntent().getStringExtra("personId");

        //init description dialog
        View view     = LayoutInflater.from(context).inflate(R.layout.dialog_add_description_layout, null);
        View headView = LayoutInflater.from(context).inflate(R.layout.dialog_head_layout, null);
        addDialogHeadTxt = (TextView) headView.findViewById(R.id.dialog_head_txt);
        addDialogDescTxt = (TextView) view.findViewById(R.id.add_desc_date_txt);
        addDialogDescEdit= (EditText) view.findViewById(R.id.add_desc_edit);

        ViewHolder holder = new ViewHolder(view);
        addDescDialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setHeader(headView)
                .setFooter(R.layout.dialog_foot_layout)
                .setCancelable(false)
                .setOnClickListener(MemberScheduleActivity.this)
                .setGravity(MTDialog.Gravity.TOP)
                .create();

        //

        Calendar calendar = Calendar.getInstance();
        int year  = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day   = calendar.get(Calendar.DAY_OF_MONTH);

        //
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

        picker.setOnDaySelected(this);
        picker.setOnMonthChange(this);
        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
            @Override
            public void onDateSelected(List<String> date) {
                HttpClient.getInstance().getSchedules(Integer.parseInt(personId), cb);
            }
        });

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

        if(!TextUtils.isEmpty(personId))
            HttpClient.getInstance().getSchedules(Integer.parseInt(personId), cb);
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

                        if(currentMonth == month && year == currentYear){
                            date = year + "-" + month + "-" + day;
                            if(schedule.getStatusId() == 4) picker.selfChecked(date);
                            if(schedule.getStatusId() == 1) picker.jsbnUsedCheck(date);
                            if(schedule.getStatusId() == 2) picker.jsbnOrderCheck(date);
                        }
                    }
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            dialog.dismiss();
            T.s(context, "获取数据错误");
        }
    };

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

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

            int year = Integer.parseInt(date.substring(0, date.indexOf("-")));
            int month = Integer.parseInt(date.substring(date.indexOf("-") + 1, date.lastIndexOf("-")));
            int day = Integer.parseInt(date.substring(date.lastIndexOf("-") + 1, date.length()));

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

    @OnClick(R.id.self_used_btn)
    public void selfUsed(View view){
        isAddDesc = true;
        addDialogDescTxt.setText("日期：" + selectedDay);
        addDialogHeadTxt.setText("占用档期");
        addDescDialog.show();
        view.setEnabled(false);
    }

    @Override
    public void onMonthChange(int month) {
        currentMonth = month;
        picker.refresh();
        getAllSchedules();
    }

    private void getAllSchedules(){
        dialog.show();
        picker.getSelfUsed().clear();
        picker.getJsbnOrder().clear();
        picker.getJsbnUsed().clear();
        HttpClient.getInstance().getSchedules(Integer.parseInt(personId), cb);
    }

    private Callback<BaseEntity> releaseScheduleCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {

            if(baseEntity.getCode() == 200){
                T.s(context, "操作成功!");

                //查询当前档期
                getAllSchedules();
                usedBtn.setEnabled(true);
                remarkBtn.setEnabled(false);
                picker.selfUnChecked(selectedDay);
                descTxt.setText("备注：");

            }else {
                T.s(context, "操作失败!");
                releaseBtn.setEnabled(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "操作失败!");
            releaseBtn.setEnabled(true);
        }
    };

    private Callback<BaseEntity> usedScheduleCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {

            if(baseEntity.getCode() == 200){
                if(addDescDialog != null && addDescDialog.isShowing()) addDescDialog.dismiss();
                T.s(context, "操作成功!");
                descTxt.setText("备注：" + addDialogDescEdit.getText().toString());
                addDialogDescEdit.setText("");
                remarkBtn.setEnabled(true);
                releaseBtn.setEnabled(true);
                //查询当前档期
                getAllSchedules();
            }else {
                T.s(context, "操作失败!");
                usedBtn.setEnabled(true);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "操作失败!");
            usedBtn.setEnabled(true);
        }
    };

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_cancel_Btn:
                InputMethodUtil.hideInputMethod(MemberScheduleActivity.this);
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
                InputMethodUtil.hideInputMethod(MemberScheduleActivity.this);
                if(!TextUtils.isEmpty(selectedDay)){
                    String account = PreUtil.getLoginAccount(context);
                    if(!TextUtils.isEmpty(account)){
                        if(isAddDesc) HttpClient.getInstance().usedSchedule(selectedDay, selectedDay, Integer.parseInt(personId), desc, usedScheduleCallback);
                        else          HttpClient.getInstance().remarkSchedule(Integer.parseInt(personId), selectedDay, desc, usedScheduleCallback);

                    }else {
                        T.s(context, "登录过期， 请重新登录");
                        view.setEnabled(true);
                    }
                }
                break;
        }
    }
}
