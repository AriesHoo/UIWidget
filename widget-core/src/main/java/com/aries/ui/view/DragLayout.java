package com.aries.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import static android.R.attr.height;

/**
 * @Author: AriesHoo on 2018/7/19 10:31
 * @E-Mail: AriesHoo@126.com
 * Function: 通过ViewDragHelper实现拖拽效果
 * Description:
 */
public class DragLayout extends FrameLayout {

    private static final int INVALID_POINTER = -1;
    private final float mDensity;
    boolean mDragEnable = true;
    private ViewDragHelper mDragHelper;
    private OnDragListener mListener;
    private int mHeight;
    private int mTop;
    private int mActivePointerId;
    private boolean mIsBeingDragged;
    private float mInitialMotionY;
    private boolean mCollapsible = false;
    private float yDiff;

    public DragLayout(Context context) {
        this(context, null);
    }

    public DragLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public DragLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mDragHelper = ViewDragHelper.create(this, 0.8f, new ViewDragCallback());
        mDensity = getResources().getDisplayMetrics().density * 400;
    }

    @Override
    public boolean onInterceptTouchEvent(@NonNull MotionEvent event) {
        final int action = MotionEventCompat.getActionMasked(event);

        if (!isEnabled()) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mActivePointerId = INVALID_POINTER;
            mIsBeingDragged = false;
            if (mCollapsible && -yDiff > mDragHelper.getTouchSlop()) {
                expand(mDragHelper.getCapturedView(), 0);
            }
            mDragHelper.cancel();
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mHeight = getChildAt(0).getHeight();
                mTop = getChildAt(0).getTop();
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mIsBeingDragged = false;
                final float initialMotionY = getMotionEventY(event, mActivePointerId);
                if (initialMotionY == -1) {
                    return false;
                }
                mInitialMotionY = initialMotionY;
                yDiff = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    return false;
                }
                final float y = getMotionEventY(event, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                yDiff = y - mInitialMotionY;
                if (mDragEnable && yDiff > mDragHelper.getTouchSlop() && !mIsBeingDragged) {
                    mIsBeingDragged = true;
                    mDragHelper.captureChildView(getChildAt(0), 0);
                }
                break;
        }
        mDragHelper.shouldInterceptTouchEvent(event);
        return mIsBeingDragged;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // Nope.
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!isEnabled()) {
            return super.onTouchEvent(ev);
        }
        try {
            if (mDragEnable) {
                mDragHelper.processTouchEvent(ev);
            }
        } catch (Exception ignored) {
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public DragLayout setOnDragListener(OnDragListener listener) {
        mListener = listener;
        return this;
    }

    public DragLayout setCollapsible(boolean mCollapsible) {
        this.mCollapsible = mCollapsible;
        return this;
    }

    public DragLayout setDragEnable(boolean enable) {
        this.mDragEnable = enable;
        return this;
    }

    private void expand(View releasedChild, float yvel) {
        if (mListener != null) {
            mListener.onOpened();
        }
    }

    private void dismiss(View view, float yvel) {
        mDragHelper.smoothSlideViewTo(view, 0, mTop + height);
        ViewCompat.postInvalidateOnAnimation(DragLayout.this);
    }

    /**
     * set listener
     */
    public interface OnDragListener {

        /**
         * Drag关闭回调
         */
        void onClosed();

        /**
         * Drag开启回调
         */
        void onOpened();
    }

    /**
     * Callback
     */
    private class ViewDragCallback extends ViewDragHelper.Callback {


        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            if (yvel > mDensity) {
                dismiss(releasedChild, yvel);
            } else {
                if (releasedChild.getTop() >= mTop + mHeight / 2) {
                    dismiss(releasedChild, yvel);
                } else {
                    mDragHelper.smoothSlideViewTo(releasedChild, 0, mTop);
                    ViewCompat.postInvalidateOnAnimation(DragLayout.this);
                }
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int mTop, int dx, int dy) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                invalidate();
            }
            if (mHeight - mTop < 1 && mListener != null) {
                mDragHelper.cancel();
                mListener.onClosed();
                mDragHelper.smoothSlideViewTo(changedView, 0, mTop);
            }
        }

        @Override
        public int clampViewPositionVertical(View child, int mTop, int dy) {
            return Math.max(mTop, DragLayout.this.mTop);
        }
    }

}