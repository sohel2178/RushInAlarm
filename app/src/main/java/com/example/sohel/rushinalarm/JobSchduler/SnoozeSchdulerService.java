package com.example.sohel.rushinalarm.JobSchduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.sohel.rushinalarm.Activities.AlarmRingingActivity;
import com.example.sohel.rushinalarm.Activities.TestActivity;
import com.example.sohel.rushinalarm.Database.AlarmDatabase;
import com.example.sohel.rushinalarm.Model.AlarmData;
import com.example.sohel.rushinalarm.Utility.Constant;

/**
 * Created by Genius 03 on 9/26/2017.
 */

public class SnoozeSchdulerService extends JobService {
    private static final String TAG="SnoozeSchdulerService";
    private AlarmDatabase alarmDatabase;

    private Handler mJobHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            JobParameters jobParameters = (JobParameters) msg.obj;
            int jobId = jobParameters.getJobId();

            alarmDatabase = new AlarmDatabase(getApplicationContext());
            alarmDatabase.open();
            AlarmData alarmData =alarmDatabase.getAlarmDataById(jobId);
            alarmDatabase.close();

            startRingingActivity(alarmData);
            jobFinished(jobParameters,false);

        }
    };
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG,"onStartJob");
        mJobHandler.sendMessage(Message.obtain(mJobHandler,1,jobParameters));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        mJobHandler.removeMessages( 1 );
        Log.d(TAG,"onStopJob");
        return false;
    }



    private void startRingingActivity(AlarmData alarmData){
        Intent intent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
        intent.putExtra(Constant.DATA,alarmData);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }
}
