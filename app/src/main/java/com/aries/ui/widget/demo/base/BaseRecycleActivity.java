package com.aries.ui.widget.demo.base;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import com.aries.ui.helper.navigation.NavigationBarUtil;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.widget.demo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Author: AriesHoo on 2019/4/15 13:26
 * @E-Mail: AriesHoo@126.com
 * @Function: 有RecycleView Activity
 * @Description:
 */
public abstract class BaseRecycleActivity<T> extends BaseActivity
        implements BaseQuickAdapter.RequestLoadMoreListener, ViewTreeObserver.OnGlobalLayoutListener {

    protected RecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;

    protected abstract void loadData(int page);

    protected boolean setLoadMore() {
        return true;
    }

    protected boolean setItemClickable() {
        return true;
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getAdapter();

    @Override
    protected void beforeControlNavigation(NavigationViewHelper navigationHelper) {
        super.beforeControlNavigation(navigationHelper);
        navigationHelper.setPlusNavigationViewEnable(isPlusView(this), true)
                .setNavigationLayoutColor(Color.TRANSPARENT)
                .setNavigationViewColor(Color.argb(isDarkIcon() ? 30 : 80, 0, 0, 0));
    }

    @Override
    protected void beforeInitView() {
        navigationHeight = NavigationBarUtil.getNavigationBarHeight(this);
        getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(this);
        mRecyclerView = findViewById(R.id.rv_content);
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
            mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        }
        return mAdapter;
    }

    protected void onItemClicked(BaseQuickAdapter<T, BaseViewHolder> adapter, View view, int position) {
        Log.d("onItemClicked", "onItemClicked:" + position);
    }

    @Override
    public void onLoadMoreRequested() {
        loadData(0);
    }

    @Override
    protected void loadData() {
        loadData(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(this);
        } else {
            getWindow().getDecorView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
        }
    }

    int navigationHeight;

    /**
     * Callback method to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     */
    @Override
    public void onGlobalLayout() {
        if (mAdapter != null && navigationHeight != NavigationBarUtil.getNavigationBarHeight(this)) {
            navigationHeight = NavigationBarUtil.getNavigationBarHeight(getWindow());
            mAdapter.notifyDataSetChanged();
        }
    }
}
