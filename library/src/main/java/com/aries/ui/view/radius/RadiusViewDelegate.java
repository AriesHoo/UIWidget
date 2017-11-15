package com.aries.ui.view.radius;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2017-02-10 14:25
 * E-Mail: AriesHoo@126.com
 * Function:  公共属性解析代理类
 * Description: 1、2017-11-15 09:36:14添加Java属性设置链式调用支持
 */
public class RadiusViewDelegate {
    private View view;
    private Context mContext;
    private GradientDrawable gdBackground = new GradientDrawable();
    private GradientDrawable gdBackgroundPressed = new GradientDrawable();
    private GradientDrawable gdBackgroundEnabled = new GradientDrawable();
    private GradientDrawable gdBackgroundSelected = new GradientDrawable();
    private GradientDrawable gdBackgroundChecked = new GradientDrawable();
    private int backgroundColor;
    private int backgroundPressedColor;
    private int backgroundEnabledColor;
    private int backgroundSelectedColor;
    private int backgroundCheckedColor;
    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomLeftRadius;
    private int bottomRightRadius;
    private int strokeWidth;
    private int strokeColor;
    private int strokePressedColor;
    private int strokeEnabledColor;
    private int strokeSelectedColor;
    private int strokeCheckedColor;
    private int textColor;
    private int textPressedColor;
    private int textEnabledColor;
    private int textSelectedColor;
    private int textCheckedColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;
    private int rippleColor;
    private boolean isRippleEnable;
    private boolean selected;
    private float[] radiusArr = new float[8];

    public RadiusViewDelegate(View view, Context context, AttributeSet attrs) {
        this.view = view;
        this.mContext = context;
        obtainAttributes(context, attrs);
        view.setSelected(selected);
        if (!(view instanceof CompoundButton) && !view.isClickable()) {
            view.setClickable(isRippleEnable);
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadiusTextView);
        backgroundColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundColor, Integer.MAX_VALUE);
        backgroundPressedColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundPressedColor, Integer.MAX_VALUE);
        backgroundEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundEnabledColor, Integer.MAX_VALUE);
        backgroundSelectedColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundSelectedColor, Integer.MAX_VALUE);
        backgroundCheckedColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundCheckedColor, Integer.MAX_VALUE);
        radius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_radius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeColor, Color.GRAY);
        strokePressedColor = ta.getColor(R.styleable.RadiusTextView_rv_strokePressedColor, Integer.MAX_VALUE);
        strokeEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeEnabledColor, Integer.MAX_VALUE);
        strokeSelectedColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeSelectedColor, Integer.MAX_VALUE);
        strokeCheckedColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeCheckedColor, Integer.MAX_VALUE);
        textColor = ta.getColor(R.styleable.RadiusTextView_rv_textColor, Integer.MAX_VALUE);
        textPressedColor = ta.getColor(R.styleable.RadiusTextView_rv_textPressedColor, Integer.MAX_VALUE);
        textEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_textEnabledColor, Integer.MAX_VALUE);
        textSelectedColor = ta.getColor(R.styleable.RadiusTextView_rv_textSelectedColor, Integer.MAX_VALUE);
        textCheckedColor = ta.getColor(R.styleable.RadiusTextView_rv_textCheckedColor, Integer.MAX_VALUE);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.RadiusTextView_rv_radiusHalfHeightEnable, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.RadiusTextView_rv_widthHeightEqualEnable, false);
        topLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topLeftRadius, 0);
        topRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topRightRadius, 0);
        bottomLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomLeftRadius, 0);
        bottomRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomRightRadius, 0);
        rippleColor = ta.getColor(R.styleable.RadiusTextView_rv_rippleColor, mContext.getResources().getColor(R.color.colorRadiusDefaultRipple));
        isRippleEnable = ta.getBoolean(R.styleable.RadiusTextView_rv_rippleEnable, getDefaultRippleEnable());
        selected = ta.getBoolean(R.styleable.RadiusTextView_rv_selected, false);
        ta.recycle();
    }

    /**
     * 是否默认开启水波纹
     *
     * @return
     */
    private boolean getDefaultRippleEnable() {
        boolean enable = !(view instanceof CompoundButton) && !(view instanceof EditText);
        return enable;
    }

    /**
     * 设置常态背景色
     *
     * @param backgroundColor
     * @return
     */
    public RadiusViewDelegate setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return setBgSelector();
    }

    /**
     * 设置按下状态背景色
     *
     * @param backgroundPressedColor
     * @return
     */
    public RadiusViewDelegate setBackgroundPressedColor(int backgroundPressedColor) {
        this.backgroundPressedColor = backgroundPressedColor;
        return setBgSelector();
    }

    /**
     * 设置不可操作状态下背景色
     *
     * @param backgroundEnabledColor
     * @return
     */
    public RadiusViewDelegate setBackgroundEnabledColor(int backgroundEnabledColor) {
        this.backgroundEnabledColor = backgroundEnabledColor;
        return setBgSelector();
    }

    /**
     * 设置selected状态下背景色
     *
     * @param backgroundSelectedColor
     * @return
     */
    public RadiusViewDelegate setBackgroundSelectedColor(int backgroundSelectedColor) {
        this.backgroundSelectedColor = backgroundSelectedColor;
        return setBgSelector();
    }

    /**
     * 设置checked状态背景色
     *
     * @param backgroundCheckedColor
     * @return
     */
    public RadiusViewDelegate setBackgroundCheckedColor(int backgroundCheckedColor) {
        this.backgroundCheckedColor = backgroundCheckedColor;
        return setBgSelector();
    }

    /**
     * 设置整体圆角弧度
     *
     * @param radius
     * @return
     */
    public RadiusViewDelegate setRadius(int radius) {
        this.radius = radius;
        return setBgSelector();
    }

    /**
     * 设置边框线宽度(粗细)
     *
     * @param strokeWidth
     * @return
     */
    public RadiusViewDelegate setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return setBgSelector();
    }

    /**
     * 设置边框线常态颜色
     *
     * @param strokeColor
     * @return
     */
    public RadiusViewDelegate setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        return setBgSelector();
    }

    /**
     * 设置边框线按下状态颜色
     *
     * @param strokePressedColor
     * @return
     */
    public RadiusViewDelegate setStrokePressedColor(int strokePressedColor) {
        this.strokePressedColor = strokePressedColor;
        return setBgSelector();
    }

    /**
     * 设置边框线不可点击状态下颜色
     *
     * @param strokeEnabledColor
     * @return
     */
    public RadiusViewDelegate setStrokeEnabledColor(int strokeEnabledColor) {
        this.strokeEnabledColor = strokeEnabledColor;
        return setBgSelector();
    }

    /**
     * 设置边框线selected状态颜色
     *
     * @param strokeSelectedColor
     * @return
     */
    public RadiusViewDelegate setStrokeSelectedColor(int strokeSelectedColor) {
        this.strokeSelectedColor = strokeSelectedColor;
        return setBgSelector();
    }

    /**
     * 设置边框checked状态颜色
     *
     * @param strokeCheckedColor
     * @return
     */
    public RadiusViewDelegate setStrokeCheckedColor(int strokeCheckedColor) {
        this.strokeCheckedColor = strokeCheckedColor;
        return setBgSelector();
    }

    /**
     * 设置文本常态颜色
     *
     * @param textColor
     * @return
     */
    public RadiusViewDelegate setTextColor(int textColor) {
        this.textColor = textColor;
        return setBgSelector();
    }

    /**
     * 设置文本按下颜色
     *
     * @param textPressedColor
     * @return
     */
    public RadiusViewDelegate setTextPressedColor(int textPressedColor) {
        this.textPressedColor = textPressedColor;
        return setBgSelector();
    }

    /**
     * 设置文本不可点击状态颜色--setEnable(false)时的颜色{@link View#setEnabled(boolean)}
     *
     * @param textEnabledColor
     * @return
     */
    public RadiusViewDelegate setTextEnabledColor(int textEnabledColor) {
        this.textEnabledColor = textEnabledColor;
        return setBgSelector();
    }

    /**
     * 设置文本selected颜色
     *
     * @param textSelectedColor
     * @return
     */
    public RadiusViewDelegate setTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
        return setBgSelector();
    }

    /**
     * 设置文本checked颜色
     *
     * @param textCheckedColor
     * @return
     */
    public RadiusViewDelegate setTextCheckedColor(int textCheckedColor) {
        this.textCheckedColor = textCheckedColor;
        return setBgSelector();
    }

    /**
     * 设置半高度圆角
     *
     * @param isRadiusHalfHeight
     * @return
     */
    public RadiusViewDelegate setEadiusHalfHeightEnable(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        return setBgSelector();
    }

    /**
     * 设置宽高相等
     *
     * @param isWidthHeightEqual
     * @return
     */
    public RadiusViewDelegate setWidthHeightEqualEnable(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        return setBgSelector();
    }

    /**
     * 设置左上圆角
     *
     * @param topLeftRadius
     * @return
     */
    public RadiusViewDelegate setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        return setBgSelector();
    }

    /**
     * 设置右上圆角
     *
     * @param topRightRadius
     * @return
     */
    public RadiusViewDelegate setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        return setBgSelector();
    }

    /**
     * 设置左下圆角
     *
     * @param bottomLeftRadius
     * @return
     */
    public RadiusViewDelegate setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        return setBgSelector();
    }

    /**
     * 设置右下圆角
     *
     * @param bottomRightRadius
     * @return
     */
    public RadiusViewDelegate setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        return setBgSelector();
    }

    /**
     * 设置水波纹颜色
     *
     * @param rippleColor
     * @return
     */
    public RadiusViewDelegate setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        return setBgSelector();
    }

    /**
     * 设置是否支持水波纹效果--5.0及以上
     *
     * @param rippleEnable
     * @return
     */
    public RadiusViewDelegate setRippleEnable(boolean rippleEnable) {
        this.isRippleEnable = rippleEnable;
        return setBgSelector();
    }

    public int getRadius() {
        return radius;
    }

    public boolean getRadiusHalfHeightEnable() {
        return isRadiusHalfHeight;
    }

    public boolean getWidthHeightEqualEnable() {
        return isWidthHeightEqual;
    }

    /**
     * 设置 背景Drawable颜色线框色及圆角值
     *
     * @param gd
     * @param color
     * @param strokeColor
     */
    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);
        //任意值大于0执行
        if (topLeftRadius > 0 || topRightRadius > 0 || bottomRightRadius > 0 || bottomLeftRadius > 0) {
            radiusArr[0] = topLeftRadius;
            radiusArr[1] = topLeftRadius;
            radiusArr[2] = topRightRadius;
            radiusArr[3] = topRightRadius;
            radiusArr[4] = bottomRightRadius;
            radiusArr[5] = bottomRightRadius;
            radiusArr[6] = bottomLeftRadius;
            radiusArr[7] = bottomLeftRadius;
            gd.setCornerRadii(radiusArr);
        } else {
            gd.setCornerRadius(radius);
        }
        gd.setStroke(strokeWidth, strokeColor);
    }


    /**
     * 设置shape属性
     */
    public RadiusViewDelegate setBgSelector() {
        setTextSelector();
        //获取view当前drawable--用于判断是否通过默认属性设置背景
        Drawable mDrawable = view.getBackground();
        //判断是否使用自定义颜色值
        boolean isSetBg = backgroundColor != Integer.MAX_VALUE
                || backgroundPressedColor != Integer.MAX_VALUE
                || backgroundEnabledColor != Integer.MAX_VALUE
                || backgroundSelectedColor != Integer.MAX_VALUE
                || backgroundCheckedColor != Integer.MAX_VALUE;
        StateListDrawable bg = new StateListDrawable();
        setDrawable(gdBackgroundChecked, backgroundCheckedColor, strokeCheckedColor);
        setDrawable(gdBackgroundSelected, backgroundPressedColor, strokeSelectedColor);
        setDrawable(gdBackground, backgroundColor == Integer.MAX_VALUE ? Color.TRANSPARENT : backgroundColor, strokeColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && isRippleEnable && view.isEnabled() && !view.isSelected()) {//5.0以上且设置水波属性并且可操作
            RippleDrawable rippleDrawable = new RippleDrawable(
                    new ColorStateList(
                            new int[][]{
                                    new int[]{android.R.attr.state_pressed},
                                    new int[]{}
                            },
                            new int[]{
                                    backgroundPressedColor != Integer.MAX_VALUE ? backgroundPressedColor : rippleColor,
                                    rippleColor
                            }
                    )
                    , getContentDrawable(mDrawable, isSetBg)
                    , null);
            view.setBackground(rippleDrawable);
        } else {
            if (!isSetBg) {//避免默认background设置无效
                return this;
            }
            if (backgroundPressedColor != Integer.MAX_VALUE || strokePressedColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundPressed,
                        backgroundPressedColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressedColor,
                        strokePressedColor == Integer.MAX_VALUE ? strokeColor : strokePressedColor);
                bg.addState(new int[]{android.R.attr.state_pressed}, gdBackgroundPressed);
            }
            if (backgroundSelectedColor != Integer.MAX_VALUE || strokeSelectedColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundSelected,
                        backgroundSelectedColor == Integer.MAX_VALUE ? backgroundColor : backgroundSelectedColor,
                        strokeSelectedColor == Integer.MAX_VALUE ? strokeColor : strokeSelectedColor);
                bg.addState(new int[]{android.R.attr.state_selected}, gdBackgroundSelected);
            }
            if (backgroundCheckedColor != Integer.MAX_VALUE || strokeCheckedColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundChecked,
                        backgroundCheckedColor == Integer.MAX_VALUE ? backgroundColor : backgroundCheckedColor,
                        strokeCheckedColor == Integer.MAX_VALUE ? strokeColor : strokeCheckedColor);
                bg.addState(new int[]{android.R.attr.state_checked}, gdBackgroundChecked);
            }
            if (backgroundEnabledColor != Integer.MAX_VALUE || strokeEnabledColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundEnabled,
                        backgroundEnabledColor == Integer.MAX_VALUE ? backgroundColor : backgroundEnabledColor,
                        strokeEnabledColor == Integer.MAX_VALUE ? strokeColor : strokeEnabledColor);
                bg.addState(new int[]{-android.R.attr.state_enabled}, gdBackgroundEnabled);
            }
            bg.addState(new int[]{}, gdBackground);//默认状态--放置在最后否则其它状态不生效
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }
        }
        return this;
    }

    /**
     * 设置文本颜色
     */
    private void setTextSelector() {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            textColor = (textColor == Integer.MAX_VALUE ? textView.getTextColors().getDefaultColor() : textColor);
            if (textColor != Integer.MAX_VALUE
                    || textPressedColor != Integer.MAX_VALUE
                    || textEnabledColor != Integer.MAX_VALUE
                    || textSelectedColor != Integer.MAX_VALUE
                    || textCheckedColor != Integer.MAX_VALUE) {
                ColorStateList colorStateList =
                        getColorSelector(textColor,
                                textPressedColor != Integer.MAX_VALUE ? textPressedColor : textColor,
                                textEnabledColor != Integer.MAX_VALUE ? textEnabledColor : textColor,
                                textSelectedColor != Integer.MAX_VALUE ? textSelectedColor : textColor,
                                textCheckedColor != Integer.MAX_VALUE ? textCheckedColor : textColor);
                textView.setTextColor(colorStateList);
            }
        }
    }

    /**
     * 水波纹效果完成后最终展示的背景Drawable
     *
     * @param mDrawable
     * @param isSetBg
     * @return
     */
    private Drawable getContentDrawable(Drawable mDrawable, boolean isSetBg) {
        if (view instanceof CompoundButton) {
            return !isSetBg ? mDrawable :
                    ((CompoundButton) view).isChecked() ? gdBackgroundChecked :
                            view.isSelected() ? gdBackgroundSelected :
                                    gdBackground;
        }
        return !isSetBg ? mDrawable : view.isSelected() ? gdBackgroundSelected : gdBackground;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ColorStateList getColorSelector(int normalColor, int pressedColor, int enabledColor, int selectedColor, int checkedColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_checked},
                        new int[]{android.R.attr.state_selected},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{}
                },
                new int[]{
                        checkedColor,
                        selectedColor,
                        pressedColor,
                        enabledColor,
                        normalColor
                }
        );
    }
}
