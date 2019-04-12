package com.aries.ui.widget.demo.util;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * @Author: AriesHoo on 2019/4/11 11:07
 * @E-Mail: AriesHoo@126.com
 * @Function: 查找某个View下的所有View
 * @Description:
 */
public class GetViewUtil {

    /**
     * 获取某个ViewGroup内某个类型且某个id的View
     *
     * @param rootView 根布局
     */
    public static void getTargetView(View rootView) {
        if (rootView == null) {
            return;
        }
        //以上条件都不满足才进行
        if (rootView instanceof ViewGroup) {
            ViewGroup contentView = (ViewGroup) rootView;
            int size = contentView.getChildCount();
            //循环遍历所有子View
            for (int i = 0; i < size; i++) {
                View childView = contentView.getChildAt(i);
                //递归查找
                getTargetView(childView);
            }
        } else {
            Log.e("getTargetView", "viewClass:" + rootView);
        }
    }

}