package com.aries.ui.widget.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.widget.demo.util.NotificationUtil;
import com.aries.ui.widget.demo.util.SPUtil;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * @Author: AriesHoo on 2018/7/19 9:31
 * @E-Mail: AriesHoo@126.com
 * Function:
 * Description:
 */
public class App extends MultiDexApplication {

    public static Context getContext() {
        return sContext;
    }

    public static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        NotificationUtil.getInstance().init(this);
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
        BGASwipeBackHelper.init(this, null);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Log.e("onActivityCreated", "isNavigationBarExist:" + NavigationBarUtil.hasNavBar(activity) + ";isNavigationAtBottom:" + NavigationBarUtil.isNavigationAtBottom(activity.getWindow()));
                new BGASwipeBackHelper(activity, new BGASwipeBackHelper.Delegate() {
                    @Override
                    public boolean isSupportSwipeBack() {
                        return true;
                    }

                    @Override
                    public void onSwipeBackLayoutSlide(float slideOffset) {

                    }

                    @Override
                    public void onSwipeBackLayoutCancel() {

                    }

                    @Override
                    public void onSwipeBackLayoutExecuted() {
                        //设置退出动画-确保效果准确
                        if (activity == null || activity.isFinishing()) {
                            return;
                        }
                        KeyboardHelper.closeKeyboard(activity);
                        activity.finish();
                        activity.overridePendingTransition(0, R.anim.bga_sbl_activity_swipeback_exit);

                    }
                })
                        //设置滑动背景
                        .setShadowResId(R.drawable.bga_sbl_shadow)
                        //底部导航条是否悬浮在内容上设置过NavigationViewHelper可以不用设置该属性
                        .setIsNavigationBarOverlap(NavigationBarUtil.isNavigationAtBottom(activity))
                        .setIsShadowAlphaGradient(true);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {
                //Activity销毁前的时机需要关闭软键盘-在onActivityStopped及onActivityDestroyed生命周期内已无法关闭
                if (activity.isFinishing()) {
                    KeyboardHelper.closeKeyboard(activity);
                }
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                Log.e("onActivityDestroyed", "isNavigationBarExist:" + NavigationBarUtil.hasNavBar(activity) + ";isNavigationAtBottom:" + NavigationBarUtil.isNavigationAtBottom(activity.getWindow()));
            }
        });
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
