package com.jsbn.mgr.ui.planner;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Customer;
import com.jsbn.mgr.net.entity.Order;
import com.jsbn.mgr.net.entity.OrderResp;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.dialog.WaitDialog;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.exception.DbException;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_customer_select_f4)
public class CustomerSelectF4Activity extends BaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Customer customer;
    private Order order;

    private MTDialog confirmDialog;
    private TextView tipsTxt;
    private DbUtils db;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.bridegroom_name_txt)
    TextView brideGroomNameTxt;

    @Bind(R.id.bridegroom_phone_txt)
    TextView brideGroomPhoneTxt;

    @Bind(R.id.bride_name_txt)
    TextView brideNameTxt;

    @Bind(R.id.bride_phone_txt)
    TextView bridePhoneTxt;

    @Bind(R.id.date_txt)
    TextView dateTxt;

    @Bind(R.id.hotel_txt)
    TextView hotelTxt;

    //-------------------------------------------------------------------------------
    @Bind(R.id.sel_f4_emcee_release_btn)
    TextView emceeReleaseBtn;

    @Bind(R.id.sel_f4_photo_release_btn)
    TextView photoReleaseBtn;

    @Bind(R.id.sel_f4_dresser_release_btn)
    TextView dresserReleaseBtn;

    @Bind(R.id.sel_f4_camera_release_btn)
    TextView cameraReleaseBtn;

    private WaitDialog waitDialog;

    @OnClick(R.id.sel_f4_emcee_release_btn)
    public void emceeReleaseClick() {
        HttpClient.getInstance().cancelPlannerSchedule(order.getZcrScheduleId(), releaseScheduleCallback);
    }

    @OnClick(R.id.sel_f4_photo_release_btn)
    public void photoReleaseClick() {
        HttpClient.getInstance().cancelPlannerSchedule(order.getSysScheduleId(), releaseScheduleCallback);
    }

    @OnClick(R.id.sel_f4_dresser_release_btn)
    public void dresserReleaseClick() {
        HttpClient.getInstance().cancelPlannerSchedule(order.getHzsScheduleId(), releaseScheduleCallback);
    }

    @OnClick(R.id.sel_f4_camera_release_btn)
    public void cameraReleaseClick() {
        HttpClient.getInstance().cancelPlannerSchedule(order.getSxsScheduleId(), releaseScheduleCallback);
    }

    @OnClick(R.id.del_customer_mrl)
    public void delCustomer() {
        if(TextUtils.isEmpty(order.getZcrPrepareDate()) && TextUtils.isEmpty(order.getSysPrepareDate()) && TextUtils.isEmpty(order.getHzsPrepareDate()) && TextUtils.isEmpty(order.getSxsPrepareDate())){
            tipsTxt.setText("是否确认删除客户【"+ customer.getBridegroomName() +"】?");
            confirmDialog.show();
        }else {
            T.sLong(context, "您已预定过此客户的档期，不能删除");
        }
    }

    @OnClick(R.id.customer_update_txt)
    public void updateCustomer() {
        if(TextUtils.isEmpty(order.getZcrPrepareDate()) && TextUtils.isEmpty(order.getSysPrepareDate()) && TextUtils.isEmpty(order.getHzsPrepareDate()) && TextUtils.isEmpty(order.getSxsPrepareDate())){
            Intent intent = new Intent(context, AddCustomerActivity.class);
            intent.putExtra("customer", customer);
            animStartForResult(intent, 208);
        }else {
            T.sLong(context, "您已预定过此客户的档期，不能修改");
        }
    }

    private Callback<BaseEntity> releaseScheduleCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {

            if(baseEntity.getCode() == 200){
                T.s(context, "释放档期成功!");
                HttpClient.getInstance().searchOrderByBridegroomPhone(customer.getBridegroomPhone(), cb);
                //查询当前档期
            }else if(baseEntity.getCode() == 3001025){
                T.sLong(context, "你没有权限操作其他统筹师预定的档期");
            }else {//3001007
                T.s(context, "操作失败!code="+ baseEntity.getCode());
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "操作失败!");
        }
    };


    ///////////////////////////////////
    @Bind(R.id.sel_f4_emcee_name_txt)
    TextView emceeNameTxt;

    @Bind(R.id.sel_f4_photo_name_txt)
    TextView photoNameTxt;

    @Bind(R.id.sel_f4_dresser_name_txt)
    TextView dresserNameTxt;

    @Bind(R.id.sel_f4_camera_name_txt)
    TextView cameraNameTxt;

    /////////////////////////////
    @Bind(R.id.sel_f4_emcee_non_txt)
    TextView emceeNonTxt;

    @Bind(R.id.sel_f4_photo_non_txt)
    TextView photoNonTxt;

    @Bind(R.id.sel_f4_dresser_non_txt)
    TextView dresserNonTxt;

    @Bind(R.id.sel_f4_camera_non_txt)
    TextView cameraNonTxt;


    @OnClick(R.id.mrl_emcee)
    public void emceeClick() {
        if(order != null && !TextUtils.isEmpty(order.getZcrPrepareDate())){
            return;
        }
        Intent intent = new Intent(context, F4Activity.class);
        intent.putExtra("type", 6);
        intent.putExtra("customer", customer);
        animStartForResult(intent,  205);
    }

    @OnClick(R.id.mrl_photographer)
    public void photoClick() {
        if(order != null && !TextUtils.isEmpty(order.getSysPrepareDate())){
            return;
        }
        Intent intent = new Intent(context, F4Activity.class);
        intent.putExtra("type", 1);
        intent.putExtra("customer", customer);
        animStartForResult(intent, 205);
    }

    @OnClick(R.id.mrl_dresser)
    public void dresserClick() {
        if(order != null && !TextUtils.isEmpty(order.getHzsPrepareDate())){
            return;
        }
        Intent intent = new Intent(context, F4Activity.class);
        intent.putExtra("type", 7);
        intent.putExtra("customer", customer);
        animStartForResult(intent, 205);
    }

    @OnClick(R.id.mrl_cameraman)
    public void cameraClick() {
        if(order != null && !TextUtils.isEmpty(order.getSxsPrepareDate())){
            return;
        }
        Intent intent = new Intent(context, F4Activity.class);
        intent.putExtra("type", 5);
        intent.putExtra("customer", customer);
        animStartForResult(intent, 205);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == 200 && requestCode == 205){
            HttpClient.getInstance().searchOrderByBridegroomPhone(customer.getBridegroomPhone(), cb);
        }else if(resultCode == 200 && requestCode == 208){
            try {
                customer = db.findById(Customer.class, customer.getId());
                if(customer != null) {
                    brideGroomNameTxt.setText(customer.getBridegroomName());
                    brideGroomPhoneTxt.setText(customer.getBridegroomPhone());
                    brideNameTxt.setText(customer.getBrideName());
                    bridePhoneTxt.setText(customer.getBridePhone());
                    dateTxt.setText(customer.getDate());
                    hotelTxt.setText(customer.getHotel());
                }
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize() {

        //
        db = DbUtils.create(context);
        db.configAllowTransaction(true);

        //
        customer = (Customer) getIntent().getSerializableExtra("customer");

        waitDialog = new WaitDialog.Builder(context).create();
        waitDialog.setCancelable(false);

        refreshLayout.setOnRefreshListener(this);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_collectmoney, null);
        tipsTxt = (TextView) view.findViewById(R.id.collect_txt);
        ViewHolder holder = new ViewHolder(view);
        confirmDialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_tips_head_layout)
                .setFooter(R.layout.dialog_foot_layout)
                .setCancelable(false)
                .setOnClickListener(this)
                .setGravity(MTDialog.Gravity.CENTER)
                .create();

        //
        if(customer != null){
            brideGroomNameTxt.setText(customer.getBridegroomName());
            brideGroomPhoneTxt.setText(customer.getBridegroomPhone());
            brideNameTxt .setText(customer.getBrideName());
            bridePhoneTxt.setText(customer.getBridePhone());
            dateTxt.setText(customer.getDate());
            hotelTxt.setText(customer.getHotel());

            HttpClient.getInstance().searchOrderByBridegroomPhone(customer.getBridegroomPhone(), cb);
        }

    }

    private Callback<OrderResp> cb = new Callback<OrderResp>() {
        @Override
        public void success(OrderResp orderResp, Response response) {
            refreshLayout.setRefreshing(false);
            if(waitDialog != null && waitDialog.isShowing()) waitDialog.dismiss();
            if(orderResp.getCode() == 200) {
                if(orderResp.getData() != null) {
                    order = orderResp.getData().get(0);
                    if(!TextUtils.isEmpty(order.getZcrPrepareDate())) {//主持人
                        emceeNonTxt.setVisibility(View.GONE);
                        emceeNameTxt.setVisibility(View.VISIBLE);
                        emceeNameTxt.setText(order.getZcrEmpName());
                        if(order.getZcrStatus() == 2) {
                            emceeReleaseBtn.setVisibility(View.VISIBLE);
                        }else {
                            emceeReleaseBtn.setVisibility(View.GONE);
                        }
                    }else {
                        emceeNonTxt.setVisibility(View.VISIBLE);
                        emceeReleaseBtn.setVisibility(View.GONE);
                        emceeNameTxt.setVisibility(View.GONE);
                    }

                    //摄影师
                    if(!TextUtils.isEmpty(order.getSysPrepareDate())) {
                        photoNonTxt.setVisibility(View.GONE);
                        photoNameTxt.setVisibility(View.VISIBLE);
                        photoNameTxt.setText(order.getSysEmpName());
                        if(order.getSysStatus() == 2) {
                            photoReleaseBtn.setVisibility(View.VISIBLE);
                        }else {
                            photoReleaseBtn.setVisibility(View.GONE);
                        }
                    }else {
                        photoNameTxt.setVisibility(View.GONE);
                        photoNonTxt.setVisibility(View.VISIBLE);
                        photoReleaseBtn.setVisibility(View.GONE);
                    }

                    //化妆师
                    if(!TextUtils.isEmpty(order.getHzsPrepareDate())) {
                        dresserNonTxt.setVisibility(View.GONE);
                        dresserNameTxt.setVisibility(View.VISIBLE);
                        dresserNameTxt.setText(order.getHzsEmpName());
                        if(order.getHzsStatus() == 2) {
                            dresserReleaseBtn.setVisibility(View.VISIBLE);
                        }else {
                            dresserReleaseBtn.setVisibility(View.GONE);
                        }
                    }else {
                        dresserNonTxt.setVisibility(View.VISIBLE);
                        dresserNameTxt.setVisibility(View.GONE);
                        dresserReleaseBtn.setVisibility(View.GONE);
                    }

                    //摄像师
                    if(!TextUtils.isEmpty(order.getSxsPrepareDate())) {
                        cameraNonTxt.setVisibility(View.GONE);
                        cameraNameTxt.setVisibility(View.VISIBLE);
                        cameraNameTxt.setText(order.getSxsEmpName());
                        if(order.getSxsStatus() == 2) {
                            cameraReleaseBtn.setVisibility(View.VISIBLE);
                        }else {
                            cameraReleaseBtn.setVisibility(View.GONE);
                        }
                    }else {
                        cameraNameTxt.setVisibility(View.GONE);
                        cameraNonTxt.setVisibility(View.VISIBLE);
                        cameraReleaseBtn.setVisibility(View.GONE);
                    }
                }
            }else {
                T.sLong(context, "获取数据错误!");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if(waitDialog != null && waitDialog.isShowing()) waitDialog.dismiss();
            refreshLayout.setRefreshing(false);
            T.s(context, "获取数据失败");
        }
    };

    @OnClick(R.id.m_title_left_btn)
    public void back() {
        animFinish();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel_Btn:
                dialog.dismiss();
                break;
            case R.id.dialog_confrim_btn:
                try {
                    db.delete(customer);
                    T.s(context, "删除成功");
                    animFinish();
                } catch (DbException e) {
                    e.printStackTrace();
                    T.s(context, "删除失败");
                }
                break;
        }
    }

    @Override
    public void onRefresh() {
        waitDialog.show();
        HttpClient.getInstance().searchOrderByBridegroomPhone(customer.getBridegroomPhone(), cb);
    }
}
