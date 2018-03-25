package com.aries.ui.view.alpha;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;

import com.aries.ui.view.alpha.delegate.AlphaDelegate;

/**
 * Created: AriesHoo on 2018/3/25/025 10:30
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class AlphaSwitch extends RadioButton {
    private AlphaDelegate delegate;

    public AlphaSwitch(Context context) {
        this(context, null);
    }

    public AlphaSwitch(Context context, AttributeSet attrs) {
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
