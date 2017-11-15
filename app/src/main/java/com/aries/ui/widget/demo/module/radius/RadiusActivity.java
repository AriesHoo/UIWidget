package com.aries.ui.widget.demo.module.radius;

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
