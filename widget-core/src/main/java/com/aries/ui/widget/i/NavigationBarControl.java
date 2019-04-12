package com.aries.ui.widget.i;

import android.app.Dialog;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationViewHelper;

/**
 * @Author: AriesHoo on 2019/4/10 17:28
 * @E-Mail: AriesHoo@126.com
 * @Function: Dialog设置虚拟导航栏控制
 * @Description:
 */
public interface NavigationBarControl {

    /**
     * Activity 全局虚拟导航栏控制
     *
     * @param dialog
     * @param helper
     * @param bottomView
     * @return true 表示调用 helper 的init方法进行设置
     */
    boolean setNavigationBar(Dialog dialog, NavigationViewHelper helper, View bottomView);
}
