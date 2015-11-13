package com.jsbn.mgr.ui.login;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

//@ActivityFeature(layout = R.layout.activity_forget)
//public class ForgetActivity extends MBaseFragmentActivity implements OnClickListener {
//
//    @ViewInject(R.id.forget_phone_edit)
//    private EditText phoneEdit;
//
//    @ViewInject(R.id.forget_code_edit)
//    private EditText codeEdit;
//
////    @ViewInject(R.id.forget_newPwd_edit)
////    private EditText newEdit;
////
////    @ViewInject(R.id.forget_againPwd_edit)
////    private EditText againEdit;
//
//    private MTDialog dialog;
//
//    @ViewInject(R.id.forget_confirm_btn)
//    private Button confirmBtn;
//
//    @ViewInject(R.id.forget_getCode_btn)
//    private Button getCodeBtn;
//
//    //重发间隔
//    private int reSend = 60;
//    private boolean isRun = false;
//    private Timer timer;
//    private TimerTask task;
//
//    /**
//     *开始计时
//     */
//    private void startTimer(){
//        if(timer == null)
//            timer = new Timer();
//        if(task == null)
//            task = new TimerTask() {
//                @Override
//                public void run() {
//                    handler.sendEmptyMessage(0);
//                }
//            };
//
//        timer.schedule(task, 0, 1000);
//    }
//
//    /**
//     * //////////////////////
//     */
//    private Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//
//            reSend -- ;
//            if(reSend <= 0){
//                getCodeBtn.setEnabled(true);
//                getCodeBtn.setText("获取验证码");
//                timer.cancel();
//                task = null;
//                timer = null;
//                isRun = false;
//                reSend = 60;
//                return;
//            }
//            getCodeBtn.setText((reSend < 10 ? "0" + reSend : reSend) + " 秒后重发");
//        }
//    };
//
//    @Override
//    public void initialize() {
//        phoneEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    String phone = phoneEdit.getText().toString();
//                    if (!TextUtils.isEmpty(phone)) {
//                        confirmBtn.setEnabled(true);
//                        confirmBtn.applyStyle(R.style.RaiseColorButtonRippleStyle);
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * get validate code
//     * @param view
//     */
//    @OnClick(R.id.forget_getCode_btn)
//    public void getCode(View view) {
//
//        view.setEnabled(false);
//
//        String phone = phoneEdit.getText().toString();
//        if (TextUtils.isEmpty(phone)) {
//            phoneEdit.setError("请输入手机号");
//            view.setEnabled(true);
//            return;
//        }
//        if (!MatcherUtil.isPhone(phone)) {
//            phoneEdit.setError("手机号格式错误，请检查!");
//            view.setEnabled(true);
//            return;
//        } else {
//            phoneEdit.clearError();
//        }
//
//        startTimer();
//
//        isRun = true;
//
//        HttpClient.getInstance().getSmsCode(phone, 2, cb);
//    }
//
//    /**
//     *
//     * @param view
//     */
//    @OnClick(R.id.forget_confirm_btn)
//    public void confirm(View view){
//
//        String phone        = phoneEdit.getText().toString();
//        String validateCode = codeEdit.getText().toString();
//
//        //
//        if (TextUtils.isEmpty(phone)) {
//            phoneEdit.setError("请输入手机号");
//            view.setEnabled(true);
//            return;
//        }
//        if (!MatcherUtil.isPhone(phone)) {
//            phoneEdit.setError("手机号格式错误，请检查!");
//            view.setEnabled(true);
//            return;
//        } else {
//            phoneEdit.clearError();
//        }
//
//        //
//        if (TextUtils.isEmpty(validateCode)) {
//            codeEdit.setError("请输入验证码");
//            return;
//        }else{
//            codeEdit.clearError();
//        }
//
//
//        HttpClient.getInstance().forget(phone, validateCode, forgetCallback);
//    }
//
//    /**
//     * validate code call back
//     */
//    private Callback<BaseResp> forgetCallback = new Callback<BaseResp>() {
//        @Override
//        public void success(BaseResp baseResp, Response response) {
//            if(baseResp.getCode() == 200){
//                View view = LayoutInflater.from(context).inflate(R.layout.dialog_signup_tips_layout, null);
//                TextView tipsTxt = (TextView) view.findViewById(R.id.signup_tips_txt);
//                tipsTxt.setText("恭喜你，找回密码成功！稍后我们会将您的新密码通过短信方式发送到您的手机，登录后请及时修改您的密码。");
//                ViewHolder holder = new ViewHolder(view);
//                dialog = new MTDialog.Builder(context)
//                        .setContentHolder(holder)
//                        .setHeader(R.layout.dialog_signup_tips_head_layout)
//                        .setFooter(R.layout.dialog_signup_tips_foot_layout)
//                        .setCancelable(false)
//                        .setGravity(MTDialog.Gravity.CENTER)
//                        .setOnClickListener(ForgetActivity.this)
//                        .create();
//                dialog.show();
//
//            }else{
//                phoneEdit.setEnabled(false);
//                T.s(context, "找回密码失败,请稍后重试");
//            }
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            T.s(context, "修改密码失败! 请稍后重试");
//        }
//    };
//
//    /**
//     * validate code call back
//     */
//    private Callback<BaseResp> cb = new Callback<BaseResp>() {
//        @Override
//        public void success(BaseResp baseResp, Response response) {
//            if(baseResp.getCode() == 200){
//                T.s(context, "短信发送成功,请注意查收!");
//            }else {
//                T.s(context, "短信发送失败!请检查网络");
//            }
//        }
//
//        @Override
//        public void failure(RetrofitError error) {
//            T.s(context, "短信发送失败!请检查网络");
//        }
//    };
//
//    @Override
//    protected void onDestroy() {
//        if(isRun){
//            timer.cancel();
//            task.cancel();
//        }
//        task = null;
//        timer = null;
//        super.onDestroy();
//    }
//
//    @OnClick(R.id.m_title_left_btn)
//    public void back(View view){
//        animFinish();
//    }
//
//    @Override
//    public boolean onKeydown() {
//        animFinish();
//        return true;
//    }
//
//    @Override
//    public void onClick(MTDialog dialog, View view) {
//        if(view.getId() == R.id.footer_confirm_button){
//            dialog.dismiss();
//            animFinish();
//        }
//    }
//}
