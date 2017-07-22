package com.aries.ui.view.title;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aries.ui.widget.R;


/**
 * Created: AriesHoo on 2017-02-09 09:42
 * Function:定制标题栏
 * Desc:
 */
public class TitleBarView extends ViewGroup {

    private static final int DEFAULT_TEXT_COLOR = 0XFFFFFFFF;//默认文本颜色
    private static final int DEFAULT_TEXT_BG_COLOR = 0X00000000;//默认子View背景色
    private static final int DEFAULT_TEXT_SIZE = 16;//文本默认size
    private static final int DEFAULT_SUB_TEXT_SIZE = 12;//副标题默认size

    private int mStatusBarHeight;//状态栏高度
    private int mScreenWidth;//屏幕高度
    private int systemUiVisibility;//Activity systemUiVisibility属性

    private Context mContext;
    /**
     * 自定义View
     */
    private View mStatusView;//状态栏View
    private LinearLayout mLeftLayout;//左边容器
    private LinearLayout mCenterLayout;//中间容器
    private LinearLayout mRightLayout;//右边容器
    private TextView mLeftTv;//左边Text
    private TextView mTitleTv;//主标题
    private TextView mSubTitleText;//副标题
    private View mDividerView;//下方分割线
    private TextView mRightTv;//右边Text

    /**
     * xml属性
     */
    private boolean mImmersible = false;
    private int mOutPadding;
    private int mActionPadding;

    private int mStatusColor;
    private int mStatusResource;
    private int mDividerColor;
    private int mDividerResource;
    private int mDividerHeight;
    private boolean mDividerVisible = false;

    private int mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftTextBackgroundColor;
    private int mLeftDrawable;
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
    private int mRightDrawablePadding;
    private int mRightTextBackgroundResource;

    private int mActionTextSize;
    private int mActionTextColor;
    private int mActionTextBackgroundColor;
    private int mActionTextBackgroundResource;

    private String mTitleMainText;
    private String mTitleSubText;
    private String mLeftText;
    private String mRightText;


    private boolean isAddTitleMain = false;//是否已设置主标题
    private boolean isAddTitleSub = false;//是否设置副标题

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
        setViewAttributes(context, mImmersible);
    }

    private void initAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBarView);
        mImmersible = ta.getBoolean(R.styleable.TitleBarView_title_immersible, true);
        mOutPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_outPadding, dip2px(context, 6));
        mActionPadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionPadding, dip2px(context, 1));

        mStatusColor = ta.getColor(R.styleable.TitleBarView_title_statusColor, Color.TRANSPARENT);
        mStatusResource = ta.getResourceId(R.styleable.TitleBarView_title_statusResource, -1);
        mDividerColor = ta.getColor(R.styleable.TitleBarView_title_dividerColor, Color.TRANSPARENT);
        mDividerResource = ta.getResourceId(R.styleable.TitleBarView_title_dividerResource, -1);
        mDividerHeight = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_dividerHeight, dip2px(context, 0.5f));
        mDividerVisible = ta.getBoolean(R.styleable.TitleBarView_title_dividerVisible, true);

        mLeftText = ta.getString(R.styleable.TitleBarView_title_leftText);
        mLeftTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mLeftTextColor = ta.getColor(R.styleable.TitleBarView_title_leftTextColor, DEFAULT_TEXT_COLOR);
        mLeftTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_leftTextBackgroundResource, -1);
        mLeftTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_leftTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mLeftDrawable = ta.getResourceId(R.styleable.TitleBarView_title_leftTextDrawable, -1);
        mLeftDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_leftTextDrawablePadding, dip2px(context, 1));

        mTitleMainText = ta.getString(R.styleable.TitleBarView_title_titleMainText);
        mTitleMainTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleMainTextSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mTitleMainTextColor = ta.getColor(R.styleable.TitleBarView_title_titleMainTextColor, DEFAULT_TEXT_COLOR);
        mTitleMainTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_titleMainTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mTitleMainTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_titleMainTextBackgroundResource, -1);
        mTitleMainTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextFakeBold, false);
        mTitleMainTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleMainTextMarquee, false);

        mTitleSubText = ta.getString(R.styleable.TitleBarView_title_titleSubText);
        mTitleSubTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_titleSubTextSize, sp2px(context, DEFAULT_SUB_TEXT_SIZE));
        mTitleSubTextColor = ta.getColor(R.styleable.TitleBarView_title_titleSubTextColor, DEFAULT_TEXT_COLOR);
        mTitleSubTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_titleSubTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mTitleSubTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_titleSubTextBackgroundResource, -1);
        mTitleSubTextFakeBold = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextFakeBold, false);
        mTitleSubTextMarquee = ta.getBoolean(R.styleable.TitleBarView_title_titleSubTextMarquee, false);

        mRightText = ta.getString(R.styleable.TitleBarView_title_rightText);
        mRightTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mRightTextColor = ta.getColor(R.styleable.TitleBarView_title_rightTextColor, DEFAULT_TEXT_COLOR);
        mRightTextBackgroundResource = ta.getResourceId(R.styleable.TitleBarView_title_rightTextBackgroundResource, -1);
        mRightTextBackgroundColor = ta.getColor(R.styleable.TitleBarView_title_rightTextBackgroundColor, DEFAULT_TEXT_BG_COLOR);
        mRightDrawable = ta.getResourceId(R.styleable.TitleBarView_title_rightTextDrawable, -1);
        mRightDrawablePadding = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_rightTextDrawablePadding, dip2px(context, 1));

        mActionTextSize = ta.getDimensionPixelSize(R.styleable.TitleBarView_title_actionTextSize, sp2px(context, DEFAULT_TEXT_SIZE));
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
        LayoutParams dividerParams = new ViewGroup.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mDividerHeight);

        mLeftLayout = new LinearLayout(context);
        mCenterLayout = new LinearLayout(context);
        mRightLayout = new LinearLayout(context);
        mStatusView = new View(context);
        mDividerView = new View(context);

        mLeftLayout.setGravity(Gravity.CENTER_VERTICAL);
        mCenterLayout.setGravity(Gravity.CENTER);
        mCenterLayout.setOrientation(LinearLayout.VERTICAL);
        mRightLayout.setGravity(Gravity.CENTER_VERTICAL);

        mLeftTv = new TextView(context);
        mLeftTv.setGravity(Gravity.CENTER);
        mLeftTv.setLines(1);

        mTitleTv = new TextView(context);
        mTitleTv.setGravity(Gravity.CENTER);

        mSubTitleText = new TextView(context);
        mSubTitleText.setGravity(Gravity.CENTER);

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

        setStatusColor(mStatusColor);
        if (mStatusResource != -1) {
            setStatusResource(mStatusResource);
        }
        setDividerColor(mDividerColor);
        setDividerResource(mDividerResource);
        setDividerHeight(mDividerHeight);
        setDividerVisible(mDividerVisible);
    }

    /**
     * 设置xml默认属性
     *
     * @param context
     * @param immersible
     */
    private void setViewAttributes(final Context context, boolean immersible) {
        mScreenWidth = getScreenWidth();
        mStatusBarHeight = getStatusBarHeight();
        if (context instanceof Activity) {
            setImmersible((Activity) context, true);
            if (!immersible) {
                setImmersible((Activity) context, false);
            }
            this.mImmersible = immersible;
        }

        setOutPadding(mOutPadding);
        setActionPadding(mActionPadding);
        setStatusColor(mStatusColor);

        setLeftText(mLeftText);
        setLeftTextSize(px2sp(context, mLeftTextSize));
        setLeftTextColor(mLeftTextColor);
        setLeftTextBackgroundColor(mLeftTextBackgroundColor);
        if (mLeftTextBackgroundResource != -1) {
            setLeftTextBackgroundResource(mLeftTextBackgroundResource);
        }
        if (mLeftDrawable != -1) {
            setLeftTextDrawable(mLeftDrawable);
        }

        setTitleMainText(mTitleMainText);
        setTitleMainTextSize(px2sp(context, mTitleMainTextSize));
        setTitleMainTextColor(mTitleMainTextColor);
        mTitleTv.setBackgroundColor(mTitleMainTextBackgroundColor);
        if (mTitleMainTextBackgroundResource != -1) {
            mTitleTv.setBackgroundResource(mTitleMainTextBackgroundResource);
        }
        setTitleMainTextFakeBold(mTitleMainTextFakeBold);
        setTitleMainTextMarquee(mTitleMainTextMarquee);

        setTitleSubText(mTitleSubText);
        setTitleSubTextSize(px2sp(context, mTitleSubTextSize));
        setTitleSubTextColor(mTitleSubTextColor);
        mSubTitleText.setBackgroundColor(mTitleSubTextBackgroundColor);
        if (mTitleSubTextBackgroundResource != -1) {
            mSubTitleText.setBackgroundResource(mTitleSubTextBackgroundResource);
        }
        setTitleSubTextFakeBold(mTitleSubTextFakeBold);
        setTitleSubTextMarquee(mTitleSubTextMarquee);

        setRightText(mRightText);
        setRightTextSize(px2sp(context, mRightTextSize));
        setRightTextColor(mRightTextColor);
        setRightTextBackgroundColor(mRightTextBackgroundColor);
        if (mRightTextBackgroundResource != -1) {
            mRightTv.setBackgroundResource(mRightTextBackgroundResource);
        }
        if (mRightDrawable != -1) {
            setRightTextDrawable(mRightDrawable);
        }
    }

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

    public TextView getTextView(int gravity) {
        if (gravity == Gravity.LEFT || gravity == Gravity.START) {
            return mLeftTv;
        } else if (gravity == (Gravity.CENTER | Gravity.TOP)) {
            return mTitleTv;
        } else if (gravity == (Gravity.CENTER | Gravity.BOTTOM)) {
            return mSubTitleText;
        } else if (gravity == Gravity.END || gravity == Gravity.RIGHT) {
            return mRightTv;
        }
        return mTitleTv;
    }

    public View getView(int gravity) {
        if (gravity == Gravity.TOP) {
            return mStatusView;
        } else if (gravity == Gravity.BOTTOM) {
            return mRightLayout;
        }
        return mStatusView;
    }

    public void setImmersible(Activity activity, boolean immersible) {
        setImmersible(activity, immersible, true);
    }

    public void setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar) {
        setImmersible(activity, immersible, isTransStatusBar, true);
    }

    /**
     * 设置沉浸式状态栏，4.4以上系统支持
     *
     * @param activity
     * @param immersible
     * @param isTransStatusBar 是否透明状态栏
     * @param isPlusStatusBar  是否增加状态栏高度--用于控制底部有输入框 (设置false/xml背景色必须保持和状态栏一致)
     */
    public void setImmersible(Activity activity, boolean immersible, boolean isTransStatusBar, boolean isPlusStatusBar) {
        this.mImmersible = immersible;
        if (isPlusStatusBar && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mStatusBarHeight = getStatusBarHeight();
        } else {
            mStatusBarHeight = 0;
        }
        if (activity == null) {
            return;
        }
        //透明状态栏
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
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
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int left = mLeftLayout.getMeasuredWidth();
        int right = mRightLayout.getMeasuredWidth();
        int center = mCenterLayout.getMeasuredWidth();
        mLeftLayout.layout(0, mStatusBarHeight, left, mLeftLayout.getMeasuredHeight() + mStatusBarHeight);
        mRightLayout.layout(mScreenWidth - right, mStatusBarHeight, mScreenWidth, mRightLayout.getMeasuredHeight() + mStatusBarHeight);
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
        measureChild(mLeftLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mRightLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mCenterLayout, widthMeasureSpec, heightMeasureSpec);
        measureChild(mDividerView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mStatusView, widthMeasureSpec, heightMeasureSpec);
        int left = mLeftLayout.getMeasuredWidth();
        int right = mRightLayout.getMeasuredWidth();
        int center = mCenterLayout.getMeasuredWidth();
        boolean isMuchScreen = left + right + center >= mScreenWidth;
        if (left > right) {
            mCenterLayout.measure(MeasureSpec.makeMeasureSpec(isMuchScreen ? mScreenWidth - left - right : mScreenWidth - 2 * left, MeasureSpec.EXACTLY), heightMeasureSpec);
        } else {
            mCenterLayout.measure(MeasureSpec.makeMeasureSpec(isMuchScreen ? mScreenWidth - left - right : mScreenWidth - 2 * right, MeasureSpec.EXACTLY), heightMeasureSpec);
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec) + mStatusBarHeight + mDividerHeight);
    }

    public void setOutPadding(int paddingValue) {
        mOutPadding = paddingValue;
        mLeftLayout.setPadding(mOutPadding, 0, 0, 0);
        mRightLayout.setPadding(0, 0, mOutPadding, 0);
    }

    public void setActionPadding(int actionPadding) {
        mActionPadding = actionPadding;
    }

    public void setStatusColor(int color) {
        mStatusView.setBackgroundColor(color);
    }

    /**
     * 透明度 0-255
     *
     * @param statusBarAlpha
     */
    public void setStatusAlpha(int statusBarAlpha) {
        if (statusBarAlpha < 0) {
            statusBarAlpha = 0;
        } else if (statusBarAlpha > 255) {
            statusBarAlpha = 255;
        }
        setStatusColor(Color.argb(statusBarAlpha, 0, 0, 0));
    }

    public void setStatusResource(int resource) {
        mStatusView.setBackgroundResource(resource);
    }

    public void setDividerColor(int color) {
        mDividerView.setBackgroundColor(color);
    }

    public void setDividerResource(int resource) {
        if (resource != -1)
            mDividerView.setBackgroundResource(resource);
    }

    public void setDividerHeight(int dividerHeight) {
        mDividerHeight = dividerHeight;
        mDividerView.getLayoutParams().height = dividerHeight;
    }

    public void setDividerVisible(boolean visible) {
        mDividerView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setLeftText(CharSequence title) {
        mLeftTv.setText(title);
    }

    public void setLeftText(int id) {
        setLeftText(mContext.getString(id));
    }

    public void setLeftTextSize(int unit, float size) {
        mLeftTv.setTextSize(unit, size);
    }

    public void setLeftTextSize(float size) {
        setLeftTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setLeftTextColor(int color) {
        mLeftTv.setTextColor(color);
    }

    public void setLeftTextColor(ColorStateList color) {
        try {
            mLeftTv.setTextColor(color);
        } catch (Exception e) {
        }
    }

    public void setLeftTextBackgroundColor(int color) {
        mLeftTv.setBackgroundColor(color);
    }

    public void setLeftTextBackgroundResource(int resId) {
        mLeftTv.setBackgroundResource(resId);
    }

    /**
     * 左边文本添加图片
     *
     * @param id 资源id
     */
    public void setLeftTextDrawable(int id, int drawablePadding) {
        mLeftTv.setCompoundDrawablesWithIntrinsicBounds(id, 0, 0, 0);
        setLeftTextDrawablePadding(drawablePadding);
    }

    public void setLeftTextDrawable(int id) {
        setLeftTextDrawable(id, mLeftDrawablePadding);
    }

    public void setLeftTextDrawablePadding(int drawablePadding) {
        this.mLeftDrawablePadding = drawablePadding;
        mLeftTv.setCompoundDrawablePadding(dip2px(getContext(), mLeftDrawablePadding));
    }

    public void setLeftTextPadding(int left, int top, int right, int bottom) {
        mLeftTv.setPadding(left, top, right, bottom);
    }

    public void setOnLeftTextClickListener(OnClickListener l) {
        mLeftTv.setOnClickListener(l);
    }

    public void setLeftVisible(boolean visible) {
        mLeftTv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setTitleMainText(int id) {
        setTitleMainText(mContext.getString(id));
    }

    public void setTitleMainText(CharSequence charSequence) {
        mTitleTv.setText(charSequence);
        if (!TextUtils.isEmpty(charSequence) && !isAddTitleMain) {//非空且还未添加主标题
            mCenterLayout.addView(mTitleTv, 0);
            isAddTitleMain = true;
        }
    }

    public void setTitleMainTextSize(float titleMainTextSpValue) {
        setTitleMainTextSize(TypedValue.COMPLEX_UNIT_SP, titleMainTextSpValue);
    }

    public void setTitleMainTextSize(int unit, float titleMainTextSpValue) {
        mTitleTv.setTextSize(unit, titleMainTextSpValue);
    }

    public void setTitleMainTextColor(int color) {
        mTitleTv.setTextColor(color);
    }

    public void setTitleMainTextBackgroundColor(int color) {
        mTitleTv.setBackgroundColor(color);
    }

    public void setTitleMainTextBackgroundResource(int resId) {
        mTitleTv.setBackgroundResource(resId);
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public void setTitleMainTextFakeBold(boolean isFakeBold) {
        this.mTitleMainTextFakeBold = isFakeBold;
        mTitleTv.getPaint().setFakeBoldText(mTitleMainTextFakeBold);
    }

    public void setTitleMainTextMarquee(boolean enable) {
        this.mTitleMainTextMarquee = enable;
        if (enable) {
            setTitleSubTextMarquee(false);
            mTitleTv.setSingleLine();
            mTitleTv.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mTitleTv.setFocusable(true);
            mTitleTv.setFocusableInTouchMode(true);
            mTitleTv.requestFocus();
            mTitleTv.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleMainTextMarquee) {
                        mTitleTv.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mTitleTv.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mTitleTv.setMaxLines(2);
            mTitleTv.setEllipsize(TextUtils.TruncateAt.END);
            mTitleTv.setOnFocusChangeListener(null);
            //关闭硬件加速
            mTitleTv.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    public void setTitleMainTextPadding(int left, int top, int right, int bottom) {
        mTitleTv.setPadding(left, top, right, bottom);
    }

    public void setTitleSubText(int id) {
        setTitleSubText(mContext.getString(id));
    }

    public void setTitleSubText(CharSequence charSequence) {
        if (charSequence == null || charSequence.toString().isEmpty()) {
            mSubTitleText.setVisibility(GONE);
        } else {
            mSubTitleText.setVisibility(VISIBLE);
        }
        mSubTitleText.setText(charSequence);
        if (!TextUtils.isEmpty(charSequence) && !isAddTitleSub) {//非空且还未添加副标题
            if (isAddTitleMain) {
                mTitleTv.setSingleLine();
                mSubTitleText.setSingleLine();
            }
            mCenterLayout.addView(mSubTitleText);
            isAddTitleSub = true;
        }
    }

    public void setTitleSubTextSize(float spValue) {
        setTitleSubTextSize(TypedValue.COMPLEX_UNIT_SP, spValue);
    }

    public void setTitleSubTextSize(int unit, float spValue) {
        mSubTitleText.setTextSize(unit, spValue);
    }

    public void setTitleSubTextColor(int color) {
        mSubTitleText.setTextColor(color);
    }

    public void setTitleSubTextBackgroundColor(int color) {
        mSubTitleText.setBackgroundColor(color);
    }

    public void setTitleSubTextBackgroundResource(int resId) {
        mSubTitleText.setBackgroundResource(resId);
    }

    /**
     * 设置粗体标题
     *
     * @param isFakeBold
     */
    public void setTitleSubTextFakeBold(boolean isFakeBold) {
        this.mTitleSubTextFakeBold = isFakeBold;
        mSubTitleText.getPaint().setFakeBoldText(mTitleSubTextFakeBold);
    }

    /**
     * 设置TextView 跑马灯
     *
     * @param enable
     */
    public void setTitleSubTextMarquee(boolean enable) {
        this.mTitleSubTextMarquee = enable;
        if (enable) {
            setTitleMainTextMarquee(false);
            mSubTitleText.setSingleLine();
            mSubTitleText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            mSubTitleText.setFocusable(true);
            mSubTitleText.setFocusableInTouchMode(true);
            mSubTitleText.requestFocus();
            mSubTitleText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus && mTitleSubTextMarquee) {
                        mTitleTv.requestFocus();
                    }
                }
            });
            //开启硬件加速
            mSubTitleText.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mSubTitleText.setMaxLines(2);
            mSubTitleText.setEllipsize(TextUtils.TruncateAt.END);
            mSubTitleText.setOnFocusChangeListener(null);
            //关闭硬件加速
            mSubTitleText.setLayerType(View.LAYER_TYPE_NONE, null);
        }
    }

    public void setOnCenterClickListener(OnClickListener l) {
        mCenterLayout.setOnClickListener(l);
    }

    public void setRightText(CharSequence title) {
        mRightTv.setText(title);
    }

    public void setRightText(int id) {
        setRightText(getContext().getString(id));
    }

    public void setRightTextSize(int unit, float size) {
        mRightTv.setTextSize(unit, size);
    }

    public void setRightTextSize(float size) {
        setRightTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public void setRightTextColor(int color) {
        mRightTv.setTextColor(color);
    }

    public void setRightTextColor(ColorStateList color) {
        try {
            mRightTv.setTextColor(color);
        } catch (Exception e) {
        }
    }

    public void setRightTextBackgroundColor(int color) {
        mRightTv.setBackgroundColor(color);
    }

    public void setRightTextBackgroundResource(int id) {
        mRightTv.setBackgroundResource(id);
    }

    /**
     * 右边文本添加图片
     *
     * @param id 资源id
     */
    public void setRightTextDrawable(int id, int drawablePadding) {
        mRightTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, id, 0);
        setRightTextDrawablePadding(drawablePadding);
    }

    public void setRightTextDrawablePadding(int drawablePadding) {
        this.mRightDrawablePadding = drawablePadding;
        mRightTv.setCompoundDrawablePadding(dip2px(getContext(), mRightDrawablePadding));
    }

    public void setRightTextDrawable(int id) {
        setRightTextDrawable(id, mRightDrawablePadding);
    }

    public void setRightTextPadding(int left, int top, int right, int bottom) {
        mRightTv.setPadding(left, top, right, bottom);
    }

    public void setOnRightTextClickListener(OnClickListener l) {
        mRightTv.setOnClickListener(l);
    }

    public void setRightVisible(boolean visible) {
        mRightTv.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setActionTextSize(int mActionTextSize) {
        this.mActionTextSize = mActionTextSize;
    }

    public void setActionTextColor(int mActionTextColor) {
        this.mActionTextColor = mActionTextColor;
    }

    public void setActionTextBackgroundColor(int mActionTextBackgroundColor) {
        this.mActionTextBackgroundColor = mActionTextBackgroundColor;
    }

    public void setActionTextBackgroundResource(int mActionTextBackgroundResource) {
        this.mActionTextBackgroundResource = mActionTextBackgroundResource;
    }

    public View addLeftAction(Action action, int position) {
        View view = inflateAction(action);
        mLeftLayout.addView(view, position);
        return view;
    }

    public View addLeftAction(Action action) {
        return addLeftAction(action, -1);
    }

    /**
     * 自定义中间部分布局
     */
    public View addCenterAction(Action action, int position) {
        View view = inflateAction(action);
        mCenterLayout.addView(view, position);
        return view;
    }

    /**
     * 自定义中间部分布局
     */
    public View addCenterAction(Action action) {
        return addCenterAction(action, -1);
    }

    /**
     * 在标题栏右边添加action
     *
     * @param action
     * @param position 添加的位置
     */
    public View addRightAction(Action action, int position) {
        View view = inflateAction(action);
        mRightLayout.addView(view, position);
        return view;
    }

    public View addRightAction(Action action) {
        View view = inflateAction(action);
        mRightLayout.addView(view);
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
            text.setTextSize(px2sp(getContext(), mActionTextSize));
            text.setTextColor(mActionTextColor);
            text.setBackgroundColor(mActionTextBackgroundColor);
            if (mActionTextBackgroundResource != -1) {
                text.setBackgroundResource(mActionTextBackgroundResource);
            }
            view = text;
        } else if (obj instanceof Integer) {
            ImageView img = new ImageView(getContext());
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            img.setImageResource((Integer) obj);
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

    public class ImageAction implements Action<Integer> {

        private int mDrawable;
        private OnClickListener onClickListener;

        public ImageAction(int mDrawable, OnClickListener onClickListener) {
            this.mDrawable = mDrawable;
            this.onClickListener = onClickListener;
        }

        public ImageAction(int mDrawable) {
            this.mDrawable = mDrawable;
        }

        @Override
        public Integer getData() {
            return mDrawable;
        }

        @Override
        public OnClickListener getOnClickListener() {
            return onClickListener;
        }

    }

    public class TextAction implements Action<String> {

        private String mText;
        private OnClickListener onClickListener;

        public TextAction(String mText, OnClickListener onClickListener) {
            this.mText = mText;
            this.onClickListener = onClickListener;
        }

        public TextAction(String mText) {
            this.mText = mText;
        }

        public TextAction(int mText) {
            this.mText = getResources().getString(mText);
        }

        public TextAction(int mText, OnClickListener onClickListener) {
            this.mText = getResources().getString(mText);
            this.onClickListener = onClickListener;
        }

        @Override
        public String getData() {
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
     * 获取状态栏高度
     *
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 将dip或dp值转换为px值
     *
     * @param context  上下文
     * @param dipValue dp值
     * @return
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

}
