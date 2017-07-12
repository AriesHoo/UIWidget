package com.aries.ui.widget.action.sheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.aries.ui.widget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created: AriesHoo on 2017-01-20 10:35
 * Function:UIActionSheet效果
 * Desc:
 */
@SuppressLint({"InflateParams", "RtlHardcoded"})
public class UIActionSheetView {
    private Context context;
    private Dialog dialog;
    private TextView txt_title;
    private View vLineTitle;
    private TextView txt_cancel;
    private View rootView;
    private LinearLayout lLayout_content;
    private LinearLayout lLayout_view;
    private boolean showTitle = false;
    private List<SheetItem> listSheetItem;

    private Window window;
    private WindowManager.LayoutParams lp;
    private int unitItems = TypedValue.COMPLEX_UNIT_SP;
    private float textSizeItems = 18;
    private float itemHeight = 45;
    private int STYLE = STYLE_NORMAL;
    public final static int STYLE_NORMAL = 0;
    public final static int STYLE_ROUND = 1;

    public interface OnSheetItemListener {
        void onClick(int position);
    }

    public UIActionSheetView(Context context, int style) {
        this.STYLE = style;
        this.context = context;
        // 获取Dialog布局
        rootView = LayoutInflater.from(context).inflate(
                R.layout.layout_action_sheet_view, null);
        if (STYLE == STYLE_NORMAL) {
            rootView.setPadding(0, 0, 0, 0);
        }
        // 获取自定义Dialog布局中的控件
        lLayout_content = (LinearLayout) rootView
                .findViewById(R.id.lLayout_itemActionSheet);
        lLayout_view = (LinearLayout) rootView
                .findViewById(R.id.lLayout_viewActionSheet);
        txt_title = (TextView) rootView.findViewById(R.id.tv_titleActionSheet);
        vLineTitle = rootView.findViewById(R.id.v_lineTitleActionSheet);
        txt_cancel = (TextView) rootView.findViewById(R.id.tv_cancelActionSheet);
        txt_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        vLineTitle.setVisibility(View.GONE);
        // 定义Dialog布局和参数
        dialog = new Dialog(context, R.style.ActionSheet);
        dialog.setContentView(rootView);
        window = dialog.getWindow();
        window.setGravity(Gravity.LEFT | Gravity.BOTTOM);
        lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                lLayout_view.removeAllViews();
                lLayout_content.removeAllViews();
            }
        });
    }

    public UIActionSheetView(Context context) {
        this(context, STYLE_ROUND);
    }

    public UIActionSheetView builder() {
        return this;
    }

    public Dialog getDialog() {
        return dialog;
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

    public UIActionSheetView setContentView(int layoutResID) {
        dialog.show();
        dialog.setContentView(layoutResID);
        return this;
    }

    public UIActionSheetView setPadding(int left, int top, int right, int bottom) {
        rootView.setPadding(left, top, right, bottom);
        return this;
    }

    public UIActionSheetView setBackgroundColor(int color) {
        rootView.setBackgroundColor(color);
        return this;
    }

    public UIActionSheetView setBackgroundResource(int colorRes) {
        rootView.setBackgroundResource(colorRes);
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
        txt_title.setVisibility(View.VISIBLE);
        txt_title.setText(title);
        txt_title.post(new Runnable() {
            @Override
            public void run() {
                if (txt_title.getLineCount() > 1) {
                    txt_title.setGravity(Gravity.LEFT);
                } else if (txt_title.getLineCount() > 2) {
                    txt_title.setGravity(Gravity.LEFT);
                    txt_title.setLayoutParams(new LayoutParams(
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
        txt_title.setTextSize(unit, textSize);
        return this;
    }

    public UIActionSheetView setTitleColor(int color) {
        txt_title.setTextColor(color);
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
        txt_cancel.setVisibility(View.VISIBLE);
        txt_cancel.setText(message);
        return this;
    }

    public UIActionSheetView setCancelMessage(int message) {
        return setCancelMessage(context.getString(message));
    }


    public UIActionSheetView setCancelMessageMargin(int left, int top, int right, int bottom) {
        setViewMargin(txt_cancel, left, left, left, left, LayoutParams.MATCH_PARENT, getItemHeight());
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
        txt_cancel.setTextSize(unit, textSize);
        return this;
    }

    public UIActionSheetView setCancelColor(int color) {
        txt_cancel.setTextColor(color);
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
        if (lLayout_view != null && view != null) {
            lLayout_view.addView(view);
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
            list.add(new SheetItem(item, null, onItemSelected));
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
        if (lLayout_content != null) {
            lLayout_content.removeAllViews();
        }
        if (lLayout_content != null)
            lLayout_content.removeAllViews();
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
    public void show() {
        setSheetItems();
        if (!dialog.isShowing())
            dialog.show();
    }

    /**
     * 关闭dialog
     */
    public void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * 生成条目
     */
    private void setSheetItems() {
        if (listSheetItem == null || listSheetItem.size() <= 0) {
            return;
        }
        lLayout_content.removeAllViews();
        // 循环添加条目
        for (int i = 0; i <= listSheetItem.size() - 1; i++) {
            final int item = i;
            SheetItem sheetItem = listSheetItem.get(i);
            final OnSheetItemListener listener = sheetItem.itemClickListener;
            View view = new View(context);

            TextView textView = new TextView(context);
            textView.setText(sheetItem.name);
            textView.setTextSize(unitItems, textSizeItems);
            textView.setGravity(Gravity.CENTER);

            vLineTitle.setVisibility(showTitle && STYLE == STYLE_NORMAL ? View.VISIBLE : View.GONE);
            // 背景图片
            if (STYLE == STYLE_ROUND) {
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
                txt_cancel.setBackgroundResource(R.drawable.action_sheet_edge);
                txt_title.setBackgroundResource(R.drawable.action_sheet_edge);
                view.setBackgroundResource(R.color.colorLineGray);
            }
            // 字体颜色
            textView.setTextColor(sheetItem.color);
            // 高度
            textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getItemHeight()));

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
            lLayout_content.addView(textView);
            if (STYLE == STYLE_NORMAL) {
                view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, (int) context.getResources().getDimension(R.dimen.dp_line_size)));
                lLayout_content.addView(view);
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

    public ViewGroup.MarginLayoutParams setViewMargin(View view, int left, int top, int right, int bottom, int width, int height) {
        if (view == null) {
            return null;
        }
        ViewParent parent = view.getParent();
        if (parent == null) {
            return null;
        }
        ViewGroup.MarginLayoutParams lp;
        if (parent instanceof LinearLayout) {
            lp = new LinearLayout.LayoutParams(width, height);
        } else if (parent instanceof RelativeLayout) {
            lp = new RelativeLayout.LayoutParams(width, height);
        } else if (parent instanceof FrameLayout) {
            lp = new FrameLayout.LayoutParams(width, height);
        } else {
            lp = new TableLayout.LayoutParams(width, height);
        }
        if (lp != null) {
            lp.setMargins(left, top, right, bottom);
            view.setLayoutParams(lp);
        }
        return lp;
    }

    public int getItemHeight() {
        float scale = context.getResources().getDisplayMetrics().density;
        int height = (int) (itemHeight * scale + 0.5f);
        return height;
    }
}
