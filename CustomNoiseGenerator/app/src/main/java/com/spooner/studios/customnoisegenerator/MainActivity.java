package com.spooner.studios.customnoisegenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GraphCanvas frequencyGraph;
    private NoiseGenerator noiseGenerator;
    private Button playButton;
    private Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frequencyGraph = findViewById(R.id.frequencyGraph);
        playButton = findViewById(R.id.playButton);
        stopButton = findViewById(R.id.stopButton);
        noiseGenerator = new NoiseGenerator();

        playButton.setOnClickListener(this::Play);
        stopButton.setOnClickListener(this::Stop);
    }

    public void Play(View v){
        List<Position> positions = frequencyGraph.GetPositions();
        noiseGenerator.stop();
        noiseGenerator.GenerateNoise(positions);
    }

    public void Stop(View v){
        noiseGenerator.stop();
    }
}
