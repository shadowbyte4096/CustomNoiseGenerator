package com.spooner.studios.customnoisegenerator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GraphCanvas.OnPointMovedListener {
    private GraphCanvas frequencyGraph;
    private NoiseGenerator noiseGenerator;
    private Button playToggleButton;
    private Button addPointButton;
    private Button removePointButton;
    private boolean IsPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frequencyGraph = findViewById(R.id.frequencyGraph);
        playToggleButton = findViewById(R.id.playToggleButton);
        addPointButton = findViewById(R.id.addPointButton);
        removePointButton = findViewById(R.id.removePointButton);
        noiseGenerator = new NoiseGenerator();

        frequencyGraph.setOnPointMovedListener(this);

        playToggleButton.setOnClickListener(this::PlayToggle);
        addPointButton.setOnClickListener(this::AddPoint);
        removePointButton.setOnClickListener(this::RemovePoint);

        OnPointAmountChange();
    }

    @Override
    public void onPointMoved() {
        if (IsPlaying){
            Play();
        }
    }

    public void PlayToggle(View v){
        if (IsPlaying){
            Stop();
            playToggleButton.setText(R.string.play_noise);
        } else {
            Play();
            playToggleButton.setText(R.string.stop_noise);
        }
    }

    public void Play(){
        IsPlaying = true;
        List<Position> positions = frequencyGraph.GetPositions();
        noiseGenerator.stop();
        noiseGenerator.GenerateNoise(positions);
    }

    public void Stop(){
        IsPlaying = false;
        noiseGenerator.stop();
    }

    public void AddPoint(View v){
        frequencyGraph.AddPoint();
        OnPointAmountChange();
    }

    public void RemovePoint(View v){
        frequencyGraph.RemovePoint();
        OnPointAmountChange();
    }

    public void OnPointAmountChange(){
        removePointButton.setEnabled(frequencyGraph.GetPositions().size() > 2);
        onPointMoved();
    }
}
