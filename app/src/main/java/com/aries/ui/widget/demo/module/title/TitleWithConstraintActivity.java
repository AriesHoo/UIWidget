package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2019/2/28 9:44
 * @E-Mail: AriesHoo@126.com
 * @Function: TitleBarView结合ConstraintLayout
 * @Description:
 */
public class TitleWithConstraintActivity extends BaseActivity {


    String text = "问题一:不清楚为什么TitleBarView的父容器为ConstraintLayout(约束布局)时,根据测量状态栏高度来重新绘制TitleBarView高度时会多回调一次导致实际高度增加,现在处理方法为:" +
            "增加一个全局变量控制当TitleBarView高度变化时进行测量;建议不在ConstraintLayout使用TitleBarView或参看StatusViewHelperActivity;有更好的解决方案或者有清楚原理的万望指点迷津";
    @BindView(R.id.tv_content) TextView tvContent;
    @BindView(R.id.rtv_nextTitle) RadiusTextView mRtvNext;
    @BindView(R.id.tv_nextTitle) TextView mTvNext;

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
                .setBgColor(Color.BLUE);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_with_constraint;
    }

    @Override
    protected void initView(Bundle var1) {
        tvContent.setText(Html.fromHtml(text));
    }


    @OnClick({R.id.tv_content, R.id.rtv_nextTitle, R.id.tv_nextTitle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_content:
                break;
            case R.id.rtv_nextTitle:
                Toast.makeText(mContext, "1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_nextTitle:
                Toast.makeText(mContext, "2", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
