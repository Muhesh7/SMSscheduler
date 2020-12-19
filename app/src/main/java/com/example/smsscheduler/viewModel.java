package com.example.smsscheduler;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;

public class viewModel extends AndroidViewModel {
    private LiveData<List<Model>> mListLive;
    private Repo mRepo;

    public viewModel(@NonNull Application application) {
        super(application);
        mRepo=new Repo(application);
        mListLive=mRepo.getAll();
    }
 public void insert(Model model)
 {
     mRepo.insert(model);
 }
    public void delete(Model model)
    {
        mRepo.delete(model);
    }

    public void deleteAll()
    {
        mRepo.deleteAll();
    }
    public LiveData<List<Model>> getAll()
    {
        return mListLive;
    }
}
