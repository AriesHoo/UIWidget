package com.aries.ui.helper.alpha;

import android.util.Log;
import android.view.View;

import com.aries.ui.util.ResourceUtil;
import com.aries.ui.widget.R;

import java.lang.ref.WeakReference;

/**
 * Created: AriesHoo on 2018/3/12/012 12:41
 * E-Mail: AriesHoo@126.com
 * Function: 设置View按下状态透明度变化帮助类
 * Description:
 * 1、新增全局style设置透明度属性
 * 2、删除设置Normal状态下透明度方法
 */
public class AlphaViewHelper {

    private WeakReference<View> mTarget;

    private float mNormalAlpha = 1f;
    private float mPressedAlpha = 0.5f;
    private float mDisabledAlpha = 0.5f;
    private ResourceUtil mResourceUtil;

    public AlphaViewHelper(View target) {
        if (target == null) return;
        mTarget = new WeakReference<>(target);
        mResourceUtil = new ResourceUtil(target.getContext());
        float press = mResourceUtil.getAttrFloat(R.attr.pressedAlpha);
        float disable = mResourceUtil.getAttrFloat(R.attr.disabledAlpha);
        setPressedAlpha(press)
                .setDisabledAlpha(disable);
    }

    public AlphaViewHelper(View target, float pressedAlpha, float disabledAlpha) {
        this(target);
        setPressedAlpha(pressedAlpha)
                .setDisabledAlpha(disabledAlpha);
    }

    /**
     * @param current the view to be handled, maybe not equal to target view
     * @param pressed
     */
    public AlphaViewHelper onPressedChanged(View current, boolean pressed) {
        Log.i("onPressedChanged", "pressd:" + mPressedAlpha);
        View target = mTarget.get();
        if (target == null) {
            return this;
        }
        if (current.isEnabled()) {
            target.setAlpha(pressed && current.isClickable() ? mPressedAlpha : mNormalAlpha);
        } else {
            target.setAlpha(mDisabledAlpha);
        }
        return this;
    }

    /**
     * @param current the view to be handled, maybe not  equal to target view
     * @param enabled
     */
    public AlphaViewHelper onEnabledChanged(View current, boolean enabled) {
        View target = mTarget.get();
        if (target == null) {
            return this;
        }
        float alphaForIsEnable = enabled ? mNormalAlpha : mDisabledAlpha;
        if (current != target && target.isEnabled() != enabled) {
            target.setEnabled(enabled);
        }
        target.setAlpha(alphaForIsEnable);
        return this;
    }

    /**
     * 设置各状态下alpha值
     *
     * @param pressed
     * @param disabled
     * @return
     */
    public AlphaViewHelper setAlpha(float pressed, float disabled) {
        return setPressedAlpha(pressed)
                .setDisabledAlpha(disabled);
    }

    public AlphaViewHelper setPressedAlpha(float pressed) {
        if (pressed < 0) {
            pressed = 0.0f;
        } else if (pressed > 1) {
            pressed = 1.0f;
        }
        this.mPressedAlpha = pressed;
        return this;
    }

    public AlphaViewHelper setDisabledAlpha(float disabled) {
        if (disabled < 0) {
            disabled = 0.0f;
        } else if (disabled > 1) {
            disabled = 1.0f;
        }
        this.mDisabledAlpha = disabled;
        return this;
    }

}
