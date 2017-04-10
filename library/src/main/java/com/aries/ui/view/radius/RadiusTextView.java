package com.aries.ui.view.radius;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created: AriesHoo on 2017-02-10 14:24
 * Function:用于需要圆角矩形框背景的TextView的情况,减少直接使用TextView时引入的shape资源文件
 * Desc:
 */
public class RadiusTextView extends TextView {
    private RadiusViewDelegate delegate;

    public RadiusTextView(Context context) {
        this(context, null);
    }

    public RadiusTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadiusTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        delegate = new RadiusViewDelegate(this, context, attrs);
    }

    /**
     * use delegate to set attr
     */
    public RadiusViewDelegate getDelegate() {
        return delegate;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (delegate.getWidthHeightEqualEnable() && getWidth() > 0 && getHeight() > 0) {
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
        if (delegate.getRadiusHalfHeightEnable()) {
            delegate.setRadius(getHeight() / 2);
        } else {
            delegate.setBgSelector();
        }
    }
}
