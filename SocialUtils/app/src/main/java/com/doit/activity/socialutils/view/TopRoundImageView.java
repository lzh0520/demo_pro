package com.doit.activity.socialutils.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class TopRoundImageView extends AppCompatImageView {


    private Path mPath;
    private RectF mRectF;

    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
    private float[] rids = {dp2px(10), dp2px(10), dp2px(10), dp2px(10), 0.0f, 0.0f, 0.0f, 0.0f,};


    public TopRoundImageView(Context context) {
        this(context, null);
    }

    public TopRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPath = new Path();
        mRectF = new RectF();
    }

    private int dp2px(final float dpValue) {
        final float scale = this.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 画图
     *
     * @param canvas
     */
    protected void onDraw(Canvas canvas) {

        int w = this.getWidth();
        int h = this.getHeight();

        mRectF.set(0, 0, w, h);
        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);
        canvas.clipPath(mPath);
        super.onDraw(canvas);
    }

}
