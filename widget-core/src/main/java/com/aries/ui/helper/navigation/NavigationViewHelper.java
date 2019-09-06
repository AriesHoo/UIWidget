package com.aries.ui.helper.navigation;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.impl.ActivityLifecycleCallbacksImpl;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.RomUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.widget.R;

import java.lang.ref.WeakReference;

/**
 * @Author: AriesHoo on 2018/11/27 18:14
 * @E-Mail: AriesHoo@126.com
 * Function: 虚拟导航栏控制帮助类-因导航栏情况过于复杂建议一次Activity/Dialog 只进行一次{@link #init()} 同时建议在竖屏时使用能应用内固定竖屏最好
 * Description:
 * 1、修改NavigationLayoutDrawable默认保持与activity的根布局背景一致
 * 2、2018-2-26 15:56:47 新增setBottomView(View bottomView, boolean enable)用于控制底部View设置padding/margin
 * 3、2018-4-18 17:50:35 去掉设置Activity DecorView 背景操作避免滑动返回背景不透明BUG
 * 4、2018-6-2 20:32:35 新增自定义NavigationView 增加DrawableTop属性
 * 5、2018-11-28 14:52:23 修改导航栏控制逻辑新增软键盘开关状态监听
 * 6、2019-4-10 16:26:38 新增Dialog底部导航栏沉浸控制效果{@link #with(Activity, Dialog)}{@link #init()} 并增加{@link #onDestroy()}
 * 7、2019-7-17 14:54:14 新增{@link #setPlusNavigationViewEnable(boolean, boolean)}和
 * {@link #setPlusNavigationViewEnable(boolean, boolean, boolean)}
 * 用于确定假导航栏位置是否添加在DecorView并增加添加在DecorView情况下是否设置paddingBottom以避免内容被导航栏遮住
 * 8、2019-9-4 17:38:20 修改{@link #setNavigationView(Window)} 未检测子view非空判断造成异常闪退问题
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
    /**
     * 增加假View是否覆盖在DecorView
     */
    private boolean mPlusDecorViewEnable;
    private boolean mPlusDecorViewPaddingEnable;
    private boolean mNavigationBarLightMode;
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
     * @param plusNavigationViewEnable   是否增加假状态栏
     * @param plusDecorViewEnable        是否添加在DecorView上层
     * @param plusDecorViewPaddingEnable plusDecorViewEnable为true时是否预留padding
     * @return
     */
    public NavigationViewHelper setPlusNavigationViewEnable(boolean plusNavigationViewEnable, boolean plusDecorViewEnable, boolean plusDecorViewPaddingEnable) {
        this.mPlusNavigationViewEnable = plusNavigationViewEnable;
        this.mPlusDecorViewEnable = plusDecorViewEnable;
        this.mPlusDecorViewPaddingEnable = plusDecorViewPaddingEnable;
        return this;
    }

    /**
     * 是否设置假的导航栏--用于沉浸遮挡
     *
     * @param plusNavigationViewEnable 是否增加假状态栏
     * @param plusDecorViewEnable      是否添加在DecorView上层
     * @return
     */
    public NavigationViewHelper setPlusNavigationViewEnable(boolean plusNavigationViewEnable, boolean plusDecorViewEnable) {
        return setPlusNavigationViewEnable(plusNavigationViewEnable, plusDecorViewEnable, false);
    }

    /**
     * 是否设置假的导航栏--用于沉浸遮挡
     *
     * @param plusNavigationViewEnable 是否增加假状态栏
     * @return
     */
    public NavigationViewHelper setPlusNavigationViewEnable(boolean plusNavigationViewEnable) {
        return setPlusNavigationViewEnable(plusNavigationViewEnable, false, false);
    }

    /**
     * 设置导航栏图标深色模式-Android O 以上 及MIUI V6以上-Dialog貌似不支持
     *
     * @param navigationBarLightMode
     * @return
     */
    public NavigationViewHelper setNavigationBarLightMode(boolean navigationBarLightMode) {
        this.mNavigationBarLightMode = navigationBarLightMode;
        return this;
    }

    /**
     * 设置是否自动控制底部输入框
     *
     * @param controlBottomEditTextEnable 是否控制
     * @return
     */
    public NavigationViewHelper setControlBottomEditTextEnable(boolean controlBottomEditTextEnable) {
        this.mControlBottomEditTextEnable = controlBottomEditTextEnable;
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
     * 设置最底部--虚拟状态栏上边的View 一般用于底部Button,不推荐使用Activity 根布局Margin
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
     * 快速设置导航栏白色样式、注意和其它方法调用顺序
     *
     * @return NavigationViewHelper 对象
     */
    public NavigationViewHelper setWhiteStyle() {
        return setControlEnable(true)
                .setTransEnable(true)
                .setPlusNavigationViewEnable(true)
                .setBottomView(mContentView)
                .setNavigationBarLightMode(NavigationBarUtil.isSupportNavigationBarFontChange())
                .setNavigationViewColor(Color.argb(NavigationBarUtil.isSupportNavigationBarFontChange() ? 0 : 102, 0, 0, 0))
                .setNavigationLayoutColor(Color.WHITE);
    }

    /**
     * 快速设置导航栏黑色样式、注意和其它方法调用顺序
     *
     * @return NavigationViewHelper 对象
     */
    public NavigationViewHelper setBlackStyle() {
        return setControlEnable(true)
                .setTransEnable(true)
                .setPlusNavigationViewEnable(true)
                .setBottomView(mContentView)
                .setNavigationBarLightMode(false)
                .setNavigationViewColor(Color.BLACK)
                .setNavigationLayoutColor(Color.BLACK);
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
        if (mNavigationBarLightMode) {
            NavigationBarUtil.setNavigationBarLightMode(window);
        } else {
            NavigationBarUtil.setNavigationBarDarkMode(window);
        }
        mNavigationHeight = NavigationBarUtil.getNavigationBarHeight(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && (mPlusNavigationViewEnable || (!mPlusNavigationViewEnable && mTransEnable))) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                window.getDecorView().setSystemUiVisibility(
                        window.getDecorView().getSystemUiVisibility()
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (mTransEnable || mPlusNavigationViewEnable) {
                    window.setNavigationBarColor(Color.TRANSPARENT);
                }
            }
        }
        if (!mIsInit) {
            if (mControlBottomEditTextEnable) {
                KeyboardHelper.with(activity, dialog)
                        .setOnKeyboardVisibilityChangedListener(mOnKeyboardVisibilityChangedListener)
                        .setLogEnable(mLogEnable)
                        .setEnable();
            }
            addOnGlobalLayoutListener();
            register();
            StatusBarUtil.fitsNotchScreen(window, true);
            mIsInit = true;
        }
        addNavigationBar(window);
        setNavigationView(window);
        log("mBottomView:" + mBottomView + ";mPlusNavigationViewEnable:" + mPlusNavigationViewEnable + ";mNavigationBarColor:" + mNavigationBarColor);
        if (mBottomView == null) {
            return;
        }
        if (mPlusNavigationViewEnable) {
            if (!mPlusDecorViewEnable) {
                return;
            }
            if (mPlusDecorViewEnable && !mPlusDecorViewPaddingEnable) {
                return;
            }
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
        if (mDecorView == null) {
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
            mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(mDecorGlobalLayoutListener);
        }
    }

    /**
     * 添加假导航栏占位
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
        if (mLinearLayout != null && mPlusNavigationViewEnable) {
            //创建假的NavigationView包裹ViewGroup用于设置背景与mContentView一致
            mLayoutNavigation = FindViewUtil.getTargetView(window.getDecorView(), R.id.fake_navigation_layout);
            Context mContext = window.getContext();
            if (mLayoutNavigation == null) {
                mLayoutNavigation = new LinearLayout(mContext);
                mLayoutNavigation.setId(R.id.fake_navigation_layout);
                //创建假的NavigationView
                mTvNavigation = new TextView(mContext);
                ViewGroup.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mTvNavigation.setId(R.id.fake_navigation_view);
                mLayoutNavigation.addView(mTvNavigation, params);
            }
            ViewParent parent = mLayoutNavigation.getParent();
            log("ViewParent1:" + parent);
            if (parent != null) {
                ((ViewGroup) parent).removeView(mLayoutNavigation);
            }
            //添加到window.getDecorView()
            if (mPlusDecorViewEnable) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        NavigationBarUtil.getNavigationBarHeight(window));
                params.gravity = Gravity.BOTTOM;
                ((FrameLayout) window.getDecorView()).addView(mLayoutNavigation, params);
                return;
            }
            int count = mLinearLayout.getChildCount();
            //其实也只有2个子View
            if (count >= 2) {
                View viewChild = null;
                if (count == 2) {
                    viewChild = mLinearLayout.getChildAt(1);
                } else if (count >= 3) {
                    viewChild = mLinearLayout.getChildAt(count - 1);
                }
                if (viewChild == null) {
                    return;
                }
                //设置LinearLayout第二个View占用屏幕高度权重为1
                // 预留假的NavigationView位置并保证Navigation始终在最底部--被虚拟导航栏遮住
                viewChild.setLayoutParams(new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
                mLinearLayout.addView(mLayoutNavigation);
            }
        }
    }

    /**
     * 设置假导航栏样式效果
     *
     * @param window
     */
    private void setNavigationView(Window window) {
        if (mLayoutNavigation != null) {
            mTvNavigation = mLayoutNavigation.findViewById(R.id.fake_navigation_view);
            //根据屏幕旋转角度重新设置宽高
            int angle = ((WindowManager) window.getDecorView().getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            int navigationHeight = NavigationBarUtil.getNavigationBarHeight(window);
            //当旋转270°时导航栏和状态栏在同一边如果是刘海屏--华为系统才会
            navigationHeight += angle == Surface.ROTATION_270 && navigationHeight > 0 && RomUtil.isEMUI() ? StatusBarUtil.getStatusBarHeight() : 0;
            boolean isNavigationAtBottom = NavigationBarUtil.isNavigationAtBottom(window);
            mTvNavigation.setCompoundDrawables(isNavigationAtBottom ? null : mNavigationViewDrawableTop, isNavigationAtBottom ? mNavigationViewDrawableTop : null, null, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mLayoutNavigation.setBackground(mNavigationLayoutDrawable);
                mTvNavigation.setBackground(mNavigationViewDrawable);
            } else {
                mLayoutNavigation.setBackgroundDrawable(mNavigationLayoutDrawable);
                mTvNavigation.setBackgroundDrawable(mNavigationViewDrawable);
            }
            //添加DecorView
            if (mPlusDecorViewEnable) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mLayoutNavigation.getLayoutParams();
                params.width = isNavigationAtBottom ? ViewGroup.LayoutParams.MATCH_PARENT : navigationHeight;
                params.height = isNavigationAtBottom ? navigationHeight : ViewGroup.LayoutParams.MATCH_PARENT;
                params.gravity = angle == 0 ? Gravity.BOTTOM : angle == 3 ? Gravity.START : angle == 1 ? Gravity.END : Gravity.TOP;
                if (!RomUtil.isEMUI() && !isNavigationAtBottom && angle == Surface.ROTATION_270) {
                    params.gravity = Gravity.START;
                }
                mLayoutNavigation.setLayoutParams(params);
                log("angle:" + angle + ";isNavigationAtBottom:" + isNavigationAtBottom);
                return;
            }
            View child = mLinearLayout.getChildAt(mLinearLayout.indexOfChild(mLayoutNavigation) - 1);
            //设置排列方式
            mLinearLayout.setOrientation(isNavigationAtBottom ? LinearLayout.VERTICAL : LinearLayout.HORIZONTAL);
            mLayoutNavigation.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            isNavigationAtBottom ? ViewGroup.LayoutParams.MATCH_PARENT : navigationHeight,
                            isNavigationAtBottom ? navigationHeight : ViewGroup.LayoutParams.MATCH_PARENT));
            child.setLayoutParams(new LinearLayout.LayoutParams(
                    isNavigationAtBottom ? ViewGroup.LayoutParams.MATCH_PARENT : 0,
                    isNavigationAtBottom ? 0 : ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
            mTvNavigation.setCompoundDrawables(isNavigationAtBottom ? null : mNavigationViewDrawableTop, isNavigationAtBottom ? mNavigationViewDrawableTop : null, null, null);

            //获取假状态栏位置--此处是有BUG的
            int index = mLinearLayout.indexOfChild(mLayoutNavigation);
            if (!RomUtil.isEMUI() && !isNavigationAtBottom && angle == Surface.ROTATION_270 && index != 0) {
                ViewGroup.LayoutParams params = mLayoutNavigation.getLayoutParams();
                mLinearLayout.removeView(mLayoutNavigation);
                mLinearLayout.addView(mLayoutNavigation, 0, params);
                mTvNavigation.setCompoundDrawables(null, null, mNavigationViewDrawableTop, null);
            } else {
                if (index == 0) {
                    ViewGroup.LayoutParams params = mLayoutNavigation.getLayoutParams();
                    mLinearLayout.removeView(mLayoutNavigation);
                    mLinearLayout.addView(mLayoutNavigation, params);
                }
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

    protected void destroy() {
        log("onDestroy");
        if (mDecorView != null && mDecorGlobalLayoutListener != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(mDecorGlobalLayoutListener);
            } else {
                mDecorView.getViewTreeObserver().removeGlobalOnLayoutListener(mDecorGlobalLayoutListener);
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
        mOnKeyboardVisibilityChangedListener = null;
    }

    /**
     * 销毁
     */
    @Deprecated
    public void onDestroy() {
        log("Deprecated_onDestroy");
    }
}
