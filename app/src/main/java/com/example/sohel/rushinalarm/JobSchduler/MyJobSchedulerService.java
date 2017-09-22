package com.example.sohel.rushinalarm.JobSchduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.sohel.rushinalarm.Activities.AlarmRingingActivity;
import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.R;
import com.example.sohel.rushinalarm.Utility.Constant;
import com.example.sohel.rushinalarm.Utility.MyJobScheduler;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Genius 03 on 7/31/2017.
 */

public class MyJobSchedulerService extends JobService {

    private AlarmDatabase alarmDatabase;

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg ) {
            Toast.makeText( getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT ).show();

            JobParameters jobParameters = (JobParameters) msg.obj;
            int jobId = jobParameters.getJobId();

            alarmDatabase = new AlarmDatabase(getApplicationContext());
            alarmDatabase.open();
            AlarmData alarmData =alarmDatabase.getAlarmDataById(jobId);
            alarmDatabase.close();

            if(alarmData.getRepeateDays().equals(getString(R.string.never))){
                startRingingActivity(alarmData);
                jobFinished(jobParameters,false);

            }else{


                List<String> repeatingDays = Arrays.asList(alarmData.getRepeateDays().split(","));
                SimpleDateFormat format = new SimpleDateFormat("EEE");
                String todayDate = format.format(new Date());
               if(repeatingDays.contains(todayDate)){
                   startRingingActivity(alarmData);
                   jobFinished(jobParameters,false);

                   Log.d("HHH","Start Activity Job");
               }

                // ReSchedule Job for next
                MyJobScheduler myJobScheduler = new MyJobScheduler(getApplicationContext());
                myJobScheduler.nextDayAlarm(alarmData);

                Log.d("HHH","Reschedule Job");


            }

            /* PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            jobFinished( (JobParameters) msg.obj, false );*/

            //jobFinished(jobParameters,false);

            return true;
        }
    } );
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("HJJHHJ","JJJJJJ");

        mJobHandler.sendMessage(Message.obtain(mJobHandler,1,params));

        return true;
    }

    private void startRingingActivity(AlarmData alarmData){
        Intent intent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
        intent.putExtra(Constant.DATA,alarmData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }
}
