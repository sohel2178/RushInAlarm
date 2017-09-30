package com.linearbd.sohel.rushinalarm.Utility;

import android.app.AlarmManager;
import android.content.Context;

import com.linearbd.sohel.rushinalarm.Model.AlarmData;

/**
 * Created by Genius 03 on 9/18/2017.
 */

public class MyAlarmManager {

    private Context context;
    private AlarmData data;

    private AlarmManager alarmManager;


    public MyAlarmManager(Context context, AlarmData data) {
        this.context = context;
        this.data = data;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setAlarm(){

    }
}
