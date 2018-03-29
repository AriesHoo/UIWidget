package com.aries.ui.view.alpha;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.aries.ui.view.alpha.delegate.AlphaDelegate;

/**
 * Created: AriesHoo on 2018/3/14/014 9:42
 * E-Mail: AriesHoo@126.com
 * Function: 控制Alpha
 * Description:
 */
public class AlphaImageView extends ImageView {

    private AlphaDelegate delegate;

    public AlphaImageView(Context context) {
        this(context, null);
    }

    public AlphaImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AlphaImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
