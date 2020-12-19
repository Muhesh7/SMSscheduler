package com.example.smsscheduler;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import java.util.List;


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
    private ArrayList<Model> mModels=new ArrayList<>();
    private Data mData;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         mBinding=FragmentMessageBinding.inflate(inflater,container,false);

        //mData=new Data(getActivity());
        //mModels=mData.Load();
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
                final Calendar calendar=Calendar.getInstance();
                int hour=calendar.get(Calendar.HOUR);
                int minute=calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar1=Calendar.getInstance();
                        calendar1.set(Calendar.HOUR,hourOfDay);
                        calendar1.set(Calendar.MINUTE,minute);
                        calendar1.set(Calendar.SECOND,00);
                        CharSequence charSequence= DateFormat.format("hh:mm:ss a",calendar1);
                        ScheduledTime=charSequence.toString();
                        CharSequence charSequence2= DateFormat.format("hh:mm a",calendar1);
                        mBinding.timetext.setText(charSequence2);
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
viewModel mViewModel;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel= ViewModelProviders.of(getActivity()).get(viewModel.class);
       mViewModel.getAll().observe(getViewLifecycleOwner(), new Observer<List<Model>>() {
            @Override
            public void onChanged(List<Model> models) {
                mModels= (ArrayList<Model>) models;
            }
        });
    }

    public void permissionResult(boolean isYes){
    if(isYes)
    {  String number=mBinding.num.getText().toString();
       String mesg=mBinding.msg.getText().toString();

        String format = "MM/dd/yyyy hh:mm:ss a";
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
        CharSequence charSequence= DateFormat.format("MM/dd/yyyy hh:mm:ss a",calendar1);
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
        Log.d("ddd",Long.toString(del));

        Model model=new Model();
        model.setNumber(number);
        model.setMsg(mesg);
        model.setTime(del);
        model.setStatus("pending");
        model.setTimestring(ScheduledDate+" "+ScheduledTime);
        model.setKey(mModels.size());
        //mModels.add(model);
       // mData.Save(mModels);
        mViewModel.insert(model);
        int id=mModels.size();
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
                reset();
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
        Toast.makeText(getContext(),"fill all the details",Toast.LENGTH_SHORT).show();
        return false;
    }else {

        return true;
    }

    }
    private void reset()
    {
        mBinding.msg.setText("");
        mBinding.datetext.setText("00/00/00");
        mBinding.timetext.setText("00:00");
        mBinding.num.setText("");
    }
}
