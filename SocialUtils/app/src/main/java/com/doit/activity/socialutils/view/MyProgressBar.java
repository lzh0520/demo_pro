package com.doit.activity.socialutils.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.doit.activity.socialutils.R;

public class MyProgressBar extends View {
    private int mInnerBackground = Color.BLUE;
    private int mOuterBackground = Color.RED;

    private int mRoundWidth;

    private float mScoreTextSize = 20;
    private float mProgressTextSize = 12;


    private int mProgressTextColor = getResources().getColor(R.color.food_red);

    private Paint mInnerPaint, mOuterPaint, mTextPaint1, mTextPaint2;

    private double mMax = 100;
    private double mProgress = 0;

    private String mProText;
    private String mProContent;

    private int mFoodColor;
    private int mFoodColor1;

    private int mFoodSize;
    private int mFoodSize1;


    public MyProgressBar(Context context) {
        this(context, null);
    }

    public MyProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ProgressBar);
        mInnerBackground = array.getColor(R.styleable.ProgressBar_innerBackground, mInnerBackground);
        mOuterBackground = array.getColor(R.styleable.ProgressBar_outerBackground, mOuterBackground);
        mRoundWidth = (int) array.getDimension(R.styleable.ProgressBar_roundWidth, dip2px(5));
        mScoreTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize,
                sp2px(mScoreTextSize));
        mProgressTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize,
                sp2px(mProgressTextSize));
        mProgressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor, mProgressTextColor);

        array.recycle();

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerBackground);
        mInnerPaint.setStrokeWidth(mRoundWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);

        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterBackground);
        mOuterPaint.setStrokeWidth(mRoundWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);

        mTextPaint1 = new Paint();
        mTextPaint1.setTextSize(mScoreTextSize);
        mTextPaint1.setAntiAlias(true);


        mTextPaint2 = new Paint();
        mTextPaint2.setAntiAlias(true);
        mTextPaint2.setTextSize(mProgressTextSize);


    }

    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

    private float dip2px(int dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 只保证是正方形
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width, height), Math.min(width, height));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 先画内圆
        int radius = getWidth() / 2;
        canvas.drawCircle(radius, radius, radius - mRoundWidth / 2, mInnerPaint);
        //画圆弧
        @SuppressLint("DrawAllocation") RectF rectF = new RectF(mRoundWidth / 2, mRoundWidth / 2,
                getWidth() - mRoundWidth / 2, getHeight() - mRoundWidth / 2);
        //如果进度为0就不绘制
        if (mProgress == 0) {
            return;
        }
        double percent = (float) mProgress / mMax;
        canvas.drawArc(rectF, 0, (float) (360 * percent), false, mOuterPaint);

        // 画进度文字
//        String text = ((int) (percent * 100)) + "%";
        Rect rect = new Rect();
        mTextPaint1.getTextBounds(mProContent, 0, mProContent.length(), rect);
        float dx = (getWidth() >> 1) - (rect.width() >> 1);
        Paint.FontMetricsInt fontMetricsInt = new Paint.FontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        float baseLine = (getHeight() >> 1) + dy;
        canvas.drawText(mProContent, dx, baseLine, mTextPaint1);


        Rect rect1 = new Rect();
        mTextPaint2.getTextBounds(mProText, 0, mProText.length(), rect1);
        float dx1 = (getWidth() >> 1) - (rect1.width() >> 1);
        Paint.FontMetricsInt fontMetricsInt1 = new Paint.FontMetricsInt();
        int dy1 = (fontMetricsInt1.bottom - fontMetricsInt1.top) / 2;
        canvas.drawText(mProText, dx1, baseLine + 65, mTextPaint2);


    }

    public void setFoodText(String str, String content) {
        this.mProText = str;
        this.mProContent = content;
    }

    public void setFoodColor(int color, int color1) {
        this.mFoodColor = color;
        this.mFoodColor1 = color1;
        mTextPaint1.setColor(mFoodColor);
        mTextPaint2.setColor(mFoodColor1);
    }

    public void setFoodSize(int size, int size1) {
        this.mFoodSize = size;
        this.mFoodSize1 = size1;

//        mTextPaint1.setTextSize(mFoodSize);
//        mTextPaint2.setTextSize(mFoodSize1);
    }

    // 给几个方法
    public synchronized void setMax(double max) {
        if (max < 0) {

        }
        this.mMax = max;
    }

    public synchronized void setProgress(double progress) {
        if (progress < 0) {
        }
        this.mProgress = progress;
        // 刷新 invalidate
        invalidate();
    }
}