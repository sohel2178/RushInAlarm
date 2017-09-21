package com.example.sohel.rushinalarm.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Utility.Constant;

/**
 * Created by Genius 03 on 9/18/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmDatabase alarmDatabase;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm! Alarm Alarm", Toast.LENGTH_SHORT).show();

        int id = intent.getIntExtra(Constant.ID,-1);

        if(id!=-1){
            Log.d("HHH",id+"");
        }

        /*if(data!=null){
            Log.d("HHH",data.getTime());
            Log.d("HHH",data.getRepeateDays());
            Log.d("HHH",data.getSnoozeDurationInMin()+"");
        }else{
            Log.d("HHH","Data Null");
        }*/



    }

    private void cancelAlarm(){

    }


}
