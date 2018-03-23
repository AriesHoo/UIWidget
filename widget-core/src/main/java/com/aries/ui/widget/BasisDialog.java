package com.aries.ui.widget;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.util.ResourceUtil;

/**
 * Created: AriesHoo on 2018/3/20/020 8:48
 * E-Mail: AriesHoo@126.com
 * Function: Widget Dialog模式基类
 * Description:
 * 1、新增基础Builder包装通用属性
 */
public class BasisDialog<T extends BasisDialog> extends Dialog {

    private Window mWindow;
    protected View mContentView;
    private WindowManager.LayoutParams mLayoutParams;
    private float mAlpha = 1.0f;
    private float mDimAmount = 0.6f;
    private int mGravity = Gravity.CENTER;
    private int mWidth = WindowManager.LayoutParams.WRAP_CONTENT;
    private int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;
    private int mWindowAnimations = -1;

    public interface OnTextViewLineListener {
        void onTextViewLineListener(TextView textView, int lineCount);
    }

    public BasisDialog(Context context) {
        super(context);
    }

    public BasisDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BasisDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
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
        mLayoutParams.alpha = mAlpha;// 透明度
        mLayoutParams.dimAmount = mDimAmount;// 黑暗度
        if (mWindowAnimations != -1) {
            mLayoutParams.windowAnimations = mWindowAnimations;// 动画
        }
        mWindow.setAttributes(mLayoutParams);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.mContentView = view;
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

    public T setAttributes(WindowManager.LayoutParams params) {
        this.mLayoutParams = params;
        return (T) this;
    }

    public T setAlpha(float alpha) {
        mAlpha = alpha;
        return (T) this;
    }

    public T setDimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    public T setGravity(int gravity) {
        mGravity = gravity;
        return (T) this;
    }

    public T setWidth(int w) {
        this.mWidth = w;
        return (T) this;
    }

    public T setHeight(int h) {
        this.mHeight = h;
        return (T) this;
    }

    /**
     * 设置动画资源
     *
     * @param res
     * @return
     */
    public T setWindowAnimations(int res) {
        this.mWindowAnimations = res;
        return (T) this;
    }

    protected void closeKeyboard() {
        if (mContentView == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) mContentView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mContentView.getWindowToken(), 0);
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

        protected boolean mCancelable;
        protected boolean mCanceledOnTouchOutside = true;
        protected OnTextViewLineListener mOnTextViewLineListener;
        protected OnDismissListener mOnDismissListener;
        protected OnKeyListener mOnKeyListener;
        protected OnCancelListener mOnCancelListener;
        protected OnShowListener mOnShowListener;


        public BasisBuilder(Context context) {
            mContext = context;
            mResourceUtil = new ResourceUtil(mContext);
        }

        /**
         * 设置根布局背景Drawable
         *
         * @param drawable
         * @return
         */
        public T setBackground(Drawable drawable) {
            this.mBackground = drawable;
            return (T) this;
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
            return (T) this;
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
            return (T) this;
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
            return (T) this;
        }

        /**
         * 设置根布局最小宽度
         *
         * @param w
         * @return
         */
        public T setMinWidth(int w) {
            this.mMinWidth = w;
            return (T) this;
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
            return (T) this;
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
            return (T) this;
        }

        /**
         * {@link TextView#setLineSpacing(float, float)}
         *
         * @param lineSpacingExtra
         * @return
         */
        public T setLineSpacingExtra(float lineSpacingExtra) {
            mLineSpacingExtra = lineSpacingExtra;
            return (T) this;
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
            return (T) this;
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
            return (T) this;
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
            return (T) this;
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
            return (T) this;
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
            return (T) this;
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
            return (T) this;
        }

        /**
         * 设置dialog显示事件监听
         * {@link #setOnShowListener(OnShowListener)}
         *
         * @param listener
         * @return
         */
        public T setOnShowListenerer(OnShowListener listener) {
            this.mOnShowListener = listener;
            return (T) this;
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
            return (T) this;
        }

        /**
         * 设置Dialog通用属性
         */
        protected void setDialog() {
            if (mDialog == null) {
                return;
            }
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

        protected void afterSetContentView() {
            //当设置点击其它地方dialog可消失需对root的父容器处理
            if (mCanceledOnTouchOutside) {
                //设置点击事件防止事件透传至父容器
                mLLayoutRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                ((ViewGroup) mLLayoutRoot.getParent()).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialog.dismiss();
                    }
                });
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
                view.setBackground(drawable);
            } else {
                view.setBackgroundDrawable(drawable);
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
