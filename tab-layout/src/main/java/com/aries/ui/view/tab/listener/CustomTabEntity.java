package com.aries.ui.view.tab.listener;

import androidx.annotation.DrawableRes;

/**
 * @Author: AriesHoo on 2018/12/3 13:07
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link com.aries.ui.view.tab.CommonTabLayout} 属性设置
 * @Description:
 */
public interface CustomTabEntity {
    /**
     * tab文本
     *
     * @return
     */
    String getTabTitle();

    /**
     * tab 选中icon 资源id
     *
     * @return
     */
    @DrawableRes
    int getTabSelectedIcon();

    /**
     * tab未选中icon资源
     *
     * @return
     */
    @DrawableRes
    int getTabUnselectedIcon();
}