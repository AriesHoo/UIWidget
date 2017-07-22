package com.aries.ui.widget.demo.helper;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.aries.ui.widget.demo.WebViewActivity;
import com.aries.ui.widget.demo.adapter.DrawerAdapter;
import com.aries.ui.widget.demo.entity.DrawerEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

/**
 * Created: AriesHoo on 2017/7/17 17:26
 * Function: 抽屉栏控制类
 * Desc:
 */
public class DrawerHelper {
    private static volatile DrawerHelper instance;

    private DrawerHelper() {
    }

    public static DrawerHelper getInstance() {
        if (instance == null) {
            synchronized (DrawerHelper.class) {
                if (instance == null) {
                    instance = new DrawerHelper();
                }
            }
        }
        return instance;
    }

    public void initRecyclerView(final Activity mContext, RecyclerView mRecyclerViewDrawer, List<DrawerEntity> list) {
        final BaseQuickAdapter mAdapterDrawer = new DrawerAdapter(mContext);
        mRecyclerViewDrawer.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerViewDrawer.setAdapter(mAdapterDrawer);
        mRecyclerViewDrawer.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                DrawerEntity entity = (DrawerEntity) mAdapterDrawer.getItem(position);
                WebViewActivity.start(mContext,entity.url);
            }
        });
        mAdapterDrawer.setNewData(list);
    }
}
