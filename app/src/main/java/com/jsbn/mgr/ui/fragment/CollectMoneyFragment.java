package com.jsbn.mgr.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Planner;
import com.jsbn.mgr.net.entity.PlannerResp;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.ui.base.OnRecyclerItemClickListener;
import com.jsbn.mgr.utils.InputMethodUtil;
import com.jsbn.mgr.utils.S;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.dialog.WaitDialog;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 收银
 * Created by 13510 on 2015/11/10.
 */
@FragmentFeature(layout = R.layout.fragment_collect_money)
public class CollectMoneyFragment extends BaseFragment implements OnRecyclerItemClickListener , OnClickListener {

    private List<Planner> dataList;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyAdapter adapter;

    private MTDialog dialog;
    private WaitDialog waitDialog ;

    public static CollectMoneyFragment newInstance(String... params) {
        CollectMoneyFragment fragment = new CollectMoneyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize() {

        //init description dialog
        waitDialog = new WaitDialog.Builder(getActivity()).create();
        waitDialog.setCancelable(false);

        View view     = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_collectmoney, null);
        ViewHolder holder = new ViewHolder(view);
        dialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setFooter(R.layout.dialog_foot_layout)
                .setCancelable(false)
                .setOnClickListener(this)
                .setGravity(MTDialog.Gravity.CENTER)
                .create();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyAdapter(null);
        recyclerView.setAdapter(adapter);

        //
        HttpClient.getInstance().getAllPreparedList(cb);
    }

    Callback<PlannerResp> cb = new Callback<PlannerResp>() {
        @Override
        public void success(PlannerResp plannerResp, Response response) {
            if(plannerResp.getData() != null && plannerResp.getCode() == 200) {
                dataList = plannerResp.getData();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "获取数据错误");
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    //------------------------
    private int currentPosition = -1;
    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_cancel_Btn:
                dialog.dismiss();
                break;
            case  R.id.dialog_confrim_btn:
                dialog.dismiss();
                waitDialog.show();
                HttpClient.getInstance().collectMoneyConfirm(dataList.get(currentPosition).getScheduleId(), confirmCallback);
//                new Handler(){
//                    @Override
//                    public void handleMessage(Message msg) {
//                        waitDialog.dismiss();
//                    }
//                }.sendEmptyMessageDelayed(0, 1900);
                break;
        }
    }

    private Callback<BaseEntity> confirmCallback = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {
            waitDialog.dismiss();
            T.s(getActivity(), "操作成功");
            if(currentPosition != -1) {
                dataList.remove(currentPosition);
                recyclerView.getAdapter().notifyItemRemoved(currentPosition);
                currentPosition = -1;
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "操作失败");
        }
    };

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MViewHolder> {

        private OnRecyclerItemClickListener onRecyclerItemClickListener;

        public MyAdapter(OnRecyclerItemClickListener listener) {
            this.onRecyclerItemClickListener = listener;
        }

        @Override
        public MViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect_money, parent, false);
            return new MViewHolder(itemView, onRecyclerItemClickListener);
        }

        @Override
        public void onBindViewHolder(MViewHolder holder, int position) {
            final Planner planner = dataList.get(position);
//
            holder.nameTxt.setText(planner.getSkillTypeName() +"：" + planner.getName());
            holder.dateTxt.setText("日期：" + planner.getLockDate());
            holder.plannerNameTxt.setText("统筹师：" + planner.getTcsPersonName());
            holder.plannerDateTxt.setText("日期：" + planner.getTcsPrepareDate());
            holder.descTxt.setText("备注:" + planner.getRemark());

//            //加载内容
//            if (!TextUtils.isEmpty(planner.getHeadUrl())) {
//                Picasso.with(getActivity()).load(planner.getHeadUrl()).error(getResources().getDrawable(R.mipmap.ic_launcher)).into(holder.headImg);
//            }
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public final class MViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @Bind(R.id.planner_name_txt)
            TextView plannerNameTxt;

            @Bind(R.id.planner_date_txt)
            TextView plannerDateTxt;

            @Bind(R.id.name_txt)
            TextView nameTxt;

            @Bind(R.id.date_txt)
            TextView dateTxt;

            @Bind(R.id.desc_txt)
            TextView descTxt;

            @Bind(R.id.confirm_layout)
            MaterialRippleLayout confirmLayout;

            OnRecyclerItemClickListener onRecyclerItemClickListener;

            public MViewHolder(View itemView, OnRecyclerItemClickListener listener) {
                super(itemView);

                ButterKnife.bind(this, itemView);

                onRecyclerItemClickListener = listener;
                confirmLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.show();
                        currentPosition = getLayoutPosition();
                    }
                });
            }

            @Override
            public void onClick(View v) {
                onRecyclerItemClickListener.onRecyclerItemClick(v, getLayoutPosition());
            }
        }
    }
}
