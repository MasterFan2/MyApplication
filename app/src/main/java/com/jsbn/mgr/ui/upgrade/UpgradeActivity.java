package com.jsbn.mgr.ui.upgrade;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.net.entity.VersionInfo;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.NetSpeed;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.expandlayout.ExpandableRelativeLayout;
import com.jsbn.mgr.widget.probutton.SubmitProcessButton;
import com.jsbn.mgr.widget.velocimeter.VelocimeterView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

@ActivityFeature(layout = R.layout.activity_upgrede,no_title = true)
public class UpgradeActivity extends BaseActivity {

    private Handler mHandler;
    private NetSpeed speed;

    private VersionInfo info;

    private String saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/venus.apk";
    private String url      ;

    @Bind(R.id.version_update_btn)
    SubmitProcessButton button;

    @Bind(R.id.version_cancel_Btn)
    Button cancelBtn;

    @Bind(R.id.version_description_txt)
    TextView description;

    @Bind(R.id.velocimeter)
    VelocimeterView velocimeterView;

    @Bind(R.id.expandableLayout)
    ExpandableRelativeLayout expandableRelativeLayout;

    private long mProgress;
    private HttpHandler handler = null;


    @OnClick(R.id.version_cancel_Btn)
    public void cancel(View view){
        finish();
    }

    @Override
    public void initialize() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 1) {
                    //speedTxt.setText(msg.obj.toString());
                    //lt.setText("当前速度 ["+msg.obj.toString()+"]");
                    velocimeterView.setValue(msg.arg1);
                }
            }
        };

        speed = new NetSpeed(this, mHandler);
        speed.startCalculateNetSpeed();

//        info = (VersionInfo) getIntent().getSerializableExtra("info");
//
//        description.setText(info.getAppInfo().replace("@", "\n"));
//        url      = info.getAppUrl();
//        saveFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + url.substring(url.lastIndexOf("/") + 1, url.length());
    }

    @OnClick(R.id.version_update_btn)
    public void upgrade(View view){
        //download(requestCallBack, url, saveFile);

        expandableRelativeLayout.toggle();

        //velocimeterView.animate().alpha(1f).setInterpolator(new DecelerateInterpolator()).setDuration(800).start();
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
        handler = http.download(url,saveFile,
                true, // 如果目标文件存在，接着未完成的部分继续下载。服务器不支持RANGE时将从新下载。
                false, // 如果从请求返回信息中获取到文件名，下载完成后自动重命名。
                callBack);
    }


    private RequestCallBack<File> requestCallBack = new RequestCallBack<File>() {

        @Override
        public void onStart() {
            button.setText("即将下载...");
//            lt.setText("即将下载...");
        }

        @Override
        public void onLoading(long total, long current, boolean isUploading) {
            mProgress = current * 100 / total;
            button.setProgress((int) mProgress);
            button.setText(mProgress + " %");
        }

        @Override
        public void onSuccess(ResponseInfo<File> fileResponseInfo) {
            T.s(context, "下载完成, 点击安装");
            button.setEnabled(true);
            button.setText("点击安装");
            button.setOnClickListener(new View.OnClickListener() {
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
            new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    velocimeterView.animate().alpha(0f).setInterpolator(new DecelerateInterpolator())
                            .setDuration(500).start();
                }
            }.sendEmptyMessageDelayed(1, 3000);
//            lt.success();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            button.setText("下载失败");
            T.s(context, "下载失败" + s);
//            lt.error();
        }
    };

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }

    @Override
    protected void onDestroy() {
        speed.stopCalculateNetSpeed();
        super.onDestroy();
    }
}
