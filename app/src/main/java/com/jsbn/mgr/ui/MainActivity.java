package com.jsbn.mgr.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.widget.common.Button;
import com.jsbn.mgr.widget.datepicker.bizs.calendars.DPCManager;
import com.jsbn.mgr.widget.datepicker.bizs.decors.DPDecor;
import com.jsbn.mgr.widget.datepicker.views.DatePicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

@ActivityFeature(layout = R.layout.activity_main)
public class MainActivity extends BaseActivity {//MonthView.OnDayChecked {

    @Bind(R.id.pickerView)
    DatePicker datePicker;

    private String chooseDate = null;

    @Bind(R.id.choose_date_txt)
    TextView dayTxt;

    @Bind(R.id.use_btn)
    Button useBtn;

//    @Bind(R.id.mEdit)
//    EditText edit;

    @OnClick(R.id.refresh_btn)
    public void refresh(){
        datePicker.selfUnChecked("2015-9-11");
    }

    @OnClick(R.id.use_btn)
    public void useClick() {
//        datePicker.getX(edit.getText().toString());

        ArrayList<String> selfMarks = new ArrayList<>();
        selfMarks.add("2015-9-11");
        selfMarks.add("2015-9-12");
        selfMarks.add("2015-9-13");
        for (int i = 0; i < selfMarks.size(); i++) {
            datePicker.selfChecked(selfMarks.get(i));
        }

        ArrayList<String> jsbnOrderMarks = new ArrayList<>();
        jsbnOrderMarks.add("2015-9-17");
        jsbnOrderMarks.add("2015-9-21");

//        datePicker.selfChecked(marksDate);
//        datePicker.setSelfUsed(marksDate);
        for (int i = 0; i < jsbnOrderMarks.size(); i++) {
            datePicker.jsbnOrderCheck(jsbnOrderMarks.get(i));
        }

        ArrayList<String> jsbnUsedMarks = new ArrayList<>();
        jsbnUsedMarks.add("2015-9-24");
        jsbnUsedMarks.add("2015-9-30");

//        datePicker.selfChecked(marksDate);
//        datePicker.setSelfUsed(marksDate);
        for (int i = 0; i < jsbnUsedMarks.size(); i++) {
            datePicker.jsbnUsedCheck(jsbnUsedMarks.get(i));
        }
    }

    @Override
    public void initialize() {
        List<String> tmpTL = new ArrayList<>();
        tmpTL.add("2015-9-12");
        DPCManager.getInstance().setDecorTR(tmpTL);

        datePicker.setDate(2015, 9);
        datePicker.setDPDecor(new DPDecor() {
            @Override
            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(getResources().getColor(R.color.pink));
                canvas.drawRect(rect, paint);
            }

            @Override
            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
                paint.setColor(getResources().getColor(R.color.blue));
                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
            }
        });
//
//        datePicker = new DatePicker(context);
//        List<String> tmpTR = new ArrayList<>();
//        tmpTR.add("2015-9-10");
//        tmpTR.add("2015-9-11");
//        tmpTR.add("2015-9-12");
//        DPCManager.getInstance().setDecorTR(tmpTR);
//
//        List<String> tmpTL = new ArrayList<>();
//        tmpTL.add("2015-9-1");
//        tmpTL.add("2015-9-2");
//        tmpTL.add("2015-9-3");
//        DPCManager.getInstance().setDecorTL(tmpTL);
//        datePicker.setDate(2015, 9);
//        datePicker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.GREEN);
//                canvas.drawRect(rect, paint);
//            }
//
//            @Override
//            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.BLUE);
//                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//            }
//        });
//        datePicker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });
//
//        container.addView(datePicker);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

    // 默认多选模式
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

    // 自定义背景绘制示例 Example of custom date's background
//        List<String> tmp = new ArrayList<>();
//        tmp.add("2015-9-1");
//        tmp.add("2015-9-8");
//        tmp.add("2015-9-16");
//        DPCManager.getInstance().setDecorBG(tmp);
//
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 9);
//        picker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorBG(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.RED);
//                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2F, paint);
//            }
//        });
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

    // 自定义前景装饰物绘制示例 Example of custom date's foreground decor
//        List<String> tmpTL = new ArrayList<>();
//        tmpTL.add("2015-7-5");
//        DPCManager.getInstance().setDecorTL(tmpTL);
//
//        List<String> tmpTR = new ArrayList<>();
//        tmpTR.add("2015-7-10");
//        DPCManager.getInstance().setDecorTR(tmpTR);
//
//        DatePicker picker = (DatePicker) findViewById(R.id.main_dp);
//        picker.setDate(2015, 7);
//        picker.setDPDecor(new DPDecor() {
//            @Override
//            public void drawDecorTL(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.GREEN);
//                canvas.drawRect(rect, paint);
//            }
//
//            @Override
//            public void drawDecorTR(Canvas canvas, Rect rect, Paint paint) {
//                paint.setColor(Color.BLUE);
//                canvas.drawCircle(rect.centerX(), rect.centerY(), rect.width() / 2, paint);
//            }
//        });
//        picker.setOnDateSelectedListener(new DatePicker.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(List<String> date) {
//                String result = "";
//                Iterator iterator = date.iterator();
//                while (iterator.hasNext()) {
//                    result += iterator.next();
//                    if (iterator.hasNext()) {
//                        result += "\n";
//                    }
//                }
//                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
//            }
//        });

    // 对话框下的DatePicker示例 Example in dialog
//        Button btnPick = (Button) findViewById(R.id.main_btn);
//        btnPick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
//                dialog.show();
//                DatePicker picker = new DatePicker(MainActivity.this);
//                picker.setDate(2015, 7);
//                picker.setMode(DPMode.SINGLE);
//                picker.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
//                    @Override
//                    public void onDatePicked(String date) {
//                        Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG).show();
//                        dialog.dismiss();
//                    }
//                });
//                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dialog.getWindow().setContentView(picker, params);
//                dialog.getWindow().setGravity(Gravity.CENTER);
//            }
//        });
//    }

    @Override
    public boolean onKeydown() {
        return false;
    }

    public void onDayChecked(String date) {
        dayTxt.setText("日期:" + date);
        chooseDate = date;
    }


}
