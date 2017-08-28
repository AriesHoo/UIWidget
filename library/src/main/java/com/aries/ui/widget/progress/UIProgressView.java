package com.aries.ui.widget.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import com.aries.ui.widget.R;

/**
 * Created: AriesHoo on 2017-01-18 13:16
 * Function: Loading加载工具
 * Desc:
 */
public class UIProgressView extends Dialog {
    private Context mContext;
    private LinearLayout layoutProgress;
    private ProgressBar progressBar;
    private TextView textView;
    private Window window;
    private WindowManager.LayoutParams lp;

    private int orientation = LinearLayout.HORIZONTAL;
    private CharSequence text;
    private int textUnit;
    private float textSize; //14dp
    private int textColor;
    private int loadingColor = Color.BLUE;//MD有效
    private Drawable mIndeterminateDrawable;
    private int mBackColor;
    private int mBackResource;
    public float alpha = 1.0f;
    public float dimAmount = 0.5f;
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
        this.textUnit = TypedValue.COMPLEX_UNIT_PX;
        this.textSize = MaterialProgressBar.dip2px(context, 14);
        this.textColor = context.getResources().getColor(R.color.colorLoadingText);
        this.loadingColor = Color.BLUE;
        this.window = getWindow();
        this.lp = window.getAttributes();
        if (mStyle == STYLE_WEI_BO) {//微博样式
            mIndeterminateDrawable = mContext.getResources().getDrawable(R.drawable.dialog_loading_wei_bo);
            mBackResource = R.drawable.loading_bg;
            textColor = mContext.getResources().getColor(R.color.colorLoadingTextWeiBo);
        } else if (mStyle == STYLE_WEI_XIN) {//微信样式
            mIndeterminateDrawable = mContext.getResources().getDrawable(R.drawable.dialog_loading_wei_xin);
            mBackResource = R.drawable.loading_bg;
            textColor = Color.WHITE;
        } else if (mStyle == STYLE_MATERIAL_DESIGN) {
            textColor = loadingColor;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_progress_view);
        initView();
    }

    protected void initView() {
        layoutProgress = (LinearLayout) findViewById(R.id.lLayout_mainProgressView);
        textView = (TextView) findViewById(R.id.tv_loadingProgressView);// 提示文字
        progressBar = (ProgressBar) findViewById(R.id.pb_mainProgressView);// loading
        progressBar.setVisibility(mStyle != STYLE_MATERIAL_DESIGN ? View.VISIBLE : View.GONE);

        layoutProgress.setOrientation(orientation);
        if (mStyle == STYLE_WEI_BO) {
            layoutProgress.setOrientation(LinearLayout.VERTICAL);
            layoutProgress.setGravity(Gravity.CENTER);
            layoutProgress.setMinimumHeight(MaterialProgressBar.dip2px(mContext, 110));
            layoutProgress.setMinimumWidth(MaterialProgressBar.dip2px(mContext, 150));
            textView.setPadding(0, MaterialProgressBar.dip2px(mContext, 15), 0, 0);
        } else {
            layoutProgress.setMinimumWidth(MaterialProgressBar.dip2px(mContext, TextUtils.isEmpty(text) ? 65 : 180));
            textView.setPadding(mStyle != STYLE_WEI_XIN ? MaterialProgressBar.dip2px(mContext, 10) : 0, 0, 0, 0);
        }
        if (mStyle == STYLE_MATERIAL_DESIGN) {
            MaterialProgressBar materialProgressBar = new MaterialProgressBar(mContext);
            materialProgressBar.setArcColor(loadingColor);
            layoutProgress.addView(materialProgressBar, 0, new ViewGroup.LayoutParams(MaterialProgressBar.dip2px(mContext, 32), MaterialProgressBar.dip2px(mContext, 32)));
            textColor = loadingColor;
        } else if (mStyle == STYLE_WEI_XIN) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) progressBar.getLayoutParams();
            params.setMargins(MaterialProgressBar.dip2px(mContext, 15), MaterialProgressBar.dip2px(mContext, 15), MaterialProgressBar.dip2px(mContext, 15), MaterialProgressBar.dip2px(mContext, 15));
            progressBar.setLayoutParams(params);
        }
        textView.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        textView.setText(text);
        textView.setTextSize(textUnit, textSize);
        textView.setTextColor(textColor);
        lp.alpha = alpha;
        lp.dimAmount = dimAmount;
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        if (mStyle == STYLE_WEI_XIN) {//不知
            textView.post(new Runnable() {
                @Override
                public void run() {
                    int min = MaterialProgressBar.dip2px(mContext, 180);
                    int minimumHeight = MaterialProgressBar.dip2px(mContext, 65);
                    int minimumWidth = MaterialProgressBar.dip2px(mContext, 65) + textView.getWidth();
                    layoutProgress.setMinimumHeight(minimumHeight);
                    layoutProgress.setMinimumWidth(TextUtils.isEmpty(text) || minimumWidth > min ? minimumWidth : min);
                    textView.getWidth();
                }
            });
        }
        if (mIndeterminateDrawable != null && progressBar.getVisibility() == View.VISIBLE) {
            mIndeterminateDrawable.setBounds(progressBar.getIndeterminateDrawable().getBounds());
            progressBar.setIndeterminateDrawable(mIndeterminateDrawable);
        }
        if (mBackResource != 0) {
            layoutProgress.setBackgroundResource(mBackResource);
        }
        if (mBackColor != 0) {
            layoutProgress.setBackgroundColor(mBackColor);
        }
    }

    /**
     * 设置提示文字
     *
     * @param message
     * @return
     */
    public UIProgressView setMessage(CharSequence message) {
        this.text = message;
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
        textUnit = unit;
        return this;
    }

    public UIProgressView setTextSize(float size) {
        return setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    public UIProgressView setTextColor(int color) {
        textColor = color;
        return this;
    }

    /**
     * 默认模式有效
     *
     * @param drawable
     * @return
     */
    public UIProgressView setIndeterminateDrawable(Drawable drawable) {
        if (mStyle == STYLE_NORMAL)
            mIndeterminateDrawable = drawable;
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
        if (mStyle == STYLE_NORMAL)
            mBackResource = background;
        return this;
    }

    /**
     * @param color 如:Color.parseColor("#F5F5DC")或Color.argb(0,79,79,79)
     * @return
     */
    public UIProgressView setBackgroundColor(int color) {
        if (mStyle == STYLE_NORMAL)
            mBackColor = color;
        return this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha
     * @return
     */
    public UIProgressView setAlpha(float alpha) {
        this.alpha = alpha;
        return this;
    }

    /**
     * 设置背景黑暗度
     *
     * @param dimAmount
     * @return
     */
    public UIProgressView setDimAmount(float dimAmount) {
        this.dimAmount = dimAmount;
        return this;
    }

}
