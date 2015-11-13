package com.jsbn.mgr.ui.login;

import android.text.TextUtils;
import android.view.View;

import com.jsbn.mgr.R;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.common.Button;
import com.jsbn.mgr.widget.common.EditText;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_update_pwd)
public class UpdatePwdActivity extends BaseActivity {

    @Bind(R.id.update_oldPwd_edit)
    EditText oldPwdEdit;

    @Bind(R.id.update_newPwd_edit)
    EditText newPwdEdit;

    @Bind(R.id.update_btn)
    Button updateBtn;

    @OnClick(R.id.update_btn)
    public void update(View view) {

        view.setEnabled(false);
        String oldPwd = oldPwdEdit.getText().toString();
        String newPwd = newPwdEdit.getText().toString();

        if (TextUtils.isEmpty(oldPwd)) {
            oldPwdEdit.setError("请输入旧密码");
            view.setEnabled(true);
            return;
        } else {
            oldPwdEdit.clearError();
        }

        if (TextUtils.isEmpty(newPwd)) {
            newPwdEdit.setError("请输入新密码");
            view.setEnabled(true);
            return;
        }

        if (newPwd.length() < 6) {
            newPwdEdit.setError("新密码长度在六位以上.");
            view.setEnabled(true);
            return;
        }
        newPwdEdit.clearError();

        //
        HttpClient.getInstance().updatePassword(newPwd, oldPwd, cb);
    }

    private Callback<BaseEntity> cb = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {
            updateBtn.setEnabled(true);
            if (baseEntity.getCode() == 200) {
                T.s(context, "修改密码成功");
                animFinish();
            } else if(baseEntity.getCode() == 1001021) {
                T.s(context, "原始密码错误");
            } else {
                T.s(context, "修改密码失败");
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(context, "修改密码失败，请稍后重试.");
            updateBtn.setEnabled(true);
        }
    };

    @Override
    public void initialize() {

    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return true;
    }
}
