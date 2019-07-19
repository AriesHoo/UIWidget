package com.aries.ui.widget.demo.module.status;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.widget.demo.BuildConfig;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2019/2/28 11:28
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link StatusViewHelper} 使用示例
 * @Description:
 */
public class StatusViewHelperActivity extends BaseActivity {
    @BindView(R.id.tv_titleStatus) TextView mTvTitle;
    @BindView(R.id.sBtn_immersibleStatus) SwitchCompat mSBtnImmersible;
    @BindView(R.id.sBtn_transStatus) SwitchCompat mSBtnTrans;
    @BindView(R.id.sBtn_marginStatus) SwitchCompat mSBtnMargin;
    @BindView(R.id.sBar_alphaStatus) SeekBar mSBarAlpha;
    @BindView(R.id.tv_alphaStatus) TextView mTvAlpha;
    @BindView(R.id.sBtn_plusStatus) SwitchCompat mSBtnPlus;
    @BindView(R.id.lLayout_alphaStatus) LinearLayout mLLayoutAlpha;

    private StatusViewHelper mStatusViewHelper;

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_status_view_helper;
    }

    @Override
    protected void initView(Bundle var1) {

        mTvTitle.setText(getTitle());
        mSBtnImmersible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSBtnImmersible.setText(isChecked ? "沉浸" : "不沉浸");
                mSBtnTrans.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mSBtnMargin.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mSBtnPlus.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                mLLayoutAlpha.setVisibility(mSBtnPlus.isChecked() && isChecked ? View.VISIBLE : View.GONE);
                setStatus();
            }
        });
        mSBtnTrans.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSBtnTrans.setText(isChecked ? "全透明" : "半透明");
                setStatus();
            }
        });
        mSBtnMargin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSBtnMargin.setText(isChecked ? "TopView 设置MarginTop" : "TopView 设置PaddingTop");
                setStatus();
            }
        });
        mSBtnPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSBtnPlus.setText(isChecked ? "增加状态栏占位View" : "不增加状态栏占位View");
                mLLayoutAlpha.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                if (isChecked) {
                    mSBtnTrans.setChecked(true);
                }
                mSBtnTrans.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                mSBtnMargin.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                setStatus();
            }
        });
        mSBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTvAlpha.setText("" + progress);
                setStatus();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mSBtnImmersible.setChecked(true);
        mSBtnTrans.setChecked(true);
    }

    private void setStatus() {
        if (mStatusViewHelper == null) {
            mStatusViewHelper = StatusViewHelper.with(this);
        }
        mStatusViewHelper.setTopView(mTvTitle, mSBtnMargin.isChecked())
                .setControlEnable(mSBtnImmersible.isChecked())
                .setTransEnable(mSBtnTrans.isChecked())
                .setLogEnable(BuildConfig.DEBUG)
                .setPlusStatusViewEnable(mLLayoutAlpha.getVisibility() == View.VISIBLE ? mSBtnPlus.isChecked() : false)
                .setStatusLayoutDrawable(mTvTitle.getBackground())
                .setStatusViewColor(Color.argb(mSBarAlpha.getProgress(), 0, 0, 0))
                .init();
    }

    @OnClick({R.id.tv_titleStatus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_titleStatus:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mStatusViewHelper = null;
        super.onDestroy();
    }
}
