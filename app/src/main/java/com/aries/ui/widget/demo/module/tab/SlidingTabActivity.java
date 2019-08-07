package com.aries.ui.widget.demo.module.tab;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.tab.IndicatorStyle;
import com.aries.ui.view.tab.SlidingTabLayout;
import com.aries.ui.view.tab.TextBold;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.widget.MsgView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.SizeUtil;

import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * @Author: AriesHoo on 2019/4/16 17:49
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link SlidingTabLayout}示例
 * @Description:
 */
public class SlidingTabActivity extends BaseActivity implements OnTabSelectListener {
    private Context mContext = this;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private final String[] mTitles = {
            "热门", "iOS", "Android"
            , "前端", "后端", "设计", "工具资源"
    };
    private MyPagerAdapter mAdapter;
    private SlidingTabLayout tabLayout_2;

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sliding_tab;
    }

    @Override
    protected void initView(Bundle var1) {
        for (String title : mTitles) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }


        View decorView = getWindow().getDecorView();
        ViewPager vp = FindViewUtil.getTargetView(decorView, R.id.vp);
        mAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(mAdapter);

        /** 默认 */
        SlidingTabLayout tabLayout_1 = FindViewUtil.getTargetView(decorView, R.id.tl_1);
        /**自定义部分属性*/
        tabLayout_2 = FindViewUtil.getTargetView(decorView, R.id.tl_2);
        /** 字体加粗,大写 */
        SlidingTabLayout tabLayout_3 = FindViewUtil.getTargetView(decorView, R.id.tl_3);
        /** tab固定宽度 */
        SlidingTabLayout tabLayout_4 = FindViewUtil.getTargetView(decorView, R.id.tl_4);
        /** indicator固定宽度 */
        SlidingTabLayout tabLayout_5 = FindViewUtil.getTargetView(decorView, R.id.tl_5);
        /** indicator圆 */
        SlidingTabLayout tabLayout_6 = FindViewUtil.getTargetView(decorView, R.id.tl_6);
        /** indicator矩形圆角 */
        final SlidingTabLayout tabLayout_7 = FindViewUtil.getTargetView(decorView, R.id.tl_7);
        /** indicator三角形 */
        SlidingTabLayout tabLayout_8 = FindViewUtil.getTargetView(decorView, R.id.tl_8);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_9 = FindViewUtil.getTargetView(decorView, R.id.tl_9);
        /** indicator圆角色块 */
        SlidingTabLayout tabLayout_10 = FindViewUtil.getTargetView(decorView, R.id.tl_10);

        tabLayout_1.setViewPager(vp);
        tabLayout_2.setViewPager(vp);
        tabLayout_2.setOnTabSelectListener(this);
        tabLayout_3.setViewPager(vp);
        tabLayout_4.setViewPager(vp);
        tabLayout_5.setViewPager(vp);
        tabLayout_6.setViewPager(vp);
        tabLayout_7.setViewPager(vp, mTitles);
        tabLayout_8.setViewPager(vp, mTitles, this, mFragments);
        tabLayout_9.setViewPager(vp);
        tabLayout_10.setViewPager(vp);

        vp.setCurrentItem(4);

        tabLayout_1.showDot(4);
        tabLayout_3.showDot(4);
        tabLayout_2.showDot(4);

        tabLayout_2.showMsg(3, 5);
        tabLayout_2.setMsgMargin(3, 0, 10);
        MsgView rtv_2_3 = tabLayout_2.getMsgView(3);
        if (rtv_2_3 != null) {
            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
        }

        tabLayout_2.showMsg(5, 5);
        tabLayout_2.setMsgMargin(5, 0, 10);

        SlidingTabLayout slidingTabLayout = new SlidingTabLayout(mContext);
        slidingTabLayout.getDelegate()
                .setIndicatorColor(Color.BLUE)
                .setIndicatorGravity(Gravity.BOTTOM)
                .setIndicatorHeight(3f)
                .setIndicatorStyle(IndicatorStyle.NORMAL)
                .setIndicatorWidthEqualTitle(false)
                .setTabPadding(6f)
                .setTextBold(TextBold.SELECT)
                .setTextSelectColor(Color.BLUE)
                .setTabSpaceEqual(true)
                .setTextUnSelectColor(Color.parseColor("#99333333"))
                .setTextSelectSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                .setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f);

        ((ViewGroup) tabLayout_1.getParent()).addView(slidingTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, SizeUtil.dp2px(48)));
        slidingTabLayout.setViewPager(vp, mTitles);
        slidingTabLayout.setOnTabSelectListener(this);

        findViewById(R.id.btn_eventTab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SlidingTabActivity.this, EventBusActivity.class));
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        Toast.makeText(mContext, "onTabSelect&position--->" + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTabReselect(int position) {
        Toast.makeText(mContext, "onTabReselect&position--->" + position, Toast.LENGTH_SHORT).show();
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

    /**
     * 主要演示 刷新上一页面tab时 Fragment 不同时切换问题{@link com.aries.ui.view.tab.utils.FragmentChangeManager#setFragments(int)}
     * 原{@link FragmentTransaction#commit()}修改为{@link FragmentTransaction#commitAllowingStateLoss()}
     *
     * @param index
     */
    @Subscriber(mode = ThreadMode.MAIN, tag = "switchTab")
    public void switchTab(final int index) {
        tabLayout_2.setCurrentTab(index == tabLayout_2.getCurrentTab() ? 1 : index);
    }
}
