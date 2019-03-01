package com.aries.ui.widget.demo.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import com.aries.ui.widget.demo.App;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

/**
 * @Author: AriesHoo on 2018/7/20 15:41
 * @E-Mail: AriesHoo@126.com
 * Function: FileProvider文件路径处理帮助类
 * Description:
 * 1、2018-7-23 14:28:33 新增安装apk兼容7.0以下版本
 * 2、2018-7-24 10:13:01 新增{@link #installApk(File, String)} 以传入开发者自定义authority--FileProvider
 */
public class FastFileUtil {

    /**
     * 安装apk 包路径在FastFileProvider 配置路径下
     *
     * @param apkPath apk 文件对象
     */
    public static void installApk(File apkPath) {
        Context context = App.sContext;
        if (context == null || apkPath == null) {
            return;
        }
        installApk(apkPath, context.getPackageName() + ".AgentWebFileProvider");
    }

    /**
     * 安装App 使用lib FileProvider
     *
     * @param apkPath apk 文件对象
     */
    public static void installApk(File apkPath, @NonNull String authority) {
        Context context = App.sContext;
        if (context == null || apkPath == null) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // context 使用startActivity需增加 FLAG_ACTIVITY_NEW_TASK TAG 否则低版本上(目前发现在7.0以下版本)会提示以下错误
        //android.util.AndroidRuntimeException: Calling startActivity() from outside of an Activity  context requires the FLAG_ACTIVITY_NEW_TASK flag. Is this really what you want?
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri apkUri;
        //判断版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //加入provider
            apkUri = FileProvider.getUriForFile(context, authority, apkPath);
            //授予一个URI的临时权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            apkUri = Uri.fromFile(apkPath);
        }
        intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}
