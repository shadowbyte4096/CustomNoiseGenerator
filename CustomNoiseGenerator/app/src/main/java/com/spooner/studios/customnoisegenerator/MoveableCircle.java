package com.spooner.studios.customnoisegenerator;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class MoveableCircle {
    public Position Pos = new Position(50,50);
    public float Radius = 50;
    public Boolean IsSelected = false;
    public int Color;
    private final Paint paint;

    public MoveableCircle(float x, float y, int Radius, int Color) {
        Pos = new Position(x, y);
        this.Radius = Radius;
        this.Color = Color;
        paint = new Paint();
        paint.setColor(Color);
        paint.setStyle(Paint.Style.FILL);
    }

    public MoveableCircle(Position pos, int Radius, int Color) {
        this.Pos = pos;
        this.Radius = Radius;
        this.Color = Color;
        paint = new Paint();
        paint.setColor(Color);
        paint.setStyle(Paint.Style.FILL);
    }

    public Boolean IsTouched(Position other) {
        return other.IsNear(Pos, Radius);
    }

    public Boolean IsTouched(Position other, float tolerance) {
        return other.IsNear(Pos, Radius * (1 + tolerance));
    }

    protected void draw(Canvas canvas) {
        canvas.drawCircle(Pos.X, Pos.Y, Radius, paint);
    }
}
