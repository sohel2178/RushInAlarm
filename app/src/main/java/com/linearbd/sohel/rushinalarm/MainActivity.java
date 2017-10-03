package com.linearbd.sohel.rushinalarm;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.linearbd.sohel.rushinalarm.Activities.AlarmActivity;
import com.linearbd.sohel.rushinalarm.Activities.AlarmRingingActivity;
import com.linearbd.sohel.rushinalarm.Activities.InfoActivity;
import com.linearbd.sohel.rushinalarm.Utility.AdUtil;
import com.linearbd.sohel.rushinalarm.Utility.MyJobScheduler;
import com.linearbd.sohel.rushinalarm.Utility.TransitionHelper;
import com.linearbd.sohel.rushinalarm.ViewModel.SohelClockView;
import com.linearbd.sohel.rushinalarm.ViewModel.SohelDigitalClock;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivInfo,ivSettings,ivAlarm;

    private SohelClockView sohelClockView;
    private SohelDigitalClock sohelDigitalClock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            setupWindowAnimations();
        }

        AdUtil adUtil = new AdUtil(this);

        initView();

    }

    private void initView() {
        ivAlarm = (ImageView) findViewById(R.id.alarm);
        ivInfo = (ImageView) findViewById(R.id.info);
        ivSettings = (ImageView) findViewById(R.id.settings);

        sohelClockView = (SohelClockView) findViewById(R.id.analog_clock);
        sohelDigitalClock = (SohelDigitalClock) findViewById(R.id.digital_clock);

        ivAlarm.setOnClickListener(this);
        ivInfo.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sohelClockView.resume();
        sohelDigitalClock.resume();
    }

    @Override
    protected void onPause() {
        sohelClockView.pause();
        sohelDigitalClock.pause();
        super.onPause();
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    private void transitionToActivity(Class target) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        startActivity(target, pairs);
    }

    private void startActivity(Class target, Pair<View, String>[] pairs) {
        Intent intent = new Intent(getApplicationContext(),target);

        ActivityOptionsCompat activityTransitionOption =
                ActivityOptionsCompat.makeSceneTransitionAnimation(this,pairs);

        startActivity(intent,activityTransitionOption.toBundle());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alarm:
                transitionToActivity(AlarmActivity.class);
                break;

            case R.id.info:
                transitionToActivity(InfoActivity.class);

                break;

            case R.id.settings:
                //transitionToActivity(AlarmRingingActivity.class);
                break;
        }
    }

    private void testJobSchduler() {
        MyJobScheduler myJobScheduler = new MyJobScheduler(getApplicationContext());
        //myJobScheduler.snoozeJob(15*1000,10);
    }
}
