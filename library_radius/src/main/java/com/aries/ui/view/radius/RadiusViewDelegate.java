package com.aries.ui.view.radius;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created: AriesHoo on 2017-02-10 14:25
 * Function: 公共属性解析类
 * Desc:
 */
public class RadiusViewDelegate {
    private View view;
    private Context context;
    private GradientDrawable gdBackground = new GradientDrawable();
    private GradientDrawable gdBackgroundPressed = new GradientDrawable();
    private GradientDrawable gdBackgroundEnabled = new GradientDrawable();
    private int backgroundColor;
    private int backgroundPressedColor;
    private int backgroundEnabledColor;
    private int radius;
    private int topLeftRadius;
    private int topRightRadius;
    private int bottomLeftRadius;
    private int bottomRightRadius;
    private int strokeWidth;
    private int strokeColor;
    private int strokePressedColor;
    private int strokeEnabledColor;
    private int textColor;
    private int textPressedColor;
    private int textEnabledColor;
    private boolean isRadiusHalfHeight;
    private boolean isWidthHeightEqual;
    private boolean isRippleEnable;
    private float[] radiusArr = new float[8];

    public RadiusViewDelegate(View view, Context context, AttributeSet attrs) {
        this.view = view;
        this.context = context;
        obtainAttributes(context, attrs);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RadiusTextView);
        backgroundColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundColor, Color.TRANSPARENT);
        backgroundPressedColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundPressedColor, Integer.MAX_VALUE);
        backgroundEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_backgroundEnabledColor, Integer.MAX_VALUE);
        radius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_radius, 0);
        strokeWidth = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_strokeWidth, 0);
        strokeColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeColor, Color.TRANSPARENT);
        strokePressedColor = ta.getColor(R.styleable.RadiusTextView_rv_strokePressedColor, Integer.MAX_VALUE);
        strokeEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_strokeEnabledColor, Integer.MAX_VALUE);
        textColor = ta.getColor(R.styleable.RadiusTextView_rv_textColor, Integer.MAX_VALUE);
        textPressedColor = ta.getColor(R.styleable.RadiusTextView_rv_textPressedColor, Integer.MAX_VALUE);
        textEnabledColor = ta.getColor(R.styleable.RadiusTextView_rv_textEnabledColor, Integer.MAX_VALUE);
        isRadiusHalfHeight = ta.getBoolean(R.styleable.RadiusTextView_rv_radiusHalfHeightEnable, false);
        isWidthHeightEqual = ta.getBoolean(R.styleable.RadiusTextView_rv_widthHeightEqualEnable, false);
        topLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topLeftRadius, 0);
        topRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_topRightRadius, 0);
        bottomLeftRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomLeftRadius, 0);
        bottomRightRadius = ta.getDimensionPixelSize(R.styleable.RadiusTextView_rv_bottomRightRadius, 0);
        isRippleEnable = ta.getBoolean(R.styleable.RadiusTextView_rv_rippleEnable, true);
        ta.recycle();
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

    public void setRadius(int radius) {
        this.radius = dp2px(radius);
        setBgSelector();
    }

    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = dp2px(strokeWidth);
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getbackgroundPressedColor() {
        return backgroundPressedColor;
    }

    public int getRadius() {
        return radius;
    }

    public int getStrokeWidth() {
        return strokeWidth;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getStrokePressedColor() {
        return strokePressedColor;
    }

    public int getTextPressedColor() {
        return textPressedColor;
    }

    public boolean getRadiusHalfHeightEnable() {
        return isRadiusHalfHeight;
    }

    public boolean getWidthHeightEqualEnable() {
        return isWidthHeightEqual;
    }

    public int gettopLeftRadius() {
        return topLeftRadius;
    }

    public int gettopRightRadius() {
        return topRightRadius;
    }

    public int getbottomLeftRadius() {
        return bottomLeftRadius;
    }

    public int getbottomRightRadius() {
        return bottomRightRadius;
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }

    private void setDrawable(GradientDrawable gd, int color, int strokeColor) {
        gd.setColor(color);

        if (topLeftRadius > 0 || topRightRadius > 0 || bottomRightRadius > 0 || bottomLeftRadius > 0) {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
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

    public void setBgSelector() {
        StateListDrawable bg = new StateListDrawable();
        setDrawable(gdBackground, backgroundColor, strokeColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isRippleEnable && view.isEnabled()) {
            RippleDrawable rippleDrawable = new RippleDrawable(
                    getColorSelector(backgroundColor,
                            backgroundPressedColor,
                            backgroundEnabledColor == Integer.MAX_VALUE ? backgroundColor : backgroundEnabledColor), gdBackground, null);
            view.setBackground(rippleDrawable);
        } else {
            if (view.isEnabled()) {
                bg.addState(new int[]{-android.R.attr.state_pressed, -android.R.attr.state_selected}, gdBackground);
            }
            if (backgroundPressedColor != Integer.MAX_VALUE || strokePressedColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundPressed,
                        backgroundPressedColor == Integer.MAX_VALUE ? backgroundColor : backgroundPressedColor,
                        strokePressedColor == Integer.MAX_VALUE ? strokeColor : strokePressedColor);
                bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_pressed}, gdBackgroundPressed);
            }
            if (backgroundEnabledColor != Integer.MAX_VALUE || strokeEnabledColor != Integer.MAX_VALUE) {
                setDrawable(gdBackgroundEnabled,
                        backgroundEnabledColor == Integer.MAX_VALUE ? backgroundColor : backgroundEnabledColor,
                        strokeEnabledColor == Integer.MAX_VALUE ? strokeColor : strokeEnabledColor);
                bg.addState(new int[]{-android.R.attr.state_enabled}, gdBackgroundEnabled);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {//16
                view.setBackground(bg);
            } else {
                view.setBackgroundDrawable(bg);
            }
        }

        if (view instanceof TextView || view instanceof EditText) {
            TextView textView = (TextView) view;
            if (textPressedColor != Integer.MAX_VALUE) {
                textColor = (textColor == Integer.MAX_VALUE ? textView.getTextColors().getDefaultColor() : textColor);
                if (textColor != Integer.MAX_VALUE || textPressedColor != Integer.MAX_VALUE || textEnabledColor != Integer.MAX_VALUE) {
                    ColorStateList colorStateList =
                            getColorSelector(textColor,
                                    textPressedColor == Integer.MAX_VALUE ? textColor : textPressedColor,
                                    textEnabledColor == Integer.MAX_VALUE ? textColor : textEnabledColor);
                    textView.setTextColor(colorStateList);
                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private ColorStateList getColorSelector(int normalColor, int pressedColor, int enabledColor) {
        return new ColorStateList(
                new int[][]{
                        new int[]{android.R.attr.state_focused},
                        new int[]{android.R.attr.state_activated},
                        new int[]{android.R.attr.state_pressed},
                        new int[]{-android.R.attr.state_enabled},
                        new int[]{}
                },
                new int[]{
                        pressedColor,
                        pressedColor,
                        pressedColor,
                        enabledColor,
                        normalColor
                }
        );
    }
}
