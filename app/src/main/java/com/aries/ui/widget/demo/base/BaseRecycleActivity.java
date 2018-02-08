package com.aries.ui.widget.demo.base;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.widget.demo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

/**
 * Created: AriesHoo on 2017/7/14 9:41
 * Function: 有RecycleView Activity
 * Desc:
 */
public abstract class BaseRecycleActivity<T> extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected int DEFAULT_PAGE = 0;
    protected int DEFAULT_PAGE_SIZE = 10;

    protected abstract void loadData(int page);

    protected boolean setLoadMore() {
        return true;
    }

    protected boolean setItemClickable() {
        return true;
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    protected void beforeInitView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_content);
        if (mRecyclerView != null) {
            initRecyclerView();
        }
        super.beforeInitView();
    }

    protected void initRecyclerView() {
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setLayoutManager(initLayoutManager());
        mRecyclerView.setAdapter(initAdapter());
        if (!setItemClickable()) {
            return;
        }
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                onItemClicked(adapter, view, position);
            }

        });
    }

    protected RecyclerView.LayoutManager initLayoutManager() {
        mLayoutManager = new LinearLayoutManager(mContext);
        return mLayoutManager;
    }

    protected RecyclerView.Adapter initAdapter() {
        mAdapter = getAdapter();
        if (mAdapter != null && setLoadMore()) {
            mAdapter.setOnLoadMoreListener(this);
        }
        return mAdapter;
    }

    protected void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position) {
        Log.d("onItemClicked", "onItemClicked:" + position);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(DEFAULT_PAGE++);
    }

    @Override
    protected void loadData() {
        loadData(DEFAULT_PAGE);
    }
}
