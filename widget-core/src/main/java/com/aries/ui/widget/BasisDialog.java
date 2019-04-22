package com.aries.ui.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.util.ResourceUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.DragLayout;
import com.aries.ui.widget.i.NavigationBarControl;

/**
 * @Author: AriesHoo on 2018/7/19 8:46
 * @E-Mail: AriesHoo@126.com
 * Function: Widget Dialog模式基类
 * Description:
 * 1、新增基础Builder包装通用属性
 * 2、新增ContentView margin属性
 * 3、新增控制虚拟导航栏功能
 * 4、2018-4-3 12:51:47 移除控制虚拟导航栏功能
 * 5、2018-7-19 09:02:00 修改点击contentView 父容器逻辑处理
 * 6、2019-4-11 12:40:03 新增底部虚拟导航栏控制{@link #setNavigationBarControl(NavigationBarControl)}
 * 修改{@link #afterSetContentView(View)}逻辑
 * 7、2019-4-18 11:39:46 修改{@link #backDialog(boolean)}逻辑使其可即时生效
 * 8、2019-4-22 16:50:33 新增{@link #setDragEnable(boolean)}手指跟随滑动关闭功能
 */
public class BasisDialog<T extends BasisDialog> extends Dialog {

    protected Window mWindow;
    protected View mContentView;
    private WindowManager.LayoutParams mLayoutParams;
    private float mAlpha = 1.0f;
    private float mDimAmount = 0.6f;
    private int mGravity = Gravity.CENTER;
    private int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;
    private int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;
    private int mWindowAnimations = -1;
    protected boolean mCanceledOnTouchOutside;
    /**
     * 是否允许手指拖动关闭
     */
    protected boolean mDragEnable = false;
    protected NavigationBarControl mNavigationBarControl;
    private NavigationViewHelper mNavigationViewHelper;
    /**
     * 虚拟导航栏底部View
     */
    protected View mNavigationBottomView;

    public interface OnTextViewLineListener {
        /**
         * TextView及其控件行数变化监听
         *
         * @param textView
         * @param lineCount
         */
        void onTextViewLineListener(TextView textView, int lineCount);
    }

    public BasisDialog(Context context) {
        this(context, 0);
    }

    public BasisDialog(Context context, int themeResId) {
        super(context, themeResId);
        if (context instanceof Activity) {
            setOwnerActivity((Activity) context);
        }
    }

    protected int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWindow = getWindow();
        if (mLayoutParams == null) {
            mLayoutParams = mWindow.getAttributes();
        }
        mLayoutParams.width = mWidth;
        mLayoutParams.height = mHeight;
        mLayoutParams.gravity = mGravity;
        // 透明度
        mLayoutParams.alpha = mAlpha;
        // 黑暗度
        mLayoutParams.dimAmount = mDimAmount;
        if (mWindowAnimations != -1) {
            // 动画
            mLayoutParams.windowAnimations = mWindowAnimations;
        }
        mWindow.setAttributes(mLayoutParams);
        if (mDragEnable) {
            setDragEnable(true);
        }
        mNavigationBottomView = mContentView;
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(View.inflate(getContext(), layoutResID, null));
    }

    @Override
    public void setContentView(View view) {
        ViewGroup.LayoutParams params = view != null ? view.getLayoutParams() : null;
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        setContentView(view, params);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        this.mContentView = view;
    }

    @Override
    public void dismiss() {
        closeKeyboard();
        super.dismiss();
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        this.mCanceledOnTouchOutside = cancel;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mContentView != null) {
            afterSetContentView(mContentView);
        }
        StatusBarUtil.fitsNotchScreen(getWindow(), true);
        Activity activity = getOwnerActivity();
        if (activity == null || mNavigationBarControl == null) {
            return;
        }
        mNavigationViewHelper = NavigationViewHelper.with(activity, this)
                .setLogEnable(BuildConfig.DEBUG)
                .setControlEnable(true)
                .setTransEnable(true)
                .setBottomView(mNavigationBottomView);
        boolean isInit = mNavigationBarControl.setNavigationBar(this, mNavigationViewHelper, mNavigationBottomView);
        if (isInit) {
            mNavigationViewHelper.init();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mNavigationViewHelper != null) {
            mNavigationViewHelper.onDestroy();
        }
        mNavigationViewHelper = null;
    }

    protected void afterSetContentView(View parent) {
        //当设置点击其它地方dialog可消失需对root的父容器处理
        if (mCanceledOnTouchOutside) {
            //设置点击事件防止事件透传至父容器
            parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            ((ViewGroup) parent.getParent()).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    /**
     * 获取根布局
     *
     * @return
     */
    public View getContentView() {
        return mContentView;
    }

    protected T backDialog() {
        return backDialog(false);
    }

    protected T backDialog(boolean attributes) {
        if (attributes && mWindow != null && mLayoutParams != null) {
            mLayoutParams.width = mWidth;
            mLayoutParams.height = mHeight;
            mLayoutParams.gravity = mGravity;
            // 透明度
            mLayoutParams.alpha = mAlpha;
            // 黑暗度
            mLayoutParams.dimAmount = mDimAmount;
            if (mWindowAnimations != -1) {
                // 动画
                mLayoutParams.windowAnimations = mWindowAnimations;
            }
            mWindow.setAttributes(mLayoutParams);
        }
        return (T) this;
    }

    public T setAttributes(WindowManager.LayoutParams params) {
        this.mLayoutParams = params;
        return backDialog(true);
    }

    public T setAlpha(float alpha) {
        mAlpha = alpha;
        return backDialog(true);
    }

    public T setDimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return backDialog(true);
    }

    public T setGravity(int gravity) {
        mGravity = gravity;
        return backDialog(true);
    }

    public T setWidth(int w) {
        this.mWidth = w;
        return backDialog(true);
    }

    public T setHeight(int h) {
        this.mHeight = h;
        return backDialog(true);
    }

    /**
     * 设置动画资源
     *
     * @param res
     * @return
     */
    public T setWindowAnimations(int res) {
        this.mWindowAnimations = res;
        return backDialog(true);
    }

    /**
     * 是否开启手指拖拽
     *
     * @param enable
     * @return
     */
    public T setDragEnable(boolean enable) {
        this.mDragEnable = enable;
        if (mContentView != null) {
            DragLayout dragLayout = FindViewUtil.getTargetView(mContentView, DragLayout.class);
            if (dragLayout != null) {
                dragLayout.setDragEnable(mDragEnable);
            } else {
                ViewGroup group = (ViewGroup) mContentView.getParent();
                group.removeView(mContentView);
                dragLayout = new DragLayout(getContext());
                dragLayout.addView(mContentView);
                dragLayout.setDragEnable(mDragEnable);
                dragLayout.setOnDragListener(new DragLayout.OnDragListener() {
                    @Override
                    public void onClosed() {
                        dismiss();
                    }

                    @Override
                    public void onOpened() {

                    }
                });
                mContentView = dragLayout;
                group.addView(dragLayout);
            }
        }
        return backDialog();
    }

    /**
     * 设置ContentView margin属性
     *
     * @return
     */
    public T setMargin(int left, int top, int right, int bottom) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mContentView.getLayoutParams();
        if (params != null) {
            params.setMargins(left, top, right, bottom);
            mContentView.setLayoutParams(params);
        }
        return backDialog();
    }

    /**
     * 设置导航栏控制
     *
     * @param control
     * @return
     */
    public T setNavigationBarControl(NavigationBarControl control) {
        this.mNavigationBarControl = control;
        return backDialog();
    }

    protected void closeKeyboard() {
        KeyboardHelper.closeKeyboard(this);
    }

    /**
     * @param <T>
     */
    protected static class BasisBuilder<T extends BasisBuilder> {
        protected int mStatePressed = android.R.attr.state_pressed;
        protected Context mContext;
        protected ResourceUtil mResourceUtil;
        protected BasisDialog mDialog;
        protected LinearLayout mLLayoutRoot;

        protected Drawable mBackground;
        protected float mBackgroundRadius;
        protected float mElevation;
        protected int mPadding;
        protected int mMinWidth;
        protected int mMinHeight;

        protected float mLineSpacingMultiplier = 1.0f;
        protected float mLineSpacingExtra = 0.0f;
        protected int mTextSizeUnit = TypedValue.COMPLEX_UNIT_DIP;

        protected boolean mCancelable = true;
        protected boolean mCanceledOnTouchOutside = true;
        protected boolean mDragEnable = false;
        protected OnTextViewLineListener mOnTextViewLineListener;
        protected OnDismissListener mOnDismissListener;
        protected OnKeyListener mOnKeyListener;
        protected OnCancelListener mOnCancelListener;
        protected OnShowListener mOnShowListener;
        protected NavigationBarControl mNavigationBarControl;


        public BasisBuilder(Context context) {
            mContext = context;
            mResourceUtil = new ResourceUtil(mContext);
        }

        protected T backBuilder() {
            return (T) this;
        }

        /**
         * 设置根布局背景Drawable
         *
         * @param drawable
         * @return
         */
        public T setBackground(Drawable drawable) {
            this.mBackground = drawable;
            return backBuilder();
        }

        /**
         * 设置根布局背景颜色值
         *
         * @param color
         * @return
         */
        public T setBackgroundColor(int color) {
            return setBackground(new ColorDrawable(color));
        }

        /**
         * 设置根布局背景资源
         *
         * @param resId
         * @return
         */
        public T setBackgroundResource(int resId) {
            return setBackground(mResourceUtil.getDrawable(resId));
        }

        /**
         * 设置背景圆角--背景为颜色值时有效
         *
         * @param radius
         * @return
         */
        public T setBackgroundRadius(float radius) {
            mBackgroundRadius = radius;
            return backBuilder();
        }

        public T setBackgroundRadiusResource(int res) {
            return setBackgroundRadius(mResourceUtil.getDimension(res));
        }

        /**
         * 设置根布局海拔
         *
         * @param elevation
         * @return
         */
        public T setElevation(float elevation) {
            this.mElevation = elevation;
            return backBuilder();
        }

        public T setElevationResoure(int res) {
            return setElevation(mResourceUtil.getDimension(res));
        }

        /**
         * 设置根布局padding值
         *
         * @param padding
         * @return
         */
        public T setPadding(int padding) {
            this.mPadding = padding;
            return backBuilder();
        }

        /**
         * 设置根布局最小宽度
         *
         * @param w
         * @return
         */
        public T setMinWidth(int w) {
            this.mMinWidth = w;
            return backBuilder();
        }

        public T setMinWidthResource(int res) {
            return setMinWidth(mResourceUtil.getDimensionPixelSize(res));
        }

        /**
         * 设置根布局最小高度
         *
         * @param h
         * @return
         */
        public T setMinHeight(int h) {
            this.mMinHeight = h;
            return backBuilder();
        }

        public T setMinHeightResource(int res) {
            return setMinHeight(mResourceUtil.getDimensionPixelSize(res));
        }

        /**
         * 设置所有文本行间距属性
         * {@link TextView#setLineSpacing(float, float)}
         *
         * @param lineSpacingMultiplier
         * @return
         */
        public T setLineSpacingMultiplier(float lineSpacingMultiplier) {
            mLineSpacingMultiplier = lineSpacingMultiplier;
            return backBuilder();
        }

        /**
         * {@link TextView#setLineSpacing(float, float)}
         *
         * @param lineSpacingExtra
         * @return
         */
        public T setLineSpacingExtra(float lineSpacingExtra) {
            mLineSpacingExtra = lineSpacingExtra;
            return backBuilder();
        }


        /**
         * 设置TextView的尺寸单位
         * {@link TextView#setTextSize(int, float)}
         *
         * @param unit
         * @return
         */
        public T setTextSizeUnit(int unit) {
            this.mTextSizeUnit = unit;
            return backBuilder();
        }

        /**
         * 设置dialog 是否可点击返回键关闭
         * {@link #setCancelable(boolean)}
         *
         * @param enable
         * @return
         */
        public T setCancelable(boolean enable) {
            this.mCancelable = enable;
            return backBuilder();
        }

        /**
         * 点击非contentView 是否关闭dialog
         * {@link #setCanceledOnTouchOutside(boolean)}
         *
         * @param enable
         * @return
         */
        public T setCanceledOnTouchOutside(boolean enable) {
            this.mCanceledOnTouchOutside = enable;
            return backBuilder();
        }

        /**
         * 是否开启手指拖拽--通过添加
         *
         * @param enable
         * @return
         */

        public T setDragEnable(boolean enable) {
            this.mDragEnable = enable;
            return backBuilder();
        }

        /**
         * 设置TextView 文本行数监听
         * {@link TextView#post(Runnable)}
         *
         * @param listener
         * @return
         */
        public T setOnTextViewLineListener(OnTextViewLineListener listener) {
            this.mOnTextViewLineListener = listener;
            return backBuilder();
        }

        /**
         * 设置dialog消失监听
         * {@link #setOnDismissListener(OnDismissListener)}
         *
         * @param listener
         * @return
         */
        public T setOnDismissListener(OnDismissListener listener) {
            this.mOnDismissListener = listener;
            return backBuilder();
        }

        /**
         * 设置dialog 键盘事件监听
         * {@link #setOnKeyListener(OnKeyListener)}
         *
         * @param listener
         * @return
         */
        public T setOnKeyListener(OnKeyListener listener) {
            this.mOnKeyListener = listener;
            return backBuilder();
        }

        /**
         * 设置dialog显示事件监听
         * {@link #setOnShowListener(OnShowListener)}
         *
         * @param listener
         * @return
         */
        public T setOnShowListener(OnShowListener listener) {
            this.mOnShowListener = listener;
            return backBuilder();
        }

        /**
         * 设置Dialog Cancel监听
         * {@link #setOnCancelListener(OnCancelListener)}
         *
         * @param listener
         * @return
         */
        public T setOnCancelListener(OnCancelListener listener) {
            this.mOnCancelListener = listener;
            return backBuilder();
        }

        /**
         * 设置导航栏控制
         *
         * @param control
         * @return
         */
        public T setNavigationBarControl(NavigationBarControl control) {
            this.mNavigationBarControl = control;
            return backBuilder();
        }

        /**
         * 设置Dialog通用属性
         */
        protected void setDialog() {
            if (mDialog == null) {
                return;
            }
            mDialog.setDragEnable(mDragEnable);
            mDialog.setCancelable(mCancelable);
            mDialog.setCanceledOnTouchOutside(mCanceledOnTouchOutside);
            if (mOnDismissListener != null) {
                mDialog.setOnDismissListener(mOnDismissListener);
            }
            if (mOnKeyListener != null) {
                mDialog.setOnKeyListener(mOnKeyListener);
            }
            if (mOnCancelListener != null) {
                mDialog.setOnCancelListener(mOnCancelListener);
            }
            if (mOnShowListener != null) {
                mDialog.setOnShowListener(mOnShowListener);
            }
            if (mNavigationBarControl != null) {
                mDialog.setNavigationBarControl(mNavigationBarControl);
            }
        }

        protected void setRootView() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mLLayoutRoot.setElevation(mElevation);
            }
            mLLayoutRoot.removeAllViews();
            mLLayoutRoot.setPadding(mPadding, mPadding, mPadding, mPadding);
            mBackground = getBackground();
            if (mBackground != null) {
                setViewBackground(mLLayoutRoot, mBackground);
            }
        }

        /**
         * 通用设置View 背景Drawable
         *
         * @param view
         * @param drawable
         */
        protected void setViewBackground(View view, Drawable drawable) {
            if (view == null) {
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(DrawableUtil.getNewDrawable(drawable));
            } else {
                view.setBackgroundDrawable(DrawableUtil.getNewDrawable(drawable));
            }
        }

        /**
         * 设置TextView属性
         *
         * @param textView
         * @param sequence
         * @param textColor
         * @param textSize
         * @param gravity
         */
        protected void setTextAttribute(TextView textView, CharSequence sequence,
                                        ColorStateList textColor, float textSize, int gravity, boolean fakeBoldText) {
            if (textView == null) {
                return;
            }
            textView.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
            textView.setGravity(gravity);
            textView.setText(sequence);
            textView.setTextSize(mTextSizeUnit, textSize);
            textView.getPaint().setFakeBoldText(fakeBoldText);
            if (textColor != null) {
                textView.setTextColor(textColor);
            }
        }

        /**
         * 设置TextView 绘制监听
         *
         * @param textView
         */
        protected void setTextViewLine(final TextView textView) {
            if (textView == null) {
                return;
            }
            textView.post(new Runnable() {
                @Override
                public void run() {
                    if (mOnTextViewLineListener != null) {
                        mOnTextViewLineListener.onTextViewLineListener(textView, textView.getLineCount());
                    }
                }
            });
        }

        private Drawable getBackground() {
            if (mBackground instanceof ColorDrawable) {
                if (mBackgroundRadius > 0) {
                    GradientDrawable back = new GradientDrawable();
                    back.setCornerRadius(mBackgroundRadius);
                    back.setColor(((ColorDrawable) mBackground).getColor());
                    mBackground = back;
                }
            }
            return mBackground;
        }

        protected int dp2px(float dipValue) {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }

        protected int getScreenHeight() {
            return Resources.getSystem().getDisplayMetrics().heightPixels;
        }

        protected int getScreenWidth() {
            return Resources.getSystem().getDisplayMetrics().widthPixels;
        }
    }
}
