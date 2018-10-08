package com.aries.ui.widget.demo.module.title;

import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.CollapsingTitleBarLayout;
import com.aries.ui.view.title.TitleBarView;
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
 * @Author: AriesHoo on 2018/8/10 18:11
 * @E-Mail: AriesHoo@126.com
 * Function: TitleBarView+CollapsingTitleBarLayout 类Toolbar+CollapsingToolbarLayout
 * Description:
 */
public class TitleWithCollapsingLayoutActivity extends BaseRecycleActivity<WidgetEntity> {
    private BaseQuickAdapter mAdapter;


    @BindView(R.id.titleBar_collapsing) TitleBarView mTitleBarView;
    @BindView(R.id.lLayout_titleBar) CollapsingTitleBarLayout mCollapsingTitleBarLayout;

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
    protected void beforeInitView() {
        super.beforeInitView();
    }

    @Override
    protected void setTitleBar() {
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_with_collapsing_layout;
    }

    @Override
    protected void initView(Bundle var1) {
        List<TitleEntity> list = new ArrayList<>();
        mAdapter.setNewData(list);
        mCollapsingTitleBarLayout
                .setTitle("CollapsingTitleBarLayout")
                .setTitleEnabled(true);
        mTitleBarView.setOnLeftTextClickListener(new View.OnClickListener() {
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
