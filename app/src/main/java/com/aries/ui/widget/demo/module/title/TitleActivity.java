package com.aries.ui.widget.demo.module.title;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.aries.ui.view.title.StatusBarUtil;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.adapter.TitleAdapter;
import com.aries.ui.widget.demo.base.BaseRecycleActivity;
import com.aries.ui.widget.demo.entity.TitleEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017-02-09 10:24
 * Function: 演示TitleBarView常见用法
 * Desc:
 */
public class TitleActivity extends BaseRecycleActivity<TitleEntity> {

    @BindView(R.id.drawer_root) DrawerLayout drawerRoot;
    @BindView(R.id.sv_slide) ScrollView svSlide;
    private SwitchCompat sBtnImmersible;
    private SwitchCompat sBtnLight;
    private SwitchCompat sBtnLine;
    private boolean isImmersible = true;
    private boolean isLight = true;
    private boolean canImmersible = true;
    private boolean canLight = true;

    private BaseQuickAdapter mAdapter;
    protected View vHeader;

    @Override
    protected boolean setLoadMore() {
        return false;
    }

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("主标题");
        titleBar.setTitleSubText("副标题");
        titleBar.setRightTextDrawable(isWhite ? R.drawable.ic_menu : R.drawable.ic_menu_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerRoot.openDrawer(svSlide);
            }
        });
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
        super.initView(bundle);
        vHeader = View.inflate(mContext, R.layout.layout_title_header, null);
        sBtnImmersible = (SwitchCompat) vHeader.findViewById(R.id.sBtn_immersible);
        sBtnLight = (SwitchCompat) vHeader.findViewById(R.id.sBtn_light);
        sBtnLine = (SwitchCompat) vHeader.findViewById(R.id.sBtn_line);
        svSlide.setFitsSystemWindows(true);//drawer视图设置
        canImmersible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        canLight = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        initView();
        initData();
    }

    private void initData() {
        List<TitleEntity> list = new ArrayList<>();
        list.add(new TitleEntity("TitleBarView与底部EditText结合", "点击查看示例", TitleEditActivity.class));
        list.add(new TitleEntity("白色主题", "点击切换白色主题", android.R.color.white));
        list.add(new TitleEntity("红色主题", "点击切换红色主题", android.R.color.holo_red_light));
        list.add(new TitleEntity("橙色主题", "点击切换橙色主题", android.R.color.holo_orange_light));
        list.add(new TitleEntity("绿色主题", "点击切换绿色主题", android.R.color.holo_green_light));
        list.add(new TitleEntity("蓝色主题", "点击切换蓝色主题", android.R.color.holo_blue_light));
        list.add(new TitleEntity("紫色主题", "点击切换紫色主题", android.R.color.holo_purple));
        mAdapter.setHeaderView(vHeader);
        mAdapter.setNewData(list);
    }

    private void initView() {
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
            sBtnLight.setText("5.0以下不支持全透明");
        }
        sBtnImmersible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isImmersible = isChecked;
                titleBar.setImmersible(mContext, isImmersible, isLight);//一般情况下使用
                sBtnImmersible.setText(isChecked ? "沉浸" : "不沉浸");
                if (isImmersible && type > 0) {
                    initTitle();
                }
                if (!isImmersible) {
                    sBtnLight.setChecked(false);
                }
            }
        });
        sBtnLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLight = isChecked;
                if (isLight) {
                    sBtnImmersible.setChecked(true);
                }
                titleBar.setImmersible(mContext, isImmersible, isLight);//一般情况下使用
                sBtnLight.setText(isChecked ? "5.0以上全透明" : "5.0以上半透明");
                if (isImmersible && type > 0) {
                    initTitle();
                }
            }
        });
        sBtnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                titleBar.setDividerVisible(isChecked);
                sBtnLine.setText(isChecked ? "显示下划线" : "隐藏下划线");
            }
        });
        drawerRoot.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (type > 0 && isImmersible) {
                    StatusBarUtil.StatusBarLightMode(mContext);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (type > 0 && isImmersible) {
                    if (isWhite) {
                        StatusBarUtil.StatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.StatusBarDarkMode(mContext);
                    }
                }
            }
        });
        if (canLight && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sBtnLight.setChecked(false);
        }
    }

    @Override
    protected void onItemClicked(BaseQuickAdapter<TitleEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        TitleEntity entity = adapter.getItem(position);
        if (entity.colorRes != 0) {
            isWhite = entity.colorRes == android.R.color.white;
            titleBar.setBackgroundResource(entity.colorRes);
            titleBar.setLeftTextDrawable(isWhite ? R.drawable.ic_arrow_left : R.drawable.ic_arrow_back_white);
            titleBar.setRightTextDrawable(isWhite ? R.drawable.ic_menu : R.drawable.ic_menu_white);
            titleBar.setTitleMainTextColor(isWhite ? getResources().getColor(R.color.colorTextBlack) : Color.WHITE);
            titleBar.setTitleSubTextColor(isWhite ? getResources().getColor(R.color.colorTextBlack) : Color.WHITE);
            if (isImmersible) {
                initTitle();
            }
            if (type > 0 && isImmersible) {
                if (isWhite) {
                    StatusBarUtil.StatusBarLightMode(mContext);
                } else {
                    StatusBarUtil.StatusBarDarkMode(mContext);
                }
            }
        } else if (entity.activity != null) {
            startActivity(entity.activity);
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
