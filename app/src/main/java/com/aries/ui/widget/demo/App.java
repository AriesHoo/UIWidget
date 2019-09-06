package com.aries.ui.widget.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.aries.ui.widget.demo.util.SPUtil;
import com.parfoismeng.slidebacklib.SlideBack;
import com.parfoismeng.slidebacklib.callback.SlideBackCallBack;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @Author: AriesHoo on 2018/7/19 9:31
 * @E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class App extends MultiDexApplication implements Application.ActivityLifecycleCallbacks {

    public static Context getContext() {
        return sContext;
    }

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        CrashReport.initCrashReport(getApplicationContext());
        String appChannel = (String) SPUtil.get(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, "");
        Log.i("appChannel", "appChannel0:" + appChannel);
        if (TextUtils.isEmpty(appChannel)) {
            appChannel = CrashReport.getAppChannel();
            Log.i("appChannel", "appChannel1:" + appChannel);
            SPUtil.put(getApplicationContext(), SPConstant.SP_KEY_APP_CHANNEL, appChannel);
        } else {
            CrashReport.setAppChannel(getApplicationContext(), appChannel);
        }
        Log.i("appChannel", "appChannel2:" + appChannel);
        if (BuildConfig.DEBUG) {
            CrashReport.closeBugly();
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // 在需要滑动返回的Activity中注册，最好但非必须在onCreate中
        SlideBack.with(activity)
                .haveScroll(true)
                .edgeMode(SlideBack.EDGE_LEFT)
                .callBack(new SlideBackCallBack() {
                    @Override
                    public void onSlideBack() {
                        activity.finish();
                    }
                })
                .register();
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SlideBack.unregister(activity);
    }
}
