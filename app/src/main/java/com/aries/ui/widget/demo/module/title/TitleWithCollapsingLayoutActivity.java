package com.aries.ui.widget.demo.module.title;

import android.os.Bundle;

import com.aries.ui.view.title.CollapsingTitleBarLayout;
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
public class TitleWithCollapsingLayoutActivity extends BaseRecycleActivity<WidgetEntity> {
    private BaseQuickAdapter mAdapter;

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
    protected void setTitleBar() {

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_title_with_collapsing_layout;
    }

    @Override
    protected void initView(Bundle var1) {
        List<TitleEntity> list = new ArrayList<>();
        list.add(new TitleEntity("TitleBarView与底部EditText结合", "点击查看示例", TitleEditActivity.class));
        list.add(new TitleEntity("TitleBarView结合ConstraintLayout", "点击查看示例", TitleWithConstraint.class));
        list.add(new TitleEntity("白色主题", "点击切换白色主题", android.R.color.white));
        list.add(new TitleEntity("红色主题", "点击切换红色主题", android.R.color.holo_red_light));
        list.add(new TitleEntity("橙色主题", "点击切换橙色主题", android.R.color.holo_orange_light));
        list.add(new TitleEntity("绿色主题", "点击切换绿色主题", android.R.color.holo_green_light));
        list.add(new TitleEntity("蓝色主题", "点击切换蓝色主题", android.R.color.holo_blue_light));
        list.add(new TitleEntity("紫色主题", "点击切换紫色主题", android.R.color.holo_purple));
        mAdapter.setNewData(list);

        mCollapsingTitleBarLayout
                .setTitle((this.getClass()).getSimpleName())
                .setTitleEnabled(true);
    }

    @Override
    protected void loadData(int page) {

    }
}
