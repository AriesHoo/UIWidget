package com.aries.ui.view.radius.delegate;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;
import android.widget.TextView;

import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2018/2/6/006 9:47
 * E-Mail: AriesHoo@126.com
 * Function: 设置Switch thumb及track相关代理
 * Description:
 * 2018-6-1 09:40:27 1、增加默认属性值并调整设置Drawable逻辑
 */
public class RadiusSwitchDelegate extends RadiusCompoundDelegate<RadiusSwitchDelegate> {

    private Switch mSwitch;
    private StateListDrawable mStateThumbDrawable;
    private StateListDrawable mStateTrackDrawable;

    //以下为xml对应属性解析
    private int mThumbDrawableWidth;
    private int mThumbDrawableHeight;
    private Drawable mThumbDrawable;
    private Drawable mThumbPressedDrawable;
    private Drawable mThumbDisabledDrawable;
    private Drawable mThumbSelectedDrawable;
    private Drawable mThumbCheckedDrawable;

    private int mThumbStrokeColor;
    private int mThumbStrokePressedColor;
    private int mThumbStrokeDisabledColor;
    private int mThumbStrokeSelectedColor;
    private int mThumbStrokeCheckedColor;
    private int mThumbStrokeWidth;
    private float mThumbRadius;

    private int mTrackDrawableWidth;
    private int mTrackDrawableHeight;
    private Drawable mTrackDrawable;
    private Drawable mTrackPressedDrawable;
    private Drawable mTrackDisabledDrawable;
    private Drawable mTrackSelectedDrawable;
    private Drawable mTrackCheckedDrawable;

    private int mTrackStrokeColor;
    private int mTrackStrokePressedColor;
    private int mTrackStrokeDisabledColor;
    private int mTrackStrokeSelectedColor;
    private int mTrackStrokeCheckedColor;
    private int mTrackStrokeWidth;
    private float mTrackRadius;
    private int mColorAccent;
    private int mColorDefault;
    //以上为xml对应属性解析

    public RadiusSwitchDelegate(TextView view, Context context, AttributeSet attrs) {
        super(view, context, attrs);
    }


    @Override
    protected void initAttributes(Context context, AttributeSet attrs) {
        mColorAccent = mResourceUtil.getAttrColor(android.R.attr.colorAccent);
        mColorDefault = Color.LTGRAY;
        mThumbDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbDrawableWidth, dp2px(24));
        mThumbDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbDrawableHeight, dp2px(24));
        mThumbDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbDrawable);
        mThumbPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbPressedDrawable);
        mThumbDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbDisabledDrawable);
        mThumbSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbSelectedDrawable);
        mThumbCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_thumbCheckedDrawable);
        mThumbStrokeColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeColor, mColorDefault);
        mThumbStrokePressedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokePressedColor, mThumbStrokeColor);
        mThumbStrokeDisabledColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeDisabledColor, mThumbStrokeColor);
        mThumbStrokeSelectedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeSelectedColor, mThumbStrokeColor);
        mThumbStrokeCheckedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_thumbStrokeCheckedColor, mColorAccent);
        mThumbStrokeWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_thumbStrokeWidth, dp2px(2));
        mThumbRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_thumbRadius, 100f);

        //轨道属性
        mTrackDrawableWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackDrawableWidth, dp2px(48));
        mTrackDrawableHeight = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackDrawableHeight, dp2px(24));
        mTrackDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackDrawable);
        mTrackPressedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackPressedDrawable);
        mTrackDisabledDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackDisabledDrawable);
        mTrackSelectedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackSelectedDrawable);
        mTrackCheckedDrawable = mTypedArray.getDrawable(R.styleable.RadiusSwitch_rv_trackCheckedDrawable);
        mTrackStrokeColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeColor, mColorDefault);
        mTrackStrokePressedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokePressedColor, mTrackStrokeColor);
        mTrackStrokeDisabledColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeDisabledColor, mTrackStrokeColor);
        mTrackStrokeSelectedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeSelectedColor, mThumbStrokeColor);
        mTrackStrokeCheckedColor = mTypedArray.getColor(R.styleable.RadiusSwitch_rv_trackStrokeCheckedColor, mColorAccent);
        mTrackStrokeWidth = mTypedArray.getDimensionPixelSize(R.styleable.RadiusSwitch_rv_trackStrokeWidth, dp2px(2));
        mTrackRadius = mTypedArray.getDimension(R.styleable.RadiusSwitch_rv_trackRadius, 100f);

        mThumbDrawable = mThumbDrawable == null ? new ColorDrawable(Color.WHITE) : mThumbDrawable;
        mThumbPressedDrawable = mTrackPressedDrawable == null ? mThumbDrawable : mThumbPressedDrawable;
        mThumbDisabledDrawable = mThumbDisabledDrawable == null ? mThumbDrawable : mThumbDisabledDrawable;
        mThumbSelectedDrawable = mThumbSelectedDrawable == null ? mThumbDrawable : mThumbSelectedDrawable;
        mThumbCheckedDrawable = mThumbCheckedDrawable == null ? mThumbDrawable : mThumbCheckedDrawable;

        mTrackDrawable = mTrackDrawable == null ? new ColorDrawable(mColorDefault) : mTrackDrawable;
        mTrackPressedDrawable = mTrackPressedDrawable == null ? mTrackDrawable : mTrackPressedDrawable;
        mTrackDisabledDrawable = mTrackDisabledDrawable == null ? mTrackDrawable : mTrackDisabledDrawable;
        mTrackSelectedDrawable = mTrackSelectedDrawable == null ? mTrackDrawable : mTrackSelectedDrawable;
        mTrackCheckedDrawable = mTrackCheckedDrawable == null ? new ColorDrawable(mColorAccent) : mTrackCheckedDrawable;


        super.initAttributes(context, attrs);
    }

    @Override
    public void init() {
        setSwitchDrawable();
        super.init();
    }

    /**
     * 设置滑块宽度
     *
     * @param width
     * @return
     */
    public RadiusSwitchDelegate setThumbDrawableWidth(int width) {
        mThumbDrawableWidth = width;
        return this;
    }

    /**
     * 设置滑块高度
     *
     * @param height
     * @return
     */
    public RadiusSwitchDelegate setThumbDrawableHeight(int height) {
        mThumbDrawableHeight = height;
        return this;
    }

    /**
     * 设置滑块默认状态drawable
     *
     * @param drawable
     * @return
     */
    public RadiusSwitchDelegate setThumbDrawable(Drawable drawable) {
        mThumbDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setThumbDrawable(int resId) {
        return setThumbDrawable(getDrawable(resId));
    }

    /**
     * 设置滑块按下状态drawable
     *
     * @param drawable
     * @return
     */
    public RadiusSwitchDelegate setThumbPressedDrawable(Drawable drawable) {
        mThumbPressedDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setThumbPressedDrawable(int resId) {
        return setThumbPressedDrawable(getDrawable(resId));
    }

    public RadiusSwitchDelegate setThumbDisabledDrawable(Drawable drawable) {
        mThumbDisabledDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setThumbDisabledDrawable(int resId) {
        return setThumbDisabledDrawable(getDrawable(resId));
    }

    public RadiusSwitchDelegate setThumbSelectedDrawable(Drawable thumbSelectedDrawable) {
        mThumbSelectedDrawable = thumbSelectedDrawable;
        return this;
    }

    public RadiusSwitchDelegate setThumbSelectedDrawable(int resId) {
        return setThumbSelectedDrawable(getDrawable(resId));
    }

    /**
     * 设置滑块checked状态drawable
     *
     * @param drawable
     * @return
     */
    public RadiusSwitchDelegate setThumbCheckedDrawable(Drawable drawable) {
        mThumbCheckedDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setThumbCheckedDrawable(int resId) {
        return setThumbCheckedDrawable(getDrawable(resId));
    }

    /**
     * 设置滑块边框颜色
     *
     * @param color
     * @return
     */
    public RadiusSwitchDelegate setThumbStrokeColor(int color) {
        mThumbStrokeColor = color;
        return this;
    }

    public RadiusSwitchDelegate setThumbStrokePressedColor(int color) {
        mThumbStrokePressedColor = color;
        return this;
    }

    public RadiusSwitchDelegate setThumbStrokeDisabledColor(int color) {
        mThumbStrokeDisabledColor = color;
        return this;
    }

    public RadiusSwitchDelegate setThumbStrokeSelectedColor(int color) {
        mThumbStrokeSelectedColor = color;
        return this;
    }

    /**
     * 设置滑块边框选中状态颜色
     *
     * @param color
     * @return
     */
    public RadiusSwitchDelegate setThumbStrokeCheckedColor(int color) {
        mThumbStrokeCheckedColor = color;
        return this;
    }

    /**
     * 设置滑块边框宽度
     *
     * @param width
     * @return
     */
    public RadiusSwitchDelegate setThumbStrokeWidth(int width) {
        mThumbStrokeWidth = width;
        return this;
    }

    /**
     * 设置边框圆角弧度
     *
     * @param radius
     * @return
     */
    public RadiusSwitchDelegate setThumbRadius(float radius) {
        mThumbRadius = radius;
        return this;
    }

    /**
     * 设置轨道宽度
     *
     * @param width
     * @return
     */
    public RadiusSwitchDelegate setTrackDrawableWidth(int width) {
        mTrackDrawableWidth = width;
        return this;
    }

    /**
     * 设置轨道高度
     *
     * @param height
     * @return
     */
    public RadiusSwitchDelegate setTrackDrawableHeight(int height) {
        mTrackDrawableHeight = height;
        return this;
    }

    /**
     * 设置轨道默认drawable
     *
     * @param drawable
     * @return
     */
    public RadiusSwitchDelegate setTrackDrawable(Drawable drawable) {
        mTrackDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setTrackDrawable(int resId) {
        return setTrackDrawable(getDrawable(resId));
    }

    public RadiusSwitchDelegate setTrackPressedDrawable(Drawable drawable) {
        mTrackPressedDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setTrackPressedDrawable(int resId) {
        return setTrackPressedDrawable(getDrawable(resId));
    }

    public RadiusSwitchDelegate setTrackDisabledDrawable(Drawable drawable) {
        mTrackDisabledDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setTrackDisabledDrawable(int resId) {
        return setTrackDisabledDrawable(getDrawable(resId));
    }

    public RadiusSwitchDelegate setTrackSelectedDrawable(Drawable drawable) {
        mTrackSelectedDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setTrackSelectedDrawable(int resId) {
        return setTrackSelectedDrawable(getDrawable(resId));
    }

    /**
     * 设置轨道checked状态drawable
     *
     * @param drawable
     * @return
     */
    public RadiusSwitchDelegate setTrackCheckedDrawable(Drawable drawable) {
        mTrackCheckedDrawable = drawable;
        return this;
    }

    public RadiusSwitchDelegate setTrackCheckedDrawable(int resId) {
        return setTrackCheckedDrawable(getDrawable(resId));
    }

    /**
     * 设置轨道边框颜色
     *
     * @param color
     * @return
     */
    public RadiusSwitchDelegate setTrackStrokeColor(int color) {
        mTrackStrokeColor = color;
        return this;
    }

    public RadiusSwitchDelegate setTrackStrokePressedColor(int color) {
        mTrackStrokePressedColor = color;
        return this;
    }

    public RadiusSwitchDelegate setTrackStrokeDisabledColor(int color) {
        mTrackStrokeDisabledColor = color;
        return this;
    }

    public RadiusSwitchDelegate setTrackStrokeSelectedColor(int color) {
        mTrackStrokeSelectedColor = color;
        return this;
    }

    /**
     * 设置轨道边框checked状态颜色
     *
     * @param color
     * @return
     */
    public RadiusSwitchDelegate setTrackStrokeCheckedColor(int color) {
        mTrackStrokeCheckedColor = color;
        return this;
    }

    /**
     * 设置轨道边框宽度
     *
     * @param width
     * @return
     */
    public RadiusSwitchDelegate setTrackStrokeWidth(int width) {
        mTrackStrokeWidth = width;
        return this;
    }

    /**
     * 设置轨道圆角弧度
     *
     * @param radius
     * @return
     */
    public RadiusSwitchDelegate setTrackRadius(float radius) {
        mTrackRadius = radius;
        return this;
    }

    /**
     * 设置Drawable相关属性
     */
    private void setSwitchDrawable() {
        mSwitch = (Switch) mView;
        mStateThumbDrawable = new StateListDrawable();
        mStateTrackDrawable = new StateListDrawable();
        mStateThumbDrawable.addState(new int[]{mStateChecked}, getStateDrawable(mThumbCheckedDrawable, mThumbRadius, mThumbDrawableWidth, mThumbDrawableHeight, mThumbStrokeWidth, mThumbStrokeCheckedColor));
        mStateThumbDrawable.addState(new int[]{mStateSelected}, getStateDrawable(mThumbSelectedDrawable, mThumbRadius, mThumbDrawableWidth, mThumbDrawableHeight, mThumbStrokeWidth, mThumbStrokeSelectedColor));
        mStateThumbDrawable.addState(new int[]{mStatePressed}, getStateDrawable(mThumbPressedDrawable, mThumbRadius, mThumbDrawableWidth, mThumbDrawableHeight, mThumbStrokeWidth, mThumbStrokePressedColor));
        mStateThumbDrawable.addState(new int[]{mStateDisabled}, getStateDrawable(mThumbDisabledDrawable, mThumbRadius, mThumbDrawableWidth, mThumbDrawableHeight, mThumbStrokeWidth, mThumbStrokeDisabledColor));
        mStateThumbDrawable.addState(new int[]{}, getStateDrawable(mThumbDrawable, mThumbRadius, mThumbDrawableWidth, mThumbDrawableHeight, mThumbStrokeWidth, mThumbStrokeColor));

        mTrackDrawableHeight = 0;
        mStateTrackDrawable.addState(new int[]{mStateChecked}, getStateDrawable(mTrackCheckedDrawable, mTrackRadius, mTrackDrawableWidth, mTrackDrawableHeight, mTrackStrokeWidth, mTrackStrokeCheckedColor));
        mStateTrackDrawable.addState(new int[]{mStateSelected}, getStateDrawable(mTrackSelectedDrawable, mTrackRadius, mTrackDrawableWidth, mTrackDrawableHeight, mTrackStrokeWidth, mTrackStrokeSelectedColor));
        mStateTrackDrawable.addState(new int[]{mStatePressed}, getStateDrawable(mTrackPressedDrawable, mTrackRadius, mTrackDrawableWidth, mTrackDrawableHeight, mTrackStrokeWidth, mTrackStrokePressedColor));
        mStateTrackDrawable.addState(new int[]{mStateDisabled}, getStateDrawable(mTrackDisabledDrawable, mTrackRadius, mTrackDrawableWidth, mTrackDrawableHeight, mTrackStrokeWidth, mTrackStrokeDisabledColor));
        mStateTrackDrawable.addState(new int[]{}, getStateDrawable(mTrackDrawable, mTrackRadius, mTrackDrawableWidth, mTrackDrawableHeight, mTrackStrokeWidth, mTrackStrokeColor));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mSwitch.setThumbDrawable(mStateThumbDrawable);
            mSwitch.setTrackDrawable(mStateTrackDrawable);
        }
    }


    protected Drawable getStateDrawable(Drawable drawable, float radius, int width, int height, int strokeWidth, int strokeColor) {
        drawable = getStateDrawable(drawable, radius, width, height);
        if (drawable instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) drawable;
            gradientDrawable.setStroke(strokeWidth, strokeColor);
        }
        return drawable;
    }
}
