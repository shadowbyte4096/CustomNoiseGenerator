package com.spooner.studios.customnoisegenerator;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.List;
import java.util.Random;

import be.tarsos.dsp.util.fft.FFT;

public class NoiseGenerator {
    private AudioTrack audioTrack;
    private boolean isPlaying = false;
    private Thread noiseThread;
    private final int sampleRate = 44100;
    private final int bufferSize = 4096; // Power of 2 for FFT
    private final Random random = new Random();

    public void stop() {
        if (isPlaying) {
            isPlaying = false;
            if (noiseThread != null) {
                noiseThread.interrupt();
                noiseThread = null;
            }
            if (audioTrack != null) {
                audioTrack.stop();
                audioTrack.release();
                audioTrack = null;
            }
        }
    }

    public void GenerateNoise(List<Position> positions) {
        if (!isPlaying) {
            isPlaying = true;
            noiseThread = new Thread(() -> generateCustomNoise(positions));
            noiseThread.start();
        }
    }

    private void generateCustomNoise(List<Position> positions) {
        CompositeResponseFunction responseFunction = new CompositeResponseFunction(positions);

        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
        int minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, audioFormat);

        if (minBufferSize == AudioTrack.ERROR_BAD_VALUE || minBufferSize == AudioTrack.ERROR_INVALID_OPERATION) {
            minBufferSize = sampleRate * 2;
        }

        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, channelConfig, audioFormat, minBufferSize, AudioTrack.MODE_STREAM);
        audioTrack.play();

        float[] buffer = new float[bufferSize];

        while (isPlaying && !Thread.interrupted()) {
            for (int i = 0; i < bufferSize; i++) {
                buffer[i] = (random.nextFloat() - 0.5f) * 2.0f;
            }

            buffer = applyFrequencyResponse(buffer, responseFunction);

            short[] shortBuffer = new short[bufferSize];
            for (int i = 0; i < bufferSize; i++) {
                shortBuffer[i] = (short) (buffer[i] * Short.MAX_VALUE);
            }
            audioTrack.write(shortBuffer, 0, bufferSize);
        }
    }

    public float[] applyFrequencyResponse(float[] audioData, CompositeResponseFunction responseFunction) {
        int n = audioData.length;
        FFT fft = new FFT(n);
        float[] complexData = new float[n * 2];

        System.arraycopy(audioData, 0, complexData, 0, n);

        fft.forwardTransform(complexData);

        for (int i = 0; i < n / 2; i++) {
            float frequency = (i * 44100.0f) / n; // Assuming a sample rate of 44100 Hz
            float scaleFactor = responseFunction.GetScaleFactor(frequency);
            complexData[i * 2] *= scaleFactor; // Apply scale to real part
            complexData[i * 2 + 1] *= scaleFactor; // Apply scale to imaginary part
        }

        fft.backwardsTransform(complexData);

        float[] outputData = new float[n];
        System.arraycopy(complexData, 0, outputData, 0, n);
        return outputData;
    }
}
