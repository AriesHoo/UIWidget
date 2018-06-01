package com.aries.ui.widget.action.sheet;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.aries.ui.util.DrawableUtil;
import com.aries.ui.util.FindViewUtil;
import com.aries.ui.view.DragLayout;
import com.aries.ui.view.alpha.AlphaTextView;
import com.aries.ui.widget.BasisDialog;
import com.aries.ui.widget.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created: AriesHoo on 2018/2/7/007 13:21
 * E-Mail: AriesHoo@126.com
 * Function: UIActionSheet效果Dialog
 * Description:
 * 1、继承自Dialog 并封装不同Builder模式
 * 2、修改Dialog继承关系并修改默认ListSheetBuilder模式属性设置
 * 3、2018-3-29 13:40:24 新增设置ListBuilder默认imageView默认宽高及文字与图片间距属性控制
 * 修改 getListView及getGridView返回对象错误问题
 * 4、2018-4-3 09:10:18 新增view拖拽关闭交互效果{@link Builder#setDragEnable(boolean)}
 * 5、2018-4-3 12:50:31 将控制虚拟导航栏功能从BasisDialog移至此处
 * 6、2018-4-6 21:43:20 调整设置cancel 及margin逻辑
 * 7、2018-5-23 16:54:27 调整Cancel背景处理逻辑避免因设置GridView 间隔造成问题
 * 8、2018-5-28 16:39:05 修改兼容Android O以上版本导航栏问题
 * 9、2018-5-31 16:48:38 删除TranslucentUtil控制导航栏透明度
 */
public class UIActionSheetDialog extends BasisDialog<UIActionSheetDialog> {

    public interface OnItemClickListener {
        void onClick(BasisDialog dialog, View itemView, int position);
    }

    public UIActionSheetDialog(Context context) {
        super(context, R.style.ActionSheetViewDialogStyle);
    }

    /**
     * 获取标题
     *
     * @return 未设置Title文本则返回空
     */
    public TextView getTitle() {
        return FindViewUtil.getTargetView(mContentView, R.id.tv_titleActionSheetDialog);
    }

    /**
     * 获取底部取消按钮
     *
     * @return 未设置Cancel文本则返回空
     */
    public TextView getCancel() {
        return FindViewUtil.getTargetView(mContentView, R.id.tv_cancelActionSheetDialog);
    }

    /**
     * 获取ListView
     *
     * @return 未设置Item或无
     */
    public ListView getListView() {
        return FindViewUtil.getTargetView(mContentView, R.id.lv_containerActionSheetDialog);
    }

    public GridView getGridView() {
        return FindViewUtil.getTargetView(mContentView, R.id.gv_containerActionSheetDialog);
    }

    public interface ICreateContentView {
        View createItemView();

        ListAdapter getDefaultAdapter();
    }

    public static class SheetItem {
        CharSequence text;
        ColorStateList textColor;
        Drawable drawable;

        public SheetItem() {
            this("");
        }

        public SheetItem(CharSequence text) {
            this(text, -1);
        }

        public SheetItem(CharSequence text, int color) {
            this(text, color > 0 ? ColorStateList.valueOf(color) : null);
        }

        public SheetItem(CharSequence text, ColorStateList color) {
            this(text, color, null);
        }

        public SheetItem(CharSequence text, ColorStateList color, Drawable drawable) {
            this.text = text;
            this.textColor = color;
            this.drawable = drawable;
        }

        public SheetItem setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public SheetItem setImageDrawable(Drawable drawable) {
            this.drawable = drawable;
            return this;
        }

        public SheetItem setTextColor(ColorStateList color) {
            this.textColor = color;
            return this;
        }

        public SheetItem setTextColor(int color) {
            return setTextColor(ColorStateList.valueOf(color));
        }
    }

    /**
     * 类微信风格
     */
    public static class ListWeChatBuilder extends ListBuilder<ListWeChatBuilder> {

        public ListWeChatBuilder(Context context) {
            super(context);
            setItemsDividerHeight(0)
                    .setTitleGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                    .setCancelGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                    .setItemsTextGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                    .setItemsGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL)
                    .setCancelMarginTop(0);
        }
    }

    /**
     * 类iOS风格
     */
    public static class ListIOSBuilder extends ListBuilder<ListIOSBuilder> {

        public ListIOSBuilder(Context context) {
            super(context);
            setBackground(null)
                    .setItemsTopPressedDrawableResource(R.drawable.action_sheet_top_pressed)
                    .setItemsTopDrawableResource(R.drawable.action_sheet_top_normal)
                    .setItemsCenterPressedDrawableResource(R.drawable.action_sheet_middle_pressed)
                    .setItemsCenterDrawableResource(R.drawable.action_sheet_middle_normal)
                    .setItemsBottomPressedDrawableResource(R.drawable.action_sheet_bottom_pressed)
                    .setItemsBottomDrawableResource(R.drawable.action_sheet_bottom_normal)
                    .setItemsSinglePressedDrawableResource(R.drawable.action_sheet_single_pressed)
                    .setItemsSingleDrawableResource(R.drawable.action_sheet_single_normal)
                    .setCancelTextColorResource(R.color.colorActionSheetItemText)
                    .setItemsTextColorResource(R.color.colorActionSheetItemText)
                    .setItemsDividerHeight(0)
                    .setPadding(dp2px(8));
        }
    }

    /**
     * 常见风格--类QQ/BottomSheet
     */
    public static class ListSheetBuilder extends ListBuilder<ListSheetBuilder> {
        public ListSheetBuilder(Context context) {
            super(context);

            setCancelTextColorResource(R.color.colorActionSheetNormalItemText)
                    .setItemsTextColorResource(R.color.colorActionSheetNormalItemText);
        }
    }

    private static class ListBuilder<T extends ListBuilder> extends Builder<T> {

        private List<View> mListHeaderViews;
        private List<View> mListFooterViews;
        private ListView mLvContainer;

        private Drawable mItemsDivider;
        private int mItemsDividerHeight;

        public ListBuilder(Context context) {
            super(context);
            setItemDrawableResource(R.color.colorActionSheetEdge)
                    .setItemPressedDrawableResource(R.color.colorActionSheetEdgePressed)
                    .setItemsDividerResource(R.color.colorActionSheetEdgeLineGray)
                    .setItemsDividerHeightResource(R.dimen.dp_action_sheet_list_line_height)
                    .setItemsImageWidth(dp2px(26))
                    .setItemsImageHeight(dp2px(26))
                    .setCancelMarginTop(dp2px(8));
        }

        /**
         * {@link ListView#addHeaderView(View)}
         *
         * @param view
         * @return
         */
        public T addHeaderView(View view) {
            if (view != null) {
                if (mListHeaderViews == null) {
                    mListHeaderViews = new ArrayList<>();
                }
                mListHeaderViews.add(view);
            }
            return backBuilder();
        }

        /**
         * {@link ListView#addFooterView(View)}
         *
         * @param view
         * @return
         */
        public T addFooterView(View view) {
            if (view != null) {
                if (mListFooterViews == null) {
                    mListFooterViews = new ArrayList<>();
                }
                mListFooterViews.add(view);
            }
            return backBuilder();
        }

        public T setItemDrawable(Drawable drawable) {
            setItemsSingleDrawable(drawable)
                    .setItemsTopDrawable(drawable)
                    .setItemsCenterDrawable(drawable)
                    .setItemsBottomDrawable(drawable);
            return backBuilder();
        }

        public T setItemDrawableResource(int res) {
            return setItemDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemDrawableColor(int color) {
            return setItemDrawable(new ColorDrawable(color));
        }

        public T setItemPressedDrawable(Drawable drawable) {
            setItemsSinglePressedDrawable(drawable).
                    setItemsTopPressedDrawable(drawable)
                    .setItemsCenterPressedDrawable(drawable)
                    .setItemsBottomPressedDrawable(drawable);
            return backBuilder();
        }

        public T setItemPressedDrawableResource(int res) {
            return setItemPressedDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemPressedDrawableColor(int color) {
            return setItemPressedDrawable(new ColorDrawable(color));
        }

        public T setItemsTopDrawable(Drawable drawable) {
            mTopDrawable = drawable;
            return backBuilder();
        }

        public T setItemsTopDrawableResource(int res) {
            return setItemsTopDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsTopDrawableColor(int color) {
            return setItemsTopDrawable(new ColorDrawable(color));
        }

        public T setItemsTopPressedDrawable(Drawable drawable) {
            mTopPressedDrawable = drawable;
            return backBuilder();
        }

        public T setItemsTopPressedDrawableResource(int res) {
            return setItemsTopPressedDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsTopPressedDrawableColor(int color) {
            return setItemsTopPressedDrawable(new ColorDrawable(color));
        }

        public T setItemsCenterDrawable(Drawable drawable) {
            mCenterDrawable = drawable;
            return backBuilder();
        }

        public T setItemsCenterDrawableResource(int res) {
            return setItemsCenterDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsCenterDrawableColor(int color) {
            return setItemsCenterDrawable(new ColorDrawable(color));
        }

        public T setItemsCenterPressedDrawable(Drawable drawable) {
            mCenterPressedDrawable = drawable;
            return backBuilder();
        }

        public T setItemsCenterPressedDrawableResource(int res) {
            return setItemsCenterPressedDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsCenterPressedDrawableColor(int color) {
            return setItemsCenterPressedDrawable(new ColorDrawable(color));
        }

        public T setItemsBottomDrawable(Drawable drawable) {
            mBottomDrawable = drawable;
            return backBuilder();
        }

        public T setItemsBottomDrawableResource(int res) {
            return setItemsBottomDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsBottomDrawableColor(int color) {
            return setItemsBottomDrawable(new ColorDrawable(color));
        }

        public T setItemsBottomPressedDrawable(Drawable drawable) {
            mBottomPressedDrawable = drawable;
            return backBuilder();
        }

        public T setItemsBottomPressedDrawableResource(int res) {
            return setItemsBottomPressedDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsBottomPressedDrawableColor(int color) {
            return setItemsBottomPressedDrawable(new ColorDrawable(color));
        }

        public T setItemsDivider(Drawable drawable) {
            mItemsDivider = drawable;
            return backBuilder();
        }

        public T setItemsDividerColor(int color) {
            return setItemsDivider(new ColorDrawable(color));
        }

        public T setItemsDividerResource(int res) {
            return setItemsDivider(mResourceUtil.getDrawable(res));
        }

        public T setItemsDividerHeight(int height) {
            mItemsDividerHeight = height;
            return backBuilder();
        }

        public T setItemsDividerHeightResource(int res) {
            return setItemsDividerHeight(mResourceUtil.getDimensionPixelSize(res));
        }

        /**
         * 创建标题线框
         */
        private void createLine(boolean isTitle) {
            if (isTitle && (TextUtils.isEmpty(mTitleStr) || mItemsDividerHeight <= 0 || mItemsDivider == null ||
                    ((mListItem == null || mListItem.size() == 0) && (mListHeaderViews == null)))) {
                return;
            }
            if (!isTitle && (TextUtils.isEmpty(mCancelStr) || mItemsDividerHeight <= 0 || mItemsDivider == null
                    || mCancelMarginTop > 0)) {
                return;
            }
            Drawable back = DrawableUtil.getNewDrawable(mItemsDivider);
            View viewLine = new View(mContext);
            viewLine.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, mItemsDividerHeight));

            mLLayoutRoot.addView(viewLine);
            setViewBackground(viewLine, back);
        }

        private void createList() {
            if (mListHeaderViews == null && mListFooterViews == null && mListItem == null) {
                return;
            }
            mLvContainer = new ListView(mContext);
            mLvContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
            mLLayoutRoot.addView(mLvContainer);

            //设置ListView相关
            mLvContainer.setId(R.id.lv_containerActionSheetDialog);
            mLvContainer.setVerticalScrollBarEnabled(false);
            mLvContainer.setOverScrollMode(View.OVER_SCROLL_NEVER);
            if (mListHeaderViews != null) {
                for (View v : mListHeaderViews) {
                    mLvContainer.addHeaderView(v);
                }
            }
            if (mListFooterViews != null) {
                for (View v : mListFooterViews) {
                    mLvContainer.addFooterView(v);
                }
            }
            mLvContainer.setDivider(DrawableUtil.getNewDrawable(mItemsDivider));
            mLvContainer.setDividerHeight(mItemsDividerHeight);
            mLvContainer.setAdapter(mAdapter);
        }

        @Override
        public View createItemView() {
            createLine(true);
            createList();
            createLine(false);
            return mLvContainer;
        }

        @Override
        public ListAdapter getDefaultAdapter() {
            return new ListAdapter();
        }

        private class ListAdapter extends SheetAdapter {
            @Override
            public View getView(final int i, View convertView, ViewGroup parent) {
                final SheetItem data = getItem(i);
                final ViewHolder holder;
                Drawable background;
                if (convertView == null) {
                    convertView = View.inflate(mContext, R.layout.item_action_sheet_list, null);
                    holder = new ViewHolder();
                    holder.imageView = (ImageView) convertView.findViewById(R.id.iv_iconActionSheetList);
                    holder.textView = (TextView) convertView.findViewById(R.id.tv_msgActionSheetList);
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                if (data.drawable != null) {
                    holder.imageView.setVisibility(View.VISIBLE);
                    holder.imageView.setImageDrawable(data.drawable);
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.imageView.getLayoutParams();
                    params.height = mItemsImageHeight;
                    params.width = mItemsImageWidth;
                    params.rightMargin = mTextDrawablePadding;
                    holder.imageView.setLayoutParams(params);
                } else {
                    holder.imageView.setVisibility(View.GONE);
                }
                setTextView(holder, data, i);

                ((LinearLayout) convertView).setGravity(mItemsGravity);
                convertView.setMinimumHeight(mItemsMinHeight);
                convertView.setPadding(mItemsTextPaddingLeft, mItemsTextPaddingTop, mItemsTextPaddingRight, mItemsTextPaddingBottom);

                int size = getCount();
                int sizeHeader = mListHeaderViews != null ? mListHeaderViews.size() : 0;
                boolean hasTitle = !TextUtils.isEmpty(mTitleStr);
                boolean hasMargin = mCancelMarginTop > 0 || TextUtils.isEmpty(mCancelStr);
                if (size == 1) {
                    if (hasTitle || sizeHeader > 0) {
                        background = hasMargin ? mStateDrawableBottom : mStateDrawableCenter;
                    } else {
                        background = hasMargin ? mStateDrawableSingle : mStateDrawableTop;
                    }
                } else {
                    if (hasTitle || sizeHeader > 0) {
                        if (i >= 0 && i < size - 1) {
                            background = mStateDrawableCenter;
                        } else {
                            background = hasMargin ? mStateDrawableBottom : mStateDrawableCenter;
                        }
                    } else {
                        if (i == 0) {
                            background = mStateDrawableTop;
                        } else if (i < size - 1) {
                            background = mStateDrawableCenter;
                        } else {
                            background = hasMargin ? mStateDrawableBottom : mStateDrawableCenter;
                        }
                    }
                }
                background = DrawableUtil.getNewDrawable(background);
                setViewBackground(convertView, background);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mItemsClickDismissEnable) {
                            mDialog.dismiss();
                        }
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(mDialog, view, i);
                        }
                    }
                });
                return convertView;
            }
        }
    }

    /**
     * 网格布局
     */
    public static class GridBuilder extends Builder<GridBuilder> {

        private GridView mGvContainer;
        private Drawable mGridBackground;
        private int mGridPadding;
        private int mNumColumns = GridView.AUTO_FIT;
        private int mStretchMode = GridView.STRETCH_COLUMN_WIDTH;
        private int mHorizontalSpacing;
        private int mVerticalSpacing;
        private boolean mItemsClickBackgroundEnable;

        public GridBuilder(Context context) {
            super(context);
            setNumColumns(3)
                    .setGridBackgroundColor(Color.WHITE)
                    .setGridPadding(dp2px(12))
                    .setItemsTextSize(12)
                    .setItemsSingleDrawableResource(R.color.colorActionSheetEdge)
                    .setItemsSinglePressedDrawableResource(R.color.colorActionSheetEdgePressed);
        }

        /**
         * 设置Grid 背景drawable
         *
         * @param drawable
         * @return
         */
        public GridBuilder setGridBackground(Drawable drawable) {
            mGridBackground = drawable;
            return this;
        }

        /**
         * 设置Grid背景颜色
         *
         * @param color
         * @return
         */
        public GridBuilder setGridBackgroundColor(int color) {
            return setGridBackground(new ColorDrawable(color));
        }

        /**
         * 设置Grid 背景资源
         *
         * @param res
         * @return
         */
        public GridBuilder setGridBackgroundResource(int res) {
            return setGridBackground(mResourceUtil.getDrawable(res));
        }

        /**
         * 设置是否item点击/按下整个item背景效果--默认为透明度变化
         *
         * @param enable
         * @return
         */
        public GridBuilder setItemsClickBackgroundEnable(boolean enable) {
            this.mItemsClickBackgroundEnable = enable;
            return this;
        }

        /**
         * 设置Grid padding
         *
         * @param padding
         * @return
         */
        public GridBuilder setGridPadding(int padding) {
            mGridPadding = padding;
            return this;
        }

        /**
         * {@link GridView#setHorizontalSpacing(int)}
         *
         * @param spacing
         * @return
         */
        public GridBuilder setHorizontalSpacing(int spacing) {
            this.mHorizontalSpacing = spacing;
            return this;
        }

        /**
         * {@link GridView#setVerticalSpacing(int)}
         *
         * @param spacing
         * @return
         */
        public GridBuilder setVerticalSpacing(int spacing) {
            this.mVerticalSpacing = spacing;
            return this;
        }

        /**
         * {@link GridView#setNumColumns(int)} (int)}
         *
         * @param numColumns
         * @return
         */
        public GridBuilder setNumColumns(int numColumns) {
            this.mNumColumns = numColumns;
            return this;
        }

        /**
         * {@link GridView#setStretchMode(int)}
         *
         * @param mode
         * @return
         */
        public GridBuilder setStretchMode(int mode) {
            this.mStretchMode = mode;
            return this;
        }

        @Override
        public View createItemView() {
            mGvContainer = new GridView(mContext);
            mGvContainer.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.0f));
            mLLayoutRoot.addView(mGvContainer);

            //设置GridView相关属性
            mGvContainer.setId(R.id.gv_containerActionSheetDialog);
            mGvContainer.setVerticalScrollBarEnabled(false);
            mGvContainer.setOverScrollMode(View.OVER_SCROLL_NEVER);
            mGvContainer.setNumColumns(mNumColumns);
            mGvContainer.setStretchMode(mStretchMode);
            mGvContainer.setHorizontalSpacing(mHorizontalSpacing);
            mGvContainer.setVerticalSpacing(mVerticalSpacing);
            mGvContainer.setAdapter(mAdapter);
            mGvContainer.setPadding(mGridPadding, mGridPadding, mGridPadding, mGridPadding);
            setViewBackground(mGvContainer, mGridBackground);
            setViewBackground(mTvTitle, mGridBackground);
            if (mItemsClickBackgroundEnable) {
                mGvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if (mItemsClickDismissEnable) {
                            mDialog.dismiss();
                        }
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onClick(mDialog, view, position);
                        }
                    }
                });
            }
            return mGvContainer;
        }

        @Override
        public ListAdapter getDefaultAdapter() {
            return new GridAdapter();
        }

        private class GridAdapter extends SheetAdapter {
            @Override
            public View getView(final int i, View convertView, ViewGroup parent) {
                final SheetItem data = getItem(i);
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    AlphaTextView txt = new AlphaTextView(mContext);
                    txt.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    convertView = txt;
                    holder.textView = txt;
                    convertView.setTag(holder);
                } else {
                    holder = (ViewHolder) convertView.getTag();
                }
                setTextView(holder, data, i);
                DrawableUtil.setDrawableWidthHeight(data.drawable, mItemsImageWidth, mItemsImageHeight);
                holder.textView.setCompoundDrawables(null, data.drawable, null, null);
                holder.textView.setPadding(mItemsTextPaddingLeft, mItemsTextPaddingTop, mItemsTextPaddingRight, mItemsTextPaddingBottom);
                if (!mItemsClickBackgroundEnable) {
                    convertView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (mItemsClickDismissEnable) {
                                mDialog.dismiss();
                            }
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onClick(mDialog, view, i);
                            }
                        }
                    });
                }
                return convertView;
            }
        }
    }

    private static abstract class Builder<T extends Builder> extends BasisBuilder<T> implements ICreateContentView {

        protected StateListDrawable mStateDrawableTop;
        protected StateListDrawable mStateDrawableCenter;
        protected StateListDrawable mStateDrawableBottom;
        protected StateListDrawable mStateDrawableSingle;

        protected Drawable mTopDrawable;
        protected Drawable mTopPressedDrawable;
        protected Drawable mCenterDrawable;
        protected Drawable mCenterPressedDrawable;
        protected Drawable mBottomDrawable;
        protected Drawable mBottomPressedDrawable;
        protected Drawable mSingleDrawable;
        protected Drawable mSinglePressedDrawable;

        protected boolean mDragEnable = false;
        protected int mMarginTop;
        protected List<SheetItem> mListItem;

        protected TextView mTvTitle;
        protected CharSequence mTitleStr;
        protected ColorStateList mTitleTextColor;
        protected int mTitleGravity = Gravity.CENTER;
        protected float mTitleTextSize = 16;

        protected TextView mTvCancel;
        protected CharSequence mCancelStr;
        protected ColorStateList mCancelTextColor;
        protected int mCancelGravity = Gravity.CENTER;
        protected float mCancelTextSize = 16;
        protected int mCancelMarginTop;

        protected Map<Integer, ColorStateList> mMapItemColor;
        protected ListAdapter mAdapter;

        protected OnItemClickListener mOnItemClickListener;

        protected int mTextDrawablePadding;
        protected float mItemsTextSize = 16;
        protected int mItemsMinHeight = 45;
        protected ColorStateList mItemsTextColor;
        protected int mItemsGravity = Gravity.CENTER;
        protected int mItemsTextGravity = Gravity.CENTER;
        protected int mItemsImageWidth = -1;
        protected int mItemsImageHeight = -1;
        protected int mItemsTextPaddingLeft;
        protected int mItemsTextPaddingTop;
        protected int mItemsTextPaddingRight;
        protected int mItemsTextPaddingBottom;
        protected boolean mItemsClickDismissEnable = true;

        public Builder(Context context) {
            super(context);
            Log.i("height", "height:" + getScreenHeight());
            setBackgroundResource(R.color.colorActionSheetNormalBackground)
                    .setItemsSingleDrawableResource(R.color.colorActionSheetEdge)
                    .setItemsSinglePressedDrawableResource(R.color.colorActionSheetEdgePressed)
                    .setMarginTop((int) (getScreenHeight() * 0.3))
                    .setTextPadding(dp2px(16), dp2px(10), dp2px(16), dp2px(10))
                    .setTitleTextColorResource(R.color.colorActionSheetTitleText)
                    .setCancelTextColorResource(R.color.colorActionSheetWeiXinText)
                    .setItemsTextColorResource(R.color.colorActionSheetWeiXinText)
                    .setItemsMinHeight(dp2px(45))
                    .setTextDrawablePadding(dp2px(12))
                    .setPadding(0);
        }

        /**
         * 是否开启手指拖拽
         *
         * @param enable
         * @return
         */
        public T setDragEnable(boolean enable) {
            this.mDragEnable = enable;
            return backBuilder();
        }

        /**
         * 设置ContentView距离顶部间距
         *
         * @param top
         * @return
         */
        public T setMarginTop(int top) {
            mMarginTop = top;
            return backBuilder();
        }

        /**
         * 设置文本与img 间距
         *
         * @param padding
         * @return
         */
        public T setTextDrawablePadding(int padding) {
            this.mTextDrawablePadding = padding;
            return backBuilder();
        }

        /**
         * 设置Item、标题、取消文本间距
         *
         * @param left
         * @param top
         * @param right
         * @param bottom
         * @return
         */
        public T setTextPadding(int left, int top, int right, int bottom) {
            this.mItemsTextPaddingLeft = left;
            this.mItemsTextPaddingTop = top;
            this.mItemsTextPaddingRight = right;
            this.mItemsTextPaddingBottom = bottom;
            return backBuilder();
        }

        /**
         * 设置标题
         * {@link TextView#setText(CharSequence)}
         *
         * @param charSequence
         * @return
         */
        public T setTitle(CharSequence charSequence) {
            this.mTitleStr = charSequence;
            return backBuilder();
        }

        public T setTitle(int resId) {
            return setTitle(mResourceUtil.getText(resId));
        }

        /**
         * 设置标题文字颜色
         * {@link TextView#setTextColor(ColorStateList)}
         *
         * @param color
         * @return
         */
        public T setTitleTextColor(ColorStateList color) {
            this.mTitleTextColor = color;
            return backBuilder();
        }

        public T setTitleTextColor(int color) {
            return setTitleTextColor(ColorStateList.valueOf(color));
        }

        public T setTitleTextColorResource(int resId) {
            return setTitleTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置标题文本尺寸
         * {@link TextView#setTextSize(int, float)}
         * {@link Builder#setTextSizeUnit(int)}
         *
         * @param size
         * @return
         */
        public T setTitleTextSize(float size) {
            this.mTitleTextSize = size;
            return backBuilder();
        }

        /**
         * 设置标题文本对齐方式
         * {@link TextView#setGravity(int)}
         *
         * @param gravity
         * @return
         */
        public T setTitleGravity(int gravity) {
            this.mTitleGravity = gravity;
            return backBuilder();
        }

        /**
         * 设置取消按钮文本
         *
         * @param charSequence
         * @return
         */
        public T setCancel(CharSequence charSequence) {
            this.mCancelStr = charSequence;
            return backBuilder();
        }

        public T setCancel(int resId) {
            return setCancel(mResourceUtil.getText(resId));
        }

        /**
         * 设置取消文本颜色
         *
         * @param color
         * @return
         */
        public T setCancelTextColor(ColorStateList color) {
            this.mCancelTextColor = color;
            return backBuilder();
        }

        public T setCancelTextColor(int color) {
            return setCancelTextColor(ColorStateList.valueOf(color));
        }

        public T setCancelTextColorResource(int resId) {
            return setCancelTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置取消文本尺寸
         *
         * @param size
         * @return
         */
        public T setCancelTextSize(float size) {
            this.mCancelTextSize = size;
            return backBuilder();
        }

        /**
         * 设置取消文本对齐方式
         *
         * @param gravity
         * @return
         */
        public T setCancelGravity(int gravity) {
            this.mCancelGravity = gravity;
            return backBuilder();
        }

        /**
         * 设置取消文本与Item间距
         *
         * @param margin
         * @return
         */
        public T setCancelMarginTop(int margin) {
            this.mCancelMarginTop = margin;
            return backBuilder();
        }

        /**
         * 设置Item 适配器
         *
         * @param adapter
         * @return
         */
        public T setAdapter(ListAdapter adapter) {
            this.mAdapter = adapter;
            return backBuilder();
        }


        /**
         * 设置单个Item默认状态背景Drawable
         *
         * @param drawable
         * @return
         */
        public T setItemsSingleDrawable(Drawable drawable) {
            mSingleDrawable = drawable;
            return backBuilder();
        }

        public T setItemsSingleDrawableResource(int res) {
            return setItemsSingleDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsSingleDrawableColor(int color) {
            return setItemsSingleDrawable(new ColorDrawable(color));
        }

        /**
         * 设置单个Item按下状态背景Drawable
         *
         * @param drawable
         * @return
         */
        public T setItemsSinglePressedDrawable(Drawable drawable) {
            mSinglePressedDrawable = drawable;
            return backBuilder();
        }

        public T setItemsSinglePressedDrawableResource(int res) {
            return setItemsSinglePressedDrawable(mResourceUtil.getDrawable(res));
        }

        public T setItemsSinglePressedDrawableColor(int color) {
            return setItemsSinglePressedDrawable(new ColorDrawable(color));
        }

        /**
         * 设置Item的最小高度
         *
         * @param height
         * @return
         */
        public T setItemsMinHeight(int height) {
            this.mItemsMinHeight = height;
            return backBuilder();
        }

        /**
         * 设置item 文本颜色-设置全部文本默认颜色
         * 设置单个文本颜色参考
         * {@link Builder#setItemTextColor(int, ColorStateList)}
         * {@link Builder#setItemTextColor(int, int)}
         * {@link Builder#setItemTextColorResource(int, int)}
         *
         * @param color
         * @return
         */
        public T setItemsTextColor(ColorStateList color) {
            this.mItemsTextColor = color;
            return backBuilder();
        }

        public T setItemsTextColor(int color) {
            return setItemsTextColor(ColorStateList.valueOf(color));
        }

        public T setItemsTextColorResource(int resId) {
            return setItemsTextColor(mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置某个item文本颜色
         *
         * @param position
         * @param color
         * @return
         */
        public T setItemTextColor(int position, ColorStateList color) {
            if (mMapItemColor == null) {
                mMapItemColor = new HashMap<>();
            }
            mMapItemColor.put(position, color);
            return backBuilder();
        }

        public T setItemTextColor(int position, int color) {
            return setItemTextColor(position, ColorStateList.valueOf(color));
        }

        public T setItemTextColorResource(int position, int resId) {
            return setItemTextColor(position, mResourceUtil.getColorStateList(resId));
        }

        /**
         * 设置所有item文本尺寸
         *
         * @param size
         * @return
         */
        public T setItemsTextSize(float size) {
            this.mItemsTextSize = size;
            return backBuilder();
        }

        /**
         * 设置所有Item 对齐方式
         * {@link LinearLayout#setGravity(int)}
         *
         * @param gravity
         * @return
         */
        public T setItemsGravity(int gravity) {
            this.mItemsGravity = gravity;
            return backBuilder();
        }

        /**
         * 设置所有Item对齐方式
         * {@link TextView#setGravity(int)}
         *
         * @param gravity
         * @return
         */
        public T setItemsTextGravity(int gravity) {
            this.mItemsTextGravity = gravity;
            return backBuilder();
        }

        /**
         * 设置Item 图标宽度默认原始宽度
         *
         * @param width
         * @return
         */
        public T setItemsImageWidth(int width) {
            mItemsImageWidth = width;
            return backBuilder();
        }

        /**
         * 设置Item图标高度默认原始高度
         *
         * @param height
         * @return
         */
        public T setItemsImageHeight(int height) {
            mItemsImageHeight = height;
            return backBuilder();
        }

        /**
         * 设置点击Item是否dialog关闭
         *
         * @param enable
         * @return
         */
        public T setItemsClickDismissEnable(boolean enable) {
            this.mItemsClickDismissEnable = enable;
            return backBuilder();
        }

        /**
         * 添加一个Item
         *
         * @param item
         * @return
         */
        public T addItem(SheetItem item) {
            if (item != null) {
                if (mListItem == null) {
                    mListItem = new ArrayList<>();
                }
                mListItem.add(item);
            }
            return backBuilder();
        }

        /**
         * 添加一个纯文本item
         *
         * @param txt
         * @return
         */
        public T addItem(CharSequence txt) {
            return addItem(new SheetItem(txt));
        }

        public T addItem(int txt) {
            return addItem(new SheetItem(mResourceUtil.getText(txt)));
        }

        public T addItem(int txt, int resDrawable) {
            return addItem(new SheetItem(mResourceUtil.getText(txt))
                    .setImageDrawable(mResourceUtil.getDrawable(resDrawable)));
        }

        public T addItem(CharSequence txt, int resDrawable) {
            return addItem(new SheetItem(txt).setImageDrawable(mResourceUtil.getDrawable(resDrawable)));
        }

        /**
         * 添加item数组
         *
         * @param list
         * @return
         */
        public T addItems(ArrayList<SheetItem> list) {
            if (list != null && list.size() > 0) {
                if (mListItem == null) {
                    mListItem = new ArrayList<>();
                }
                mListItem.addAll(list);
            }
            return backBuilder();
        }

        /**
         * 添加纯文本Item list数组
         *
         * @param items
         * @return
         */
        public T addItems(List<CharSequence> items) {
            if (items != null && items.size() > 0) {
                if (mListItem == null) {
                    mListItem = new ArrayList<>();
                }
                for (CharSequence item : items) {
                    addItem(item);
                }
            }
            return backBuilder();
        }

        /**
         * 添加纯文本Item []数组
         *
         * @param items
         * @return
         */
        public T addItems(CharSequence[] items) {
            return addItems(Arrays.asList(items));
        }

        /**
         * 添加一个array资源item
         *
         * @param itemsRes
         * @return
         */
        public T addItems(int itemsRes) {
            return addItems(mResourceUtil.getTextArray(itemsRes));
        }

        /**
         * 设置item点击事件监听
         *
         * @param listener
         * @return
         */
        public T setOnItemClickListener(OnItemClickListener listener) {
            this.mOnItemClickListener = listener;
            return backBuilder();
        }

        /**
         * 创建dialog
         *
         * @return
         */
        public UIActionSheetDialog create() {
            mDialog = new UIActionSheetDialog(mContext);
            View contentView = createContentView();
            mDialog.setContentView(contentView);
            setDialog();
            mDialog.setGravity(Gravity.BOTTOM);
            mDialog.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
            mDialog.setMargin(0, mMarginTop, 0, 0);
            afterSetContentView(contentView);
            return (UIActionSheetDialog) mDialog;
        }

        View mViewItem;

        /**
         * 创建dialog ContentView
         *
         * @return
         */
        private View createContentView() {
            createDrawable();
            DragLayout dragLayout = new DragLayout(mContext);
            mLLayoutRoot = new LinearLayout(mContext);
            mLLayoutRoot.setId(R.id.lLayout_rootActionSheetDialog);
            mLLayoutRoot.setOrientation(LinearLayout.VERTICAL);
            mLLayoutRoot.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mAdapter = mAdapter == null ? getDefaultAdapter() : mAdapter;
            dragLayout.addView(mLLayoutRoot);
            dragLayout.setDragEnable(mDragEnable);
            dragLayout.setOnDragListener(new DragLayout.OnDragListener() {
                @Override
                public void onClosed() {
                    mDialog.dismiss();
                }

                @Override
                public void onOpened() {

                }
            });
            setRootView();
            createTitle();
            mViewItem = createItemView();
            createCancel();
            return dragLayout;
        }

        /**
         * 创建Title
         */
        private void createTitle() {
            if (TextUtils.isEmpty(mTitleStr)) {
                return;
            }
            mTvTitle = new TextView(mContext);
            mTvTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvTitle.setMinimumHeight(dp2px(20));
            mTvTitle.setId(R.id.tv_titleActionSheetDialog);
            mLLayoutRoot.addView(mTvTitle);

            Drawable background = mStateDrawableSingle.getCurrent();
            mTvTitle.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
            mTvTitle.setGravity(mTitleGravity);
            mTvTitle.setPadding(mItemsTextPaddingLeft, mItemsTextPaddingTop, mItemsTextPaddingRight, mItemsTextPaddingBottom);
            mTvTitle.setCompoundDrawablePadding(mTextDrawablePadding);
            mTvTitle.setText(mTitleStr);
            mTvTitle.setTextSize(mTextSizeUnit, mTitleTextSize);
            mTvTitle.setTextColor(mTitleTextColor);

            if ((mListItem != null && mListItem.size() > 0) ||
                    (!TextUtils.isEmpty(mCancelStr) && mCancelMarginTop <= 0)) {
                background = mStateDrawableTop.getCurrent();
            }
            setViewBackground(mTvTitle, background);
            setTextViewLine(mTvTitle);
        }

        /**
         * 创建取消按钮
         */
        private void createCancel() {
            if (TextUtils.isEmpty(mCancelStr)) {
                return;
            }
            mTvCancel = new TextView(mContext);
            mTvCancel.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            mTvCancel.setMinimumHeight(mItemsMinHeight);
            mTvCancel.setId(R.id.tv_cancelActionSheetDialog);
            mLLayoutRoot.addView(mTvCancel);

            mTvCancel.setLineSpacing(mLineSpacingExtra, mLineSpacingMultiplier);
            mTvCancel.setGravity(mCancelGravity);
            mTvCancel.setCompoundDrawablePadding(mTextDrawablePadding);
            mTvCancel.setPadding(mItemsTextPaddingLeft, mItemsTextPaddingTop, mItemsTextPaddingRight, mItemsTextPaddingBottom);
            mTvCancel.setText(mCancelStr);
            mTvCancel.setTextSize(mTextSizeUnit, mCancelTextSize);
            mTvCancel.setTextColor(mCancelTextColor);
            if (mCancelMarginTop > 0) {
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTvCancel.getLayoutParams();
                if (lp != null) {
                    lp.topMargin = mCancelMarginTop;
                    mTvCancel.setLayoutParams(lp);
                }
            }
            boolean single = mCancelMarginTop > 0 || TextUtils.isEmpty(mTitleStr) && mViewItem == null || mBottomDrawable == null;
            setViewBackground(mTvCancel, single ? mStateDrawableSingle : mStateDrawableBottom);

            mTvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
            setTextViewLine(mTvCancel);
        }

        /**
         * 创建Item Drawable
         */
        private void createDrawable() {
            mStateDrawableTop = new StateListDrawable();
            mStateDrawableTop.addState(new int[]{mStatePressed}, mTopPressedDrawable);
            mStateDrawableTop.addState(new int[]{}, mTopDrawable);

            mStateDrawableCenter = new StateListDrawable();
            mStateDrawableCenter.addState(new int[]{mStatePressed}, mCenterPressedDrawable);
            mStateDrawableCenter.addState(new int[]{}, mCenterDrawable);

            mStateDrawableBottom = new StateListDrawable();
            mStateDrawableBottom.addState(new int[]{mStatePressed}, mBottomPressedDrawable);
            mStateDrawableBottom.addState(new int[]{}, mBottomDrawable);

            mStateDrawableSingle = new StateListDrawable();
            mStateDrawableSingle.addState(new int[]{mStatePressed}, mSinglePressedDrawable);
            mStateDrawableSingle.addState(new int[]{}, mSingleDrawable);

        }

        protected abstract class SheetAdapter extends BaseAdapter {
            @Override
            public int getCount() {
                return mListItem == null ? 0 : mListItem.size();
            }

            @Override
            public SheetItem getItem(int position) {
                return mListItem == null ? null : mListItem.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            protected void setTextView(final ViewHolder holder, SheetItem data, final int position) {
                if (holder == null || holder.textView == null) {
                    return;
                }
                holder.textView.setCompoundDrawablePadding(mTextDrawablePadding);
                setTextAttribute(holder.textView, data.text,
                        getTextColor(position, data.textColor), mItemsTextSize, mItemsGravity, false);
                setTextViewLine(holder.textView);
            }
        }

        protected ColorStateList getTextColor(int position, ColorStateList colorStateList) {
            ColorStateList result = colorStateList;
            if (mMapItemColor != null && mMapItemColor.containsKey(position)) {
                result = mMapItemColor.get(position);
            }
            result = result != null ? result : mItemsTextColor;
            return result;
        }

        protected static class ViewHolder {
            ImageView imageView;
            TextView textView;
        }
    }
}
