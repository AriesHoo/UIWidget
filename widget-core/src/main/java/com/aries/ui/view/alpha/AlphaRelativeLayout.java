package com.aries.ui.view.alpha;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.aries.ui.view.alpha.delegate.AlphaDelegate;

/**
 * Created: AriesHoo on 2018/3/14/014 9:55
 * E-Mail: AriesHoo@126.com
 * Function:控制Alpha
 * Description:
 */
public class AlphaRelativeLayout extends RelativeLayout {

    private AlphaDelegate delegate;

    public AlphaRelativeLayout(Context context) {
        this(context, null);
    }

    public AlphaRelativeLayout(Context context, AttributeSet attrs) {
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
