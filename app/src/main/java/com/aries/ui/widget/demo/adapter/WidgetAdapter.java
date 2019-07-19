package com.aries.ui.widget.demo.adapter;

import android.app.Activity;
import android.util.Log;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.view.radius.RadiusTextView;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.entity.WidgetEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * @Author: AriesHoo on 2019/7/17 15:00
 * @E-Mail: AriesHoo@126.com
 * @Function: 条目效果
 * @Description:
 */
public class WidgetAdapter extends BaseQuickAdapter<WidgetEntity, BaseViewHolder> {

    public WidgetAdapter() {
        super(R.layout.item_widget);
    }

    @Override
    protected void convert(BaseViewHolder helper, WidgetEntity item) {
        int position = helper.getAdapterPosition() - getHeaderLayoutCount();
        boolean status = position % 2 == 0;

        helper.setText(R.id.tv_titleWidget, item.title)
                .setText(R.id.tv_contentWidget, item.content);

        RadiusTextView radiusTextView = helper.getView(R.id.rtv_status_widget);
        radiusTextView.getDelegate()
                .setSelected(status);

        helper.itemView.setPadding(helper.itemView.getPaddingLeft(), helper.itemView.getPaddingTop()
                , helper.itemView.getPaddingBottom(), position == getData().size() - 1 ? NavigationBarUtil.getNavigationBarHeight((Activity) mContext) : 0);
        Log.i("position", "position:" + position);
    }
}
