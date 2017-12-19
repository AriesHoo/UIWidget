package com.aries.ui.widget.demo.module.radius;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/19 10:53
 * Function: RadiusView示例
 * Desc:
 */
public class RadiusActivity extends BaseActivity {

    @BindView(R.id.rtv_testRadius) RadiusTextView rtvTest;
    @BindView(R.id.rtv_disable) RadiusTextView rtvDisable;
    @BindView(R.id.rtv_javaBg) RadiusTextView rtvJavaBg;
    private GradientDrawable mBgDrawable = new GradientDrawable();

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("RadiusView")
                .setBottomEditTextControl();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_radius;
    }

    @Override
    protected void initView(Bundle var1) {
        mBgDrawable.setCornerRadius(4f);
        mBgDrawable.setStroke(4, Color.MAGENTA, 20f, 10f);
        mBgDrawable.setShape(GradientDrawable.RECTANGLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rtvJavaBg.setBackground(mBgDrawable);
        } else {
            rtvJavaBg.setBackgroundDrawable(mBgDrawable);
        }
        //以上是java代码设置GradientDrawable设置背景属性
        //以下是通过RadiusViewDelegate代理类设置达到相同效果大家可参照下依然是通过设置GradientDrawable属性
        rtvJavaBg.getDelegate()
                .setRadius(4f)
                .setStrokeWidth(4)
                .setStrokeColor(getResources().getColor(android.R.color.holo_purple))
                .setStrokeDashWidth(20f)
                .setStrokeDashGap(10f);
    }


    @OnClick({R.id.rtv_testRadius})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_testRadius:
                boolean enable = rtvDisable.isEnabled();
                rtvDisable.setEnabled(!enable);
                rtvTest.setSelected(!rtvTest.isSelected());
                break;
        }
    }
}
