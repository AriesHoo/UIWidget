package com.aries.ui.widget.alert;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.widget.BasisDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2018/7/19 10:46
 * @E-Mail: AriesHoo@126.com
 * Function: UIAlert Dialog+Builder模式重构
 * Description:
 * 1、2018-3-29 14:07:04 新增获取Title及Message回调
 * 2、新增控制Button点击是否关闭弹框属性{@link Builder#setButtonClickDismissEnable(boolean)}
 */
public class UIAlertDialog extends BasisDialog<UIAlertDialog> {

    public UIAlertDialog(Context context) {
        super(context, R.style.AlertViewDialogStyle);
    }

    public interface ICreateButtons {

        /**
         * 设置Title 之前的View
         *
         * @return
         */
        View createBeforeTitle();

        /**
         * 创建Button
         *
         * @return
         */
        List<View> createButtons();
    }

    /**
     * 获取Title
     *
     * @return
     */
    public TextView getTitle() {
        return FindViewUtil.getTargetView(mContentView, R.id.tv_titleAlertDialog);
    }

    /**
     * 返回Message控件
     *
     * @return
     */
    public TextView getMessage() {
        return FindViewUtil.getTargetView(mContentView, R.id.tv_messageAlertDialog);
    }

    /**
     * 获取Button
     * {@link DialogInterface#BUTTON_NEGATIVE}
     * {@link DialogInterface#BUTTON_NEUTRAL}
     * {@link DialogInterface#BUTTON_POSITIVE}
     *
     * @param witch
     * @return
     */
    public Button getButton(int witch) {
        return FindViewUtil.getTargetView(mContentView, witch == DialogInterface.BUTTON_NEGATIVE ?
                R.id.btn_negativeAlertDialog : witch == DialogInterface.BUTTON_NEUTRAL ?
                R.id.btn_neutralAlertDialog : R.id.btn_positiveAlertDialog);
    }

    /**
     * QQ风格--title上边一个横线
     */
    public static class DividerQQBuilder extends DividerBuilder<DividerQQBuilder> {

        protected Drawable mTitleDivider;
        protected int mTitleDividerHeight;

        public DividerQQBuilder(Context context) {
            super(context);
            setTitleDividerHeight(dp2px(4))
                    .setTitleDividerColor(Color.argb(255, 17, 183, 245))
                    .setBackgroundColor(Color.WHITE)
                    .setBorderLessButtonEnable(true)
                    .setBackgroundPressedColor(Color.argb(255, 245, 245, 245))
                    .setBackgroundRadius(6f)
                    .setPadding(dp2px(20))
                    .setNegativeButtonTextSize(18f)
                    .setPositiveButtonTextSize(18f)
                    .setNeutralButtonTextSize(18f)
                    .setDividerColor(Color.argb(255, 230, 230, 230))
                    .setMessageTextColor(Color.BLACK)
                    .setNegativeButtonTextColor(Color.BLACK)
                    .setNeutralButtonTextColor(Color.BLACK)
                    .setPositiveButtonTextColor(Color.BLACK)
                    .setTitleTextColor(Color.BLACK)
                    .setTitleTextSize(20f);
        }

        /**
         * 设置分割线drawable
         *
         * @param drawable
         * @return
         */
        public DividerQQBuilder setTitleDivider(Drawable drawable) {
            mTitleDivider = drawable;
            return this;
        }

        public DividerQQBuilder setTitleDividerColor(int color) {
            return setTitleDivider(new ColorDrawable(color));
        }

        public DividerQQBuilder setTitleDividerResource(int res) {
            return setTitleDivider(mResourceUtil.getDrawable(res));
        }

        /**
         * 设置分割线宽度
         *
         * @param h
         * @return
         */
        public DividerQQBuilder setTitleDividerHeight(int h) {
            mTitleDividerHeight = h;
            return this;
        }

        public DividerQQBuilder setTitleDividerHeightResource(int res) {
            return setTitleDividerHeight(mResourceUtil.getDimensionPixelSize(res));
        }

        protected Drawable getTitleDrawable(Drawable drawable) {
            if (drawable != null && drawable instanceof ColorDrawable) {
                GradientDrawable gradient = new GradientDrawable();
                gradient.setColor(((ColorDrawable) drawable).getColor());
                float[] radiusArr = new float[8];
                radiusArr[0] = mBackgroundRadius;
                radiusArr[1] = mBackgroundRadius;
                radiusArr[2] = mBackgroundRadius;
                radiusArr[3] = mBackgroundRadius;
                gradient.setCornerRadii(radiusArr);
                drawable = gradient;
            }
            return drawable;
        }

        @Override
        public View createBeforeTitle() {
            if (mTitleDivider != null && mTitleDividerHeight > 0) {
                return getDivider(ViewGroup.LayoutParams.MATCH_PARENT,
                        mTitleDividerHeight,
                        getTitleDrawable(mTitleDivider));
            }
            return super.createBeforeTitle();
        }
    }

    public static class DividerIOSBuilder extends DividerBuilder<DividerIOSBuilder> {

        public DividerIOSBuilder(Context context) {
            super(context);
        }
    }

    /**
     * iOS风格
     */
    protected static class DividerBuilder<T extends DividerBuilder> extends Builder<T> {
        private Drawable mDivider;
        private int mDividerWidth;
        protected LinearLayout mLLayoutButton;
        protected List<Button> mListButton = new ArrayList<>();
        private int padding = dp2px(10);
        private int minHeight = dp2px(45);

        public DividerBuilder(Context context) {
            super(context);
            setDividerResource(R.color.colorAlertLineGray)
                    .setDividerWidthResource(R.dimen.dp_line_size)
                    .setTitleTextColorResource(R.color.colorAlertTitle)
                    .setMessageTextColorResource(R.color.colorAlertMessage)
                    .setNegativeButtonTextColorResource(R.color.colorAlertButton)
                    .setNeutralButtonTextColorResource(R.color.colorAlertButton)
                    .setPositiveButtonTextColorResource(R.color.colorAlertButton)
                    .setBackgroundRadiusResource(R.dimen.alert_radius);
        }

        /**
         * 设置分割线drawable
         *
         * @param drawable
         * @return
         */
        public T setDivider(Drawable drawable) {
            mDivider = drawable;
            return backBuilder();
        }

        public T setDividerColor(int color) {
            return setDivider(new ColorDrawable(color));
        }

        public T setDividerResource(int res) {
            return setDivider(mResourceUtil.getDrawable(res));
        }

        /**
         * 设置分割线宽度
         *
         * @param w
         * @return
         */
        public T setDividerWidth(int w) {
            mDividerWidth = w;
            return backBuilder();
        }

        public T setDividerWidthResource(int res) {
            return setDividerWidth(mResourceUtil.getDimensionPixelSize(res));
        }

        /**
         * 获取 分割线
         *
         * @param w
         * @param h
         * @param drawable
         * @return
         */
        protected View getDivider(int w, int h, Drawable drawable) {
            final View view = new View(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(w, h));
            setViewBackground(view, DrawableUtil.getNewDrawable(drawable));
            return view;
        }

        /**
         * 获取Button背景--圆角及按下效果转换
         *
         * @param gravity
         * @return
         */
        protected Drawable getButtonDrawable(int gravity) {
            StateListDrawable listDrawable = new StateListDrawable();
            Drawable drawable = DrawableUtil.getNewDrawable(mBackground);
            Drawable drawablePressed = DrawableUtil.getNewDrawable(mBackgroundPressed);
            //圆角且 背景均为ColorDrawable才进行圆角操作非中间
            if (mBackgroundRadius > 0
                    && ((mBackground instanceof ColorDrawable) || (mBackground instanceof GradientDrawable))
                    && (mBackgroundPressed instanceof ColorDrawable)) {
                GradientDrawable gradient = new GradientDrawable();
                GradientDrawable gradientPressed = new GradientDrawable();
                float[] radiusArr = new float[8];
                if (gravity == Gravity.LEFT || gravity == Gravity.BOTTOM) {//左边第一个/或只有一个
                    radiusArr[6] = mBackgroundRadius;
                    radiusArr[7] = mBackgroundRadius;
                }
                if (gravity == Gravity.RIGHT || gravity == Gravity.BOTTOM) {//右边/或只有一个
                    radiusArr[4] = mBackgroundRadius;
                    radiusArr[5] = mBackgroundRadius;
                }
                gradient.setCornerRadii(radiusArr);
                if (drawable instanceof ColorDrawable) {
                    gradient.setColor(((ColorDrawable) drawable).getColor());
                } else if (drawable instanceof GradientDrawable) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        gradient.setColor(((GradientDrawable) drawable).getColor().getDefaultColor());
                    }
                }
                gradientPressed.setCornerRadii(radiusArr);
                gradientPressed.setColor(((ColorDrawable) drawablePressed).getColor());

                drawable = gradient;
                drawablePressed = gradientPressed;
            }
            listDrawable.addState(new int[]{mStatePressed}, drawablePressed);
            listDrawable.addState(new int[]{}, drawable);
            return listDrawable;
        }

        /**
         * 添加Button
         *
         * @param sequence
         * @param textSize
         * @param textColor
         * @param fakeBoldText
         * @param witch
         * @param listener
         */
        protected void addButton(CharSequence sequence, float textSize,
                                 ColorStateList textColor, boolean fakeBoldText, final int witch, final OnClickListener listener) {
            if (TextUtils.isEmpty(sequence)) {
                return;
            }
            Button btn = new Button(mContext);
            if (mBorderLessButtonEnable) {
                btn = (Button) View.inflate(mContext, R.layout.layout_alert_button_border_less, null);
            }
            btn.setId(witch == DialogInterface.BUTTON_NEGATIVE ?
                    R.id.btn_negativeAlertDialog : witch == DialogInterface.BUTTON_NEUTRAL ?
                    R.id.btn_neutralAlertDialog : R.id.btn_positiveAlertDialog);

            setTextAttribute(btn, sequence, textColor, textSize, Gravity.CENTER, fakeBoldText);
            setTextViewLine(btn);
            btn.setPadding(padding, padding, padding, padding);
            btn.setMinimumHeight(minHeight);
            btn.setSingleLine(true);
            btn.setEllipsize(TextUtils.TruncateAt.MIDDLE);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(mDialog, witch);
                    }
                    if (mButtonClickDismissEnable) {
                        mDialog.dismiss();
                    }
                }
            });
            mListButton.add(btn);
        }

        @Override
        public View createBeforeTitle() {
            return null;
        }

        @Override
        public List<View> createButtons() {
            List<View> listView = new ArrayList<>();
            addButton(mNegativeButtonStr, mNegativeButtonTextSize,
                    mNegativeButtonTextColor, mNegativeButtonTextFakeBoldEnable,
                    DialogInterface.BUTTON_NEGATIVE, mOnNegativeButtonClickListener);
            addButton(mNeutralButtonStr, mNeutralButtonTextSize,
                    mNeutralButtonTextColor, mNeutralButtonTextFakeBoldEnable,
                    DialogInterface.BUTTON_NEUTRAL, mOnNeutralButtonClickListener);
            addButton(mPositiveButtonStr, mPositiveButtonTextSize,
                    mPositiveButtonTextColor, mPositiveButtonTextFakeBoldEnable,
                    DialogInterface.BUTTON_POSITIVE, mOnPositiveButtonClickListener);
            if (mListButton.size() == 0) {
                return listView;
            }
            //画水平分割线
            listView.add(getDivider(ViewGroup.LayoutParams.MATCH_PARENT, mDividerWidth, mDivider));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if (mListButton.size() == 1) {
                Button btn = mListButton.get(0);
                btn.setLayoutParams(params);
                setViewBackground(btn, getButtonDrawable(Gravity.BOTTOM));
                listView.add(btn);
                return listView;
            }
            //超过1个即水平LinearLayout包裹
            mLLayoutButton = new LinearLayout(mContext);
            mLLayoutButton.setMinimumHeight(minHeight);
            mLLayoutButton.setLayoutParams(params);
            int size = mListButton.size();
            for (int i = 0; i < size; i++) {
                Button btn = mListButton.get(i);
                btn.setLayoutParams(new LinearLayout.LayoutParams(
                        0, minHeight, 1.0f));
                mLLayoutButton.addView(btn);
                if (i != mListButton.size() - 1) {
                    //设置竖直分割线
                    mLLayoutButton.addView(getDivider(mDividerWidth, ViewGroup.LayoutParams.MATCH_PARENT, mDivider));
                }
                Drawable drawable;
                if (size == 2) {
                    drawable = getButtonDrawable(i == 0 ? Gravity.LEFT : Gravity.RIGHT);
                } else {
                    drawable = getButtonDrawable(i == 0 ? Gravity.LEFT : i == 1 ? Gravity.CENTER : Gravity.RIGHT);
                }
                setViewBackground(btn, drawable);
            }
            listView.add(mLLayoutButton);
            return listView;
        }
    }

    /**
     * 通用属性
     *
     * @param <T>
     */
    private static abstract class Builder<T extends Builder> extends BasisBuilder<T> implements ICreateButtons {

        protected boolean mBorderLessButtonEnable = true;
        protected TextView mTvTitle;
        protected CharSequence mTitleStr;
        protected ColorStateList mTitleTextColor;
        protected float mTitleTextSize = 16;
        protected boolean mTitleTextFakeBoldEnable;
        protected int mTitleTextGravity = Gravity.CENTER;

        protected TextView mTvMessage;
        protected CharSequence mMessageStr;
        protected ColorStateList mMessageTextColor;
        protected float mMessageTextSize = 16;
        protected boolean mMessageTextFakeBoldEnable;
        protected int mMessageTextGravity = Gravity.CENTER;
        protected int mCenterGravity = Gravity.CENTER;

        protected Drawable mBackgroundPressed;
        private List<View> mListViews = new ArrayList<>();

        protected LinearLayout mLLayoutContainer;
        protected ScrollView mSvView;
        protected LinearLayout mLLayoutView;

        protected boolean mButtonClickDismissEnable = true;
        protected CharSequence mNegativeButtonStr;
        protected ColorStateList mNegativeButtonTextColor;
        protected float mNegativeButtonTextSize = 16;
        protected boolean mNegativeButtonTextFakeBoldEnable;
        protected DialogInterface.OnClickListener mOnNegativeButtonClickListener;

        protected CharSequence mNeutralButtonStr;
        protected ColorStateList mNeutralButtonTextColor;
        protected float mNeutralButtonTextSize = 16;
        protected boolean mNeutralButtonTextFakeBoldEnable;
        protected DialogInterface.OnClickListener mOnNeutralButtonClickListener;

        protected CharSequence mPositiveButtonStr;
        protected ColorStateList mPositiveButtonTextColor;
        protected float mPositiveButtonTextSize = 16;
        protected boolean mPositiveButtonTextFakeBoldEnable;
        protected DialogInterface.OnClickListener mOnPositiveButtonClickListener;

        public Builder(Context context) {
            super(context);
            setBackgroundResource(R.color.colorAlertBg)
                    .setBackgroundPressedResource(R.color.colorAlertBgPressed)
                    .setTitleTextColorResource(R.color.colorAlertTitle)
                    .setMinWidthResource(R.dimen.alert_min_width)
                    .setMinHeightResource(R.dimen.alert_min_height)
                    .setPadding(mResourceUtil.getDimensionPixelSize(R.dimen.alert_padding));
        }

        /**
         * 设置5.0以后Button 按下无阴影
         *
         * @param enable
         * @return
         */
        public T setBorderLessButtonEnable(boolean enable) {
            this.mBorderLessButtonEnable = enable;
            return backBuilder();
        }

        /**
         * 设置Button按下背景Drawable
         *
         * @param drawable
         * @return
         */
        public T setBackgroundPressed(Drawable drawable) {
            this.mBackgroundPressed = drawable;
            return backBuilder();
        }

        /**
         * 设置根布局背景颜色值
         *
         * @param color
         * @return
         */
        public T setBackgroundPressedColor(int color) {
            return setBackgroundPressed(new ColorDrawable(color));
        }

        /**
         * 设置根布局背景资源
         *
         * @param resId
         * @return
         */
        public T setBackgroundPressedResource(int resId) {
            return setBackgroundPressed(mResourceUtil.getDrawable(resId));
        }

        /**
         * 设置标题
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setTitle(CharSequence charSequence) {
            this.mTitleStr = charSequence;
            return backBuilder();
        }

        public T setTitle(int resId) {
            return setTitle(mResourceUtil.getText(resId));
        }

        /**
         * 设置标题文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setTitleTextColor(ColorStateList color) {
            this.mTitleTextColor = color;
            return backBuilder();
        }

        public T setTitleTextColor(int color) {
            return setTitleTextColor(ColorStateList.valueOf(color));
        }

        public T setTitleTextColorResource(int resId) {
            return setTitleTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置标题文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link #setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setTitleTextSize(float size) {
            this.mTitleTextSize = size;
            return backBuilder();
        }

        /**
         * 设置文本是否粗体
         *
         * @param enable
         * @return
         */
        public T setTitleTextFakeBoldEnable(boolean enable) {
            this.mTitleTextFakeBoldEnable = enable;
            return backBuilder();
        }

        /**
         * 设置标题文本对齐方式
         * {@link TextView#setGravity(int)}
         *
         * @param gravity
         * @return
         */
        public T setTitleTextGravity(int gravity) {
            this.mTitleTextGravity = gravity;
            return backBuilder();
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
            return backBuilder();
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
        public T setMessageTextColor(ColorStateList color) {
            this.mMessageTextColor = color;
            return backBuilder();
        }

        public T setMessageTextColor(int color) {
            return setMessageTextColor(ColorStateList.valueOf(color));
        }

        public T setMessageTextColorResource(int resId) {
            return setMessageTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置标题文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link #setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setMessageTextSize(float size) {
            this.mMessageTextSize = size;
            return backBuilder();
        }

        /**
         * 设置文本是否粗体
         *
         * @param enable
         * @return
         */
        public T setMessageTextFakeBoldEnable(boolean enable) {
            this.mMessageTextFakeBoldEnable = enable;
            return backBuilder();
        }

        /**
         * 设置标题文本对齐方式
         * {@link TextView#setGravity(int)}
         *
         * @param gravity
         * @return
         */
        public T setMessageTextGravity(int gravity) {
            this.mMessageTextGravity = gravity;
            return backBuilder();
        }

        /**
         * 设置 Message相应父容器对齐方式
         *
         * @param gravity
         * @return
         */
        public T setCenterGravity(int gravity) {
            this.mCenterGravity = gravity;
            return backBuilder();
        }

        /**
         * 添加View
         *
         * @param v
         * @return
         */
        public T setView(View v) {
            if (v != null) {
                mListViews.add(v);
            }
            return backBuilder();
        }

        public T setView(int res) {
            return setView(View.inflate(mContext, res, null));
        }

        /**
         * 设置点击Button是否关闭弹窗
         *
         * @param enable
         * @return
         */
        public T setButtonClickDismissEnable(boolean enable) {
            this.mButtonClickDismissEnable = enable;
            return backBuilder();
        }

        /**
         * 设置Button
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setNegativeButton(CharSequence charSequence, DialogInterface.OnClickListener listener) {
            this.mNegativeButtonStr = charSequence;
            this.mOnNegativeButtonClickListener = listener;
            return backBuilder();
        }

        public T setNegativeButton(int resId, DialogInterface.OnClickListener listener) {
            return setNegativeButton(mResourceUtil.getText(resId), listener);
        }

        /**
         * 设置文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setNegativeButtonTextColor(ColorStateList color) {
            this.mNegativeButtonTextColor = color;
            return backBuilder();
        }

        public T setNegativeButtonTextColor(int color) {
            return setNegativeButtonTextColor(ColorStateList.valueOf(color));
        }

        public T setNegativeButtonTextColorResource(int resId) {
            return setNegativeButtonTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link #setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setNegativeButtonTextSize(float size) {
            this.mNegativeButtonTextSize = size;
            return backBuilder();
        }

        /**
         * 设置文本是否粗体
         *
         * @param enable
         * @return
         */
        public T setNegativeButtonFakeBoldEnable(boolean enable) {
            this.mNegativeButtonTextFakeBoldEnable = enable;
            return backBuilder();
        }

        /**
         * 设置Button
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setNeutralButton(CharSequence charSequence, DialogInterface.OnClickListener listener) {
            this.mNeutralButtonStr = charSequence;
            this.mOnNeutralButtonClickListener = listener;
            return backBuilder();
        }

        public T setNeutralButton(int resId, DialogInterface.OnClickListener listener) {
            return setNeutralButton(mResourceUtil.getText(resId), listener);
        }

        /**
         * 设置文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setNeutralButtonTextColor(ColorStateList color) {
            this.mNeutralButtonTextColor = color;
            return backBuilder();
        }

        public T setNeutralButtonTextColor(int color) {
            return setNeutralButtonTextColor(ColorStateList.valueOf(color));
        }

        public T setNeutralButtonTextColorResource(int resId) {
            return setNeutralButtonTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link #setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setNeutralButtonTextSize(float size) {
            this.mNeutralButtonTextSize = size;
            return backBuilder();
        }

        /**
         * 设置文本是否粗体
         *
         * @param enable
         * @return
         */
        public T setNeutralButtonFakeBoldEnable(boolean enable) {
            this.mNeutralButtonTextFakeBoldEnable = enable;
            return backBuilder();
        }

        /**
         * 设置Button
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setPositiveButton(CharSequence charSequence, DialogInterface.OnClickListener listener) {
            this.mPositiveButtonStr = charSequence;
            this.mOnPositiveButtonClickListener = listener;
            return backBuilder();
        }

        public T setPositiveButton(int resId, DialogInterface.OnClickListener listener) {
            return setPositiveButton(mResourceUtil.getText(resId), listener);
        }

        /**
         * 设置文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setPositiveButtonTextColor(ColorStateList color) {
            this.mPositiveButtonTextColor = color;
            return backBuilder();
        }

        public T setPositiveButtonTextColor(int color) {
            return setPositiveButtonTextColor(ColorStateList.valueOf(color));
        }

        public T setPositiveButtonTextColorResource(int resId) {
            return setPositiveButtonTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link #setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setPositiveButtonTextSize(float size) {
            this.mPositiveButtonTextSize = size;
            return backBuilder();
        }

        /**
         * 设置文本是否粗体
         *
         * @param enable
         * @return
         */
        public T setPositiveButtonFakeBoldEnable(boolean enable) {
            this.mPositiveButtonTextFakeBoldEnable = enable;
            return backBuilder();
        }

        public UIAlertDialog create() {
            int margin = dp2px(16);
            View contentView = createContentView();
            mDialog = new UIAlertDialog(mContext);
            mDialog.setContentView(contentView);
            setDialog();
            mDialog.setGravity(Gravity.CENTER);
            mDialog.setMargin(margin, margin, margin, margin);
            return (UIAlertDialog) mDialog;
        }

        private View createContentView() {
            mLLayoutRoot = new LinearLayout(mContext);
            mLLayoutRoot.setId(R.id.lLayout_rootAlertDialog);
            mLLayoutRoot.setOrientation(LinearLayout.VERTICAL);
            mLLayoutRoot.setMinimumWidth(mMinWidth);
            mLLayoutRoot.setMinimumHeight(mMinHeight);
            setRootView();
            if (createBeforeTitle() != null) {
                mLLayoutRoot.addView(createBeforeTitle());
            }
            createTitle();
            createMessage();
            createContainerView();
            mLLayoutRoot.setPadding(0, 0, 0, 0);
            for (View v : createButtons()) {
                if (v != null) {
                    mLLayoutRoot.addView(v);
                }
            }
            return mLLayoutRoot;
        }

        private void createTitle() {
            if (TextUtils.isEmpty(mTitleStr)) {
                return;
            }
            mTvTitle = new TextView(mContext);
            mTvTitle.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvTitle.setMaxLines(2);
            mTvTitle.setId(R.id.tv_titleAlertDialog);
            mLLayoutRoot.addView(mTvTitle);

            setTextViewLine(mTvTitle);
            setTextAttribute(mTvTitle, mTitleStr, mTitleTextColor, mTitleTextSize, mTitleTextGravity, mTitleTextFakeBoldEnable);
            mTvTitle.setPadding(mPadding, mPadding, mPadding, 0);
        }

        private void createContainerView() {
            mLLayoutContainer = new LinearLayout(mContext);
            mLLayoutContainer.setId(R.id.lLayout_containerAlertDialog);
            mLLayoutContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
            mLLayoutContainer.setOrientation(LinearLayout.VERTICAL);
            mLLayoutContainer.setPadding(mPadding, dp2px(12), mPadding, dp2px(12));
            mLLayoutContainer.setGravity(mCenterGravity);

            mLLayoutRoot.addView(mLLayoutContainer);

            mLLayoutView = new LinearLayout(mContext);
            mLLayoutContainer.setId(R.id.lLayout_ViewAlertDialog);
            mLLayoutView.setOrientation(LinearLayout.VERTICAL);
            mLLayoutView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            mSvView = new ScrollView(mContext);
            mSvView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mSvView.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mSvView.setVerticalScrollBarEnabled(false);
            mSvView.addView(mLLayoutView);

            mLLayoutContainer.addView(mSvView);
            if (mListViews != null) {
                for (View v : mListViews) {
                    mLLayoutView.addView(v);
                }
            }

        }

        private void createMessage() {
            if (TextUtils.isEmpty(mMessageStr)) {
                return;
            }
            ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            mTvMessage = new TextView(mContext);
            mTvMessage.setLayoutParams(params);
            mTvMessage.setId(R.id.tv_messageAlertDialog);
            mListViews.add(0, mTvMessage);

            setTextAttribute(mTvMessage, mMessageStr, mMessageTextColor, mMessageTextSize, mMessageTextGravity, mMessageTextFakeBoldEnable);
            mTvMessage.post(new Runnable() {
                @Override
                public void run() {
                    int lineCount = mTvMessage.getLineCount();
                    if (lineCount > 1) {
                        mTvMessage.setGravity(Gravity.LEFT | mTvMessage.getGravity());
                    }
                    if (mOnTextViewLineListener != null) {
                        mOnTextViewLineListener.onTextViewLineListener(mTvMessage, lineCount);
                    }
                }
            });
        }
    }
}
