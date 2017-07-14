package com.aries.ui.widget.demo.entity;

import android.app.Activity;

/**
 * Created: AriesHoo on 2017/7/14 9:45
 * Function: 应用实体
 * Desc:
 */
public class WidgetEntity {

    public String title;
    public String content;
    public Class<? extends Activity> activity;

    public WidgetEntity(String title, String content, Class<? extends Activity> activity) {
        this.title = title;
        this.content = content;
        this.activity = activity;
    }
}
