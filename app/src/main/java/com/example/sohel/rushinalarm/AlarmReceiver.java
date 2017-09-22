package com.example.sohel.rushinalarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.sohel.rushinalarm.Activities.AlarmRingingActivity;
import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Utility.Constant;

/**
 * Created by Genius 03 on 9/18/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private AlarmDatabase alarmDatabase;
    private AlarmData alarmData;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Alarm! Alarm Alarm", Toast.LENGTH_SHORT).show();

        int id = intent.getIntExtra(Constant.ID,-1);

        if(id!=-1){
            Log.d("HHH",id+"");
        }

        fetchAlarnData(id,context);

        Intent startActivityIntent = new Intent(context, AlarmRingingActivity.class);
        startActivityIntent.putExtra(Constant.DATA,alarmData);
        context.startActivity(startActivityIntent);
    }

    private void fetchAlarnData(int id, Context context) {
        alarmDatabase = new AlarmDatabase(context);
        alarmDatabase.open();
        alarmData = alarmDatabase.getAlarmDataById(id);
        alarmDatabase.close();

    }



}
