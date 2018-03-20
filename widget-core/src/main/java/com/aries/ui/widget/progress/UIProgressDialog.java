package com.aries.ui.widget.progress;

import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.ui.util.ResourceUtil;
import com.aries.ui.widget.R;
import com.aries.ui.widget.UIBasisDialog;

/**
 * Created: AriesHoo on 2018/3/19/019 17:36
 * E-Mail: AriesHoo@126.com
 * Function:UIProgress Dialog模式重构
 * Description:
 */
public class UIProgressDialog extends UIBasisDialog<UIProgressDialog> {

    public UIProgressDialog(Context context) {
        super(context, R.style.ProgressViewDialogStyle);
    }

    public interface ICreateContentView {
        View createProgressView();

        int getGravity();

        int getOrientation();
    }

    /**
     * Material 风格
     */
    public static class MaterialBuilder extends Builder<MaterialBuilder> {

        private MaterialProgressBar mProgressBar;
        private int mLoadingColor = Color.BLUE;
        private int mDuration = 600;
        private boolean mRoundEnable;
        private float mBorderWidth = 6;

        public MaterialBuilder(Context context) {
            super(context);
            mLoadingColor = mResourceUtil.getAttrColor(android.R.attr.colorAccent);
            setTextColor(mLoadingColor);
        }

        /**
         * 设置弧度颜色
         *
         * @param color
         * @return
         */
        public MaterialBuilder setLoadingColor(int color) {
            mLoadingColor = color;
            return this;
        }

        /**
         * @param res
         * @return
         */
        public MaterialBuilder setLoadingColorResource(int res) {
            return setLoadingColor(mResourceUtil.getColor(res));
        }

        /**
         * 设置转动速度
         *
         * @param duration
         * @return
         */
        public MaterialBuilder setDuration(int duration) {
            mDuration = duration;
            return this;
        }

        /**
         * 设置是否圆角
         *
         * @param enable
         * @return
         */
        public MaterialBuilder setRoundEnable(boolean enable) {
            this.mRoundEnable = enable;
            return this;
        }

        /**
         * 设置弧度粗细
         *
         * @param w
         * @return
         */
        public MaterialBuilder setBorderWidth(float w) {
            this.mBorderWidth = w;
            return this;
        }

        @Override
        public View createProgressView() {
            mProgressBar = new MaterialProgressBar(mContext);
            mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(mLoadingSize, mLoadingSize));
            mProgressBar.setDuration(mDuration)
                    .setRoundEnable(mRoundEnable)
                    .setBorderWidth(mBorderWidth)
                    .setArcColor(mLoadingColor);
            return mProgressBar;
        }
    }

    /**
     * 微博风格
     */
    public static class WeBoBuilder extends ProgressBarBuilder<WeBoBuilder> {

        public WeBoBuilder(Context context) {
            super(context);
            setIndeterminateDrawable(R.drawable.dialog_loading_wei_bo)
                    .setBackgroundResource(R.color.colorLoadingBgWei)
                    .setMinWidth(dp2px(150))
                    .setMinHeight(dp2px(110))
                    .setTextColorResource(R.color.colorLoadingTextWeiBo);
        }

        @Override
        public int getGravity() {
            return Gravity.CENTER;
        }

        @Override
        public int getOrientation() {
            return LinearLayout.VERTICAL;
        }
    }

    /**
     * 类微信模式
     */
    public static class WeChatBuilder extends ProgressBarBuilder<WeChatBuilder> {
        public WeChatBuilder(Context context) {
            super(context);
            setIndeterminateDrawable(R.drawable.dialog_loading_wei_xin)
                    .setTextColorResource(R.color.colorLoadingTextWeiBo)
                    .setBackgroundResource(R.color.colorLoadingBgWei);
        }
    }

    /**
     * 标准模式
     */
    public static class NormalBuilder extends ProgressBarBuilder<NormalBuilder> {
        public NormalBuilder(Context context) {
            super(context);
        }
    }

    /**
     * 系统默认ProgressBar Builder
     *
     * @param <T>
     */
    private static class ProgressBarBuilder<T extends ProgressBarBuilder> extends Builder<T> {
        private Drawable mIndeterminateDrawable;
        private ProgressBar mProgressBar;

        public ProgressBarBuilder(Context context) {
            super(context);
        }

        public T setIndeterminateDrawable(Drawable drawable) {
            mIndeterminateDrawable = drawable;
            return (T) this;
        }

        public T setIndeterminateDrawable(int resId) {
            return setIndeterminateDrawable(mResourceUtil.getDrawable(resId));
        }

        @Override
        public View createProgressView() {
            mProgressBar = new ProgressBar(mContext);
            mProgressBar.setLayoutParams(new ViewGroup.LayoutParams(mLoadingSize, mLoadingSize));
            if (mIndeterminateDrawable != null) {
                mProgressBar.setIndeterminateDrawable(mIndeterminateDrawable);
            }
            return mProgressBar;
        }
    }

    /**
     * 基础Builder
     *
     * @param <T>
     */
    private static abstract class Builder<T extends Builder> implements ICreateContentView {
        protected Context mContext;
        protected UIProgressDialog mDialog;
        protected ResourceUtil mResourceUtil;

        protected LinearLayout mLLayoutRoot;
        protected Drawable mBackground;
        protected float mBackgroundRadius;
        protected int mPadding;
        protected int mLoadingSize;
        protected int mMinWidth;
        protected int mMinHeight;

        protected TextView mTvText;
        protected CharSequence mTextStr;
        protected ColorStateList mTextColor;
        protected float mTextSize = 14;
        protected int mTextSizeUnit = TypedValue.COMPLEX_UNIT_DIP;
        protected int mTextPadding = 16;

        protected boolean mCancelable;
        protected boolean mCanceledOnTouchOutside = true;
        protected OnTextViewLineListener mOnTextViewLineListener;
        protected OnDismissListener mOnDismissListener;
        protected OnKeyListener mOnKeyListener;
        protected OnCancelListener mOnCancelListener;
        protected OnShowListener mOnShowListener;

        public Builder(Context context) {
            this.mContext = context;
            this.mResourceUtil = new ResourceUtil(mContext);
            setBackgroundResource(R.color.colorLoadingBg)
                    .setBackgroundRadius(mResourceUtil.getDimension(R.dimen.dp_radius_loading))
                    .setLoadingSize(mResourceUtil.getDimensionPixelSize(R.dimen.dp_size_loading))
                    .setTextColorResource(R.color.colorLoadingText)
                    .setMinWidth(dp2px(200))
                    .setMinHeight(dp2px(65))
                    .setPadding(dp2px(16));
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

        public T setBackgroundRadius(int res) {
            return setBackgroundRadius(mResourceUtil.getDimension(res));
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
         * 设置Loading 宽高
         *
         * @param size
         * @return
         */
        public T setLoadingSize(int size) {
            this.mLoadingSize = size;
            return (T) this;
        }

        /**
         * 设置view最小宽度--有Text有效其它通过
         * {@link #setPadding(int)}
         *
         * @param w
         * @return
         */
        public T setMinWidth(int w) {
            this.mMinWidth = w;
            return (T) this;
        }

        /**
         * 设置view最小高度--有Text有效其它通过
         *
         * @param h
         * @return
         */
        public T setMinHeight(int h) {
            this.mMinHeight = h;
            return (T) this;
        }

        /**
         * 设置标题
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setText(CharSequence charSequence) {
            this.mTextStr = charSequence;
            return (T) this;
        }

        public T setText(int resId) {
            return setText(mResourceUtil.getText(resId));
        }

        /**
         * 设置标题文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setTextColor(ColorStateList color) {
            this.mTextColor = color;
            return (T) this;
        }

        public T setTextColor(int color) {
            return setTextColor(ColorStateList.valueOf(color));
        }

        public T setTextColorResource(int resId) {
            return setTextColor(mResourceUtil.getColorStateList(resId));
        }

        public T setTextSize(float size) {
            this.mTextSize = size;
            return (T) this;
        }

        /**
         * 设置标题文本尺寸
         * {@link TextView#setTextSize(int, float)}
         *
         * @param size
         * @return
         */
        public T setTextSize(int unit, float size) {
            this.mTextSize = size;
            this.mTextSizeUnit = unit;
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
         * 设置文本padding
         *
         * @param padding
         * @return
         */
        public T setTextPadding(int padding) {
            this.mTextPadding = padding;
            return (T) this;
        }

        /**
         * 设置dialog 是否可点击返回键关闭
         * {@link Dialog#setCancelable(boolean)}
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
         * {@link Dialog#setOnKeyListener(OnKeyListener)}
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
         * {@link Dialog#setOnShowListener(OnShowListener)}
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
         * {@link Dialog#setOnCancelListener(OnCancelListener)}
         *
         * @param listener
         * @return
         */
        public T setOnCancelListener(OnCancelListener listener) {
            this.mOnCancelListener = listener;
            return (T) this;
        }

        public UIProgressDialog create() {
            View contentView = createContentView();
            mDialog = new UIProgressDialog(mContext);
            mDialog.setContentView(contentView, contentView.getLayoutParams());
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
            mDialog.setGravity(Gravity.CENTER);
            mDialog.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            return mDialog;
        }

        private View createContentView() {
            mLLayoutRoot = new LinearLayout(mContext);
            mLLayoutRoot.setId(R.id.lLayout_rootProgressView);
            mLLayoutRoot.setOrientation(getOrientation());
            mLLayoutRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mLLayoutRoot.removeAllViews();
            mLLayoutRoot.setGravity(getGravity());
            mLLayoutRoot.setPadding(mPadding, mPadding, mPadding, mPadding);
            if (mBackground != null) {
                mBackground = getBackground();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLLayoutRoot.setBackground(mBackground);
                } else {
                    mLLayoutRoot.setBackgroundDrawable(mBackground);
                }
            }
            mLLayoutRoot.addView(createProgressView());
            createText();
            return mLLayoutRoot;
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

        private void createText() {
            if (TextUtils.isEmpty(mTextStr)) {
                return;
            }
            mLLayoutRoot.setMinimumWidth(mMinWidth);
            mLLayoutRoot.setMinimumHeight(mMinHeight);

            mTvText = new TextView(mContext);
            mTvText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvText.setId(R.id.tv_textProgressView);
            mLLayoutRoot.addView(mTvText);

            mTvText.setText(mTextStr);
            mTvText.setTextSize(mTextSizeUnit, mTextSize);
            mTvText.setTextColor(mTextColor);
            mTvText.setPadding(mTextPadding, mTextPadding, mTextPadding, mTextPadding);
            mTvText.post(new Runnable() {
                @Override
                public void run() {
                    if (mOnTextViewLineListener != null) {
                        mOnTextViewLineListener.onTextViewLineListener(mTvText, mTvText.getLineCount());
                    }
                }
            });
        }

        @Override
        public int getGravity() {
            return Gravity.CENTER_VERTICAL;
        }

        @Override
        public int getOrientation() {
            return LinearLayout.HORIZONTAL;
        }

        protected int dp2px(float dipValue) {
            final float scale = Resources.getSystem().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        }
    }
}
