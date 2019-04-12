package com.aries.ui.widget.demo.entity;

import android.app.Activity;

/**
 * @Author: AriesHoo on 2019/4/11 14:17
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class TitleEntity {

    public String title;
    public String content;
    public Class<? extends Activity> activity;
    public int colorRes;
    public boolean dialog;

    public TitleEntity(String title, String content, Class<? extends Activity> activity) {
        this.title = title;
        this.content = content;
        this.activity = activity;
    }

    public TitleEntity(String title, String content, int colorRes) {
        this.title = title;
        this.content = content;
        this.colorRes = colorRes;
    }

    public TitleEntity(String title, String content, boolean dialog) {
        this.title = title;
        this.content = content;
        this.dialog = dialog;
    }
}
