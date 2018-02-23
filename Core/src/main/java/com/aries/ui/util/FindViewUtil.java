package com.aries.ui.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created: AriesHoo on 2018/2/9/009 10:44
 * E-Mail: AriesHoo@126.com
 * Function: 查找View工具类
 * Description:
 */
public class FindViewUtil {

    /**
     * 获取某个ViewGroup内某个类型的View--第一个
     *
     * @param rootView    根布局
     * @param targetClass View类型Class
     * @param <T>         返回View对象泛型
     * @return
     */
    public static <T extends View> T getTargetView(View rootView, Class<? extends View> targetClass) {
        if (rootView != null && targetClass != null) {
            if (targetClass == rootView.getClass()) {
                return (T) rootView;
            } else if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int childCount = contentView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childView = contentView.getChildAt(i);
                    View target = getTargetView(childView, targetClass);
                    if (target != null) {
                        return (T) target;
                    }
                }
            }
        }
        return null;
    }
}
