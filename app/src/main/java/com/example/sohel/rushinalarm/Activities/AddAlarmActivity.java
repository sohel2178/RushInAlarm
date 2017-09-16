package com.example.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sohel.rushinalarm.DialogFragment.ClockFragment;
import com.example.sohel.rushinalarm.Listener.TimeListener;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Model.Sound;
import com.example.sohel.rushinalarm.R;

import java.util.List;

public class AddAlarmActivity extends BaseDetailActivity implements View.OnClickListener,
        TimeListener{

    private static final int SNOOZE_REQ=1000;
    private static final int REPEAT_REQ=2000;
    private static final int MUSIC_REQ=3000;

    private TextView tvTime,tvRepeatMode,tvSnooze,tvSound;
    private LinearLayout llRepeatContainer,llSoundandMusic;
    private RelativeLayout rlvolContainer,rlSnoozeContainer;
    private SeekBar volSeekbar;
    private CheckBox ckbVibration,ckbFadeIn;
    private EditText etNote;
    private Button btnSave;

    private AlarmData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        setupWindowAnimations();

        setupToolbar();

        if(getIntent().getSerializableExtra("data")!=null){
            data = (AlarmData) getIntent().getSerializableExtra("data");
        }else{
            data = new AlarmData();
        }


        initView();

        bindData();
    }

    private void bindData() {
        tvTime.setText(data.getTime());
        tvSnooze.setText(data.getSnoozeDurationInMin()+" Minutes");
        tvSound.setText(data.getSound().getName());

        etNote.setText(data.getNote());

        if(data.getRepeateDays()!=null){
            if(data.getRepeateDays().size()<=0){
                tvRepeatMode.setText(getString(R.string.never));
            }else{
            //set the Repear Mode
                setRepeatMode();
            }
        }

        if(data.isVibration()){
            ckbVibration.setChecked(true);
        }

        if(data.isFadeIn()){
            ckbFadeIn.setChecked(true);
        }


    }

    private void setRepeatMode() {
        List<String> repeatModes = data.getRepeateDays();

        String value ="";

        for(String x:repeatModes){
            if(repeatModes.indexOf(x)<repeatModes.size()-1){
                value = value+x+",";
            }else{
                value = value+x;
            }

        }

        tvRepeatMode.setText(value);
    }

    private void setupWindowAnimations() {
        // Re-enter transition is executed when returning to this activity
        Slide slideTransition = new Slide();
        slideTransition.setSlideEdge(Gravity.RIGHT);
        slideTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        getWindow().setReenterTransition(slideTransition);
        getWindow().setExitTransition(slideTransition);
    }

    private void initView() {
        tvTime = (TextView) findViewById(R.id.time);
        tvTime.setOnClickListener(this);
        tvRepeatMode = (TextView) findViewById(R.id.repeat_mode);
        tvSnooze = (TextView) findViewById(R.id.snooze);
        tvSound = (TextView) findViewById(R.id.sound);

        llRepeatContainer = (LinearLayout) findViewById(R.id.repeat);
        llSoundandMusic = (LinearLayout) findViewById(R.id.sound_container);

        llRepeatContainer.setOnClickListener(this);
        llSoundandMusic.setOnClickListener(this);

        rlvolContainer = (RelativeLayout) findViewById(R.id.vol_container);
        rlSnoozeContainer = (RelativeLayout) findViewById(R.id.snooze_container);

        rlSnoozeContainer.setOnClickListener(this);

        volSeekbar = (SeekBar) findViewById(R.id.vol_seek);
        volSeekbar.setProgress(100);

        volSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(data!=null){
                    data.setVolume(i);
                }
                volSeekbar.setProgress(data.getVolume(),true);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ckbVibration = (CheckBox) findViewById(R.id.vibration_check);
        ckbFadeIn = (CheckBox) findViewById(R.id.fade_in_check);

        ckbVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    data.setVibration(true);
                }else{
                    data.setVibration(false);
                }
            }
        });

        ckbFadeIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    data.setFadeIn(true);
                }else{
                    data.setFadeIn(false);
                }
            }
        });

        etNote = (EditText) findViewById(R.id.note);

        btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.time:
                showTimePicker();
                break;

            case R.id.save:
                break;

            case R.id.snooze_container:
                gotoActivity(SnoozeDurationActivity.class,SNOOZE_REQ);
                break;

            case R.id.repeat:
                gotoActivity(RepeatActivity.class,REPEAT_REQ);
                break;

            case R.id.sound_container:
                gotoActivity(SoundActivity.class,MUSIC_REQ);
                break;
        }

    }

    private void showTimePicker() {
        FragmentManager manager = getSupportFragmentManager();
        ClockFragment clockFragment = new ClockFragment();
        clockFragment.setTimeListener(this);
        clockFragment.show(manager,"TAG");
    }

    @Override
    public void getTime(int hour, int min) {

        String time = String.format("%02d",hour)+":"+String.format("%02d",min);
        tvTime.setText(time);
        data.setTime(time);

    }

    private void gotoActivity(Class target,int requestCode){

        Intent intent = new Intent(getApplicationContext(),target);
        if (data!=null){
            intent.putExtra("data",data);
        }
        transitionToResult(intent,requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SNOOZE_REQ:
                if(resultCode==RESULT_OK){
                    Log.d("YYYY",data.getIntExtra("snooze",0)+"");

                    int value = data.getIntExtra("snooze",0);

                    tvSnooze.setText(value+" Minutes");
                    this.data.setSnoozeDurationInMin(value);

                }
                break;

            case MUSIC_REQ:
                if(resultCode==RESULT_OK){
                    Log.d("YYYY",data.getIntExtra("snooze",0)+"");

                    Sound selectedSound = (Sound) data.getSerializableExtra("sound");

                    this.data.setSound(selectedSound);
                    tvSound.setText(selectedSound.getName());
                }
                break;

            case REPEAT_REQ:
                if(resultCode==RESULT_OK){

                    List<String> repeatDays = (List<String>) data.getSerializableExtra("repeat_days");
                    this.data.setRepeateDays(repeatDays);
                    setRepeatMode();
                }
                break;
        }
    }
}
