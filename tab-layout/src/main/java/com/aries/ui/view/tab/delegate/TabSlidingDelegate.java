package com.aries.ui.view.tab.delegate;

import android.util.AttributeSet;
import android.view.View;

import com.aries.ui.view.tab.R;
import com.aries.ui.view.tab.listener.ITabLayout;

/**
 * @Author: AriesHoo on 2018/12/3 9:19
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link com.aries.ui.view.tab.SlidingTabLayout}属性解析及方法设置代理
 * @Description:
 */
public class TabSlidingDelegate extends TabCommonSlidingDelegate<TabSlidingDelegate> {

    private boolean mIndicatorWidthEqualTitle;

    public TabSlidingDelegate(View view, AttributeSet attrs, ITabLayout iTabLayout) {
        super(view, attrs, iTabLayout);
        mTabSpaceEqual = mTypedArray.getBoolean(R.styleable.TabLayout_tl_tab_space_equal, false);

        mTabPadding = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_tab_padding, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(20));
        mIndicatorWidthEqualTitle = mTypedArray.getBoolean(R.styleable.TabLayout_tl_indicator_width_equal_title, false);
    }

    public TabSlidingDelegate setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        return back();
    }


    public boolean isIndicatorWidthEqualTitle() {
        return mIndicatorWidthEqualTitle;
    }
}
