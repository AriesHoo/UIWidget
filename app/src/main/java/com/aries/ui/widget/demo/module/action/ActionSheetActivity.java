package com.aries.ui.widget.demo.module.action;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.action.sheet.UIActionSheetDialog;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.base.BaseActivity;
import com.aries.ui.widget.demo.util.SizeUtil;
import com.aries.ui.widget.i.NavigationBarControl;

import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: AriesHoo on 2019/4/10 15:58
 * @E-Mail: AriesHoo@126.com
 * @Function: UIActionSheetDialog示例
 * @Description:
 */
public class ActionSheetActivity extends BaseActivity implements NavigationBarControl {

    @BindView(R.id.titleBar) TitleBarView titleBar;
    @BindView(R.id.sBtn_marginActionSheet) SwitchCompat sBtnMargin;
    @BindView(R.id.sBtn_titleActionSheet) SwitchCompat sBtnTitle;
    @BindView(R.id.sBtn_titleColorActionSheet) SwitchCompat sBtnTitleColor;
    @BindView(R.id.sBtn_itemColorActionSheet) SwitchCompat sBtnItemColor;
    @BindView(R.id.sBtn_cancelColorActionSheet) SwitchCompat sBtnCancelColor;
    @BindView(R.id.sBtn_backActionSheet) SwitchCompat sBtnBack;
    @BindView(R.id.sBtn_navigationActionSheet) SwitchCompat sBtnNavigation;
    @BindView(R.id.sBtn_navigationPlusActionSheet) SwitchCompat sBtnNavigationPlus;
    @BindView(R.id.rtv_showActionSheet) RadiusTextView rtvShow;
    @BindView(R.id.rtv_showGridActionSheet) RadiusTextView rtvShowGrid;
//    private String mFilePath = FastFileUtil.getCacheDir();
//    private String mFormat = "保存图片<br><small><font color='#2394FE'>图片文件夹路径:%1s</font></small>";

    private boolean isShowMargin = true;
    private boolean isShowTitle = true;

    private boolean isDefaultTitleColor = true;
    private boolean isDefaultItemColor = true;
    private boolean isDefaultCancelColor = true;

    private boolean isBackDim = true;
    private boolean isNavigation = true;
    private boolean isNavigationPlus = true;
    private UIActionSheetDialog mDialog;

    @Override
    protected void setTitleBar() {
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
        sBtnNavigation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNavigation = isChecked;
                sBtnNavigation.setText(isNavigation ? "控制底部虚拟导航栏" : "不控制底部虚拟导航栏");
            }
        });
        sBtnNavigationPlus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isNavigationPlus = isChecked;
                sBtnNavigationPlus.setText(isNavigation ? "底部虚拟导航栏增加View占位" : "底部虚拟导航栏不增加View占位");
            }
        });
        sBtnTitle.setChecked(true);
        sBtnMargin.setChecked(true);
        sBtnTitleColor.setChecked(true);
        sBtnItemColor.setChecked(true);
        sBtnCancelColor.setChecked(true);
        sBtnBack.setChecked(true);
        sBtnNavigation.setChecked(true);
        sBtnNavigationPlus.setChecked(true);
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
                        .setNavigationBarControl(isNavigation ? ActionSheetActivity.this : null)
                        .setCancelMarginTop(SizeUtil.dp2px(isShowMargin ? 8 : 0))
                        .setCancelTextColorResource(isDefaultCancelColor ? R.color.colorActionSheetNormalItemText : android.R.color.darker_gray)
                        .setOnItemClickListener(mOnItemClickListener)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f)
                        .show();
                break;
            case R.id.rtv_showIOSActionSheet:
                new UIActionSheetDialog.ListIOSBuilder(this)
                        .setNavigationBarControl(isNavigation ? ActionSheetActivity.this : null)
//                        .addItem(Html.fromHtml(String.format(mFormat, mFilePath)))
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
                        .setNavigationBarControl(isNavigation ? ActionSheetActivity.this : null)
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
                        .setNavigationBarControl(isNavigation ? ActionSheetActivity.this : null)
                        //设置手指拖拽
                        .setDragEnable(true)
                        .setTextDrawablePadding(SizeUtil.dp2px(28))
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f);
                sheetDialog.getListView().setPadding(0, SizeUtil.dp2px(10), 0, SizeUtil.dp2px(10));
                sheetDialog.show();
                break;
            case R.id.rtv_showGridActionSheet:
                mDialog = new UIActionSheetDialog.GridBuilder(this)
                        .addItem("分享微信", R.drawable.ic_more_operation_share_friend)
                        .addItem("分享朋友圈", R.drawable.ic_more_operation_share_moment)
                        .addItem("分享微博", R.drawable.ic_more_operation_share_weibo)
                        .addItem("分享短信", R.drawable.ic_more_operation_share_chat)
                        .addItem("保存本地", R.drawable.ic_more_operation_save)
                        .setItemsTextColorResource(isDefaultItemColor ? R.color.colorActionSheetNormalItemText : android.R.color.holo_green_dark)
                        .setTitle(isShowTitle ? "请选择分享平台" : "")
                        .setCancel(R.string.cancel)
                        .setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                StatusBarUtil.setStatusBarDarkMode(mDialog.getWindow());
                            }
                        })
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
                        .setNavigationBarControl(isNavigation ? ActionSheetActivity.this : null)
                        .create()
                        .setDimAmount(isBackDim ? 0.6f : 0f);
                mDialog.show();
                break;
        }
    }

    @Override
    public boolean setNavigationBar(Dialog dialog, NavigationViewHelper helper, View bottomView) {
        Drawable drawableTop = ContextCompat.getDrawable(mContext, R.color.colorLineGray);
        DrawableUtil.setDrawableWidthHeight(drawableTop, SizeUtil.getScreenWidth(), SizeUtil.dp2px(0.5f));
        helper.setNavigationViewDrawableTop(drawableTop)
                .setPlusNavigationViewEnable(isNavigationPlus)
                .setNavigationViewColor(Color.argb(isTrans() ? 0 : 102, 0, 0, 0))
                .setNavigationLayoutColor(Color.WHITE);
        return isNavigation;
    }
}
