package com.aries.ui.widget.demo.module.loading;

import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.progress.UIProgressDialog;

import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2019/4/11 15:32
 * @E-Mail: AriesHoo@126.com
 * @Function: UIProgressDialog示例
 * @Description:
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

    @Override
    protected void setTitleBar() {
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

    @OnClick({R.id.rtv_showLoading, R.id.rtv_showWeiBoLoading, R.id.rtv_showWeiXinLoading,
            R.id.rtv_showMaterialLoading, R.id.rtv_showFragmentLoading})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rtv_showLoading:
                new UIProgressDialog.NormalBuilder(this)
                        .setMessage(isShowMsg ? R.string.loading : 0)
                        .setIndeterminateDrawable(isDefaultProgress ? 0 : R.drawable.progress_loading)
                        .setBackgroundRadiusResource(isDefaultRadiusBack ? R.dimen.dp_radius_loading : 0)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showWeiBoLoading:
                new UIProgressDialog.WeBoBuilder(this)
                        .setMessage(isShowMsg ? R.string.loading : 0)
                        .setIndeterminateDrawable(isDefaultProgress ? R.drawable.dialog_loading_wei_bo : R.drawable.progress_loading)
                        .setBackgroundRadiusResource(isDefaultRadiusBack ? R.dimen.dp_radius_loading : 0)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showWeiXinLoading:
                new UIProgressDialog.WeChatBuilder(this)
                        .setMessage(isShowMsg ? R.string.loading : 0)
                        .setIndeterminateDrawable(isDefaultProgress ? R.drawable.dialog_loading_wei_xin : R.drawable.progress_loading)
                        .setBackgroundRadiusResource(isDefaultRadiusBack ? R.dimen.dp_radius_loading : 0)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showMaterialLoading:
                new UIProgressDialog.MaterialBuilder(this)
                        .setMessage(isShowMsg ? R.string.loading : 0)
                        .setBackgroundRadiusResource(isDefaultRadiusBack ? R.dimen.dp_radius_loading : 0)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showFragmentLoading:
                new ProgressDialogFragment().show(getSupportFragmentManager(),"ProgressDialogFragment");
                break;
            default:
                break;
        }
    }
}
