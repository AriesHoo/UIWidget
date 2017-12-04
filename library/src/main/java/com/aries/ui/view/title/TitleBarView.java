package com.aries.ui.view.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
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

import com.aries.ui.util.KeyboardUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2017-02-09 09:42
 * E-Mail: AriesHoo@126.com
 * Function:定制标题栏
 * Description:
 * 1、2017-11-21 10:30:14 AriesHoo 修改onMeasure及onLayout回调控制宽度获取TitleBarView实际宽度(之前为屏幕宽度)
 * 2、2017-12-4 10:09:50 AriesHoo 修改onMeasure中重新测量中间Layout时机避免TitleBarView测量显示错误(目前在Fragment嵌套在FragmentLayout里会出现不显示Title BUG)
 */
public class TitleBarView extends ViewGroup {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 102;//默认透明度--5.0以上优化半透明状态栏一致
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;//默认文本颜色
    private static final int DEFAULT_TEXT_BG_COLOR = Color.TRANSPARENT;//默认子View背景色
    private static final float DEFAULT_MAIN_TEXT_SIZE = 18;//主标题size dp
    private static final float DEFAULT_TEXT_SIZE = 14;//文本默认size dp
    private static final float DEFAULT_SUB_TEXT_SIZE = 14;//副标题默认size dp
    private static final float DEFAULT_OUT_PADDING = 12;//左右padding dp--ToolBar默认16dp
    private static final float DEFAULT_CENTER_GRAVITY_LEFT_PADDING = 24;//左右padding dp--ToolBar默认32dp

    private int mStatusBarHeight;//状态栏高度
    private int mScreenWidth;//TitleBarView实际占用宽度
    private int systemUiVisibility;//Activity systemUiVisibility属性

    private Context mContext;
    /**
     * 自定义View
     */
    private View mStatusView;//状态栏View-用于单独设置颜色
    private LinearLayout mLeftLayout;//左边容器
    private LinearLayout mCenterLayout;//中间容器
    private LinearLayout mRightLayout;//右边容器
    private TextView mLeftTv;//左边TextView
    private TextView mTitleMain;//主标题
    private TextView mTitleSub;//副标题
    private TextView mRightTv;//右边TextView
    private View mDividerView;//下方下划线

    /**
     * 是否增加状态栏高度
     */
    private boolean mIsPlusStatusHeight = true;
    /**
     * xml属性
     */
    private boolean mImmersible = false;
    private int mOutPadding;
    private int mActionPadding;
    private int mCenterLayoutPadding;//中间部分是Layout左右padding
    private boolean mCenterGravityLeft = false;//中间部分是否左对齐--默认居中
    private int mCenterGravityLeftPadding;//中间部分左对齐是Layout左padding
    private boolean mStatusBarLightMode = false;//是否浅色状态栏(黑色文字及图标)
    private int mStatusBarModeType = StatusBarUtil.STATUS_BAR_TYPE_DEFAULT;//设置状态栏浅色或深色模式类型标记;>0则表示支持模式切换

    private int mStatusColor;
    private int mStatusResource;
    private int mDividerColor;
    private int mDividerResource;
    private int mDividerHeight;
    private boolean mDividerVisible;

    private int mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftTextBackgroundColor;
    private int mLeftDrawable;
    private Drawable mLeftTextDrawable;
    private int mLeftTextDrawableWidth;
    private int mLeftTextDrawableHeight;
    private int mLeftDrawablePadding;
    private int mLeftTextBackgroundResource;

    private int mTitleMainTextSize;
    private int mTitleMainTextColor;
    private int mTitleMainTextBackgroundColor;
    private int mTitleMainTextBackgroundResource;
    private boolean mTitleMainTextFakeBold;
    private boolean mTitleMainTextMarquee;//主标题是否跑马灯效果

    private int mTitleSubTextSize;
    private int mTitleSubTextColor;
    private int mTitleSubTextBackgroundColor;
    private int mTitleSubTextBackgroundResource;
    private boolean mTitleSubTextFakeBold;
    private boolean mTitleSubTextMarquee;//副标题是否跑马灯效果

    private int mRightTextSize;
    private int mRightTextColor;
    private int mRightTextBackgroundColor;
    private int mRightDrawable;
    private Drawable mRightTextDrawable;
    private int mRightTextDrawableWidth;
    private int mRightTextDrawableHeight;
    private int mRightDrawablePadding;
    private int mRightTextBackgroundResource;

    private int mActionTextSize;
    private int mActionTextColor;
    private int mActionTextBackgroundColor;
    private int mActionTextBackgroundResource;

    private CharSequence mTitleMainText;
    private CharSequence mTitleSubText;
    private CharSequence mLeftText;
    private CharSequence mRightText;

    public TitleBarView(Context context) {
        this(context, null, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initAttributes(context, attrs);
        initView(context);
        setViewAttributes(context);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mImmersible = ta.getBoolean(R.styleable.TitleBarView_title_immersible, true);
        mOutPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_outPadding, dip2px(DEFAULT_OUT_PADDING));
        mActionPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionPadding, dip2px(1));
        mCenterLayoutPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_centerLayoutPadding, dip2px(2));
        mCenterGravityLeft = ta.getBoolean(R.styleable.TitleBarView_title_centerGravityLeft, false);
        mCenterGravityLeftPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_centerGravityLeftPadding, dip2px(DEFAULT_CENTER_GRAVITY_LEFT_PADDING));
        mStatusBarLightMode = ta.getBoolean(R.styleable.TitleBarView_title_statusBarLightMode, false);

        mStatusColor = ta.getColor(R.styleable.TitleBarView_title_statusColor, -1);
        mStatusResource = ta.getResourceId(R.styleable.TitleBarView_title_statusResource, -1);
        mDividerColor = ta.getColor(R.styleable.TitleBarView_title_dividerColor, Color.TRANSPARENT);
        mDividerResource = ta.getResourceId(R.styleable.TitleBarView_title_dividerResource, -1);
        mDividerHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_dividerHeight, dip2px(0.5f));
        mDividerVisible = ta.getBoolean(R.styleable.TitleBarView_title_dividerVisible, true);

        mLeftText = ta.getString(R.styleable.TitleBarView_title_leftText);
        mLeftTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mLeftTextColor = ta.getColor(R.styleable.TitleBarView_title_leftTextColor, DEFAULT_TEXT_COLOR);
        mLeftTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_leftTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mLeftTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_leftTextBackgroundResource, -1);
        mLeftDrawable = ta.getResourceId(R.styleable.TitleBarView_title_leftTextDrawable, -1);
        mLeftTextDrawableWidth = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawableWidth, -1);
        mLeftTextDrawableHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawableHeight, -1);
        mLeftDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawablePadding, dip2px(1));

        mTitleMainText = ta.getString(R.styleable.TitleBarView_title_titleMainText);
        mTitleMainTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleMainTextSize, dip2px(DEFAULT_MAIN_TEXT_SIZE));
        mTitleMainTextColor = ta.getColor(R.styleable.TitleBarView_title_titleMainTextColor, DEFAULT_TEXT_COLOR);
        mTitleMainTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_titleMainTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mTitleMainTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_titleMainTextBackgroundResource, -1);
        mTitleMainTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextFakeBold, false);
        mTitleMainTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextMarquee, false);

        mTitleSubText = ta.getString(R.styleable.TitleBarView_title_titleSubText);
        mTitleSubTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleSubTextSize, dip2px(DEFAULT_SUB_TEXT_SIZE));
        mTitleSubTextColor = ta.getColor(R.styleable.TitleBarView_title_titleSubTextColor, DEFAULT_TEXT_COLOR);
        mTitleSubTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_titleSubTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mTitleSubTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_titleSubTextBackgroundResource, -1);
        mTitleSubTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextFakeBold, false);
        mTitleSubTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextMarquee, false);

        mRightText = ta.getString(R.styleable.TitleBarView_title_rightText);
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mRightTextColor = ta.getColor(R.styleable.TitleBarView_title_rightTextColor, DEFAULT_TEXT_COLOR);
        mRightTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_rightTextBackgroundResource, -1);
        mRightTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_rightTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mRightDrawable = ta.getResourceId(R.styleable.TitleBarView_title_rightTextDrawable, -1);
        mRightTextDrawableWidth = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawableWidth, -1);
        mRightTextDrawableHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawableHeight, -1);
        mRightDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawablePadding, dip2px(1));

        mActionTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionTextSize, dip2px(DEFAULT_TEXT_SIZE));
        mActionTextColor = ta.getColor(R.styleable.TitleBarView_title_actionTextColor, DEFAULT_TEXT_COLOR);
        mActionTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_actionTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mActionTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_actionTextBackgroundResource, -1);
        ta.recycle();//回收
    }

    /**
     * 初始化子View
     *
     * @param context
     */
    private void initView(Context context) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        LayoutParams dividerParams = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, mDividerHeight);

        mLeftLayout = new LinearLayout(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mStatusView = new View(context);
        mDividerView = new View(context);

        mLeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        mCenterLayout.setOrientation(LinearLayout.VERTICAL);
        mRightLayout.setGravity(Gravity.CENTER_VERTICAL);

        mLeftTv = new TextView(context);
        mLeftTv.setGravity(Gravity.CENTER);
        mLeftTv.setLines(1);

        mTitleMain = new TextView(context);
        mTitleSub = new TextView(context);

        mRightTv = new TextView(context);
        mRightTv.setGravity(Gravity.CENTER);
        mRightTv.setLines(1);

        mLeftLayout.addView(mLeftTv, params);
        mRightLayout.addView(mRightTv, params);
        addView(mLeftLayout, params);//添加左边容器
        addView(mCenterLayout, params);//添加中间容器
        addView(mRightLayout, params);//添加右边容器
        addView(mDividerView, dividerParams);//添加下划线View
        addView(mStatusView);//添加状态栏View
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
        setStatusColor(mStatusColor);
        setStatusResource(mStatusResource);
        setDividerColor(mDividerColor);
        setDividerResource(mDividerResource);
        setDividerHeight(mDividerHeight);
        setDividerVisible(mDividerVisible);
        setLeftText(mLeftText);
        setLeftTextSize(TypedValue.COMPLEX_UNIT_PX, mLeftTextSize);
        setLeftTextColor(mLeftTextColor);
        setLeftTextBackgroundColor(mLeftTextBackgroundColor);
        setLeftTextBackgroundResource(mLeftTextBackgroundResource);
        setLeftTextDrawable(mLeftDrawable);
        setLeftTextDrawableWidth(mLeftTextDrawableWidth);
        setLeftTextDrawableHeight(mLeftTextDrawableHeight);
        setTitleMainText(mTitleMainText);
        setTitleMainTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleMainTextSize);
        setTitleMainTextColor(mTitleMainTextColor);
        setTitleMainTextBackgroundColor(mTitleMainTextBackgroundColor);
        setTitleMainTextBackgroundResource(mTitleMainTextBackgroundResource);
        setTitleMainTextFakeBold(mTitleMainTextFakeBold);
        setTitleMainTextMarquee(mTitleMainTextMarquee);
        setTitleSubText(mTitleSubText);
        setTitleSubTextSize(TypedValue.COMPLEX_UNIT_PX, mTitleSubTextSize);
        setTitleSubTextColor(mTitleSubTextColor);
        setTitleSubTextBackgroundColor(mTitleSubTextBackgroundColor);
        setTitleSubTextBackgroundResource(mTitleSubTextBackgroundResource);
        setTitleSubTextFakeBold(mTitleSubTextFakeBold);
        setTitleSubTextMarquee(mTitleSubTextMarquee);
        setRightText(mRightText);
        setRightTextSize(TypedValue.COMPLEX_UNIT_PX, mRightTextSize);
        setRightTextColor(mRightTextColor);
        setRightTextBackgroundColor(mRightTextBackgroundColor);
        setRightTextBackgroundResource(mRightTextBackgroundResource);
        setRightTextDrawable(mRightDrawable);
        setRightTextDrawableWidth(mRightTextDrawableWidth);
        setRightTextDrawableHeight(mRightTextDrawableHeight);
    }

    /**
     * 根据位置获取 LinearLayout
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public LinearLayout getLinearLayout(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            return mLeftLayout;
        } else if (gravity == Gravity.CENTER) {
            return mCenterLayout;
        } else if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
            return mRightLayout;
        }
        return mCenterLayout;
    }

    /**
     * 根据位置获取TextView
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public TextView getTextView(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            return mLeftTv;
        } else if (gravity == (Gravity.CENTER | Gravity.TOP)) {
            return mTitleMain;
        } else if (gravity == (Gravity.CENTER | Gravity.BOTTOM)) {
            return mTitleSub;
        } else if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
            return mRightTv;
        }
        return mTitleMain;
    }

    /**
     * 根据位置获取View
     *
     * @param gravity 参考{@link Gravity}
     * @return
     */
    public View getView(int gravity) {
        if (gravity == Gravity.TOP) {
            return mStatusView;
        } else if (gravity == Gravity.BOTTOM) {
            return mDividerView;
        }
        return mStatusView;
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
        return setImmersible(activity, immersible, true);
    }

    public TitleBarView setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar) {
        return setImmersible(activity, immersible, isTransStatusBar, true);
    }

    /**
     * 设置沉浸式状态栏，4.4以上系统支持
     *
     * @param activity
     * @param immersible
     * @param isTransStatusBar 是否透明状态栏
     * @param isPlusStatusBar  是否增加状态栏高度--用于控制底部有输入框 (设置false/xml背景色必须保持和状态栏一致)
     */
    public TitleBarView setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar, boolean isPlusStatusBar) {
        this.mImmersible = immersible;
        this.mIsPlusStatusHeight = isPlusStatusBar;
        mStatusBarHeight = getNeedStatusBarHeight();
        if (activity == null) {
            return this;
        }
        //透明状态栏
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusView.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, mStatusBarHeight));
            // 透明状态栏
            window.addFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                systemUiVisibility = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                window.getDecorView().setSystemUiVisibility(systemUiVisibility);
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
        int left = mLeftLayout.getMeasuredWidth();
        int right = mRightLayout.getMeasuredWidth();
        int center = mCenterLayout.getMeasuredWidth();
        mLeftLayout.layout(0, isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2, left, mLeftLayout.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - right, isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2, mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
        boolean isMuchScreen = left + right + center >= mScreenWidth;
        if (left > right) {
            mCenterLayout.layout(left, mStatusBarHeight, isMuchScreen ? mScreenWidth - right : mScreenWidth - left, getMeasuredHeight() - mDividerHeight);
        } else {
            mCenterLayout.layout(isMuchScreen ? left : right, mStatusBarHeight, mScreenWidth - right, getMeasuredHeight() - mDividerHeight);
        }
        mDividerView.layout(0, getMeasuredHeight() - mDividerView.getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight());
        mStatusView.layout(0, 0, getMeasuredWidth(), mStatusBarHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mStatusBarHeight = getNeedStatusBarHeight();
        measureChild(mLeftLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mCenterLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mStatusView, widthMeasureSpec, heightMeasureSpec);
        //重新测量宽高--增加状态栏及下划线的高度
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec) + (isNormalParent() ? mStatusBarHeight : mStatusBarHeight / 2) + mDividerHeight);
        mScreenWidth = getMeasuredWidth();

        int left = mLeftLayout.getMeasuredWidth();
        int right = mRightLayout.getMeasuredWidth();
        int center = mCenterLayout.getMeasuredWidth();
        //判断左、中、右实际占用宽度是否等于或者超过屏幕宽度
        boolean isMuchScreen = left + right + center >= mScreenWidth;
        if (!mCenterGravityLeft) {//不设置中间布局左对齐才进行中间布局重新测量
            if (isMuchScreen) {
                center = mScreenWidth - left - right;
            } else {
                if (left > right) {
                    center = mScreenWidth - 2 * left;
                } else {
                    center = mScreenWidth - 2 * right;
                }
            }
            mCenterLayout.measure(MeasureSpec.makeMeasureSpec(center, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(background);
        } else {
            setBackgroundDrawable(background);
        }
        return this;
    }

    public TitleBarView setBgColor(int color) {
        setBackgroundColor(color);
        return this;
    }

    public TitleBarView setBgResource(int res) {
        super.setBackgroundResource(res);
        return this;
    }

    public TitleBarView setOutPadding(int paddingValue) {
        mOutPadding = paddingValue;
        mLeftLayout.setPadding(mOutPadding, 0, 0, 0);
        mRightLayout.setPadding(0, 0, mOutPadding, 0);
        return this;
    }

    public TitleBarView setCenterLayoutPadding(int centerLayoutPadding) {
        this.mCenterLayoutPadding = centerLayoutPadding;
        mCenterLayout.setPadding(mCenterLayoutPadding, mCenterLayout.getTop(), mCenterLayoutPadding, mCenterLayout.getPaddingBottom());
        return this;
    }

    public TitleBarView setCenterGravityLeft(boolean enable) {
        this.mCenterGravityLeft = enable;
        mTitleMain.setGravity(mCenterGravityLeft ? Gravity.LEFT : Gravity.CENTER);
        mCenterLayout.setGravity(mCenterGravityLeft ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.CENTER);
        mTitleSub.setGravity(mCenterGravityLeft ? Gravity.LEFT : Gravity.CENTER);
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
            mCenterLayout.setPadding(mCenterGravityLeftPadding, mCenterLayout.getTop(), mCenterLayout.getPaddingRight(), mCenterLayout.getPaddingBottom());
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
     * 设置状态栏颜色
     *
     * @param color
     */
    public TitleBarView setStatusColor(int color) {
        try {
            mStatusColor = color;
            mStatusView.setBackgroundColor(color);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 透明度 0-255
     *
     * @param statusBarAlpha
     */
    public TitleBarView setStatusAlpha(int statusBarAlpha) {
        if (statusBarAlpha < 0) {
            statusBarAlpha = 0;
        } else if (statusBarAlpha > 255) {
            statusBarAlpha = 255;
        }
        return setStatusColor(Color.argb(statusBarAlpha, 0, 0, 0));
    }

    public TitleBarView setStatusResource(int resource) {
        try {
            mStatusResource = resource;
            mStatusView.setBackgroundResource(resource);
        } catch (Exception e) {

        }
        return this;
    }

    public TitleBarView setDividerColor(int color) {
        mDividerColor = color;
        mDividerView.setBackgroundColor(color);
        return this;
    }

    public TitleBarView setDividerResource(int resource) {
        mDividerResource = resource;
        if (mDividerResource != -1)
            mDividerView.setBackgroundResource(resource);
        return this;
    }

    public TitleBarView setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
        mDividerView.getLayoutParams().height = dividerHeight;
        return this;
    }

    public TitleBarView setDividerVisible(boolean visible) {
        mDividerVisible = visible;
        mDividerView.setVisibility(visible ? VISIBLE : GONE);
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

    public TitleBarView setLeftText(CharSequence title) {
        mLeftText = title;
        mLeftTv.setText(title);
        return this;
    }

    public TitleBarView setLeftText(int id) {
        return setLeftText(getResources().getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit 文字单位{@link TypedValue}
     * @param size
     * @return
     */
    public TitleBarView setLeftTextSize(int unit, float size) {
        mLeftTv.setTextSize(unit, size);
        return this;
    }

    public TitleBarView setLeftTextSize(float size) {
        return setLeftTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBarView setLeftTextColor(int color) {
        mLeftTv.setTextColor(color);
        return this;
    }

    /**
     * 设置文字状态颜色-如按下颜色变化
     *
     * @param color
     * @return
     */
    public TitleBarView setLeftTextColor(ColorStateList color) {
        try {
            mLeftTv.setTextColor(color);
        } catch (Exception e) {
        }
        return this;
    }

    public TitleBarView setLeftTextBackgroundColor(int color) {
        mLeftTv.setBackgroundColor(color);
        return this;
    }

    /**
     * @param resId
     */
    public TitleBarView setLeftTextBackgroundResource(int resId) {
        try {
            mLeftTextBackgroundResource = resId;
            mLeftTv.setBackgroundResource(mLeftTextBackgroundResource);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 设置左边图片资源
     *
     * @param drawable
     * @return
     */
    public TitleBarView setLeftTextDrawable(Drawable drawable) {
        try {
            if (drawable != null) {
                drawable.setBounds(0, 0,
                        mLeftTextDrawableWidth != -1 ? mLeftTextDrawableWidth : drawable.getIntrinsicWidth(),
                        mLeftTextDrawableHeight != -1 ? mLeftTextDrawableHeight : drawable.getIntrinsicHeight());
            }
        } catch (Exception e) {
        }
        mLeftTextDrawable = drawable;
        mLeftTv.setCompoundDrawables(mLeftTextDrawable, null, null, null);
        return this;
    }

    public TitleBarView setLeftTextDrawable(int id) {
        mLeftDrawable = id;
        Drawable drawable = null;
        try {
            drawable = mContext.getResources().getDrawable(id);
        } catch (Exception e) {

        }
        return setLeftTextDrawable(drawable);
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
        this.mLeftDrawablePadding = drawablePadding;
        mLeftTv.setCompoundDrawablePadding(mLeftDrawablePadding);
        return this;
    }

    public TitleBarView setLeftTextPadding(int left, int top, int right, int bottom) {
        mLeftTv.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setOnLeftTextClickListener(OnClickListener l) {
        mLeftTv.setOnClickListener(l);
        return this;
    }

    public TitleBarView setLeftVisible(boolean visible) {
        mLeftTv.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBarView setTitleMainText(int id) {
        return setTitleMainText(getResources().getText(id));
    }

    public TitleBarView setTitleMainText(CharSequence charSequence) {
        mTitleMain.setText(charSequence);
        if (!TextUtils.isEmpty(charSequence) && !hasChildView(mCenterLayout, mTitleMain)) {//非空且还未添加主标题
            mCenterLayout.addView(mTitleMain, 0);
        }
        return this;
    }

    public TitleBarView setTitleMainTextSize(int unit, float titleMainTextSpValue) {
        mTitleMain.setTextSize(unit, titleMainTextSpValue);
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
        mTitleMain.setTextColor(color);
        return this;
    }

    public TitleBarView setTitleMainTextBackgroundColor(int color) {
        try {
            mTitleMainTextBackgroundColor = color;
            mTitleMain.setBackgroundColor(color);
        } catch (Exception e) {

        }
        return this;
    }

    public TitleBarView setTitleMainTextBackgroundResource(int resId) {
        try {
            mTitleMainTextBackgroundResource = resId;
            mTitleMain.setBackgroundResource(resId);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public TitleBarView setTitleMainTextFakeBold(boolean isFakeBold) {
        this.mTitleMainTextFakeBold = isFakeBold;
        mTitleMain.getPaint().setFakeBoldText(mTitleMainTextFakeBold);
        return this;
    }

    public TitleBarView setTitleMainTextMarquee(boolean enable) {
        this.mTitleMainTextMarquee = enable;
        if (enable) {
            setTitleSubTextMarquee(false);
            mTitleMain.setSingleLine();
            mTitleMain.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTitleMain.setFocusable(true);
            mTitleMain.setFocusableInTouchMode(true);
            mTitleMain.requestFocus();
            mTitleMain.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleMainTextMarquee) {
                        mTitleMain.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mTitleMain.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mTitleMain.setMaxLines(1);
            mTitleMain.setEllipsize(TextUtils.TruncateAt.END);
            mTitleMain.setOnFocusChangeListener(null);
            //关闭硬件加速
            mTitleMain.setLayerType(View.LAYER_TYPE_NONE, null);
        }
        return this;
    }

    public TitleBarView setTitleMainTextPadding(int left, int top, int right, int bottom) {
        mTitleMain.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setTitleSubText(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().isEmpty()) {
            mTitleSub.setVisibility(GONE);
        } else {
            mTitleSub.setVisibility(VISIBLE);
        }
        mTitleSub.setText(charSequence);
        if (!TextUtils.isEmpty(charSequence) && !hasChildView(mCenterLayout, mTitleSub)) {//非空且还未添加副标题
            if (hasChildView(mCenterLayout, mTitleMain)) {
                mTitleMain.setSingleLine();
                mTitleSub.setSingleLine();
            }
            mCenterLayout.addView(mTitleSub);
        }
        return this;
    }

    public TitleBarView setTitleSubText(int id) {
        return setTitleSubText(getResources().getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit  单位 参考{@link TypedValue}
     * @param value
     * @return
     */
    public TitleBarView setTitleSubTextSize(int unit, float value) {
        mTitleSub.setTextSize(unit, value);
        return this;
    }

    public TitleBarView setTitleSubTextSize(float spValue) {
        return setTitleSubTextSize(TypedValue.COMPLEX_UNIT_SP, spValue);
    }

    public TitleBarView setTitleSubTextColor(int color) {
        mTitleSub.setTextColor(color);
        return this;
    }

    public TitleBarView setTitleSubTextBackgroundColor(int color) {
        try {
            mTitleSubTextBackgroundColor = color;
            mTitleSub.setBackgroundColor(color);
        } catch (Exception e) {

        }
        return this;
    }

    public TitleBarView setTitleSubTextBackgroundResource(int resId) {
        try {
            mTitleSubTextBackgroundResource = resId;
            mTitleSub.setBackgroundResource(resId);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public TitleBarView setTitleSubTextFakeBold(boolean isFakeBold) {
        this.mTitleSubTextFakeBold = isFakeBold;
        mTitleSub.getPaint().setFakeBoldText(mTitleSubTextFakeBold);
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
            mTitleSub.setSingleLine();
            mTitleSub.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTitleSub.setFocusable(true);
            mTitleSub.setFocusableInTouchMode(true);
            mTitleSub.requestFocus();
            mTitleSub.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleSubTextMarquee) {
                        mTitleMain.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mTitleSub.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mTitleSub.setMaxLines(1);
            mTitleSub.setEllipsize(TextUtils.TruncateAt.END);
            mTitleSub.setOnFocusChangeListener(null);
            //关闭硬件加速
            mTitleSub.setLayerType(View.LAYER_TYPE_NONE, null);
        }
        return this;
    }

    public TitleBarView setOnCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
        return this;
    }

    public TitleBarView setRightText(CharSequence title) {
        mRightTv.setText(title);
        return this;
    }

    public TitleBarView setRightText(int id) {
        return setRightText(getResources().getText(id));
    }

    /**
     * 设置文字大小
     *
     * @param unit 单位 参考{@link TypedValue}
     * @param size
     * @return
     */
    public TitleBarView setRightTextSize(int unit, float size) {
        mRightTv.setTextSize(unit, size);
        return this;
    }

    public TitleBarView setRightTextSize(float size) {
        return setRightTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public TitleBarView setRightTextColor(int color) {
        mRightTv.setTextColor(color);
        return this;
    }

    public TitleBarView setRightTextColor(ColorStateList color) {
        try {
            mRightTv.setTextColor(color);
        } catch (Exception e) {
        }
        return this;
    }

    public TitleBarView setRightTextBackgroundColor(int color) {
        try {
            mRightTextBackgroundColor = color;
            mRightTv.setBackgroundColor(color);
        } catch (Exception e) {

        }
        return this;
    }

    public TitleBarView setRightTextBackgroundResource(int id) {
        try {
            mRightTextBackgroundResource = id;
            mRightTv.setBackgroundResource(id);
        } catch (Exception e) {

        }
        return this;
    }

    /**
     * 右边文本添加图片
     *
     * @param drawable 资源
     */
    public TitleBarView setRightTextDrawable(Drawable drawable) {
        try {
            if (drawable != null) {
                drawable.setBounds(0, 0,
                        mRightTextDrawableWidth != -1 ? mRightTextDrawableWidth : drawable.getIntrinsicWidth(),
                        mRightTextDrawableHeight != -1 ? mRightTextDrawableHeight : drawable.getIntrinsicHeight());
            }
        } catch (Exception e) {
        }
        mRightTextDrawable = drawable;
        mRightTv.setCompoundDrawables(null, null, mRightTextDrawable, null);
        return this;
    }

    public TitleBarView setRightTextDrawable(int id) {
        mRightDrawable = id;
        Drawable drawable = null;
        try {
            drawable = mContext.getResources().getDrawable(id);
        } catch (Exception e) {

        }
        return setRightTextDrawable(drawable);
    }

    public TitleBarView setRightTextDrawablePadding(int drawablePadding) {
        this.mRightDrawablePadding = drawablePadding;
        mRightTv.setCompoundDrawablePadding(mRightDrawablePadding);
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
        mRightTv.setPadding(left, top, right, bottom);
        return this;
    }

    public TitleBarView setOnRightTextClickListener(OnClickListener l) {
        mRightTv.setOnClickListener(l);
        return this;
    }

    public TitleBarView setRightVisible(boolean visible) {
        mRightTv.setVisibility(visible ? View.VISIBLE : View.GONE);
        return this;
    }

    public TitleBarView setActionTextSize(int mActionTextSize) {
        this.mActionTextSize = mActionTextSize;
        return this;
    }

    public TitleBarView setActionTextColor(int mActionTextColor) {
        this.mActionTextColor = mActionTextColor;
        return this;
    }

    public TitleBarView setActionTextBackgroundColor(int mActionTextBackgroundColor) {
        this.mActionTextBackgroundColor = mActionTextBackgroundColor;
        return this;
    }

    public TitleBarView setActionTextBackgroundResource(int mActionTextBackgroundResource) {
        this.mActionTextBackgroundResource = mActionTextBackgroundResource;
        return this;
    }

    /**
     * 设置底部有输入框控制方案--IM常见
     */
    public TitleBarView setBottomEditTextControl(Activity mActivity) {
        KeyboardUtil.with(mActivity).setEnable();
        return this;
    }

    public TitleBarView setBottomEditTextControl() {
        if (mContext instanceof Activity) {
            setBottomEditTextControl((Activity) mContext);
        }
        return this;
    }

    public TitleBarView addLeftAction(Action action, int position) {
        View view = inflateAction(action);
        mLeftLayout.addView(view, position);
        return this;
    }

    public TitleBarView addLeftAction(Action action) {
        return addLeftAction(action, -1);
    }

    /**
     * 自定义中间部分布局
     */
    public TitleBarView addCenterAction(Action action, int position) {
        View view = inflateAction(action);
        mCenterLayout.addView(view, position);
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
        mRightLayout.addView(view, position);
        return this;
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
            TextView text = new TextView(getContext());
            text.setGravity(Gravity.CENTER);
            text.setText((String) obj);
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, mActionTextSize);
            text.setTextColor(mActionTextColor);
            text.setBackgroundColor(mActionTextBackgroundColor);
            if (mActionTextBackgroundResource != -1) {
                text.setBackgroundResource(mActionTextBackgroundResource);
            }
            view = text;
        } else if (obj instanceof Drawable) {
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageDrawable((Drawable) obj);
            view = img;
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

        public ImageAction(int drawable, OnClickListener onClickListener) {
            try {
                this.mDrawable = getResources().getDrawable(drawable);
            } catch (Exception e) {
            }
            this.onClickListener = onClickListener;
        }

        public ImageAction(int drawable) {
            try {
                this.mDrawable = getResources().getDrawable(drawable);
            } catch (Exception e) {

            }
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
            this.mText = getResources().getText(mText);
        }

        public TextAction(int mText, OnClickListener onClickListener) {
            this.mText = getResources().getText(mText);
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
        return mIsPlusStatusHeight && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = Resources.getSystem().getDimensionPixelSize(resourceId);
        }
        return result;
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
