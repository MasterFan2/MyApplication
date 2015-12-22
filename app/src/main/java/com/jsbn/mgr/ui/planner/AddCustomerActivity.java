package com.jsbn.mgr.ui.planner;

import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.Customer;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.MatcherUtil;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.common.Button;
import com.jsbn.mgr.widget.common.EditText;
import com.jsbn.mgr.widget.picker.TimePopupWindow;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

@ActivityFeature(layout = R.layout.activity_add_customer)
public class AddCustomerActivity extends BaseActivity {

    TimePopupWindow pwTime;
    private DbUtils db;

    @Bind(R.id.bridegroom_name_edit)
    EditText bridegroomNameEdit;

    @Bind(R.id.bridegroom_phone_edit)
    EditText bridegroomPhoneEdit;

    @Bind(R.id.bride_name_edit)
    EditText bridegNameEdit;

    @Bind(R.id.bride_phone_edit)
    EditText bridePhoneEdit;

    @Bind(R.id.hotel_name_edit)
    EditText hotelEdit;

    @Bind(R.id.add_customer_save_btn)
    Button btn;

    @Bind(R.id.add_customer_selected_date_txt)
    TextView selectedDataTxt;

    private Customer updateCustomer;

    private String selectedDate = "";

    @OnClick(R.id.m_title_left_btn)
    public void back() {
        animFinish();
    }

    @OnClick(R.id.add_customer_selected_date_txt)
    public void onSelected() {
        pwTime.showAtLocation(selectedDataTxt, Gravity.BOTTOM, 0, 0, new Date());
    }

    @OnClick(R.id.add_customer_save_btn)
    public void addClick() {
        String bridegroomName = bridegroomNameEdit.getText().toString();
        String bridegroomPhone = bridegroomPhoneEdit.getText().toString();
        String brideName = bridegNameEdit.getText().toString();
        String bridePhone = bridePhoneEdit.getText().toString();
        String hotel = hotelEdit.getText().toString();

        if (TextUtils.isEmpty(bridegroomName)) {
            bridegroomNameEdit.setError("请输入新郎名字");
            return;
        } else {
            bridegroomNameEdit.clearError();
            bridegroomNameEdit.setCompletionHint("新郎名字");
        }

        if (TextUtils.isEmpty(bridegroomPhone)) {
            bridegroomPhoneEdit.setError("请输入新郎手机号");
            return;
        }

        if (!MatcherUtil.isPhone(bridegroomPhone)) {
            bridegroomPhoneEdit.setError("新郎手机号号码错误,请检查");
            return;
        }
        bridegroomPhoneEdit.clearError();
        bridegroomPhoneEdit.setCompletionHint("新郎手机号");

        if (TextUtils.isEmpty(brideName)) {
            bridegNameEdit.setError("请输入新娘名字");
            return;
        } else {
            bridegNameEdit.clearError();
            bridegNameEdit.setCompletionHint("新娘名字");
        }

        if (TextUtils.isEmpty(bridePhone)) {
            bridePhoneEdit.setError("请输入新娘手机号");
            return;
        }

        if (!MatcherUtil.isPhone(bridePhone)) {
            bridePhoneEdit.setError("新娘手机号号码错误,请检查");
            return;
        }
        bridePhoneEdit.clearError();
        bridePhoneEdit.setCompletionHint("新娘手机号");

//        if (TextUtils.isEmpty(hotel)) {
//            hotelEdit.setError("请输入酒店名字");
//            return;
//        } else {
//            hotelEdit.clearError();
//            hotelEdit.setCompletionHint("酒店名字");
//        }

        if (TextUtils.isEmpty(selectedDate)) {
            T.s(context, "请选择档期!");
            return;
        }

        //
        Customer customer = new Customer();
        customer.setHotel(hotel);
        customer.setDate(selectedDate);
        customer.setBridegroomName(bridegroomName);
        customer.setBridegroomPhone(bridegroomPhone);
        customer.setBrideName(brideName);
        customer.setBridePhone(bridePhone);

        try {
            if (updateCustomer == null) {//新增
                List<Customer> customerList = db.findAll(Selector.from(Customer.class).where("bridegroomPhone", "=", bridegroomPhone));
                if (customerList == null || customerList.size() <= 0) {
                    db.save(customer);
                } else {
                    T.s(context, "此手机号的客户已存在,请检查");
                    return;
                }
            } else {                     //修改
                customer.setId(updateCustomer.getId());
                db.update(customer, new String[]{"bridegroomName", "bridegroomPhone", "brideName", "bridePhone", "date", "hotel"});
                setResult(200);
            }

            T.s(context, "保存成功");
            animFinish();
        } catch (DbException e) {
            e.printStackTrace();
            T.s(context, "保存失败");
        }
    }

    @Override
    public void initialize() {

        //
        updateCustomer = (Customer) getIntent().getSerializableExtra("customer");
        if (updateCustomer != null) {
            bridegroomNameEdit.setText(updateCustomer.getBridegroomName());
            bridegroomPhoneEdit.setText(updateCustomer.getBridegroomPhone());
            bridegNameEdit.setText(updateCustomer.getBrideName());
            bridePhoneEdit.setText(updateCustomer.getBridePhone());
            hotelEdit.setText(updateCustomer.getHotel());
            selectedDataTxt.setText("档期日期:" + updateCustomer.getDate());
            selectedDate = updateCustomer.getDate();
        }

        //
        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        //时间选择器
        pwTime = new TimePopupWindow(context, TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setTime(new Date());
        pwTime.setDefaultRange();
        pwTime.hideClearButton();

        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                Calendar calendarNow = Calendar.getInstance();
                String now = calendarNow.get(Calendar.YEAR) + "-" + (calendarNow.get(Calendar.MONTH) + 1) + "-" + calendarNow.get(Calendar.DAY_OF_MONTH);

//                Calendar calendarSelect = Calendar.getInstance();
//                calendarSelect.setTime(date);
                String selected = date.getYear() + 1900 + "-" + (date.getMonth() + 1) + "-" + date.getDate();

                if (date.getTime() < calendarNow.getTime().getTime()) {
                    if (!now.equals(selected)) {
                        T.s(context, "选择的日期必须是今天或之后");
                        return;
                    }
                }

                selectedDate = selected;
                selectedDataTxt.setText("档期日期:" + selectedDate);
            }
        });

    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

}
