package com.example.smsscheduler;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
@Database(entities = {Model.class},version = 2)
public abstract class DB extends RoomDatabase {
private  static DB instance;
public abstract DAO mDAO();
public static DB getInstance(Context context)
{
  if(instance==null)
  {
      instance= Room.databaseBuilder(context,DB.class,"Messages")
              .fallbackToDestructiveMigration()
              .build();
  }
 return instance;
}

}
