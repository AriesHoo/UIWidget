package com.aries.ui.widget.demo.adapter;

import android.app.Activity;

import com.aries.ui.widget.demo.R;
import com.aries.ui.widget.demo.entity.DrawerEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;

/**
 * Created: AriesHoo on 2017/7/14 9:55
 * Function: Drawer 适配器
 * Desc:
 */
public class DrawerAdapter extends BaseQuickAdapter<DrawerEntity, BaseViewHolder> {
    private Activity mActivity;

    public DrawerAdapter(Activity mActivity) {
        super(R.layout.item_widget, new ArrayList<DrawerEntity>());
        this.mActivity = mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, DrawerEntity item) {
        helper.setText(R.id.tv_titleWidget, item.title);
        helper.setText(R.id.tv_contentWidget, item.content);
    }
}
