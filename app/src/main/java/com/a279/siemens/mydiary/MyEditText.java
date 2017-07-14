package com.a279.siemens.mydiary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

public class MyEditText extends android.support.v7.widget.AppCompatEditText{

    Paint mPaint;

    public MyEditText(Context context) {
        super(context);
        init(context);
    }
    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public MyEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
}
    private void init(Context context) {
        //tSize = getTextSize();
        mPaint = new Paint();
    }

    @Override
    public float getTextSize() {
        return super.getTextSize();
    }
    @Override
    public int getLineHeight() {
        return super.getLineHeight();
    }
    @Override
    public int getLineCount() {
        return super.getLineCount();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#808080"));
        mPaint.setStrokeWidth(1);
        //mPaint.setTextSize(10);

        int height = getHeight();
        int width = getWidth();
        for (int i=42;i<height;i=i+42) {
            canvas.drawLine(0, i, width, i, mPaint);
            //Log.d("MyLog", "i:"+i);
        }

    }

}
