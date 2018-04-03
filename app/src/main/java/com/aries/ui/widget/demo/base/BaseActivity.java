package com.aries.ui.widget.demo.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.demo.BuildConfig;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.util.AppUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created: AriesHoo on 2017/7/3 16:04
 * Function: title 基类
 * Desc:
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected TitleBarView titleBar;
    protected Activity mContext;
    protected boolean mIsFirstShow = true;
    private Unbinder mUnBinder;
    protected int type = 0;
    protected boolean isWhite = true;
    protected View mContentView;
    protected String TAG = getClass().getSimpleName();
    protected NavigationViewHelper mNavigationViewHelper;

    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
    }


    protected abstract void setTitleBar();

    protected boolean isShowLine() {
        return true;
    }

    @LayoutRes
    protected abstract int getLayout();

    protected void loadData() {
    }

    protected void beforeSetView() {
    }

    protected void beforeInitView() {
    }

    protected abstract void initView(Bundle var1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        this.mContext = this;
        this.beforeSetView();
        mContentView = View.inflate(mContext, getLayout(), null);
        this.setContentView(mContentView);
        mContentView.setBackgroundResource(R.color.colorBackground);
        mUnBinder = ButterKnife.bind(this);
        initTitle();
        this.beforeInitView();
        mNavigationViewHelper = NavigationViewHelper.with(this)
                .setLogEnable(BuildConfig.DEBUG)
                .setControlEnable(true)
                .setTransEnable(false)
                //导航栏可关闭/开启的不建议设置该属性为true
                .setPlusNavigationViewEnable(false)
                .setControlBottomEditTextEnable(true)
                .setNavigationViewColor(Color.argb(102, 0, 0, 0))
                .setNavigationLayoutColor(getResources().getColor(R.color.colorBackground))
                .setBottomView(mContentView,true);
        beforeControlNavigation(mNavigationViewHelper);
        mNavigationViewHelper.init();
        this.initView(savedInstanceState);
    }

    protected void initTitle() {
        titleBar = (TitleBarView) findViewById(R.id.titleBar);
        if (titleBar == null) {
            return;
        }
        type = titleBar.getStatusBarModeType();
        if (type <= 0) {//无法设置白底黑字
            titleBar.setStatusAlpha(102);//5.0 半透明模式alpha-102
        }
        titleBar.setTitleMainText(mContext.getClass().getSimpleName());
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitleLine(isShowLine());
        setTitleBar();
    }

    public void setTitleLine(boolean enable) {
        titleBar.setDividerVisible(enable);
    }

    public void startActivity(Activity mContext, Class<? extends Activity> activity, Bundle bundle) {
        AppUtil.startActivity(mContext, activity, bundle);
    }

    public void startActivity(Class<? extends Activity> activity, Bundle bundle) {
        startActivity(mContext, activity, bundle);
    }

    public void startActivity(Class<? extends Activity> activity) {
        startActivity(activity, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
    }

    protected void onResume() {
        if (this.mIsFirstShow) {
            this.mIsFirstShow = false;
            this.loadData();
        }
        super.onResume();
    }

}
