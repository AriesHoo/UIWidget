package com.aries.ui.widget.demo.module.tab;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.tab.SegmentTabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.widget.MsgView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

/**
 * @Author: AriesHoo on 2019/4/16 17:47
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link SegmentTabLayout}示例
 * @Description:
 */
public class SegmentTabActivity extends BaseActivity {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息"};
    private String[] mTitles_2 = {"首页", "消息", "联系人"};
    private String[] mTitles_3 = {"首页", "消息", "联系人", "更多"};
    private View mDecorView;
    private SegmentTabLayout mTabLayout_3;
    private SegmentTabLayout tabLayout_4;

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_segment_tab;
    }

    @Override
    protected void initView(Bundle var1) {
        for (String title : mTitles_3) {
            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
        }

        for (String title : mTitles_2) {
            mFragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }

        mDecorView = getWindow().getDecorView();

        SegmentTabLayout tabLayout_1 = FindViewUtil.getTargetView(mDecorView, R.id.tl_1);
        SegmentTabLayout tabLayout_2 = FindViewUtil.getTargetView(mDecorView, R.id.tl_2);
        mTabLayout_3 = FindViewUtil.getTargetView(mDecorView, R.id.tl_3);
        tabLayout_4 = FindViewUtil.getTargetView(mDecorView, R.id.tl_4);
        SegmentTabLayout tabLayout_5 = FindViewUtil.getTargetView(mDecorView, R.id.tl_5);

        tabLayout_1.setTabData(mTitles);
        tabLayout_2.setTabData(mTitles_2);
        tl_3();
        tabLayout_4.setTabData(mTitles_2, this, R.id.fl_change, mFragments2);
        tabLayout_5.setTabData(mTitles_3);

        //显示未读红点
        tabLayout_1.showDot(1);
        tabLayout_2.showDot(2);
        mTabLayout_3.showDot(1);
        tabLayout_4.showDot(1);

        //设置未读消息红点
        mTabLayout_3.showDot(2);
        MsgView rtv_3_2 = mTabLayout_3.getMsgView(2);
        if (rtv_3_2 != null) {
            rtv_3_2.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tabLayout_4.setCurrentTab(2);
    }

    private void tl_3() {
        final ViewPager vp_3 = FindViewUtil.getTargetView(mDecorView, R.id.vp_2);
        vp_3.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        mTabLayout_3.setTabData(mTitles_3);
        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                Toast.makeText(SegmentTabActivity.this, "onTabSelect:" + position, Toast.LENGTH_SHORT).show();
                vp_3.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                Toast.makeText(SegmentTabActivity.this, "onTabReselect:" + position, Toast.LENGTH_SHORT).show();
            }
        });

        vp_3.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_3.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout_3.setCurrentTab(0);

        findViewById(R.id.btn_eventTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SegmentTabActivity.this, EventBusActivity.class));
            }
        });
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles_3[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    /**
     * 主要演示 刷新上一页面tab时 Fragment 不同时切换问题{@link com.aries.ui.view.tab.utils.FragmentChangeManager#setFragments(int)}
     * 原{@link FragmentTransaction#commit()}修改为{@link FragmentTransaction#commitAllowingStateLoss()}
     *
     * @param index
     */
    @Subscriber(mode = ThreadMode.MAIN, tag = "switchTab")
    public void switchTab(final int index) {
        tabLayout_4.setCurrentTab(index == tabLayout_4.getCurrentTab() ? 1 : index);
    }
}
