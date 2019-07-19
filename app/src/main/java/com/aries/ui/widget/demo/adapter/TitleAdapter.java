package com.aries.ui.widget.demo.adapter;

import android.app.Activity;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.entity.TitleEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function: TitleBarView 适配器
 * Desc:
 */

/**
 * @Author: AriesHoo on 2019/7/17 15:02
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description:
 */
public class TitleAdapter extends BaseQuickAdapter<TitleEntity, BaseViewHolder> {

    public TitleAdapter() {
        super(R.layout.item_widget);
    }

    @Override
    protected void convert(BaseViewHolder helper, TitleEntity item) {
        int position = helper.getAdapterPosition() - getHeaderLayoutCount();
        helper.setText(R.id.tv_titleWidget, item.title);
        helper.setText(R.id.tv_contentWidget, item.content);
        helper.itemView.setPadding(helper.itemView.getPaddingLeft(),helper.itemView.getPaddingTop()
                ,helper.itemView.getPaddingBottom(),position == getData().size()-1? NavigationBarUtil.getNavigationBarHeight((Activity)mContext):0);
    }
}
