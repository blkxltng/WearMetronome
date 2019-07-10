package com.blkxltng.wearmetronome;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupActivity extends WearableActivity {

    private Button mButtonStart, mButtonPlus, mButtonMinus;
    private TextView mTextViewBPM;
    private int selectedBPM;

    //Used to quickly adjust the BPM
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;
    private final int NUMBER_ADJUSTMENT_DELAY = 50;
    private final int BPM_UPPER_LIMIT = 300;
    private final int BPM_LOWER_LIMIT = 40;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        mButtonStart = findViewById(R.id.buttonStart);
        mButtonPlus = findViewById(R.id.buttonPlus);
        mButtonMinus = findViewById(R.id.buttonMinus);
        mTextViewBPM = findViewById(R.id.textViewBPM);

        selectedBPM = Integer.parseInt(mTextViewBPM.getText().toString());

        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetupActivity.this, MainActivity.class);
                intent.putExtra("selectedBPM", selectedBPM);
                startActivity(intent);
            }
        });

        mButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mButtonMinus.isEnabled()) {
                    mButtonMinus.setEnabled(true);
                }

                incrementCounter();
            }
        });

        mButtonPlus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAutoIncrement = true;
                repeatUpdateHandler.post(new RepeatUpdater());
                return false;
            }
        });

        mButtonPlus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction()== MotionEvent.ACTION_UP || event.getAction()== MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement ){
                    mAutoIncrement = false;
                }

                return false;
            }
        });

        mButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mButtonPlus.isEnabled()) {
                    mButtonPlus.setEnabled(true);
                }

                decrementCounter();
            }
        });

        mButtonMinus.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mAutoDecrement = true;
                repeatUpdateHandler.post(new RepeatUpdater());
                return false;
            }
        });

        mButtonMinus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if((event.getAction()== MotionEvent.ACTION_UP || event.getAction()== MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement ){
                    mAutoDecrement = false;
                }

                return false;
            }

        });

        // Enables Always-on
        setAmbientEnabled();
    }

    class RepeatUpdater implements Runnable {
        public void run() {
            if( mAutoIncrement ){
                incrementCounter();
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), NUMBER_ADJUSTMENT_DELAY);
            } else if( mAutoDecrement ){
                decrementCounter();
                repeatUpdateHandler.postDelayed(new RepeatUpdater(), NUMBER_ADJUSTMENT_DELAY);
            }
        }
    }

    public void incrementCounter(){
        selectedBPM++;
        mTextViewBPM.setText(Integer.toString(selectedBPM));

        if(selectedBPM >= BPM_UPPER_LIMIT) {
            mButtonPlus.setEnabled(false);
            mAutoIncrement = false;
        }
    }

    private void decrementCounter() {
        selectedBPM--;
        mTextViewBPM.setText(Integer.toString(selectedBPM));

        if(selectedBPM <= BPM_LOWER_LIMIT) {
            mButtonMinus.setEnabled(false);
            mAutoDecrement = false;
        }
    }
}
