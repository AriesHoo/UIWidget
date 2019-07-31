package com.aries.ui.helper.status;

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

import com.aries.ui.impl.ActivityLifecycleCallbacksImpl;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.NotchUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.widget.R;

import java.lang.ref.WeakReference;

/**
 * @Author: AriesHoo on 2018/7/19 9:43
 * @E-Mail: AriesHoo@126.com
 * Function: 沉浸式状态栏控制帮助类
 * Description:
 * 1、2019-4-15 10:39:33 增加状态栏深色图标及文字颜色api {@link #setStatusBarLightMode(boolean)}支持 MIUI V6及以上、Flyme及Android M以上
 * 2、2019-5-10 17:27:26 新增刘海屏适配{@link NotchUtil}
 * 3、2019-7-19 10:33:05 新增{@link #register()}监听生命周期自动进行资源回收
 */
public class StatusViewHelper {

    /**
     * 设置padding
     */
    public final static int TAG_SET_STATUS_CONTROL = 0x10000012;
    /**
     * 设置margin
     */
    public final static int TAG_SET_STATUS_CONTROL_MARGIN = 0x10000013;
    private String TAG = getClass().getSimpleName();
    private WeakReference<Activity> mActivity;
    /**
     * activity xml设置根布局
     */
    private View mContentView;
    private LinearLayout mLinearLayout;
    private LinearLayout mLayoutStatus;
    private View mViewStatus;
    private boolean mLogEnable;
    private boolean mControlEnable;
    private boolean mTransEnable;
    private boolean mPlusStatusViewEnable;
    private Drawable mStatusViewDrawable;
    private Drawable mStatusLayoutDrawable;
    /**
     * 设置activity最底部View用于增加状态栏的padding
     */
    private View mTopView;
    /**
     * 设置activity最顶部View用于是否增加导航栏margin
     */
    private boolean mTopViewMarginEnable;
    /**
     * 设置状态栏白底深色文字图标模式
     */
    private boolean mStatusBarLightMode;
    private boolean mIsInit;

    private StatusViewHelper(Activity activity) {
        mActivity = new WeakReference<>(activity);
        try {
            mContentView = ((ViewGroup) activity.getWindow().getDecorView()
                    .findViewById(android.R.id.content)).getChildAt(0);
        } catch (Exception e) {
            log("mContentView:" + e.getMessage());
        }
    }

    public static StatusViewHelper with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("null");
        }
        return new StatusViewHelper(activity);
    }

    /**
     * 注册Activity生命周期监听
     */
    private void register() {
        Activity activity = mActivity.get();
        if (activity == null) {
            return;
        }
        activity.getApplication().registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksImpl() {
            @Override
            public void onActivityDestroyed(Activity activity) {
                if (activity == null) {
                    return;
                }
                Activity current = mActivity.get();
                log("onActivityDestroyed--" + activity.getClass().getSimpleName()
                        + ";isFinishing:" + activity.isFinishing() + ";current:" + current);
                //只移除当前Activity对象监听
                if (current == null || current != activity) {
                    return;
                }
                //移除监听
                activity.getApplication().unregisterActivityLifecycleCallbacks(this);
                destroy();
            }
        });
    }

    /**
     * 是否打印log
     *
     * @param logEnable
     * @return
     */
    public StatusViewHelper setLogEnable(boolean logEnable) {
        mLogEnable = logEnable;
        return this;
    }

    /**
     * 设置是否控制虚拟导航栏
     *
     * @param controlEnable
     * @return
     */
    public StatusViewHelper setControlEnable(boolean controlEnable) {
        mControlEnable = controlEnable;
        if (!controlEnable) {
            setPlusStatusViewEnable(true)
                    .setStatusLayoutDrawableColor(Color.BLACK)
                    .setStatusViewColor(Color.BLACK);
        }
        return this;
    }

    /**
     * 设置是否全透明
     *
     * @param transEnable
     * @return
     */
    public StatusViewHelper setTransEnable(boolean transEnable) {
        this.mTransEnable = transEnable;
        if (mContentView != null && mContentView.getBackground() != null) {
            setStatusLayoutDrawable(DrawableUtil.getNewDrawable(mContentView.getBackground()));
        } else {
            setStatusLayoutColor(Color.WHITE);
        }
        return setStatusViewColor(transEnable ? Color.TRANSPARENT : Color.argb(102, 0, 0, 0));
    }

    /**
     * 是否设置假的导航栏--用于沉浸遮挡
     *
     * @param plusStatusViewEnable
     * @return
     */
    public StatusViewHelper setPlusStatusViewEnable(boolean plusStatusViewEnable) {
        this.mPlusStatusViewEnable = plusStatusViewEnable;
        if (mPlusStatusViewEnable) {
            setTransEnable(true);
        }
        return this;
    }

    /**
     * 设置状态栏白底深色文字图标模式 支持 MIUI V6及以上、Flyme及Android M以上
     *
     * @param statusBarLightMode
     * @return
     */
    public StatusViewHelper setStatusBarLightMode(boolean statusBarLightMode) {
        this.mStatusBarLightMode = statusBarLightMode;
        return this;
    }

    /**
     * 设置 StatusView背景颜色
     *
     * @param StatusViewColor ColorInt
     * @return
     */
    public StatusViewHelper setStatusViewColor(int StatusViewColor) {
        return setStatusViewDrawable(new ColorDrawable(StatusViewColor));
    }

    /**
     * 设置假StatusView背景资源
     *
     * @param drawable
     * @return
     */
    public StatusViewHelper setStatusViewDrawable(Drawable drawable) {
        this.mStatusViewDrawable = drawable;
        return this;
    }

    /**
     * 设置假StatusView父ViewGroup背景颜色
     *
     * @param StatusLayoutColor ColorInt
     * @return
     */
    public StatusViewHelper setStatusLayoutColor(int StatusLayoutColor) {
        return setStatusLayoutDrawable(new ColorDrawable(StatusLayoutColor));
    }

    /**
     * 设置假StatusView父ViewGroup背景颜色
     *
     * @param colorInt
     * @return
     */
    public StatusViewHelper setStatusLayoutDrawableColor(int colorInt) {
        return setStatusLayoutDrawable(new ColorDrawable(colorInt));
    }

    /**
     * 设置假StatusView父ViewGroup背景资源
     *
     * @param StatusLayoutDrawable
     * @return
     */
    public StatusViewHelper setStatusLayoutDrawable(Drawable StatusLayoutDrawable) {
        this.mStatusLayoutDrawable = StatusLayoutDrawable;
        return this;
    }

    /**
     * 设置最顶部--虚拟状态栏上边的View
     *
     * @param view
     * @param enable 是否设置Margin
     * @return
     */
    public StatusViewHelper setTopView(View view, boolean enable) {
        if (mTopView != null) {
            resetStatusView(mTopView);
        }
        mTopView = view;
        mTopViewMarginEnable = enable;
        return this;
    }

    public StatusViewHelper setTopView(View view) {
        return setTopView(view, false);
    }

    /**
     * 快速设置状态栏白色样式、注意和其它方法调用顺序
     *
     * @return StatusViewHelper 对象
     */
    public StatusViewHelper setWhiteStyle() {
        return setControlEnable(true)
                .setTransEnable(true)
                .setPlusStatusViewEnable(true)
                .setTopView(getTopView(mContentView))
                .setStatusBarLightMode(StatusBarUtil.isSupportStatusBarFontChange())
                .setStatusViewColor(Color.argb(StatusBarUtil.isSupportStatusBarFontChange() ? 0 : 102, 0, 0, 0))
                .setStatusLayoutDrawableColor(Color.WHITE);
    }

    /**
     * 快速设置状态栏黑色样式、注意和其它方法调用顺序
     *
     * @return StatusViewHelper 对象
     */
    public StatusViewHelper setBlackStyle() {
        return setControlEnable(true)
                .setTransEnable(true)
                .setPlusStatusViewEnable(true)
                .setTopView(getTopView(mContentView))
                .setStatusBarLightMode(false)
                .setStatusViewColor(Color.BLACK)
                .setStatusLayoutDrawableColor(Color.BLACK);
    }

    /**
     * 获取顶部View
     *
     * @param target 目标View
     * @return 返回顶部View
     */
    private View getTopView(View target) {
        if (target != null && target instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) target;
            if (group.getChildCount() > 0) {
                target = ((ViewGroup) target).getChildAt(0);
            }
        }
        return target;
    }

    /**
     * 开始设置StatusView相应效果
     */
    public void init() {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            throw new NullPointerException("not exist");
        }
        if (!mIsInit) {
            mIsInit = true;
            register();
        }
        setControlEnable(mControlEnable);
        resetStatusView(mTopView);
        if (mStatusBarLightMode) {
            StatusBarUtil.setStatusBarLightMode(activity);
        } else {
            StatusBarUtil.setStatusBarDarkMode(activity);
        }
        final Window window = activity.getWindow();
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                int now = window.getDecorView().getSystemUiVisibility();
                int systemUi = mControlEnable ?
                        now | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :
                        (now & View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN) == View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ? now ^ View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN : now;
                window.getDecorView().setSystemUiVisibility(systemUi);
                if (mControlEnable) {
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                }
                window.setStatusBarColor(!mControlEnable ? Color.BLACK : Color.TRANSPARENT);
                if (!mTransEnable) {
                    window.setStatusBarColor(Color.argb(mControlEnable ? 102 : 0, 0, 0, 0));
                }
            }

        }
        StatusBarUtil.fitsNotchScreen(window, mControlEnable);
        addStatusBar(window);
        setStatusView();
        setTopView();
    }

    /**
     * 添加假状态栏
     *
     * @param window
     */
    private void addStatusBar(Window window) {
        if (!isSupportStatusBarControl()) {
            return;
        }
        if (mLinearLayout == null) {
            mLinearLayout = FindViewUtil.getTargetView(window.getDecorView(), LinearLayout.class);
        }
        if (mLinearLayout != null && mPlusStatusViewEnable) {
            final LinearLayout linearLayout = mLinearLayout;
            Context mContext = window.getContext();
            //创建假的StatusView包裹ViewGroup用于设置背景与mContentView一致
            mLayoutStatus = linearLayout.findViewById(R.id.fake_status_layout);

            if (mLayoutStatus == null) {
                mLayoutStatus = new LinearLayout(mContext);
                mLayoutStatus.setId(R.id.fake_status_layout);
                //创建假的StatusView
                mViewStatus = new View(mContext);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mViewStatus.setId(R.id.fake_status_view);
                mLayoutStatus.addView(mViewStatus, params);
                linearLayout.addView(mLayoutStatus, 0,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mViewStatus = mLayoutStatus.findViewById(R.id.fake_status_view);
            }
        }

    }

    /**
     * 设置假状态栏样式
     */
    private void setStatusView() {
        if (mLayoutStatus != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLayoutStatus.setBackground(mStatusLayoutDrawable);
                mViewStatus.setBackground(mStatusViewDrawable);
            } else {
                mLayoutStatus.setBackgroundDrawable(mStatusLayoutDrawable);
                mViewStatus.setBackgroundDrawable(mStatusViewDrawable);
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mLayoutStatus.getLayoutParams();
            if (params != null) {
                params.height = isSupportStatusBarControl() && mPlusStatusViewEnable ? getNeedTop(mLayoutStatus) : 0;
            }
        }
    }

    private int getNeedTop(View view) {
        int status = StatusBarUtil.getStatusBarHeight();
        int safe = NotchUtil.getSafeInsetTop(view);
        return isSupportStatusBarControl() ? status >= safe ? status : safe : 0;
    }

    /**
     * 设置顶部状态栏效果-padding或margin
     */
    private void setTopView() {
        if (mTopView == null || mPlusStatusViewEnable || !mControlEnable) {
            return;
        }
        mTopView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mTopView.getLayoutParams();
                //是否处理过topView
                Object control = mTopView.getTag(TAG_SET_STATUS_CONTROL);
                boolean isSet = control != null && control instanceof Boolean ? ((Boolean) control) : false;
                if (mTopViewMarginEnable) {
                    control = mTopView.getTag(TAG_SET_STATUS_CONTROL_MARGIN);
                    isSet = control != null && control instanceof Boolean ? ((Boolean) control) : false;
                    if (params != null) {
                        params.topMargin += isSet ? 0 : getNeedTop(mTopView);
                        mTopView.setLayoutParams(params);
                    }
                    log("mTopView:" + mTopView + "设置margin成功:" + control + ";params:" + params);
                    mTopView.setTag(TAG_SET_STATUS_CONTROL_MARGIN, true);
                } else {
                    //默认
                    if (params != null && params.height >= 0) {
                        params.height += isSet ? 0 : getNeedTop(mTopView);
                    }
                    mTopView.setPadding(
                            mTopView.getPaddingLeft(),
                            mTopView.getPaddingTop() + (isSet ? 0 : getNeedTop(mTopView)),
                            mTopView.getPaddingRight(),
                            mTopView.getPaddingBottom());
                    log("mTopView:" + mTopView + "设置padding成功:" + control + ";params:" + params);
                    mTopView.setTag(TAG_SET_STATUS_CONTROL, true);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mTopView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mTopView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    protected boolean isSupportStatusBarControl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private void log(String log) {
        if (mLogEnable) {
            Log.i(TAG, log);
        }
    }

    /**
     * 重置状态
     */
    private void resetStatusView(View topView) {
        if (topView == null) {
            return;
        }
        Object control = topView.getTag(TAG_SET_STATUS_CONTROL);
        boolean isSet = control != null && control instanceof Boolean ? ((Boolean) control) : false;
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) topView.getLayoutParams();
        //设置了paddingTop
        if (isSet) {
            //默认
            if (params != null && params.height >= 0) {
                params.height -= getNeedTop(topView);
            }
            topView.setPadding(
                    topView.getPaddingLeft(),
                    topView.getPaddingTop() - getNeedTop(topView),
                    topView.getPaddingRight(),
                    topView.getPaddingBottom());
            log("resetStatusView_padding:" + topView + "恢复成功");
            topView.setTag(TAG_SET_STATUS_CONTROL, false);
        }
        control = topView.getTag(TAG_SET_STATUS_CONTROL_MARGIN);
        isSet = control != null && control instanceof Boolean ? ((Boolean) control) : false;
        if (isSet) {
            if (params != null) {
                params.topMargin -= getNeedTop(topView);
            }
            topView.setTag(TAG_SET_STATUS_CONTROL_MARGIN, false);
            log("resetStatusView_margin:" + topView + "恢复成功");
        }
    }

    protected void destroy() {
        log("onDestroy");
        if (mLinearLayout != null && mLayoutStatus != null) {
            mLinearLayout.removeView(mLayoutStatus);
        }
        resetStatusView(mTopView);
        mActivity = null;
        mContentView = null;
        mViewStatus = null;
        mStatusViewDrawable = null;
        mStatusLayoutDrawable = null;
        mLayoutStatus = null;
        mTopView = null;
    }

    /**
     * 销毁
     */
    @Deprecated
    public void onDestroy() {
        log("Deprecated_onDestroy");
    }
}
