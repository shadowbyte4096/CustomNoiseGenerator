package com.spooner.studios.customnoisegenerator;

public class SingleResponseFunction {
    private float m;
    private float c;

    private Position lowerBound;
    private Position upperBound;

    public SingleResponseFunction(Position lowerBound, Position upperBound, float max){
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;

        m = (lowerBound.Y - upperBound.Y) / (lowerBound.X - upperBound.X);
        c = lowerBound.Y - (m * lowerBound.X);
    }

    public boolean IsWithinBounds(float x){
        return !(x < lowerBound.X) && !(x > upperBound.X);
    }

    public float GetScaleFactor(float frequency){
        return (m * frequency) + c;
    }

}
