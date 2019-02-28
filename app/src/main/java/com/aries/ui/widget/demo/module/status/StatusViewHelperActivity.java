package com.aries.ui.widget.demo.module.status;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2019/2/28 11:28
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link StatusViewHelper} 使用示例
 * @Description:
 */
public class StatusViewHelperActivity extends BaseActivity {
    @BindView(R.id.tv_title) TextView mTvTitle;
    @BindView(R.id.tv_content) TextView mTvContent;
    @BindView(R.id.rtv_nextTitle) RadiusTextView mRtvNextTitle;
    @BindView(R.id.tv_nextTitle) TextView mTvNextTitle;

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_status_view_helper;
    }

    @Override
    protected void initView(Bundle var1) {
        StatusViewHelper.with(this)
                .setTopView(mTvTitle)
                .setControlEnable(true)
                .setTransEnable(true)
                .init();
    }

    @OnClick({R.id.tv_title, R.id.rtv_nextTitle, R.id.tv_nextTitle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                onBackPressed();
                break;
            case R.id.rtv_nextTitle:
                break;
            case R.id.tv_nextTitle:
                break;
        }
    }
}
