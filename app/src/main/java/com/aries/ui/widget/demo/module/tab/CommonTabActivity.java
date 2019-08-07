package com.aries.ui.widget.demo.module.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.tab.CommonTabLayout;
import com.aries.ui.view.tab.listener.CustomTabEntity;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.utils.UnreadMsgUtils;
import com.aries.ui.view.tab.widget.MsgView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.module.tab.entity.TabEntity;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Random;

/**
 * @Author: AriesHoo on 2019/4/16 17:47
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link CommonTabLayout}示例
 * @Description:
 */
public class CommonTabActivity extends BaseActivity {
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();

    private String[] mTitles = {"首页", "消息", "联系人", "更多"};
    private int[] mIconUnSelectIds = {
            R.mipmap.tab_home_unselect, R.mipmap.tab_speech_unselect,
            R.mipmap.tab_contact_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds = {
            R.mipmap.tab_home_select, R.mipmap.tab_speech_select,
            R.mipmap.tab_contact_select, R.mipmap.tab_more_select};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private View mDecorView;
    private ViewPager mViewPager;
    private CommonTabLayout mTabLayout_1;
    private CommonTabLayout mTabLayout_2;
    private CommonTabLayout mTabLayout_3;
    private CommonTabLayout mTabLayout_4;
    private CommonTabLayout mTabLayout_5;
    private CommonTabLayout mTabLayout_6;
    private CommonTabLayout mTabLayout_7;
    private CommonTabLayout mTabLayout_8;

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_common_tab;
    }

    @Override
    protected void initView(Bundle var1) {

        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance("Switch ViewPager " + title));
            mFragments2.add(SimpleCardFragment.getInstance("Switch Fragment " + title));
        }


        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }

        mDecorView = getWindow().getDecorView();
        mViewPager = FindViewUtil.getTargetView(mDecorView, R.id.vp_2);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        /** with nothing */
        mTabLayout_1 = FindViewUtil.getTargetView(mDecorView, R.id.tl_1);
        /** with ViewPager */
        mTabLayout_2 = FindViewUtil.getTargetView(mDecorView, R.id.tl_2);
        /** with Fragments */
        mTabLayout_3 = FindViewUtil.getTargetView(mDecorView, R.id.tl_3);
        /** indicator固定宽度 */
        mTabLayout_4 = FindViewUtil.getTargetView(mDecorView, R.id.tl_4);
        /** indicator固定宽度 */
        mTabLayout_5 = FindViewUtil.getTargetView(mDecorView, R.id.tl_5);
        /** indicator矩形圆角 */
        mTabLayout_6 = FindViewUtil.getTargetView(mDecorView, R.id.tl_6);
        /** indicator三角形 */
        mTabLayout_7 = FindViewUtil.getTargetView(mDecorView, R.id.tl_7);
        /** indicator圆角色块 */
        mTabLayout_8 = FindViewUtil.getTargetView(mDecorView, R.id.tl_8);

        mTabLayout_1.setTabData(mTabEntities);
        tl_2();
        mTabLayout_3.setTabData(mTabEntities, this, R.id.fl_change, mFragments2);
        mTabLayout_4.setTabData(mTabEntities);
        mTabLayout_5.setTabData(mTabEntities);
        mTabLayout_6.setTabData(mTabEntities);
        mTabLayout_7.setTabData(mTabEntities);
        mTabLayout_8.setTabData(mTabEntities);

        mTabLayout_3.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mTabLayout_1.setCurrentTab(position);
                mTabLayout_2.setCurrentTab(position);
                mTabLayout_4.setCurrentTab(position);
                mTabLayout_5.setCurrentTab(position);
                mTabLayout_6.setCurrentTab(position);
                mTabLayout_7.setCurrentTab(position);
                mTabLayout_8.setCurrentTab(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout_8.setCurrentTab(2);
        mTabLayout_3.setCurrentTab(1);

        //显示未读红点
        mTabLayout_1.showDot(2);
        mTabLayout_3.showDot(1);
        mTabLayout_4.showDot(1);

        //两位数
        mTabLayout_2.showMsg(0, 55);
        mTabLayout_2.setMsgMargin(0, -5, 5);

        //三位数
        mTabLayout_2.showMsg(1, 100);
        mTabLayout_2.setMsgMargin(1, -5, 5);

        //设置未读消息红点
        mTabLayout_2.showDot(2);
        MsgView rtv_2_2 = mTabLayout_2.getMsgView(2);
        if (rtv_2_2 != null) {
            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
        }

        //设置未读消息背景
        mTabLayout_2.showMsg(3, 5);
        mTabLayout_2.setMsgMargin(3, 0, 5);
        MsgView rtv_2_3 = mTabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        findViewById(R.id.btn_eventTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CommonTabActivity.this, EventBusActivity.class));
            }
        });
    }

    Random mRandom = new Random();

    private void tl_2() {
        mTabLayout_2.setTabData(mTabEntities);
        mTabLayout_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mTabLayout_2.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mTabLayout_2.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(1);
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
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 主要演示 刷新上一页面tab时 Fragment 不同时切换问题{@link com.aries.ui.view.tab.utils.FragmentChangeManager#setFragments(int)}
     * 原{@link FragmentTransaction#commit()}修改为{@link FragmentTransaction#commitAllowingStateLoss()}
     *
     * @param index
     */
    @Subscriber(mode = ThreadMode.MAIN, tag = "switchTab")
    public void switchTab(final int index) {
        mTabLayout_3.setCurrentTab(index == mTabLayout_3.getCurrentTab() ? 1 : index);
    }
}
