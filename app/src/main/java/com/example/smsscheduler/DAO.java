package com.example.smsscheduler;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;


@Dao
public interface DAO {

    @Insert
    void Insert(Model model);

    @Delete
    void Delete(Model model);

    @Update
    void Update(Model model);

    @Query("SELECT * FROM content_table")
    LiveData<List<Model>> GetAll();

    @Query("SELECT * FROM content_table")
    List<Model> GetAllModel();

    @Query("DELETE FROM content_table")
     void DeleteAll();

}
