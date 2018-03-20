package com.aries.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created: AriesHoo on 2018/3/20/020 8:48
 * E-Mail: AriesHoo@126.com
 * Function: Widget Dialog模式基类
 * Description:
 */
public class UIBasisDialog<T extends UIBasisDialog> extends Dialog {

    private Window mWindow;
    protected View mContentView;
    private WindowManager.LayoutParams mLayoutParams;
    private float mAlpha = 1.0f;
    private float mDimAmount = 0.6f;
    private int mGravity = Gravity.CENTER;
    private int mWidth = WindowManager.LayoutParams.MATCH_PARENT;
    private int mHeight = WindowManager.LayoutParams.WRAP_CONTENT;

    public interface OnTextViewLineListener {
        void onTextViewLineListener(TextView textView, int lineCount);
    }

    public UIBasisDialog(Context context) {
        super(context);
    }

    public UIBasisDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected UIBasisDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWindow = getWindow();
        mLayoutParams = mWindow.getAttributes();
        mLayoutParams.width = mWidth;
        mLayoutParams.height = mHeight;
        mLayoutParams.alpha = mAlpha;// 透明度
        mLayoutParams.dimAmount = mDimAmount;// 黑暗度
        mWindow.setAttributes(mLayoutParams);
        mWindow.setGravity(mGravity);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        this.mContentView = view;
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        this.mContentView = view;
    }

    public T setAlpha(float alpha) {
        mAlpha = alpha;
        return (T) this;
    }

    public T setDimAmount(float dimAmount) {
        mDimAmount = dimAmount;
        return (T) this;
    }

    public T setGravity(int gravity) {
        mGravity = gravity;
        return (T) this;
    }

    public T setWidth(int w) {
        this.mWidth = w;
        return (T) this;
    }

    public T setHeight(int h) {
        this.mHeight = h;
        return (T) this;
    }
}
