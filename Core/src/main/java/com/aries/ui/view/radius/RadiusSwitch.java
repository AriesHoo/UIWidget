package com.aries.ui.view.radius;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

import com.aries.ui.view.radius.delegate.RadiusSwitchDelegate;

/**
 * Created: AriesHoo on 2018/2/11/011 14:05
 * E-Mail: AriesHoo@126.com
 * Function: switch自定义--api 16以上
 * Description:
 */
@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class RadiusSwitch extends Switch {


    private RadiusSwitchDelegate delegate;

    public RadiusSwitch(Context context) {
        this(context, null);
    }

    public RadiusSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RadiusSwitchDelegate(this, context, attrs);
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate != null && delegate.getWidthHeightEqualEnable() && getWidth() > 0 && getHeight() > 0) {
            int max = Math.max(getWidth(), getHeight());
            int measureSpec = MeasureSpec.makeMeasureSpec(max, MeasureSpec.EXACTLY);
            super.onMeasure(measureSpec, measureSpec);
            return;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (delegate != null) {
            if (delegate.getRadiusHalfHeightEnable()) {
                delegate.setRadius(getHeight() / 2);
            }
            delegate.init();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (delegate != null)
            delegate.setSelected(selected);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (delegate != null)
            delegate.init();
    }
}
