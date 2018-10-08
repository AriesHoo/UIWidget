package com.aries.ui.view.alpha;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.aries.ui.view.alpha.delegate.AlphaDelegate;

/**
 * @Author: AriesHoo on 2018/7/19 9:55
 * @E-Mail: AriesHoo@126.com
 * Function: 控制Alpha 按下效果
 * Description:
 */
public class AlphaLinearLayout extends LinearLayout {

    private AlphaDelegate delegate;

    public AlphaLinearLayout(Context context) {
        this(context, null);
    }

    public AlphaLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        delegate = new AlphaDelegate(this);
    }

    public AlphaDelegate getDelegate() {
        return delegate;
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        delegate.getAlphaViewHelper().onPressedChanged(this, pressed);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        delegate.getAlphaViewHelper().onEnabledChanged(this, enabled);
    }
}
