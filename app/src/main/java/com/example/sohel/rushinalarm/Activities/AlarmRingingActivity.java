package com.example.sohel.rushinalarm.Activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.Utility.Constant;
import com.example.sohel.rushinalarm.Utility.SoundHelper;
import com.example.sohel.rushinalarm.ViewModel.SohelClockView;

import java.util.List;

public class AlarmRingingActivity extends AppCompatActivity implements View.OnClickListener {

    private SohelClockView sohelClockView;

    private AlarmData alarmData;
    private Thread thread;
    private SoundHelper helper;

    private MediaPlayer mediaPlayer;

    private JobScheduler mJobScheduler;

    private AlarmDatabase alarmDatabase;

    // View Element
    private ImageView ivSnooze,ivStopAlarm;

    private PowerManager.WakeLock fullWakeLock,partialWakeLock;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        createWakeLocks();
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        openAlarmDatabase();

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);

        helper = new SoundHelper(getApplicationContext());

        alarmData  = (AlarmData) getIntent().getSerializableExtra(Constant.DATA);


        // Cancel the Job if it is not Repeating
        if(alarmData.getRepeateDays().equals(getString(R.string.never))){
            Log.d("HHH","Cancel Job");
            mJobScheduler.cancel(alarmData.getId());
            // cancel Alarm in Database
            alarmData.setIsSet(0);
            alarmDatabase.updateAlarmData(alarmData);
        }else {
            // get the RepeateDays


        }


        if(alarmData!=null){
            Log.d("HHH","Data Found");
        }

        initView();




    }

    private void initView() {
        sohelClockView = (SohelClockView) findViewById(R.id.clock);
        ivSnooze = (ImageView) findViewById(R.id.snooze);
        ivStopAlarm = (ImageView) findViewById(R.id.stop);

        ivSnooze.setOnClickListener(this);
        ivStopAlarm.setOnClickListener(this);
    }

    private void openAlarmDatabase() {
        alarmDatabase = new AlarmDatabase(getApplicationContext());
        alarmDatabase.open();
    }


    @Override
    protected void onStart() {
        super.onStart();

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), helper.getSoundById(alarmData.getSoundId()).getRes_id());
                mediaPlayer.setLooping(true);
               // mediaPlayer.setVolume();
                mediaPlayer.start();

                Log.d("THREAD",Thread.currentThread().getName());

            }
        });

        thread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sohelClockView.resume();
        if(partialWakeLock.isHeld()){
            partialWakeLock.release();
        }

        wakeDevice();
    }

    public void wakeDevice() {
        fullWakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }

    @Override
    protected void onPause() {
        partialWakeLock.acquire();
        sohelClockView.pause();
        pauseThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        stopMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        if(alarmDatabase!=null){
            alarmDatabase.close();
        }
        super.onDestroy();
    }

    private void pauseThread(){
        while (true){
            try {
                thread.join();
                // After Join Break the Loop
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        thread=null;
    }

    private void stopMediaPlayer(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.snooze:
                snoozeAlarm();
                break;

            case R.id.stop:
                stopMediaPlayer();
                break;
        }
    }

    private void snoozeAlarm() {
    }


    // Called from onCreate
    protected void createWakeLocks(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");
        partialWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Loneworker - PARTIAL WAKE LOCK");
    }


}
