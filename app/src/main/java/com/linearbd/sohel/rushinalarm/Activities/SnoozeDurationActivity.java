package com.linearbd.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.linearbd.sohel.rushinalarm.Model.AlarmData;
import com.linearbd.sohel.rushinalarm.R;

public class SnoozeDurationActivity extends BaseDetailActivity implements View.OnClickListener {

    private Button btnOk;

    private RadioButton rb5,rb10,rb15,rb20,rb25,rb30,rbNever;

    private RadioGroup radGroup;

    private int value;

    private int duration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_duration);

        AlarmData alarmData = (AlarmData) getIntent().getSerializableExtra("data");

        if(alarmData!= null){
            Log.d("HHH",alarmData.getSnoozeDurationInMin()+"");
            duration = alarmData.getSnoozeDurationInMin();
        }

        setupToolbar();
        setupWindowAnimations();

        initView();
    }

    private void initView() {
        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);

        // init Radio Button Here
        rb5 = (RadioButton) findViewById(R.id.rad_5);
        rb10 = (RadioButton) findViewById(R.id.rad_10);
        rb15 = (RadioButton) findViewById(R.id.rad_15);
        rb20 = (RadioButton) findViewById(R.id.rad_20);
        rb25 = (RadioButton) findViewById(R.id.rad_25);
        rb30 = (RadioButton) findViewById(R.id.rad_30);
        rbNever = (RadioButton) findViewById(R.id.rad_never);

        radGroup = (RadioGroup) findViewById(R.id.group);
        radGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                Log.d("YYYY",i+"");
                calculateValue(i);
            }
        });


        // Checked based on Value
        checkedRadio();
    }

    private void checkedRadio() {
        switch (duration){
            case 0:
                rbNever.setChecked(true);
                break;
            case 5:
                rb5.setChecked(true);
                break;
            case 10:
                rb10.setChecked(true);
                break;
            case 15:
                rb15.setChecked(true);
                break;
            case 20:
                rb20.setChecked(true);
                break;
            case 25:
                rb25.setChecked(true);
                break;
            case 30:
                rb30.setChecked(true);
                break;
        }
    }

    private void calculateValue(int i) {
        switch (i){
            case R.id.rad_5:
                value=5;
                break;

            case R.id.rad_10:
                value=10;
                break;

            case R.id.rad_15:
                value=15;
                break;

            case R.id.rad_20:
                value=20;
                break;

            case R.id.rad_25:
                value=25;
                break;

            case R.id.rad_30:
                value=30;
                break;

            case R.id.rad_never:
                value=0;
                break;
        }
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.RIGHT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();
        intent.putExtra("snooze",value);
        setResult(RESULT_OK,intent);
        finish();

    }
}
