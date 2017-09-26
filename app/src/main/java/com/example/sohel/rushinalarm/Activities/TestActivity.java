package com.example.sohel.rushinalarm.Activities;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.Utility.VolumeHandler;

public class TestActivity extends AppCompatActivity {

    PowerManager.WakeLock fullWakeLock;

    private MediaPlayer mediaPlayer;
    private Thread mediaPlayerThread;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.d("Call","Call");
        }
    };

    private AudioManager audioManager;

    private VolumeHandler vH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        fullWakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Loneworker - FULL WAKE LOCK");

        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        vH = new VolumeHandler(audioManager,mHandler);




    }

    @Override
    protected void onResume() {
        super.onResume();

        vH.resume();
        wakeDevice();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mediaPlayerThread = new Thread(new Runnable() {
            @Override
            public void run() {

                mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sweet_alarm);
                mediaPlayer.setLooping(true);
                // mediaPlayer.setVolume();
                mediaPlayer.start();

            }
        });

        mediaPlayerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseMediaPlayerThread();
    }

    @Override
    protected void onStop() {
        super.onStop();

        fullWakeLock.release();

        stopMediaPlayer();
    }

    private void wakeDevice() {
        fullWakeLock.acquire();

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("TAG");
        keyguardLock.disableKeyguard();
    }


    private void stopMediaPlayer(){
        if(mediaPlayer!=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }

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
}
