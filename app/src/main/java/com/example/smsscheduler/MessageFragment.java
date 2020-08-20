package com.example.smsscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.smsscheduler.databinding.FragmentMessageBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MessageFragment extends Fragment {

    public MessageFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentMessageBinding mBinding;

    private String ScheduledDate,ScheduledTime,CurrentDate;
    private ArrayList<Model> mModels;
    private Data mData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         mBinding=FragmentMessageBinding.inflate(inflater,container,false);

        mData=new Data(getActivity());
        mModels=mData.Load();
        JobScheduler scheduler=(JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
        scheduler.cancel(mModels.size()-1);
        mBinding.datebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                int day=calendar.get(Calendar.DATE);
                final DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(Calendar.YEAR,year);
                        calendar1.set(Calendar.MONTH,month);
                        calendar1.set(Calendar.DATE,dayOfMonth);
                        CharSequence charSequence= DateFormat.format("MM/dd/yyyy",calendar1);
                        ScheduledDate=charSequence.toString();
                        mBinding.datetext.setText(charSequence);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        mBinding.timebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR);
                int minute=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(Calendar.HOUR,hourOfDay);
                        calendar1.set(Calendar.MINUTE,minute);
                        CharSequence charSequence= DateFormat.format("hh:mm a",calendar1);
                        ScheduledTime=charSequence.toString();
                        mBinding.timetext.setText(charSequence);
                    }
                },hour,minute,false);
                timePickerDialog.show();

            }
        });
        mBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check())
                ((MainActivity) getActivity()).checkPermission();

            }
        });
        return mBinding.getRoot();
    }

public void permissionResult(boolean isYes){
    if(isYes)
    {  String number=mBinding.num.getText().toString();
       String mesg=mBinding.msg.getText().toString();

        String format = "MM/dd/yyyy hh:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR);
        int minute=calendar.get(Calendar.MINUTE);
        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH);
        int day=calendar.get(Calendar.DATE);
        Calendar calendar1=Calendar.getInstance();
        calendar1.set(Calendar.HOUR,hour);
        calendar1.set(Calendar.MINUTE,minute);
        calendar1.set(Calendar.YEAR,year);
        calendar1.set(Calendar.MONTH,month);
        calendar1.set(Calendar.DATE,day);
        CharSequence charSequence= DateFormat.format("MM/dd/yyyy hh:mm a",calendar1);
        CurrentDate=charSequence.toString();
        Date date1= null;
        Date date2=null;
        try {
            date1 = sdf.parse(CurrentDate);
            date2=sdf.parse(ScheduledDate+" "+ScheduledTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long del=date2.getTime()-date1.getTime();
        Model model=new Model();
        model.setNumber(number);
        model.setMsg(mesg);
        model.setTime(del);
        mModels.add(model);
        mData.Save(mModels);
        int id=mModels.size();
        Log.d("ddd",Long.toString(del));
        if(del>0) {
            ComponentName serviceComponent = new ComponentName(getContext(), MyJobService.class);

            JobInfo jobInfo = new JobInfo.Builder(id, serviceComponent)
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setPersisted(true)
                    .setMinimumLatency(del)
                    .build();

            JobScheduler scheduler = (JobScheduler) getActivity().getSystemService(Context.JOB_SCHEDULER_SERVICE);
            int res = scheduler.schedule(jobInfo);
            if (res == JobScheduler.RESULT_SUCCESS) {
                Toast.makeText(getContext(), "SMS Scheduled Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "SMS Scheduled Failed", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getContext(), "Scheduled time is obselete", Toast.LENGTH_SHORT).show();        }
    }
    else {
        Log.d("ddd","Not send");
    }
}
    private boolean check()
    { if(mBinding.msg.getText().toString().isEmpty()|mBinding.num.getText().toString().isEmpty()
            |mBinding.datetext.getText().toString().equals("00/00/00")|mBinding.datebutton.getText().toString().equals("00:00"))
    {
        Toast.makeText(getContext(),"Properly fill the details",Toast.LENGTH_SHORT).show();
        return false;
    }else {

        return true;
    }

    }
}
