package com.aries.ui.widget.demo.module.title;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.helper.status.StatusViewHelper;
import com.aries.ui.widget.demo.BuildConfig;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.adapter.TitleAdapter;
import com.aries.ui.widget.demo.base.BaseRecycleActivity;
import com.aries.ui.widget.demo.entity.TitleEntity;
import com.aries.ui.widget.demo.entity.WidgetEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2018/2/7/007 17:36
 * E-Mail: AriesHoo@126.com
 * Function:  TitleBarView+CollapsingTitleBarLayout 类Toolbar+CollapsingToolbarLayout
 * Description:
 */
public class ToolWithCollapsingLayoutActivity extends BaseRecycleActivity<WidgetEntity> {
    private BaseQuickAdapter mAdapter;

    @BindView(R.id.lLayout_toolBar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.toolBar) Toolbar mToolbar;

    @Override
    protected boolean setLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new TitleAdapter(mContext);
        return mAdapter;
    }

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        StatusViewHelper.with(this)
                .setControlEnable(true)
                .setLogEnable(BuildConfig.DEBUG)
                .setTransEnable(true)
                .setPlusStatusViewEnable(false)
                .setTopView(mToolbar)
                .init();
        super.beforeControlNavigation(navigationHelper);
    }

    @Override
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_tool_with_collapsing_layout;
    }

    @Override
    protected void initView(Bundle var1) {
        List<TitleEntity> list = new ArrayList<>();
        mAdapter.setNewData(list);

        mCollapsingToolbarLayout
                .setTitle("CollapsingToolbarLayout");

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void loadData(int page) {

    }
}
