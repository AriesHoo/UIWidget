package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Bundle;

import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/14 15:57
 * Function: titleBar与底部状态栏使用
 * Desc:
 */
public class TitleEditActivity extends BaseActivity {

    @BindView(R.id.titleBar) TitleBarView titleBar;
    @BindView(R.id.rtv_send) RadiusTextView rtvSend;

    @Override
    protected void setTitleBar() {
        StatusBarUtil.setStatusBarDarkMode(this);
        titleBar.setLeftTextDrawable(R.drawable.ic_arrow_back_white);
        titleBar.setBackgroundColor(getResources().getColor(android.R.color.holo_purple));
        titleBar.setTitleMainTextColor(Color.WHITE);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_edit;
    }

    @Override
    protected void initView(Bundle var1) {
        //底部有输入框时使用--最后一个参数false
        titleBar.setImmersible(mContext, true, true, false);
        //设置根布局setFitsSystemWindows(true)
        getRootView().setFitsSystemWindows(true);
        //根布局背景色保持和titleBar背景一致
        getRootView().setBackground(titleBar.getBackground());
        //注:软键盘弹起会透出根布局背景(即:TitleBarView背景色)
    }

    @OnClick(R.id.rtv_send)
    public void onViewClicked() {

    }
}
