package com.aries.ui.view.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.aries.ui.view.tab.delegate.TabSlidingDelegate;
import com.aries.ui.view.tab.listener.ITabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.utils.UnreadMsgUtils;
import com.aries.ui.view.tab.widget.MsgView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: AriesHoo on 2018/11/30 11:18
 * @E-Mail: AriesHoo@126.com
 * @Function: 滑动TabLayout, 对于ViewPager的依赖性强
 * @Description: 1、2018-11-30 11:18:41 修改原库 https://github.com/H07000223/FlycoTabLayout 选中粗体当初始化选中第一项不生效BUG
 * {@link #updateTabStyles()}
 * 2、2018-12-4 14:12:34 新增部分设置方法返回值方便链式调用;删除原库相应xml属性 set/get方法
 * 3、2018-12-13 09:40:51 新增选中文字字号设置 textSelectSize
 * 4、2019-9-16 12:38:57 修改{@link #notifyDataSetChanged()}及{@link #addNewTab(String)} pageTitle 非空判断
 */
public class SlidingTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener, ITabLayout {
    private TabSlidingDelegate mDelegate;
    private Context mContext;
    private ViewPager mViewPager;
    private ArrayList<String> mTitles;
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    /**
     * 用于实现滚动居中
     */
    private Rect mTabRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();

    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;

    private int mLastScrollX;
    private int mHeight;
    private boolean mSnapOnTabClick;

    public SlidingTabLayout(Context context) {
        this(context, null, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new TabSlidingDelegate(this, attrs, this);
        //设置滚动视图是否可以伸缩其内容以填充视口
        setFillViewport(true);
        //重写onDraw方法,需要调用这个方法来清除flag
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        //get layout_height
        String height = attrs == null ? "" : attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    public TabSlidingDelegate getDelegate() {
        return mDelegate;
    }

    /**
     * 关联ViewPager
     */
    public void setViewPager(ViewPager vp) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        this.mViewPager = vp;

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPager(ViewPager vp, String[] titles) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        if (titles.length != vp.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }

        this.mViewPager = vp;
        mTitles = new ArrayList<>();
        Collections.addAll(mTitles, titles);

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 关联ViewPager,用于连适配器都不想自己实例化的情况
     */
    public void setViewPager(ViewPager vp, String[] titles, FragmentActivity fa, ArrayList<Fragment> fragments) {
        if (vp == null) {
            throw new IllegalStateException("ViewPager can not be NULL !");
        }

        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        this.mViewPager = vp;
        this.mViewPager.setAdapter(new InnerPagerAdapter(fa.getSupportFragmentManager(), fragments, titles));

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.layout_tab, null);
            CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(i) : mTitles.get(i);
            addTab(i, TextUtils.isEmpty(pageTitle) ? "" : pageTitle.toString(), tabView);
        }
        if (mCurrentTab != mViewPager.getCurrentItem()) {
            setCurrentTab(mViewPager.getCurrentItem());
        }
        updateTabStyles();
    }

    public SlidingTabLayout addNewTab(String title) {
        View tabView = View.inflate(mContext, R.layout.layout_tab, null);
        if (mTitles != null) {
            mTitles.add(title);
        }

        CharSequence pageTitle = mTitles == null ? mViewPager.getAdapter().getPageTitle(mTabCount) : mTitles.get(mTabCount);
        addTab(mTabCount, TextUtils.isEmpty(pageTitle) ? "" : pageTitle.toString(), tabView);
        this.mTabCount = mTitles == null ? mViewPager.getAdapter().getCount() : mTitles.size();

        updateTabStyles();
        return this;
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        if (tv_tab_title != null && title != null) {
            tv_tab_title.setText(title);
        }

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mTabsContainer.indexOfChild(v);
                if (position != -1) {
                    setCurrentTab(position);
                }
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = getDelegate().isTabSpaceEqual() ?
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (getDelegate().getTabWidth() > 0) {
            lp_tab = new LinearLayout.LayoutParams(getDelegate().getTabWidth(), LayoutParams.MATCH_PARENT);
        }

        mTabsContainer.addView(tabView, position, lp_tab);
    }

    @Override
    public void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            TextView tv_tab_title = v.findViewById(R.id.tv_tab_title);
            if (tv_tab_title != null) {
                tv_tab_title.setTextColor(i == mCurrentTab ? getDelegate().getTextSelectColor() : getDelegate().getTextUnSelectColor());
                tv_tab_title.setTextSize(getDelegate().getTextSizeUnit(), mCurrentTab == i ? getDelegate().getTextSelectSize() : getDelegate().getTextSize());
                tv_tab_title.setPadding(getDelegate().getTabPadding(), 0, getDelegate().getTabPadding(), 0);
                if (getDelegate().isTextAllCaps()) {
                    tv_tab_title.setText(tv_tab_title.getText().toString().toUpperCase());
                }
                if (getDelegate().getTextBold() == TextBold.BOTH) {
                    tv_tab_title.getPaint().setFakeBoldText(true);
                } else if (getDelegate().getTextBold() == TextBold.SELECT) {
                    //增加-以修正原库第一次选中粗体不生效问题
                    tv_tab_title.getPaint().setFakeBoldText(mCurrentTab == i);
                } else {
                    tv_tab_title.getPaint().setFakeBoldText(false);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        /**
         * position:当前View的位置
         * mCurrentPositionOffset:当前View的偏移量比例.[0,1)
         */
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        setCurrentTab(position);
        updateTabSelection(position);
        Log.e("onPageSelected", "position:" + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).getWidth());
        /**当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            /**HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            /** scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
             *  x:表示离起始位置的x水平方向的偏移量
             *  y:表示离起始位置的y垂直方向的偏移量
             */
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        Log.e("updateTabSelection", "position:" + position);
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);

            if (tab_title != null) {
                tab_title.setTextColor(isSelect ? getDelegate().getTextSelectColor() : getDelegate().getTextUnSelectColor());
                tab_title.setTextSize(getDelegate().getTextSizeUnit(), isSelect ? getDelegate().getTextSelectSize() : getDelegate().getTextSize());
                if (getDelegate().getTextBold() == TextBold.BOTH) {
                    tab_title.getPaint().setFakeBoldText(true);
                } else if (getDelegate().getTextBold() == TextBold.SELECT) {
                    //增加-以修正原库第一次选中粗体不生效问题
                    tab_title.getPaint().setFakeBoldText(mCurrentTab == i);
                } else {
                    tab_title.getPaint().setFakeBoldText(false);
                }
            }
        }
    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (getDelegate().getIndicatorStyle() == STYLE_NORMAL && getDelegate().isIndicatorWidthEqualTitle()) {
            TextView tab_title = currentTabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(getDelegate().getTextSize());
            float textWidth = mTextPaint.measureText(tab_title.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (getDelegate().getIndicatorStyle() == STYLE_NORMAL && getDelegate().isIndicatorWidthEqualTitle()) {
                TextView next_tab_title = nextTabView.findViewById(R.id.tv_tab_title);
                mTextPaint.setTextSize(getDelegate().getTextSize());
                float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        //for mIndicatorWidthEqualTitle
        if (getDelegate().getIndicatorStyle() == STYLE_NORMAL && getDelegate().isIndicatorWidthEqualTitle()) {
            mIndicatorRect.left = (int) (left + margin - 1);
            mIndicatorRect.right = (int) (right - margin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        if (getDelegate().getIndicatorWidth() < 0) {

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - getDelegate().getIndicatorWidth()) / 2;

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2 + nextTab.getWidth() / 2);
            }

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + getDelegate().getIndicatorWidth());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (getDelegate().getDividerWidth() > 0) {
            mDividerPaint.setStrokeWidth(getDelegate().getDividerWidth());
            mDividerPaint.setColor(getDelegate().getDividerColor());
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), getDelegate().getDividerPadding(),
                        paddingLeft + tab.getRight(), height - getDelegate().getDividerPadding(), mDividerPaint);
            }
        }

        // draw underline
        if (getDelegate().getUnderlineHeight() > 0) {
            mRectPaint.setColor(getDelegate().getUnderlineColor());
            if (getDelegate().getUnderlineGravity() == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - getDelegate().getUnderlineHeight(),
                        mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft,
                        getDelegate().getUnderlineHeight(), mRectPaint);
            }
        }

        //draw indicator line

        calcIndicatorRect();
        if (getDelegate().getIndicatorStyle() == STYLE_TRIANGLE) {
            if (getDelegate().getIndicatorHeight() > 0) {
                mTrianglePaint.setColor(getDelegate().getIndicatorColor());
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - getDelegate().getIndicatorHeight());
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (getDelegate().getIndicatorStyle() == STYLE_BLOCK) {
            if (getDelegate().getIndicatorHeight() < 0) {
                getDelegate().setIndicatorHeight(height - getDelegate().getIndicatorMarginTop() - getDelegate().getIndicatorMarginBottom());
            }

            if (getDelegate().getIndicatorHeight() > 0) {
                if (getDelegate().getIndicatorCornerRadius() < 0 || getDelegate().getIndicatorCornerRadius() > getDelegate().getIndicatorHeight() / 2) {
                    getDelegate().setIndicatorCornerRadius(getDelegate().getIndicatorHeight() / 2);
                }

                mIndicatorDrawable.setColor(getDelegate().getIndicatorColor());
                mIndicatorDrawable.setBounds(paddingLeft + getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                        getDelegate().getIndicatorMarginTop(), (paddingLeft + mIndicatorRect.right - getDelegate().getIndicatorMarginRight()),
                        (getDelegate().getIndicatorMarginTop() + getDelegate().getIndicatorHeight()));
                mIndicatorDrawable.setCornerRadius(getDelegate().getIndicatorCornerRadius());
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (getDelegate().getIndicatorHeight() > 0) {
                mIndicatorDrawable.setColor(getDelegate().getIndicatorColor());
                if (getDelegate().getIndicatorGravity() == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                            height - getDelegate().getIndicatorHeight() - getDelegate().getIndicatorMarginBottom(),
                            paddingLeft + mIndicatorRect.right - getDelegate().getIndicatorMarginRight(),
                            height - getDelegate().getIndicatorMarginBottom());
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                            getDelegate().getIndicatorMarginTop(),
                            paddingLeft + mIndicatorRect.right - getDelegate().getIndicatorMarginRight(),
                            getDelegate().getIndicatorHeight() + getDelegate().getIndicatorMarginTop());
                }
                mIndicatorDrawable.setCornerRadius(getDelegate().getIndicatorCornerRadius());
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    public SlidingTabLayout setCurrentTab(int currentTab) {
        return setCurrentTab(currentTab, false);
    }

    public SlidingTabLayout setCurrentTab(int currentTab, boolean smoothScroll) {
        if (getCurrentTab() != currentTab) {
            this.mCurrentTab = currentTab;
            mViewPager.setCurrentItem(currentTab, mSnapOnTabClick ? false : smoothScroll);
            if (mListener != null) {
                mListener.onTabSelect(currentTab);
            }
        } else {
            if (mListener != null) {
                mListener.onTabReselect(currentTab);
            }
        }
        return this;
    }

    public SlidingTabLayout setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
        return this;
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        return tv_tab_title;
    }

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public SlidingTabLayout showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            UnreadMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position) != null && mInitSetMap.get(position)) {
                return this;
            }

            setMsgMargin(position, 4f, 2f);
            mInitSetMap.put(position, true);
        }
        return this;
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public SlidingTabLayout showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        return showMsg(position, 0);
    }

    /**
     * 隐藏未读消息
     */
    public SlidingTabLayout hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    public SlidingTabLayout setMsgMargin(int position, int leftPadding, int bottomPadding) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(getDelegate().getTextSize());
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();
            lp.leftMargin = getDelegate().getTabWidth() >= 0
                    ? (int) (getDelegate().getTabWidth() / 2 + textWidth / 2 + leftPadding)
                    : (int) (getDelegate().getTabPadding() + textWidth + leftPadding);
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - bottomPadding : 0;
            tipView.setLayoutParams(lp);
        }
        return this;
    }

    public SlidingTabLayout setMsgMargin(int position, float leftPadding, float bottomPadding) {
        return setMsgMargin(position, getDelegate().dp2px(leftPadding), getDelegate().dp2px(bottomPadding));
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public MsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        MsgView tipView = tabView.findViewById(R.id.rtv_msg_tip);
        return tipView;
    }

    private OnTabSelectListener mListener;

    public SlidingTabLayout setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
        return this;
    }

    class InnerPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> fragments;
        private String[] titles;

        public InnerPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments, String[] titles) {
            super(fm);
            this.fragments = fragments;
            this.titles = titles;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // 覆写destroyItem并且空实现,这样每个Fragment中的视图就不会被销毁
            // super.destroyItem(container, position, object);
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                //原库恢复状态时未将Fragment选中CurrentTab
                setCurrentTab(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }
}
