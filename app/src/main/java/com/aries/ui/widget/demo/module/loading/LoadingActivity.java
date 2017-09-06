package com.aries.ui.widget.demo.module.loading;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.progress.UIProgressView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/18 16:38
 * Function: UIProgressView示例
 * Desc:
 */
public class LoadingActivity extends BaseActivity {

    @BindView(R.id.sBtn_msgLoading) SwitchCompat sBtnMsg;
    @BindView(R.id.sBtn_radiusBackLoading) SwitchCompat sBtnRadiusBack;
    @BindView(R.id.sBtn_viewBackLoading) SwitchCompat sBtnViewBack;
    @BindView(R.id.sBtn_progressLoading) SwitchCompat sBtnProgress;
    @BindView(R.id.sBtn_backLoading) SwitchCompat sBtnBack;

    private boolean isShowMsg = true;
    private boolean isDefaultRadiusBack = true;
    private boolean isDefaultViewBack = true;
    private boolean isDefaultProgress = true;
    private boolean isBackDim = true;
    private int style = UIProgressView.STYLE_NORMAL;

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("UIProgressView");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_loading;
    }

    @Override
    protected void initView(Bundle var1) {
        sBtnMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowMsg = isChecked;
                sBtnMsg.setText(isShowMsg ? "显示Message" : "隐藏Message");
            }
        });

        sBtnRadiusBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultRadiusBack = isChecked;
                sBtnRadiusBack.setText(isDefaultRadiusBack ? "背景圆角" : "背景直角");
            }
        });
        sBtnViewBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultViewBack = isChecked;
                sBtnViewBack.setText(isDefaultViewBack ? "默认View背景" : "自定义View背景");
            }
        });
        sBtnProgress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultProgress = isChecked;
                sBtnProgress.setText(isDefaultProgress ? "默认Progress" : "自定义Progress");
            }
        });
        sBtnBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBackDim = isChecked;
                sBtnBack.setText(isBackDim ? "背景半透明" : "背景全透明");
            }
        });

        sBtnMsg.setChecked(true);
        sBtnRadiusBack.setChecked(true);
        sBtnViewBack.setChecked(true);
        sBtnProgress.setChecked(true);
        sBtnBack.setChecked(true);
    }

    @OnClick({R.id.rtv_showLoading, R.id.rtv_showWeiBoLoading, R.id.rtv_showWeiXinLoading, R.id.rtv_showMaterialLoading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_showLoading:
                style = UIProgressView.STYLE_NORMAL;
                break;
            case R.id.rtv_showWeiBoLoading:
                style = UIProgressView.STYLE_WEI_BO;
                break;
            case R.id.rtv_showWeiXinLoading:
                style = UIProgressView.STYLE_WEI_XIN;
                break;
            case R.id.rtv_showMaterialLoading:
                style = UIProgressView.STYLE_MATERIAL_DESIGN;
                break;
        }
        UIProgressView loading = new UIProgressView(mContext, style);
        if (isShowMsg) {
            loading.setMessage(R.string.loading);
        }
        if (!isDefaultViewBack) {
            if (isDefaultRadiusBack) {
                loading.setBgColor(Color.MAGENTA);
            } else
                loading.setBackgroundColor(Color.MAGENTA);
        }
        if (!isDefaultRadiusBack) {
            loading.setBgRadius(0);
        }
        if (!isDefaultProgress) {
            loading.setIndeterminateDrawable(R.drawable.progress_loading);
        }
        if (!isBackDim) {
            loading.setDimAmount(0f);
        }
        loading.setLoadingColor(getResources().getColor(R.color.colorAccent));
        loading.show();
    }
}
