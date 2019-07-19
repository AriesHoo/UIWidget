package com.aries.ui.listener;

/**
 * @Author: AriesHoo on 2019/7/18 9:38
 * @E-Mail: AriesHoo@126.com
 * @Function: 生命周期监听--用于传入Activity/Fragment 进行生命周期绑定功能
 * @Description:
 */
public interface LifecycleListener {

    void onDestroy();

    void onStart();

    void onStop();
}
