package com.example.sohel.rushinalarm.Activities;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.job.JobScheduler;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.Utility.Constant;
import com.example.sohel.rushinalarm.Utility.SoundHelper;
import com.example.sohel.rushinalarm.ViewModel.SohelClockView;

import java.util.List;

public class AlarmRingingActivity extends AppCompatActivity {

    private SohelClockView sohelClockView;

    private AlarmData alarmData;
    private Thread thread;
    private SoundHelper helper;

    private MediaPlayer mediaPlayer;

    private JobScheduler mJobScheduler;

    private AlarmDatabase alarmDatabase;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);
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

        sohelClockView = (SohelClockView) findViewById(R.id.clock);


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
    }

    @Override
    protected void onPause() {
        sohelClockView.pause();
        pauseThread();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mediaPlayer!=null){
            mediaPlayer.release();
            mediaPlayer=null;
        }
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
}
