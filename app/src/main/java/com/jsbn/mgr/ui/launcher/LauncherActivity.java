package com.jsbn.mgr.ui.launcher;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.PreferenceUtils;
import com.jsbn.mgr.net.entity.MemberResp;
import com.jsbn.mgr.net.entity.Members;
import com.jsbn.mgr.net.entity.Version;
import com.jsbn.mgr.net.entity.VersionInfo;
import com.jsbn.mgr.ui.HomeActivity;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.ui.login.LoginActivity;
import com.jsbn.mgr.utils.DateUtil;
import com.jsbn.mgr.utils.NetSpeed;
import com.jsbn.mgr.utils.PackageUtil;
import com.jsbn.mgr.utils.S;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.probutton.SubmitProcessButton;
import com.jsbn.mgr.widget.util.PreUtil;
import com.jsbn.mgr.widget.velocimeter.VelocimeterView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_launcher)
public class LauncherActivity extends BaseActivity implements OnClickListener {

    private String saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/jsbn_mgr.apk";
    private String url = null;// "https://download.alicdn.com/wangwang/AliQinTao(1.70.02U).exe";


    private Handler mHandler;

    private MTDialog dialog;

    private MViewHolder viewHolder;

    private VersionInfo info;

    private HttpHandler handler = null;
    private long mProgress;

    private NetSpeed speed;

//    private static final char last2byte = (char) Integer.parseInt("00000011", 2);
//    private static final char last4byte = (char) Integer.parseInt("00001111", 2);
//    private static final char last6byte = (char) Integer.parseInt("00111111", 2);
//    private static final char lead6byte = (char) Integer.parseInt("11111100", 2);
//    private static final char lead4byte = (char) Integer.parseInt("11110000", 2);
//    private static final char lead2byte = (char) Integer.parseInt("11000000", 2);
//    private static final char[] encodeTable = new char[] { 'A', 'B', 'C', 'D',
//            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
//            'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
//            'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
//            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
//            '4', '5', '6', '7', '8', '9', '+', '/'
//    };

//    public static String standardURLEncoder(String data, String key) {
//        byte[] byteHMAC = null;
//        String urlEncoder = "";
//        try {
//            Mac mac = Mac.getInstance("HmacSHA1");
//            SecretKeySpec spec = new SecretKeySpec(key.getBytes(), "HmacSHA1");
//            mac.init(spec);
//            byteHMAC = mac.doFinal(data.getBytes());
//            if (byteHMAC != null) {
//                String oauth = encode(byteHMAC);
//                if (oauth != null) {
//                    urlEncoder = URLEncoder.encode(oauth, "utf8");
//                }
//            }
//        } catch (InvalidKeyException e1) {
//            e1.printStackTrace();
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//        return urlEncoder;
//    }
//
//    public static String encode(byte[] from) {
//        StringBuffer to = new StringBuffer((int) (from.length * 1.34) + 3);
//        int num = 0;
//        char currentByte = 0;
//        for (int i = 0; i < from.length; i++) {
//            num = num % 8;
//            while (num < 8) {
//                switch (num) {
//                    case 0:
//                        currentByte = (char) (from[i] & lead6byte);
//                        currentByte = (char) (currentByte >>> 2);
//                        break;
//                    case 2:
//                        currentByte = (char) (from[i] & last6byte);
//                        break;
//                    case 4:
//                        currentByte = (char) (from[i] & last4byte);
//                        currentByte = (char) (currentByte << 2);
//                        if ((i + 1) < from.length) {
//                            currentByte |= (from[i + 1] & lead2byte) >>> 6;
//                        }
//                        break;
//                    case 6:
//                        currentByte = (char) (from[i] & last2byte);
//                        currentByte = (char) (currentByte << 4);
//                        if ((i + 1) < from.length) {
//                            currentByte |= (from[i + 1] & lead4byte) >>> 4;
//                        }
//                        break;
//                }
//                to.append(encodeTable[currentByte]);
//                num += 6;
//            }
//        }
//        if (to.length() % 4 != 0) {
//            for (int i = 4 - to.length() % 4; i > 0; i--) {
//                to.append("=");
//            }
//        }
//        return to.toString();
//    }


    @Override
    public void initialize() {

        //----------------------------------
        HttpClient.getInstance().upgradeCheck(2, PackageUtil.getVersion(context), 2, cb);

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    //speedTxt.setText(msg.obj.toString());
                    //lt.setText("当前速度 ["+msg.obj.toString()+"]");
                    viewHolder.velocimeterView.setValue(msg.arg1);
                }
            }
        };

        speed = new NetSpeed(this, mHandler);
        speed.startCalculateNetSpeed();

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_signup_tips_layout, null);
        View footerView = LayoutInflater.from(context).inflate(R.layout.dialog_launcher_foot_layout, null);
        viewHolder = new MViewHolder(view, footerView);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_signup_tips_head_layout)
                .setFooter(footerView)
                .setCancelable(false)
                .setGravity(MTDialog.Gravity.CENTER)
                .setOnClickListener(LauncherActivity.this)
                .create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        speed.stopCalculateNetSpeed();
    }

    private void autoLogin(){
        String account = PreUtil.getLoginAccount(context);
        String password= PreUtil.getLoginPwd(context);
        HttpClient.getInstance().login(account, password, autoLoginCallback);
    }

    private Callback<MemberResp> autoLoginCallback = new Callback<MemberResp>() {
        @Override
        public void success(MemberResp members, Response response) {


            if(members.getCode() == 200){

                Config.members = members.getData().get(0);

                List<Header> headers= response.getHeaders();
                for (Header header : headers){
                    if(header.getName().equals("BTOKEN")){

                        PreferenceUtils.saveValue(context,
                                PreferenceUtils.PREFERENCE_B_TOKEN,
                                PreferenceUtils.DataType.STRING,
                                header.getValue());
                        break;
                    }
                }
                finish();
                animStart(HomeActivity.class);

            }else if(members.getCode() == 1001011) {
                T.s(context, "用户名密码错误");
                animStart(LoginActivity.class);
                finish();
            }else if(members.getCode() == 1001020){
                T.s(context, "该用户不存在 ");
                animStart(LoginActivity.class);
                finish();
            } else {
                T.s(context, "登录失败 ");
                animStart(LoginActivity.class);
                finish();
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private void checkLogin() {
        final boolean isLogin = PreUtil.isLogin(context);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Intent intent = null;
                if (isLogin)
//                    intent = new Intent(context, HomeActivity.class);
                autoLogin();
                else {
                    intent = new Intent(context, LoginActivity.class);
                    animStart(intent);
                    finish();
                }

                //

            }
        }.sendEmptyMessageDelayed(0, 1500);
    }

    private Callback<Version> cb = new Callback<Version>() {
        @Override
        public void success(Version version, Response response) {
            if (version.getCode() == 200) {
                if (version.getData() == null) checkLogin();
                else {
                    info = version.getData();

                    if(TextUtils.isEmpty(info.getAppVersion())) checkLogin();
                    else if(info.getIsForcedUpdate() == 1){//force upgrade
                        viewHolder.descTxt.setText("(您的版本过旧，需要更新版本了)\n\n" + info.getAppInfo().replace("@", "\n"));
                        dialog.show();
                    }else {
                        viewHolder.descTxt.setText("有新版本更新了\n\n" + info.getAppInfo().replace("@", "\n"));
                        dialog.show();
                    }
                }
            } else {
                T.s(context, "服务器维护中， 请稍后再试");
            }
        }

        @Override
        public void failure(RetrofitError error) {
             T.s(context, "服务器维护中， 请稍后再试");
        }
    };

    class MViewHolder {
        SubmitProcessButton processButton;
        Button cancelBtn;
        TextView descTxt;
        VelocimeterView velocimeterView;

        public MViewHolder(View view, View footer){
            velocimeterView = (VelocimeterView) view.findViewById(R.id.velocimeter);
            cancelBtn = (Button) footer.findViewById(R.id.version_cancel_Btn);
            descTxt = (TextView) view.findViewById(R.id.signup_tips_txt);
            processButton = (SubmitProcessButton) footer.findViewById(R.id.version_update_btn);
        }
    }

    @Override
    public boolean onKeydown() {
        return false;
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        if(view.getId() == R.id.version_update_btn){
            viewHolder.velocimeterView.animate().translationX(-viewHolder.velocimeterView.getWidth()).setInterpolator(new DecelerateInterpolator(2)).start();

            url      = info.getAppUrl();
            saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url.substring(url.lastIndexOf("/") + 1, url.length());

            viewHolder.cancelBtn.setEnabled(false);

            download(requestCallBack, url, saveFile);
        }else if(view.getId() == R.id.version_cancel_Btn){
            dialog.dismiss();
            if(info.getIsForcedUpdate() == 1){
                finish();
            }else{
                checkLogin();
            }
//            viewHolder.velocimeterView.animate().translationX(viewHolder.velocimeterView.getWidth()).setInterpolator(new DecelerateInterpolator(2)).start();

        }
    }

    /**
     * 开始下载
     * @param callBack
     * @param url
     * @param saveFile
     */
    public void download(RequestCallBack<File> callBack, String url, String saveFile){
        File file = new File(saveFile);
        if(file.exists())
            file.delete();

        HttpUtils http = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addHeader("Referer", "http://www.jsbn.love");//防盗链
        http.download(url,saveFile,params,true, true, callBack);
//        handler = http.download(url,saveFile,
//                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
//                true, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
//                callBack);
    }

    private RequestCallBack<File> requestCallBack = new RequestCallBack<File>() {

        @Override
        public void onStart() {
            viewHolder.processButton.setText("即将下载...");
//            lt.setText("即将下载...");
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            mProgress = current * 100 / total;
            viewHolder.processButton.setProgress((int) mProgress);
            viewHolder.processButton.setText(mProgress + " %");
        }

        @Override
        public void onSuccess(ResponseInfo<File> fileResponseInfo) {
            T.s(context, "下载完成, 点击安装");
            viewHolder.processButton.setEnabled(true);
            viewHolder.cancelBtn.setEnabled(true);
            viewHolder.processButton.setText("点击安装");
            viewHolder.processButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Intent intent = new Intent();
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setAction(android.content.Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(saveFile)), "application/vnd.android.package-archive");
                    startActivityForResult(intent, 205);
                }
            });

            viewHolder.velocimeterView.animate().translationX(viewHolder.velocimeterView.getWidth()).setInterpolator(new DecelerateInterpolator(2)).setDuration(1800).start();
//            lt.success();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            viewHolder.processButton.setText("下载失败");
            T.s(context, "下载失败" + s);
//            lt.error();
        }
    };
}
