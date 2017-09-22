package com.example.sohel.rushinalarm.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Visibility;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.sohel.rushinalarm.Adapter.AlarmDataAdapter;
import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.JobSchduler.MyJobSchedulerService;
import com.example.sohel.rushinalarm.Listener.AlarmClickListener;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.AlarmReceiver;
import com.example.sohel.rushinalarm.Utility.Constant;
import com.example.sohel.rushinalarm.Utility.MyJobScheduler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmActivity extends BaseDetailActivity implements View.OnClickListener,
        AlarmClickListener{
    private static final int ALARM_DATA_REQ=4000;

    private RecyclerView rvAlarm;
    private ImageView ivAddAlarm;
    private AlarmDatabase alarmDatabase;

    private List<AlarmData> alarmDataList;
    private AlarmDataAdapter adapter;


    private AlarmManager alarmManager;
    private List<PendingIntent> pendinIntenList;


    private MyJobScheduler myJobScheduler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        myJobScheduler = new MyJobScheduler(getApplicationContext());

        pendinIntenList = new ArrayList<>();
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        //Open Alarm Database
        openAlarmDatabase();

        alarmDataList = alarmDatabase.getAllAlarmData();

        Log.d("DDDD",alarmDataList.size()+"");
        adapter = new AlarmDataAdapter(getApplicationContext(),alarmDataList);
        adapter.setListener(this);

        setupWindowAnimations();
        setupToolbar();

        initView();


    }

    private void openAlarmDatabase() {
        alarmDatabase = new AlarmDatabase(getApplicationContext());
        alarmDatabase.open();
    }

    @Override
    protected void onDestroy() {
        alarmDatabase.close();
        super.onDestroy();
    }

    private void initView() {
        rvAlarm = (RecyclerView) findViewById(R.id.rv_alarm);
        rvAlarm.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvAlarm.setAdapter(adapter);
        ivAddAlarm = (ImageView) findViewById(R.id.add);
        ivAddAlarm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        transitionToResult(new Intent(getApplicationContext(),AddAlarmActivity.class),ALARM_DATA_REQ);
    }

    private void setupWindowAnimations() {
        Visibility enterTransition = buildEnterTransition();
        getWindow().setEnterTransition(enterTransition);
    }

    private Visibility buildEnterTransition() {
        Fade enterTransition = new Fade();
        enterTransition.setDuration(getResources().getInteger(R.integer.anim_duration_long));
        enterTransition.excludeTarget(12,true); // You can replace any integer for |R.id.square_red|

        return enterTransition;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case ALARM_DATA_REQ:
                if(resultCode==RESULT_OK){
                    boolean isupdate = data.getBooleanExtra("is_update",false);
                    AlarmData alarmData = (AlarmData) data.getSerializableExtra("data");

                    if(isupdate){
                        Log.d("UPDATE","UPDATE");
                        int position = getPosition(alarmData);

                        Log.d("UPDATE","Position "+position);

                        if(position!=-1){
                            alarmDataList.set(position,alarmData);
                        }

                    }else{
                        alarmDataList.add(alarmData);
                    }

                    adapter.notifyDataSetChanged();

                }
                break;
        }
    }

    private int getPosition(AlarmData alarmData) {
        int id = alarmData.getId();
        int retPos=-1;

        for(AlarmData x: alarmDataList){
            if(x.getId()==id){
               retPos = alarmDataList.indexOf(x);
            }
        }
        return retPos;
    }

    @Override
    public void onAlarmClick(int position,int viewId,boolean isChecked) {
        AlarmData data = alarmDataList.get(position);

        if(viewId==1){
            Intent intent = new Intent(getApplicationContext(),AddAlarmActivity.class);
            intent.putExtra(Constant.DATA,data);
            transitionToResult(intent,ALARM_DATA_REQ);
        }else if(viewId==2){

            if(isChecked){
                myJobScheduler.setAlarm(data);
            }else{
                myJobScheduler.cancelAlarm(data);
            }

        }


    }

    @Override
    public void obSwitchChange(int position, boolean value) {
        AlarmData data = alarmDataList.get(position);

        Log.d("DDD",value+"");

        if(value){
            data.setIsSet(1);

        }else {
            data.setIsSet(0);
        }

        boolean fal =alarmDatabase.updateAlarmData(data);

        Log.d("DDD",fal+"");
    }

    /*private void setAlarm(AlarmData alarmData) {

        Log.d("HHH","Alarm Set");



        int hour = alarmData.getHour();
        int minutes = alarmData.getMinutes();

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        if(hour>=12){
            hour = hour%12;
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.AM_PM,Calendar.PM);
        }else{
            calendar.set(Calendar.HOUR_OF_DAY, hour);
        }

        calendar.set(Calendar.MINUTE, minutes);


        PendingIntent alarmIntent = createPendingIntent(alarmData);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        Log.d("HHH","Alarm Remaining "+(calendar.getTimeInMillis()-System.currentTimeMillis()) );



        //alarmManager.re(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+10000,alarmIntent);
    }*/




    /*private void cancelAlarm(AlarmData data){

        PendingIntent pendingIntent = createPendingIntent(data);

        if(alarmManager!=null){
            Log.d("HHH","Alarm Cancel");
            alarmManager.cancel(pendingIntent);
        }

    }*/

    private PendingIntent createPendingIntent(AlarmData data){

        Intent intent = new Intent(getApplicationContext(),AlarmReceiver.class);
        intent.putExtra(Constant.ID,data.getId());

        PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(),data.getId(),intent,0);

        //PendingIntent.getBroadcast()

        return pi;

    }


}
