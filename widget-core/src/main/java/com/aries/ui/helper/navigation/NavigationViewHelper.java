package com.aries.ui.helper.navigation;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.widget.R;

import java.lang.ref.WeakReference;

/**
 * @Author: AriesHoo on 2018/11/27 18:14
 * @E-Mail: AriesHoo@126.com
 * Function: 虚拟导航栏控制帮助类
 * Description:
 * 1、修改NavigationLayoutDrawable默认保持与activity的根布局背景一致
 * 2、2018-2-26 15:56:47 新增setBottomView(View bottomView, boolean enable)用于控制底部View设置padding/margin
 * 3、2018-4-18 17:50:35 去掉设置Activity DecorView 背景操作避免滑动返回背景不透明BUG
 * 4、2018-6-2 20:32:35 新增自定义NavigationView 增加DrawableTop属性
 * 5、2018-11-28 14:52:23 修改导航栏控制逻辑新增软键盘开关状态监听
 * 6、2019-4-10 16:26:38 新增Dialog底部导航栏沉浸控制效果{@link #with(Activity, Dialog)}{@link #init()} 并增加{@link #onDestroy()}
 */
public class NavigationViewHelper {

    public final static int TAG_NAVIGATION_BAR_HEIGHT = 0x10000012;
    public final static int TAG_NAVIGATION_PADDING_BOTTOM = 0x10000013;
    private String TAG = getClass().getSimpleName();
    private WeakReference<Activity> mActivity;
    private WeakReference<Dialog> mDialog;
    private boolean mLogEnable;
    private boolean mControlEnable;
    private boolean mTransEnable;
    private boolean mPlusNavigationViewEnable;
    private boolean mControlBottomEditTextEnable = true;
    private Drawable mNavigationViewDrawableTop;
    private Drawable mNavigationViewDrawable;
    private Drawable mNavigationLayoutDrawable;
    /**
     * 设置activity最底部View用于增加导航栏的padding/margin
     */
    private View mBottomView;
    /**
     * 设置activity最底部View用于是否增加导航栏margin
     */
    private boolean mBottomViewMarginEnable;
    private boolean mIsInit;

    private View mDecorView;
    private View mDecorContentView;
    /**
     * activity xml设置根布局
     */
    private View mContentView;
    private LinearLayout mLinearLayout;
    private LinearLayout mLayoutNavigation;
    private TextView mTvNavigation;
    private int mNavigationHeight;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingRight;
    private int mPaddingBottom;
    /**
     * 记录原始颜色
     */
    private int mNavigationBarColor;
    private KeyboardHelper.OnKeyboardVisibilityChangedListener mOnKeyboardVisibilityChangedListener;
    private ViewTreeObserver.OnGlobalLayoutListener mDecorGlobalLayoutListener;

    private NavigationViewHelper(Activity activity) {
        this(activity, null);
    }

    private NavigationViewHelper(Activity activity, Dialog dialog) {
        mActivity = new WeakReference<>(activity);
        mDecorView = activity.getWindow().getDecorView();
        mDecorContentView = mDecorView.findViewById(android.R.id.content);
        mContentView = dialog == null ? ((ViewGroup) mDecorContentView).getChildAt(0) : mDecorContentView;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mNavigationBarColor = activity.getWindow().getNavigationBarColor();
        }
        if (dialog != null) {
            mDialog = new WeakReference<>(dialog);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mNavigationBarColor = dialog.getWindow().getNavigationBarColor();
            }
        }
    }

    public static NavigationViewHelper with(Activity activity) {
        if (activity == null) {
            throw new NullPointerException("null");
        }
        return new NavigationViewHelper(activity);
    }

    public static NavigationViewHelper with(Activity activity, Dialog dialog) {
        if (activity == null) {
            throw new NullPointerException("null");
        }
        return new NavigationViewHelper(activity, dialog);
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
     * 设置是否全透明--通过设置NavigationView 背景色来控制注意调用顺序
     *
     * @param transEnable
     * @return
     */
    public NavigationViewHelper setTransEnable(boolean transEnable) {
        this.mTransEnable = transEnable;
        setNavigationLayoutColor(Color.WHITE);
        if (mContentView != null && mContentView.getBackground() != null) {
            Drawable drawable = mContentView.getBackground().mutate();
            setNavigationLayoutDrawable(drawable);
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
     * 设置软键盘开关状态转换回调--即有软键盘开关变化才会回调
     *
     * @param listener {@link #setControlBottomEditTextEnable(boolean)} 为true方有效
     * @return
     */
    public NavigationViewHelper setOnKeyboardVisibilityChangedListener(KeyboardHelper.OnKeyboardVisibilityChangedListener listener) {
        this.mOnKeyboardVisibilityChangedListener = listener;
        return this;
    }

    /**
     * 设置 NavigationView背景颜色
     *
     * @param navigationViewColor ColorInt
     * @return
     */
    public NavigationViewHelper setNavigationViewColor(int navigationViewColor) {
        return setNavigationViewDrawable(new ColorDrawable(navigationViewColor));
    }

    /**
     * 设置假NavigationView DrawableTop属性
     *
     * @param drawable
     * @return
     */
    public NavigationViewHelper setNavigationViewDrawableTop(Drawable drawable) {
        this.mNavigationViewDrawableTop = drawable;
        return this;
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
     * @param view
     * @param enable 是否设置Margin
     * @return
     */
    public NavigationViewHelper setBottomView(View view, boolean enable) {
        mBottomView = view;
        mBottomViewMarginEnable = enable;
        if (mBottomView == null) {
            return this;
        }
        if (enable) {
            ViewGroup.MarginLayoutParams marginLayout = (ViewGroup.MarginLayoutParams) mBottomView.getLayoutParams();
            if (marginLayout != null) {
                mPaddingLeft = marginLayout.leftMargin;
                mPaddingTop = marginLayout.topMargin;
                mPaddingRight = marginLayout.rightMargin;
                mPaddingBottom = marginLayout.bottomMargin;
            }
        } else {
            mPaddingLeft = mBottomView.getPaddingLeft();
            mPaddingTop = mBottomView.getPaddingTop();
            mPaddingRight = mBottomView.getPaddingRight();
            mPaddingBottom = mBottomView.getPaddingBottom();
        }
        mBottomView.setTag(TAG_NAVIGATION_PADDING_BOTTOM, mPaddingBottom);
        log("left:" + mPaddingLeft + ";top:" + mPaddingTop + ";right:" + mPaddingRight + ";bottom:" + mPaddingBottom);
        return this;
    }

    public NavigationViewHelper setBottomView(View view) {
        return setBottomView(view, false);
    }

    /**
     * 开始设置NavigationView相应效果
     */
    public void init() {
        final Activity activity = mActivity.get();
        if (activity == null || activity.isFinishing()) {
            return;
        }
        final Dialog dialog = mDialog != null ? mDialog.get() : null;
        setControlEnable(mControlEnable);
        final Window window = dialog != null ? dialog.getWindow() : activity.getWindow();
        mNavigationHeight = NavigationBarUtil.getNavigationBarHeight(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && (mPlusNavigationViewEnable || (!mPlusNavigationViewEnable && mTransEnable))) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(
                    window.getDecorView().getSystemUiVisibility()
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (mTransEnable || mPlusNavigationViewEnable) {
                window.setNavigationBarColor(Color.TRANSPARENT);
            }
        }
        setNavigationBackground();
        if (!mIsInit) {
            if (mControlBottomEditTextEnable) {
                KeyboardHelper.with(activity, dialog)
                        .setOnKeyboardVisibilityChangedListener(mOnKeyboardVisibilityChangedListener)
                        .setLogEnable(mLogEnable)
                        .setEnable();
            }
            addOnGlobalLayoutListener();
            mIsInit = true;
        }
        addNavigationBar(window);
        if (mLayoutNavigation != null) {
            ViewGroup.LayoutParams params = mLayoutNavigation.getLayoutParams();
            params.height = mPlusNavigationViewEnable ? mNavigationHeight : 0;
            mLayoutNavigation.setLayoutParams(params);
        }
        log("mBottomView:" + mBottomView + ";mPlusNavigationViewEnable:" + mPlusNavigationViewEnable+";mNavigationBarColor:"+mNavigationBarColor);
        if (mBottomView == null || mPlusNavigationViewEnable) {
            return;
        }
        mBottomView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Object heightKeep = mBottomView.getTag(TAG_NAVIGATION_BAR_HEIGHT);
                int height = heightKeep instanceof Integer ? (int) heightKeep : 0;
                int heightReal = mNavigationHeight > height ? mNavigationHeight : 0 - height;
                ViewGroup.LayoutParams params = mBottomView.getLayoutParams();

                if (mBottomViewMarginEnable) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) params;
                    mPaddingBottom += heightReal;
                    if (marginLayoutParams != null) {
                        marginLayoutParams.bottomMargin = mPaddingBottom;
                    }
                    mBottomView.setLayoutParams(marginLayoutParams);
                } else {
                    //默认
                    if (params != null && params.height >= 0) {
                        params.height += heightReal;
                    }
                    mPaddingBottom += heightReal;
                    mBottomView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
                }
                //将当前导航栏高度保存
                mBottomView.setTag(TAG_NAVIGATION_BAR_HEIGHT, heightReal);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mBottomView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mBottomView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                log("mBottomView:" + mBottomView + ";heightReal:" + heightReal + ";mBottomViewMarginEnable:" + mBottomViewMarginEnable
                        + ";mPaddingLeft:" + mPaddingLeft + ";mPaddingTop:" + mPaddingTop + ";mPaddingRight:" + mPaddingRight + ";mPaddingBottom:" + mPaddingBottom);
            }
        });
    }

    /**
     * Activity ContentView监听用于控制华为导航栏可隐藏问题
     */
    private void addOnGlobalLayoutListener() {
        if (mDecorContentView == null) {
            return;
        }
        //控制华为
        if (mDecorGlobalLayoutListener == null) {
            mDecorGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (mActivity == null) {
                        return;
                    }
                    Activity activity = mActivity.get();
                    if (activity == null) {
                        return;
                    }
                    //导航栏高度变化
                    if (NavigationBarUtil.getNavigationBarHeight(activity) != mNavigationHeight) {
                        log("导航栏变化前高度:" + mNavigationHeight + ";变化后高度:" + NavigationBarUtil.getNavigationBarHeight(activity) + ";paddingBottom:" + mContentView.getPaddingBottom());
                        init();
                    }
                }
            };
            mDecorContentView.getViewTreeObserver().addOnGlobalLayoutListener(mDecorGlobalLayoutListener);
        }
    }

    /**
     * 添加假导航栏
     *
     * @param window
     */
    private void addNavigationBar(Window window) {
        if (!isSupportNavigationBarControl()) {
            return;
        }
        if (mLinearLayout == null) {
            mLinearLayout = FindViewUtil.getTargetView(window.getDecorView(), LinearLayout.class);
        }
        //避免重复添加
        if (mLinearLayout != null
                && mPlusNavigationViewEnable
                && mLayoutNavigation == null) {
            final LinearLayout linearLayout = mLinearLayout;
            Context mContext = window.getContext();
            int count = linearLayout.getChildCount();
            //其实也只有2个子View
            if (count >= 2) {
                View viewChild = null;
                if (count == 2) {
                    viewChild = linearLayout.getChildAt(1);
                } else if (count >= 3) {
                    viewChild = linearLayout.getChildAt(count - 1);
                }
                if (viewChild == null) {
                    return;
                }
                //设置LinearLayout第二个View占用屏幕高度权重为1
                // 预留假的NavigationView位置并保证Navigation始终在最底部--被虚拟导航栏遮住
                viewChild.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));

                //创建假的NavigationView包裹ViewGroup用于设置背景与mContentView一致
                mLayoutNavigation = linearLayout.findViewById(R.id.fake_navigation_layout);
                ;
                if (mLayoutNavigation == null) {
                    mLayoutNavigation = new LinearLayout(mContext);
                    mLayoutNavigation.setId(R.id.fake_navigation_layout);
                    //创建假的NavigationView
                    mTvNavigation = new TextView(mContext);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    mTvNavigation.setId(R.id.fake_navigation_view);
                    mTvNavigation.setCompoundDrawables(null, mNavigationViewDrawableTop, null, null);
                    mLayoutNavigation.addView(mTvNavigation, params);
                    linearLayout.addView(mLayoutNavigation,
                            new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    mNavigationHeight));
                } else {
                    mTvNavigation = mLayoutNavigation.findViewById(R.id.fake_navigation_view);
                }
                setNavigationBackground();
            }
        }
    }

    private void setNavigationBackground() {
        if (mLayoutNavigation != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLayoutNavigation.setBackground(mNavigationLayoutDrawable);
                mTvNavigation.setBackground(mNavigationViewDrawable);
            } else {
                mLayoutNavigation.setBackgroundDrawable(mNavigationLayoutDrawable);
                mTvNavigation.setBackgroundDrawable(mNavigationViewDrawable);
            }
        }
    }

    protected boolean isSupportNavigationBarControl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    private void log(String log) {
        if (mLogEnable) {
            Log.i(TAG, log);
        }
    }

    public void onDestroy() {
        log("onDestroy");
        if (mDecorContentView != null && mDecorGlobalLayoutListener != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mDecorContentView.getViewTreeObserver().removeOnGlobalLayoutListener(mDecorGlobalLayoutListener);
            } else {
                mDecorContentView.getViewTreeObserver().removeGlobalOnLayoutListener(mDecorGlobalLayoutListener);
            }
        }
        //还原View原始paddingBottom或marginBottom
        if (mBottomView != null) {
            Object heightPadding = mBottomView.getTag(TAG_NAVIGATION_PADDING_BOTTOM);
            int padding = heightPadding instanceof Integer ? (Integer) heightPadding : -1;
            if (padding >= 0) {
                if (mBottomViewMarginEnable) {
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mBottomView.getLayoutParams();
                    if (params != null) {
                        params.bottomMargin = padding;
                    }
                }
                mBottomView.setPadding(mBottomView.getPaddingLeft(), mBottomView.getPaddingTop(), mBottomView.getPaddingRight(), padding);
            }
            mBottomView.setTag(TAG_NAVIGATION_BAR_HEIGHT, 0);
        }
        mActivity = null;
        mDialog = null;
        mNavigationViewDrawableTop = null;
        mNavigationViewDrawable = null;
        mNavigationLayoutDrawable = null;
        mBottomView = null;
        mDecorView = null;
        mDecorContentView = null;
        mContentView = null;
        mLinearLayout = null;
        mLayoutNavigation = null;
        mDecorGlobalLayoutListener = null;
        System.gc();
    }
}
