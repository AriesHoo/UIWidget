package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017/11/14 8:59
 * E-Mail: AriesHoo@126.com
 * Function: TitleBarView结合ConstraintLayout
 * Description:
 */
public class TitleWithConstraint extends BaseActivity {


    String text = "不清楚为什么TitleBarView的父容器为ConstraintLayout(约束布局)时,根据测量状态栏高度来重新绘制TitleBarView高度的时候整个TitleBarView高度会是增加两个StatusBarView(状态栏)的高度,现在处理方法为:<font color=\"#C00000\">在TitleBarView的onMeasure回调中将动态增加StatusBarView高度的1/2(其它为1倍),onLayout的回调中重新摆放各子View位置时top设置为StatusBarView的1/2(其它为1倍),具体查看源码.有更好的解决方案或者有清楚原理的万望指点迷津</font>";
    @BindView(R.id.tv_content) TextView tvContent;

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("TitleBarView结合ConstraintLayout使用")
                .setTextColor(Color.WHITE)
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
    }

}
