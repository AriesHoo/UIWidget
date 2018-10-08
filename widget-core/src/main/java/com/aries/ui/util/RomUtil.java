package com.aries.ui.util;

import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * @Author: AriesHoo on 2018/7/19 9:49
 * @E-Mail: AriesHoo@126.com
 * Function:手机ROM工具类-主要国产手机:通过android.os.SystemProperties系统属性获取
 * Description:
 * 1、新增是否为某个手机方法新增OPPO/VIVO/SMARTISAN/360 ROM方法
 */
public class RomUtil {

    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_EMUI_VERSION_NAME = "ro.build.version.emui";
    private static final String KEY_OPPO_VERSION_NAME = "ro.build.version.opporom";
    private static final String KEY_VIVO_VERSION_NAME = "ro.vivo.os.version";
    private static final String KEY_SMARTISAN_VERSION_NAME = "ro.smartisan.version";

    /**
     * 判断是否为MIUI-小米ROM
     *
     * @return
     */
    public static boolean isMIUI() {
        return isRom(KEY_MIUI_VERSION_NAME);
    }

    /**
     * 获取MUI版本
     *
     * @return
     */
    public static String getMIUIVersion() {
        return getSystemProperty(KEY_MIUI_VERSION_NAME);
    }

    /**
     * 获取MIUI版本-数字用于大小判断
     *
     * @return
     */
    public static int getMIUIVersionCode() {
        int code = -1;
        String property = getMIUIVersion();
        try {
            property = property.trim().toUpperCase().replace("V", "");
            code = Integer.parseInt(property);
        } catch (Exception e) {
        }
        return code;
    }

    /**
     * 判断是否为EMUI
     *
     * @return
     */
    public static boolean isEMUI() {
        return isRom(KEY_EMUI_VERSION_NAME);
    }


    /**
     * 获取EMUI的版本
     *
     * @return
     */
    public static String getEMUIVersion() {
        return getSystemProperty(KEY_EMUI_VERSION_NAME);
    }

    /**
     * 判断是否为oppo
     *
     * @return
     */
    public static boolean isOPPO() {
        return isRom(KEY_OPPO_VERSION_NAME);
    }


    /**
     * 获取oppo的版本
     *
     * @return
     */
    public static String getOPPOVersion() {
        return getSystemProperty(KEY_OPPO_VERSION_NAME);
    }

    /**
     * 判断是否为vivo
     *
     * @return
     */
    public static boolean isVIVO() {
        return isRom(KEY_VIVO_VERSION_NAME);
    }


    /**
     * 获取vivo的版本
     *
     * @return
     */
    public static String getVIVOVersion() {
        return getSystemProperty(KEY_VIVO_VERSION_NAME);
    }

    /**
     * 判断是否为smartisan
     *
     * @return
     */
    public static boolean isSmartisan() {
        return isRom(KEY_SMARTISAN_VERSION_NAME);
    }


    /**
     * 获取smartisan的版本
     *
     * @return
     */
    public static String getSmartisanVersion() {
        return getSystemProperty(KEY_SMARTISAN_VERSION_NAME);
    }

    /**
     * 判断是否为Flyme
     *
     * @return
     */
    public static boolean isFlyme() {
        return Build.DISPLAY.toLowerCase().contains("flyme");
    }

    /**
     * 获取Flyme的版本
     *
     * @return
     */
    public static String getFlymeVersion() {
        return isFlyme() ? Build.DISPLAY : "";
    }

    /**
     * 获取Flyme版本号
     *
     * @return
     */
    public static int getFlymeVersionCode() {
        int code = 0;
        String version = getFlymeVersion();
        if (!TextUtils.isEmpty(version)) {
            if (version.toLowerCase().contains("os")) {
                code = Integer.valueOf(version.substring(9, 10));
            } else {
                code = Integer.valueOf(version.substring(6, 7));
            }
        }
        return code;
    }


    /**
     * 判断是否为QiKu 360
     *
     * @return
     */
    public static boolean isQiKu() {
        return Build.MANUFACTURER.toUpperCase().contains("QIKU") || Build.MANUFACTURER.contains("360");
    }

    /**
     * 获取QiKu 360的版本
     *
     * @return
     */
    public static String getQiKuVersion() {
        return isFlyme() ? Build.MANUFACTURER : "";
    }

    /**
     * 根据android.os.SystemProperties是否有某个手机系统版本key来判断是否存在来判断是否有某个ROM
     *
     * @param key
     * @return
     */
    public static boolean isRom(String key) {
        return !TextUtils.isEmpty(getSystemProperty(key));
    }

    /**
     * 通过反射获取系统属性
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public static String getSystemProperty(String key, String defaultValue) {
        try {
            Class<?> clz = Class.forName("android.os.SystemProperties");
            Method get = clz.getMethod("get", String.class, String.class);
            return (String) get.invoke(clz, key, defaultValue);
        } catch (Exception e) {
        }
        return defaultValue;
    }

    public static String getSystemProperty(String key) {
        return getSystemProperty(key, "");
    }
}
