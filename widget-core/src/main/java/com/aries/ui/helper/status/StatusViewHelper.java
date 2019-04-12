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

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.widget.R;

import java.lang.ref.WeakReference;

/**
 * @Author: AriesHoo on 2018/7/19 9:43
 * @E-Mail: AriesHoo@126.com
 * Function: 沉浸式状态栏控制帮助类
 * Description:
 */
public class StatusViewHelper {

    public final static int TAG_SET_STATUS_CONTROL = 0x10000012;
    private String TAG = getClass().getSimpleName();
    private WeakReference<Activity> mActivity;
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
     * activity xml设置根布局
     */
    private View mContentView;
    private int mStatusBarColor;
    private LinearLayout mLinearLayout;
    private LinearLayout mLayoutStatus;
    private View mViewStatus;

    private StatusViewHelper(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mStatusBarColor = activity.getWindow().getStatusBarColor();
        }
        mActivity = new WeakReference<>(activity);
        mContentView = ((ViewGroup) activity.getWindow().getDecorView()
                .findViewById(android.R.id.content)).getChildAt(0);
    }

    public static StatusViewHelper with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("null");
        }
        return new StatusViewHelper(activity);
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
            setPlusStatusViewEnable(false);
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
        mTopView = view;
        mTopViewMarginEnable = enable;
        return this;
    }

    public StatusViewHelper setTopView(View view) {
        return setTopView(view, false);
    }

    /**
     * 开始设置StatusView相应效果
     */
    public void init() {
        Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            throw new NullPointerException("not exist");
        }
        setControlEnable(mControlEnable);
        final Window window = activity.getWindow();
        //透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            if (mControlEnable) {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(
                        mControlEnable ? window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN :
                                window.getDecorView().getSystemUiVisibility() ^ View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(!mControlEnable ? Color.BLACK : Color.TRANSPARENT);
                if (!mTransEnable) {
                    window.setStatusBarColor(Color.argb(102, 0, 0, 0));
                }
            }

        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            // 透明状态栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
//                    && (mPlusStatusViewEnable || (!mPlusStatusViewEnable && mTransEnable))) {
//
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
////                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
////                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
//                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                if (mTransEnable || mPlusStatusViewEnable) {
//                    window.setStatusBarColor(Color.TRANSPARENT);
//                }
//            }
//
//        }
        addStatusBar(window);
        if (mLayoutStatus != null) {
            mLayoutStatus.setVisibility(mPlusStatusViewEnable ? View.VISIBLE : View.GONE);
        }
        if (mTopView == null) {
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
                    if (params != null) {
                        params.topMargin += isSet ? 0 : StatusBarUtil.getStatusBarHeight();
                        params.topMargin -= !mPlusStatusViewEnable ? 0 : StatusBarUtil.getStatusBarHeight();
                    }
                } else {
                    //默认
                    if (params != null && params.height >= 0) {
                        params.height += isSet ? 0 : StatusBarUtil.getStatusBarHeight();
                        params.height -= !mPlusStatusViewEnable ? 0 : StatusBarUtil.getStatusBarHeight();
                    }
                    mTopView.setPadding(
                            mTopView.getPaddingLeft(),
                            mTopView.getPaddingTop() + (isSet ? 0 : StatusBarUtil.getStatusBarHeight()),
                            mTopView.getPaddingRight(),
                            mTopView.getPaddingBottom());
                    mTopView.setPadding(
                            mTopView.getPaddingLeft(),
                            mTopView.getPaddingTop() - (!mPlusStatusViewEnable ? 0 : StatusBarUtil.getStatusBarHeight()),
                            mTopView.getPaddingRight(),
                            mTopView.getPaddingBottom());
                    log("mTopView:" + mTopView + "设置成功");
                }
                mTopView.setTag(TAG_SET_STATUS_CONTROL, !mPlusStatusViewEnable);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mTopView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mTopView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    private void addStatusBar(Window window) {
        setStatusView();
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
                        ViewGroup.LayoutParams.MATCH_PARENT, StatusBarUtil.getStatusBarHeight());
                mViewStatus.setId(R.id.fake_status_view);
                mLayoutStatus.addView(mViewStatus, params);
                linearLayout.addView(mLayoutStatus, 0,
                        new ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                mViewStatus = mLayoutStatus.findViewById(R.id.fake_status_view);
            }
            setStatusView();
        }

    }

    private void setStatusView() {
        if (mLayoutStatus != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLayoutStatus.setBackground(mStatusLayoutDrawable);
                mViewStatus.setBackground(mStatusViewDrawable);
            } else {
                mLayoutStatus.setBackgroundDrawable(mStatusLayoutDrawable);
                mViewStatus.setBackgroundDrawable(mStatusViewDrawable);
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mViewStatus.getLayoutParams();
            if (params != null) {
                params.height = isSupportStatusBarControl() && mPlusStatusViewEnable ? StatusBarUtil.getStatusBarHeight() : 0;
            }
        }
    }

    protected boolean isSupportStatusBarControl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private void log(String log) {
        if (mLogEnable) {
            Log.i(TAG, log);
        }
    }
}
