package com.aries.ui.widget.demo.util;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created: AriesHoo on 2017/12/19 14:07
 * E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class SizeUtil {
    public static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static int getScreenWidth() {
        return getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return getDisplayMetrics().heightPixels;
    }

    public static int px2dp(float pxValue) {
        final float scale = getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dp2px(float dpValue) {
        final float scale = getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2sp(float pxValue) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(float spValue) {
        final float fontScale = getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
