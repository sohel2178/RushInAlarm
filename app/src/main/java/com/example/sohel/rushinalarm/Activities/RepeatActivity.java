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

    private List<String> repeatDays;

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
                if(repeatDays.size()>0){
                    checkAllDays();
                }
            }


        }


    }

    private void checkAllDays() {

        for(String x: repeatDays){
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

        List<String> checkedDays = getCheckedDay();

        Intent intent = new Intent();
        intent.putExtra("repeat_days", (Serializable) checkedDays);
        setResult(RESULT_OK,intent);

        finish();

    }

    public List<String> getCheckedDay(){
        List<String> retList = new ArrayList<>();


        if(ckMon.isChecked()){
            retList.add("Mon");
        }

        if(ckTue.isChecked()){
            retList.add("Tue");
        }

        if(ckWed.isChecked()){
            retList.add("Wed");
        }

        if(ckThu.isChecked()){
            retList.add("Thu");
        }

        if(ckFri.isChecked()){
            retList.add("Fri");
        }

        if(ckSat.isChecked()){
            retList.add("Sat");
        }

        if(ckSun.isChecked()){
            retList.add("Sun");
        }

        return retList;

    }
}
