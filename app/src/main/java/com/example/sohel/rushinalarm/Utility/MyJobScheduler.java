package com.example.sohel.rushinalarm.Utility;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import com.example.sohel.rushinalarm.JobSchduler.MyJobSchedulerService;
import com.example.sohel.rushinalarm.JobSchduler.SnoozeSchdulerService;
import com.example.sohel.rushinalarm.Model.AlarmData;

import java.util.Calendar;

/**
 * Created by sohel on 22-09-17.
 */

public class MyJobScheduler {

    private Context context;

    private JobScheduler mJobScheduler;

    public MyJobScheduler(Context context) {
        this.context = context;
        mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    public void setAlarm(AlarmData alarmData){
        int hour = alarmData.getHour();
        int minutes = alarmData.getMinutes();

        // Set the alarm to start at 8:30 a.m.
        Calendar calendar = Calendar.getInstance();

        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                hour,minutes
        );

        long duration = calendar.getTimeInMillis()-System.currentTimeMillis();

        JobInfo jobInfo =getJobInfo(duration,alarmData.getId());

        mJobScheduler.schedule(jobInfo);
    }


    private JobInfo getJobInfo(long duration,int jobId){

        ComponentName componentName = new ComponentName(context.getPackageName(), MyJobSchedulerService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(jobId,componentName)
                .setMinimumLatency(duration)
                .setRequiresDeviceIdle(false)
                .setOverrideDeadline(duration);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        return builder.build();

    }

    public void startTestJob(int duration,int id){

        JobInfo jobInfo = getTestJobInfo(duration,id);
        mJobScheduler.schedule(jobInfo);

    }

    private JobInfo getTestJobInfo(long duration,int jobId){

        ComponentName componentName = new ComponentName(context.getPackageName(), SnoozeSchdulerService.class.getName());
        JobInfo.Builder builder = new JobInfo.Builder(jobId,componentName)
                .setMinimumLatency(duration)
                .setRequiresDeviceIdle(false)
                .setOverrideDeadline(duration);
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);

        return builder.build();

    }


    public void nextDayAlarm(AlarmData alarmData){
        //Cancel the Current Alarm
        mJobScheduler.cancel(alarmData.getId());

        JobInfo jobInfo = getJobInfo(24*60*60*1000,alarmData.getId());
        mJobScheduler.schedule(jobInfo);


    }

    public void cancelAlarm(AlarmData alarmData){
        cancelAlarm(alarmData.getId());
    }

    private void cancelAlarm(int id){
        if(mJobScheduler!=null){
            mJobScheduler.cancel(id);
        }

    }


}
