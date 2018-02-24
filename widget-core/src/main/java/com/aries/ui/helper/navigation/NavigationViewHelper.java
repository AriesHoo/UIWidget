package com.aries.ui.helper.navigation;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.widget.R;

import java.lang.ref.SoftReference;

/**
 * Created: AriesHoo on 2018/1/30/030 9:10
 * E-Mail: AriesHoo@126.com
 * Function: 虚拟导航栏控制帮助类
 * Description:
 * 1、修改NavigationLayoutDrawable默认保持与activity的根布局背景一致
 */
public class NavigationViewHelper {

    public final static int TAG_SET_NAVIGATION_CONTROL = 0x10000011;
    private String TAG = getClass().getSimpleName();
    private SoftReference<Activity> mActivity;
    private boolean mLogEnable;
    private boolean mControlEnable;
    private boolean mTransEnable;
    private boolean mPlusNavigationViewEnable;
    private boolean mControlBottomEditTextEnable = true;
    private int mNavigationViewColor;
    private Drawable mNavigationViewDrawable;
    private Drawable mNavigationLayoutDrawable;
    private View mBottomView;//设置activity最底部View用于增加导航栏的padding

    private View mContentView;//activity xml设置根布局
    private LinearLayout mLinearLayout;
    private LinearLayout mLayoutNavigation;

    private NavigationViewHelper(Activity activity) {
        mActivity = new SoftReference<>(activity);
        mContentView = ((ViewGroup) activity.getWindow().getDecorView()
                .findViewById(android.R.id.content)).getChildAt(0);
    }

    public static NavigationViewHelper with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("null");
        }
        return new NavigationViewHelper(activity);
    }

    /**
     * 是否打印log
     *
     * @param logEnable
     * @return
     */
    public NavigationViewHelper setLogEnable(boolean logEnable) {
        mLogEnable = logEnable;
        return this;
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param controlEnable
     * @return
     */
    public NavigationViewHelper setControlEnable(boolean controlEnable) {
        mControlEnable = controlEnable;
        if (!controlEnable) {
            setPlusNavigationViewEnable(true)
                    .setNavigationViewColor(Color.BLACK)
                    .setNavigationLayoutColor(Color.BLACK);
        }
        return this;
    }

    /**
     * 设置是否全透明
     *
     * @param transEnable
     * @return
     */
    public NavigationViewHelper setTransEnable(boolean transEnable) {
        this.mTransEnable = transEnable;
        if (mContentView != null && mContentView.getBackground() != null) {
            setNavigationLayoutDrawable(mContentView.getBackground());
        } else {
            setNavigationLayoutColor(Color.WHITE);
        }
        return setNavigationViewColor(transEnable ? Color.TRANSPARENT : Color.argb(102, 0, 0, 0));
    }

    /**
     * 是否设置假的导航栏--用于沉浸遮挡
     *
     * @param plusNavigationViewEnable
     * @return
     */
    public NavigationViewHelper setPlusNavigationViewEnable(boolean plusNavigationViewEnable) {
        this.mPlusNavigationViewEnable = plusNavigationViewEnable;
        return this;
    }

    /**
     * 设置是否自动控制底部输入框
     *
     * @param controlBottomEditTextEnable
     * @return
     */
    public NavigationViewHelper setControlBottomEditTextEnable(boolean controlBottomEditTextEnable) {
        mControlBottomEditTextEnable = controlBottomEditTextEnable;
        return this;
    }

    /**
     * 设置 NavigationView背景颜色
     *
     * @param navigationViewColor ColorInt
     * @return
     */
    public NavigationViewHelper setNavigationViewColor(int navigationViewColor) {
        this.mNavigationViewColor = navigationViewColor;
        return setNavigationViewDrawable(new ColorDrawable(navigationViewColor));
    }

    /**
     * 设置假NavigationView背景资源
     *
     * @param drawable
     * @return
     */
    public NavigationViewHelper setNavigationViewDrawable(Drawable drawable) {
        this.mNavigationViewDrawable = drawable;
        return this;
    }

    /**
     * 设置假NavigationView父ViewGroup背景颜色
     *
     * @param navigationLayoutColor ColorInt
     * @return
     */
    public NavigationViewHelper setNavigationLayoutColor(int navigationLayoutColor) {
        return setNavigationLayoutDrawable(new ColorDrawable(navigationLayoutColor));
    }

    /**
     * 设置假NavigationView父ViewGroup背景资源
     *
     * @param navigationLayoutDrawable
     * @return
     */
    public NavigationViewHelper setNavigationLayoutDrawable(Drawable navigationLayoutDrawable) {
        this.mNavigationLayoutDrawable = navigationLayoutDrawable;
        return this;
    }

    /**
     * 设置最底部--虚拟状态栏上边的View
     *
     * @param bottomView
     * @return
     */
    public NavigationViewHelper setBottomView(View bottomView) {
        mBottomView = bottomView;
        return this;
    }

    /**
     * 开始设置NavigationView相应效果
     */
    public void init() {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            throw new NullPointerException("not exist");
        }
        setTransEnable(mTransEnable)
                .setControlEnable(mControlEnable);
        final Window window = activity.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0默认半透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    window.getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(!mPlusNavigationViewEnable ? mNavigationViewColor : Color.TRANSPARENT);
        }
        //控制底部输入框
        if (mControlBottomEditTextEnable) {
            boolean controlEnable = !mPlusNavigationViewEnable && mControlEnable && mBottomView == mContentView;
            setBottomView(controlEnable ? null : mBottomView);
            KeyboardHelper.with(activity)
                    .setControlNavigationBarEnable(controlEnable)
                    .setEnable();
        }
        addNavigationBar(window);
        if (mLayoutNavigation != null) {
            mLayoutNavigation.setVisibility(mPlusNavigationViewEnable ? View.VISIBLE : View.GONE);
        }
        Log.i(TAG, "mBottomView:" + mBottomView);
        if (mBottomView != null) {
            if (!mPlusNavigationViewEnable)
                mBottomView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup.LayoutParams params = mBottomView.getLayoutParams();
                        if (params != null && params.height >= 0) {//默认
                            params.height = params.height + NavigationBarUtil.getNavigationBarHeight(window.getWindowManager());
                        }
                        Object isSet = mBottomView.getTag(TAG_SET_NAVIGATION_CONTROL);
                        if (isSet == null) {
                            mBottomView.setPadding(
                                    mBottomView.getPaddingLeft(),
                                    mBottomView.getPaddingTop(),
                                    mBottomView.getPaddingRight(),
                                    mBottomView.getPaddingBottom() +
                                            NavigationBarUtil.getNavigationBarHeight(window.getWindowManager()));
                            if (mLogEnable)
                                Log.i(TAG, "mBottomView:" + mBottomView + "设置成功");
                        }
                        mBottomView.setTag(TAG_SET_NAVIGATION_CONTROL, true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            mBottomView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        } else {
                            mBottomView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        }
                    }
                });
        }
    }

    private void addNavigationBar(Window window) {
        if (!isSupportNavigationBarControl(window)) {
            return;
        }
        if (mLinearLayout == null) {
            mLinearLayout = FindViewUtil.getTargetView(window.getDecorView(), LinearLayout.class);
        }
        if (mLinearLayout != null && mPlusNavigationViewEnable) {
            final LinearLayout linearLayout = mLinearLayout;
            Context mContext = window.getContext();
            int count = linearLayout.getChildCount();
            if (count >= 2) {//其实也只有2个子View
                View viewChild = null;
                if (count == 2) {
                    viewChild = linearLayout.getChildAt(1);
                } else if (count >= 3) {
                    viewChild = linearLayout.getChildAt(count - 1);
                }
                if (viewChild == null) {
                    return;
                }
                Log.i(TAG, "viewChild:" + viewChild.getId());
                //设置LinearLayout第二个View占用屏幕高度权重为1
                // 预留假的NavigationView位置并保证Navigation始终在最底部--被虚拟导航栏遮住
                viewChild.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

                //创建假的NavigationView包裹ViewGroup用于设置背景与mContentView一致
                mLayoutNavigation = (LinearLayout) linearLayout.findViewById(R.id.fake_navigation_layout);
                View viewNavigation;
                if (mLayoutNavigation == null) {
                    mLayoutNavigation = new LinearLayout(mContext);
                    mLayoutNavigation.setId(R.id.fake_navigation_layout);
                    //创建假的NavigationView
                    viewNavigation = new View(mContext);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            NavigationBarUtil.getNavigationBarHeight(window.getWindowManager()));
                    viewNavigation.setId(R.id.fake_navigation_view);
                    mLayoutNavigation.addView(viewNavigation, params);
                    linearLayout.addView(mLayoutNavigation,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                } else {
                    viewNavigation = mLayoutNavigation.findViewById(R.id.fake_navigation_view);
                }
                if (mLayoutNavigation != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mLayoutNavigation.setBackground(mNavigationLayoutDrawable);
                        viewNavigation.setBackground(mNavigationViewDrawable);
                    } else {
                        mLayoutNavigation.setBackgroundDrawable(mNavigationLayoutDrawable);
                        viewNavigation.setBackgroundDrawable(mNavigationViewDrawable);
                    }
                }
            }
        }
    }

    protected boolean isSupportNavigationBarControl(Window window) {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
                NavigationBarUtil.hasSoftKeys(window.getWindowManager());
    }
}
