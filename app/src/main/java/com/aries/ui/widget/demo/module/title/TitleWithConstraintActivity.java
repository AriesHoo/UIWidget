package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/11/14 8:59
 * E-Mail: AriesHoo@126.com
 * Function: TitleBarView结合ConstraintLayout
 * Description:
 */
public class TitleWithConstraintActivity extends BaseActivity {


    String text = "问题一:不清楚为什么TitleBarView的父容器为ConstraintLayout(约束布局)时,根据测量状态栏高度来重新绘制TitleBarView高度的时候整个TitleBarView高度会是增加两个StatusBarView(状态栏)的高度,现在处理方法为:" +
            "<font color=\"#C00000\">在TitleBarView的onMeasure回调中将动态增加StatusBarView高度的1/2(其它为1倍),onLayout的回调中重新摆放各子View位置时top设置为StatusBarView的1/2(其它为1倍),具体查看源码.</font>" +
            "<br>问题二:ConstraintLayout布局中使用NavigationViewHelper控制底部虚拟导航栏如导航栏可动态隐藏开启会造成TitleBarView被盖住;目前使用ConstraintLayout不进行NavigationViewHelper控制;" +
            "有更好的解决方案或者有清楚原理的万望指点迷津";
    @BindView(R.id.tv_content) TextView tvContent;

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setPlusNavigationViewEnable(true);
    }

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("TitleBarView结合ConstraintLayout使用")
                .setTextColor(Color.WHITE)
                .setLeftTextDrawableTint(Color.WHITE)
                .setRightTextDrawableTint(Color.WHITE)
                .setStatusBarLightMode(false)
                .setTitleMainTextMarquee(true)
                .setRightTextDrawable(R.drawable.ic_menu_white)
                .setLeftTextDrawable(R.drawable.ic_arrow_back_white)
                .setBgResource(android.R.color.holo_blue_dark);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_with_constraint;
    }

    @Override
    protected void initView(Bundle var1) {
        tvContent.setText(Html.fromHtml(text));
//        mContentView.setBackgroundColor(Color.TRANSPARENT);
    }


    @OnClick({R.id.tv_content, R.id.iv_nextTitle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
                break;
            case R.id.iv_nextTitle:
                Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }
}
