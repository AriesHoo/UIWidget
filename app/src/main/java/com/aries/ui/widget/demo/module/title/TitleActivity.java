package com.aries.ui.widget.demo.module.title;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.RomUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.adapter.TitleAdapter;
import com.aries.ui.widget.demo.base.BaseRecycleActivity;
import com.aries.ui.widget.demo.entity.DrawerEntity;
import com.aries.ui.widget.demo.entity.TitleEntity;
import com.aries.ui.widget.demo.helper.DrawerHelper;
import com.aries.ui.widget.demo.manager.GlideManager;
import com.aries.ui.widget.demo.util.ViewUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * @Author: AriesHoo on 2018/11/16 11:31
 * @E-Mail: AriesHoo@126.com
 * @Function: 演示TitleBarView常见用法
 * @Description:
 */
public class TitleActivity extends BaseRecycleActivity<TitleEntity> {

    @BindView(R.id.drawer_root) DrawerLayout drawerRoot;
    @BindView(R.id.sv_slide) ScrollView svSlide;
    @BindView(R.id.titleBarDrawer) TitleBarView titleBarDrawer;
    @BindView(R.id.fLayout_drawer) FrameLayout fLayoutDrawer;
    @BindView(R.id.rv_contentDrawer) RecyclerView mRecyclerViewDrawer;
    @BindView(R.id.iv_headDrawer) ImageView ivHead;
    private SwitchCompat sBtnImmersible;
    private SwitchCompat sBtnLight;
    private SwitchCompat sBtnLine;
    private LinearLayout lLayoutAlpha;
    private SeekBar sBarAlpha;
    private TextView tvStatusAlpha;

    private boolean isImmersible = true;
    private boolean isLight = true;
    private boolean canImmersible = true;
    private boolean canLight = true;

    private BaseQuickAdapter mAdapter;
    protected View vHeader;
    private int mAlpha = 102;
    private List<TitleEntity> mListTheme = new ArrayList<>();

    @Override
    protected boolean setLoadMore() {
        return false;
    }

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setBottomView(mRecyclerView)
                .setPlusNavigationViewEnable(!isDarkIcon());
        if (isDarkIcon()) {
            mRecyclerView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("主标题")
                .setTitleSubText(getSubText())
//                .setLeftTextDrawableWidth(24)
//                .setLeftTextDrawableHeight(24)
                .setRightTextDrawable(R.drawable.ic_menu)
//                .setRightTextDrawableWidth(10)
//                .setRightText("菜单")
//                .setRightTextDrawableHeight(10)
                .setOnRightTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawerRoot.openDrawer(svSlide);
                    }
                });
    }

    private String getSubText() {
        String text = "Android" + Build.VERSION.RELEASE;
        if (RomUtil.isMIUI()) {
            text += "-MIUI" + RomUtil.getMIUIVersion();
        } else if (RomUtil.isFlyme()) {
            text += "-Flyme" + RomUtil.getFlymeVersionCode();
        } else if (RomUtil.isEMUI()) {
            text += "-EMUI" + RomUtil.getEMUIVersion();
        } else if (RomUtil.isVIVO()) {
            text += "-VIVO" + RomUtil.getVIVOVersion();
        } else if (RomUtil.isOPPO()) {
            text += "-OPPO" + RomUtil.getOPPOVersion();
        } else if (RomUtil.isSmartisan()) {
            text += "-SMARTISAN" + RomUtil.getSmartisanVersion();
        } else if (RomUtil.isQiKu()) {
            text += "-QIKU" + RomUtil.getQiKuVersion();
        }
        return text;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title;
    }

    @Override
    protected BaseQuickAdapter<TitleEntity, BaseViewHolder> getAdapter() {
        mAdapter = new TitleAdapter(mContext);
        return mAdapter;
    }

    @Override
    protected void loadData(int page) {

    }

    @Override
    protected void initView(Bundle bundle) {
        //背景高亮
        drawerRoot.setScrimColor(Color.argb(10, 0, 0, 0));
        //海拔高度
        drawerRoot.setDrawerElevation(40);
        GlideManager.loadCircleImg("https://avatars3.githubusercontent.com/u/19605922?v=4&s=460", ivHead);
        titleBarDrawer.setImmersible(mContext, isImmersible, isLight);
        vHeader = View.inflate(mContext, R.layout.layout_title_header, null);
        sBtnImmersible = vHeader.findViewById(R.id.sBtn_immersible);
        sBtnLight = vHeader.findViewById(R.id.sBtn_light);
        sBtnLine = vHeader.findViewById(R.id.sBtn_line);
        lLayoutAlpha = vHeader.findViewById(R.id.lLayout_alpha);
        sBarAlpha = vHeader.findViewById(R.id.sBar_alpha);
        tvStatusAlpha = vHeader.findViewById(R.id.tv_statusAlpha);
        initView();
        setDrawerList();
        initData();
    }

    private void setDrawerList() {
        List<DrawerEntity> listDrawer = new ArrayList<>();
        listDrawer.add(new DrawerEntity("AriesHoo", "点击跳转GitHub个人主页", "https://github.com/AriesHoo"));
        listDrawer.add(new DrawerEntity("FastLib-快速开发库", "点击跳转GitHub项目页", "https://github.com/AriesHoo/FastLib/blob/master/README.md"));
        listDrawer.add(new DrawerEntity("UIWidget-常用UI控件库", "点击跳转GitHub项目页", "https://github.com/AriesHoo/UIWidget/blob/master/README.md"));
        listDrawer.add(new DrawerEntity("TitleBarView-标题栏控件", "点击跳转GitHub项目页", "https://github.com/AriesHoo/TitleBarView/blob/master/README.md"));
        listDrawer.add(new DrawerEntity("简书-TitleBarView解析", "点击跳转简书", "http://www.jianshu.com/p/34ace867b29f"));
        DrawerHelper.getInstance().initRecyclerView(mContext, mRecyclerViewDrawer, listDrawer);
    }

    private void initData() {
        List<TitleEntity> list = new ArrayList<>();
        list.add(new TitleEntity("TitleBarView 自定义左中右View", "点击查看示例", TitleActionActivity.class));
        list.add(new TitleEntity("TitleBarView与底部EditText结合", "点击查看示例", TitleEditActivity.class));
        list.add(new TitleEntity("TitleBarView结合ConstraintLayout", "点击查看示例", TitleWithConstraintActivity.class));
        list.add(new TitleEntity("TitleBarView结合CollapsingTitleBarLayout", "点击查看示例", TitleWithCollapsingLayoutActivity.class));
        list.add(new TitleEntity("Toolbar结合CollapsingToolbarLayout", "点击查看示例", ToolWithCollapsingLayoutActivity.class));
        list.add(new TitleEntity("切换TitleBarView颜色主题", "点击切换主题", true));
        mAdapter.setHeaderView(vHeader);
        mAdapter.setNewData(list);
        ViewUtil.getInstance().setViewHeight(fLayoutDrawer, (int) (getResources().getDimension(R.dimen.dp_drawer_header)) + StatusBarUtil.getStatusBarHeight());
        mListTheme.add(new TitleEntity("白色主题", "点击切换白色主题", android.R.color.white));
        mListTheme.add(new TitleEntity("红色主题", "点击切换红色主题", android.R.color.holo_red_light));
        mListTheme.add(new TitleEntity("橙色主题", "点击切换橙色主题", android.R.color.holo_orange_light));
        mListTheme.add(new TitleEntity("绿色主题", "点击切换绿色主题", android.R.color.holo_green_light));
        mListTheme.add(new TitleEntity("蓝色主题", "点击切换蓝色主题", android.R.color.holo_blue_light));
        mListTheme.add(new TitleEntity("紫色主题", "点击切换紫色主题", android.R.color.holo_purple));
        mListTheme.add(new TitleEntity("黑色主题", "点击切换黑色主题", android.R.color.black));
    }

    private void initView() {
        canImmersible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        canLight = canImmersible;
        if (!canImmersible) {
            sBtnImmersible.setClickable(false);
            sBtnImmersible.setChecked(false);
            sBtnImmersible.setText("4.4以下不支持沉浸状态栏");
            sBtnLight.setClickable(false);
            sBtnLight.setChecked(false);
        }
        if (!canLight) {
            sBtnLight.setClickable(false);
            sBtnLight.setChecked(false);
            sBtnLight.setText("4.4以下不支持全透明");
            lLayoutAlpha.setVisibility(View.GONE);
        }
        sBarAlpha.setMax(255);
        sBtnImmersible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isImmersible = isChecked;
                titleBar.setStatusAlpha(isImmersible ? 0 : 255);
                titleBarDrawer.setStatusAlpha(isImmersible ? 0 : 255);
                titleBarDrawer.setImmersible(mContext, isImmersible, isLight);
                sBtnImmersible.setText(isChecked ? "沉浸" : "不沉浸");
                if (!isImmersible) {
                    sBtnLight.setChecked(false);
                    sBarAlpha.setProgress(255);
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.setStatusBarDarkMode(mContext);
                    }
                }
            }
        });
        sBtnLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLight = isChecked;
                int alpha = isLight ? 0 : 102;
                sBtnImmersible.setChecked(isLight);
                sBarAlpha.setProgress(alpha);
                titleBar.setStatusAlpha(alpha);
                titleBarDrawer.setStatusAlpha(alpha);
                if (!isImmersible) {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.setStatusBarDarkMode(mContext);
                    }
                }
                sBtnLight.setText(isChecked ? "状态栏全透明" : "状态栏半透明");
            }
        });
        sBtnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                titleBar.setDividerVisible(isChecked);
                sBtnLine.setText(isChecked ? "显示下划线" : "隐藏下划线");
            }
        });
        sBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStatusAlpha.setText(progress + "");
                mAlpha = progress;
                if (canImmersible) {
                    sBtnImmersible.setChecked(mAlpha < 230);
                }
                if (canLight) {
                    sBtnLight.setChecked(mAlpha == 0);
                }
                titleBar.setStatusAlpha(mAlpha);
                if (mAlpha > 225 && isWhite) {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
//        drawerRoot.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
//            @Override
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                titleBar.setHeight(SizeUtil.dp2px(60));
//            }
//
//            @Override
//            public void onDrawerClosed(View drawerView) {
//                super.onDrawerClosed(drawerView);
//                titleBar.setHeight(SizeUtil.dp2px(48));
//            }
//        });
        if (canLight && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sBtnLight.setChecked(false);
            sBarAlpha.setProgress(TitleBarView.DEFAULT_STATUS_BAR_ALPHA);
        } else {
            sBarAlpha.setProgress(0);
        }
        sBtnLine.setChecked(true);
    }

    @Override
    protected void onItemClicked(BaseQuickAdapter<TitleEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        TitleEntity entity = adapter.getItem(position);
        if (entity.colorRes != 0) {
            isWhite = entity.colorRes == android.R.color.white;
            titleBar.setBgResource(entity.colorRes)
                    .setLeftTextDrawableTintResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                    .setRightTextDrawableTintResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                    .setTitleMainTextColorResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                    .setTitleSubTextColorResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite);
            if (type > 0 && isImmersible) {
                if (isWhite) {
                    StatusBarUtil.setStatusBarLightMode(mContext);
                } else {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                }
            }
        } else if (entity.activity != null) {
            startActivity(entity.activity);
        } else if (entity.dialog) {
            CharSequence[] listItem = new CharSequence[mListTheme.size()];
            for (int i = 0; i < mListTheme.size(); i++) {
                listItem[i] = mListTheme.get(i).title;
            }
            new AlertDialog.Builder(mContext)
                    .setTitle("切换TitleBarView颜色主题")
                    .setItems(listItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onItemClick(mListTheme.get(which));
                        }
                    })
                    .create()
                    .show();
        }
    }

    private void onItemClick(TitleEntity item) {
        isWhite = item.colorRes == android.R.color.white;
        titleBar.setBgResource(item.colorRes)
                .setLeftTextDrawableTintResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                .setRightTextDrawableTintResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                .setTitleMainTextColorResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite)
                .setTitleSubTextColorResource(isWhite ? R.color.colorTextBlack : R.color.colorWhite);
        if (type > 0 && isImmersible) {
            if (isWhite) {
                StatusBarUtil.setStatusBarLightMode(mContext);
            } else {
                StatusBarUtil.setStatusBarDarkMode(mContext);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerRoot.isDrawerOpen(svSlide)) {
            drawerRoot.closeDrawer(svSlide);
        } else {
            super.onBackPressed();
        }
    }
}
