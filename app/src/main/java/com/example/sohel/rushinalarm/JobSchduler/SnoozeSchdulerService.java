package com.example.sohel.rushinalarm.JobSchduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.sohel.rushinalarm.Activities.TestActivity;

/**
 * Created by Genius 03 on 9/26/2017.
 */

public class SnoozeSchdulerService extends JobService {
    private static final String TAG="SOHEL";

    private Handler mJobHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            JobParameters parameters = (JobParameters) msg.obj;

            startTestActivity();

            jobFinished(parameters,false);
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


    private void startTestActivity(){
        Intent intent = new Intent(getApplicationContext(), TestActivity.class);
        startActivity(intent);
    }
}
