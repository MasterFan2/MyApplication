package com.jsbn.mgr.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.net.HttpClient;
import com.jsbn.mgr.net.entity.Customer;
import com.jsbn.mgr.net.entity.Member;
import com.jsbn.mgr.ui.base.BaseFragment;
import com.jsbn.mgr.ui.base.FragmentFeature;
import com.jsbn.mgr.ui.base.OnRecyclerItemClickListener;
import com.jsbn.mgr.ui.planner.AddCustomerActivity;
import com.jsbn.mgr.ui.planner.CustomerSelectF4Activity;
import com.jsbn.mgr.utils.PinyinUtil;
import com.jsbn.mgr.utils.S;
import com.jsbn.mgr.widget.MaterialRippleLayout;
import com.jsbn.mgr.widget.indexer.FancyIndexer;
import com.jsbn.mgr.widget.picker.TimePopupWindow;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的客户
 * Created by 13510 on 2015/11/21.
 */
@FragmentFeature(layout = R.layout.fragment_customer)
public class CustomerFragment extends BaseFragment implements OnRecyclerItemClickListener {

    private List<Customer> dataList;
    private MyAdapter adapter;
    private DbUtils db;


    private String strSelctedDate = "";

    @Bind(R.id.recyclerView)
    ListView listView;

    @Bind(R.id.customer_add_txt)
    TextView addTxt;

    @Bind(R.id.bar)
    FancyIndexer mFancyIndexer;

    @Bind(R.id.planner_search_edit)
    EditText editText;

    @Bind(R.id.customer_date_txt)
    TextView dateTxt;

    TimePopupWindow pwTime;

    private List<Customer> memberTempList = new ArrayList<>();

    @OnClick(R.id.customer_date_txt)
    public void dateClick() {
        pwTime.showAtLocation(editText, Gravity.BOTTOM, 0, 0, new Date());
    }

    @Override
    public void initialize() {

        //时间选择器
        pwTime = new TimePopupWindow(getActivity(), TimePopupWindow.Type.YEAR_MONTH_DAY);
        pwTime.setTime(new Date());
        pwTime.setDefaultRange();

        //时间选择后回调
        pwTime.setOnTimeSelectListener(new TimePopupWindow.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {

                if (date == null) {//清除筛选
                    strSelctedDate = null;
                    dateTxt.setText("筛选档期");
                    adapter.setDatas(dataList);
                    adapter.notifyDataSetChanged();
                } else {            //具体时间筛选
                    if(dataList != null && dataList.size() > 0){
                        dateTxt.setText(new SimpleDateFormat("MM月dd").format(date));
                        String str = 1900 + date.getYear() + "-" +  (date.getMonth() + 1) + "-" + date.getDate();
                        strSelctedDate = str;
                        memberTempList.clear();
                        for (Customer customer:dataList){
                            if(customer.getDate().equals(str)){
                                memberTempList.add(customer);
                            }
                        }
                        if(memberTempList.size() > 1) Collections.sort(memberTempList);
                        adapter.setDatas(memberTempList);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });

        db = DbUtils.create(getActivity());
        db.configAllowTransaction(true);

        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                memberTempList.clear();

                if (TextUtils.isEmpty(s)) {
                    memberTempList.addAll(dataList);
                    adapter.notifyDataSetChanged();
                    return;
                }

                for (Customer member : dataList) {
                    if (member.getBridegroomName().contains(s) || member.getBridegroomName().equalsIgnoreCase(s.toString()) ||
                            member.getBrideName().contains(s)  || member.getBrideName().equals(s.toString()) ||
                            member.getBridegroomPhone().contains(s) || member.getBridegroomPhone().equals(s) ||
                            member.getBridePhone().contains(s) || member.getBridePhone().equals(s) ||
                            member.getHotel().contains(s) || member.getHotel().equals(s)             ) {
                        memberTempList.add(member);
                    }
                }

                if (memberTempList.size() > 1) Collections.sort(memberTempList);
                adapter.setDatas(memberTempList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mFancyIndexer.setOnTouchLetterChangedListener(new FancyIndexer.OnTouchLetterChangedListener() {

            @Override
            public void onTouchLetterChanged(String letter) {
                // 从集合中查找第一个拼音首字母为letter的索引, 进行跳转
                for (int i = 0; i < memberTempList.size(); i++) {
                    Customer customer = memberTempList.get(i);
                    String s = customer.getPinyin().charAt(0) + "";
                    if (TextUtils.equals(s, letter)) {
                        // 匹配成功, 中断循环, 跳转到i位置
                        listView.setSelection(i);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            dataList = db.findAll(Customer.class);
            if (dataList != null && dataList.size() > 0) {
                memberTempList.clear();
                if(TextUtils.isEmpty(strSelctedDate)){//没有选择日期
                    for (Customer customer : dataList) {
                        customer.setPinyin(PinyinUtil.getPinyin(customer.getBridegroomName()));
                    }
                    // 排序
                    Collections.sort(dataList);
                    memberTempList.addAll(dataList);
                }else {
                    for (Customer customer:dataList){
                        if(customer.getDate().equals(strSelctedDate)){
                            memberTempList.add(customer);
                        }
                    }
                    if(memberTempList.size() > 1) Collections.sort(memberTempList);
                }
                adapter.setDatas(memberTempList);
            }else{
                adapter.setDatas(null);
            }
            adapter.notifyDataSetChanged();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.customer_add_txt)
    public void addClick() {
        animStart(AddCustomerActivity.class);
    }

    /**
     * init
     *
     * @param params
     * @return
     */
    public static CustomerFragment newInstance(String... params) {
        CustomerFragment fragment = new CustomerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onRecyclerItemClick(View v, int position) {
        Intent intent = new Intent(getActivity(), CustomerSelectF4Activity.class);
        intent.putExtra("customer", dataList.get(position));
        animStart(intent);
    }

    public class MyAdapter extends BaseAdapter {

        public MyAdapter() {
        }

        private List<Customer> datas;

        public void setDatas(List<Customer> datas) {
            this.datas = datas;
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
            MViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_customer, parent, false);
                holder = new MViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (MViewHolder) convertView.getTag();
            }
            //
            final Customer customer = datas.get(position);
            holder.hotelNameTxt.setText(customer.getHotel());
            holder.dateTxt.setText(customer.getDate());
            holder.bridegroomNameTxt.setText(customer.getBridegroomName());
            holder.bridegroomPhoneTxt.setText(customer.getBridegroomPhone());
            holder.brideNameTxt.setText(customer.getBrideName());
            holder.bridePhoneTxt.setText(customer.getBridePhone());
            holder.mrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), CustomerSelectF4Activity.class);
                    intent.putExtra("customer", customer);
                    animStart(intent);
                }
            });
            return convertView;
        }

        public class MViewHolder {

            @Bind(R.id.item_customer_hotel_name_txt)
            TextView hotelNameTxt;

            @Bind(R.id.item_customer_date_txt)
            TextView dateTxt;

            @Bind(R.id.item_customer_bridegroom_txt)
            TextView bridegroomNameTxt;

            @Bind(R.id.item_customer_bridegroom_phone_txt)
            TextView bridegroomPhoneTxt;

            @Bind(R.id.item_customer_bride_name_txt)
            TextView brideNameTxt;

            @Bind(R.id.item_customer_bride_phone_txt)
            TextView bridePhoneTxt;

            @Bind(R.id.item_customer_root)
            MaterialRippleLayout mrl;

            public MViewHolder(View itemView) {
                ButterKnife.bind(this, itemView);
            }
        }
    }

}
