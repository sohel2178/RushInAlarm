package com.linearbd.sohel.rushinalarm.Utility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.linearbd.sohel.rushinalarm.JobSchduler.MyJobSchedulerService;
import com.linearbd.sohel.rushinalarm.JobSchduler.SnoozeSchdulerService;
import com.linearbd.sohel.rushinalarm.Model.AlarmData;
import com.linearbd.sohel.rushinalarm.R;

import java.util.Calendar;

/**
 * Created by sohel on 22-09-17.
 */

public class MyJobScheduler {

    private static final int NOTIFICATION_EX = 1;
    private NotificationManager notificationManager;

    private Context context;

    private JobScheduler mJobScheduler;

    public MyJobScheduler(Context context) {
        this.context = context;
        mJobScheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
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

        if(alarmData.getRepeateDays().equals("Never")){
            if(duration<=0){
                duration = duration+24*60*60*1000;
            }
        }

        JobInfo jobInfo =getJobInfo(duration,alarmData.getId());

        buildNotification(alarmData.getId());

        mJobScheduler.schedule(jobInfo);
    }

    private void buildNotification(int id){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context )
                .setSmallIcon(R.mipmap.ic_launcher);

        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(context, id , intent, 0);
        builder.setContentIntent(pIntent);
        builder.setOngoing(true);

        Notification notif = builder.build();
        notificationManager.notify(id, notif);
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

    public void snoozeJob(AlarmData data){

        int duration = data.getSnoozeDurationInMin()*60*1000;

        JobInfo jobInfo = getTestJobInfo(duration,data.getId());
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
            cancelNotification(id);
        }

    }

    private void cancelNotification(int id){
        notificationManager.cancel(id);
    }


}
