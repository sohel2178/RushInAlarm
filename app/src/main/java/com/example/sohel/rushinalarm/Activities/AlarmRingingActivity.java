package com.example.sohel.rushinalarm.Activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.ViewModel.SohelClockView;

public class AlarmRingingActivity extends AppCompatActivity {

    private SohelClockView sohelClockView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        sohelClockView = (SohelClockView) findViewById(R.id.clock);


    }


    @Override
    protected void onStart() {
        super.onStart();





    }

    @Override
    protected void onResume() {
        super.onResume();
        sohelClockView.resume();
    }

    @Override
    protected void onPause() {
        sohelClockView.pause();
        super.onPause();
    }
}
