package com.example.sohel.rushinalarm.Activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.sohel.rushinalarm.DialogFragment.ClockFragment;
import com.example.sohel.rushinalarm.Listener.TimeListener;
import com.example.sohel.rushinalarm.R;

public class AddAlarmActivity extends BaseDetailActivity implements View.OnClickListener,
        TimeListener{

    private TextView tvTime,tvRepeatMode;
    private LinearLayout llRepeatContainer,llSoundandMusic;
    private RelativeLayout rlvolContainer,rlSnoozeContainer;
    private SeekBar volSeekbar;
    private CheckBox ckbVibration,ckbFadeIn;
    private EditText etNote;
    private Button btnSave;

    private int volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_alarm);

        setupToolbar();

        initView();
    }

    private void initView() {
        tvTime = (TextView) findViewById(R.id.time);
        tvTime.setOnClickListener(this);
        tvRepeatMode = (TextView) findViewById(R.id.repeat_mode);

        llRepeatContainer = (LinearLayout) findViewById(R.id.repeat);
        llSoundandMusic = (LinearLayout) findViewById(R.id.sound_container);

        rlvolContainer = (RelativeLayout) findViewById(R.id.vol_container);
        rlSnoozeContainer = (RelativeLayout) findViewById(R.id.snooze_container);

        volSeekbar = (SeekBar) findViewById(R.id.vol_seek);
        volSeekbar.setProgress(100);

        volSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                volume=i;
                volSeekbar.setProgress(volume,true);

                Log.d("IIII",volume+"");
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

    }
}
