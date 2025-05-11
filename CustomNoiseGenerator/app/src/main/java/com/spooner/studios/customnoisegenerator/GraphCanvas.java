package com.spooner.studios.customnoisegenerator;

import static android.graphics.Color.argb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphCanvas extends View {
    private Paint circlePaint, linePaint, marginPaint;


    public List<MoveableCircle> points = new ArrayList<>();
    private float canvasWidth, canvasHeight;
    private Map<Integer, MoveableCircle> selectedCircles = new HashMap<>();
    private final int circleRadius = 50;
    private RectF marginRect;

    public GraphCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        circlePaint = new Paint();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStyle(Paint.Style.FILL);

        linePaint = new Paint();
        linePaint.setColor(Color.WHITE);
        linePaint.setStrokeWidth(5);

        marginPaint = new Paint();
        marginPaint.setColor(Color.DKGRAY);
        marginPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        canvasWidth = w;
        canvasHeight = h;

        marginRect = new RectF(circleRadius, circleRadius, canvasWidth - circleRadius, canvasHeight - circleRadius);

        points.add(new MoveableCircle(circleRadius, canvasHeight/2, circleRadius, Color.rgb(30 ,30 ,30)));
        points.add(new MoveableCircle(canvasWidth/2, canvasHeight/2, circleRadius, Color.rgb(30 ,30 ,30)));
        points.add(new MoveableCircle(canvasWidth/2, canvasHeight/2, circleRadius, Color.rgb(30 ,30 ,30)));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the margin rectangle
        canvas.drawRect(marginRect, marginPaint);

        MoveableCircle lastPoint = null;
        for (MoveableCircle point : points) {
            point.draw(canvas);
            if (lastPoint == null) {
                lastPoint = point;
                continue;
            }
            canvas.drawLine(lastPoint.Pos.X, lastPoint.Pos.Y, point.Pos.X, point.Pos.Y, linePaint);
            lastPoint = point;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                Position pointerPosition = new Position(event.getX(pointerIndex), event.getY(pointerIndex));

                for (MoveableCircle point : points) {
                    if (point.IsTouched(pointerPosition, 1.0F)) {
                        selectedCircles.put(pointerId, point);
                        break;
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int id = event.getPointerId(i);
                    MoveableCircle point = selectedCircles.get(id);
                    if (point != null) {
                        float newX = event.getX(i);
                        float newY = event.getY(i);

                        newX = Math.max(point.Radius, Math.min(newX, canvasWidth - point.Radius));
                        newY = Math.max(point.Radius, Math.min(newY, canvasHeight - point.Radius));

                        point.Pos.X = newX;
                        point.Pos.Y = newY;
                    }
                }

                points.sort((p1, p2) -> Float.compare(p1.Pos.GetX(), p2.Pos.GetX()));
                invalidate();
                return true;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                selectedCircles.remove(pointerId);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public List<Position> GetPositions(){
        List<Position> positions = new ArrayList<>();
        for (MoveableCircle point : points) {
            var percentageX = (point.Pos.X - circleRadius) / (canvasWidth - 2 * circleRadius);
            var percentageY = (canvasHeight -point.Pos.Y - circleRadius) / (canvasHeight - 2 * circleRadius);
            Position percentage = new Position(percentageX, percentageY);
            positions.add(percentage);
        }
        return positions;
    }
}