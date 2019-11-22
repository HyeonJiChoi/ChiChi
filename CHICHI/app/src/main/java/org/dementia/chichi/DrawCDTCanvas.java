package org.dementia.chichi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DrawCDTCanvas extends View {
    private ArrayList<Bundle> points = new ArrayList<Bundle>();
    Paint paint;
    public ArrayList<Bundle> moving_points = new ArrayList<Bundle>();
    public ArrayList<Bundle> number_points = new ArrayList<Bundle>();
    public int width, height;

    public DrawCDTCanvas(Context context) {
        super(context);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    public DrawCDTCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                Bundle newBundle = new Bundle();
                newBundle.putFloat("X", event.getX());
                newBundle.putFloat("Y", event.getY());
                newBundle.putBoolean("isDraw", true);
                points.add(newBundle);
                // 화면을 갱신함 -> onDraw()를 호출
                invalidate();
                return true;
            // 아래 작업을 안하게 되면 선이 어색하게 그려지는 현상 발생
            // ex) 점을 찍고 이동한 뒤에 점을 찍는 경우
            case MotionEvent.ACTION_UP:
                Bundle anotherBundle = new Bundle();
                anotherBundle.putFloat("X", event.getX());
                anotherBundle.putFloat("Y", event.getY());
                anotherBundle.putBoolean("isDraw", false);
                points.add(anotherBundle);
                moving_points.add(anotherBundle);
                return false;
            case MotionEvent.ACTION_DOWN:
                Bundle otherBundle = new Bundle();
                otherBundle.putFloat("X", event.getX());
                otherBundle.putFloat("Y", event.getY());
                otherBundle.putBoolean("isDraw", false);
                points.add(otherBundle);
                moving_points.add(otherBundle);
                return true;
        }
        // System.out.println("DrawingView.onTouch - " + points);
        // return false 로 하게되면 이벤트가 한번 발생하고 종료 -> 점을 그림
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        width = getWidth();
        height = getHeight();
        setNumber_points();
        paint.setStrokeWidth(15);
        System.out.println(moving_points);
        canvas.drawPoint(getWidth() / 2, getHeight() / 2, paint);
        paint.setTextSize(40);
        paint.setTextAlign(Paint.Align.CENTER);
        for (int i = 0; i < 12; i++ ) {
            int clock = i;
            if (i == 0) clock = 12;
            canvas.drawText(Integer.toString(clock), (float)number_points.get(i).getDouble("X"), (float)number_points.get(i).getDouble("Y"), paint);
        }
        for (int i = 1; i < points.size(); i++) {
            if (!points.get(i).getBoolean("isDraw")) continue;
            // 선을 그려줌
            // canvas.drawLine( 이전 좌표, 현재 좌표, 선 속성 );
            canvas.drawLine(points.get(i - 1).getFloat("X"), points.get(i - 1).getFloat("Y"), points.get(i).getFloat("X"), points.get(i).getFloat("Y"), paint);
        }
    }
    public void setNumber_points(){
        number_points.clear();
        int init_x = 0;
        int init_y = height / 2 - 80;
        for (int i = 0; i < 12; i++ ) {
            double theta = -2.0 * i * Math.PI / 12;
            double s = Math.sin(theta);
            double c = Math.cos(theta);
            double x = -s * init_y + c * init_x + width / 2;
            double y = s * init_x + c * init_y - height / 2;
            Bundle newbundle = new Bundle();
            newbundle.putDouble("X", x);
            newbundle.putDouble("Y", -y);
            number_points.add(newbundle);
        }

    }

    // Reset Function
    public void reset() {
        points.clear(); // PaintPoint ArrayList Clear
        moving_points.clear();
        invalidate(); // 화면을 갱신함 -> onDraw()를 호출
    }
}
