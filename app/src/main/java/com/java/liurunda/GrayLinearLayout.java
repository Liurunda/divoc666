package com.java.liurunda;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GrayLinearLayout extends LinearLayout {
    private final Paint mPaint = new Paint();

    public GrayLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setSaturation(float sat) {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(sat);
        mPaint.setColorFilter(new ColorMatrixColorFilter(cm));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(null, mPaint, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        canvas.restore();
    }
}
