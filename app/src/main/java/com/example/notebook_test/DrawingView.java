package com.example.notebook_test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

public class DrawingView extends View {

    //initial color基础颜色
    private static final int DEFAULT_PAINT_COLOR = Color.BLACK;
    //drawing path绘画路径
    private Path drawPath;
    //drawing and canvas paint基础画笔和画板画笔
    private Paint drawPaint, canvasPaint;
    //canvas画板
    private Canvas drawCanvas;
    //canvas bitmap位图
    private Bitmap canvasBitmap;
    //brush size画笔粗细
    private float brushSize;
    //erase mode擦除模式
    private boolean erase;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();

        setBrushSize(10);

        drawPaint.setColor(DEFAULT_PAINT_COLOR);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        if (canvasBitmap == null){
            canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            drawCanvas = new Canvas(canvasBitmap);
        }
        else {
            canvasBitmap = Bitmap.createBitmap(canvasBitmap);
            canvasBitmap = canvasBitmap.copy(Bitmap.Config.ARGB_8888, true);
            drawCanvas = new Canvas(canvasBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    public boolean draw(float x, float y, int motionAction) {
        switch (motionAction){
            case MotionEvent
                    .ACTION_DOWN:
                     drawPath.moveTo(x, y);
                     break;
            case MotionEvent
                    .ACTION_MOVE:
                     drawPath.lineTo(x, y);
                     break;
            case MotionEvent
                    .ACTION_UP:
                     drawCanvas.drawPath(drawPath, drawPaint);
                     drawPath.reset();
                     break;
            default:
                     return false;
        }
        invalidate();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        return draw(event.getX(),
                    event.getY(),
                    event.getAction());
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();//刷新
    }

    public void setPaintColor(int paintColor){
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float size){
        //pixel
        brushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    size, getResources().getDisplayMetrics());
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setErase(boolean isErase){
        erase = isErase;//擦除

        if(erase){
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }
        else {
            drawPaint.setXfermode(null);
        }
    }

    public Bitmap getCanvasBitmap(){
        return canvasBitmap;
    }

    public void setBitmap(Bitmap bmp){
        canvasBitmap = bmp;
    }
}
