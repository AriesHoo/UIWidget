package com.aries.ui.widget.progress;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


/**
 * Created: AriesHoo on 2017-01-18 13:16
 * Function: ProgressDialog加载工具
 * Desc:
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class UIProgressView {
    private ProgressDialog progressDialog;
    private View rootView;
    private LinearLayout layoutProgress;
    private ProgressBar progressBar;
    private TextView textView;
    private Context mContext;
    private Window window;
    private WindowManager.LayoutParams lp;

    /**
     *
     */
    @SuppressLint("InflateParams")
    public UIProgressView(Context context) {
        this.mContext = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.layout_progress_view, null);// 得到加载view

        layoutProgress = (LinearLayout) rootView.findViewById(R.id.lLayout_mainProgressView);
        textView = (TextView) rootView.findViewById(R.id.tv_loadingProgressView);// 提示文字
        progressBar = (ProgressBar) rootView.findViewById(R.id.pb_mainProgressView);// loading
        progressDialog = new ProgressDialog(context, R.style.ProgressViewDialogStyle);// 创建自定义样式dialog
        progressDialog.show();
        progressDialog.setContentView(rootView);// 设置布局

        window = progressDialog.getWindow();
        lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
    }

    /**
     * 获取布局根视图
     *
     * @return
     */
    public View getRootView() {
        rootView = ((ViewGroup) progressDialog.findViewById(android.R.id.content)).getChildAt(0);
        return rootView;
    }

    /**
     * ProgressDialog 添加根布局
     *
     * @param layoutResID
     * @return
     */
    public UIProgressView setContentView(int layoutResID) {
        progressDialog.show();
        progressDialog.setContentView(layoutResID);
        return this;
    }

    public ProgressDialog getProgressDialog() {
        return progressDialog;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    /**
     * 设置提示文字
     *
     * @param message
     * @return
     */
    public UIProgressView setMessage(CharSequence message) {
        if (!message.toString().isEmpty()) {
            textView.setText(message);
            textView.setVisibility(View.VISIBLE);
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

    public UIProgressView setIndeterminateDrawable(Drawable drawable) {
        Drawable mDrawable = progressBar.getIndeterminateDrawable();
        Rect rect = mDrawable.getBounds();
        drawable.setBounds(rect);
        progressBar.setIndeterminateDrawable(drawable);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UIProgressView setIndeterminateDrawable(int resId) {
        Drawable drawable = progressBar.getIndeterminateDrawable();
        try {
            drawable = mContext.getDrawable(resId);
        } catch (Exception e) {

        }
        return setIndeterminateDrawable(drawable);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public UIProgressView setProgressDrawable(int resId) {
        Drawable drawable = progressBar.getProgressDrawable();
        try {
            drawable = mContext.getDrawable(resId);
        } catch (Exception e) {

        }
        return setIndeterminateDrawable(drawable);
    }

    public UIProgressView setProgressDrawable(Drawable drawable) {
        Drawable mDrawable = progressBar.getProgressDrawable();
        Rect rect = mDrawable.getBounds();
        drawable.setBounds(rect);
        progressBar.setProgressDrawable(drawable);
        return this;
    }

    /**
     * 设置文字颜色
     *
     * @param color
     * @return
     */
    public UIProgressView setTextColor(int color) {
        textView.setTextColor(color);
        return this;
    }

    /**
     * 设置背景
     *
     * @param background
     * @return
     */
    public UIProgressView setBackgroundResource(int background) {
        layoutProgress.setBackgroundResource(background);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public UIProgressView setBackground(Drawable background) {
        layoutProgress.setBackground(background);
        return this;
    }

    /**
     * @param color 如:Color.parseColor("#F5F5DC")或Color.argb(0,79,79,79)
     * @return
     */
    public UIProgressView setBackgroundColor(int color) {
        layoutProgress.setBackgroundColor(color);
        return this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha
     * @return
     */
    public UIProgressView setAlpha(float alpha) {
        lp.alpha = alpha;// 透明度
        window.setAttributes(lp);
        return this;
    }

    /**
     * 设置背景黑暗度
     *
     * @param dimAmount
     * @return
     */
    public UIProgressView setDimAmount(float dimAmount) {
        lp.dimAmount = dimAmount;// 黑暗度
        window.setAttributes(lp);
        return this;
    }

    /**
     * 设置是否点击back键关闭窗口
     *
     * @param flag
     * @return
     */
    public UIProgressView setCancelable(boolean flag) {
        progressDialog.setCancelable(flag);
        return this;
    }

    /**
     * 设置区域外是否关闭
     *
     * @param flag
     * @return
     */
    public UIProgressView setCanceledOnTouchOutside(boolean flag) {
        progressDialog.setCanceledOnTouchOutside(flag);
        return this;
    }

    /**
     * 关闭
     *
     * @return
     */
    public UIProgressView dismiss() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        return this;
    }

    /**
     * @return
     * @返回值 UIProgressView
     * @所属类 UIProgressView.java
     * @创建者 Aries_Hoo
     * @作用 结束窗口
     * @创建时间 2015年3月11日上午10:07:29
     */
    public UIProgressView cancel() {
        if (progressDialog.isShowing()) {
            progressDialog.cancel();
        }
        return this;
    }

    /**
     * 显示
     *
     * @return
     */
    public UIProgressView show() {
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
        return this;
    }

}
