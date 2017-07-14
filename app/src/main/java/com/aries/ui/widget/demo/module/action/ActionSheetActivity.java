package com.aries.ui.widget.demo.module.action;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.aries.ui.widget.action.sheet.UIActionSheetView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

public class ActionSheetActivity extends BaseActivity {


    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("UIActionSheetView");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_action_sheet;
    }

    @Override
    protected void initView(Bundle var1) {
        findViewById(R.id.btn_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIActionSheetView(ActionSheetActivity.this, UIActionSheetView.STYLE_NORMAL)
                        .setCancelMessage("取消")
                        .setCancelMessageMargin(0, 0, 0, 0)
                        .setItems(R.array.arrays_items_action, onActionSheetItemLister)
                        .setItemsTextColor(Color.BLACK)
                        .show();
            }
        });
        findViewById(R.id.btn_normalColor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIActionSheetView(ActionSheetActivity.this)
                        .setTitle("UIActionSheetView-变色")
                        .setTitleColorResource(R.color.colorItems)
                        .setCancelMessage("取消")
                        .setCancelColorResource(R.color.colorAccent)
                        .setItems(R.array.arrays_items_action, onActionSheetItemLister)
                        .setItemsTextColorResource(R.color.colorAccent)
                        .setItemTextColor(2, Color.parseColor("#000000"))
                        .setItemTextColorResource(5, R.color.colorItems)
                        .show();
            }
        });
        findViewById(R.id.btn_noTitle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UIActionSheetView(ActionSheetActivity.this)
                        .setCancelMessage("取消")
                        .setItems(R.array.arrays_items_action, onActionSheetItemLister)
                        .show();
            }
        });
    }


    private UIActionSheetView.OnSheetItemListener onActionSheetItemLister = new UIActionSheetView.OnSheetItemListener() {
        @Override
        public void onClick(int item) {
            Toast.makeText(ActionSheetActivity.this, "item position:" + item, Toast.LENGTH_SHORT).show();
        }
    };
}
