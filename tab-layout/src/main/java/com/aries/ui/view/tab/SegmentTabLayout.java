package com.aries.ui.view.tab;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aries.ui.view.tab.delegate.TabSegmentDelegate;
import com.aries.ui.view.tab.listener.ITabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.utils.FragmentChangeManager;
import com.aries.ui.view.tab.utils.UnreadMsgUtils;
import com.aries.ui.view.tab.widget.MsgView;

import java.util.ArrayList;

/**
 * @Author: AriesHoo on 2018/11/30 11:22
 * @E-Mail: AriesHoo@126.com
 * @Function:
 * @Description: 1、2018-11-30 11:18:41 修改原库 https://github.com/H07000223/FlycoTabLayout 选中粗体当初始化选中第一项不生效BUG
 * * * {@link #updateTabStyles()}
 * 2、2018-12-3 13:03:31 将xml属性解析及设置移植到代理类{@link TabSegmentDelegate}
 * 3、2018-12-13 09:40:51 新增选中文字字号设置 textSelectSize
 */
public class SegmentTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, ITabLayout {
    private TabSegmentDelegate mDelegate;
    private Context mContext;
    private String[] mTitles;
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private GradientDrawable mRectDrawable = new GradientDrawable();

    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int mHeight;

    /**
     * anim
     */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(0.8f);

    private FragmentChangeManager mFragmentChangeManager;
    private float[] mRadiusArr = new float[8];

    public SegmentTabLayout(Context context) {
        this(context, null, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SegmentTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new TabSegmentDelegate(this, attrs, this);
        //重写onDraw方法,需要调用这个方法来清除flag
        setWillNotDraw(false);
        setClipChildren(false);
        setClipToPadding(false);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        //get layout_height
        String height = attrs == null ? "" : attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");

        //create ViewPager
        if (height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "")) {
        } else if (height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
        } else {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }

        mValueAnimator = ValueAnimator.ofObject(new PointEvaluator(), mLastP, mCurrentP);
        mValueAnimator.addUpdateListener(this);
    }

    public TabSegmentDelegate getDelegate() {
        return mDelegate;
    }

    public SegmentTabLayout setTabData(String[] titles) {
        if (titles == null || titles.length == 0) {
            throw new IllegalStateException("Titles can not be NULL or EMPTY !");
        }

        this.mTitles = titles;

        notifyDataSetChanged();
        return this;
    }

    /**
     * 关联数据支持同时切换fragments
     */
    public SegmentTabLayout setTabData(String[] titles, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        return setTabData(titles);
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTitles.length;
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            tabView = View.inflate(mContext, R.layout.layout_tab_segment, null);
            tabView.setTag(i);
            addTab(i, tabView);
        }

        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, View tabView) {
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
        tv_tab_title.setText(mTitles[position]);

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                setCurrentTab(position);
            }
        });

        /** 每一个Tab的布局参数 */
        LinearLayout.LayoutParams lp_tab = getDelegate().isTabSpaceEqual() ?
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (getDelegate().getTabWidth() > 0) {
            lp_tab = new LinearLayout.LayoutParams(getDelegate().getTabWidth(), LayoutParams.MATCH_PARENT);
        }
        mTabsContainer.addView(tabView, position, lp_tab);
    }

    @Override
    public void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View tabView = mTabsContainer.getChildAt(i);
            tabView.setPadding((int) getDelegate().getTabPadding(), 0, (int) getDelegate().getTabPadding(), 0);
            TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
            tv_tab_title.setTextColor(i == mCurrentTab ? getDelegate().getTextSelectColor() : getDelegate().getTextUnSelectColor());
            tv_tab_title.setTextSize(getDelegate().getTextSizeUnit(), mCurrentTab == i ? getDelegate().getTextSelectSize() : getDelegate().getTextSize());
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

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabView = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView tab_title = tabView.findViewById(R.id.tv_tab_title);
            tab_title.setTextColor(isSelect ? getDelegate().getTextSelectColor() : getDelegate().getTextUnSelectColor());
            tab_title.setTextSize(getDelegate().getTextSizeUnit(), isSelect ? getDelegate().getTextSelectSize() : getDelegate().getTextSize());
            if (getDelegate().getTextBold() == TextBold.SELECT) {
                tab_title.getPaint().setFakeBoldText(isSelect);
            }
        }
    }

    private void calcOffset() {
        final View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        mCurrentP.left = currentTabView.getLeft();
        mCurrentP.right = currentTabView.getRight();

        final View lastTabView = mTabsContainer.getChildAt(this.mLastTab);
        mLastP.left = lastTabView.getLeft();
        mLastP.right = lastTabView.getRight();

        if (mLastP.left == mCurrentP.left && mLastP.right == mCurrentP.right) {
            invalidate();
        } else {
            mValueAnimator.setObjectValues(mLastP, mCurrentP);
            if (getDelegate().isIndicatorBounceEnable()) {
                mValueAnimator.setInterpolator(mInterpolator);
            }

            if (getDelegate().getIndicatorAnimDuration() < 0) {
                getDelegate().setIndicatorAnimDuration(getDelegate().isIndicatorBounceEnable() ? 500 : 250);
            }
            mValueAnimator.setDuration(getDelegate().getIndicatorAnimDuration());
            mValueAnimator.start();
        }
    }

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;

        if (!getDelegate().isIndicatorAnimEnable()) {
            if (mCurrentTab == 0) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                mRadiusArr[0] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[1] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[2] = 0;
                mRadiusArr[3] = 0;
                mRadiusArr[4] = 0;
                mRadiusArr[5] = 0;
                mRadiusArr[6] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[7] = getDelegate().getIndicatorCornerRadius();
            } else if (mCurrentTab == mTabCount - 1) {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                mRadiusArr[0] = 0;
                mRadiusArr[1] = 0;
                mRadiusArr[2] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[3] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[4] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[5] = getDelegate().getIndicatorCornerRadius();
                mRadiusArr[6] = 0;
                mRadiusArr[7] = 0;
            } else {
                /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
                mRadiusArr[0] = 0;
                mRadiusArr[1] = 0;
                mRadiusArr[2] = 0;
                mRadiusArr[3] = 0;
                mRadiusArr[4] = 0;
                mRadiusArr[5] = 0;
                mRadiusArr[6] = 0;
                mRadiusArr[7] = 0;
            }
        } else {
            /**The corners are ordered top-left, top-right, bottom-right, bottom-left*/
            mRadiusArr[0] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[1] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[2] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[3] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[4] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[5] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[6] = getDelegate().getIndicatorCornerRadius();
            mRadiusArr[7] = getDelegate().getIndicatorCornerRadius();
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;
        invalidate();
    }

    private boolean mIsFirstDraw = true;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();

        if (getDelegate().getIndicatorHeight() < 0) {
            getDelegate().setIndicatorHeight(height - getDelegate().getIndicatorMarginTop() - getDelegate().getIndicatorMarginBottom());
        }

        if (getDelegate().getIndicatorCornerRadius() < 0 || getDelegate().getIndicatorCornerRadius() > getDelegate().getIndicatorHeight() / 2) {
            getDelegate().setIndicatorCornerRadius(getDelegate().getIndicatorHeight() / 2);
        }

        //draw rect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mRectDrawable.setColor(getDelegate().getBarColor());
            mRectDrawable.setStroke(getDelegate().getBarStrokeWidth(), getDelegate().getBarStrokeColor());
        }
        mRectDrawable.setCornerRadius(getDelegate().getIndicatorCornerRadius());
        mRectDrawable.setBounds(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        mRectDrawable.draw(canvas);

        // draw divider
        if (!getDelegate().isIndicatorAnimEnable() && getDelegate().getDividerWidth() > 0) {
            mDividerPaint.setStrokeWidth(getDelegate().getDividerWidth());
            mDividerPaint.setColor(getDelegate().getDividerColor());
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), getDelegate().getDividerPadding(),
                        paddingLeft + tab.getRight(), height - getDelegate().getDividerPadding(), mDividerPaint);
            }
        }


        //draw indicator line
        if (getDelegate().isIndicatorAnimEnable()) {
            if (mIsFirstDraw) {
                mIsFirstDraw = false;
                calcIndicatorRect();
            }
        } else {
            calcIndicatorRect();
        }

        mIndicatorDrawable.setColor(getDelegate().getIndicatorColor());
        mIndicatorDrawable.setBounds(paddingLeft + getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                getDelegate().getIndicatorMarginTop(), (paddingLeft + mIndicatorRect.right - getDelegate().getIndicatorMarginRight()),
                (int) (getDelegate().getIndicatorMarginTop() + getDelegate().getIndicatorHeight()));
        mIndicatorDrawable.setCornerRadii(mRadiusArr);
        mIndicatorDrawable.draw(canvas);

    }

    /**
     * 设置选中tab
     *
     * @param currentTab
     * @return
     */
    public SegmentTabLayout setCurrentTab(int currentTab) {
        if (mCurrentTab != currentTab) {
            mLastTab = this.mCurrentTab;
            this.mCurrentTab = currentTab;
            updateTabSelection(currentTab);
            if (getDelegate().isIndicatorAnimEnable()) {
                calcOffset();
            } else {
                invalidate();
            }
            if (mFragmentChangeManager != null) {
                mFragmentChangeManager.setFragments(currentTab);
            }
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

    /**
     * show MsgTipView
     */
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private SparseArray<Boolean> mInitSetMap = new SparseArray<>();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public SegmentTabLayout showMsg(int position, int num) {
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

            setMsgMargin(position, 2f, 2f);

            mInitSetMap.put(position, true);
        }
        return this;
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public SegmentTabLayout showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        return showMsg(position, 0);
    }

    public SegmentTabLayout hideMsg(int position) {
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
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     */
    public SegmentTabLayout setMsgMargin(int position, int leftPadding, int bottomPadding) {
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

            lp.leftMargin = leftPadding;
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - bottomPadding : bottomPadding;

            tipView.setLayoutParams(lp);
        }
        return this;
    }

    public SegmentTabLayout setMsgMargin(int position, float leftPadding, float bottomPadding) {
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

    public SegmentTabLayout setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
        return this;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        Log.i("save0", "mCurrentTab:" + mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            Log.i("save1", "mCurrentTab:" + mCurrentTab);
            mCurrentTab = bundle.getInt("mCurrentTab");
            Log.i("save2", "mCurrentTab:" + mCurrentTab);
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                //updateTabSelection(mCurrentTab); 原库恢复状态时未将Fragment选中CurrentTab
                setCurrentTab(mCurrentTab);
            }
        }
        super.onRestoreInstanceState(state);
    }

    class IndicatorPoint {
        public float left;
        public float right;
    }

    private IndicatorPoint mCurrentP = new IndicatorPoint();
    private IndicatorPoint mLastP = new IndicatorPoint();

    class PointEvaluator implements TypeEvaluator<IndicatorPoint> {
        @Override
        public IndicatorPoint evaluate(float fraction, IndicatorPoint startValue, IndicatorPoint endValue) {
            float left = startValue.left + fraction * (endValue.left - startValue.left);
            float right = startValue.right + fraction * (endValue.right - startValue.right);
            IndicatorPoint point = new IndicatorPoint();
            point.left = left;
            point.right = right;
            return point;
        }
    }
}
