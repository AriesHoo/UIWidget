package com.aries.ui.view.tab.delegate;

import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.view.tab.IndicatorStyle;
import com.aries.ui.view.tab.R;
import com.aries.ui.view.tab.listener.ITabLayout;

/**
 * @Author: AriesHoo on 2018/11/30 16:59
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link com.aries.ui.view.tab.CommonTabLayout}及{@link com.aries.ui.view.tab.SlidingTabLayout}共有属性及Java设置方法代理
 * @Description: 1、2018-12-4 13:41:55 将{@link com.aries.ui.view.tab.CommonTabLayout}及{@link com.aries.ui.view.tab.SlidingTabLayout}共有属性初始值进行调整
 */
public class TabCommonSlidingDelegate<T extends TabCommonSlidingDelegate> extends TabLayoutDelegate<T> {

    protected int mIndicatorStyle;
    protected int mIndicatorWidth;
    protected int mIndicatorGravity;

    /**
     * underline
     */
    protected int mUnderlineColor;
    protected int mUnderlineHeight;
    protected int mUnderlineGravity;


    public TabCommonSlidingDelegate(View view, AttributeSet attrs, ITabLayout iTabLayout) {
        super(view, attrs, iTabLayout);
        mIndicatorStyle = mTypedArray.getInt(R.styleable.TabLayout_tl_indicator_style, IndicatorStyle.NORMAL);
        mIndicatorColor = mTypedArray.getColor(R.styleable.TabLayout_tl_indicator_color, Color.parseColor(mIndicatorStyle == IndicatorStyle.BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_indicator_height, dp2px(mIndicatorStyle == IndicatorStyle.TRIANGLE ? 4 : (mIndicatorStyle == IndicatorStyle.BLOCK ? -1 : 2)));
        mIndicatorWidth = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_indicator_width, dp2px(mIndicatorStyle == IndicatorStyle.TRIANGLE ? 10 : -1));
        mIndicatorMarginTop = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_indicator_margin_top, dp2px(mIndicatorStyle == IndicatorStyle.BLOCK ? 7 : 0));
        mIndicatorMarginBottom = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_indicator_margin_bottom, dp2px(mIndicatorStyle == IndicatorStyle.BLOCK ? 7 : 0));
        mIndicatorCornerRadius = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_indicator_corner_radius, dp2px(mIndicatorStyle == IndicatorStyle.BLOCK ? -1 : 0));
        mIndicatorGravity = mTypedArray.getInt(R.styleable.TabLayout_tl_indicator_gravity, Gravity.BOTTOM);

        mUnderlineColor = mTypedArray.getColor(R.styleable.TabLayout_tl_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_underline_height, dp2px(0));
        mUnderlineGravity = mTypedArray.getInt(R.styleable.TabLayout_tl_underline_gravity, Gravity.BOTTOM);
    }

    /**
     * 设置指示器样式{@link IndicatorStyle#NORMAL}{@link IndicatorStyle#TRIANGLE}{@link IndicatorStyle#BLOCK}
     *
     * @param indicatorStyle
     * @return
     */
    public T setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        return back();
    }

    public T setIndicatorWidth(float indicatorWidth) {
        return setIndicatorWidth(dp2px(indicatorWidth));
    }

    /**
     * 新增方法
     *
     * @param indicatorWidth
     * @return
     */
    public T setIndicatorWidth(int indicatorWidth) {
        this.mIndicatorWidth = indicatorWidth;
        return back();
    }

    public T setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        return back();
    }

    public T setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        return back();
    }

    public T setUnderlineHeight(float underlineHeight) {
        return setUnderlineHeight(dp2px(underlineHeight));
    }

    public T setUnderlineHeight(int underlineHeight) {
        this.mUnderlineHeight = underlineHeight;
        return back();
    }

    public T setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        return back();
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public int getIndicatorWidth() {
        return mIndicatorWidth;
    }


    public int getIndicatorGravity() {
        return mIndicatorGravity;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getUnderlineGravity() {
        return mUnderlineGravity;
    }
}
