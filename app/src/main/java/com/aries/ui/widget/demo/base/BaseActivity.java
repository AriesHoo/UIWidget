package com.aries.ui.widget.demo.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.aries.ui.view.title.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
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
    protected boolean isWhite =true;

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
        this.mContext = this;
        this.beforeSetView();
        this.setContentView(this.getLayout());
        mUnBinder = ButterKnife.bind(this);
        initTitle();
        this.beforeInitView();
        this.initView(savedInstanceState);
    }

    protected void initTitle() {
        titleBar = (TitleBarView) findViewById(R.id.titleBar);
        if (titleBar == null) {
            return;
        }
        type = StatusBarUtil.StatusBarLightMode(mContext);
        if (type <= 0) {//无法设置白底黑字
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0推荐使用
                titleBar.setImmersible(mContext, true, false);// 非透明状态栏模式
            } else {//4.4建议使用
                if(isWhite) {
                    titleBar.setStatusColor(Color.parseColor("#D2888888"));//可使用CF--D3 透明度灰色
                    titleBar.setStatusColor(Color.GRAY); //常规白底模式推荐使用
                }else {
                    titleBar.setStatusColor(Color.TRANSPARENT);
                }
            }
        }else {
            titleBar.setImmersible(mContext,true);
        }
        titleBar.setTitleMainText(mContext.getClass().getSimpleName());
        setTitleBar();
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitleLine(isShowLine());
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

    public View getRootView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
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
