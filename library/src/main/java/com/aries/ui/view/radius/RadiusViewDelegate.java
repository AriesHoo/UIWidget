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
 * Function: 公共属性解析类
 * Desc:
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
        if (!(view instanceof CompoundButton) && !view.isClickable())
            view.setClickable(isRippleEnable);
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
        strokeColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeColor, Color.TRANSPARENT);
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

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        setBgSelector();
    }

    public void setBackgroundPressedColor(int backgroundPressedColor) {
        this.backgroundPressedColor = backgroundPressedColor;
        setBgSelector();
    }

    public void setBackgroundEnabledColor(int backgroundEnabledColor) {
        this.backgroundEnabledColor = backgroundEnabledColor;
        setBgSelector();
    }

    public void setBackgroundSelectedColor(int backgroundSelectedColor) {
        this.backgroundSelectedColor = backgroundSelectedColor;
        setBgSelector();
    }

    public void setBackgroundCheckedColor(int backgroundCheckedColor) {
        this.backgroundCheckedColor = backgroundCheckedColor;
        setBgSelector();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        setBgSelector();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        setBgSelector();
    }

    public void setStrokeColor(int strokeColor) {
        this.strokeColor = strokeColor;
        setBgSelector();
    }

    public void setStrokePressedColor(int strokePressedColor) {
        this.strokePressedColor = strokePressedColor;
        setBgSelector();
    }

    public void setStrokeEnabledColor(int strokeEnabledColor) {
        this.strokeEnabledColor = strokeEnabledColor;
        setBgSelector();
    }

    public void setStrokeSelectedColor(int strokeSelectedColor) {
        this.strokeSelectedColor = strokeSelectedColor;
        setBgSelector();
    }

    public void setStrokeCheckedColor(int strokeCheckedColor) {
        this.strokeCheckedColor = strokeCheckedColor;
        setBgSelector();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        setBgSelector();
    }

    public void setTextPressedColor(int textPressedColor) {
        this.textPressedColor = textPressedColor;
        setBgSelector();
    }

    public void setTextEnabledColor(int textEnabledColor) {
        this.textEnabledColor = textEnabledColor;
        setBgSelector();
    }

    public void seTextSelectedColor(int textSelectedColor) {
        this.textSelectedColor = textSelectedColor;
        setBgSelector();
    }

    public void setTextCheckedColor(int textSelectedColor) {
        this.textCheckedColor = textCheckedColor;
        setBgSelector();
    }

    public void setEadiusHalfHeightEnable(boolean isRadiusHalfHeight) {
        this.isRadiusHalfHeight = isRadiusHalfHeight;
        setBgSelector();
    }

    public void setWidthHeightEqualEnable(boolean isWidthHeightEqual) {
        this.isWidthHeightEqual = isWidthHeightEqual;
        setBgSelector();
    }

    public void setTopLeftRadius(int topLeftRadius) {
        this.topLeftRadius = topLeftRadius;
        setBgSelector();
    }

    public void setTopRightRadius(int topRightRadius) {
        this.topRightRadius = topRightRadius;
        setBgSelector();
    }

    public void setBottomLeftRadius(int bottomLeftRadius) {
        this.bottomLeftRadius = bottomLeftRadius;
        setBgSelector();
    }

    public void setBottomRightRadius(int bottomRightRadius) {
        this.bottomRightRadius = bottomRightRadius;
        setBgSelector();
    }

    public void setRippleColor(int rippleColor) {
        this.rippleColor = rippleColor;
        setBgSelector();
    }

    public void setRippleEnable(boolean rippleEnable) {
        this.isRippleEnable = rippleEnable;
        setBgSelector();
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
    public void setBgSelector() {
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
                return;
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
     *  水波纹效果完成后最终展示的背景Drawable
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
