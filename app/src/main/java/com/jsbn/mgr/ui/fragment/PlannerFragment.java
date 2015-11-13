package com.jsbn.mgr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
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
import com.jsbn.mgr.net.entity.Member;
import com.jsbn.mgr.net.entity.MyMemberResp;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.ui.planner.PlannerScheduleActivity;
import com.jsbn.mgr.utils.PinyinUtil;
import com.jsbn.mgr.utils.T;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.dialog.MTDialog;
import com.jsbn.mgr.widget.dialog.OnClickListener;
import com.jsbn.mgr.widget.dialog.ViewHolder;
import com.jsbn.mgr.widget.dialog.WaitDialog;
import com.jsbn.mgr.widget.indexer.FancyIndexer;
import com.jsbn.mgr.widget.picker.TimePopupWindow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * 统筹师
 * Created by 13510 on 2015/11/10.
 */
@FragmentFeature(layout = R.layout.fragment_planner)
public class PlannerFragment extends BaseFragment implements OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private List<Member> dataList;

    @Bind(R.id.lv_content)
    ListView contentListView;
    private MyAdapter myAdapter;

    @Bind(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @Bind(R.id.tv_index_center)
    TextView indexCenter;

    @Bind(R.id.fragment_date_txt)
    TextView dateTxt;

    @Bind(R.id.fragment_selected_txt)
    TextView selectText;

    @Bind(R.id.bar)
    FancyIndexer mFancyIndexer;

//    @Bind(R.id.planner_spinner)
//    MaterialSpinner spinner;
//    ArrayAdapter adapter;

    @Bind(R.id.planner_search_edit)
    EditText editText;

    private static final String[] ITEMS = {"主持人", "摄影师", "造型师", "摄像师"};

    TimePopupWindow pwTime;

    private WaitDialog dialog;

    private MTDialog selecteDialog;

    @OnClick(R.id.fragment_selected_txt)
    public  void selectedClick(){
        selecteDialog.show();
    }

    @OnClick(R.id.fragment_date_txt)
    public void dateClick() {
        pwTime.showAtLocation(dateTxt, Gravity.BOTTOM, 0, 0, new Date());
    }

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
    private int spinnerCurrentSelected = -1;
    private List<Member> memberTempList = new ArrayList<>();

    @Override
    public void initialize() {

        refreshLayout.setOnRefreshListener(this);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_selected, null);
        ViewHolder holder = new ViewHolder(view);
        selecteDialog = new MTDialog.Builder(getActivity())
                .setContentHolder(holder)
                .setCancelable(false)
                .setOnClickListener(this)
                .setGravity(MTDialog.Gravity.BOTTOM)
                .create();

        dialog = new WaitDialog.Builder(getActivity()).create();
        dialog.setCancelable(false);


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberTempList.clear();

                if(TextUtils.isEmpty(s)){
                    memberTempList.addAll(dataList);
                    myAdapter.notifyDataSetChanged();
                    return;
                }

                for (Member member : dataList) {
                    if (member.getName().contains(s) || member.getName().equalsIgnoreCase(s.toString())) {
                        memberTempList.add(member);
                    }
                }

                if(memberTempList.size() > 0) Collections.sort(memberTempList);
                myAdapter.setDatas(memberTempList);
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        myAdapter = new MyAdapter();
        contentListView.setAdapter(myAdapter);

        //时间选择器
        pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setTime(new Date());
        pwTime.setDefaultRange();

        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if(date == null) {
                    dateTxt.setText("筛选档期");
                    HttpClient.getInstance().getPersonByPlannerName(Config.members.getEmpNo(), cb);
                }else {
                    dialog.show();
                    //
                    dateTxt.setText(new SimpleDateFormat("MM月dd").format(date));
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    HttpClient.getInstance().getPersonByPlannerNameDate(Config.members.getEmpNo(), sdf.format(date), cb);
                }
            }
        });


//        fillAndSortData(persons);

        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                System.out.println("letter: " + letter);
//				Util.showToast(getApplicationContext(), letter);

//				showLetter(letter);

                // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
                for (int i = 0; i < dataList.size(); i++) {
                    Member goodMan = dataList.get(i);
                    String s = goodMan.getPinyin().charAt(0) + "";
                    if (TextUtils.equals(s, letter)) {
                        // 匹配成功, 中断循环, 跳转到i位置
                        contentListView.setSelection(i);
                        break;
                    }
                }
            }
        });

        HttpClient.getInstance().getPersonByPlannerName(Config.members.getEmpNo(), cb);
    }

    Callback<MyMemberResp> cb = new Callback<MyMemberResp>() {
        @Override
        public void success(MyMemberResp memberResp, Response response) {

            if(isRefresh)  {
                refreshLayout.setRefreshing(false);
                isRefresh = false;
            }
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            if (memberResp.getData() != null && memberResp.getCode() == 200) {
//                dataList = plannerResp.getData();
                dataList = memberResp.getData().getPersons();
                for (Member member : dataList) {
                    member.setPinyin(PinyinUtil.getPinyin(member.getName()));
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
            if(dialog != null && dialog.isShowing()) dialog.dismiss();
            T.s(getActivity(), "获取数据错误");
        }
    };

    public static PlannerFragment newInstance(String param1s) {
        PlannerFragment fragment = new PlannerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(MTDialog dialog, View view) {
        switch (view.getId()){
            case R.id.dialog_selected_all_txt:
                memberTempList.clear();
                memberTempList.addAll(dataList);
                selectText.setText("四大金刚");
                break;
            case R.id.dialog_selected_emcee_txt://主持人
                memberTempList.clear();
                selectText.setText("主持人");
                for (Member member : dataList) {
                    if (member.getSkillTypeId() == 6) {
                        memberTempList.add(member);
                    }
                }
                break;
            case R.id.dialog_selected_dresser_txt://化妆师
                memberTempList.clear();
                selectText.setText("化妆师");
                for (Member member : dataList) {
                    if (member.getSkillTypeId() == 7) {
                        memberTempList.add(member);
                    }
                }
                break;
            case R.id.dialog_selected_photographer_txt:
                memberTempList.clear();
                selectText.setText("摄影师");
                for (Member member : dataList) {
                    if (member.getSkillTypeId() == 1) {
                        memberTempList.add(member);
                    }
                }
                break;
            case R.id.dialog_selected_cameraman_txt:
                memberTempList.clear();
                selectText.setText("摄像师");
                for (Member member : dataList) {
                    if (member.getSkillTypeId() == 5) {
                        memberTempList.add(member);
                    }
                }
                break;
        }

        dialog.dismiss();
        if (memberTempList.size() > 0) Collections.sort(memberTempList);
        myAdapter.setDatas(memberTempList);
        myAdapter.notifyDataSetChanged();
    }

    //----------------------------------------
    private boolean isRefresh = false;
    @Override
    public void onRefresh() {
        isRefresh = true;
        HttpClient.getInstance().getPersonByPlannerName(Config.members.getEmpNo(), cb);
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
                convertView = View.inflate(getActivity(), R.layout.item_f4, null);
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
                String preLetter = datas.get(position -1 ).getPinyin().charAt(0) + "";
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
                    Intent intent = new Intent(getActivity(), PlannerScheduleActivity.class);
                    intent.putExtra("personId", member.getPersonId() + "");
                    animStart(intent);
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

    private String getOccupationByTypeId(int typeId) {
        String str = "";
        switch (typeId) {
            case 1:
                str = "摄影师";
            break;
            case 5:
                str =  "摄像师";
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
}
