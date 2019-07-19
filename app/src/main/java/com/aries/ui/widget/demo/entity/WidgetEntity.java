package com.aries.ui.widget.demo.entity;

import android.app.Activity;

/**
 * @Author: AriesHoo on 2019/7/19 9:39
 * @E-Mail: AriesHoo@126.com
 * @Function: 条目实体
 * @Description:
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
