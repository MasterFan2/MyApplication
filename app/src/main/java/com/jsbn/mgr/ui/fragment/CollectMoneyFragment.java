package com.jsbn.mgr.ui.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Planner;
import com.jsbn.mgr.net.entity.PlannerResp;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.ui.base.OnRecyclerItemClickListener;
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
public class CollectMoneyFragment extends BaseFragment implements OnRecyclerItemClickListener, OnClickListener,  SwipeRefreshLayout.OnRefreshListener {

    private List<Planner> dataList;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyAdapter adapter;

    private MTDialog dialog;
    private WaitDialog waitDialog;

    private TextView tipsTxt;

    public static CollectMoneyFragment newInstance(String... params) {
        CollectMoneyFragment fragment = new CollectMoneyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initialize() {
        refreshLayout.setOnRefreshListener(this);

        //init description dialog
        waitDialog = new WaitDialog.Builder(getActivity()).create();
        waitDialog.setCancelable(false);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_collectmoney, null);
        tipsTxt = (TextView) view.findViewById(R.id.collect_txt);
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
            if(isRefresh)  {
                refreshLayout.setRefreshing(false);
                isRefresh = false;
            }
            if (plannerResp.getData() != null && plannerResp.getCode() == 200) {
                dataList = plannerResp.getData();
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "获取数据错误");
            if(isRefresh)  {
                refreshLayout.setRefreshing(false);
                isRefresh = false;
            }
        }
    };

    @Override
    public void onRecyclerItemClick(View v, int position) {

    }

    //------------------------
    private int currentPosition = -1;

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel_Btn:
                dialog.dismiss();
                break;
            case R.id.dialog_confrim_btn:
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
            if(baseEntity.getCode() == 200){
                T.s(getActivity(), "操作成功");
                if (currentPosition != -1) {
                    dataList.remove(currentPosition);
                    recyclerView.getAdapter().notifyItemRemoved(currentPosition);
                    currentPosition = -1;
                }
            }else if(baseEntity.getCode() == 3001007){
                T.s(getActivity(), "该订单已被确认");
                HttpClient.getInstance().getAllPreparedList(cb);

            }else if(baseEntity.getCode() == 3001005){
                T.s(getActivity(), "该订单已无效");
                HttpClient.getInstance().getAllPreparedList(cb);
            }
        }

        @Override
        public void failure(RetrofitError error) {
            T.s(getActivity(), "操作失败");
        }
    };


    /**
     * 获取小时分
     *
     * @param strDate
     * @return
     */
    private String getHHmm(String strDate) {
        String temp = strDate.split(" ")[1];
        temp = temp.substring(0, temp.indexOf(":")) + "时" + temp.substring(temp.indexOf(":") + 1, temp.lastIndexOf(":"))+ "分";
        return temp;
    }

    /**
     * 获取月，日
     *
     * @param strDate
     * @return
     */
    private String getMMdd(String strDate) {
        String temp = strDate.split(" ")[0];
        String MM = temp.substring(temp.indexOf("-") + 1, temp.lastIndexOf("-"));
        String dd = temp.substring(temp.lastIndexOf("-") + 1, temp.length());

        if (MM.length() == 2) {
            if (MM.charAt(0) == '0') {
                MM = String.valueOf(MM.charAt(1));
            }
        }

        if (dd.length() == 2) {
            if (dd.charAt(0) == '0') {
                dd = String.valueOf(dd.charAt(1));
            }
        }

        return MM + "月" + dd + "日";
    }

    //----------------------------------------
    private boolean isRefresh = false;
    @Override
    public void onRefresh() {
        //
        isRefresh = true;
        HttpClient.getInstance().getAllPreparedList(cb);
    }

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
            holder.nameTxt.setText(planner.getSkillTypeName() + "：" + planner.getName());

            holder.dateTxt.setText("预定日期：" + getMMdd(planner.getLockDate()));
            holder.plannerNameTxt.setText("统筹师：" + planner.getTcsPersonName());
            holder.plannerDateTxt.setText("锁定时间：" + getHHmm(planner.getTcsPrepareDate()));
            holder.descTxt.setText("备注：" + planner.getRemark());

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
                        currentPosition = getLayoutPosition();
                        final Planner planner = dataList.get(currentPosition);
                        tipsTxt.setText("锁定("+planner.getSkillTypeName() + "：" + planner.getName()+")的订单，请确认");
                        dialog.show();
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
