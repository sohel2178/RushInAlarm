package com.linearbd.sohel.rushinalarm.Utility;

import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Genius 03 on 9/26/2017.
 */

public class VolumeHandler implements Runnable {

    private AudioManager audioManager;
    private Handler mHandler;
    private final static int DELAY_UNTILL_NEXT_INCREASE = 10*1000;

    private Thread thread;

    public VolumeHandler(AudioManager audioManager, Handler mHandler) {
        this.audioManager = audioManager;
        this.mHandler = mHandler;
        Log.d("Call","ConsTructor Called");



        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        float percent = 0.1f;
        int tenVol = (int) (maxVolume*percent);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, tenVol, 0);



    }

    @Override
    public void run() {

        Log.d("Call","Run Method is Called");

        int currentAlarmVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("Call",currentAlarmVolume+"");
        Log.d("Call",audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)+"");
        if(currentAlarmVolume != audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)){ //if we havent reached the max

            Log.d("Call","Criteria Match");
            //here increase the volume of the alarm stream by adding currentAlarmVolume+someNewFactor
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,currentAlarmVolume+1,0);
            mHandler.postDelayed(this,DELAY_UNTILL_NEXT_INCREASE); //"recursively call this runnable again with some delay between each increment of the volume, untill the condition above is satisfied.
        }

    }

    public void resume(){
        thread = new Thread(this);
        thread.start();

    }

    public void pause(){
        if(thread!=null){
            while (true){
                try {
                    thread.join();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            thread=null;
        }
    }
}
