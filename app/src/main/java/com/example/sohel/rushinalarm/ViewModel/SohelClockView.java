package com.example.sohel.rushinalarm.ViewModel;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;

import com.example.sohel.rushinalarm.R;

import java.util.Calendar;

import br.tiagohm.clockview.ClockView;

/**
 * Created by Genius 03 on 9/18/2017.
 */

public class SohelClockView extends ClockView implements Runnable {
    private Context context;

    private Thread thread;

    private boolean canDraw = false;

    private Handler handler = new Handler();


    public SohelClockView(Context context) {
        this(context,null);
    }

    public SohelClockView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs){
        this.context = context;

        setDigitalClockTextColor(context.getColor(R.color.text_light));
        setCircleColor(context.getColor(R.color.theme_blue_text));
        setDotColor(context.getColor(R.color.sample_red));
        setHourHandColor(context.getColor(R.color.material_animations_primary_dark));
        setMinuteHandColor(context.getColor(R.color.material_animations_primary));
        setSecondHandColor(context.getColor(R.color.sample_yellow));
        setShowNumerals(true);

        thread = new Thread(this);

    }

    @Override
    public void run() {

        while (canDraw){

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

        final int seconds = (int) (localTome / 1000) % 60 ;
        final int minutes = (int) ((localTome / (1000*60)) % 60);
        final int hours   = (int) ((localTome / (1000*60*60)) % 24);

        handler.post(new Runnable() {
            @Override
            public void run() {
                setHour(hours);
                setMinute(minutes);
                setSecond(seconds);

                postInvalidate();
            }
        });


    }

    public void pause(){
        canDraw=false;

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

    public void resume(){


        // Do the thing When Activity is Resume
        canDraw = true;
        thread = new Thread(this);
        thread.start();




    }
}
