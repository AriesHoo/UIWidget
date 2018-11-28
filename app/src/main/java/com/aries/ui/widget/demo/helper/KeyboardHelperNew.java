package com.aries.ui.widget.demo.helper;

import android.app.Activity;
import android.app.Application;
import android.database.ContentObserver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.widget.R;

/**
 * @Author: AriesHoo on 2018/7/19 9:31
 * @E-Mail: AriesHoo@126.com
 * Function: 软键盘和虚拟导航栏统一设置
 * Description:
 * 1、2018-2-7 12:27:36 修改是否控制NavigationBar参数及对应java方法
 */
public class KeyboardHelperNew {

    private Activity mActivity;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private View mChildView;
    private boolean mSystemWindows;
    private boolean mControlNavigationBarEnable;
    private int keyboardHeightPrevious;
    private int statusBarHeight;
    private int actionBarHeight;

    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int navigationBarHeight;
    private boolean navigationAtBottom;
    private boolean isSupportActionBar;
    private int mKeyMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED;
    private static final String NAVIGATION_BAR_IS_MIN = "navigationbar_is_min";
    private boolean mFullScreen;
    /**
     * emui3.1监听器
     */
    public ContentObserver mNavigationStatusObserver;
    private OnKeyboardListener mOnKeyboardListener;

    public interface OnKeyboardListener {
        /**
         * On keyboard change.
         *
         * @param isPopup        the is popup  是否弹出
         * @param keyboardHeight the keyboard height  软键盘高度
         */
        void onKeyboardChange(boolean isPopup, int keyboardHeight);
    }

    public static KeyboardHelperNew with(Activity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity不能为null");
        }
        return new KeyboardHelperNew(activity);
    }

    private KeyboardHelperNew(Activity activity) {
        this.mActivity = activity;
        this.mWindow = activity.getWindow();
        this.mDecorView = mWindow.getDecorView();
        FrameLayout frameLayout = mDecorView.findViewById(android.R.id.content);
        this.mChildView = frameLayout.getChildAt(0);
        this.mContentView = mChildView != null ? mChildView : frameLayout;


        BarUtil barConfig = new BarUtil(mActivity);
        this.statusBarHeight = barConfig.getStatusBarHeight();
        this.navigationBarHeight = barConfig.getNavigationBarHeight();
        this.actionBarHeight = barConfig.getActionBarHeight();
        navigationAtBottom = barConfig.isNavigationAtBottom();

        this.paddingLeft = mContentView.getPaddingLeft();
        this.paddingTop = mContentView.getPaddingTop();
        this.paddingRight = mContentView.getPaddingRight();
        this.paddingBottom = mContentView.getPaddingBottom();
        mActivity.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (mNavigationStatusObserver != null) {
                    mActivity.getContentResolver().unregisterContentObserver(mNavigationStatusObserver);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
                } else {
                    mDecorView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
                }
            }
        });
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param enable
     * @return
     */
    public KeyboardHelperNew setControlNavigationBarEnable(boolean enable) {
        mControlNavigationBarEnable = enable;
        return this;
    }

    /**
     * 监听layout变化
     */
    public KeyboardHelperNew setEnable() {
        setEnable(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return this;
    }

    /**
     * 设置监听
     *
     * @param mode
     */
    public KeyboardHelperNew setEnable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 当在一个视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变时,所要调用的回调函数的接口类
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
            if (mNavigationStatusObserver == null) {
                mNavigationStatusObserver = new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        int navigationBarIsMin;
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                            navigationBarIsMin = Settings.System.getInt(mActivity.getContentResolver(),
                                    NAVIGATION_BAR_IS_MIN, 0);
                        } else {
                            navigationBarIsMin = Settings.Global.getInt(mActivity.getContentResolver(),
                                    NAVIGATION_BAR_IS_MIN, 0);
                        }
                        Log.e("ContentObserver", "onChange: " + navigationBarIsMin);
                        if (navigationBarIsMin == 1) {
                            //导航键隐藏了
                        } else {
                            //导航键显示了
                        }
                        Log.i("ContentObserver", "heightNavigation:;getRealNavigationHeight:" + getRealNavigationHeight() + ";hasNavBar:" + NavigationBarUtil.hasNavBar(mActivity) + ";min:" + (Settings.System.getInt(mActivity.getContentResolver(), NAVIGATION_BAR_IS_MIN, 0)) + ";activity:" + mActivity);
                    }

                    @Override
                    public void onChange(boolean selfChange, Uri uri) {
                        super.onChange(selfChange, uri);
                    }
                };
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mActivity.getContentResolver().registerContentObserver(Settings.System.getUriFor
                        (NAVIGATION_BAR_IS_MIN), true, mNavigationStatusObserver);
            } else {
                mActivity.getContentResolver().registerContentObserver(Settings.Global.getUriFor
                        (NAVIGATION_BAR_IS_MIN), true, mNavigationStatusObserver);
            }
        }
        return this;
    }

    /**
     * 取消监听
     */
    public KeyboardHelperNew setDisable() {
        setDisable(mKeyMode);
        return this;
    }

    /**
     * 取消监听
     *
     * @param mode
     */
    public KeyboardHelperNew setDisable(int mode) {
        mWindow.setSoftInputMode(mode);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
        }
        return this;
    }

    /**
     * 设置View变化监听
     */
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            //获取当前窗口可视区域大小的
            mDecorView.getWindowVisibleDisplayFrame(r);
            int navigationBarHeight = NavigationBarUtil.getNavigationBarHeight(mActivity);
            Log.i("ContentObserver", "heightNavigation:navigationBarHeight:" + navigationBarHeight + ";activity:" + mActivity);
            int diff;
            int keyboardHeight;
            boolean isPopup = false;
            if (mSystemWindows) {
                keyboardHeight = mContentView.getHeight() - r.bottom - navigationBarHeight;
                if (mOnKeyboardListener != null) {
                    if (keyboardHeight > navigationBarHeight) {
                        isPopup = true;
                    }
                    mOnKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                }
                return;
            }
            if (mChildView != null) {
                if (isSupportActionBar) {
                    diff = mContentView.getHeight() + statusBarHeight + actionBarHeight - r.bottom;
                } else {
                    diff = mContentView.getHeight() - r.bottom;
                }
                if (mFullScreen) {
                    keyboardHeight = diff - navigationBarHeight;
                } else {
                    keyboardHeight = diff;
                }
                if (mFullScreen && diff == navigationBarHeight) {
                    diff -= navigationBarHeight;
                }
                if (keyboardHeight != keyboardHeightPrevious) {
                    mContentView.setPadding(paddingLeft, paddingTop, paddingRight, diff + paddingBottom);
                    keyboardHeightPrevious = keyboardHeight;
                    if (mOnKeyboardListener != null) {
                        if (keyboardHeight > navigationBarHeight) {
                            isPopup = true;
                        }
                        mOnKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                    }
                }
                return;
            }
//            if (diff >= navigationBarHeight) {
//                mDecorView.setPadding(0, mContentView.getPaddingTop(), 0, diff - navigationBarHeight);
//            }
        }
    };

    public int getNavigationHeight() {
        View viewNavigation = mDecorView.findViewById(R.id.fake_navigation_layout);
        if (viewNavigation != null) {
            return viewNavigation.getMeasuredHeight();
        }
        return 0;
    }

    public int getRealNavigationHeight() {
        View viewNavigation = mDecorView.findViewById(android.R.id.navigationBarBackground);
        if (viewNavigation != null) {
            return viewNavigation.getMeasuredHeight();
        }
        return 0;
    }

    public boolean isSupportActionBar() {
        if (mActivity instanceof AppCompatActivity) {
            return ((AppCompatActivity) mActivity).getSupportActionBar() != null;
        }
        return false;
    }
}
