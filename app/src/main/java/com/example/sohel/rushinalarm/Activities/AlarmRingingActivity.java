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
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    private Animation blinkAnimation;
    private MyVibrator myVibrator;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        // this object is Simply Blink the Icon
        blinkAnimation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        myVibrator = new MyVibrator(getApplication());


        openAlarmDatabase();

        mJobScheduler = new MyJobScheduler(getApplicationContext());

        helper = new SoundHelper(getApplicationContext());

        alarmData  = (AlarmData) getIntent().getSerializableExtra(Constant.DATA);

        if(alarmData!=null){

            float volumePercent =(float) alarmData.getVolume()/100;
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            if(alarmData.getFadeIn()==1){
                vH = new VolumeHandler(audioManager,new Handler());
            }else{
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (maxVolume*volumePercent),0);
            }

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

        // This Code is for Vibrating Alarm
        if(alarmData.getVibration()==1){
            myVibrator.resume();
        }

        ivSnooze.setAnimation(blinkAnimation);
        ivStopAlarm.setAnimation(blinkAnimation);

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

       clearAnimation();

        myVibrator.pause();

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

            if(myVibrator.isRunning){
                myVibrator.pause();
            }
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
                finishAfterTenSecond();
                break;
        }
    }

    private void snoozeAlarm() {
        mJobScheduler.snoozeJob(alarmData);
        stopMediaPlayer();

        finishAfterTenSecond();
    }

    private void finishAfterTenSecond() {
        clearAnimation();

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        },10*1000);
    }

    private void clearAnimation(){
        ivStopAlarm.clearAnimation();
        ivSnooze.clearAnimation();
    }


    // This Inner Class is Responsible for Vibration
    private class MyVibrator implements Runnable{
        private Context context;
        Vibrator v;
        private Thread thread;

        private boolean isRunning = false;

        private MyVibrator(Context context) {
            this.context = context;
            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            //thread = new Thread(this);
        }

        @Override
        public void run() {

            while (isRunning){

                try {
                    if(v.hasVibrator()){
                        v.vibrate(500);
                    }

                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }

        void resume(){
            isRunning = true;
            thread = new Thread(this);
            thread.start();
        }

        void pause(){
            isRunning=false;

            if(thread!=null){
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


        }
    }


}
