package com.aries.ui.helper.navigation;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Method;

/**
 * @Author: AriesHoo on 2018/7/19 9:32
 * @E-Mail: AriesHoo@126.com
 * Function: 导航栏控制工具类
 * Description:
 * 1、2018-11-9 11:30:44 修改获取虚拟导航栏高度方法增加VIVO及MIUI全面屏判断
 */
public class NavigationBarUtil {

    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String MIUI_FORCE_FSG_NAV_BAR = "force_fsg_nav_bar";
    private static final String VIVO_NAVIGATION_GESTURE_ON = "navigation_gesture_on";

    /**
     * 是否开启虚拟导航栏
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Activity activity) {
        if (activity == null) {
            return false;
        }
        //判断小米手机是否开启了全面屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(activity.getContentResolver(), MIUI_FORCE_FSG_NAV_BAR, 0) != 0) {
                return false;
            }
        }
        //ViVo是否开启全面屏
        if (Settings.Secure.getInt(activity.getContentResolver(), VIVO_NAVIGATION_GESTURE_ON, 0) != 0) {
            return false;
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager = activity.getWindowManager();
        Display d = windowManager.getDefaultDisplay();
        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            d.getRealMetrics(realDisplayMetrics);
        }
        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;
        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }

    /**
     * 获取导航栏高度
     *
     * @param activity
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static int getNavigationBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        //是否横屏
        boolean landscape = Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        if (hasNavBar(activity)) {
            return getInternalDimensionSize(landscape ? NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME : NAV_BAR_HEIGHT_RES_NAME);
        }
        return 0;
    }

    private static int getInternalDimensionSize(String key) {
        int result = 0;
        try {
            int resourceId = Resources.getSystem().getIdentifier(key, "dimen", "android");
            if (resourceId > 0) {
                result = Resources.getSystem().getDimensionPixelSize(resourceId);
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取导航栏高度
     *
     * @param windowManager 废弃 使用{{@link #getNavigationBarHeight(Activity)}}替换
     * @return
     */
    @Deprecated
    public static int getNavigationBarHeight(WindowManager windowManager) {
        int dpi = 0;
        try {
            Display display = windowManager.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            @SuppressWarnings("rawtypes")
            Class c;
            try {
                c = Class.forName("android.view.Display");
                @SuppressWarnings("unchecked")
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                dpi = dm.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return dpi - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            return 0;
        }

    }
}
