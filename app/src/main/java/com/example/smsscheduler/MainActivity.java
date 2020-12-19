package com.example.smsscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
FrameLayout mFrameLayout;
Fragment mFragment,mFragment2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout=findViewById(R.id.fragment);
        mFragment=new MessageFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,mFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.bottomsheet,new List()).commit();
    }

    public void checkPermission()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
        == PackageManager.PERMISSION_GRANTED)
        {
          notifyFragment(true);
        }
        else {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS))
            {
                new AlertDialog.Builder(this)
                        .setTitle("Sms Permission")
                        .setMessage("Enable SMS Permission to send SMS")
                        .setPositiveButton("Ask Me", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
            else {
                requestPermission();
            }
        }
    }
public void requestPermission() {
    String[] s = {Manifest.permission.SEND_SMS};
    ActivityCompat.requestPermissions(this,s,123);
}
   public void notifyFragment(boolean isYes)
   {

   if(true)
   {
       ((MessageFragment) mFragment).permissionResult(isYes);
   }
   else {
       Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
   }

   }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==123)
        {
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
           { notifyFragment(true);}
           else { notifyFragment(false); }

        }
    }
}
