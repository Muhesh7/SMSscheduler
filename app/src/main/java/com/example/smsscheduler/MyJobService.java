package com.example.smsscheduler;

import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.telephony.SmsManager;
import android.util.Log;

public class MyJobService extends JobService {
    boolean jobCancel=false;
    @Override
    public boolean onStartJob(final JobParameters params) {
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
        Repo repo=new Repo(getApplication());
        Model model=repo.getModel(params.getJobId());
        if(model!=null) {
            Model model1 = new Model();
            model1.setKey(model.getKey());
            model1.setNumber(model.getNumber());
            model1.setMsg(model.getMsg());
            model1.setTime(model.getTime());
            model1.setStatus("Sent");
            model1.setTimestring(model.getTimestring());
            Intent intent = new Intent(this, MyJobService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(model.getNumber(), null, model.getMsg(), pendingIntent, null);
            Log.d("ddd", Integer.toString(params.getJobId()));
            repo.update(model1);
            pushNoti.push(this).pushNotification(model.getNumber());
            jobFinished(params, false);
        }
        else
        {pushNoti.push(this).pushNotification("Couldn't complete Action");
            jobFinished(params,true);
        }

    }
}
