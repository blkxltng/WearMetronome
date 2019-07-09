package com.blkxltng.wearmetronome;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends WearableActivity {

    private TextView mTextView;

    int testNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);

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
                    }
                });
            }
        }, 0, 1000);

        // Enables Always-on
        setAmbientEnabled();
    }
}
