package com.jsbn.mgr.ui.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.entity.Member;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.ui.login.LoginActivity;
import com.jsbn.mgr.ui.login.UpdatePwdActivity;
import com.jsbn.mgr.ui.member.MyMemberActivity;
import com.jsbn.mgr.widget.util.PreUtil;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by 13510 on 2015/9/14.
 */
@FragmentFeature(layout = R.layout.fragment_personal)
public class PersonalFragment extends BaseFragment {

    @Bind(R.id.name_txt)
    TextView nameTxt;

    @Bind(R.id.profession_txt)
    TextView professionTxt;

    @Bind(R.id.gender_txt)
    TextView gendarTxt;

    @Bind(R.id.memberTxt)
    TextView myTeamTxt;

    @Bind(R.id.modifyTxt)
    TextView modifyPwdTxt;

    @Bind(R.id.logoutBtn)
    Button logoutBtn;

//    @Bind(R.id.head_img)
//    ImageView headImg;

    private Member member;

    @OnClick(R.id.memberTxt)
    public void memebers(){
        animStart(MyMemberActivity.class);
    }

    @OnClick(R.id.modifyTxt)
    public void modify(){
        animStart(UpdatePwdActivity.class);
    }

    @OnClick(R.id.logoutBtn)
    public void logout(){
        PreUtil.clearLoginInfo(getActivity());
        Config.members = null;
        animStart(LoginActivity.class);
        getActivity().finish();
    }

    @Override
    public void initialize() {
        if(Config.members != null){
            member = Config.members;

            nameTxt.setText("姓名：" + member.getName());

            gendarTxt.setText("性别：" + member.getGender());

            professionTxt.setText("职业：" + member.getSkillTypeName());

            if(member.getPersonId() == member.getGroupLeaderId()){//是组长
                myTeamTxt.setVisibility(View.VISIBLE);
            }else{
                myTeamTxt.setVisibility(View.GONE);
            }

//            if(!TextUtils.isEmpty(member.getHeadUrl())) {
//                Picasso.with(getActivity()).load(member.getHeadUrl()).into(headImg);
//            }
        }
    }

    public static PersonalFragment newInstance(String param1s) {
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
}
