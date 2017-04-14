package com.aries.ui.widget.alert;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aries.ui.widget.R;


/**
 * Created: AriesHoo on 2017-01-19 14:16
 * Function: 自定义AlertDialog 弹出提示框
 * Desc:
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
@SuppressLint("InflateParams")
public class UIAlertView {
    private Context context;
    private AlertDialog dialog;
    private TextView txt_title;
    private TextView txt_msg;
    //addView 父容器
    private LinearLayout dialog_Group;
    private LinearLayout linearLayoutMain;
    private LinearLayout linearLayoutGroup;
    private TextView btn_left;
    private TextView btn_middle;
    private TextView btn_right;
    private ImageView imageViewDelete;
    private View mViewLine;
    private View mViewLineRight;
    private View mViewLineHorizontal;
    private boolean showTitle = false;
    private boolean showMsg = false;
    private boolean showLayout = false;
    private boolean showPosBtn = false;
    private boolean showNegBtn = false;
    private boolean showNeuBtn = false;
    private boolean setMinWidth = false;
    /**
     * 是否自定义了button样式
     */
    private boolean isCustomButtonStyle = false;

    private int gravity = Gravity.CENTER;
    private Window window;
    private WindowManager.LayoutParams lp;

    public UIAlertView(Context context) {
        this.context = context;
        // 获取Dialog布局
        View view = LayoutInflater.from(context).inflate(
                R.layout.layout_alert_view, null);
        // 获取自定义Dialog布局中的控件
        imageViewDelete = (ImageView) view.findViewById(R.id.iv_deleteAlertView);
        imageViewDelete.setVisibility(View.GONE);
        txt_title = (TextView) view.findViewById(R.id.tv_titleAlertView);
        txt_title.setVisibility(View.GONE);
        txt_msg = (TextView) view.findViewById(R.id.tv_msgAlertView);
        txt_msg.setVisibility(View.GONE);
        dialog_Group = (LinearLayout) view.findViewById(R.id.lLayout_viewAlertView);
        dialog_Group.setVisibility(View.GONE);
        btn_left = (TextView) view.findViewById(R.id.tv_leftAlertView);
        btn_left.setVisibility(View.GONE);
        btn_middle = (TextView) view.findViewById(R.id.tv_middleAlertView);
        btn_middle.setVisibility(View.GONE);
        btn_right = (TextView) view.findViewById(R.id.tv_rightAlertView);
        btn_right.setVisibility(View.GONE);
        mViewLine = view.findViewById(R.id.v_lineAlertView);
        mViewLine.setVisibility(View.GONE);
        mViewLineHorizontal = view.findViewById(R.id.v_lineHorizontalAlertView);
        mViewLineHorizontal.setVisibility(View.GONE);
        mViewLineRight = view.findViewById(R.id.v_lineRightAlertView);
        mViewLineRight.setVisibility(View.GONE);
        linearLayoutGroup = (LinearLayout) view.findViewById(R.id.lLayout_groupAlertView);
        linearLayoutMain = (LinearLayout) view.findViewById(R.id.lLayout_mainAlertView);
        // 定义Dialog布局和参数
        dialog = new AlertDialog.Builder(context, R.style.AlertViewDialogStyle).create();
        dialog.show();
        dialog.setContentView(view);
        window = dialog.getWindow();
        lp = window.getAttributes();
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog_Group.removeAllViews();
            }
        });
        dialog.dismiss();
    }

    public UIAlertView builder() {
        return this;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public ImageView getImageDelete() {
        return imageViewDelete;
    }

    public TextView getTitleView() {
        return txt_title;
    }

    public TextView getMessageView() {
        return txt_msg;
    }

    public TextView getLeftButton() {
        return btn_left;
    }

    public TextView getMiddleButton() {
        return btn_middle;
    }

    public TextView getRightButton() {
        return btn_right;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha
     * @return
     */
    public UIAlertView setAlpha(float alpha) {
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
    public UIAlertView setDimAmount(float dimAmount) {
        lp.dimAmount = dimAmount;// 黑暗度
        window.setAttributes(lp);
        return this;
    }

    public UIAlertView setContentView(int layoutResID) {
        dialog.show();
        dialog.setContentView(layoutResID);
        return this;
    }

    public UIAlertView setBackgroundColor(int color) {
        linearLayoutMain.setBackgroundColor(color);
        return this;
    }

    public UIAlertView setBackgroundResource(int resId) {
        linearLayoutMain.setBackgroundResource(resId);
        return this;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public UIAlertView setBackground(Drawable background) {
        linearLayoutMain.setBackground(background);
        return this;
    }

    public UIAlertView setDeleteImageBitmap(Bitmap bitmap) {
        imageViewDelete.setImageBitmap(bitmap);
        imageViewDelete.setVisibility(View.VISIBLE);
        return this;
    }

    public UIAlertView setDeleteImageResource(int resource) {
        imageViewDelete.setImageResource(resource);
        imageViewDelete.setVisibility(View.VISIBLE);
        return this;
    }

    public UIAlertView setDeleteImageDrawable(Drawable drawable) {
        imageViewDelete.setImageDrawable(drawable);
        imageViewDelete.setVisibility(View.VISIBLE);
        return this;
    }

    public UIAlertView setDeleteImageVisibility(int visibility) {
        imageViewDelete.setVisibility(visibility);
        return this;
    }

    public UIAlertView setDeleteImageWidthHeight(int width, int height) {
        setViewWidthAndHeight(imageViewDelete, width, height);
        return this;
    }

    public UIAlertView setOnDeleteImageClickListener(OnClickListener onClickListener) {
        imageViewDelete.setOnClickListener(onClickListener);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public UIAlertView setTitle(CharSequence title) {
        if (!TextUtils.isEmpty(title)) {
            showTitle = true;
            txt_title.setText(title);
        }
        return this;
    }

    public UIAlertView setTitle(int title) {
        return setTitle(context.getString(title));
    }

    public UIAlertView setTitleTextColor(int color) {
        txt_title.setTextColor(color);
        return this;
    }

    public UIAlertView setTitleTextColor(ColorStateList color) {
        txt_title.setTextColor(color);
        return this;
    }

    /**
     * 设置title textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */
    public UIAlertView setTitleTextSize(int unit, float textSize) {
        txt_title.setTextSize(unit, textSize);
        return this;
    }

    /**
     * 设置提示语
     *
     * @param msg
     * @return
     */
    public UIAlertView setMessage(CharSequence msg) {
        showMsg = true;
        txt_msg.setText(msg);
        txt_msg.post(new Runnable() {

            @Override
            public void run() {
                if (txt_msg.getLineCount() > 4) {
                    txt_msg.setMaxWidth((int) context.getResources()
                            .getDimension(R.dimen.alert_max_width));
                }
                {
                    txt_msg.setMaxWidth((int) context.getResources()
                            .getDimension(R.dimen.alert_max_width_));
                }
            }
        });
        int padding = (int) context.getResources().getDimension(R.dimen.alert_dp_padding);
        txt_msg.setPadding(padding, padding, padding, padding);
        txt_msg.setGravity(gravity);
        return this;
    }

    public UIAlertView setMessage(CharSequence msg, int gravity) {
        this.gravity = gravity;
        return setMessage(msg);
    }

    public UIAlertView setMessage(int msg, int gravity) {
        this.gravity = gravity;
        return setMessage(msg);
    }

    public UIAlertView setMessage(int msg) {
        return setMessage(context.getString(msg));
    }


    public UIAlertView setMessageTextColor(int color) {
        txt_msg.setTextColor(color);
        return this;
    }

    public UIAlertView setMessageTextColor(ColorStateList color) {
        txt_msg.setTextColor(color);
        return this;
    }

    /**
     * 设置Message textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */
    public UIAlertView setMessageTextSize(int unit, float textSize) {
        txt_msg.setTextSize(unit, textSize);
        return this;
    }

    /**
     * 设置message最低高度
     *
     * @param minHeight
     * @return
     */
    public UIAlertView setMessageMinHeight(final int minHeight) {
        txt_msg.setMinimumHeight(minHeight);
        return this;
    }

    /**
     * @param minHeight
     * @return
     */
    public UIAlertView setMinHeight(final int minHeight) {
        linearLayoutMain.setMinimumHeight(minHeight);
        linearLayoutGroup.setMinimumHeight(minHeight);
        return this;
    }


    public UIAlertView setMinWidth(final int minWidth) {
        linearLayoutMain.setMinimumWidth(minWidth);
        linearLayoutGroup.setMinimumWidth(minWidth);
        setMinWidth = true;
        return this;
    }

    /**
     * 设置Button textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */
    public UIAlertView setButtonTextSize(int unit, float textSize) {
        btn_left.setTextSize(unit, textSize);
        btn_right.setTextSize(unit, textSize);
        return this;
    }

    /**
     * 添加视图
     *
     * @param view
     * @return
     */
    public UIAlertView setView(View view) {
        showLayout = true;
        if (view == null) {
            showLayout = false;
        } else {
            dialog_Group.addView(view,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                    android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        }
        return this;
    }

    /**
     * 设置左边按钮
     *
     * @param text
     * @param listener
     * @return
     */
    public UIAlertView setNegativeButton(CharSequence text,
                                         final DialogInterface.OnClickListener listener) {
        showNegBtn = true;
        btn_left.setText(text);
        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 修改左边button背景
     *
     * @param resId
     * @return
     */
    public UIAlertView setNegativeButtonBackgroundResource(int resId) {
        isCustomButtonStyle = true;
        btn_left.setBackgroundResource(resId);
        return this;
    }

    public UIAlertView setNegativeButtonBackgroundColor(int color) {
        isCustomButtonStyle = true;
        btn_left.setBackgroundColor(color);
        return this;
    }

    public UIAlertView setNegativeButtonBackground(Drawable background) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            isCustomButtonStyle = true;
            btn_left.setBackground(background);
        }
        return this;
    }

    public UIAlertView setNegativeButtonTextColor(int color) {
        btn_left.setTextColor(color);
        return this;
    }

    public UIAlertView setNegativeButtonTextColor(ColorStateList color) {
        btn_left.setTextColor(color);
        return this;
    }

    public UIAlertView setNeutralButton(CharSequence text,
                                        final DialogInterface.OnClickListener listener) {
        showNeuBtn = true;
        btn_middle.setText(text);
        btn_middle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_NEUTRAL);
                }
                dialog.dismiss();
            }
        });
        return this;
    }

    public UIAlertView setNeutralButton(int text,
                                        final DialogInterface.OnClickListener listener) {
        return setNeutralButton(context.getString(text), listener);
    }

    public UIAlertView setNeutralButtonTextColor(int color) {
        btn_middle.setTextColor(color);
        return this;
    }

    public UIAlertView setNeutralButtonTextColor(ColorStateList color) {
        btn_middle.setTextColor(color);
        return this;
    }

    /**
     * 设置中间按钮样式
     *
     * @param background
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public UIAlertView setNeutralButtonBackground(Drawable background) {
        isCustomButtonStyle = true;
        btn_middle.setBackground(background);
        return this;
    }

    public UIAlertView setNeutralButtonBackgroundResource(int resId) {
        isCustomButtonStyle = true;
        btn_middle.setBackgroundResource(resId);
        return this;
    }

    public UIAlertView setNeutralButtonBackgroundColor(int color) {
        isCustomButtonStyle = true;
        btn_middle.setBackgroundColor(color);
        return this;
    }


    /**
     * 设置右边按钮样式
     *
     * @param background
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public UIAlertView setPositiveButtonBackground(Drawable background) {
        isCustomButtonStyle = true;
        btn_right.setBackground(background);
        return this;
    }

    /**
     * 设置右边button背景样式
     *
     * @param resId
     * @return
     */
    public UIAlertView setPositiveButtonBackgroundResource(int resId) {
        isCustomButtonStyle = true;
        btn_right.setBackgroundResource(resId);
        return this;
    }

    public UIAlertView setPositiveButtonBackgroundColor(int color) {
        isCustomButtonStyle = true;
        btn_right.setBackgroundColor(color);
        return this;
    }

    public UIAlertView setNegativeButton(int text,
                                         final DialogInterface.OnClickListener listener) {
        return setNegativeButton(context.getString(text), listener);
    }

    public UIAlertView setPositiveButtonTextColor(int color) {
        btn_right.setTextColor(color);
        return this;
    }

    public UIAlertView setPositiveButtonTextColor(ColorStateList color) {
        btn_right.setTextColor(color);
        return this;
    }

    public UIAlertView setPositiveButton(CharSequence text,
                                         final DialogInterface.OnClickListener listener) {
        return setPositiveButton(text, listener, true);
    }

    public UIAlertView setPositiveButton(CharSequence text,
                                         final DialogInterface.OnClickListener listener, final boolean isDismiss) {
        showPosBtn = true;
        btn_right.setText(text);
        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
                if (isDismiss)
                    dialog.dismiss();
            }
        });
        return this;
    }

    /**
     * 设置右边按钮
     *
     * @param text
     * @param listener
     * @return
     */
    public UIAlertView setPositiveButton(int text,
                                         final DialogInterface.OnClickListener listener) {
        return setPositiveButton(context.getString(text), listener);
    }

    public UIAlertView setPositiveButton(int text,
                                         final DialogInterface.OnClickListener listener, final boolean isDismiss) {
        return setPositiveButton(context.getString(text), listener, isDismiss);
    }

    private void setLayout() {
        if (showTitle) {
            txt_title.setVisibility(View.VISIBLE);
        }
        linearLayoutGroup.setGravity(gravity);
        txt_msg.setGravity(gravity);
        linearLayoutGroup.setGravity(gravity);
        if (showMsg) {
            txt_msg.setVisibility(View.VISIBLE);
        }
        if (showLayout) {
            dialog_Group.setVisibility(View.VISIBLE);
        }
        if (showPosBtn || showNegBtn || showNeuBtn) {//都没有
            mViewLineHorizontal.setVisibility(View.VISIBLE);
        }
        if (isCustomButtonStyle) {//设置过自定义样式不再控制
            if (showNegBtn) {
                btn_left.setVisibility(View.VISIBLE);
            }
            if (showNeuBtn) {
                btn_middle.setVisibility(View.VISIBLE);
            }
            if (showPosBtn) {
                btn_right.setVisibility(View.VISIBLE);
            }
            return;
        }
        if (!showPosBtn && showNegBtn && !showNeuBtn) {//左一个
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setBackgroundResource(R.drawable.alert_btn_single_selector);
            mViewLine.setVisibility(View.GONE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (showPosBtn && !showNegBtn & !showNeuBtn) {//右一个
            btn_right.setVisibility(View.VISIBLE);
            btn_right.setBackgroundResource(R.drawable.alert_btn_single_selector);
            mViewLine.setVisibility(View.GONE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (!showPosBtn && !showNegBtn & showNeuBtn) {//中一个
            btn_middle.setVisibility(View.VISIBLE);
            btn_middle.setBackgroundResource(R.drawable.alert_btn_single_selector);
            mViewLine.setVisibility(View.GONE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (showPosBtn && showNegBtn && !showNeuBtn) {//左右两个
            btn_right.setVisibility(View.VISIBLE);
            btn_right.setBackgroundResource(R.drawable.alert_btn_right_selector);
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setBackgroundResource(R.drawable.alert_btn_left_selector);
            mViewLine.setVisibility(View.VISIBLE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (!showPosBtn && showNegBtn && showNeuBtn) {//左中两个
            btn_middle.setVisibility(View.VISIBLE);
            btn_middle
                    .setBackgroundResource(R.drawable.alert_btn_right_selector);
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setBackgroundResource(R.drawable.alert_btn_left_selector);
            mViewLine.setVisibility(View.VISIBLE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (showPosBtn && !showNegBtn && showNeuBtn) {//中右两个
            btn_right.setVisibility(View.VISIBLE);
            btn_right
                    .setBackgroundResource(R.drawable.alert_btn_right_selector);
            btn_middle.setVisibility(View.VISIBLE);
            btn_middle.setBackgroundResource(R.drawable.alert_btn_left_selector);
            mViewLine.setVisibility(View.VISIBLE);
            mViewLineRight.setVisibility(View.GONE);
        } else if (showPosBtn && showNegBtn && showNeuBtn) {//三个
            btn_right.setVisibility(View.VISIBLE);
            btn_right.setBackgroundResource(R.drawable.alert_btn_right_selector);
            btn_left.setVisibility(View.VISIBLE);
            btn_left.setBackgroundResource(R.drawable.alert_btn_left_selector);
            btn_middle.setVisibility(View.VISIBLE);
            btn_middle.setBackgroundResource(R.drawable.alert_btn_middle_selector);
            mViewLine.setVisibility(View.VISIBLE);
            mViewLineRight.setVisibility(View.VISIBLE);
        }

    }

    public UIAlertView setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return false;
            }
        });
        return this;
    }

    public UIAlertView setOnDismissListener(OnDismissListener onDismissListener) {
        dialog.setOnDismissListener(onDismissListener);
        return this;
    }

    /**
     * 是否设置点击dialog区域外，dialog消失
     *
     * @param cancel
     */
    public UIAlertView setCanceledOnTouchOutside(boolean cancel) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(cancel);
        }
        return this;
    }

    /**
     * 师傅返回键弹框可消失
     *
     * @param cancel
     * @return
     */
    public UIAlertView setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public UIAlertView show() {
        setLayout();
        if (!dialog.isShowing()) {
            dialog.show();
        }
        return this;
    }

    public UIAlertView dismiss() {
        if (dialog.isShowing()) {
            dialog.cancel();
            dialog.dismiss();
        }
        return this;
    }

    public void setViewWidthAndHeight(View view, int width, int height) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.width = width;
        lp.height = height;
        view.setLayoutParams(lp);
    }
}
