package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.demo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2018/11/16 11:27
 * @E-Mail: AriesHoo@126.com
 * @Function: 带底部输入框处理方案
 * @Description:
 */
public class TitleFragment extends Fragment {

    @BindView(R.id.titleBar) TitleBarView titleBar;
    @BindView(R.id.rtv_send) RadiusTextView rtvSend;
    private Unbinder unbinder;
    private View mContentView;

    public static TitleFragment newInstance() {
        Bundle args = new Bundle();
        TitleFragment fragment = new TitleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.fragment_title, container, false);
        unbinder = ButterKnife.bind(this, mContentView);
        initView();
        return mContentView;
    }

    private void initView() {
        titleBar.setTitleMainText(getClass().getSimpleName())
                .setTitleMainTextColor(Color.WHITE)
                .setLeftTextDrawableTint(Color.WHITE)
                .setOnLeftTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                })
                .setStatusBarLightMode(false)
                .setCenterGravityLeft(true)
                //软键盘弹起处理--参考
//                .setBottomEditTextControl()
                .setBgResource(android.R.color.holo_purple);

//        //底部有输入框时使用--最后一个参数false
//        titleBar.setImmersible(getActivity(), true, true, false);
//        //设置根布局setFitsSystemWindows(true)
//        mContentView.setFitsSystemWindows(true);
//        //根布局背景色保持和titleBar背景一致
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            mContentView.setBackground(titleBar.getBackground());
//        } else {
//            mContentView.setBackgroundResource(android.R.color.holo_purple);
//        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
