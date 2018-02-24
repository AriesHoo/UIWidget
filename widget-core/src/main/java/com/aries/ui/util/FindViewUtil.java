package com.aries.ui.util;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created: AriesHoo on 2018/2/9/009 10:44
 * E-Mail: AriesHoo@126.com
 * Function: 查找View工具类
 * Description:
 * 1、2018-2-24 10:10:55 新增查找某个id的目标View
 */
public class FindViewUtil {

    /**
     * 获取某个ViewGroup内某个类型且某个id的View
     *
     * @param rootView    根布局
     * @param targetClass View类型Class
     * @param id          ViewId:-1即满足targetClass的第一个
     * @param <T>         返回View对象泛型
     * @return
     */
    public static <T extends View> T getTargetView(View rootView, Class<? extends View> targetClass, int id) {
        if (rootView != null) {
            //满足View id是目标id
            if (rootView.getId() == id) {
                if (targetClass == null || targetClass == rootView.getClass()) {
                    return (T) rootView;
                }
                //满足View class是目标class
            } else if (targetClass == rootView.getClass()) {
                if (id == View.NO_ID || id == rootView.getId()) {
                    return (T) rootView;
                }
            }
            //以上条件都不满足才进行
            if (rootView instanceof ViewGroup) {
                ViewGroup contentView = (ViewGroup) rootView;
                int size = contentView.getChildCount();
                //循环遍历所有子View
                for (int i = 0; i < size; i++) {
                    View childView = contentView.getChildAt(i);
                    //递归查找
                    View target = getTargetView(childView, targetClass, id);
                    //如果找到了返回;如果没有找到继续当前的循环
                    if (target != null) {
                        return (T) target;
                    }
                }
            }
        }
        //最后都没找到,即不存在(该类型/该id)的View
        return null;
    }

    /**
     * 查找目标ViewGroup里第一个目标View类型的View
     *
     * @param rootView
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T extends View> T getTargetView(View rootView, Class<? extends View> targetClass) {
        return getTargetView(rootView, targetClass, View.NO_ID);
    }

    /**
     * 查找某个id的view
     *
     * @param rootView
     * @param id
     * @param <T>
     * @return
     */
    public static <T extends View> T getTargetView(View rootView, int id) {
        return getTargetView(rootView, null, id);
    }
}