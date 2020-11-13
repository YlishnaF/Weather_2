package com.example.weather;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class PointView extends View {
    private int pointColor = Color.WHITE;
    private RectF pointRectangle = new RectF();
    private RectF topPoint = new RectF();
    private Rect leftPoint = new Rect();
    private Rect rightPoint = new Rect();
    private Rect bottomPoint = new Rect();
    private Paint pointPaint;
    private Paint pointPaintTop;
    private Paint pointPaintBottom;
    private Paint pointPaintRight;
    private Paint pointPaintLeft;
    private int pointPressedColor = Color.WHITE;
    private Paint pointPressedPaint;
    private int width = 0;
    private int height = 0;
    private final static int padding = 7;
    private boolean pressed = false;
    private OnClickListener listener;

    public PointView(Context context) {
        super(context);
        init();
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
        init();
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init();
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PointView, 0,0);

        pointColor = typedArray.getColor(R.styleable.PointView_point_color, Color.WHITE);
        pointPressedColor = typedArray.getColor(R.styleable.PointView_point_pressed_color, Color.WHITE);

        typedArray.recycle();

    }

    private void init(){
        pointPaint = new Paint();
        pointPaint.setColor(pointColor);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setStrokeWidth(3);
        pointPaintTop= new Paint();
        pointPaintBottom = new Paint();
        pointPaintLeft = new Paint();
        pointPaintRight = new Paint();
        pointPaintRight.setColor(pointColor);
        pointPaintRight.setStyle(Paint.Style.FILL);
        pointPaintLeft.setColor(pointColor);
        pointPaintLeft.setStyle(Paint.Style.FILL);
        pointPaintBottom.setColor(pointColor);
        pointPaintBottom.setStyle(Paint.Style.FILL);
        pointPaintTop.setColor(pointColor);
        pointPaintTop.setStyle(Paint.Style.FILL);
        pointPressedPaint = new Paint();
        pointPressedPaint.setColor(pointPressedColor);
        pointPressedPaint.setStyle(Paint.Style.FILL);
  }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
      pointRectangle.set(padding,padding, width-padding, height-padding);
      topPoint.set((width)/2-2, 0, width/2 +2, padding);
      bottomPoint.set((width)/2-2, height-padding, width/2 +2, height);
      leftPoint.set(0,(height/2)-2, padding,(height/2)+2 );
      rightPoint.set(width-padding, (height/2)-2, width, (height/2)+2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(pressed){
            canvas.drawOval(pointRectangle, pointPressedPaint);
            canvas.drawRect(topPoint, pointPaintTop);
            canvas.drawRect(bottomPoint, pointPaintBottom);
            canvas.drawRect(leftPoint, pointPaintLeft);
            canvas.drawRect(rightPoint, pointPaintRight);

        } else{
            canvas.drawOval(pointRectangle, pointPaint);
            canvas.drawRect(topPoint, pointPaintTop);
            canvas.drawRect(bottomPoint, pointPaintBottom);
            canvas.drawRect(leftPoint, pointPaintLeft);
            canvas.drawRect(rightPoint, pointPaintRight);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int Action = event.getAction();
        if(Action == MotionEvent.ACTION_DOWN){
            pressed = true;
            invalidate();

            if(listener != null){
                listener.onClick(this);
            }
        }
        else if(Action == MotionEvent.ACTION_UP){
            pressed = false;
            invalidate();
        }
        return true;
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        this.listener = l;
    }
}
