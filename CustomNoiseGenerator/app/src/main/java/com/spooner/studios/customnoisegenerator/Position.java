package com.spooner.studios.customnoisegenerator;

public class Position {
    public float X;
    public float Y;

    public float GetX(){
        return X;
    }

    public Position(float x, float y) {
        this.X = x;
        this.Y = y;
    }

    public Boolean IsNear(Position other, float distance){
        float dx = this.X - other.X;
        float dy = this.Y - other.Y;
        float actualDistance = (float) Math.sqrt(dx * dx + dy * dy);
        return actualDistance < distance;
    }

    public Boolean IsNear(Position other){
        float distance = 50;
        return IsNear(other, distance);
    }
}
