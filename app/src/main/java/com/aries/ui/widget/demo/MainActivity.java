package com.aries.ui.widget.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_alert).setOnClickListener(onClickListener);
        findViewById(R.id.btn_sheet).setOnClickListener(onClickListener);
        findViewById(R.id.btn_loading).setOnClickListener(onClickListener);
        findViewById(R.id.btn_title).setOnClickListener(onClickListener);
        findViewById(R.id.btn_radius).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_alert:
                    startActivity(MainActivity.this, AlertActivity.class);
                    break;
                case R.id.btn_sheet:
                    startActivity(MainActivity.this, ActionSheetActivity.class);
                    break;
                case R.id.btn_loading:
                    startActivity(MainActivity.this, LoadingActivity.class);
                    break;
                case R.id.btn_title:
                    startActivity(MainActivity.this, TitleActivity.class);
                    break;
                case R.id.btn_radius:
                    startActivity(MainActivity.this, RadiusActivity.class);
                    break;
            }
        }
    };

    public void startActivity(Activity mContext, Class<? extends Activity> activity, Bundle bundle) {
        if (mContext == null) {
            return;
        }
        Intent intent = new Intent(mContext, activity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);
    }

    public void startActivity(Activity mContext, Class<? extends Activity> activity) {
        startActivity(mContext, activity, null);
    }
}
