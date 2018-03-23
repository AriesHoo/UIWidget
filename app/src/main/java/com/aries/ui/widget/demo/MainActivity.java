package com.aries.ui.widget.demo;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.aries.ui.widget.demo.adapter.WidgetAdapter;
import com.aries.ui.widget.demo.base.BaseRecycleActivity;
import com.aries.ui.widget.demo.entity.WidgetEntity;
import com.aries.ui.widget.demo.module.action.ActionSheetActivity;
import com.aries.ui.widget.demo.module.alert.AlertActivity;
import com.aries.ui.widget.demo.module.loading.LoadingActivity;
import com.aries.ui.widget.demo.module.radius.RadiusActivity;
import com.aries.ui.widget.demo.module.title.TitleActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: AriesHoo on 2017/7/14 10:02
 * Function: 项目主页面
 * Desc:
 */
public class MainActivity extends BaseRecycleActivity<WidgetEntity> {

    protected BaseQuickAdapter mAdapter;

    @Override
    protected boolean setLoadMore() {
        return false;
    }

    @Override
    protected BaseQuickAdapter<WidgetEntity, BaseViewHolder> getAdapter() {
        mAdapter = new WidgetAdapter(mContext);
        return mAdapter;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText(R.string.app_name)
                .setTitleSubText(R.string.app_title_content)
                .setLeftTextDrawable(0)
                .setTitleSubTextMarquee(true);
        new AlertDialog.Builder(this)
                .create();
    }

    @Override
    protected void initView(Bundle bundle) {
        List<WidgetEntity> list = new ArrayList<>();
        list.add(new WidgetEntity("UIAlertDialog", "一款自定义Alert效果控件:属性命名及调用方式同Android原生AlertDialog,增加样式背景及文本相关属性自定义自定义。", AlertActivity.class));
        list.add(new WidgetEntity("UIActionSheetDialog", "一款底部弹框控件:支持List模式(iOS、微信及QQ样式)和Grid模式", ActionSheetActivity.class));
        list.add(new WidgetEntity("UIProgressDialog", "一款仿微博、微信、MD loading控件:扩展背景及loading 样式设置。", LoadingActivity.class));
        list.add(new WidgetEntity("RadiusView", "一款扩展原生TextView、EditText、LinearLayout、FrameLayout、RelativeLayout控件库:主要实现xml设置圆角、手指按下、不可操作样式-减少drawable文件创建;同时支持5.0以上水波纹效果设置。", RadiusActivity.class));
        list.add(new WidgetEntity("TitleBarView", "一款支持沉浸状态栏效果设置的ToolBar控件:支持xml设置是否沉浸、主标题及副标题、左边文字及icon、右边文字及icon、下划线;支持添加左边、中间及右边view方便扩展。", TitleActivity.class));
        mAdapter.setNewData(list);
        mAdapter.loadMoreEnd();
    }

    @Override
    protected void loadData(int page) {
    }

    @Override
    protected void onItemClicked(BaseQuickAdapter<WidgetEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        startActivity(adapter.getItem(position).activity);
    }
}
