package com.aries.ui.view.radius.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.widget.R;

/**
 * @Author: AriesHoo on 2018/7/19 9:57
 * @E-Mail: AriesHoo@126.com
 * Function: 设置CompoundButton ButtonDrawable相关代理
 * Description:
 * 1、新增控制是否Button为系统自带属性
 */
public class RadiusCompoundDelegate<T extends RadiusCompoundDelegate> extends RadiusTextDelegate<T> {

    private CompoundButton mButton;
    private StateListDrawable mStateButtonDrawable;

    private boolean mButtonDrawableSystemEnable;
    private float mButtonDrawableColorRadius;
    private boolean mButtonDrawableColorCircleEnable;
    private int mButtonDrawableWidth;
    private int mButtonDrawableHeight;
    private Drawable mButtonDrawable;
    private Drawable mButtonPressedDrawable;
    private Drawable mButtonDisabledDrawable;
    private Drawable mButtonSelectedDrawable;
    private Drawable mButtonCheckedDrawable;


    public RadiusCompoundDelegate(CompoundButton view, Context context, AttributeSet attrs) {
        super(view, context, attrs);
    }


    @Override
    protected void initAttributes(Context context, AttributeSet attrs) {
        mButtonDrawableSystemEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_buttonDrawableSystemEnable, false);
        mButtonDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_buttonDrawableColorRadius, 0);
        mButtonDrawableColorCircleEnable = mTypedArray.getBoolean(R.styleable.RadiusSwitch_rv_buttonDrawableColorCircleEnable, false);
        mButtonDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_buttonDrawableWidth, -1);
        mButtonDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_buttonDrawableHeight, -1);
        mButtonDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonDrawable);
        mButtonPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonPressedDrawable);
        mButtonDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonDisabledDrawable);
        mButtonSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonSelectedDrawable);
        mButtonCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_buttonCheckedDrawable);

//        mButtonPressedDrawable = mButtonPressedDrawable == null ? mButtonDrawable : mButtonPressedDrawable;
//        mButtonDisabledDrawable = mButtonDisabledDrawable == null ? mButtonDrawable : mButtonDisabledDrawable;
//        mButtonSelectedDrawable = mButtonSelectedDrawable == null ? mButtonDrawable : mButtonSelectedDrawable;
//        mButtonCheckedDrawable = mButtonCheckedDrawable == null ? mButtonDrawable : mButtonCheckedDrawable;
        super.initAttributes(context, attrs);
    }

    @Override
    public void init() {
        super.init();
        setButtonDrawable();
    }

    /**
     * 是否系统自带ButtonDrawable
     *
     * @param buttonDrawableSystemEnable
     * @return
     */
    public T setButtonDrawableSystemEnable(boolean buttonDrawableSystemEnable) {
        mButtonDrawableSystemEnable = buttonDrawableSystemEnable;
        return back();
    }

    /**
     * colorDrawable 圆角
     *
     * @param buttonDrawableColorRadius
     * @return
     */
    public T setButtonDrawableColorRadius(float buttonDrawableColorRadius) {
        mButtonDrawableColorRadius = buttonDrawableColorRadius;
        return back();
    }

    /**
     * 是否ColorDrawable 为圆形
     *
     * @param buttonDrawableColorCircleEnable
     * @return
     */
    public T setButtonDrawableColorCircleEnable(boolean buttonDrawableColorCircleEnable) {
        mButtonDrawableColorCircleEnable = buttonDrawableColorCircleEnable;
        return back();
    }

    /**
     * 设置drawable宽度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableWidth
     * @return
     */
    public T setButtonDrawableWidth(int drawableWidth) {
        mButtonDrawableWidth = drawableWidth;
        return (T) this;
    }

    /**
     * 设置drawable高度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableHeight
     * @return
     */
    public T setButtonDrawableHeight(int drawableHeight) {
        mButtonDrawableHeight = drawableHeight;
        return (T) this;
    }

    /**
     * 设置默认状态Drawable
     *
     * @param drawable
     */
    public T setButtonDrawable(Drawable drawable) {
        mButtonDrawable = drawable;
        return (T) this;
    }

    public T setButtonDrawable(int resId) {
        return setButtonDrawable(getDrawable(resId));
    }

    /**
     * 设置按下效果Drawable
     *
     * @param drawable
     */
    public T setButtonPressedDrawable(Drawable drawable) {
        mButtonPressedDrawable = drawable;
        return (T) this;
    }

    public T setButtonPressedDrawable(int resId) {
        return setButtonPressedDrawable(getDrawable(resId));
    }

    /**
     * 设置不可操作效果Drawable
     *
     * @param drawable
     */
    public T setButtonDisabledDrawable(Drawable drawable) {
        mButtonDisabledDrawable = drawable;
        return (T) this;
    }

    public T setButtonDisabledDrawable(int resId) {
        return setButtonDisabledDrawable(getDrawable(resId));
    }

    /**
     * 设置选中效果Drawable
     *
     * @param drawable
     * @return
     */
    public T setButtonSelectedDrawable(Drawable drawable) {
        mButtonSelectedDrawable = drawable;
        return (T) this;
    }

    public T setButtonSelectedDrawable(int resId) {
        return setButtonSelectedDrawable(getDrawable(resId));
    }

    /**
     * 设置Checked状态Drawable
     *
     * @param drawable
     * @return
     */
    public T setButtonCheckedDrawable(Drawable drawable) {
        mButtonCheckedDrawable = drawable;
        return (T) this;
    }

    public T setButtonCheckedDrawable(int resId) {
        return setButtonCheckedDrawable(getDrawable(resId));
    }

    /**
     * 设置CompoundButton的setButtonDrawable属性
     */
    private void setButtonDrawable() {
        mButton = (CompoundButton) mView;
        if (mButtonDrawableSystemEnable) {
            return;
        }
        Log.i("setButtonDrawable", "id:" + mButton.getId() + ";mButtonDrawable:" + mButtonDrawable);
        if (mButtonDrawable == null
                && mButtonPressedDrawable == null
                && mButtonDisabledDrawable == null
                && mButtonSelectedDrawable == null
                && mButtonCheckedDrawable == null) {
            mButton.setButtonDrawable(null);
            return;
        }
        float radius = mButtonDrawableColorCircleEnable ?
                mButtonDrawableWidth + mButtonDrawableHeight / 2 : mButtonDrawableColorRadius;
        mStateButtonDrawable = new StateListDrawable();
        mStateButtonDrawable.addState(new int[]{mStateChecked}, getStateDrawable(mButtonCheckedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
        mStateButtonDrawable.addState(new int[]{mStateSelected}, getStateDrawable(mButtonSelectedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
        mStateButtonDrawable.addState(new int[]{mStatePressed}, getStateDrawable(mButtonPressedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
        mStateButtonDrawable.addState(new int[]{mStateDisabled}, getStateDrawable(mButtonDisabledDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
        mStateButtonDrawable.addState(new int[]{}, getStateDrawable(mButtonDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
        DrawableUtil.setDrawableWidthHeight(mStateButtonDrawable, mButtonDrawableWidth, mButtonDrawableHeight);
        mButton.setButtonDrawable(mStateButtonDrawable);
    }
}
