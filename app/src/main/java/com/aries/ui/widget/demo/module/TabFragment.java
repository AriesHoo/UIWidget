package com.aries.ui.widget.demo.module;

import android.os.Bundle;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseFragment;

/**
 * @Author: AriesHoo on 2019/4/12 11:29
 * @E-Mail: AriesHoo@126.com
 * @Function: Tab
 * @Description:
 */
public class TabFragment extends BaseFragment {

    public static TabFragment newInstance() {
        Bundle args = new Bundle();
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public int getContentLayout() {
        return R.layout.fragment_tab;
    }

    @Override
    public void initView() {
    }
}
