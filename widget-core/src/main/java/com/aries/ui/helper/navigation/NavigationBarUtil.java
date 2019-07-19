package com.aries.ui.helper.navigation;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.aries.ui.widget.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author: AriesHoo on 2018/7/19 9:32
 * @E-Mail: AriesHoo@126.com
 * Function: 导航栏控制工具类
 * Description:
 * 1、2018-11-9 11:30:44 修改获取虚拟导航栏高度方法增加VIVO及MIUI全面屏判断
 * 2、2018-11-28 13:21:53 新增华为、三星导航栏可动态隐藏显示判断逻辑
 * 3、2018-12-17 10:11:29 新增是否开启全面屏手势方法{@link #isOpenFullScreenGestures(Context)}
 * 4、2019-4-9 14:54:13 新增参数Activity有关方法增加对应Window方法{@link #hasNavBar(Window)}
 * 5、2019-4-15 09:51:42 新增判断导航栏是否位于底部方法{@link #isNavigationAtBottom(Window)}{@link #isNavigationAtBottom(Activity)}
 * 6、2019-4-15 10:30:13 新增导航栏图标颜色深浅方法{@link #setNavigationBarDarkMode(Window)}{@link #setNavigationBarDarkMode(Activity)}
 * {@link #setNavigationBarLightMode(Window)} {@link #setNavigationBarLightMode(Activity)}
 */
public class NavigationBarUtil {

    private static final String NAV_BAR_HEIGHT_RES_NAME = "navigation_bar_height";
    private static final String NAV_BAR_HEIGHT_LANDSCAPE_RES_NAME = "navigation_bar_height_landscape";
    private static final String MIUI_FORCE_FSG_NAV_BAR = "force_fsg_nav_bar";
    private static final String VIVO_NAVIGATION_GESTURE_ON = "navigation_gesture_on";
    /**
     * 监控虚拟导航栏是否关闭--目前测试华为手机可用户手动开启关闭
     */
    private static final String NAVIGATION_BAR_IS_MIN = "navigationbar_is_min";
    /**
     * 三星导航栏可关闭属性
     */
    private static final String NAVIGATION_BAR_HIDE_BAR_ENABLED = "navigationbar_hide_bar_enabled";
    /**
     * 其它导航栏可关闭属性
     */
    private static final String NAVIGATION_BAR_POLICY_CONTROL = "policy_control";

    private static final String NAVIGATION_BAR_POLICY_CONTROL_VALUE = "immersive.navigation=*";

    /**
     * 判断是否是全面屏
     */
    private volatile static boolean mHasCheckFullScreen;
    private volatile static boolean mIsFullScreenDevice;
    private volatile static float mAspectRatio = 1.97f;

    /**
     * 判断手机是否开启全面屏手势--根据判断系统是否开启虚拟导航栏(如华为可手动开关该方法不是完全正确的)
     *
     * @param context
     * @return
     */
    public static boolean isOpenFullScreenGestures(Context context) {
        if (context == null || !isFullScreenDevice(context)) {
            return false;
        }
        //判断小米手机是否开启了全面屏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            if (Settings.Global.getInt(context.getContentResolver(), MIUI_FORCE_FSG_NAV_BAR, 0) != 0) {
                return true;
            }
        }
        //ViVo是否开启了全面屏手势
        if (Settings.Secure.getInt(context.getContentResolver(), VIVO_NAVIGATION_GESTURE_ON, 0) != 0) {
            return true;
        }
        //华为导航栏隐藏
        if (Settings.System.getInt(context.getContentResolver(), NAVIGATION_BAR_IS_MIN, 0) == 1) {
            return true;
        }
        //三星导航栏隐藏
        if (Settings.System.getInt(context.getContentResolver(), NAVIGATION_BAR_HIDE_BAR_ENABLED, 0) == 1) {
            return true;
        }
        //其它导航栏隐藏
        if (NAVIGATION_BAR_POLICY_CONTROL_VALUE.equals(Settings.System.getString(context.getContentResolver(), NAVIGATION_BAR_POLICY_CONTROL))) {
            return true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.i("isOpenFull", "NavigationBar:" + (Settings.Global.getInt(context.getContentResolver(), NAVIGATION_BAR_IS_MIN, 0)));
            //华为导航栏隐藏
            if (Settings.Global.getInt(context.getContentResolver(), NAVIGATION_BAR_IS_MIN, 0) == 1) {
                return true;
            }
            //三星导航栏隐藏
            if (Settings.Global.getInt(context.getContentResolver(), NAVIGATION_BAR_HIDE_BAR_ENABLED, 0) == 1) {
                return true;
            }
            //其它导航栏隐藏
            if (NAVIGATION_BAR_POLICY_CONTROL_VALUE.equals(Settings.Global.getString(context.getContentResolver(), NAVIGATION_BAR_POLICY_CONTROL))) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasNavBar(Activity activity) {
        if (activity == null) {
            return false;
        }
        return hasNavBar(activity.getWindow());
    }

    /**
     * 是否开启虚拟导航栏
     *
     * @param window
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Window window) {
        if (window == null) {
            return false;
        }
        if (isOpenFullScreenGestures(window.getContext())) {
            return false;
        }
        //其他手机根据屏幕真实高度与显示高度是否相同来判断
        WindowManager windowManager = window.getWindowManager();
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

    public static int getFakeNavigationBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        return getFakeNavigationBarHeight(activity.getWindow());
    }

    /**
     * 获取假导航栏高度
     *
     * @return
     */
    public static int getFakeNavigationBarHeight(Window window) {
        if (window == null) {
            return 0;
        }
        View viewNavigation = window.getDecorView().findViewById(R.id.fake_navigation_layout);
        if (viewNavigation != null) {
            return viewNavigation.getMeasuredHeight();
        }
        return 0;
    }

    public static int getRealNavigationBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        return getRealNavigationBarHeight(activity.getWindow());
    }

    /**
     * 获取真实的导航栏高度--有些全面屏设置全面屏手势通过{@link #getNavigationBarHeight(Activity)}仍会获取到高度
     * 该方法通过获取Activity activity.getWindow().getDecorView()下是否有导航栏View --
     * 像华为这种可以动态隐藏的
     * 1-进入Activity 前没有打开导航栏 是获取不到导航栏View的,当上滑时才创建即可获取到，再关闭{@link View#getVisibility()}
     *
     * @param window
     * @return 获取的前提是 window.getDecorView().setSystemUiVisibility 未设置 View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
     * 设置后系统是不会生成那个View的
     */
    public static int getRealNavigationBarHeight(Window window) {
        if (window != null) {
            View viewNavigation = window.getDecorView().findViewById(android.R.id.navigationBarBackground);
            if (viewNavigation != null) {
                return viewNavigation.getMeasuredHeight();
            }
        }
        return 0;
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (activity == null) {
            return 0;
        }
        return getNavigationBarHeight(activity.getWindow());
    }

    /**
     * 获取导航栏高度
     *
     * @param window
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static int getNavigationBarHeight(Window window) {
        //是否横屏
        boolean landscape = Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        if (hasNavBar(window)) {
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

    /**
     * 判断当前手机是否为全面屏--通过纵横比
     *
     * @param context
     * @return
     */
    public static boolean isFullScreenDevice(Context context) {
        if (context == null) {
            return false;
        }
        if (mHasCheckFullScreen) {
            return mIsFullScreenDevice;
        }
        mHasCheckFullScreen = true;
        mIsFullScreenDevice = false;
        // 低于 API 21的，都不会是全面屏
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return false;
        }
        mIsFullScreenDevice = getAspectRatio(context) >= mAspectRatio;
        return mIsFullScreenDevice;
    }

    /**
     * 获取手机纵横比
     *
     * @param context
     * @return
     */
    public static float getAspectRatio(Context context) {
        if (context == null) {
            return 0f;
        }
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) {
            Display display = windowManager.getDefaultDisplay();
            Point point = new Point();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealSize(point);
            }
            float width, height;
            if (point.x < point.y) {
                width = point.x;
                height = point.y;
            } else {
                width = point.y;
                height = point.x;
            }
            return height / width;
        }
        return 0f;
    }

    @SuppressLint("NewApi")
    private static float getSmallestWidthDp(Window window) {
        DisplayMetrics metrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            window.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            // TODO this is not correct, but we don't really care pre-kitkat
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        float widthDp = metrics.widthPixels / metrics.density;
        float heightDp = metrics.heightPixels / metrics.density;
        return Math.min(widthDp, heightDp);
    }

    public static boolean isNavigationAtBottom(Activity activity) {
        if (activity == null) {
            return false;
        }
        return isNavigationAtBottom(activity.getWindow());
    }

    /**
     * 判断导航栏是否在底部
     *
     * @param window
     * @return
     */
    public static boolean isNavigationAtBottom(Window window) {
        if (window == null) {
            return false;
        }
        boolean mInPortrait = (Resources.getSystem().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
        float mSmallestWidthDp = getSmallestWidthDp(window);
        return (mSmallestWidthDp >= 600 || mInPortrait);
    }


    public static final int NAVIGATION_BAR_TYPE_DEFAULT = 0;
    public static final int NAVIGATION_BAR_TYPE_MI_UI = 1;
    public static final int NAVIGATION_BAR_TYPE_ANDROID_O = 2;

    public static int setNavigationBarLightMode(Activity activity) {
        if (activity == null) {
            return -1;
        }
        return setNavigationBarLightMode(activity.getWindow());
    }

    /**
     * 设置导航栏栏浅色模式--黑色字体图标，
     *
     * @param window
     * @return
     */
    public static int setNavigationBarLightMode(Window window) {
        if (window == null) {
            return -1;
        }
        int result = NAVIGATION_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setNavigationBarModeForAndroidO(window, true)) {
                result = NAVIGATION_BAR_TYPE_ANDROID_O;
            }
            if (setNavigationBarModeForMIUI(window, true)) {
                result = NAVIGATION_BAR_TYPE_MI_UI;
            }
        }
        return result;
    }

    public static int setNavigationBarDarkMode(Activity activity) {
        if (activity == null) {
            return -1;
        }
        return setNavigationBarDarkMode(activity.getWindow());
    }

    /**
     * 设置导航栏深色模式--白色字体图标，
     *
     * @param window
     * @return
     */
    public static int setNavigationBarDarkMode(Window window) {
        int result = NAVIGATION_BAR_TYPE_DEFAULT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //MIUI 9版本开始状态栏文字颜色恢复为系统原生方案-为防止反复修改先进行6.0方案
            if (setNavigationBarModeForAndroidO(window, false)) {
                result = NAVIGATION_BAR_TYPE_ANDROID_O;
            }
            if (setNavigationBarModeForMIUI(window, false)) {
                result = NAVIGATION_BAR_TYPE_MI_UI;
            }
        }
        return result;
    }

    /**
     * 设置MIUI导航栏深色
     *
     * @param window   需要设置的窗口
     * @param darkText 是否把导航栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    private static boolean setNavigationBarModeForMIUI(Window window, boolean darkText) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_NAVIGATION_BAR_DARK_MODE");
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

    private static boolean setNavigationBarModeForAndroidO(Window window, boolean darkText) {
        if (window == null) {
            return false;
        }
        boolean result = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int now = window.getDecorView().getSystemUiVisibility();
            int systemUi = darkText ?
                     now | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR :
                    (now & View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR) == View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR ? now ^ View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR : now;
            window.getDecorView().setSystemUiVisibility(systemUi);
            result = true;
        }
        return result;
    }
}
