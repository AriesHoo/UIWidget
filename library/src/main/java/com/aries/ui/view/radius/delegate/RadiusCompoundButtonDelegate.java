package com.aries.ui.view.radius.delegate;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2018/2/6/006 9:47
 * E-Mail: AriesHoo@126.com
 * Function: 设置CompoundButton ButtonDrawable相关代理
 * Description:
 */
public class RadiusCompoundButtonDelegate extends RadiusTextViewDelegate {

    private CompoundButton mButton;

    private float mButtonDrawableColorRadius;
    private boolean mButtonDrawableColorCircleEnable;
    private int mButtonDrawableWidth;
    private int mButtonDrawableHeight;
    private Drawable mButtonDrawable;
    private Drawable mButtonPressedDrawable;
    private Drawable mButtonDisabledDrawable;
    private Drawable mButtonSelectedDrawable;
    private Drawable mButtonCheckedDrawable;

    public RadiusCompoundButtonDelegate(TextView view, Context context, AttributeSet attrs) {
        super(view, context, attrs);
    }


    @Override
    protected void initAttributes(Context context, AttributeSet attrs) {
        mButtonDrawableColorRadius = mTypedArray.getDimension(R.styleable.RadiusCheckBox_rv_buttonDrawableColorRadius, 0);
        mButtonDrawableColorCircleEnable = mTypedArray.getBoolean(R.styleable.RadiusCheckBox_rv_buttonDrawableColorCircleEnable, false);
        mButtonDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusCheckBox_rv_buttonDrawableWidth, -1);
        mButtonDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusCheckBox_rv_buttonDrawableHeight, -1);
        mButtonDrawable = mTypedArray.getDrawable(R.styleable.RadiusCheckBox_rv_buttonDrawable);
        mButtonPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusCheckBox_rv_buttonPressedDrawable);
        mButtonDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusCheckBox_rv_buttonDisabledDrawable);
        mButtonSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusCheckBox_rv_buttonSelectedDrawable);
        mButtonCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusCheckBox_rv_buttonCheckedDrawable);
        super.initAttributes(context, attrs);
    }

    @Override
    public void init() {
        setButtonDrawable();
        super.init();
    }

    /**
     * 设置drawable宽度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableWidth
     * @return
     */
    public RadiusCompoundButtonDelegate setButtonDrawableWidth(int drawableWidth) {
        mButtonDrawableWidth = drawableWidth;
        return this;
    }

    /**
     * 设置drawable高度--ColorDrawable有效其它不知为啥失效
     *
     * @param drawableHeight
     * @return
     */
    public RadiusCompoundButtonDelegate setButtonDrawableHeight(int drawableHeight) {
        mButtonDrawableHeight = drawableHeight;
        return this;
    }

    /**
     * 设置默认状态Drawable
     *
     * @param drawable
     */
    public RadiusCompoundButtonDelegate setButtonDrawable(Drawable drawable) {
        mButtonDrawable = drawable;
        return this;
    }

    public RadiusCompoundButtonDelegate setButtonDrawable(int resId) {
        return setButtonDrawable(getDrawable(resId));
    }

    /**
     * 设置按下效果Drawable
     *
     * @param drawable
     */
    public RadiusCompoundButtonDelegate setButtonPressedDrawable(Drawable drawable) {
        mButtonPressedDrawable = drawable;
        return this;
    }

    public RadiusCompoundButtonDelegate setButtonPressedDrawable(int resId) {
        return setButtonPressedDrawable(getDrawable(resId));
    }

    /**
     * 设置不可操作效果Drawable
     *
     * @param drawable
     */
    public RadiusCompoundButtonDelegate setButtonDisabledDrawable(Drawable drawable) {
        mButtonDisabledDrawable = drawable;
        return this;
    }

    public RadiusCompoundButtonDelegate setButtonDisabledDrawable(int resId) {
        return setButtonDisabledDrawable(getDrawable(resId));
    }

    /**
     * 设置选中效果Drawable
     *
     * @param drawable
     * @return
     */
    public RadiusCompoundButtonDelegate setButtonSelectedDrawable(Drawable drawable) {
        mButtonSelectedDrawable = drawable;
        return this;
    }

    public RadiusCompoundButtonDelegate setButtonSelectedDrawable(int resId) {
        return setButtonSelectedDrawable(getDrawable(resId));
    }

    /**
     * 设置Checked状态Drawable
     *
     * @param drawable
     * @return
     */
    public RadiusCompoundButtonDelegate setButtonCheckedDrawable(Drawable drawable) {
        mButtonCheckedDrawable = drawable;
        return this;
    }

    public RadiusCompoundButtonDelegate setButtonCheckedDrawable(int resId) {
        return setButtonCheckedDrawable(getDrawable(resId));
    }

    /**
     * 设置CompoundButton的setButtonDrawable属性
     */
    private void setButtonDrawable() {
        mButton = (CompoundButton) mView;
        if (mButtonDrawable != null || mButtonPressedDrawable != null
                || mButtonDisabledDrawable != null || mButtonSelectedDrawable != null
                || mButtonCheckedDrawable != null) {
            float radius = mButtonDrawableColorCircleEnable ?
                    mButtonDrawableWidth + mButtonDrawableHeight / 2 : mButtonDrawableColorRadius;
            StateListDrawable stateDrawable = new StateListDrawable();
            stateDrawable.addState(new int[]{mStateChecked}, getStateDrawable(mButtonCheckedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
            stateDrawable.addState(new int[]{mStateSelected}, getStateDrawable(mButtonSelectedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
            stateDrawable.addState(new int[]{mStatePressed}, getStateDrawable(mButtonPressedDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
            stateDrawable.addState(new int[]{mStateDisabled}, getStateDrawable(mButtonDisabledDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
            stateDrawable.addState(new int[]{}, getStateDrawable(mButtonDrawable, radius, mButtonDrawableWidth, mButtonDrawableHeight));
            DrawableUtil.setDrawableWidthHeight(stateDrawable, mButtonDrawableWidth, mButtonDrawableHeight);
            mButton.setButtonDrawable(stateDrawable);
            Log.i("setButtonDrawable","mButtonDrawable:"+mButtonDrawable.getBounds()+";height:"+mButtonDrawable.getIntrinsicHeight()+";stateDrawable:"+stateDrawable.getBounds()+";height:"+stateDrawable.getIntrinsicHeight());
        }
    }
}
