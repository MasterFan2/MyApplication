package com.jsbn.mgr.ui.member;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.Member;
import com.jsbn.mgr.net.entity.MemberResp;
import com.jsbn.mgr.net.entity.MyMemberResp;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.ui.base.OnRecyclerItemClickListener;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.dialog.WaitDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_my_member)
public class MyMemberActivity extends BaseActivity implements OnRecyclerItemClickListener{

    @Bind(R.id.toolBar)
    Toolbar toolbar;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyAdapter adapter;

    private WaitDialog dialog;

    private ArrayList<Member> dataList = null;

    @Override
    public void initialize() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dialog = new WaitDialog.Builder(context).create();
        dialog.setCancelable(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        toolbar.setSubtitle("点击可查看档期");
        adapter = new MyAdapter(MyMemberActivity.this);
        recyclerView.setAdapter(adapter);

        dialog.show();
        HttpClient.getInstance().getPersonByPlannerName(Config.members.getEmpNo(), cb);
    }

    private Callback<MyMemberResp> cb = new Callback<MyMemberResp>() {
        @Override
        public void success(MyMemberResp memberResp, Response response) {
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            if(memberResp.getCode() == 200 && memberResp.getData() != null){
                dataList = memberResp.getData().getPersons();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
        }
    };

    @Override
    public void onRecyclerItemClick(View view, int position) {
        Intent intent = new Intent(context, MemberScheduleActivity.class);
        intent.putExtra("personId", dataList.get(position).getPersonId()+"");
        animStart(intent);
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;
        public MyAdapter(OnRecyclerItemClickListener listener){
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_member, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            final Member member = dataList.get(position);

            holder.nameTxt.setText("姓名：" + member.getName());
            holder.genderTxt.setText("性别：" + member.getGender());
            holder.professionTxt.setText("职业：" + member.getSkillTypeName());

            //加载内容
            if(!TextUtils.isEmpty(member.getHeadUrl())){
                Picasso.with(context).load(member.getHeadUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.headImg);
            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @Bind(R.id.item_name_txt)
            TextView nameTxt;

            @Bind(R.id.item_gender_txt)
            TextView genderTxt;

            @Bind(R.id.item_profession_txt)
            TextView professionTxt;

            @Bind(R.id.item_head_img)
            ImageView headImg;

            @Bind(R.id.member_root_layout)
            MaterialRippleLayout rootLayout;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);

                ButterKnife.bind(this, itemView);

                onRecyclerItemClickListener = listener;
                rootLayout.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }

    @Override
    public boolean onKeydown() {

        if(dialog != null && dialog.isShowing()){
            return false;
        }
        animFinish();
        return true;
    }
}
