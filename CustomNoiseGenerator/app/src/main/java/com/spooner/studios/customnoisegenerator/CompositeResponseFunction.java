package com.spooner.studios.customnoisegenerator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.lang.Math;

public class CompositeResponseFunction {

    private final double minFrequency = 20.0; // Minimum frequency (20 Hz)
    private final double maxFrequency = 20000.0; // Maximum frequency (20 kHz)

    private final List<SingleResponseFunction> functions = new ArrayList<>();

    public CompositeResponseFunction(List<Position> positions){
        List<Position> truePositions = new ArrayList<>();
        float max = 0;
        for (Position position : positions){
            if (position.Y > max){
                max = position.Y;
            }

            double x = minFrequency * Math.pow((maxFrequency/minFrequency), position.X);
            truePositions.add(new Position((float)x, position.Y));
        }
        truePositions.sort(Comparator.comparing(Position::GetX));

        for (int i = 0; i < truePositions.size() - 1; i++) {
            Position first = truePositions.get(i);
            Position second = truePositions.get(i + 1);
            functions.add(new SingleResponseFunction(first, second, max));
        }
    }

    public float GetScaleFactor(float frequency){
        for (SingleResponseFunction function : functions){
            if (function.IsWithinBounds(frequency)){
                return function.GetScaleFactor(frequency);
            }
        }
        return 0;
    }
}
