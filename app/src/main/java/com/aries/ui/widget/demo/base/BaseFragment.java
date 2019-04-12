package com.aries.ui.widget.demo.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2019/4/12 11:29
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public abstract class BaseFragment extends Fragment {
    protected Activity mContext;
    protected View mContentView;
    protected boolean mIsFirstShow;
    protected Unbinder mUnBinder;

    public abstract int getContentLayout();

    public abstract void initView();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
        mIsFirstShow = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getContentLayout(), container, false);
        mUnBinder = ButterKnife.bind(this, mContentView);
        initView();
        return mContentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
    }
}
