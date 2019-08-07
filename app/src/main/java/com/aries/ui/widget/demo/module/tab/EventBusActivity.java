package com.aries.ui.widget.demo.module.tab;

import android.os.Bundle;
import android.view.View;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import org.simple.eventbus.EventBus;

/**
 * @Author: AriesHoo on 2019/4/17 10:35
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class EventBusActivity extends BaseActivity {

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_event_bus;
    }

    @Override
    protected void initView(Bundle var1) {
        findViewById(R.id.btn_sendEventBus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(2, "switchTab");
                finish();
            }
        });
    }
}
