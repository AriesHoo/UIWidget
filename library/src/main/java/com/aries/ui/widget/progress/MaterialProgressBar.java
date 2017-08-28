package com.aries.ui.widget.progress;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.aries.ui.widget.R;


/**
 * Created: AriesHoo on 2017/8/27 13:21
 * Function: 自定义Material Design 风格ProgressBar
 * Desc:
 */
public class MaterialProgressBar extends View {

    private static float DEFAULT_MAX_ANGLE = -305f;
    private static float DEFAULT_MIN_ANGLE = -0f;

    private AnimatorSet animatorSet;
    private Paint arcPaint;
    private RectF arcRect;
    private float startAngle = -45f;
    private float sweepAngle = -15f;
    private float incrementAngele = 0;
    private int size;

    private int arcColor = Color.BLUE;
    private float borderWidth = 3;
    private int duration = 600;//最小300(600与5.0以上接近),最大建议不超过1000

    public MaterialProgressBar(Context context) {
        this(context, null, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaterialProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs, defStyleAttr);
    }

    private void initAttribute(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaterialProgressBar, defStyle, 0);
        arcColor = typedArray.getColor(R.styleable.MaterialProgressBar_progress_arcColor, Color.BLUE);
        borderWidth = typedArray.getDimensionPixelSize(R.styleable.MaterialProgressBar_progress_borderWidth, dip2px(context, 3));
        duration = typedArray.getInt(R.styleable.MaterialProgressBar_progress_duration, duration);
        typedArray.recycle();
        init();
        setViewAttributes();
    }

    private void init() {
        arcPaint = new Paint();
        arcPaint.setColor(arcColor);
        arcPaint.setStrokeWidth(borderWidth);
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);
        arcRect = new RectF();
    }

    private void setViewAttributes() {
        setArcColor(arcColor);
        setBorderWidth(borderWidth);
        setDuration(duration);
    }

    public void setArcColor(int color) {
        this.arcColor = color;
        arcPaint.setColor(arcColor);
    }

    public void setBorderWidth(float width) {
        this.borderWidth = width;
        arcPaint.setStrokeWidth(borderWidth);
    }

    public void setDuration(int duration) {
        if (duration < 300) {
            duration = 300;
        }
        this.duration = duration;
    }


    private void startBound() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        arcRect.set(paddingLeft + borderWidth, paddingTop + borderWidth, size - paddingLeft - borderWidth, size - paddingTop - borderWidth);
    }

    private void startAnimation() {
        if (animatorSet != null && animatorSet.isRunning()) {
            animatorSet.cancel();
        }
        animatorSet = new AnimatorSet();
        AnimatorSet set = getAnimator();
        animatorSet.play(set);
        animatorSet.addListener(new AnimatorListener() {
            private boolean isCancel = false;

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCancel) {
                    startAnimation();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCancel = true;
            }
        });
        animatorSet.start();
    }

    /**
     * 循环的动画
     */
    private AnimatorSet getAnimator() {
        //从小圈到大圈
        ValueAnimator holdAnimator1 = ValueAnimator.ofFloat(incrementAngele + DEFAULT_MIN_ANGLE, incrementAngele + 115f);
        holdAnimator1.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                incrementAngele = (float) animation.getAnimatedValue();
            }
        });
        holdAnimator1.setDuration(duration);
        holdAnimator1.setInterpolator(new LinearInterpolator());


        ValueAnimator expandAnimator = ValueAnimator.ofFloat(DEFAULT_MIN_ANGLE, DEFAULT_MAX_ANGLE);
        expandAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (float) animation.getAnimatedValue();
                incrementAngele -= sweepAngle;
                invalidate();
            }
        });
        expandAnimator.setDuration(duration);
        expandAnimator.setInterpolator(new DecelerateInterpolator(2));


        //从大圈到小圈
        ValueAnimator holdAnimator = ValueAnimator.ofFloat(startAngle, startAngle + 115f);
        holdAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                startAngle = (float) animation.getAnimatedValue();
            }
        });

        holdAnimator.setDuration(duration);
        holdAnimator.setInterpolator(new LinearInterpolator());

        ValueAnimator narrowAnimator = ValueAnimator.ofFloat(DEFAULT_MAX_ANGLE, DEFAULT_MIN_ANGLE);
        narrowAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                sweepAngle = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        narrowAnimator.setDuration(duration);
        narrowAnimator.setInterpolator(new DecelerateInterpolator(2));

        AnimatorSet set = new AnimatorSet();
        set.play(holdAnimator1).with(expandAnimator);
        set.play(holdAnimator).with(narrowAnimator).after(holdAnimator1);
        return set;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(arcRect, startAngle + incrementAngele, sweepAngle, false, arcPaint);
        if (animatorSet == null || !animatorSet.isRunning()) {
            startAnimation();
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        size = (w < h) ? w : h;
        startBound();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
    }

}
