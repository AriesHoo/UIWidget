package com.aries.ui.view.tab.utils;


import android.util.DisplayMetrics;
import android.view.View;
import android.widget.RelativeLayout;

import com.aries.ui.view.tab.widget.MsgView;
/**
 * @Author: AriesHoo on 2018/12/3 13:10
 * @E-Mail: AriesHoo@126.com
 * @Function: 未读消息提示View, 显示小红点或者带有数字的红点:数字一位,圆数字两位,圆角矩形,圆角是高度的一半数字超过两位,显示99+
 * @Description:
 */
public class UnreadMsgUtils {

    public static void show(MsgView msgView, int num) {
        if (msgView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) msgView.getLayoutParams();
        DisplayMetrics dm = msgView.getResources().getDisplayMetrics();
        msgView.setVisibility(View.VISIBLE);
        //圆点,设置默认宽高
        if (num <= 0) {
            msgView.setStrokeWidth(0);
            msgView.setText("");

            lp.width = (int) (5 * dm.density);
            lp.height = (int) (5 * dm.density);
            msgView.setLayoutParams(lp);
        } else {
            lp.height = (int) (18 * dm.density);
            //圆
            if (num > 0 && num < 10) {
                lp.width = (int) (18 * dm.density);
                msgView.setText(num + "");
            } else if (num > 9 && num < 100) {
                //圆角矩形,圆角是高度的一半,设置默认padding
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                msgView.setText(num + "");
            } else {//数字超过两位,显示99+
                lp.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                msgView.setPadding((int) (6 * dm.density), 0, (int) (6 * dm.density), 0);
                msgView.setText("99+");
            }
            msgView.setLayoutParams(lp);
        }
    }

    public static void setSize(MsgView rtv, int size) {
        if (rtv == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) rtv.getLayoutParams();
        lp.width = size;
        lp.height = size;
        rtv.setLayoutParams(lp);
    }
}
