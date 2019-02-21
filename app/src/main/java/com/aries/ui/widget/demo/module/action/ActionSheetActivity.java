package com.aries.ui.widget.demo.module.action;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.SizeUtil;

import androidx.appcompat.widget.SwitchCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created: AriesHoo on 2017/7/18 15:07
 * E-Mail: AriesHoo@126.com
 * Function: UIActionSheetDialog示例
 * Description:
 */
public class ActionSheetActivity extends BaseActivity {

    @BindView(R.id.titleBar) TitleBarView titleBar;
    @BindView(R.id.sBtn_marginActionSheet) SwitchCompat sBtnMargin;
    @BindView(R.id.sBtn_titleActionSheet) SwitchCompat sBtnTitle;
    @BindView(R.id.sBtn_titleColorActionSheet) SwitchCompat sBtnTitleColor;
    @BindView(R.id.sBtn_itemColorActionSheet) SwitchCompat sBtnItemColor;
    @BindView(R.id.sBtn_cancelColorActionSheet) SwitchCompat sBtnCancelColor;
    @BindView(R.id.sBtn_backActionSheet) SwitchCompat sBtnBack;
    @BindView(R.id.rtv_showActionSheet) RadiusTextView rtvShow;
    @BindView(R.id.rtv_showGridActionSheet) RadiusTextView rtvShowGrid;

    private boolean isShowMargin = true;
    private boolean isShowTitle = true;

    private boolean isDefaultTitleColor = true;
    private boolean isDefaultItemColor = true;
    private boolean isDefaultCancelColor = true;

    private boolean isBackDim = true;

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText(UIActionSheetDialog.class.getSimpleName());
    }

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setBottomView(rtvShowGrid, true);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_action_sheet;
    }

    @Override
    protected void initView(Bundle bundle) {
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
        sBtnMargin.setChecked(true);
        sBtnTitleColor.setChecked(true);
        sBtnItemColor.setChecked(true);
        sBtnCancelColor.setChecked(true);
        sBtnBack.setChecked(true);
    }


    private UIActionSheetDialog.OnItemClickListener mOnItemClickListener = new UIActionSheetDialog.OnItemClickListener() {
        @Override
        public void onClick(BasisDialog dialog, View itemView, int position) {
            Toast.makeText(ActionSheetActivity.this, "item position:" + position, Toast.LENGTH_SHORT).show();
        }
    };

    @OnClick({R.id.sBtn_titleActionSheet, R.id.rtv_showActionSheet, R.id.rtv_showIconActionSheet
            , R.id.rtv_showIOSActionSheet, R.id.rtv_showWeiXinActionSheet, R.id.rtv_showGridActionSheet})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sBtn_titleActionSheet:
                break;
            case R.id.rtv_showActionSheet:
                new UIActionSheetDialog.ListSheetBuilder(this)
                        .addItems(R.array.arrays_items_action)
                        .setItemsTextColorResource(isDefaultItemColor ? R.color.colorActionSheetNormalItemText : android.R.color.holo_purple)
                        .setTitle(isShowTitle ? "标题" : null)
                        .setCancel(R.string.cancel)
                        .setItemsMinHeight(200)
                        .setCancelMarginTop(SizeUtil.dp2px(isShowMargin ? 8 : 0))
                        .setCancelTextColorResource(isDefaultCancelColor ? R.color.colorActionSheetNormalItemText : android.R.color.darker_gray)
                        .setOnItemClickListener(mOnItemClickListener)
                        .create().setDimAmount(isBackDim ? 0.6f : 0f).show();
                break;
            case R.id.rtv_showIOSActionSheet:
                new UIActionSheetDialog.ListIOSBuilder(this)
                        .addItems(R.array.arrays_items_action)
                        .setItemsTextColorResource(isDefaultItemColor ? R.color.colorActionSheetItemText : android.R.color.holo_purple)
                        .setTitle(isShowTitle ? "标题" : null)
                        .setCancel(R.string.cancel)
                        .setCancelMarginTop(SizeUtil.dp2px(isShowMargin ? 8 : 0))
                        .setCancelTextColorResource(isDefaultCancelColor ? R.color.colorActionSheetItemText : android.R.color.darker_gray)
                        .setOnItemClickListener(mOnItemClickListener)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .setAlpha(1f)
                        .show();
                break;
            case R.id.rtv_showWeiXinActionSheet:
                new UIActionSheetDialog.ListWeChatBuilder(this)
                        .addItems(R.array.arrays_items_action)
                        .setItemsTextColorResource(isDefaultItemColor ? R.color.colorActionSheetWeiXinText : android.R.color.holo_purple)
//                        .setTitle(isShowTitle ? "标题" : null)
                        .setCancelTextColorResource(isDefaultCancelColor ? R.color.colorActionSheetWeiXinText : android.R.color.darker_gray)
                        .setOnItemClickListener(mOnItemClickListener)
                        .create().setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showIconActionSheet:
                UIActionSheetDialog sheetDialog = new UIActionSheetDialog.ListWeChatBuilder(this)
                        .setBackgroundColor(Color.WHITE)
                        .addItem("分享微信", R.drawable.ic_more_operation_share_friend)
                        .addItem("分享朋友圈", R.drawable.ic_more_operation_share_moment)
                        .addItem("分享微博", R.drawable.ic_more_operation_share_weibo)
                        .addItem("分享短信", R.drawable.ic_more_operation_share_chat)
                        //设置手指拖拽
                        .setDragEnable(true)
                        .setTextDrawablePadding(SizeUtil.dp2px(28))
                        .create();
                sheetDialog.getListView().setPadding(0, SizeUtil.dp2px(10), 0, SizeUtil.dp2px(10));
                sheetDialog.show();
                break;
            case R.id.rtv_showGridActionSheet:
                UIActionSheetDialog dialog = new UIActionSheetDialog.GridBuilder(this)
                        .addItem("分享微信", R.drawable.ic_more_operation_share_friend)
                        .addItem("分享朋友圈", R.drawable.ic_more_operation_share_moment)
                        .addItem("分享微博", R.drawable.ic_more_operation_share_weibo)
                        .addItem("分享短信", R.drawable.ic_more_operation_share_chat)
                        .addItem("保存本地", R.drawable.ic_more_operation_save)
                        .setItemsTextColorResource(isDefaultItemColor ? R.color.colorActionSheetNormalItemText : android.R.color.holo_green_dark)
                        .setTitle(isShowTitle ? "请选择分享平台" : "")
                        .setCancel(R.string.cancel)
                        .setCancelMarginTop(SizeUtil.dp2px(isShowMargin ? 8 : 0))
                        .setNumColumns(3)
                        .setItemsTextSize(12)
                        .setItemsImageWidth(SizeUtil.dp2px(60))
                        .setItemsImageHeight(SizeUtil.dp2px(60))
                        .setItemsClickBackgroundEnable(false)
                        .setOnItemClickListener(new UIActionSheetDialog.OnItemClickListener() {
                            @Override
                            public void onClick(BasisDialog dialog, View itemView, int position) {
                                if (itemView instanceof TextView) {
                                    Toast.makeText(ActionSheetActivity.this, ((TextView) itemView).getText(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f);
                dialog.show();
                break;
        }
    }
}
