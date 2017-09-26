package com.example.sohel.rushinalarm.Activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.KeyguardManager;
import android.app.job.JobScheduler;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
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
import com.example.sohel.rushinalarm.Utility.MyJobScheduler;
import com.example.sohel.rushinalarm.Utility.SoundHelper;
import com.example.sohel.rushinalarm.Utility.VolumeHandler;
import com.example.sohel.rushinalarm.ViewModel.SohelClockView;

import java.util.List;

public class AlarmRingingActivity extends AppCompatActivity implements View.OnClickListener {

    private SohelClockView sohelClockView;

    private AlarmData alarmData;
    PowerManager.WakeLock fullWakeLock;
    private MediaPlayer mediaPlayer;
    private Thread mediaPlayerThread;
    private AudioManager audioManager;
    private VolumeHandler vH;

    private SoundHelper helper;

    private MyJobScheduler mJobScheduler;

    private AlarmDatabase alarmDatabase;

    // View Element
    private ImageView ivSnooze,ivStopAlarm;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);


        openAlarmDatabase();

        mJobScheduler = new MyJobScheduler(getApplicationContext());

        helper = new SoundHelper(getApplicationContext());

        alarmData  = (AlarmData) getIntent().getSerializableExtra(Constant.DATA);

        if(alarmData!=null){

            if(alarmData.getFadeIn()==1){
                vH = new VolumeHandler(audioManager,new Handler());
            }
            Log.d("HHH","Data Found");
        }


        // Cancel the Job if it is not Repeating
        if(alarmData.getRepeateDays().equals(getString(R.string.never))){
            Log.d("HHH","Cancel Job");
            mJobScheduler.cancelAlarm(alarmData);
            // cancel Alarm in Database
            alarmData.setIsSet(0);
            alarmDatabase.updateAlarmData(alarmData);
        }else {
            // get the RepeateDays


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

        mediaPlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), helper.getSoundById(alarmData.getSoundId()).getRes_id());
                mediaPlayer.setLooping(true);
                mediaPlayer.start();

            }
        });

        mediaPlayerThread.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sohelClockView.resume();
        // Check Vh not Null
        if(vH!=null){
            vH.resume();
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
        if(vH!=null){
            vH.pause();
        }

        sohelClockView.pause();
        pauseMediaPlayerThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        fullWakeLock.release();
        stopMediaPlayer();
    }

    @Override
    protected void onDestroy() {
        if(alarmDatabase!=null){
            alarmDatabase.close();
        }
        super.onDestroy();
    }

    private void pauseMediaPlayerThread(){

        if(mediaPlayerThread!=null){
            while (true){
                try {
                    mediaPlayerThread.join();
                    // After Join Break the Loop
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            mediaPlayerThread=null;
        }


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
                //actualVolume++;

                break;

            case R.id.stop:
                stopMediaPlayer();
                break;
        }
    }

    private void snoozeAlarm() {
    }


}
