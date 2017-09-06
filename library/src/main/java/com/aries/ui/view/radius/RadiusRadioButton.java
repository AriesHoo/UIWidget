package com.aries.ui.view.radius;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

/**
 * Created: AriesHoo on 2017-02-13 16:10
 * Function:用于需要圆角矩形框背景的RadioButton的情况,减少直接使用RadioButton时引入的shape资源文件
 * Desc:
 */
public class RadiusRadioButton extends RadioButton {
    private RadiusViewDelegate delegate;

    public RadiusRadioButton(Context context) {
        this(context, null);
    }

    public RadiusRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new RadiusViewDelegate(this, context, attrs);
    }


    /**
     * 获取代理类用于Java代码控制shape属性
     *
     * @return
     */
    public RadiusViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
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
        if (delegate != null && delegate.getRadiusHalfHeightEnable()) {
            delegate.setRadius(getHeight() / 2);
        } else {
            if (delegate != null)
                delegate.setBgSelector();
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (delegate != null)
            delegate.setBgSelector();
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (delegate != null)
            delegate.setBgSelector();
    }
}
