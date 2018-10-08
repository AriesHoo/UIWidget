package com.aries.ui.view.radius.delegate;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Author: AriesHoo on 2018/7/19 10:00
 * @E-Mail: AriesHoo@126.com
 * Function:TextView及EditText代理类
 * Description:
 * 1、2018-5-25 11:27:05 新增代理解决泛型链式调用BUG
 */
public class RadiusTextViewDelegate extends RadiusTextDelegate<RadiusTextViewDelegate> {

    public RadiusTextViewDelegate(TextView view, Context context, AttributeSet attrs) {
        super(view, context, attrs);
    }

    @Override
    public void init() {
        super.init();
    }
}
