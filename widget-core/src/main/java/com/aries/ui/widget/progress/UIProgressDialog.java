package com.aries.ui.widget.progress;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.ui.util.FindViewUtil;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2018/3/19/019 17:36
 * E-Mail: AriesHoo@126.com
 * Function:UIProgress Dialog模式重构
 * Description:
 * 1、新增progressBar颜色值设置
 */
public class UIProgressDialog extends BasisDialog<UIProgressDialog> {

    public UIProgressDialog(Context context) {
        super(context, R.style.ProgressViewDialogStyle);
    }

    public interface ICreateContentView {
        View createProgressView();

        int getGravity();

        int getOrientation();
    }

    /**
     * 获取Message控件
     *
     * @return
     */
    public TextView getMessage() {
        return FindViewUtil.getTargetView(mContentView, R.id.tv_messageProgressDialog);
    }

    /**
     * Material 风格
     */
    public static class MaterialBuilder extends Builder<MaterialBuilder> {

        private MaterialProgressBar mProgressBar;
        private int mDuration = 600;
        private boolean mRoundEnable;
        private float mBorderWidth = 6;

        public MaterialBuilder(Context context) {
            super(context);
            setTextColor(mLoadingColor)
                    .setBorderWidth(dp2px(3));
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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mProgressBar.setIndeterminateTintList(ColorStateList.valueOf(mLoadingColor));
            }
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
    private static abstract class Builder<T extends Builder> extends BasisBuilder<T> implements ICreateContentView {

        protected int mLoadingSize;

        protected TextView mTvMessage;
        protected CharSequence mMessageStr;
        protected ColorStateList mTextColor;
        protected float mTextSize = 14;
        protected int mTextPadding = 16;
        protected int mLoadingColor = Color.BLUE;

        public Builder(Context context) {
            super(context);
            mLoadingColor = mResourceUtil.getAttrColor(android.R.attr.colorAccent);
            setBackgroundResource(R.color.colorLoadingBg)
                    .setLoadingSize(dp2px(30))
                    .setTextColorResource(R.color.colorLoadingText)
                    .setMinWidth(dp2px(200))
                    .setMinHeight(dp2px(65))
                    .setBackgroundRadiusResource(R.dimen.dp_radius_loading)
                    .setPadding(dp2px(16));
        }


        /**
         * 设置弧度颜色
         *
         * @param color
         * @return
         */
        public T setLoadingColor(int color) {
            mLoadingColor = color;
            return (T) this;
        }

        /**
         * @param res
         * @return
         */
        public T setLoadingColorResource(int res) {
            return setLoadingColor(mResourceUtil.getColor(res));
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
         * 设置标题
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setMessage(CharSequence charSequence) {
            this.mMessageStr = charSequence;
            return (T) this;
        }

        public T setMessage(int resId) {
            return setMessage(mResourceUtil.getText(resId));
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
         * 设置文本padding
         *
         * @param padding
         * @return
         */
        public T setTextPadding(int padding) {
            this.mTextPadding = padding;
            return (T) this;
        }

        public UIProgressDialog create() {
            int margin = dp2px(12);
            View contentView = createContentView();
            mDialog = new UIProgressDialog(mContext);
            mDialog.setContentView(contentView);
            setDialog();
            mDialog.setGravity(Gravity.CENTER);
            mDialog.setMargin(margin, margin, margin, margin);
            afterSetContentView();
            return (UIProgressDialog) mDialog;
        }

        private View createContentView() {
            mLLayoutRoot = new LinearLayout(mContext);
            mLLayoutRoot.setId(R.id.lLayout_rootProgressDialog);
            mLLayoutRoot.setOrientation(getOrientation());
            mLLayoutRoot.setGravity(getGravity());

            setRootView();
            mLLayoutRoot.addView(createProgressView());
            createText();
            return mLLayoutRoot;
        }

        private void createText() {
            if (TextUtils.isEmpty(mMessageStr)) {
                return;
            }
            mLLayoutRoot.setMinimumWidth(mMinWidth);
            mLLayoutRoot.setMinimumHeight(mMinHeight);

            mTvMessage = new TextView(mContext);
            mTvMessage.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvMessage.setId(R.id.tv_messageProgressDialog);
            mLLayoutRoot.addView(mTvMessage);

            setTextViewLine(mTvMessage);
            setTextAttribute(mTvMessage, mMessageStr, mTextColor, mTextSize, Gravity.LEFT, false);
            mTvMessage.setPadding(mTextPadding, mTextPadding, mTextPadding, mTextPadding);

        }

        @Override
        public int getGravity() {
            return Gravity.CENTER_VERTICAL;
        }

        @Override
        public int getOrientation() {
            return LinearLayout.HORIZONTAL;
        }
    }
}
