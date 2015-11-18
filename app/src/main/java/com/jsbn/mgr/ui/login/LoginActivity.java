package com.jsbn.mgr.ui.login;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.PreferenceUtils;
import com.jsbn.mgr.net.entity.MemberResp;
import com.jsbn.mgr.net.entity.Members;
import com.jsbn.mgr.ui.HomeActivity;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.common.EditText;
import com.jsbn.mgr.widget.imageview.SimpleTagImageView;
import com.jsbn.mgr.widget.util.PreUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_login, statusBarColor = R.color.gray_900)
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_account_edit)
    EditText accountEdit;

    @Bind(R.id.login_password_edit)
    EditText passwordEdit;

//    @Bind(R.id.toolBar)
//    Toolbar toolbar;

    @Bind(R.id.login_success_layout)
    LinearLayout headLayout;

    @Bind(R.id.login_success_account_txt)
    TextView loginSuccessAccountTxt;

    @Bind(R.id.login_success_head_img)
    SimpleTagImageView loginSuccessHeadImg;

    @Bind(R.id.login_otherAccount_txt)
    TextView otherTxt;

    private String account;
    private String password;
    private PreUtil.LoginSucessInfo info;

    private AlertDialog dialog;

    @OnClick(R.id.login_otherAccount_txt)
    public void other(View view){
        view.setVisibility(View.GONE);
        accountEdit.setVisibility(View.VISIBLE);
        headLayout.setVisibility(View.GONE);
        info = null;
    }


    @OnClick(R.id.login_login_btn)
    public void click(View view) {

        if(info != null){
            account = info.getAccount();
        }else{
            account = accountEdit.getText().toString();
            if (TextUtils.isEmpty(account)) {
                accountEdit.setError("请输入用户名");
                view.setEnabled(true);
                return;
            } else {
                accountEdit.clearError();
            }
        }

        password = passwordEdit.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordEdit.setError("请输入密码");
            view.setEnabled(true);
            return;
        }

        if (password.length() < 6) {
            passwordEdit.setError("密码长度在六位以上.");
            view.setEnabled(true);
            return;
        }

        passwordEdit.clearError();
        HttpClient.getInstance().login(account, password, cb);
        dialog.show();
    }

    private Callback<MemberResp> cb = new Callback<MemberResp>() {
        @Override
        public void success(MemberResp memberResp, Response response) {
            dialog.dismiss();
            if (memberResp.getCode() == 200) {
                Config.members = memberResp.getData().get(0);
                List<Header> headers = response.getHeaders();
                for (Header header : headers) {
                    if (header.getName().equals("BTOKEN")) {

                        PreferenceUtils.saveValue(context,
                                PreferenceUtils.PREFERENCE_B_TOKEN,
                                PreferenceUtils.DataType.STRING,
                                header.getValue());
                        break;
                    }
                }
                T.s(context, "登录成功 ");
                PreUtil.saveLoginInfo(context, account, password);
                PreUtil.saveLoginSuccessInfo(context, account, memberResp.getData().get(0).getHeadUrl());

                finish();
                animStart(HomeActivity.class);

            } else if (memberResp.getCode() == 1001011) {
                T.s(context, "用户名密码错误");
            } else if(memberResp.getCode() == 1001020){
                T.s(context, "用户名不存在");
            }else if(memberResp.getCode() == 500){
                T.s(context, "服务器错误(code=500)");
            }else {
                T.s(context, "登录失败 ");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            dialog.dismiss();
            T.s(context, "服务器维护中...");
        }
    };

    @Override
    public void initialize() {
        info = PreUtil.getLoginSuccessInfo(context);
        if(info != null){
            headLayout.setVisibility(View.VISIBLE);
            loginSuccessAccountTxt.setText(info.getAccount());
            if(!TextUtils.isEmpty(info.getUrl())){
                Picasso.with(context).load(info.getUrl()).into(loginSuccessHeadImg);
            }
            otherTxt.setVisibility(View.VISIBLE);
            accountEdit.setVisibility(View.GONE);
        }else{
            headLayout.setVisibility(View.GONE);
            accountEdit.setVisibility(View.VISIBLE);
            otherTxt.setVisibility(View.GONE);
        }

        //
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading_layout, null);
        dialog = new AlertDialog.Builder(context).setView(view).setCancelable(false).create();
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

}
