package com.aries.ui.util;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * @Author: AriesHoo on 2018/7/19 9:47
 * @E-Mail: AriesHoo@126.com
 * Function: Drawable设置相关工具类
 * Description:
 * 1、2018-11-16 16:36:09 修改newDrawable返回
 * 2、2019-4-22 17:00:49 新增{@link #setTintDrawable(Drawable, int)}{@link #setTintDrawable(Drawable, ColorStateList)}用于动态设置Drawable 颜色
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


    /**
     * 给一个Drawable变换线框颜色
     *
     * @param drawable 需要变换颜色的drawable
     * @param color    需要变换的颜色
     * @return
     */
    public static Drawable setTintDrawable(Drawable drawable, @ColorInt int color) {
        if (drawable != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    DrawableCompat.setTint(drawable, color);
                } catch (Exception e) {
                    drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
                }

            } else {
                drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
            }
        }
        return drawable;
    }

    public static Drawable setTintDrawable(Drawable drawable, @Nullable ColorStateList tint) {
        if (drawable != null && tint != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    DrawableCompat.setTintList(drawable, tint);
                } catch (Exception e) {
                    drawable.setColorFilter(tint.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
                }
            } else {
                drawable.setColorFilter(tint.getDefaultColor(), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return drawable;
    }

    public static Drawable setTintMode(Drawable drawable, @NonNull PorterDuff.Mode tintMode, int color) {
        if (drawable != null && tintMode != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    DrawableCompat.setTintMode(drawable, tintMode);
                } catch (Exception e) {
                    drawable.setColorFilter(color, tintMode);
                }
            } else {
                drawable.setColorFilter(color, tintMode);
            }
        }
        return drawable;
    }
}
