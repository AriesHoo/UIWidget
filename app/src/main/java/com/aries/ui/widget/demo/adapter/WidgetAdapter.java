package com.aries.ui.widget.demo.adapter;

import android.app.Activity;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.entity.WidgetEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function:
 * Desc:
 */
public class WidgetAdapter extends BaseQuickAdapter<WidgetEntity, BaseViewHolder> {
    private Activity mActivity;

    public WidgetAdapter(Activity mActivity) {
        super(R.layout.item_widget, new ArrayList<WidgetEntity>());
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, WidgetEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title);
        helper.setText(R.id.tv_contentWidget, item.content);
    }
}
