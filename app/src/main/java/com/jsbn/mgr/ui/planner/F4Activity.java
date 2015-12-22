package com.jsbn.mgr.ui.planner;

import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.BaseEntity;
import com.jsbn.mgr.net.entity.Customer;
import com.jsbn.mgr.net.entity.Member;
import com.jsbn.mgr.net.entity.MyMemberResp;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.utils.PinyinUtil;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.dialog.WaitDialog;
import com.jsbn.mgr.widget.indexer.FancyIndexer;
import com.jsbn.mgr.widget.masterfan.MasterTitleView;
import com.jsbn.mgr.widget.picker.TimePopupWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

@ActivityFeature(layout = R.layout.activity_f4)
public class F4Activity extends BaseActivity implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<Member> dataList;

    @Bind(R.id.titleView)
    MasterTitleView titleView;

    @Bind(R.id.lv_content)
    ListView contentListView;
    private MyAdapter myAdapter;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.tv_index_center)
    TextView indexCenter;

    @Bind(R.id.bar)
    FancyIndexer mFancyIndexer;

    @Bind(R.id.planner_search_edit)
    EditText editText;

    private static final String[] ITEMS = {"主持人", "摄影师", "造型师", "摄像师"};

    TimePopupWindow pwTime;

    private WaitDialog dialog;

    TextView tipsTxt;

    //--------------------
    private int type;//emcee, photographer, dresser, cameraman
    private Customer customer;//
    //----------------------

    private MTDialog selecteDialog;
    private Member currentSelected;

    @OnClick(R.id.planner_search_txt)
    public void searchClick() {
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
            if (memberTempList.size() > 0) memberTempList.clear();
            memberTempList.addAll(dataList);
            if (memberTempList.size() > 0) Collections.sort(memberTempList);
            myAdapter.setDatas(memberTempList);
            myAdapter.notifyDataSetChanged();
            return;
        }

        memberTempList.clear();

        for (Member member : dataList) {
            if (member.getName().contains(s) || member.getName().equalsIgnoreCase(s)) {
                memberTempList.add(member);
            }
        }

        if (memberTempList.size() > 0) Collections.sort(memberTempList);
        myAdapter.setDatas(memberTempList);
        myAdapter.notifyDataSetChanged();
    }

    //-----------------------------------
    private List<Member> memberTempList = new ArrayList<>();

    @Override
    public void initialize() {
        type = getIntent().getIntExtra("type", 0);
        titleView.setTitleText("选择" + getOccupationByTypeId(type));
        customer = (Customer) getIntent().getSerializableExtra("customer");

        refreshLayout.setOnRefreshListener(this);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_collectmoney, null);
        tipsTxt = (TextView) view.findViewById(R.id.collect_txt);
        ViewHolder holder = new ViewHolder(view);
        selecteDialog = new MTDialog.Builder(context)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_tips_head_layout)
                .setFooter(R.layout.dialog_foot_layout)
                .setCancelable(false)
                .setOnClickListener(this)
                .setGravity(MTDialog.Gravity.CENTER)
                .create();

        dialog = new WaitDialog.Builder(context).create();
        dialog.setCancelable(false);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberTempList.clear();

                if (TextUtils.isEmpty(s)) {
                    memberTempList.addAll(dataList);
                    myAdapter.notifyDataSetChanged();
                    return;
                }

                for (Member member : dataList) {
                    if (member.getName().contains(s) || member.getName().equalsIgnoreCase(s.toString())) {
                        memberTempList.add(member);
                    }
                }

                if (memberTempList.size() > 0) Collections.sort(memberTempList);
                myAdapter.setDatas(memberTempList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myAdapter = new MyAdapter();
        contentListView.setAdapter(myAdapter);

        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
                for (int i = 0; i < memberTempList.size(); i++) {
                    Member goodMan = memberTempList.get(i);
                    String s = goodMan.getPinyin().charAt(0) + "";
                    if (TextUtils.equals(s, letter)) {
                        // 匹配成功, 中断循环, 跳转到i位置
                        contentListView.setSelection(i);
                        break;
                    }
                }
            }
        });

        //
        HttpClient.getInstance().getPersonByPlannerNameDate(Config.members.getEmpNo(), customer.getDate(), cb);
    }

    Callback<MyMemberResp> cb = new Callback<MyMemberResp>() {
        @Override
        public void success(MyMemberResp memberResp, Response response) {

            if (isRefresh) {
                refreshLayout.setRefreshing(false);
                isRefresh = false;
            }
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (memberResp.getData() != null && memberResp.getCode() == 200) {
                if (dataList == null) dataList = new ArrayList<>();
                for (Member member : memberResp.getData().getPersons()) {
                    member.setPinyin(PinyinUtil.getPinyin(member.getName()));
                    if (member.getSkillTypeId() == type) {
                        dataList.add(member);
                    }
                }

                // 排序
                Collections.sort(dataList);
                memberTempList.clear();
                memberTempList.addAll(dataList);
                myAdapter.setDatas(memberTempList);
                myAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void failure(RetrofitError error) {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            if (isRefresh) {
                refreshLayout.setRefreshing(false);
                isRefresh = false;
            }
            T.s(context, "获取数据错误");
        }
    };

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()) {
            case R.id.dialog_cancel_Btn:
                dialog.dismiss();
                break;
            case R.id.dialog_confrim_btn:

                if(currentSelected != null) {
                    String remark = "婚期:" + customer.getDate() + "\n" +
                            "酒店:" + customer.getHotel() + "\n" +
                            "新郎:" + customer.getBridegroomName() + "\t" + "电话:" + customer.getBridegroomPhone() + "\n" +
                            "新娘:" + customer.getBrideName() + "\t" + "电话:" + customer.getBridePhone() + "\n";

                    HttpClient.getInstance().plannerScheduleF4(Config.members.getPersonId(), currentSelected.getPersonId(), customer.getDate(), remark, customer.getHotel(), Config.members.getName(), customer.getBridegroomName()
                            , customer.getBridegroomPhone(), customer.getBrideName(), customer.getBridePhone(), cBack);
                }else {
                    T.s(context, "请重新选择!");
                }
                break;
        }
    }

    //----------------------------------------
    private boolean isRefresh = false;

    @Override
    public void onRefresh() {
        isRefresh = true;
        if (dataList != null) dataList.clear();
        HttpClient.getInstance().getPersonByPlannerNameDate(Config.members.getEmpNo(), customer.getDate(), cb);
    }

    public class MyAdapter extends BaseAdapter {

        private List<Member> datas;

        public List<Member> getDatas() {
            return datas;
        }

        public void setDatas(List<Member> datas) {
            this.datas = datas;
        }

        public MyAdapter() {
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Member member = datas.get(position);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(context, R.layout.item_f4, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            // 进行分组, 比较上一个拼音的首字母和自己是否一致, 如果不一致, 就显示tv_index
            String currentLetter = member.getPinyin().charAt(0) + "";
            String indexStr = null;
            if (position == 0) {
                // 1. 如果是第一位
                indexStr = currentLetter;
            } else {
                // 获取上一个拼音
                String preLetter = datas.get(position - 1).getPinyin().charAt(0) + "";
                if (!TextUtils.equals(currentLetter, preLetter)) {
                    // 2. 当跟上一个不同时, 赋值, 显示
                    indexStr = currentLetter;
                }
            }

            holder.tv_index.setVisibility(indexStr == null ? View.GONE : View.VISIBLE);
            holder.tv_index.setText(indexStr);
            holder.tv_occupation.setText(getOccupationByTypeId(member.getSkillTypeId()));
            holder.tv_name.setText(member.getName());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tipsTxt.setText("是否确认   " +  getOccupationByTypeId(member.getSkillTypeId()) + "-" + member.getName() + " ?");
                    selecteDialog.show();
                    currentSelected = member;
//                    String remark = "婚期:" + customer.getDate() + "\n" +
//                                    "酒店:" + customer.getHotel() + "\n" +
//                                    "新郎:" + customer.getBridegroomName() + "\t" + "电话:" + customer.getBridegroomPhone() + "\n" +
//                                    "新娘:" + customer.getBrideName() + "\t" + "电话:" + customer.getBridePhone() + "\n";
//
//                    HttpClient.getInstance().plannerScheduleF4(Config.members.getPersonId(), member.getPersonId(), customer.getDate(),  remark,customer.getHotel(), Config.members.getName(), customer.getBridegroomName()
//                    ,customer.getBridegroomPhone(), customer.getBrideName(), customer.getBridePhone(), cBack);
                }
            });
            return convertView;
        }

        class ViewHolder {
            public TextView tv_index;
            public TextView tv_name;
            public TextView tv_occupation;
            public MaterialRippleLayout layout;

            public ViewHolder(View view) {
                tv_index = (TextView) view.findViewById(R.id.tv_index);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                tv_occupation = (TextView) view.findViewById(R.id.tv_occupation);
                layout = (MaterialRippleLayout) view.findViewById(R.id.planner_layout);
            }
        }
    }

    private Callback<BaseEntity> cBack = new Callback<BaseEntity>() {
        @Override
        public void success(BaseEntity baseEntity, Response response) {
            if (baseEntity.getCode() == 200) {
                T.sLong(context, "操作成功! 此档期将锁定一定时间");
                currentSelected = null;
                setResult(200);
                animFinish();
            } else if (baseEntity.getCode() == 3001017) {
                T.s(context, "该档期已被占用!");
            } else {
                T.s(context, "操作失败!");
            }
        }

        @Override
        public void failure(RetrofitError error) {

        }
    };

    private String getOccupationByTypeId(int typeId) {
        String str = "";
        switch (typeId) {
            case 1:
                str = "摄影师";
                break;
            case 5:
                str = "摄像师";
                break;
            case 6:
                str = "主持人";
                break;
            case 7:
                str = "化妆师";
                break;
            default:
                str = "摄影师";
                break;
        }
        return str;
    }

    @Override
    public boolean onKeydown() {
        animFinish();
        return false;
    }

    @OnClick(R.id.m_title_left_btn)
    public void back() {
        animFinish();
    }

}
