package com.aries.ui.widget.demo;

import android.app.Application;
import android.content.Context;

/**
 * Created: AriesHoo on 2017/6/29 17:00
 * Function:
 * Desc:
 */
public class App extends Application {

    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

}
