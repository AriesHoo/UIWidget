package com.aries.ui.util;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created: AriesHoo on 2017/7/17 9:52
 * E-Mail: AriesHoo@126.com
 * Function: 状态栏工具类(状态栏文字颜色)
 * Description:
 */
public class StatusBarUtil {

    public static final int STATUS_BAR_TYPE_DEFAULT = 0;
    public static final int STATUS_BAR_TYPE_MI_UI = 1;
    public static final int STATUS_BAR_TYPE_FLY_ME = 2;
    public static final int STATUS_BAR_TYPE_ANDROID_M = 3;

    /**
     * 设置状态栏浅色模式--黑色字体图标，
     *
     * @param activity
     * @return
     */
    public static int setStatusBarLightMode(Activity activity) {
        int result = STATUS_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setStatusBarModeForAndroidM(activity.getWindow(), true)) {
                result = STATUS_BAR_TYPE_ANDROID_M;
            }
            if (setStatusBarModeForMIUI(activity.getWindow(), true)) {
                result = STATUS_BAR_TYPE_MI_UI;
            } else if (setStatusBarModeForFlyMe(activity.getWindow(), true)) {
                result = STATUS_BAR_TYPE_FLY_ME;
            }
        }
        return result;
    }

    /**
     * 设置状态栏深色模式--白色字体图标，
     *
     * @param activity
     * @return
     */
    public static int setStatusBarDarkMode(Activity activity) {
        int result = STATUS_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setStatusBarModeForAndroidM(activity.getWindow(), false)) {
                result = STATUS_BAR_TYPE_ANDROID_M;
            }
            if (setStatusBarModeForMIUI(activity.getWindow(), false)) {
                result = STATUS_BAR_TYPE_MI_UI;
            } else if (setStatusBarModeForFlyMe(activity.getWindow(), false)) {
                result = STATUS_BAR_TYPE_FLY_ME;
            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window   需要设置的窗口
     * @param darkText 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarModeForMIUI(Window window, boolean darkText) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (darkText) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @param window   需要设置的窗口
     * @param darkText 是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setStatusBarModeForFlyMe(Window window, boolean darkText) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (darkText) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置原生Android 6.0以上系统状态栏
     *
     * @param window
     * @param darkText 是否把状态栏字体及图标颜色设置为深色
     * @return
     */
    private static boolean setStatusBarModeForAndroidM(Window window, boolean darkText) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(darkText ? View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | 0x00002000 : View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
            result = true;
        }
        return result;
    }


    /**
     * 判断系统是否支持状态栏文字及图标颜色变化
     *
     * @return
     */
    public static boolean isSupportStatusBarFontChange() {
        if (RomUtil.getMIUIVersionCode() >= 6 || RomUtil.getFlymeVersionCode() >= 4
                || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)) {
            return true;
        } else
            return false;
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
