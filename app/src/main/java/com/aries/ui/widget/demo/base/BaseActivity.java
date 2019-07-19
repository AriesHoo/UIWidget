package com.aries.ui.widget.demo.base;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.demo.BuildConfig;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.util.AppUtil;
import com.aries.ui.widget.demo.util.SizeUtil;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: AriesHoo on 2019/4/11 15:34
 * @E-Mail: AriesHoo@126.com
 * @Function: title 基类
 * @Description:
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

    /**
     * 获取contentView xml id
     *
     * @return
     */
    @LayoutRes
    protected abstract int getLayout();

    protected void loadData() {
    }

    protected void beforeSetView() {
    }

    protected void beforeInitView() {
    }

    /**
     * @param var1
     */
    protected abstract void initView(Bundle var1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("getSystemUiVisibility", getWindow().getDecorView().getSystemUiVisibility() + ";window:" + getWindow());
        super.onCreate(savedInstanceState);
        Log.i("savedInstanceState", "savedInstanceState:" + savedInstanceState);
        Log.d(TAG, "lifecycle_onCreate");
        this.mContext = this;
        this.beforeSetView();
        mContentView = View.inflate(mContext, getLayout(), null);
        this.setContentView(mContentView);
        mContentView.setBackgroundResource(R.color.colorBackground);
        mUnBinder = ButterKnife.bind(this);
        initTitle();
        this.beforeInitView();
        Drawable drawableTop = new ColorDrawable(Color.LTGRAY);
        Log.i("BaseActivity", "height:" + SizeUtil.getScreenHeight() + ";width:" + SizeUtil.getScreenWidth());
        DrawableUtil.setDrawableWidthHeight(drawableTop,
                NavigationBarUtil.isNavigationAtBottom(mContext) ? SizeUtil.getScreenWidth() : SizeUtil.dp2px(0.5f),
                NavigationBarUtil.isNavigationAtBottom(mContext) ? SizeUtil.dp2px(0.5f) : SizeUtil.getScreenHeight());
        mNavigationViewHelper = NavigationViewHelper.with(this)
                .setLogEnable(BuildConfig.DEBUG)
                .setControlEnable(true)
                .setTransEnable(true)
                .setPlusNavigationViewEnable(isPlusView(this))
                .setNavigationBarLightMode(isDarkIcon() && isPlusView(this))
                .setControlBottomEditTextEnable(true)
                .setNavigationViewDrawableTop(drawableTop)
                .setNavigationViewColor(Color.argb(isDarkIcon() && isPlusView(this) ? 30 : 80, 0, 0, 0))
                .setNavigationViewColor(Color.argb(isDarkIcon() ? 30 : 80, 0, 0, 0))
                .setNavigationLayoutColor(Color.TRANSPARENT)
                .setBottomView(mContentView);
        beforeControlNavigation(mNavigationViewHelper);
        //不推荐4.4版本使用透明导航栏--话说现在谁还用那么低版本的手机
        if (isControlNavigation()) {
            mNavigationViewHelper.init();
        } else {
            KeyboardHelper.with(this)
                    .setEnable()
                    .setOnKeyboardVisibilityChangedListener(new KeyboardHelper.OnKeyboardVisibilityChangedListener() {
                        @Override
                        public boolean onKeyboardVisibilityChanged(Activity activity, boolean isOpen, int heightDiff, int navigationHeight) {
                            return false;
                        }
                    });
        }
        this.initView(savedInstanceState);
    }

    protected void initTitle() {
        titleBar = findViewById(R.id.titleBar);
        if (titleBar == null) {
            return;
        }
        type = titleBar.getStatusBarModeType();
        //无法设置白底黑字
        if (type <= 0) {
            //5.0 半透明模式alpha-102
            titleBar.setStatusAlpha(60);
        }
        titleBar.setTitleMainText(getTitle());
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
        Log.d(TAG, "lifecycle_onDestroy_isFinishing" + isFinishing());
        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        mContext=null;
        mUnBinder = null;
        mContentView = null;
        titleBar = null;
        TAG=null;
        mNavigationViewHelper = null;
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "lifecycle_onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "lifecycle_onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "lifecycle_onRestart");
        super.onRestart();
    }

    @Override
    protected void onStart() {
        Log.d(TAG, "lifecycle_onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "lifecycle_onResume");
        if (this.mIsFirstShow) {
            this.mIsFirstShow = false;
            this.loadData();
        }
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        KeyboardHelper.handleAutoCloseKeyboard(true, getCurrentFocus(), ev, this);
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 是否控制底部导航栏---目前发现小米8上检查是否有导航栏出现问题
     * TitleWithConstraintActivity ConstraintLayout 布局中布局变化会造成ConstraintLayout控件位置盖住TitleBarView情况;暂未找到解决方案
     *
     * @return
     */
    private boolean isControlNavigation() {
        return true;
    }

    /**
     * 是否全透明-华为4.1以上、小米V6以上及Android O以上版本
     * 可根据导航栏位置颜色设置导航图标颜色
     *
     * @return
     */
    protected boolean isDarkIcon() {
        return (RomUtil.isEMUI() && (RomUtil.getEMUIVersion().compareTo("EmotionUI_4.1") > 0))
                || RomUtil.isMIUI() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /**
     * 是否增加假导航栏占位
     *
     * @param activity
     * @return
     */
    protected boolean isPlusView(Activity activity) {
        return !(activity.getClass().getSimpleName().equals("DisplayLeakActivity"));
    }

    @Override
    public void onContentChanged() {
        Log.d(TAG, "lifecycle_onContentChanged");
        super.onContentChanged();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "lifecycle_onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "lifecycle_onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
