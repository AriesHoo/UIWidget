package com.aries.ui.view.tab.utils;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * @Author: AriesHoo on 2018/11/29 16:18
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description: 1、2018-11-29 16:18:47 修改原库初始化判断{@link #initFragments()}
 * 2、2019-4-17 11:18:20 修改tab时 Fragment 不同时切换问题{@link com.aries.ui.view.tab.utils.FragmentChangeManager#setFragments(int)}
 * * 原{@link FragmentTransaction#commit()}修改为{@link FragmentTransaction#commitAllowingStateLoss()}
 */
public class FragmentChangeManager {
    private FragmentManager mFragmentManager;
    private int mContainerViewId;
    /**
     * Fragment切换数组
     */
    private ArrayList<Fragment> mFragments;
    /**
     * 当前选中的Tab
     */
    private int mCurrentTab;

    public FragmentChangeManager(FragmentManager fm, int containerViewId, ArrayList<Fragment> fragments) {
        this.mFragmentManager = fm;
        this.mContainerViewId = containerViewId;
        this.mFragments = fragments;
        initFragments();
    }

    /**
     * 初始化fragments
     */
    private void initFragments() {
        for (Fragment fragment : mFragments) {
            if (fragment.isAdded()) {
                mFragmentManager.beginTransaction().hide(fragment).commit();
            } else {
                mFragmentManager.beginTransaction().add(mContainerViewId, fragment).hide(fragment).commit();
            }
        }
        setFragments(0);
    }

    /**
     * 界面切换控制
     */
    public void setFragments(int index) {
        for (int i = 0; i < mFragments.size(); i++) {
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            Fragment fragment = mFragments.get(i);
            if (i == index) {
                ft.show(fragment);
            } else {
                ft.hide(fragment);
            }
            //解决当前页面设置上一页面currentTab Fragment不同时切换问题
            //一定几率报  IllegalStateException: Can not perform this action after onSaveInstanceState 异常
            ft.commitAllowingStateLoss();
        }
        mCurrentTab = index;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public Fragment getCurrentFragment() {
        return mFragments.get(mCurrentTab);
    }
}