package com.example.sohel.rushinalarm.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
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

import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.DialogFragment.ClockFragment;
import com.example.sohel.rushinalarm.Listener.TimeListener;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Model.Sound;
import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.Utility.SoundHelper;

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

    private SoundHelper helper;

    // Declare SQLITE Database
    private AlarmDatabase alarmDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        //Open Database
        openDatabase();

        helper = new SoundHelper(getApplicationContext());


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

    @Override
    protected void onDestroy() {
        closeDatabase();
        super.onDestroy();
    }

    private void closeDatabase() {
        alarmDatabase.close();
    }

    private void openDatabase() {
        alarmDatabase = new AlarmDatabase(getApplicationContext());
        alarmDatabase.open();
    }

    private void bindData() {
        tvTime.setText(data.getTime());
        tvSnooze.setText(data.getSnoozeDurationInMin()+" Minutes");
        tvSound.setText(helper.getSoundById(data.getSoundId()).getName());

        etNote.setText(data.getNote());

        if(data.getRepeateDays()!=null){
            if(data.getRepeateDays()==null){
                tvRepeatMode.setText(getString(R.string.never));
            }else{
            //set the Repear Mode
                setRepeatMode();
            }
        }

        if(data.getVibration()==1){
            ckbVibration.setChecked(true);
        }

        if(data.getFadeIn()==1){
            ckbFadeIn.setChecked(true);
        }

        volSeekbar.setProgress(data.getVolume());


    }

    private void setRepeatMode() {
        //List<String> repeatModes = data.getRepeateDays();
        tvRepeatMode.setText(data.getRepeateDays());
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
                    Log.d("TTTT",i+"");

                }
                //volSeekbar.setProgress(data.getVolume(),true);
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
                    data.setVibration(1);
                }else{
                    data.setVibration(0);
                }
            }
        });

        ckbFadeIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    data.setFadeIn(1);
                }else{
                    data.setFadeIn(0);
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
                saveData();
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

    private void saveData() {
        data.setNote(etNote.getText().toString());

        boolean isUpdate=false;

        if(getIntent().getSerializableExtra("data")==null){
            long id = alarmDatabase.insertRow(data);
            data.setId((int)id);

            if(id==-1){
                isUpdate=false;
            }
            Log.d("DDDD",id+"");
        }else{
            Log.d("DDDD","Update");

            Log.d("DDDD","ID of Data before Update "+data.getId());

            // If Id of data not Yet Save it is impossible to update operation in data base.
            // So set Id before Update

            if(data.getId()==-1){

            }

            if(alarmDatabase.updateAlarmData(data)){
                Log.d("DDDD","Update in DB");
                isUpdate =true;
            }
        }

        Intent intent = new Intent();
        intent.putExtra("is_update",isUpdate);
        intent.putExtra("data",data);
        setResult(RESULT_OK,intent);
        finish();

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

                    int selectedSoundId = data.getIntExtra("sound",-1);

                    if(selectedSoundId!=-1){
                        this.data.setSoundId(selectedSoundId);
                    }

                    tvSound.setText(helper.getSoundById(selectedSoundId).getName());
                }
                break;

            case REPEAT_REQ:
                if(resultCode==RESULT_OK){

                    String repeatDays = data.getStringExtra("repeat_days");
                    this.data.setRepeateDays(repeatDays);
                    setRepeatMode();
                }
                break;
        }
    }
}
