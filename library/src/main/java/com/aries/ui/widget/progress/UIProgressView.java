package com.aries.ui.widget.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aries.ui.view.radius.RadiusLinearLayout;
import com.aries.ui.view.radius.RadiusViewDelegate;
import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2017-01-18 13:16
 * Function: Loading加载工具
 * Desc:
 */
public class UIProgressView extends Dialog {
    private Context mContext;
    private static UIProgressView dialog;
    private RadiusLinearLayout rootLayout;
    private ProgressBar progressBar;
    private MaterialProgressBar materialProgressBar;
    private TextView textView;
    private Window window;
    private WindowManager.LayoutParams lp;

    private int orientation = LinearLayout.HORIZONTAL;
    private CharSequence text;
    private int textColor;
    private int loadingColor = Color.BLUE;//MD有效
    private Drawable mIndeterminateDrawable;
    private static int mStyle;

    public static final int STYLE_NORMAL = 0;
    public static final int STYLE_MATERIAL_DESIGN = 1;
    public static final int STYLE_WEI_BO = 2;
    public static final int STYLE_WEI_XIN = 3;

    public UIProgressView(Context context) {
        this(context, STYLE_NORMAL);
    }

    public UIProgressView(Context context, int style) {
        super(context, R.style.ProgressViewDialogStyle);
        this.mStyle = style;
        this.mContext = context;
        this.textColor = context.getResources().getColor(R.color.colorLoadingText);
        this.loadingColor = Color.BLUE;
        this.window = getWindow();
        this.lp = window.getAttributes();
        if (mStyle == STYLE_WEI_BO) {//微博样式
            mIndeterminateDrawable = mContext.getResources().getDrawable(R.drawable.dialog_loading_wei_bo);
            textColor = mContext.getResources().getColor(R.color.colorLoadingTextWeiBo);
        } else if (mStyle == STYLE_WEI_XIN) {//微信样式
            mIndeterminateDrawable = mContext.getResources().getDrawable(R.drawable.dialog_loading_wei_xin);
            textColor = Color.WHITE;
        } else if (mStyle == STYLE_MATERIAL_DESIGN) {
            textColor = loadingColor;
        }
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        setContentView(R.layout.layout_progress_view);
        initView();
    }

    protected void initView() {
        rootLayout = (RadiusLinearLayout) findViewById(R.id.lLayout_mainProgressView);
        textView = (TextView) findViewById(R.id.tv_loadingProgressView);// 提示文字
        progressBar = (ProgressBar) findViewById(R.id.pb_mainProgressView);// loading
        progressBar.setVisibility(mStyle != STYLE_MATERIAL_DESIGN ? View.VISIBLE : View.GONE);

        rootLayout.setOrientation(orientation);
        if (mStyle == STYLE_WEI_BO) {
            rootLayout.setOrientation(LinearLayout.VERTICAL);
            rootLayout.setGravity(Gravity.CENTER);
            rootLayout.setMinimumHeight(dp2px(110));
            rootLayout.setMinimumWidth(dp2px(150));
            textView.setPadding(0, dp2px(15), 0, 0);
        }
        if (mStyle == STYLE_MATERIAL_DESIGN) {
            materialProgressBar = new MaterialProgressBar(mContext);
            materialProgressBar.setArcColor(loadingColor);
            rootLayout.addView(materialProgressBar, 0, new ViewGroup.LayoutParams(dp2px(32), dp2px(32)));
        }
        setIndeterminateDrawable(mIndeterminateDrawable);
        setTextColor(textColor);
        setBgColor(mStyle != STYLE_WEI_BO && mStyle != STYLE_WEI_XIN ?
                mContext.getResources().getColor(R.color.colorLoadingBg) :
                mContext.getResources().getColor(R.color.colorLoadingBgWei));
    }

    /**
     * 设置提示文字
     *
     * @param message
     * @return
     */
    public UIProgressView setMessage(CharSequence message) {
        this.text = message;
        textView.setText(message);
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        if (mStyle != STYLE_WEI_BO) {//不知
            textView.post(new Runnable() {
                @Override
                public void run() {
                    int min = dp2px(200);
                    int minimumHeight = dp2px(65);
                    int minimumWidth = dp2px(65) + textView.getWidth();
                    rootLayout.setMinimumHeight(minimumHeight);
                    rootLayout.setMinimumWidth(TextUtils.isEmpty(text) || minimumWidth > min ? minimumWidth : min);
                    textView.getWidth();
                }
            });
        }
        return this;
    }

    /**
     * @param message
     * @return
     */
    public UIProgressView setMessage(int message) {
        return setMessage(mContext.getString(message));
    }

    /**
     * 设置MD风格颜色
     *
     * @param color
     * @return
     */
    public UIProgressView setLoadingColor(int color) {
        loadingColor = color;
        if (materialProgressBar != null) {
            materialProgressBar.setArcColor(color);
            setTextColor(color);
        }
        return this;
    }

    /**
     * 设置文字大小
     *
     * @param unit
     * @param size
     * @return
     */
    public UIProgressView setTextSize(int unit, float size) {
        textView.setTextSize(unit, size);
        return this;
    }

    public UIProgressView setTextSize(float size) {
        return setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public UIProgressView setTextColor(int color) {
        textView.setTextColor(color);
        return this;
    }

    /**
     * 默认模式有效
     *
     * @param drawable
     * @return
     */
    public UIProgressView setIndeterminateDrawable(Drawable drawable) {
        mIndeterminateDrawable = drawable;
        if (mIndeterminateDrawable != null && progressBar.getVisibility() == View.VISIBLE) {
            mIndeterminateDrawable.setBounds(progressBar.getIndeterminateDrawable().getBounds());
            progressBar.setIndeterminateDrawable(mIndeterminateDrawable);
        }
        return this;
    }

    public UIProgressView setIndeterminateDrawable(int resId) {
        Drawable drawable = mContext.getResources().getDrawable(resId);
        return setIndeterminateDrawable(drawable);
    }

    /**
     * 设置背景
     *
     * @param background
     * @return
     */
    public UIProgressView setBackgroundResource(int background) {
        setBgColor(Integer.MAX_VALUE);
        rootLayout.setBackgroundResource(background);
        return this;
    }

    /**
     * 设置背景 Drawable
     *
     * @param background
     * @return
     */
    public UIProgressView setBackground(Drawable background) {
        setBgColor(Integer.MAX_VALUE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            rootLayout.setBackground(background);
        } else {
            rootLayout.setBackgroundDrawable(background);
        }
        return this;
    }

    /**
     * @param color 如:Color.parseColor("#F5F5DC")或Color.argb(0,79,79,79)
     * @return
     */
    public UIProgressView setBackgroundColor(int color) {
        setBgColor(Integer.MAX_VALUE);
        rootLayout.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置背景--包括背景圆角
     *
     * @param color
     * @return
     */
    public UIProgressView setBgColor(int color) {
        RadiusViewDelegate delegate = rootLayout.getDelegate();
        delegate.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置背景--包括背景圆角
     *
     * @param radius 圆角dp
     * @return
     */
    public UIProgressView setBgRadius(float radius) {
        RadiusViewDelegate delegate = rootLayout.getDelegate();
        delegate.setRadius(dp2px(radius));
        return this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha
     * @return
     */
    public UIProgressView setAlpha(float alpha) {
        lp.alpha = alpha;
        return this;
    }

    /**
     * 设置背景黑暗度
     *
     * @param dimAmount
     * @return
     */
    public UIProgressView setDimAmount(float dimAmount) {
        lp.dimAmount = dimAmount;
        return this;
    }

    protected int dp2px(float dp) {
        return MaterialProgressBar.dip2px(mContext, dp);
    }
}
