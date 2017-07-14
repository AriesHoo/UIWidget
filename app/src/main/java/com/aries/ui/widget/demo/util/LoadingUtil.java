package com.aries.ui.widget.demo.util;

import android.content.Context;

import com.aries.ui.widget.progress.UIProgressView;

/**
 * Created: AriesHoo on 2017-02-23 16:44
 * Function: 封装UIProgressView调用
 * Desc:
 */
public class LoadingUtil {

    public static UIProgressView show(Context mContext, String msg, boolean canceledOnTouchOutside, boolean canceledKeyBack) {
        UIProgressView uiProgressView = getProgressView(mContext);
        if (msg != null && !msg.isEmpty()) {
            uiProgressView.setMessage(msg);
        }
        uiProgressView.setCancelable(canceledKeyBack);
        uiProgressView.setCanceledOnTouchOutside(canceledOnTouchOutside);
        uiProgressView.show();
        return uiProgressView;
    }

    public static UIProgressView show(Context mContext, String msg, boolean canceledOnTouchOutside) {
        return show(mContext, msg, canceledOnTouchOutside, true);
    }

    public static UIProgressView show(Context mContext, int msg) {
        return show(mContext, mContext.getString(msg), true);
    }

    public static UIProgressView show(Context mContext, String msg) {
        return show(mContext, msg, true);
    }

    public static UIProgressView show(Context mContext) {
        return show(mContext, null, true);
    }

    public static UIProgressView getProgressView(Context mContext) {

        UIProgressView uiProgressView = new UIProgressView(mContext);
        if (uiProgressView == null) {
            uiProgressView = new UIProgressView(mContext);
        }
        uiProgressView.show();
        return uiProgressView;
    }
}
