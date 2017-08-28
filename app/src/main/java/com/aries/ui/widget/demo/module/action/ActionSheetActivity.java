package com.aries.ui.widget.demo.module.action;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.action.sheet.UIActionSheetView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/18 15:07
 * Function: UIActionSheetView示例
 * Desc:
 */
public class ActionSheetActivity extends BaseActivity {

    @BindView(R.id.titleBar) TitleBarView titleBar;
    @BindView(R.id.sBtn_styleActionSheet) SwitchCompat sBtnStyle;
    @BindView(R.id.sBtn_marginActionSheet) SwitchCompat sBtnMargin;
    @BindView(R.id.sBtn_titleActionSheet) SwitchCompat sBtnTitle;
    @BindView(R.id.sBtn_titleColorActionSheet) SwitchCompat sBtnTitleColor;
    @BindView(R.id.sBtn_itemColorActionSheet) SwitchCompat sBtnItemColor;
    @BindView(R.id.sBtn_cancelColorActionSheet) SwitchCompat sBtnCancelColor;
    @BindView(R.id.sBtn_backActionSheet) SwitchCompat sBtnBack;
    @BindView(R.id.rtv_showActionSheet) RadiusTextView rtvShow;

    private boolean isRoundStyle = true;
    private boolean isShowMargin = true;
    private boolean isShowTitle = true;

    private boolean isDefaultTitleColor = true;
    private boolean isDefaultItemColor = true;
    private boolean isDefaultCancelColor = true;

    private boolean isBackDim = true;

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("UIActionSheetView");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_action_sheet;
    }

    @Override
    protected void initView(Bundle bundle) {
        sBtnStyle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isRoundStyle = isChecked;
                sBtnStyle.setText(isRoundStyle ? "圆角模式" : "直角模式");
                sBtnMargin.setVisibility(isRoundStyle ? View.GONE : View.VISIBLE);
            }
        });

        sBtnMargin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowMargin = isChecked;
                sBtnMargin.setText(isShowMargin ? "cancel按钮与item之间有间隙" : "cancel按钮与item之间无间隙");
            }
        });
        sBtnTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isShowTitle = isChecked;
                sBtnTitle.setText(isShowTitle ? "显示Title" : "隐藏Title");
                sBtnTitleColor.setVisibility(isShowTitle ? View.VISIBLE : View.GONE);
            }
        });

        sBtnTitleColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultTitleColor = isChecked;
                sBtnTitleColor.setText(isDefaultTitleColor ? "默认Title文本颜色" : "自定义Title文本颜色");
            }
        });
        sBtnItemColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultItemColor = isChecked;
                sBtnItemColor.setText(isDefaultItemColor ? "默认Item文本颜色" : "自定义Item文本颜色");
            }
        });

        sBtnCancelColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultCancelColor = isChecked;
                sBtnCancelColor.setText(isDefaultCancelColor ? "默认Cancel文本颜色" : "自定义Cancel文本颜色");
            }
        });
        sBtnCancelColor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDefaultCancelColor = isChecked;
                sBtnCancelColor.setText(isDefaultCancelColor ? "默认Cancel文本颜色" : "自定义Cancel文本颜色");
            }
        });
        sBtnBack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isBackDim = isChecked;
                sBtnBack.setText(isBackDim ? "背景半透明" : "背景全透明");
            }
        });
        sBtnTitle.setChecked(true);
        sBtnStyle.setChecked(true);
        sBtnMargin.setChecked(true);
        sBtnTitleColor.setChecked(true);
        sBtnItemColor.setChecked(true);
        sBtnCancelColor.setChecked(true);
        sBtnBack.setChecked(true);
    }


    private UIActionSheetView.OnSheetItemListener onActionSheetItemLister = new UIActionSheetView.OnSheetItemListener() {
        @Override
        public void onClick(int item) {
            Toast.makeText(mContext, "item position:" + item, Toast.LENGTH_SHORT).show();
        }
    };


    @OnClick({R.id.sBtn_styleActionSheet, R.id.sBtn_titleActionSheet, R.id.rtv_showActionSheet
            , R.id.rtv_showIOSActionSheet, R.id.rtv_showWeiXinActionSheet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sBtn_styleActionSheet:
                break;
            case R.id.sBtn_titleActionSheet:
                break;
            case R.id.rtv_showActionSheet:
                showAction(UIActionSheetView.STYLE_NORMAL);
                break;
            case R.id.rtv_showIOSActionSheet:
                showAction(UIActionSheetView.STYLE_IOS);
                break;
            case R.id.rtv_showWeiXinActionSheet:
                showAction(UIActionSheetView.STYLE_WEI_XIN);
                break;
        }
    }


    private void showAction(int style) {
        UIActionSheetView action = new UIActionSheetView(mContext, style);
        action.setItems(R.array.arrays_items_action, onActionSheetItemLister);
        action.setCancelMessageMargin(0, isShowMargin ? 10 : 0, 0, 0);
        if (isShowTitle) {
            action.setTitle("Title");
        }
        if (!isDefaultTitleColor) {
            action.setTitleColor(Color.BLACK);
        }
        if (!isDefaultItemColor) {
            action.setItemsTextColor(Color.BLACK);
            action.setItemTextColor(0, Color.BLUE);
            action.setItemTextColor(2, Color.GREEN);
            action.setItemTextColor(4, Color.RED);
            action.setItemTextColor(6, Color.YELLOW);
        }
        if (!isDefaultCancelColor) {
            action.setCancelColor(Color.BLACK);
        }
        if (!isBackDim) {
            action.setDimAmount(0f);
        }
        action.show();
    }
}
