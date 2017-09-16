package com.example.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RepeatActivity extends BaseDetailActivity implements View.OnClickListener{

    private CheckBox ckMon,ckTue,ckWed,ckThu,ckFri,ckSat,ckSun;
    private Button btnOk;

    private String repeatDays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repeat);

        AlarmData data = (AlarmData) getIntent().getSerializableExtra("data");



        setupToolbar();
        setupWindowAnimations();

        initView();

        if(data!=null){
            repeatDays = data.getRepeateDays();

            if(repeatDays!=null){
                checkAllDays();

            }


        }


    }

    private void checkAllDays() {

        for(String x: repeatDays.split(",")){
            check(x);
        }
    }

    private void check(String x) {
        switch (x){
            case "Mon":
                ckMon.setChecked(true);
                break;

            case "Tue":
                ckTue.setChecked(true);
                break;

            case "Wed":
                ckWed.setChecked(true);
                break;

            case "Thu":
                ckThu.setChecked(true);
                break;

            case "Fri":
                ckFri.setChecked(true);
                break;

            case "Sat":
                ckSat.setChecked(true);
                break;

            case "Sun":
                ckSun.setChecked(true);
                break;
        }
    }

    private void initView() {
        ckMon = (CheckBox) findViewById(R.id.ckb_monday);
        ckTue = (CheckBox) findViewById(R.id.ckb_tuesday);
        ckWed = (CheckBox) findViewById(R.id.ckb_wednesday);
        ckThu = (CheckBox) findViewById(R.id.ckb_thursday);
        ckFri = (CheckBox) findViewById(R.id.ckb_friday);
        ckSat = (CheckBox) findViewById(R.id.ckb_satday);
        ckSun = (CheckBox) findViewById(R.id.ckb_sunday);

        btnOk = (Button) findViewById(R.id.ok);
        btnOk.setOnClickListener(this);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.LEFT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    @Override
    public void onClick(View view) {

        String checkedDays = getCheckedDay();

        Intent intent = new Intent();
        intent.putExtra("repeat_days", checkedDays);
        setResult(RESULT_OK,intent);

        finish();

    }

    public String getCheckedDay(){
        String reStr = "";


        if(ckMon.isChecked()){
            reStr=reStr+"Mon,";
        }

        if(ckTue.isChecked()){
            reStr=reStr+"Tue,";
        }

        if(ckWed.isChecked()){
            reStr=reStr+"Wed,";
        }

        if(ckThu.isChecked()){
            reStr=reStr+"Thu,";
        }

        if(ckFri.isChecked()){
            reStr=reStr+"Fri,";
        }

        if(ckSat.isChecked()){
            reStr=reStr+"Sat,";
        }

        if(ckSun.isChecked()){
            reStr=reStr+"Sun,";
        }

        if(reStr.length()>=4){
            reStr = reStr.substring(0,reStr.length()-1);
        }else{
            reStr="Never";
        }

        return reStr;

    }
}
