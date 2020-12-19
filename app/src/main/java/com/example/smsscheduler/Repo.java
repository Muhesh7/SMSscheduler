package com.example.smsscheduler;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class Repo {
    private LiveData<List<Model>> list;
    private List<Model> ModelList;
    private DAO mDAO;


    public Repo(Application application)
    {
        DB db=DB.getInstance(application);
        mDAO=db.mDAO();
        list=mDAO.GetAll();

    }
    public LiveData<List<Model>> getAll() { return list; }
    public void insert(Model model)
    {
        new insert(mDAO).execute(model);
    }
    public void update(Model model)
    {
        new update(mDAO).execute(model);
    }
    public void delete(Model model)
    {
        new delete(mDAO).execute(model);
    }
    public void deleteAll()
    {
        new deleteAll(mDAO).execute();
    }

    public Model getModel(Integer integer)
    {
        try {
            return new getAll(mDAO).execute(integer).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }

    }


    private static class insert extends AsyncTask<Model,Void,Void>{
        private DAO mDAO;
        public insert(DAO dao)
        {
            mDAO=dao;
        }
        @Override
        protected Void doInBackground(Model... models) {
            mDAO.Insert(models[0]);
            return null;
        }
    }
    private static class delete extends AsyncTask<Model,Void,Void>{
        private DAO mDAO;
        public delete(DAO dao)
        {
            mDAO=dao;
        }
        @Override
        protected Void doInBackground(Model... models) {
            mDAO.Delete(models[0]);
            return null;
        }
    }
    private static class update extends AsyncTask<Model,Void,Void>{
        private DAO mDAO;
        public update(DAO dao)
        {
            mDAO=dao;
        }
        @Override
        protected Void doInBackground(Model... models) {
            mDAO.Update(models[0]);
            return null;
        }
    }
    private static class deleteAll extends AsyncTask<Void,Void,Void>{
        private DAO mDAO;
        public deleteAll(DAO dao)
        {
            mDAO=dao;
        }
        @Override
        protected Void doInBackground(Void...voids) {
            mDAO.DeleteAll();
            return null;
        }
    }
    private static class getAll extends AsyncTask<Integer,Void,Model>{
        private DAO mDAO;
        public getAll(DAO dao)
        {
            mDAO=dao;
        }

        @Override
        protected Model doInBackground(Integer... integers) {
            Model Rmodel =new Model();
            List<Model> models=mDAO.GetAllModel();
            for(Model model:models)
            {
                if(model.getKey()==integers[0])
                {Rmodel=model;}
            }
            return Rmodel;
        }

        @Override
        protected void onPostExecute(Model model) {
            super.onPostExecute(model);
        }
    }

}
