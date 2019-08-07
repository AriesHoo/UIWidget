package com.aries.ui.view.tab;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.aries.ui.view.tab.delegate.TabCommonDelegate;
import com.aries.ui.view.tab.listener.CustomTabEntity;
import com.aries.ui.view.tab.listener.ITabLayout;
import com.aries.ui.view.tab.listener.OnTabSelectListener;
import com.aries.ui.view.tab.utils.FragmentChangeManager;
import com.aries.ui.view.tab.utils.UnreadMsgUtils;
import com.aries.ui.view.tab.widget.MsgView;

import java.util.ArrayList;

/**
 * @Author: AriesHoo on 2018/11/30 9:48
 * @E-Mail: AriesHoo@126.com
 * @Function: 没有继承HorizontalScrollView不能滑动, 对于ViewPager无依赖
 * @Description: 1、2018-11-30 09:48:45 修改原库在处理Activity状态回收后当前选中tab正确Fragment不正确问题{@link #onRestoreInstanceState(Parcelable)}
 * 2、2018-11-30 10:07:38 修改参数方法拼写错误及java方法废弃部分错误拼写
 * 3、2018-11-30 10:08:19 增加java方法回调值方便链式调用
 * 4、2018-11-30 11:18:41 修改原库 https://github.com/H07000223/FlycoTabLayout 选中粗体当初始化选中第一项不生效BUG{@link #updateTabStyles()}
 * 5、2018-12-13 09:40:51 新增选中文字字号设置 textSelectSize
 */
public class CommonTabLayout extends FrameLayout implements ValueAnimator.AnimatorUpdateListener, ITabLayout {
    private TabCommonDelegate mDelegate;
    private Context mContext;
    private ArrayList<CustomTabEntity> mTabEntity = new ArrayList<>();
    private LinearLayout mTabsContainer;
    private int mCurrentTab;
    private int mLastTab;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private Rect mIndicatorRect = new Rect();
    private GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Path mTrianglePath = new Path();
    private int mHeight;

    /**
     * anim
     */
    private ValueAnimator mValueAnimator;
    private OvershootInterpolator mInterpolator = new OvershootInterpolator(1.5f);

    private FragmentChangeManager mFragmentChangeManager;

    public CommonTabLayout(Context context) {
        this(context, null, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDelegate = new TabCommonDelegate(this, attrs, this);
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

    public TabCommonDelegate getDelegate() {
        return mDelegate;
    }

    public CommonTabLayout setTabData(ArrayList<CustomTabEntity> tabEntity) {
        if (tabEntity == null || tabEntity.size() == 0) {
            throw new IllegalStateException("TabEntity can not be NULL or EMPTY !");
        }

        this.mTabEntity.clear();
        this.mTabEntity.addAll(tabEntity);

        notifyDataSetChanged();
        return this;
    }

    /**
     * 关联数据支持同时切换fragments
     */
    public CommonTabLayout setTabData(ArrayList<CustomTabEntity> tabEntity, FragmentActivity fa, int containerViewId, ArrayList<Fragment> fragments) {
        mFragmentChangeManager = new FragmentChangeManager(fa.getSupportFragmentManager(), containerViewId, fragments);
        return setTabData(tabEntity);
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        this.mTabCount = mTabEntity.size();
        View tabView;
        for (int i = 0; i < mTabCount; i++) {
            if (getDelegate().getIconGravity() == Gravity.LEFT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_left, null);
            } else if (getDelegate().getIconGravity() == Gravity.RIGHT) {
                tabView = View.inflate(mContext, R.layout.layout_tab_right, null);
            } else if (getDelegate().getIconGravity() == Gravity.BOTTOM) {
                tabView = View.inflate(mContext, R.layout.layout_tab_bottom, null);
            } else {
                tabView = View.inflate(mContext, R.layout.layout_tab_top, null);
            }

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
        tv_tab_title.setText(mTabEntity.get(position).getTabTitle());
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        iv_tab_icon.setImageResource(mTabEntity.get(position).getTabUnselectedIcon());

        tabView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                setCurrentTab(position);
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
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            if (getDelegate().isIconVisible()) {
                iv_tab_icon.setVisibility(View.VISIBLE);
                CustomTabEntity tabEntity = mTabEntity.get(i);
                iv_tab_icon.setImageResource(i == mCurrentTab ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        getDelegate().getIconWidth() <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : getDelegate().getIconWidth(),
                        getDelegate().getIconHeight() <= 0 ? LinearLayout.LayoutParams.WRAP_CONTENT : getDelegate().getIconHeight());
                if (getDelegate().getIconGravity() == Gravity.LEFT) {
                    lp.rightMargin = getDelegate().getIconMargin();
                } else if (getDelegate().getIconGravity() == Gravity.RIGHT) {
                    lp.leftMargin = getDelegate().getIconMargin();
                } else if (getDelegate().getIconGravity() == Gravity.BOTTOM) {
                    lp.topMargin = getDelegate().getIconMargin();
                } else {
                    lp.bottomMargin = getDelegate().getIconMargin();
                }
                iv_tab_icon.setLayoutParams(lp);
            } else {
                iv_tab_icon.setVisibility(View.GONE);
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
            ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
            CustomTabEntity tabEntity = mTabEntity.get(i);
            iv_tab_icon.setImageResource(isSelect ? tabEntity.getTabSelectedIcon() : tabEntity.getTabUnselectedIcon());
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

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        if (getDelegate().getIndicatorWidth() < 0) {
        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - getDelegate().getIndicatorWidth()) / 2;
            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + getDelegate().getIndicatorWidth());
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        IndicatorPoint p = (IndicatorPoint) animation.getAnimatedValue();
        mIndicatorRect.left = (int) p.left;
        mIndicatorRect.right = (int) p.right;

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        if (getDelegate().getIndicatorWidth() < 0) {

        } else {//indicatorWidth大于0时,圆角矩形以及三角形
            float indicatorLeft = p.left + (currentTabView.getWidth() - getDelegate().getIndicatorWidth()) / 2;

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + getDelegate().getIndicatorWidth());
        }
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
        // draw divider
        if (getDelegate().getDividerWidth() > 0) {
            mDividerPaint.setStrokeWidth(getDelegate().getDividerWidth());
            mDividerPaint.setColor(getDelegate().getDividerColor());
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), getDelegate().getDividerPadding(), paddingLeft + tab.getRight(), height - getDelegate().getDividerPadding(), mDividerPaint);
            }
        }

        // draw underline
        if (getDelegate().getUnderlineHeight() > 0) {
            mRectPaint.setColor(getDelegate().getUnderlineColor());
            if (getDelegate().getUnderlineGravity() == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - getDelegate().getUnderlineHeight(), mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, getDelegate().getUnderlineHeight(), mRectPaint);
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


        if (getDelegate().getIndicatorStyle() == IndicatorStyle.TRIANGLE) {
            if (getDelegate().getIndicatorHeight() > 0) {
                mTrianglePaint.setColor(getDelegate().getIndicatorColor());
                mTrianglePath.reset();
                mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2 + mIndicatorRect.right / 2, height - getDelegate().getIndicatorHeight());
                mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                mTrianglePath.close();
                canvas.drawPath(mTrianglePath, mTrianglePaint);
            }
        } else if (getDelegate().getIndicatorStyle() == IndicatorStyle.BLOCK) {
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
                        (int) (getDelegate().getIndicatorMarginTop() + getDelegate().getIndicatorHeight()));
                mIndicatorDrawable.setCornerRadius(getDelegate().getIndicatorCornerRadius());
                mIndicatorDrawable.draw(canvas);
            }
        } else {
            if (getDelegate().getIndicatorHeight() > 0) {
                mIndicatorDrawable.setColor(getDelegate().getIndicatorColor());
                if (getDelegate().getIndicatorGravity() == Gravity.BOTTOM) {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                            height - (int) getDelegate().getIndicatorHeight() - (int) getDelegate().getIndicatorMarginBottom(),
                            paddingLeft + mIndicatorRect.right - (int) getDelegate().getIndicatorMarginRight(),
                            height - (int) getDelegate().getIndicatorMarginBottom());
                } else {
                    mIndicatorDrawable.setBounds(paddingLeft + (int) getDelegate().getIndicatorMarginLeft() + mIndicatorRect.left,
                            (int) getDelegate().getIndicatorMarginTop(),
                            paddingLeft + mIndicatorRect.right - (int) getDelegate().getIndicatorMarginRight(),
                            (int) getDelegate().getIndicatorHeight() + (int) getDelegate().getIndicatorMarginTop());
                }
                mIndicatorDrawable.setCornerRadius(getDelegate().getIndicatorCornerRadius());
                mIndicatorDrawable.draw(canvas);
            }
        }
    }

    /**
     * 设置当前选中Tab
     *
     * @param currentTab
     * @return
     */
    public CommonTabLayout setCurrentTab(int currentTab) {
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

    public ImageView getIconView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        ImageView iv_tab_icon = tabView.findViewById(R.id.iv_tab_icon);
        return iv_tab_icon;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        TextView tv_tab_title = tabView.findViewById(R.id.tv_tab_title);
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
    public CommonTabLayout showMsg(int position, int num) {
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

            if (!getDelegate().isIconVisible()) {
                setMsgMargin(position, 2f, 2f);
            } else {
                setMsgMargin(position, 0,
                        getDelegate().getIconGravity() == Gravity.LEFT || getDelegate().getIconGravity() == Gravity.RIGHT ? 4 : 0);
            }

            mInitSetMap.put(position, true);
        }
        return this;
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public CommonTabLayout showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        return showMsg(position, 0);
    }

    public CommonTabLayout hideMsg(int position) {
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
     * 新增方法
     * 设置提示红点偏移,注意
     * 1.控件为固定高度:参照点为tab内容的右上角
     * 2.控件高度不固定(WRAP_CONTENT):参照点为tab内容的右上角,此时高度已是红点的最高显示范围,所以这时bottomPadding其实就是topPadding
     *
     * @param position
     * @param leftPadding
     * @param bottomPadding
     * @return
     */
    public CommonTabLayout setMsgMargin(int position, int leftPadding, int bottomPadding) {
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

            float iconH = getDelegate().getIconHeight();
            float margin = 0;
            if (getDelegate().isIconVisible()) {
                if (iconH <= 0) {
                    iconH = mContext.getResources().getDrawable(mTabEntity.get(position).getTabSelectedIcon()).getIntrinsicHeight();
                }
                margin = getDelegate().getIconMargin();
            }

            if (getDelegate().getIconGravity() == Gravity.TOP || getDelegate().getIconGravity() == Gravity.BOTTOM) {
                lp.leftMargin = leftPadding;
                lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight - iconH - margin) / 2 - bottomPadding : bottomPadding;
            } else {
                lp.leftMargin = leftPadding;
                lp.topMargin = mHeight > 0 ? (int) (mHeight - Math.max(textHeight, iconH)) / 2 - bottomPadding : bottomPadding;
            }

            tipView.setLayoutParams(lp);
        }
        return this;
    }

    public CommonTabLayout setMsgMargin(int position, float leftPadding, float bottomPadding) {
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

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
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
