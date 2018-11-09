package com.aries.ui.helper.navigation;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import com.aries.ui.util.StatusBarUtil;

/**
 * @Author: AriesHoo on 2018/7/19 9:31
 * @E-Mail: AriesHoo@126.com
 * Function: 软键盘和虚拟导航栏统一设置
 * Description:
 * 1、2018-2-7 12:27:36 修改是否控制NavigationBar参数及对应java方法
 */
public class KeyboardHelper {

    private Activity mActivity;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private boolean mControlNavigationBarEnable;
    private int mKeyMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;

    public static KeyboardHelper with(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity不能为null");
        }
        return new KeyboardHelper(activity);
    }

    private KeyboardHelper(Activity activity) {
        this(activity, ((ViewGroup) activity.getWindow().getDecorView().findViewById(android.R.id.content)).getChildAt(0));
    }

    private KeyboardHelper(Activity activity, View contentView) {
        this(activity, null, contentView);
    }

    private KeyboardHelper(Activity activity, Dialog dialog) {
        this(activity, dialog, dialog.getWindow().findViewById(android.R.id.content));
    }

    private KeyboardHelper(Activity activity, Dialog dialog, View contentView) {
        this.mActivity = activity;
        this.mWindow = dialog != null ? dialog.getWindow() : activity.getWindow();
        this.mDecorView = activity.getWindow().getDecorView();
        this.mContentView = contentView != null ? contentView
                : mWindow.getDecorView().findViewById(android.R.id.content);
    }

    private KeyboardHelper(Activity activity, Window window) {
        this.mActivity = activity;
        this.mWindow = window;
        this.mDecorView = activity.getWindow().getDecorView();
        ViewGroup frameLayout = mWindow.getDecorView().findViewById(android.R.id.content);
        this.mContentView = frameLayout.getChildAt(0) != null ? frameLayout.getChildAt(0) : frameLayout;
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param enable
     * @return
     */
    public KeyboardHelper setControlNavigationBarEnable(boolean enable) {
        mControlNavigationBarEnable = enable;
        return this;
    }

    /**
     * 监听layout变化
     */
    public KeyboardHelper setEnable() {
        setEnable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return this;
    }

    /**
     * 设置监听
     *
     * @param mode
     */
    public KeyboardHelper setEnable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时,所要调用的回调函数的接口类
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        return this;
    }

    /**
     * 取消监听
     */
    public KeyboardHelper setDisable() {
        setDisable(mKeyMode);
        return this;
    }

    /**
     * 取消监听
     *
     * @param mode
     */
    public KeyboardHelper setDisable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        return this;
    }

    boolean showInput = false;
    int statusBarHeight;
    /**
     * 设置View变化监听
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            statusBarHeight = StatusBarUtil.getStatusBarHeight();
            Rect r = new Rect();
            //可视区域
            mContentView.getWindowVisibleDisplayFrame(r);
            int heightDiff = mContentView.getRootView().getHeight() - (r.bottom - r.top);
            Log.i("heightDiff", "heightDiff:" + heightDiff);
            if (heightDiff > 100) {
//                if (!showInput) {
//                    showInput = true;
//                }
                //导航栏
                int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(mWindow.getWindowManager());
                if (mContentView.getPaddingBottom() != heightDiff - statusBarHeight - navigationBarHeight) {
                    if (navigationBarHeight == 0 && r.top == 0) {
                        mContentView.setPadding(0, 0, 0, heightDiff);
                    } else {
                        mContentView.setPadding(0, 0, 0, heightDiff - statusBarHeight - navigationBarHeight);
                    }
                }
            } else {
//                if (!showInput) {
//                    return;
//                }
                showInput = false;
                if (mContentView.getPaddingBottom() != 0) {
                    mContentView.setPadding(0, 0, 0, 0);
                }
            }

        }
    };
}
