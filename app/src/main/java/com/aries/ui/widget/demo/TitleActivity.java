package com.aries.ui.widget.demo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;

/**
 * Created: AriesHoo on 2017-02-09 10:24
 * Function:
 * Desc:
 */
public class TitleActivity extends Activity {

    private TitleBarView titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);
        titleBar = (TitleBarView) findViewById(R.id.titleBar);
        titleBar.setImmersible(this, true);
        initView();
    }

    private void initView() {
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TitleActivity.this, "点击左边", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
