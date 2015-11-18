package com.jsbn.mgr.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

import com.jsbn.mgr.R;
import com.jsbn.mgr.config.Config;
import com.jsbn.mgr.ui.base.ActivityFeature;
import com.jsbn.mgr.ui.base.BaseActivity;
import com.jsbn.mgr.ui.fragment.CalendarFragment;
import com.jsbn.mgr.ui.fragment.CollectMoneyFragment;
import com.jsbn.mgr.ui.fragment.PersonalFragment;
import com.jsbn.mgr.ui.fragment.PlannerFragment;

import butterknife.Bind;

/**
 * 主界面
 */
@ActivityFeature(layout = R.layout.activity_home)
public class HomeActivity extends BaseActivity {

    private String[] filters = null;
    private int[] icons = null;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    private Fragment fragments[] = new Fragment[2];

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String currentSelectName = "";

    @Override
    public void initialize() {
        if (Config.members.getPersonType() == 1) {
            if (Config.members.getSkillTypeId() == 12) {//收银员
                filters = new String[]{"待确认", "个人中心"};
                fragments[0] = CollectMoneyFragment.newInstance("");
                icons = new int[]{R.mipmap.ic_money, R.mipmap.ic_person};
            } else if (Config.members.getSkillTypeId() == 3) {//统筹师
                filters = new String[]{"四大人员", "个人中心"};
                fragments[0] = PlannerFragment.newInstance("");
                icons = new int[]{R.mipmap.ic_planner, R.mipmap.ic_person};
            }else {
                fragments = new Fragment[1];
                filters =  new String[]{"个人中心"};
                fragments[0]= PersonalFragment.newInstance("");
                icons = new int[]{R.mipmap.ic_person};
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.add(R.id.container, fragments[0]);

                fragmentTransaction.show(fragments[0]).commit();

                for (int i = 0; i < filters.length; i++) {
                    TabLayout.Tab tab = tabLayout.newTab();
                    tab.setText(filters[i]);
                    tab.setIcon(icons[i]);
                    tabLayout.addTab(tab);
                }

                tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        doFiltrate(tab);
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {

                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
                return;
            }
        } else if (Config.members.getPersonType() == 2) {//四大金刚
            fragments[0] = CalendarFragment.newInstance("");
            filters = new String[]{"我的档期", "个人中心"};
            icons = new int[]{R.mipmap.ic_calendar, R.mipmap.ic_person};
        }else {
            fragments[0] = CalendarFragment.newInstance("");
            filters = new String[]{"我的档期", "个人中心"};
            icons = new int[]{R.mipmap.ic_calendar, R.mipmap.ic_person};
        }

        fragments[1] = PersonalFragment.newInstance("");

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.container, fragments[0]);
        fragmentTransaction.add(R.id.container, fragments[1]);

        fragmentTransaction.hide(fragments[1]).show(fragments[0]).commit();

        for (int i = 0; i < filters.length; i++) {
            TabLayout.Tab tab = tabLayout.newTab();
            tab.setText(filters[i]);
            tab.setIcon(icons[i]);
            tabLayout.addTab(tab);
        }

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                doFiltrate(tab);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void doFiltrate(TabLayout.Tab tab) {
        String tName = tab.getText().toString();
        if (tName.equals(currentSelectName)) {
            return;
        }
        currentSelectName = tName;
        fragmentTransaction = fragmentManager.beginTransaction();
        if (tName.equals(filters[0])) {
            fragmentTransaction.hide(fragments[1]).show(fragments[0]).commit();
        } else if (tName.equals(filters[1])) {
            fragmentTransaction.hide(fragments[0]).show(fragments[1]).commit();
        }

    }

    @Override
    public boolean onKeydown() {
        //
        if (currentSelectName.equals(filters[0])) {
            if(fragments[0] instanceof CalendarFragment) {
                ((CalendarFragment) fragments[0]).dialogShow();
                return false;
            }
        }

        //
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
        return true;
    }


    public interface OnBackpress {
        void onBackpress();
    }

    class MPagerApdater extends FragmentPagerAdapter {

        public MPagerApdater(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "我的档期";
            } else if (position == 1) {
                return "个人中心";
            }

            return "null";
        }

        @Override
        public int getCount() {
            return fragments == null ? 0 : fragments.length;
        }
    }
}
