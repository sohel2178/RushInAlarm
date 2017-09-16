package com.example.sohel.rushinalarm.JobSchduler;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.sohel.rushinalarm.Activities.AlarmRingingActivity;

/**
 * Created by Genius 03 on 7/31/2017.
 */

public class MyJobSchedulerService extends JobService {

    private Handler mJobHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage( Message msg ) {
            Toast.makeText( getApplicationContext(), "JobService task running", Toast.LENGTH_SHORT ).show();

            JobParameters jobParameters = (JobParameters) msg.obj;

            Intent intent = new Intent(getApplicationContext(), AlarmRingingActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           /* PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    12345, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            jobFinished( (JobParameters) msg.obj, false );*/
            startActivity(intent);
            return true;
        }
    } );
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.d("HJJHHJ","JJJJJJ");
        mJobHandler.sendMessage(Message.obtain(mJobHandler,1,params));
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        mJobHandler.removeMessages( 1 );
        return false;
    }
}
