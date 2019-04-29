package com.aries.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: AriesHoo on 2019/4/29 15:01
 * @E-Mail: AriesHoo@126.com
 * @Function: 可以监听滚动事件的 {@link ScrollView}，并能在滚动回调中获取每次滚动前后的偏移量;
 * 由于 {@link ScrollView} 没有类似于 addOnScrollChangedListener 的方法可以监听滚动事件，所以需要通过重写 {@link View#onScrollChanged(int, int, int, int)}，来触发滚动监听
 * @Description:
 */
public class ObservableScrollView extends ScrollView {

    private int mScrollOffset = 0;

    private List<OnScrollChangedListener> mOnScrollChangedListeners;

    public interface OnScrollChangedListener {
        /**
         * 滚动回调
         *
         * @param scrollView
         * @param l
         * @param t
         * @param oldl
         * @param oldt
         */
        void onScrollChanged(ObservableScrollView scrollView, int l, int t, int oldl, int oldt);
    }

    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 添加监听
     *
     * @param onScrollChangedListener
     */
    public void addOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new ArrayList<>();
        }
        if (mOnScrollChangedListeners.contains(onScrollChangedListener)) {
            return;
        }
        mOnScrollChangedListeners.add(onScrollChangedListener);
    }

    /**
     * 移除监听
     *
     * @param onScrollChangedListener
     */
    public void removeOnScrollChangedListener(OnScrollChangedListener onScrollChangedListener) {
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(onScrollChangedListener);
    }

    /**
     * 滚动偏移量
     *
     * @return
     */
    public int getScrollOffset() {
        return mScrollOffset;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        mScrollOffset = t;
        if (mOnScrollChangedListeners != null && !mOnScrollChangedListeners.isEmpty()) {
            for (OnScrollChangedListener listener : mOnScrollChangedListeners) {
                listener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }
    }
}
