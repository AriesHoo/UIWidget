package com.aries.ui.widget.demo;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.widget.demo.adapter.WidgetAdapter;
import com.aries.ui.widget.demo.base.BaseRecycleActivity;
import com.aries.ui.widget.demo.entity.WidgetEntity;
import com.aries.ui.widget.demo.module.action.ActionSheetActivity;
import com.aries.ui.widget.demo.module.alert.AlertActivity;
import com.aries.ui.widget.demo.module.loading.LoadingActivity;
import com.aries.ui.widget.demo.module.radius.RadiusActivity;
import com.aries.ui.widget.demo.module.title.TitleActivity;
import com.aries.ui.widget.demo.util.SizeUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;

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
                .setLeftTextDrawable(R.drawable.ic_github)
                .setLeftTextDrawableWidth(SizeUtil.dp2px(32))
                .setLeftTextDrawableHeight(SizeUtil.dp2px(32))
                .setOnLeftTextClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WebViewActivity.start(mContext, "https://github.com/AriesHoo/UIWidget/blob/master/README.md");
                    }
                })
                .setTitleSubTextMarquee(true);
    }

    @Override
    protected void initView(Bundle bundle) {
        int padding = getResources().getDimensionPixelSize(R.dimen.dp_margin);
        TextView textView = new TextView(mContext);
        textView.setText(Html.fromHtml("是否全面屏:" + NavigationBarUtil.isFullScreenDevice(mContext) + "<br>屏幕纵横比:" + NavigationBarUtil.getAspectRatio(mContext) + "<br>是否开启全面屏手势:" + NavigationBarUtil.isOpenFullScreenGestures(mContext) + "<br>是否有导航栏:" + NavigationBarUtil.hasNavBar(mContext) + "<br>导航栏高度:" + NavigationBarUtil.getNavigationBarHeight(mContext)));
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.colorTextBlack));
        textView.setPadding(padding, padding, padding, padding);
        textView.setLineSpacing(2f, 2f);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.dp_text_size_main));
        textView.setBackgroundColor(Color.WHITE);
        mAdapter.addHeaderView(textView);
        List<WidgetEntity> list = new ArrayList<>();
        list.add(new WidgetEntity("UIAlertDialog", "一款自定义Alert效果控件:属性命名及调用方式同Android原生AlertDialog,增加样式背景及文本相关属性自定义自定义。", AlertActivity.class));
        list.add(new WidgetEntity("UIActionSheetDialog", "一款底部弹框控件:支持List模式(iOS、微信及QQ样式)和Grid模式", ActionSheetActivity.class));
        list.add(new WidgetEntity("UIProgressDialog", "一款仿微博、微信、MD loading控件:扩展背景及loading 样式设置。", LoadingActivity.class));
        list.add(new WidgetEntity("RadiusView", "一款扩展原生TextView、EditText、LinearLayout、FrameLayout、RelativeLayout控件库:主要实现xml设置圆角、手指按下、不可操作样式-减少drawable文件创建;同时支持5.0以上水波纹效果设置。", RadiusActivity.class));
        list.add(new WidgetEntity("TitleBarView", "一款支持沉浸状态栏效果设置的ToolBar控件:支持xml设置是否沉浸、主标题及副标题、左边文字及icon、右边文字及icon、下划线;支持添加左边、中间及右边view方便扩展。", TitleActivity.class));
        mAdapter.setNewData(list);
        mAdapter.loadMoreEnd();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                titleBar.setLeftTextDrawable(0);
            }
        }, 5000);
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
