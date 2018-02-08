package com.aries.ui.widget.action.sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.aries.ui.widget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: AriesHoo on 2018/2/7/007 12:35
 * E-Mail: AriesHoo@126.com
 * Function: UIActionSheet效果
 * Description:
 */
public class UIActionSheetView {
    private Context context;
    private Dialog dialog;
    private TextView tvTitle;
    private View vLineTitle;
    private TextView tvCancel;
    private View rootView;
    private LinearLayout lLayoutItem;
    private LinearLayout lLayoutView;
    private boolean showTitle = false;
    private List<SheetItem> listSheetItem;

    private Window window;
    private WindowManager.LayoutParams lp;
    private int unitItems = TypedValue.COMPLEX_UNIT_DIP;
    private float textSizeItems = 16;
    private float itemHeight = 45;
    private int STYLE = STYLE_NORMAL;
    public final static int STYLE_NORMAL = 0;
    public final static int STYLE_IOS = 1;
    public final static int STYLE_WEI_XIN = 2;
    private boolean canceledOnTouchOutside = true;
    private float mDefaultMarginTop = 120f;
    private float mLineSpacingMultiplier = 1.0f;
    private float mLineSpacingExtra = 0.0f;
    private float mPaddingTop = 4f;
    private float mPaddingLeft = 18f;

    public interface OnSheetItemListener {
        void onClick(int position);
    }

    public UIActionSheetView(Context context, int style) {
        this.STYLE = style;
        this.context = context;
        // 获取Dialog布局
        rootView = LayoutInflater.from(context).inflate(
                R.layout.layout_action_sheet_view, null);
        // 获取自定义Dialog布局中的控件
        lLayoutItem = (LinearLayout) rootView
                .findViewById(R.id.lLayout_itemActionSheet);
        lLayoutView = (LinearLayout) rootView
                .findViewById(R.id.lLayout_viewActionSheet);
        tvTitle = (TextView) rootView.findViewById(R.id.tv_titleActionSheet);
        vLineTitle = rootView.findViewById(R.id.v_lineTitleActionSheet);
        tvCancel = (TextView) rootView.findViewById(R.id.tv_cancelActionSheet);
        tvCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tvCancel.setVisibility(STYLE == STYLE_WEI_XIN ? View.GONE : View.VISIBLE);
        if (STYLE != STYLE_IOS) {
            setPadding(0, 0, 0, 0);
        }
        tvTitle.setVisibility(View.INVISIBLE);
        setMarginTop(mDefaultMarginTop);
        setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
        tvTitle.setPadding(dip2px(mPaddingLeft), dip2px(mPaddingTop), dip2px(mPaddingLeft), dip2px(mPaddingTop));
        tvCancel.setPadding(dip2px(mPaddingLeft), dip2px(mPaddingTop), dip2px(mPaddingLeft), dip2px(mPaddingTop));
        if (STYLE == STYLE_WEI_XIN) {
            setViewMargin(tvCancel, 0, 0, 0, 0);
            tvCancel.setGravity(Gravity.CENTER_VERTICAL);
            tvTitle.setGravity(Gravity.CENTER_VERTICAL);
            setTitleColorResource(R.color.colorActionSheetWeiXinText);
            setCancelColorResource(R.color.colorActionSheetWeiXinText);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            tvCancel.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            itemHeight = 48;
            setViewMargin(tvTitle, 0, (int) (Resources.getSystem().getDisplayMetrics().heightPixels * 0.4), 0, 0);
        }
        setCancelColorResource(STYLE == STYLE_IOS ? R.color.colorActionSheetCancelText : R.color.colorActionSheetNormalItemText);
        vLineTitle.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheetViewDialogStyle);
        dialog.setContentView(rootView);
        window = dialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                lLayoutView.removeAllViews();
                lLayoutItem.removeAllViews();
            }
        });
        rootView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canceledOnTouchOutside)
                    dialog.dismiss();
            }
        });
        tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canceledOnTouchOutside)
                    dialog.dismiss();
            }
        });
    }

    public UIActionSheetView(Context context) {
        this(context, STYLE_NORMAL);
    }

    public Dialog getDialog() {
        return dialog;
    }

    public View getRootView() {
        return rootView;
    }

    /**
     * 设置与顶部间隙避免item过多
     *
     * @param dp
     * @return
     */
    public UIActionSheetView setMarginTop(float dp) {
        setViewMargin(tvTitle, 0, dip2px(dp), 0, 0);
        return this;
    }


    /**
     * @param lineSpacingExtra
     * @param lineSpacingMultiplier {@link TextView#setLineSpacing(float, float)}
     * @return
     */
    public UIActionSheetView setLineSpacing(float lineSpacingExtra, float lineSpacingMultiplier) {
        mLineSpacingExtra = lineSpacingExtra;
        mLineSpacingMultiplier = lineSpacingMultiplier;
        tvTitle.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier);
        tvCancel.setLineSpacing(lineSpacingExtra, lineSpacingMultiplier);
        return this;
    }

    /**
     * 设置窗口透明度
     *
     * @param alpha
     * @return
     */
    public UIActionSheetView setAlpha(float alpha) {
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
    public UIActionSheetView setDimAmount(float dimAmount) {
        lp.dimAmount = dimAmount;// 黑暗度
        window.setAttributes(lp);
        return this;
    }

    public UIActionSheetView setPadding(int left, int top, int right, int bottom) {
        rootView.setPadding(left, top, right, bottom);
        return this;
    }

    public UIActionSheetView setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
        setViewMargin(tvTitle, 0, 0, 0, 0);
        return this;
    }

    public UIActionSheetView setBackgroundResource(int colorRes) {
        rootView.setBackgroundResource(colorRes);
        setViewMargin(tvTitle, 0, 0, 0, 0);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title
     * @return
     */
    public UIActionSheetView setTitle(CharSequence title) {
        showTitle = true;
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        tvTitle.post(new Runnable() {
            @Override
            public void run() {
                if (tvTitle.getLineCount() > 1) {
                    tvTitle.setGravity(Gravity.LEFT);
                } else if (tvTitle.getLineCount() > 2) {
                    tvTitle.setGravity(Gravity.LEFT);
                    tvTitle.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT));
                }
            }
        });
        return this;
    }

    public UIActionSheetView setTitle(int title) {
        return setTitle(context.getString(title));
    }

    /**
     * 设置tititle textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */
    public UIActionSheetView setTitleTextSize(int unit, float textSize) {
        tvTitle.setTextSize(unit, textSize);
        return this;
    }

    public UIActionSheetView setTitleColor(int color) {
        tvTitle.setTextColor(color);
        return this;
    }

    public UIActionSheetView setTitleColor(String color) {
        return setTitleColor(Color.parseColor(color));
    }

    public UIActionSheetView setTitleColorResource(int colorRes) {
        int color = context.getResources().getColor(R.color.colorActionSheetTitleText);
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Exception e) {

        }
        return setTitleColor(color);
    }

    /**
     * 设置cancel 内容
     *
     * @param message
     * @return
     */
    public UIActionSheetView setCancelMessage(CharSequence message) {
        tvCancel.setVisibility(View.VISIBLE);
        tvCancel.setText(message);
        return this;
    }

    public UIActionSheetView setCancelMessage(int message) {
        return setCancelMessage(context.getString(message));
    }


    /**
     * 设置cancel 间隙 --normal模式有效
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @return
     */
    public UIActionSheetView setCancelMessageMargin(float left, float top, float right, float bottom) {
        if (STYLE == STYLE_NORMAL)
            setViewMargin(tvCancel, dip2px(left), dip2px(top), dip2px(right), dip2px(bottom));
        return this;
    }

    /**
     * 设置CancelMessage textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */

    public UIActionSheetView setCancelMessageTextSize(int unit, float textSize) {
        tvCancel.setTextSize(unit, textSize);
        return this;
    }

    public UIActionSheetView setCancelColor(int color) {
        tvCancel.setTextColor(color);
        return this;
    }

    public UIActionSheetView setCancelColor(String color) {
        return setCancelColor(Color.parseColor(color));
    }

    public UIActionSheetView setCancelColorResource(int colorRes) {
        int color = context.getResources().getColor(R.color.colorActionSheetTitleText);
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Exception e) {

        }
        return setCancelColor(color);
    }

    public UIActionSheetView setView(View view) {
        if (lLayoutView != null && view != null) {
            lLayoutView.addView(view);
        }
        return this;
    }

    /**
     * 设置item
     *
     * @param itemList
     * @return
     */
    public UIActionSheetView setItems(List<SheetItem> itemList) {
        listSheetItem = itemList;
        return this;
    }

    public UIActionSheetView setItems(List<CharSequence> items, OnSheetItemListener onItemSelected) {
        if (items == null || items.size() == 0) {
            return this;
        }
        List<SheetItem> list = new ArrayList<>();
        for (CharSequence item : items) {
            list.add(new SheetItem(item,
                    STYLE == STYLE_WEI_XIN ? context.getResources().getColor(R.color.colorActionSheetWeiXinText) :
                            STYLE == STYLE_NORMAL ? context.getResources().getColor(R.color.colorActionSheetNormalItemText) :
                                    context.getResources().getColor(R.color.colorActionSheetItemText)
                    , onItemSelected));
        }
        return setItems(list);
    }

    public UIActionSheetView setItems(CharSequence[] items, OnSheetItemListener onItemSelected) {
        if (items == null || items.length == 0) {
            return this;
        }
        return setItems(Arrays.asList(items), onItemSelected);
    }

    public UIActionSheetView setItems(int itemsRes, OnSheetItemListener onItemSelected) {
        return setItems(context.getResources().getStringArray(itemsRes), onItemSelected);
    }


    /**
     * 设置某个item颜色--需在setItems后调用
     *
     * @param index
     * @param color
     * @return
     */
    public UIActionSheetView setItemTextColor(int index, int color) {
        if (listSheetItem == null || listSheetItem.size() == 0 || index < 0 || index >= listSheetItem.size()) {
            return this;
        }
        listSheetItem.get(index).color = color;
        return this;
    }

    /**
     * @param index
     * @param colorRes
     * @return
     */
    public UIActionSheetView setItemTextColorResource(int index, int colorRes) {
        int color = context.getResources().getColor(R.color.colorActionSheetItemText);
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Exception e) {

        }
        return setItemTextColor(index, color);
    }

    /**
     * 设置所有item颜色-setItems后调用
     *
     * @param color
     * @return
     */
    public UIActionSheetView setItemsTextColor(int color) {
        if (listSheetItem == null || listSheetItem.size() == 0) {
            return this;
        }
        for (SheetItem item : listSheetItem) {
            item.color = color;
        }
        return this;
    }

    /**
     * @param colorRes 设置资源文件颜色
     * @return
     */
    public UIActionSheetView setItemsTextColorResource(int colorRes) {
        int color = context.getResources().getColor(R.color.colorActionSheetItemText);
        try {
            color = context.getResources().getColor(colorRes);
        } catch (Exception e) {

        }
        return setItemsTextColor(color);
    }

    /**
     * 设置条目高度
     *
     * @param itemHeightDp
     * @return
     */
    public UIActionSheetView setItemsHeight(float itemHeightDp) {
        itemHeight = itemHeightDp;
        return this;
    }

    /**
     * 设置item textSize参考 TextView.setTextSize(unit, textSize)方法
     *
     * @param unit
     * @param textSize
     * @return
     */
    public UIActionSheetView setItemsTextSize(int unit, float textSize) {
        unitItems = unit;
        textSizeItems = textSize;
        return this;
    }

    /**
     * 设置返回键是否关闭
     *
     * @param cancel
     * @return
     */
    public UIActionSheetView setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    /**
     * 设置点击非dialog窗口是否关闭
     *
     * @param cancel
     * @return
     */
    public UIActionSheetView setCanceledOnTouchOutside(boolean cancel) {
        this.canceledOnTouchOutside = cancel;
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    /**
     * 设置关闭dialog 监听
     *
     * @param listener
     * @return
     */
    public UIActionSheetView setOnDismissListener(OnDismissListener listener) {
        dialog.setOnDismissListener(listener);
        if (lLayoutItem != null) {
            lLayoutItem.removeAllViews();
        }
        if (lLayoutItem != null)
            lLayoutItem.removeAllViews();
        return this;
    }

    /**
     * 设置监听键盘事件
     *
     * @param listener
     * @return
     */
    public UIActionSheetView setOnKeyListener(OnKeyListener listener) {
        dialog.setOnKeyListener(listener);
        return this;
    }

    /**
     * 展示效果
     */
    public UIActionSheetView show() {
        setSheetItems();
        if (!dialog.isShowing())
            dialog.show();
        return this;
    }

    /**
     * 关闭dialog
     */
    public UIActionSheetView dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
        return this;
    }

    /**
     * 生成条目
     */
    private void setSheetItems() {
        if (listSheetItem == null || listSheetItem.size() <= 0) {
            return;
        }
        lLayoutItem.setGravity(STYLE == STYLE_WEI_XIN ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.CENTER);
        lLayoutItem.removeAllViews();
        // 循环添加条目
        for (int i = 0; i <= listSheetItem.size() - 1; i++) {
            final int item = i;
            SheetItem sheetItem = listSheetItem.get(i);
            final OnSheetItemListener listener = sheetItem.itemClickListener;
            View view = new View(context);

            TextView textView = new TextView(context);
            textView.setText(sheetItem.name);
            textView.setTextSize(unitItems, textSizeItems);
            textView.setGravity(STYLE == STYLE_WEI_XIN ? Gravity.LEFT | Gravity.CENTER_VERTICAL : Gravity.CENTER);
            textView.setPadding(dip2px(mPaddingLeft), dip2px(mPaddingTop), dip2px(mPaddingLeft), dip2px(mPaddingTop));
            vLineTitle.setVisibility(showTitle && STYLE == STYLE_NORMAL ? View.VISIBLE : View.GONE);
            // 背景图片
            if (STYLE == STYLE_IOS) {
                if (listSheetItem.size() == 1) {
                    if (showTitle) {
                        textView.setBackgroundResource(R.drawable.action_sheet_bottom);
                    } else {
                        textView.setBackgroundResource(R.drawable.action_sheet_single);
                    }
                } else {
                    if (showTitle) {
                        if (i >= 0 && i < listSheetItem.size() - 1) {
                            textView.setBackgroundResource(R.drawable.action_sheet_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.action_sheet_bottom);
                        }
                    } else {
                        if (i == 0) {
                            textView.setBackgroundResource(R.drawable.action_sheet_top);
                        } else if (i < listSheetItem.size() - 1) {
                            textView.setBackgroundResource(R.drawable.action_sheet_middle);
                        } else {
                            textView.setBackgroundResource(R.drawable.action_sheet_bottom);
                        }
                    }
                }
            } else {
                textView.setBackgroundResource(R.drawable.action_sheet_edge);
                tvCancel.setBackgroundResource(R.drawable.action_sheet_edge);
                tvTitle.setBackgroundResource(R.color.colorActionSheetEdge);
                view.setBackgroundResource(R.color.colorActionSheetEdgeLineGray);
            }
            // 字体颜色
            textView.setTextColor(sheetItem.color);
            LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            // 高度
            textView.setLayoutParams(params);
            textView.setMinimumHeight(getItemHeight());
            tvTitle.setMinimumHeight(STYLE != STYLE_IOS ? getItemHeight() : dip2px(20));

            tvTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            if (tvTitle.getVisibility() != View.VISIBLE) {
                tvTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
            }
            textView.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
            // 点击事件
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(item);
                    }
                    dialog.dismiss();
                }
            });
            lLayoutItem.addView(textView);
            if (STYLE == STYLE_NORMAL) {
                view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp_line_size)));
                lLayoutItem.addView(view);
            }
        }
    }

    public class SheetItem {
        CharSequence name;
        int color;
        OnSheetItemListener itemClickListener;

        public SheetItem(CharSequence name, Object color, OnSheetItemListener itemClickListener) {
            this.name = name;
            this.itemClickListener = itemClickListener;
            try {
                if (color instanceof Integer) {
                    this.color = (int) color;
                } else if (color instanceof String) {
                    this.color = Color.parseColor((String) color);
                } else {
                    this.color = context.getResources().getColor(R.color.colorActionSheetItemText);
                }
            } catch (Exception e) {
                this.color = context.getResources().getColor(R.color.colorActionSheetItemText);
            }
        }
    }

    public ViewGroup.MarginLayoutParams setViewMargin(View view, int left, int top, int right, int bottom) {
        if (view == null) {
            return null;
        }
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        if (lp != null) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
        return lp;
    }

    public int getItemHeight() {
        return dip2px(itemHeight);
    }

    public int dip2px(float dipValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
