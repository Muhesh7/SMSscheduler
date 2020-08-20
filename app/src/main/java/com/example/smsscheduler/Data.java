package com.example.smsscheduler;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Data {
    SharedPreferences mSharedPref;
    ArrayList<model> mModels;
    int money=20000;

    public Data(Context context) {
        mSharedPref=context.getSharedPreferences("MyHeros",Context.MODE_PRIVATE);
    }

    public void Save(ArrayList<model> superHeroes)
    {
        this.mModels =superHeroes;
        SharedPreferences.Editor editor = mSharedPref.edit();
        Gson gson=new Gson();
        String mine=gson.toJson(superHeroes);
        editor.putString("MyHeroes",mine);
        editor.apply();
    }

    public  ArrayList<model> Load()
    {    Gson gson =new Gson();
        String mine =mSharedPref.getString("MyHeroes",null);
        Type type = new TypeToken<ArrayList<model>>() {}.getType();
        mModels =gson.fromJson(mine,type);
        if(mModels ==null)
        {
            mModels =new ArrayList<>();
        }

        return mModels;
    }
}
