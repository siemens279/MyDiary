package com.a279.siemens.mydiary;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

//@SuppressLint("AppCompatCustomView")
public class MyEditText extends android.support.v7.widget.AppCompatEditText{

    Paint p;
    int w = 0;
    int h = 0;
    int hie = 0;
    float tSize = 0;

    public MyEditText(Context context) {
        super(context);
        //setTextSize(30);
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
        //do stuff that was in your original constructor...
        tSize = getTextSize();
        p=new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        w = widthMeasureSpec;
        h = heightMeasureSpec;
        //Log.d("MyLog", ""+w+"="+h);
    }

    @Override
    public float getTextSize() {
        float f = super.getTextSize();
        //Log.d("MyLog", "1:"+String.valueOf(f));
        return f;
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
        //Log.d("MyLog", "22222222222");
        super.onDraw(canvas);
//        p.setStrokeWidth(10);
//        p.setColor(Color.parseColor("#fff000"));
//        canvas.drawLine(100,100,300,300,p);

        //if ()
        Log.d("MyLog", "--------------------");
        Log.d("MyLog", "height:"+getLineHeight());
        Log.d("MyLog", "count:"+getLineCount());

        Paint mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#808080"));//Color.BLACK);
        mPaint.setStrokeWidth(1);

        //mPaint.setTextSize(10);


        //canvas.drawLine(0, 50, w, 50, mPaint);
        for (int i=42;i<h/42;i=i+42) {
            canvas.drawLine(0, i, w, i, mPaint);
//            canvas.drawLine(0, 30, w, 30, mPaint);
//            canvas.drawLine(0, 45, w, 45, mPaint);
//            canvas.drawLine(0, 60, w, 60, mPaint);
        }

//        Paint mPaint2 = new Paint();
//        mPaint2.setColor(Color.YELLOW);
//        mPaint2.setStyle(Paint.Style.FILL);
//        canvas.drawPaint(mPaint2);

    }

}
