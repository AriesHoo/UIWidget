package com.aries.ui.widget.demo.adapter;

import android.app.Activity;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.entity.TitleEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function: TitleBarView 适配器
 * Desc:
 */
public class TitleAdapter extends BaseQuickAdapter<TitleEntity, BaseViewHolder> {
    private Activity mActivity;

    public TitleAdapter(Activity mActivity) {
        super(R.layout.item_widget, new ArrayList<TitleEntity>());
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, TitleEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title);
        helper.setText(R.id.tv_contentWidget, item.content);
    }
}
