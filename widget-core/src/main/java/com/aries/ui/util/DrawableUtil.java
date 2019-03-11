package com.aries.ui.util;

import android.graphics.drawable.Drawable;

/**
 * @Author: AriesHoo on 2018/7/19 9:47
 * @E-Mail: AriesHoo@126.com
 * Function: Drawable设置相关工具类
 * Description:
 * 1、2018-11-16 16:36:09 修改newDrawable返回
 */
public class DrawableUtil {

    /**
     * 设置drawable宽高
     *
     * @param drawable
     * @param width
     * @param height
     */
    public static void setDrawableWidthHeight(Drawable drawable, int width, int height) {
        try {
            if (drawable != null) {
                drawable.setBounds(0, 0,
                        width >= 0 ? width : drawable.getIntrinsicWidth(),
                        height >= 0 ? height : drawable.getIntrinsicHeight());
            }
        } catch (Exception e) {
        }
    }

    /**
     * 复制当前drawable
     *
     * @param drawable
     * @return
     */
    public static Drawable getNewDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }
        return drawable.getConstantState().newDrawable();
    }

}
