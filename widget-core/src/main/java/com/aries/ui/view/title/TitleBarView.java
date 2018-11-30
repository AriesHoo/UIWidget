package com.aries.ui.view.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.helper.navigation.KeyboardHelper;
import com.aries.ui.helper.navigation.NavigationViewHelper;
import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.ResourceUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.alpha.AlphaImageView;
import com.aries.ui.view.alpha.AlphaTextView;
import com.aries.ui.view.title.util.ViewGroupUtils;
import com.aries.ui.widget.R;

/**
 * @Author: AriesHoo on 2018/7/19 10:21
 * @E-Mail: AriesHoo@126.com
 * Function: 定制标题栏-支持沉浸式
 * Description:
 * 1、2017-11-21 10:30:14 AriesHoo 修改onMeasure及onLayout回调控制宽度获取TitleBarView实际宽度(之前为屏幕宽度)
 * 2、2017-12-4 10:09:50 AriesHoo 修改onMeasure中重新测量中间Layout时机避免TitleBarView测量显示错误(目前在Fragment嵌套在FragmentLayout里会出现不显示Title BUG)
 * 3、2018-2-3 10:39:42 属性大改造-去掉之前设置背景色和背景资源id 属性统一用对应的background 属性控制,对应的java方法也会有相应的调整;TextColor修改成color|reference对应ColorStateList方便设置状态颜色
 * 4、2018-2-7 10:27:28 将 setBottomEditTextControl方法废弃;通过{@link NavigationViewHelper}或者{@link KeyboardHelper}类控制底部导航栏
 * 5、2018-3-29 09:21:17 通过ResourceUtil获取资源
 * 6、2018-3-29 12:02:53 删除废弃方法 setBottomEditTextControl
 * 7、2018-3-30 10:43:49 设置View按下alpha 控制属性{@link #setViewPressedAlpha(float)}
 * 8、2018-4-4 15:06:21 调整设置{@link #setOutPadding(int)}以便增加左右TextView 点击范围
 * 9、2018-10-8 13:50:42 修改xml设置主标题{@link #setTitleMainText(CharSequence)}无法实时显示问题
 * 10、2018-11-15 16:05:01 修改设置TextDrawable逻辑--避免将已设置的drawable覆盖掉{@link #setRightTextDrawable(Drawable)}{@link #setLeftTextDrawable(Drawable)}
 * 11、2018-11-16 10:12:34 新增TextView/ImageView tint和tintMode属性及对应方法
 * 12、2018-11-16 10:15:10 新增设置color 资源id相关方法
 * 13、2018-11-16 11:21:23 新增是否增加状态栏高度属性title_statusBarPlusEnable--慎用(一般当TitleBarView不在状态栏下边但是长得又像时使用)
 * 14、2018-11-20 15:09:23 修改addAction逻辑避免生产null对象View
 */
public class TitleBarView extends ViewGroup {

    /**
     * 默认透明度--5.0以上优化半透明状态栏一致
     */
    public static final int DEFAULT_STATUS_BAR_ALPHA = 102;
    /**
     * 默认文本颜色
     */
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    /**
     * 主标题size dp
     */
    private static final float DEFAULT_MAIN_TEXT_SIZE = 18;
    /**
     * 文本默认size dp
     */
    private static final float DEFAULT_TEXT_SIZE = 14;
    /**
     * 副标题默认size dp
     */
    private static final float DEFAULT_SUB_TEXT_SIZE = 14;
    /**
     * 左右padding dp--ToolBar默认16dp
     */
    private static final float DEFAULT_OUT_PADDING = 12;
    /**
     * 左右padding dp--ToolBar默认32dp
     */
    private static final float DEFAULT_CENTER_GRAVITY_LEFT_PADDING = 24;
    /**
     * 状态栏高度
     */
    private int mStatusBarHeight;
    /**
     * TitleBarView实际占用宽度
     */
    private int mScreenWidth;

    private Context mContext;
    /**
     * 自定义View-状态栏View-用于单独设置颜色
     */
    private View mVStatus;
    /**
     * 左边容器
     */
    private LinearLayout mLLayoutLeft;
    /**
     * 中间容器
     */
    private LinearLayout mLLayoutCenter;
    /**
     * 右边容器
     */
    private LinearLayout mLLayoutRight;
    /**
     * 左边TextView
     */
    private AlphaTextView mTvLeft;
    /**
     * 主标题
     */
    private TextView mTvTitleMain;
    /**
     * 副标题
     */
    private TextView mTvTitleSub;
    /**
     * 右边TextView
     */
    private AlphaTextView mTvRight;
    /**
     * 下方下划线
     */
    private View mVDivider;

    /**
     * 是否增加状态栏高度
     */
    private boolean mStatusBarPlusEnable = true;
    /**
     * 设置状态栏浅色或深色模式类型标记;>0则表示支持模式切换
     */
    private int mStatusBarModeType = StatusBarUtil.STATUS_BAR_TYPE_DEFAULT;
    /**
     * xml属性
     */
    private boolean mImmersible = false;
    private int mOutPadding;
    private int mActionPadding;
    /**
     * 中间部分是Layout左右padding
     */
    private int mCenterLayoutPadding;
    /**
     * 中间部分是否左对齐--默认居中
     */
    private boolean mCenterGravityLeft = false;
    /**
     * 中间部分左对齐是Layout左padding
     */
    private int mCenterGravityLeftPadding;
    /**
     * 是否浅色状态栏(黑色文字及图标)
     */
    private boolean mStatusBarLightMode = false;
    private float mViewPressedAlpha;

    private Drawable mStatusBackground;
    private Drawable mDividerBackground;
    private int mDividerHeight;
    private boolean mDividerVisible;

    private CharSequence mLeftText;
    private int mLeftTextSize;
    private ColorStateList mLeftTextColor;
    private Drawable mLeftTextBackground;
    private Drawable mLeftTextDrawable;
    private ColorStateList mLeftTextDrawableTint;
    private PorterDuff.Mode mLeftTextDrawableTintMode;
    private int mLeftTextDrawableWidth;
    private int mLeftTextDrawableHeight;
    private int mLeftTextDrawablePadding;

    private CharSequence mTitleMainText;
    private int mTitleMainTextSize;
    private ColorStateList mTitleMainTextColor;
    private Drawable mTitleMainTextBackground;
    private boolean mTitleMainTextFakeBold;
    private boolean mTitleMainTextMarquee;

    private CharSequence mTitleSubText;
    private int mTitleSubTextSize;
    private ColorStateList mTitleSubTextColor;
    private Drawable mTitleSubTextBackground;
    private boolean mTitleSubTextFakeBold;
    private boolean mTitleSubTextMarquee;

    private CharSequence mRightText;
    private int mRightTextSize;
    private ColorStateList mRightTextColor;
    private Drawable mRightTextBackground;
    private Drawable mRightTextDrawable;
    private ColorStateList mRightTextDrawableTint;
    private PorterDuff.Mode mRightTextDrawableTintMode;
    private int mRightTextDrawableWidth;
    private int mRightTextDrawableHeight;
    private int mRightTextDrawablePadding;


    private int mActionTextSize;
    private ColorStateList mActionTextColor;
    private Drawable mActionTextBackground;
    private ColorStateList mActionTint;
    private PorterDuff.Mode mActionTintMode;
    private Rect mTitleContainerRect;
    private ResourceUtil mResourceUtil;

    public TitleBarView(Context context) {
        this(context, null, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mResourceUtil = new ResourceUtil(mContext);
        initAttributes(context, attrs);
        initView(context);
        setViewAttributes(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mImmersible = ta.getBoolean(R.styleable.TitleBarView_title_immersible, true);
        mStatusBarPlusEnable = ta.getBoolean(R.styleable.TitleBarView_title_statusBarPlusEnable, true);
        mOutPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_outPadding, dip2px(DEFAULT_OUT_PADDING));
        mActionPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionPadding, dip2px(2));
        mCenterLayoutPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_centerLayoutPadding, dip2px(2));
        mCenterGravityLeft = ta.getBoolean(R.styleable.TitleBarView_title_centerGravityLeft, false);
        mCenterGravityLeftPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_centerGravityLeftPadding, dip2px(DEFAULT_CENTER_GRAVITY_LEFT_PADDING));
        mStatusBarLightMode = ta.getBoolean(R.styleable.TitleBarView_title_statusBarLightMode, false);
        mViewPressedAlpha = ta.getFloat(R.styleable.TitleBarView_title_viewPressedAlpha, mResourceUtil.getAttrFloat(R.attr.pressedAlpha));

        mStatusBackground = ta.getDrawable(R.styleable.TitleBarView_title_statusBackground);
        mDividerBackground = ta.getDrawable(R.styleable.TitleBarView_title_dividerBackground);
        mDividerHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_dividerHeight, dip2px(0.5f));
        mDividerVisible = ta.getBoolean(R.styleable.TitleBarView_title_dividerVisible, true);

        mLeftText = ta.getString(R.styleable.TitleBarView_title_leftText);
        mLeftTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mLeftTextColor = ta.getColorStateList(R.styleable.TitleBarView_title_leftTextColor);
        mLeftTextBackground = ta.getDrawable(R.styleable.TitleBarView_title_leftTextBackground);
        mLeftTextDrawable = ta.getDrawable(R.styleable.TitleBarView_title_leftTextDrawable);
        mLeftTextDrawableTint = ta.getColorStateList(R.styleable.TitleBarView_title_leftTextDrawableTint);
        mLeftTextDrawableTintMode = parseTintMode(ta.getInt(R.styleable.TitleBarView_title_leftTextDrawableTintMode, -1), mLeftTextDrawableTintMode);
        mLeftTextDrawableWidth = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawableWidth, -1);
        mLeftTextDrawableHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawableHeight, -1);
        mLeftTextDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawablePadding, dip2px(1));

        mTitleMainText = ta.getString(R.styleable.TitleBarView_title_titleMainText);
        mTitleMainTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleMainTextSize, dip2px(DEFAULT_MAIN_TEXT_SIZE));
        mTitleMainTextColor = ta.getColorStateList(R.styleable.TitleBarView_title_titleMainTextColor);
        mTitleMainTextBackground = ta.getDrawable(R.styleable.TitleBarView_title_titleMainTextBackground);
        mTitleMainTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextFakeBold, false);
        mTitleMainTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextMarquee, false);

        mTitleSubText = ta.getString(R.styleable.TitleBarView_title_titleSubText);
        mTitleSubTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleSubTextSize, dip2px(DEFAULT_SUB_TEXT_SIZE));
        mTitleSubTextColor = ta.getColorStateList(R.styleable.TitleBarView_title_titleSubTextColor);
        mTitleSubTextBackground = ta.getDrawable(R.styleable.TitleBarView_title_titleSubTextBackground);
        mTitleSubTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextFakeBold, false);
        mTitleSubTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextMarquee, false);

        mRightText = ta.getString(R.styleable.TitleBarView_title_rightText);
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mRightTextColor = ta.getColorStateList(R.styleable.TitleBarView_title_rightTextColor);
        mRightTextBackground = ta.getDrawable(R.styleable.TitleBarView_title_rightTextBackground);
        mRightTextDrawable = ta.getDrawable(R.styleable.TitleBarView_title_rightTextDrawable);
        mRightTextDrawableTint = ta.getColorStateList(R.styleable.TitleBarView_title_rightTextDrawableTint);
        mRightTextDrawableTintMode = parseTintMode(ta.getInt(R.styleable.TitleBarView_title_rightTextDrawableTintMode, -1), mRightTextDrawableTintMode);
        mRightTextDrawableWidth = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawableWidth, -1);
        mRightTextDrawableHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawableHeight, -1);
        mRightTextDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawablePadding, dip2px(1));

        mActionTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mActionTextColor = ta.getColorStateList(R.styleable.TitleBarView_title_actionTextColor);
        mActionTextBackground = ta.getDrawable(R.styleable.TitleBarView_title_actionTextBackground);
        mActionTint = ta.getColorStateList(R.styleable.TitleBarView_title_actionTint);
        mActionTintMode = parseTintMode(ta.getInt(R.styleable.TitleBarView_title_actionTintMode, -1), mActionTintMode);
        ta.recycle();//回收
    }

    public PorterDuff.Mode parseTintMode(int value, PorterDuff.Mode defaultMode) {
        switch (value) {
            case 3:
                return PorterDuff.Mode.SRC_OVER;
            case 5:
                return PorterDuff.Mode.SRC_IN;
            case 9:
                return PorterDuff.Mode.SRC_ATOP;
            case 14:
                return PorterDuff.Mode.MULTIPLY;
            case 15:
                return PorterDuff.Mode.SCREEN;
            case 16:
                return PorterDuff.Mode.ADD;
            default:
                return defaultMode;
        }
    }

    /**
     * 初始化子View
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams dividerParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, mDividerHeight);

        mLLayoutLeft = new LinearLayout(context);
        mLLayoutCenter = new LinearLayout(context);
        mLLayoutRight = new LinearLayout(context);
        mVStatus = new View(context);
        mVDivider = new View(context);

        mLLayoutLeft.setGravity(Gravity.CENTER_VERTICAL);
        mLLayoutCenter.setOrientation(LinearLayout.VERTICAL);
        mLLayoutRight.setGravity(Gravity.CENTER_VERTICAL);

        mTvLeft = new AlphaTextView(context);
        mTvLeft.setGravity(Gravity.CENTER);
        mTvLeft.setLines(1);

        mTvTitleMain = new TextView(context);
        mTvTitleSub = new TextView(context);

        mTvRight = new AlphaTextView(context);
        mTvRight.setGravity(Gravity.CENTER);
        mTvRight.setLines(1);

        mLLayoutLeft.addView(mTvLeft, params);
        mLLayoutRight.addView(mTvRight, params);
        //添加左边容器
        addView(mLLayoutLeft, params);
        //添加中间容器
        addView(mLLayoutCenter, params);
        //添加右边容器
        addView(mLLayoutRight, params);
        //添加下划线View
        addView(mVDivider, dividerParams);
        //添加状态栏View
        addView(mVStatus);
    }

    /**
     * 设置xml默认属性
     *
     * @param context
     */
    private void setViewAttributes(final Context context) {
        mScreenWidth = getMeasuredWidth();
        mStatusBarHeight = getStatusBarHeight();
        if (context instanceof Activity) {
            setImmersible((Activity) context, mImmersible);
            if (mStatusBarLightMode) {
                setStatusBarLightMode(mStatusBarLightMode);
            }
        }
        setOutPadding(mOutPadding);
        setActionPadding(mActionPadding);
        setCenterLayoutPadding(mCenterLayoutPadding);
        setCenterGravityLeft(mCenterGravityLeft);
        setStatusBackground(mStatusBackground);
        setDividerBackground(mDividerBackground);
        setDividerHeight(mDividerHeight);
        setDividerVisible(mDividerVisible);
        setViewPressedAlpha(mViewPressedAlpha);

        setLeftText(mLeftText);
        setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        setLeftTextColor(DEFAULT_TEXT_COLOR);
        setLeftTextColor(mLeftTextColor);
        setLeftTextBackground(mLeftTextBackground);
        setLeftTextDrawable(mLeftTextDrawable);
        setLeftTextDrawableTint(mLeftTextDrawableTint);
        setLeftTextDrawableTintMode(mLeftTextDrawableTintMode);
        setLeftTextDrawableWidth(mLeftTextDrawableWidth);
        setLeftTextDrawableHeight(mLeftTextDrawableHeight);
        setLeftTextDrawablePadding(mLeftTextDrawablePadding);

        setTitleMainText(mTitleMainText);
        setTitleMainTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleMainTextSize);
        setTitleMainTextColor(DEFAULT_TEXT_COLOR);
        setTitleMainTextColor(mTitleMainTextColor);
        setTitleMainTextBackground(mTitleMainTextBackground);
        setTitleMainTextFakeBold(mTitleMainTextFakeBold);
        setTitleMainTextMarquee(mTitleMainTextMarquee);

        setTitleSubText(mTitleSubText);
        setTitleSubTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSubTextSize);
        setTitleSubTextColor(DEFAULT_TEXT_COLOR);
        setTitleSubTextColor(mTitleSubTextColor);
        setTitleSubTextBackground(mTitleSubTextBackground);
        setTitleSubTextFakeBold(mTitleSubTextFakeBold);
        setTitleSubTextMarquee(mTitleSubTextMarquee);

        setRightText(mRightText);
        setRightTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        setRightTextColor(DEFAULT_TEXT_COLOR);
        setRightTextColor(mRightTextColor);
        setRightTextBackground(mRightTextBackground);
        setRightTextDrawable(mRightTextDrawable);
        setRightTextDrawable(mRightTextDrawable);
        setRightTextDrawableTint(mRightTextDrawableTint);
        setRightTextDrawableWidth(mRightTextDrawableWidth);
        setRightTextDrawableHeight(mRightTextDrawableHeight);
        setRightTextDrawablePadding(mRightTextDrawablePadding);
    }


    public Rect getTitleContainerRect() {
        if (mTitleContainerRect == null) {
            mTitleContainerRect = new Rect();
        }
        if (mLLayoutCenter == null) {
            mTitleContainerRect.set(0, 0, 0, 0);
        } else {
            ViewGroupUtils.getDescendantRect(this, mLLayoutCenter, mTitleContainerRect);
        }
        mTitleContainerRect.set(mTitleContainerRect.left + mLLayoutCenter.getPaddingLeft(),
                mTitleContainerRect.top, mTitleContainerRect.right, mTitleContainerRect.bottom);
        return mTitleContainerRect;
    }

    /**
     * 根据位置获取 LinearLayout
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public LinearLayout getLinearLayout(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            return mLLayoutLeft;
        } else if (gravity == Gravity.CENTER) {
            return mLLayoutCenter;
        } else if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
            return mLLayoutRight;
        }
        return mLLayoutCenter;
    }

    /**
     * 根据位置获取TextView
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public TextView getTextView(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            return mTvLeft;
        } else if (gravity == (Gravity.CENTER | Gravity.TOP)) {
            return mTvTitleMain;
        } else if (gravity == (Gravity.CENTER | Gravity.BOTTOM)) {
            return mTvTitleSub;
        } else if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
            return mTvRight;
        }
        return mTvTitleMain;
    }

    /**
     * 根据位置获取View
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public View getView(int gravity) {
        if (gravity == Gravity.TOP) {
            return mVStatus;
        } else if (gravity == Gravity.BOTTOM) {
            return mVDivider;
        }
        return mVStatus;
    }

    /**
     * 获取设置状态栏文字图标样式模式
     *
     * @return >0则表示设置成功 参考{@link StatusBarUtil}
     */
    public int getStatusBarModeType() {
        return mStatusBarModeType;
    }

    public TitleBarView setImmersible(Activity activity, boolean immersible) {
        return setImmersible(activity, immersible, mStatusBarPlusEnable);
    }

    public TitleBarView setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar) {
        return setImmersible(activity, immersible, isTransStatusBar, mStatusBarPlusEnable);
    }

    /**
     * 设置沉浸式状态栏，4.4以上系统支持
     *
     * @param activity
     * @param immersible       是否沉浸
     * @param isTransStatusBar 是否透明状态栏 --xml未设置statusBackground 属性才会执行
     * @param isPlusStatusBar  是否增加状态栏高度--用于控制底部有输入框 (设置false/xml背景色必须保持和状态栏一致)
     */
    public TitleBarView setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar, boolean isPlusStatusBar) {
        this.mImmersible = immersible;
        this.mStatusBarPlusEnable = isPlusStatusBar;
        mStatusBarHeight = getNeedStatusBarHeight();
        if (activity == null) {
            return this;
        }
        //透明状态栏
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVStatus.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, mStatusBarHeight));
            // 透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.getDecorView().setSystemUiVisibility(window.getDecorView().getSystemUiVisibility()
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
        }
        setStatusAlpha(immersible ? isTransStatusBar ? 0 : 102 : 255);
        return this;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //实时获取避免因横竖屏切换造成测量错误
        mScreenWidth = getMeasuredWidth();
        mStatusBarHeight = getNeedStatusBarHeight();
        int left = mLLayoutLeft.getMeasuredWidth();
        int right = mLLayoutRight.getMeasuredWidth();
        int center = mLLayoutCenter.getMeasuredWidth();
        mLLayoutLeft.layout(0, isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2, left, mLLayoutLeft.getMeasuredHeight() + mStatusBarHeight);
        mLLayoutRight.layout(mScreenWidth - right, isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2, mScreenWidth, mLLayoutRight.getMeasuredHeight() + mStatusBarHeight);
        boolean isMuchScreen = left + right + center >= mScreenWidth;
        if (left > right) {
            mLLayoutCenter.layout(left, mStatusBarHeight, isMuchScreen ? mScreenWidth - right : mScreenWidth - left, getMeasuredHeight() - mDividerHeight);
        } else {
            mLLayoutCenter.layout(isMuchScreen ? left : right, mStatusBarHeight, mScreenWidth - right, getMeasuredHeight() - mDividerHeight);
        }
        mVDivider.layout(0, getMeasuredHeight() - mVDivider.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        mVStatus.layout(0, 0, getMeasuredWidth(), mStatusBarHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mStatusBarHeight = getNeedStatusBarHeight();
        measureChild(mLLayoutLeft, widthMeasureSpec, heightMeasureSpec);
        measureChild(mLLayoutRight, widthMeasureSpec, heightMeasureSpec);
        measureChild(mLLayoutCenter, widthMeasureSpec, heightMeasureSpec);
        measureChild(mVDivider, widthMeasureSpec, heightMeasureSpec);
        measureChild(mVStatus, widthMeasureSpec, heightMeasureSpec);
        //重新测量宽高--增加状态栏及下划线的高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec) + (isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2) + mDividerHeight);
        mScreenWidth = getMeasuredWidth();

        int left = mLLayoutLeft.getMeasuredWidth();
        int right = mLLayoutRight.getMeasuredWidth();
        int center = mLLayoutCenter.getMeasuredWidth();
        //判断左、中、右实际占用宽度是否等于或者超过屏幕宽度
        boolean isMuchScreen = left + right + center >= mScreenWidth;
        if (mCenterGravityLeft) {
            return;
        }
        //不设置中间布局左对齐才进行中间布局重新测量
        if (isMuchScreen) {
            center = mScreenWidth - left - right;
        } else {
            if (left > right) {
                center = mScreenWidth - 2 * left;
            } else {
                center = mScreenWidth - 2 * right;
            }
        }
        mLLayoutCenter.measure(MeasureSpec.makeMeasureSpec(center, MeasureSpec.EXACTLY), heightMeasureSpec);
    }

    /**
     * 设置TitleBarView高度--不包含状态栏及下划线
     *
     * @param height
     * @return
     */
    public TitleBarView setHeight(int height) {
        ViewGroup.LayoutParams params = getLayoutParams();
        if (params != null) {
            params.height = height;
        }
        return this;
    }

    public TitleBarView setBgDrawable(Drawable background) {
        return setViewBackground(this, background);
    }

    public TitleBarView setBgColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    public TitleBarView setBgResource(int res) {
        super.setBackgroundResource(res);
        return this;
    }

    /**
     * 距左右边距--根据具体情况设置
     *
     * @param paddingValue
     * @return
     */
    public TitleBarView setOutPadding(int paddingValue) {
        mOutPadding = paddingValue;
        if (TextUtils.isEmpty(mLeftText)
                && mLeftTextDrawable == null
                || mLLayoutLeft.indexOfChild(mTvLeft) != 0) {
            mLLayoutLeft.setPadding(mOutPadding, 0, 0, 0);
            mTvLeft.setPadding(0, 0, 0, 0);
        } else {
            mLLayoutLeft.setPadding(0, 0, 0, 0);
            mTvLeft.setPadding(mOutPadding, 0, mActionPadding, 0);
        }
        if (TextUtils.isEmpty(mRightText)
                && mRightTextDrawable == null
                || mLLayoutRight.indexOfChild(mTvRight) != mLLayoutRight.getChildCount() - 1) {
            mLLayoutRight.setPadding(0, 0, mOutPadding, 0);
            mTvRight.setPadding(0, 0, 0, 0);
        } else {
            mLLayoutRight.setPadding(0, 0, 0, 0);
            mTvRight.setPadding(mActionPadding, 0, mOutPadding, 0);
        }
        return this;
    }

    public TitleBarView setCenterLayoutPadding(int centerLayoutPadding) {
        this.mCenterLayoutPadding = centerLayoutPadding;
        mLLayoutCenter.setPadding(mCenterLayoutPadding, mLLayoutCenter.getPaddingTop(), mCenterLayoutPadding, mLLayoutCenter.getPaddingBottom());
        return this;
    }

    /**
     * 设置中间是否左对齐
     *
     * @param enable
     * @return
     */
    public TitleBarView setCenterGravityLeft(boolean enable) {
        this.mCenterGravityLeft = enable;
        mTvTitleMain.setGravity(mCenterGravityLeft ? Gravity.LEFT : Gravity.CENTER);
        mLLayoutCenter.setGravity(mCenterGravityLeft ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.CENTER);
        mTvTitleSub.setGravity(mCenterGravityLeft ? Gravity.LEFT : Gravity.CENTER);
        return setCenterGravityLeftPadding(mCenterGravityLeftPadding);
    }

    /**
     * 设置title 左边距--当设置setCenterGravityLeft(true)生效
     *
     * @param padding
     * @return
     */
    public TitleBarView setCenterGravityLeftPadding(int padding) {
        if (mCenterGravityLeft) {
            mCenterGravityLeftPadding = padding;
            mLLayoutCenter.setPadding(mCenterGravityLeftPadding, mLLayoutCenter.getTop(), mLLayoutCenter.getPaddingRight(), mLLayoutCenter.getPaddingBottom());
        } else {
            return setCenterLayoutPadding(mCenterLayoutPadding);
        }
        return this;
    }

    public TitleBarView setStatusBarLightMode(boolean mStatusBarLightMode) {
        if (mContext instanceof Activity) {
            return setStatusBarLightMode((Activity) mContext, mStatusBarLightMode);
        }
        return this;
    }

    /**
     * 设置状态栏文字黑白颜色切换
     *
     * @param mActivity
     * @param mStatusBarLightMode
     * @return
     */
    public TitleBarView setStatusBarLightMode(Activity mActivity, boolean mStatusBarLightMode) {
        this.mStatusBarLightMode = mStatusBarLightMode;
        if (mActivity != null) {
            if (mStatusBarLightMode) {
                mStatusBarModeType = StatusBarUtil.setStatusBarLightMode(mActivity);
            } else {
                mStatusBarModeType = StatusBarUtil.setStatusBarDarkMode(mActivity);
            }
        }
        return this;
    }

    /**
     * 返回是否支持状态栏颜色切换
     *
     * @return
     */
    public boolean isStatusBarLightModeEnable() {
        return StatusBarUtil.isSupportStatusBarFontChange();
    }

    /**
     * 设置view左右两边内边距
     *
     * @param actionPadding
     * @return
     */
    public TitleBarView setActionPadding(int actionPadding) {
        mActionPadding = actionPadding;
        return this;
    }

    /**
     * 设置状态栏背景
     *
     * @param drawable
     * @return
     */
    public TitleBarView setStatusBackground(Drawable drawable) {
        mStatusBackground = drawable;
        return setViewBackground(mVStatus, mStatusBackground);
    }

    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    public TitleBarView setStatusBackgroundColor(int color) {
        return setStatusBackground(new ColorDrawable(color));
    }

    /**
     * 设置透明度
     *
     * @param alpha
     * @return
     */
    public TitleBarView setStatusAlpha(int alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 255) {
            alpha = 255;
        }
        return setStatusBackgroundColor(Color.argb(alpha, 0, 0, 0));
    }

    public TitleBarView setStatusBackgroundResource(int resId) {
        return setStatusBackground(mResourceUtil.getDrawable(resId));
    }

    /**
     * 设置下划线背景
     *
     * @param drawable
     * @return
     */
    public TitleBarView setDividerBackground(Drawable drawable) {
        mDividerBackground = drawable;
        return setViewBackground(mVDivider, mDividerBackground);
    }

    public TitleBarView setDividerBackgroundColor(int color) {
        return setDividerBackground(new ColorDrawable(color));
    }

    public TitleBarView setDividerBackgroundResource(int resId) {
        return setDividerBackground(mResourceUtil.getDrawable(resId));
    }

    public TitleBarView setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
        mVDivider.getLayoutParams().height = dividerHeight;
        return this;
    }

    public TitleBarView setDividerVisible(boolean visible) {
        mDividerVisible = visible;
        mVDivider.setVisibility(visible ? VISIBLE : GONE);
        return this;
    }

    /**
     * 设置view点击按下时alpha变化
     *
     * @param alpha
     * @return
     */
    public TitleBarView setViewPressedAlpha(float alpha) {
        if (alpha > 1) {
            alpha = 1.0f;
        } else if (alpha < 0) {
            alpha = 0f;
        }
        this.mViewPressedAlpha = alpha;
        mTvLeft.getDelegate().getAlphaViewHelper().setPressedAlpha(mViewPressedAlpha);
        mTvRight.getDelegate().getAlphaViewHelper().setPressedAlpha(mViewPressedAlpha);
        return this;
    }

    /**
     * 设置所有TextView的文本颜色--注意和其它方法的先后顺序
     *
     * @param color
     * @return
     */
    public TitleBarView setTextColor(int color) {
        return setLeftTextColor(color)
                .setTitleMainTextColor(color)
                .setTitleSubTextColor(color)
                .setRightTextColor(color)
                .setActionTextColor(color);
    }

    public TitleBarView setTextColor(ColorStateList colors) {
        return setLeftTextColor(colors)
                .setTitleMainTextColor(colors)
                .setTitleSubTextColor(colors)
                .setRightTextColor(colors)
                .setActionTextColor(colors);
    }

    public TitleBarView setTextColorResource(int res) {
        return setTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setLeftText(CharSequence title) {
        mLeftText = title;
        mTvLeft.setText(title);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView setLeftText(int id) {
        return setLeftText(mResourceUtil.getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit 文字单位{@link TypedValue}
     * @param size
     * @return
     */
    public TitleBarView setLeftTextSize(int unit, float size) {
        mTvLeft.setTextSize(unit, size);
        return this;
    }

    public TitleBarView setLeftTextSize(float size) {
        return setLeftTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBarView setLeftTextColor(int color) {
        mTvLeft.setTextColor(color);
        return this;
    }

    /**
     * 设置文字状态颜色-如按下颜色变化
     *
     * @param colors
     * @return
     */
    public TitleBarView setLeftTextColor(ColorStateList colors) {
        if (colors != null) {
            mTvLeft.setTextColor(colors);
        }
        return this;
    }

    public TitleBarView setLeftTextColorResource(int res) {
        return setLeftTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setLeftTextBackground(Drawable drawable) {
        mLeftTextBackground = drawable;
        return setViewBackground(mTvLeft, mLeftTextBackground);
    }

    public TitleBarView setLeftTextBackgroundColor(int color) {
        return setLeftTextBackground(new ColorDrawable(color));
    }

    /**
     * @param resId
     */
    public TitleBarView setLeftTextBackgroundResource(int resId) {
        return setLeftTextBackground(mResourceUtil.getDrawable(resId));
    }

    /**
     * 设置左边图片资源
     *
     * @param drawable
     * @return
     */
    public TitleBarView setLeftTextDrawable(Drawable drawable) {
        mLeftTextDrawable = drawable;
        DrawableUtil.setDrawableWidthHeight(mLeftTextDrawable, mLeftTextDrawableWidth, mLeftTextDrawableHeight);
        Drawable[] drawables = mTvLeft.getCompoundDrawables();
        mTvLeft.setCompoundDrawables(mLeftTextDrawable, drawables[1], drawables[2], drawables[3]);
        setTextDrawableTint(mTvLeft, mLeftTextDrawableTint, mLeftTextDrawableTintMode);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView setLeftTextDrawable(int resId) {
        return setLeftTextDrawable(mResourceUtil.getDrawable(resId));
    }

    public TitleBarView setLeftTextDrawableTint(int color) {
        return setLeftTextDrawableTint(ColorStateList.valueOf(color));
    }

    public TitleBarView setLeftTextDrawableTint(ColorStateList colors) {
        if (colors == null) {
            return this;
        }
        mLeftTextDrawableTint = colors;
        return setTextDrawableTint(mTvLeft, mLeftTextDrawableTint, mLeftTextDrawableTintMode);
    }

    public TitleBarView setLeftTextDrawableTintResource(int res) {
        return setLeftTextDrawableTint(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setLeftTextDrawableTintMode(PorterDuff.Mode mode) {
        if (mode == null) {
            return this;
        }
        mLeftTextDrawableTintMode = mode;
        return setTextDrawableTint(mTvLeft, mLeftTextDrawableTint, mLeftTextDrawableTintMode);
    }

    private TitleBarView setTextDrawableTint(TextView textView, ColorStateList tint, PorterDuff.Mode tintMode) {
        if (tint == null && tintMode == null) {
            return this;
        }
        Drawable[] drawables = textView.getCompoundDrawables();
        for (Drawable item : drawables) {
            if (item != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (tint != null) {
                        DrawableCompat.setTintList(item, tint);
                    }
                    if (tintMode != null) {
                        DrawableCompat.setTintMode(item, tintMode);
                    }
                } else {
                    if (tint != null) {
                        item = item.mutate();
                        item.setColorFilter(tint.getDefaultColor(), tintMode != null ? tintMode : PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
        }
        return this;
    }

    public TitleBarView setLeftTextDrawableWidth(int width) {
        mLeftTextDrawableWidth = width;
        return setLeftTextDrawable(mLeftTextDrawable);
    }

    public TitleBarView setLeftTextDrawableHeight(int height) {
        mLeftTextDrawableHeight = height;
        return setLeftTextDrawable(mLeftTextDrawable);
    }

    public TitleBarView setLeftTextDrawablePadding(int drawablePadding) {
        this.mLeftTextDrawablePadding = drawablePadding;
        mTvLeft.setCompoundDrawablePadding(mLeftTextDrawablePadding);
        return this;
    }

    public TitleBarView setLeftTextPadding(int left, int top, int right, int bottom) {
        mTvLeft.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setOnLeftTextClickListener(OnClickListener l) {
        mTvLeft.setOnClickListener(l);
        return this;
    }

    public TitleBarView setLeftVisible(boolean visible) {
        mTvLeft.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBarView setTitleMainText(int id) {
        return setTitleMainText(mResourceUtil.getText(id));
    }

    public TitleBarView setTitleMainText(CharSequence charSequence) {
        mTvTitleMain.setText(charSequence);
        if (hasChildView(mLLayoutCenter, mTvTitleMain)) {
            return this;
        }
        if (TextUtils.isEmpty(charSequence)) {
            return this;
        }
        //CollapsingTitleBarLayout 作为父控件则不进行操作
        if ((getParent() != null && getParent().getClass().getSimpleName().equals("CollapsingTitleBarLayout"))) {
            return this;
        }
        mLLayoutCenter.addView(mTvTitleMain, 0);
        return this;
    }

    /**
     * {@link TextView#setTextSize(int, float)}
     *
     * @param unit
     * @param titleMainTextSpValue
     * @return
     */
    public TitleBarView setTitleMainTextSize(int unit, float titleMainTextSpValue) {
        mTvTitleMain.setTextSize(unit, titleMainTextSpValue);
        return this;
    }

    /**
     * 设置文字大小 参考{@link TypedValue}
     *
     * @param titleMainTextSpValue
     * @return
     */
    public TitleBarView setTitleMainTextSize(float titleMainTextSpValue) {
        return setTitleMainTextSize(TypedValue.COMPLEX_UNIT_SP, titleMainTextSpValue);
    }

    public TitleBarView setTitleMainTextColor(int color) {
        mTvTitleMain.setTextColor(color);
        return this;
    }

    public TitleBarView setTitleMainTextColor(ColorStateList colors) {
        if (colors != null) {
            mTvTitleMain.setTextColor(colors);
        }
        return this;
    }

    public TitleBarView setTitleMainTextColorResource(int res) {
        return setTitleMainTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setTitleMainTextBackground(Drawable drawable) {
        mTitleMainTextBackground = drawable;
        return setViewBackground(mTvTitleMain, mTitleMainTextBackground);
    }

    public TitleBarView setTitleMainTextBackgroundColor(int color) {
        return setTitleMainTextBackground(new ColorDrawable(color));
    }

    public TitleBarView setTitleMainTextBackgroundResource(int resId) {
        return setTitleMainTextBackground(mResourceUtil.getDrawable(resId));
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public TitleBarView setTitleMainTextFakeBold(boolean isFakeBold) {
        this.mTitleMainTextFakeBold = isFakeBold;
        mTvTitleMain.getPaint().setFakeBoldText(mTitleMainTextFakeBold);
        return this;
    }

    public TitleBarView setTitleMainTextMarquee(boolean enable) {
        this.mTitleMainTextMarquee = enable;
        if (enable) {
            setTitleSubTextMarquee(false);
            mTvTitleMain.setSingleLine();
            mTvTitleMain.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTvTitleMain.setFocusable(true);
            mTvTitleMain.setFocusableInTouchMode(true);
            mTvTitleMain.requestFocus();
            mTvTitleMain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleMainTextMarquee) {
                        mTvTitleMain.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mTvTitleMain.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mTvTitleMain.setMaxLines(1);
            mTvTitleMain.setEllipsize(TextUtils.TruncateAt.END);
            mTvTitleMain.setOnFocusChangeListener(null);
            //关闭硬件加速
            mTvTitleMain.setLayerType(View.LAYER_TYPE_NONE, null);
        }
        return this;
    }

    public TitleBarView setTitleMainTextPadding(int left, int top, int right, int bottom) {
        mTvTitleMain.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setTitleSubText(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().isEmpty()) {
            mTvTitleSub.setVisibility(GONE);
        } else {
            mTvTitleSub.setVisibility(VISIBLE);
        }
        mTvTitleSub.setText(charSequence);
        //非空且还未添加副标题
        if (!TextUtils.isEmpty(charSequence) && !hasChildView(mLLayoutCenter, mTvTitleSub)) {
            if (hasChildView(mLLayoutCenter, mTvTitleMain)) {
                mTvTitleMain.setSingleLine();
                mTvTitleSub.setSingleLine();
            }
            mLLayoutCenter.addView(mTvTitleSub);
        }
        return this;
    }

    public TitleBarView setTitleSubText(int id) {
        return setTitleSubText(mResourceUtil.getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit  单位 参考{@link TypedValue}
     * @param value
     * @return
     */
    public TitleBarView setTitleSubTextSize(int unit, float value) {
        mTvTitleSub.setTextSize(unit, value);
        return this;
    }

    public TitleBarView setTitleSubTextSize(float spValue) {
        return setTitleSubTextSize(TypedValue.COMPLEX_UNIT_SP, spValue);
    }

    public TitleBarView setTitleSubTextColor(int color) {
        mTvTitleSub.setTextColor(color);
        return this;
    }

    public TitleBarView setTitleSubTextColor(ColorStateList colors) {
        if (colors != null) {
            mTvTitleSub.setTextColor(colors);
        }
        return this;
    }

    public TitleBarView setTitleSubTextColorResource(int res) {
        return setTitleSubTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setTitleSubTextBackground(Drawable drawable) {
        mTitleSubTextBackground = drawable;
        return setViewBackground(mTvTitleSub, mTitleSubTextBackground);
    }

    public TitleBarView setTitleSubTextBackgroundColor(int color) {
        return setTitleSubTextBackground(new ColorDrawable(color));
    }

    public TitleBarView setTitleSubTextBackgroundResource(int resId) {
        return setTitleSubTextBackground(mResourceUtil.getDrawable(resId));
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public TitleBarView setTitleSubTextFakeBold(boolean isFakeBold) {
        this.mTitleSubTextFakeBold = isFakeBold;
        mTvTitleSub.getPaint().setFakeBoldText(mTitleSubTextFakeBold);
        return this;
    }

    /**
     * 设置TextView 跑马灯
     *
     * @param enable
     */
    public TitleBarView setTitleSubTextMarquee(boolean enable) {
        this.mTitleSubTextMarquee = enable;
        if (enable) {
            setTitleMainTextMarquee(false);
            mTvTitleSub.setSingleLine();
            mTvTitleSub.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTvTitleSub.setFocusable(true);
            mTvTitleSub.setFocusableInTouchMode(true);
            mTvTitleSub.requestFocus();
            mTvTitleSub.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleSubTextMarquee) {
                        mTvTitleMain.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mTvTitleSub.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mTvTitleSub.setMaxLines(1);
            mTvTitleSub.setEllipsize(TextUtils.TruncateAt.END);
            mTvTitleSub.setOnFocusChangeListener(null);
            //关闭硬件加速
            mTvTitleSub.setLayerType(View.LAYER_TYPE_NONE, null);
        }
        return this;
    }

    public TitleBarView setOnCenterClickListener(OnClickListener l) {
        mLLayoutCenter.setOnClickListener(l);
        return this;
    }

    public TitleBarView setRightText(CharSequence title) {
        mRightText = title;
        mTvRight.setText(title);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView setRightText(int id) {
        return setRightText(mResourceUtil.getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit 单位 参考{@link TypedValue}
     * @param size
     * @return
     */
    public TitleBarView setRightTextSize(int unit, float size) {
        mTvRight.setTextSize(unit, size);
        return this;
    }

    public TitleBarView setRightTextSize(float size) {
        return setRightTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBarView setRightTextColor(int color) {
        mTvRight.setTextColor(color);
        return this;
    }

    public TitleBarView setRightTextColor(ColorStateList colors) {
        if (colors != null) {
            mTvRight.setTextColor(colors);
        }
        return this;
    }

    public TitleBarView setRightTextColorResource(int res) {
        return setRightTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setRightTextBackground(Drawable drawable) {
        mRightTextBackground = drawable;
        return setViewBackground(mTvRight, mRightTextBackground);
    }

    public TitleBarView setRightTextBackgroundColor(int color) {
        return setRightTextBackground(new ColorDrawable(color));
    }

    public TitleBarView setRightTextBackgroundResource(int resId) {
        return setRightTextBackground(mResourceUtil.getDrawable(resId));
    }

    /**
     * 右边文本添加图片
     *
     * @param drawable 资源
     */
    public TitleBarView setRightTextDrawable(Drawable drawable) {
        mRightTextDrawable = drawable;
        DrawableUtil.setDrawableWidthHeight(mRightTextDrawable, mRightTextDrawableWidth, mRightTextDrawableHeight);
        Drawable[] drawables = mTvRight.getCompoundDrawables();
        mTvRight.setCompoundDrawables(drawables[0], drawables[1], mRightTextDrawable, drawables[3]);
        setTextDrawableTint(mTvRight, mRightTextDrawableTint, mRightTextDrawableTintMode);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView setRightTextDrawable(int resId) {
        return setRightTextDrawable(mResourceUtil.getDrawable(resId));
    }

    public TitleBarView setRightTextDrawableTint(int color) {
        return setRightTextDrawableTint(ColorStateList.valueOf(color));
    }

    public TitleBarView setRightTextDrawableTint(ColorStateList colors) {
        if (colors == null) {
            return this;
        }
        mRightTextDrawableTint = colors;
        return setTextDrawableTint(mTvRight, mRightTextDrawableTint, mRightTextDrawableTintMode);
    }

    public TitleBarView setRightTextDrawableTintResource(int res) {
        return setRightTextDrawableTint(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setRightTextDrawableTintMode(PorterDuff.Mode mode) {
        if (mode == null) {
            return this;
        }
        mRightTextDrawableTintMode = mode;
        return setTextDrawableTint(mTvRight, mRightTextDrawableTint, mRightTextDrawableTintMode);
    }

    public TitleBarView setRightTextDrawablePadding(int drawablePadding) {
        this.mRightTextDrawablePadding = drawablePadding;
        mTvRight.setCompoundDrawablePadding(mRightTextDrawablePadding);
        return this;
    }

    public TitleBarView setRightTextDrawableWidth(int width) {
        mRightTextDrawableWidth = width;
        return setRightTextDrawable(mRightTextDrawable);
    }

    public TitleBarView setRightTextDrawableHeight(int height) {
        mRightTextDrawableHeight = height;
        return setRightTextDrawable(mRightTextDrawable);
    }

    public TitleBarView setRightTextPadding(int left, int top, int right, int bottom) {
        mTvRight.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setOnRightTextClickListener(OnClickListener l) {
        mTvRight.setOnClickListener(l);
        return this;
    }

    public TitleBarView setRightVisible(boolean visible) {
        mTvRight.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBarView setActionTextSize(int mActionTextSize) {
        this.mActionTextSize = mActionTextSize;
        return this;
    }

    public TitleBarView setActionTextColor(int mActionTextColor) {
        this.mActionTextColor = ColorStateList.valueOf(mActionTextColor);
        return this;
    }

    public TitleBarView setActionTextColor(ColorStateList mActionTextColor) {
        this.mActionTextColor = mActionTextColor;
        return this;
    }

    public TitleBarView setActionTextColorResource(int res) {
        return setActionTextColor(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setActionTextBackground(Drawable drawable) {
        this.mActionTextBackground = drawable;
        return this;
    }

    public TitleBarView setActionTextBackgroundColor(int color) {
        return setActionTextBackground(new ColorDrawable(color));
    }

    public TitleBarView setActionTextBackgroundResource(int resId) {
        return setActionTextBackground(mResourceUtil.getDrawable(resId));
    }

    public TitleBarView setActionTint(int color) {
        return setActionTint(ColorStateList.valueOf(color));
    }

    public TitleBarView setActionTint(ColorStateList colors) {
        if (colors == null) {
            return this;
        }
        mActionTint = colors;
        return setActionTint();
    }

    public TitleBarView setActionTintResource(int res) {
        return setActionTint(mResourceUtil.getColorStateList(res));
    }

    public TitleBarView setActionTintMode(PorterDuff.Mode mode) {
        if (mode == null) {
            return this;
        }
        mActionTintMode = mode;
        return setActionTint();
    }

    private TitleBarView setActionTint() {
        if (mActionTint == null && mActionTintMode == null) {
            return this;
        }
        int sizeLeft = mLLayoutLeft.getChildCount();
        int sizeCenter = mLLayoutCenter.getChildCount();
        int sizeRight = mLLayoutRight.getChildCount();
        for (int i = 0; i < sizeLeft; i++) {
            View view = mLLayoutLeft.getChildAt(i);
            if (view instanceof TextView) {
                if (view != mTvLeft) {
                    setTextDrawableTint((TextView) view, mActionTint, mActionTintMode);
                }
            } else if (view instanceof ImageView) {
                setImageTint((ImageView) view, mActionTint, mActionTintMode);
            }
        }
        for (int i = 0; i < sizeCenter; i++) {
            View view = mLLayoutCenter.getChildAt(i);
            if (view instanceof TextView) {
                if (view != mTvTitleMain && view != mTvTitleSub) {
                    setTextDrawableTint((TextView) view, mActionTint, mActionTintMode);
                }
            } else if (view instanceof ImageView) {
                setImageTint((ImageView) view, mActionTint, mActionTintMode);
            }
        }
        for (int i = 0; i < sizeRight; i++) {
            View view = mLLayoutRight.getChildAt(i);
            if (view instanceof TextView) {
                if (view != mTvRight) {
                    setTextDrawableTint((TextView) view, mActionTint, mActionTintMode);
                }
            } else if (view instanceof ImageView) {
                setImageTint((ImageView) view, mActionTint, mActionTintMode);
            }
        }
        return this;
    }

    private void setImageTint(ImageView imageView, ColorStateList tint, PorterDuff.Mode tintMode) {
        if (imageView.getDrawable() == null) {
            return;
        }
        if (tint == null && tintMode == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (mActionTint != null) {
                imageView.setImageTintList(mActionTint);
            }
            if (mActionTintMode != null) {
                imageView.setImageTintMode(mActionTintMode);
            }
        } else {
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && mActionTint != null) {
                drawable = drawable.mutate();
                drawable.setColorFilter(mActionTint.getDefaultColor(), mActionTintMode != null ? mActionTintMode : PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

    public TitleBarView addLeftAction(Action action, int position) {
        View view = inflateAction(action);
        if (view == null) {
            return this;
        }
        mLLayoutLeft.addView(view, position);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView addLeftAction(Action action) {
        return addLeftAction(action, -1);
    }

    /**
     * 自定义中间部分布局
     */
    public TitleBarView addCenterAction(Action action, int position) {
        View view = inflateAction(action);
        if (view == null) {
            return this;
        }
        mLLayoutCenter.addView(view, position);
        return this;
    }

    /**
     * 自定义中间部分布局
     */
    public TitleBarView addCenterAction(Action action) {
        return addCenterAction(action, -1);
    }

    /**
     * 在标题栏右边添加action
     *
     * @param action
     * @param position 添加的位置
     */
    public TitleBarView addRightAction(Action action, int position) {
        View view = inflateAction(action);
        if (view == null) {
            return this;
        }
        mLLayoutRight.addView(view, position);
        return setOutPadding(mOutPadding);
    }

    public TitleBarView addRightAction(Action action) {
        return addRightAction(action, -1);
    }

    /**
     * 通过action加载一个View
     *
     * @param action
     * @return
     */
    private View inflateAction(Action action) {
        View view = null;
        Object obj = action.getData();
        if (obj == null)
            return null;
        if (obj instanceof View) {
            view = (View) obj;
        } else if (obj instanceof String) {
            AlphaTextView text = new AlphaTextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText((String) obj);
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActionTextSize);
            if (mActionTextColor != null) {
                text.setTextColor(mActionTextColor);
            } else {
                text.setTextColor(DEFAULT_TEXT_COLOR);
            }
            text.getDelegate().getAlphaViewHelper().setPressedAlpha(mViewPressedAlpha);
            setViewBackground(text, mActionTextBackground);
            view = text;
            setTextDrawableTint(text, mActionTint, mActionTintMode);
        } else if (obj instanceof Drawable) {
            AlphaImageView img = new AlphaImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageDrawable((Drawable) obj);
            img.getDelegate().getAlphaViewHelper().setPressedAlpha(mViewPressedAlpha);
            view = img;
            setImageTint(img, mActionTint, mActionTintMode);
        }
        view.setPadding(mActionPadding, 0, mActionPadding, 0);
        view.setTag(action);
        view.setOnClickListener(action.getOnClickListener());
        return view;
    }

    /**
     * 添加View以及相应的动作接口
     */
    public interface Action<T> {
        T getData();

        OnClickListener getOnClickListener();
    }

    public class ImageAction implements Action<Drawable> {


        private Drawable mDrawable;
        private OnClickListener onClickListener;

        public ImageAction(Drawable mDrawable, OnClickListener onClickListener) {
            this.mDrawable = mDrawable;
            this.onClickListener = onClickListener;
        }

        public ImageAction(int drawableId, OnClickListener onClickListener) {
            this.mDrawable = mResourceUtil.getDrawable(drawableId);
            this.onClickListener = onClickListener;
        }

        public ImageAction(int resId) {
            this.mDrawable = mResourceUtil.getDrawable(resId);
        }

        public ImageAction(Drawable drawable) {
            this.mDrawable = drawable;
        }

        @Override
        public Drawable getData() {
            return mDrawable;
        }

        @Override
        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

    }

    public class TextAction implements Action<CharSequence> {

        private CharSequence mText;
        private OnClickListener onClickListener;

        public TextAction(CharSequence mText, OnClickListener onClickListener) {
            this.mText = mText;
            this.onClickListener = onClickListener;
        }

        public TextAction(CharSequence mText) {
            this.mText = mText;
        }

        public TextAction(int mText) {
            this.mText = mResourceUtil.getText(mText);
        }

        public TextAction(int mText, OnClickListener onClickListener) {
            this.mText = mResourceUtil.getText(mText);
            this.onClickListener = onClickListener;
        }

        @Override
        public CharSequence getData() {
            return mText;
        }

        @Override
        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

    }

    public class ViewAction implements Action<View> {

        private View mView;
        private OnClickListener onClickListener;

        public ViewAction(View mView, OnClickListener onClickListener) {
            this.mView = mView;
            this.onClickListener = onClickListener;
        }

        public ViewAction(View mView) {
            this.mView = mView;
        }

        @Override
        public View getData() {
            return mView;
        }

        @Override
        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

    }

    /**
     * 设置view背景drawable
     *
     * @param view
     * @param drawable
     */
    private TitleBarView setViewBackground(View view, Drawable drawable) {
        if (view == null) {
            return this;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
        return this;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取真实需要的状态栏高度
     *
     * @return
     */
    private int getNeedStatusBarHeight() {
        return isNeedStatusBar() ? getStatusBarHeight() : 0;
    }

    /**
     * 当TitleBarView的父容器为ConstraintLayout(约束布局)时TitleBarView新增的高度会变成状态栏高度2倍需做特殊处理--暂不知原因
     *
     * @return
     */
    private boolean isNormalParent() {
        return !(getParent() != null && getParent().getClass().getSimpleName().contains("ConstraintLayout"));
    }

    private boolean isNeedStatusBar() {
        return mStatusBarPlusEnable && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        return StatusBarUtil.getStatusBarHeight();
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param dipValue dp值
     * @return
     */
    public static int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 判断父控件是否包含某个子View
     *
     * @param father
     * @param child
     * @return
     */
    public static boolean hasChildView(ViewGroup father, View child) {
        boolean had = false;
        try {
            had = father.indexOfChild(child) != -1;
        } catch (Exception e) {
        }
        return had;
    }
}
