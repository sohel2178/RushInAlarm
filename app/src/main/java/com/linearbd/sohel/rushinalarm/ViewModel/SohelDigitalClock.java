package com.linearbd.sohel.rushinalarm.ViewModel;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.linearbd.sohel.rushinalarm.Utility.Constant;

import java.text.DecimalFormat;
import java.util.Calendar;

/**
 * Created by sohel on 30-09-17.
 */

public class SohelDigitalClock extends AppCompatTextView implements Runnable {

    private Context context;
    private Thread thread;

    private boolean isRunning=false;

    private Handler handler = new Handler();


    public SohelDigitalClock(Context context) {
        this(context,null);
    }

    public SohelDigitalClock(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SohelDigitalClock(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        this.context = context;

        Typeface tf = Typeface.createFromAsset(context.getAssets(), Constant.T_SEVEN_SEGMENT);
        this.setTypeface(tf);

    }


    @Override
    public void run() {

        while (isRunning){

            try {
                Thread.sleep(1000);
                updateTime();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void updateTime() {

        long systemTime = System.currentTimeMillis();
        long zoneTime = Calendar.getInstance().getTimeZone().getRawOffset();
        long localTome = systemTime+zoneTime;

        DecimalFormat df = new DecimalFormat("00");
        final int seconds = (int) (localTome / 1000) % 60 ;
        final int minutes = (int) ((localTome / (1000*60)) % 60);
        final int hours   = (int) ((localTome / (1000*60*60)) % 24);

        final String text = df.format(hours) +":"+df.format(minutes)+":"+df.format(seconds);

        handler.post(new Runnable() {
            @Override
            public void run() {

                setText(text);

                postInvalidate();
            }
        });


    }


    public void resume(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pause(){
        isRunning=false;

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
