package com.example.sohel.rushinalarm;

import android.content.Intent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.example.sohel.rushinalarm.Activities.AlarmActivity;
import com.example.sohel.rushinalarm.Activities.AlarmRingingActivity;
import com.example.sohel.rushinalarm.Utility.TransitionHelper;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivInfo,ivSettings,ivAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupWindowAnimations();

        initView();

    }

    private void initView() {
        ivAlarm = (ImageView) findViewById(R.id.alarm);
        ivInfo = (ImageView) findViewById(R.id.info);
        ivSettings = (ImageView) findViewById(R.id.settings);

        ivAlarm.setOnClickListener(this);
        ivInfo.setOnClickListener(this);
        ivSettings.setOnClickListener(this);
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

                break;

            case R.id.settings:
                transitionToActivity(AlarmRingingActivity.class);


                break;
        }
    }
}
