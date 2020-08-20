package com.example.smsscheduler;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

import java.util.ArrayList;

public class MyJobService extends JobService {
    boolean jobCancel=false;
    Data mData;
    ArrayList<Model> models;
    @Override
    public boolean onStartJob(final JobParameters params) {
        mData=new Data(this);
        models=mData.Load();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendsms(params);
                }
            }).start();

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        jobCancel=true;
        return true;
    }
    public void sendsms(JobParameters params) {

        Intent intent = new Intent(this,MyJobService.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(models.get(params.getJobId()-1).getNumber(), null, models.get(params.getJobId()-1).getMsg(), pendingIntent, null);
        Log.d("ddd", Integer.toString(params.getJobId()));
        pushNoti.push(this).pushNotification(models.get(params.getJobId()-1).getNumber());
        jobFinished(params,false);

    }
}
