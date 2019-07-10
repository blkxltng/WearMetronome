package com.blkxltng.wearmetronome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity {

    private TextView mTextView, mTextViewBPM;

    int testNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        int selectedBPM = intent.getIntExtra("selectedBPM", 120);
        int numMilliseconds = 60000 / selectedBPM;

        mTextViewBPM = findViewById(R.id.textViewBPM);
        mTextView = findViewById(R.id.text);

        mTextViewBPM.setText("BPM: " + selectedBPM);

        final Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        testNum++;
                        if(testNum > 4) {
                            testNum = 1;
                        }
                        mTextView.setText((Integer.toString(testNum)));
//                        if(vibrator.hasVibrator()) {
//                            vibrator.vibrate(100);
//                        }
                    }
                });
            }
        }, 0, numMilliseconds);

        // Enables Always-on
        setAmbientEnabled();
    }
}
