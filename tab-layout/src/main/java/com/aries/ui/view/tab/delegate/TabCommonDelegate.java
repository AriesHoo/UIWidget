package com.aries.ui.view.tab.delegate;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.aries.ui.view.tab.R;
import com.aries.ui.view.tab.listener.ITabLayout;

/**
 * @Author: AriesHoo on 2018/11/30 17:00
 * @E-Mail: AriesHoo@126.com
 * @Function: {@link com.aries.ui.view.tab.CommonTabLayout}属性解析及设置代理
 * @Description:
 */
public class TabCommonDelegate extends TabCommonSlidingDelegate<TabCommonDelegate> {

    private long mIndicatorAnimDuration;
    private boolean mIndicatorAnimEnable;
    private boolean mIndicatorBounceEnable;

    private boolean mIconVisible;
    private int mIconGravity;
    private int mIconWidth;
    private int mIconHeight;
    private int mIconMargin;

    public TabCommonDelegate(View view, AttributeSet attrs, ITabLayout iTabLayout) {
        super(view, attrs, iTabLayout);

        mIndicatorAnimEnable = mTypedArray.getBoolean(R.styleable.TabLayout_tl_indicator_anim_enable, true);
        mIndicatorBounceEnable = mTypedArray.getBoolean(R.styleable.TabLayout_tl_indicator_bounce_enable, true);
        mIndicatorAnimDuration = mTypedArray.getInt(R.styleable.TabLayout_tl_indicator_anim_duration, -1);

        mIconVisible = mTypedArray.getBoolean(R.styleable.TabLayout_tl_iconVisible, true);
        mIconGravity = mTypedArray.getInt(R.styleable.TabLayout_tl_iconGravity, Gravity.TOP);
        mIconWidth = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_iconWidth, dp2px(0));
        mIconHeight = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_iconHeight, dp2px(0));
        mIconMargin = mTypedArray.getDimensionPixelSize(R.styleable.TabLayout_tl_iconMargin, dp2px(2.5f));

    }


    public TabCommonDelegate setIndicatorAnimDuration(long indicatorAnimDuration) {
        this.mIndicatorAnimDuration = indicatorAnimDuration;
        return back();
    }

    public TabCommonDelegate setIndicatorAnimEnable(boolean indicatorAnimEnable) {
        this.mIndicatorAnimEnable = indicatorAnimEnable;
        return back();
    }

    public TabCommonDelegate setIndicatorBounceEnable(boolean indicatorBounceEnable) {
        this.mIndicatorBounceEnable = indicatorBounceEnable;
        return back();
    }

    public TabCommonDelegate setIconVisible(boolean iconVisible) {
        this.mIconVisible = iconVisible;
        return back(true);
    }

    public TabCommonDelegate setIconGravity(int iconGravity) {
        this.mIconGravity = iconGravity;
        return back();
    }

    /**
     * 新增设置icon宽度
     *
     * @param iconWidth
     * @return
     */
    public TabCommonDelegate setIconWidth(int iconWidth) {
        this.mIconWidth = iconWidth;
        return back(true);
    }

    public TabCommonDelegate setIconWidth(float iconWidth) {
        return setIconWidth(dp2px(iconWidth));
    }

    /**
     * 新增设置icon 高度
     *
     * @param iconHeight
     * @return
     */
    public TabCommonDelegate setIconHeight(int iconHeight) {
        this.mIconHeight = iconHeight;
        return back(true);
    }

    public TabCommonDelegate setIconHeight(float iconHeight) {
        return setIconHeight(dp2px(iconHeight));
    }

    /**
     * 新增设置icon间margin方法
     *
     * @param iconMargin
     * @return
     */
    public TabCommonDelegate setIconMargin(int iconMargin) {
        this.mIconMargin = iconMargin;
        return back(true);
    }

    public TabCommonDelegate setIconMargin(float iconMargin) {
        return setIconMargin(dp2px(iconMargin));
    }

    public long getIndicatorAnimDuration() {
        return mIndicatorAnimDuration;
    }

    public boolean isIndicatorAnimEnable() {
        return mIndicatorAnimEnable;
    }

    public boolean isIndicatorBounceEnable() {
        return mIndicatorBounceEnable;
    }

    public int getIconGravity() {
        return mIconGravity;
    }

    public int getIconWidth() {
        return mIconWidth;
    }

    public int getIconHeight() {
        return mIconHeight;
    }

    public int getIconMargin() {
        return mIconMargin;
    }

    public boolean isIconVisible() {
        return mIconVisible;
    }
}
