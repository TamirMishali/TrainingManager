package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Database.DAOs.RoutineDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Routine.Routine;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RoutineRepository {
    private RoutineDao  routineDao;
//    private WorkoutDao  workoutDao;
    private LiveData<List<Routine>> allRoutines;
//    private LiveData<List<Workout>> allWorkouts;

    public RoutineRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        routineDao = database.routineDao();
 //       workoutDao = database.workoutDao();
        allRoutines = routineDao.getAllRoutines();
//        allWorkouts = workoutDao.getAllWorkouts();
    }

    //-----Routines------
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

    public Routine getRoutine(int routineId){
        Routine routine = new Routine();
        try {
            routine = new GetRoutineAsyncTask(routineDao).execute(routineId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return routine;
    }
    public Routine getFirstRoutine(){
        Routine routine = new Routine();
        try {
            routine = new GetFirstRoutineAsyncTask(routineDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return routine;
    }



    //Routine - AsyncTasks
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

    private static class GetRoutineAsyncTask extends AsyncTask<Integer, Void, Routine>{
        private RoutineDao routineDao;


        private GetRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Routine doInBackground(Integer... params) {
            return routineDao.getRoutine(params[0]);
        }
    }
    private static class GetFirstRoutineAsyncTask extends AsyncTask<Void, Void, Routine>{
        private RoutineDao routineDao;


        private GetFirstRoutineAsyncTask(RoutineDao routineDao){
            this.routineDao = routineDao;
        }

        @Override
        protected Routine doInBackground(Void... voids) {
            return routineDao.getFirstRoutine();
        }
    }

}

