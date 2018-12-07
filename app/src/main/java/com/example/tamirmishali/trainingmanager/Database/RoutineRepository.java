package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.RoutineDao;
import com.example.tamirmishali.trainingmanager.Routine;

import java.util.List;

public class RoutineRepository {
    private RoutineDao  routineDao;
    private LiveData<List<Routine>> allRoutines;

    public RoutineRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
        allRoutines = routineDao.getAllRoutines();
    }

    public void insert(Routine routine){
        new InsertRoutineAsyncTask(routineDao).execute(routine);
    }

    public void update(Routine routine){
        new UpdateRoutineAsyncTask(routineDao).execute(routine);
    }

    public void delete(Routine routine){
        new DeleteRoutineAsyncTask(routineDao).execute(routine);
    }

    public void deleteAll(){
        new DeleteAllRoutineAsyncTask(routineDao).execute();
    }

    public LiveData<List<Routine>> getAllRoutines(){
        return allRoutines;
    }


    //AsyncTasks
    private static class InsertRoutineAsyncTask extends AsyncTask<Routine, Void, Void>{
        private RoutineDao routineDao;

        private InsertRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.insert(routines[0]);
            return null;
        }
    }

    private static class UpdateRoutineAsyncTask extends AsyncTask<Routine, Void, Void>{
        private RoutineDao routineDao;

        private UpdateRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.update(routines[0]);
            return null;
        }
    }

    private static class DeleteRoutineAsyncTask extends AsyncTask<Routine, Void, Void>{
        private RoutineDao routineDao;

        private DeleteRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Routine... routines) {
            routineDao.delete(routines[0]);
            return null;
        }
    }

    private static class DeleteAllRoutineAsyncTask extends AsyncTask<Void, Void, Void>{
        private RoutineDao routineDao;

        private DeleteAllRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            routineDao.deleteAll();
            return null;
        }
    }

}
