package com.aries.ui.widget.demo.module.loading;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.progress.UIProgressView;

public class LoadingActivity extends BaseActivity {

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
        findViewById(R.id.btn_loadingNormal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIProgressView(LoadingActivity.this)
                        .setMessage("Loading...")
                        .show();
            }
        });

        findViewById(R.id.btn_loadingDimAmount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIProgressView(LoadingActivity.this)
                        .setMessage("Loading...")
                        .setDimAmount(0f)
                        .show();
            }
        });

        findViewById(R.id.btn_loadingBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIProgressView(LoadingActivity.this)
                        .setMessage("Loading...")
                        .setBackgroundColor(Color.parseColor("purple"))
                        .show();
            }
        });

        findViewById(R.id.btn_loadingIndeterminateDrawable).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIProgressView(LoadingActivity.this)
                        .setMessage("Loading...")
                        .setIndeterminateDrawable(R.drawable.progress_loading)
                        .show();
            }
        });
    }
}
