package com.aries.ui.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: AriesHoo on 2018/7/19 9:50
 * @E-Mail: AriesHoo@126.com
 * Function: 状态栏工具类(状态栏文字颜色)
 * Description:
 * 1、修改状态栏黑白字 功能逻辑--参考 https://github.com/QMUI/QMUI_Android  QMUIStatusBarHelper类
 * 2、2019-4-11 10:42:27 新增Activity参数相关的Window参数方法
 * {@link #setStatusBarDarkMode(Window)} {@link #setStatusBarLightMode(Window)}
 */
public class StatusBarUtil {

    public static final int STATUS_BAR_TYPE_DEFAULT = 0;
    public static final int STATUS_BAR_TYPE_MI_UI = 1;
    public static final int STATUS_BAR_TYPE_FLY_ME = 2;
    public static final int STATUS_BAR_TYPE_ANDROID_M = 3;

    public static int setStatusBarLightMode(Activity activity) {
        if (activity == null) {
            return -1;
        }
        return setStatusBarLightMode(activity.getWindow());
    }

    /**
     * 设置状态栏浅色模式--黑色字体图标，
     *
     * @param window
     * @return
     */
    public static int setStatusBarLightMode(Window window) {
        if (window == null) {
            return -1;
        }
        int result = STATUS_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setStatusBarModeForAndroidM(window, true)) {
                result = STATUS_BAR_TYPE_ANDROID_M;
            }
            if (setStatusBarModeForMIUI(window, true)) {
                result = STATUS_BAR_TYPE_MI_UI;
            } else if (setStatusBarModeForFlyMe(window, true)) {
                result = STATUS_BAR_TYPE_FLY_ME;
            }
        }
        return result;
    }

    public static int setStatusBarDarkMode(Activity activity) {
        if (activity == null) {
            return -1;
        }
        return setStatusBarDarkMode(activity.getWindow());
    }

    /**
     * 设置状态栏深色模式--白色字体图标，
     *
     * @param window
     * @return
     */
    public static int setStatusBarDarkMode(Window window) {
        int result = STATUS_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setStatusBarModeForAndroidM(window, false)) {
                result = STATUS_BAR_TYPE_ANDROID_M;
            }
            if (setStatusBarModeForMIUI(window, false)) {
                result = STATUS_BAR_TYPE_MI_UI;
            } else if (setStatusBarModeForFlyMe(window, false)) {
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
                    //状态栏透明且黑色字体
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
                } else {
                    //清除黑色字体
                    extraFlagField.invoke(window, 0, darkModeFlag);
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
            int systemUi = darkText ? View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            systemUi = changeStatusBarModeRetainFlag(window, systemUi);
            window.getDecorView().setSystemUiVisibility(systemUi);
            //int now = window.getDecorView().getSystemUiVisibility();
            //int systemUi = darkText ?
            //         now | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR :
            //       (now & View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR) == View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR ? now ^ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR : now;
            //window.getDecorView().setSystemUiVisibility(systemUi);
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
        } else {
            return false;
        }
    }

    /**
     * 获取状态栏高度
     *
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

    @TargetApi(23)
    private static int changeStatusBarModeRetainFlag(Window window, int out) {
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_FULLSCREEN);
        //隐藏导航栏按键
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //隐藏导航栏背景View
        out = retainSystemUiFlag(window, out, View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        return out;
    }

    public static int retainSystemUiFlag(Window window, int out, int type) {
        int now = window.getDecorView().getSystemUiVisibility();
        if ((now & type) == type) {
            out |= type;
        }
        return out;
    }

    public static void fitsNotchScreen(Window window, boolean fit) {
        if (window == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.layoutInDisplayCutoutMode = fit ? WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES : WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_DEFAULT;
            window.setAttributes(lp);
        }
    }
}
