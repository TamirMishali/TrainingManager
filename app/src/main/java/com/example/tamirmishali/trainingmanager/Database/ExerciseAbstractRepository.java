package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractDao;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractInfoValue;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractNickname;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseAbstractRepository {
    private ExerciseAbstractDao  exerciseAbstractDao;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;

    // NEW:
    private LiveData<List<ExerciseAbstractNickname>> allNicknames;
    private LiveData<List<ExerciseAbstractOperation>> allOperations;
    private LiveData<List<ExerciseAbstractInfoValue>> allInfoValues;



    public ExerciseAbstractRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        exerciseAbstractDao = database.exerciseAbstractDao();
        allExerciseAbstracts = exerciseAbstractDao.getAllExercisesAbstract();
        allNicknames = exerciseAbstractDao.getAllExerciseAbstractNicknames();
        allOperations = exerciseAbstractDao.getAllExerciseAbstractOperations();
        allInfoValues = exerciseAbstractDao.getAllExerciseAbstractInfoValues();
    }

    //-----ExerciseAbstracts-----
    public void insert(ExerciseAbstract exerciseAbstract){
        new InsertExerciseAbstractAsyncTask(exerciseAbstractDao).execute(exerciseAbstract);
    }
    public void update(ExerciseAbstract exerciseAbstract){
        new UpdateExerciseAbstractAsyncTask(exerciseAbstractDao).execute(exerciseAbstract);
    }
    public void delete(ExerciseAbstract exerciseAbstract){
        new DeleteExerciseAbstractAsyncTask(exerciseAbstractDao).execute(exerciseAbstract);
    }
    public void deleteAllExerciseAbstracts(){
        new DeleteAllExerciseAbstractAsyncTask(exerciseAbstractDao).execute();
    }
    public LiveData<List<ExerciseAbstract>> getAllExerciseAbstracts(){
        return allExerciseAbstracts;
    }
    public LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int workoutId){
        return exerciseAbstractDao.getExerciseAbstractsForWorkout(workoutId);
    }
    public ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId){
        ExerciseAbstract exerciseAbstract = new ExerciseAbstract();
        try {
            exerciseAbstract = new GetExerciseAbsFromIdAsyncTask(exerciseAbstractDao).execute(exerciseAbsId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return exerciseAbstract;
    }
    public List<String> getMuscles(){
        List<String> muscleList = new ArrayList();
        try {
            muscleList = new GetMusclesAsyncTask(exerciseAbstractDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return muscleList;
    }


    // NEW
    public LiveData<List<ExerciseAbstractInfoValue>> getAllExerciseAbstractInfoValues() {
        return allInfoValues;
    }
    public LiveData<List<ExerciseAbstractOperation>> getAllExerciseAbstractOperations() {
        return allOperations;
    }
    public LiveData<List<ExerciseAbstractNickname>> getAllExerciseAbstractNicknames() {
        return allNicknames;
    }

    //ExerciseAbstract - AsyncTasks
    private static class InsertExerciseAbstractAsyncTask extends AsyncTask<ExerciseAbstract, Void, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private InsertExerciseAbstractAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(ExerciseAbstract... exerciseAbstracts) {
            exerciseAbstractDao.insert(exerciseAbstracts[0]);
            return null;
        }
    }
    private static class UpdateExerciseAbstractAsyncTask extends AsyncTask<ExerciseAbstract, Void, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private UpdateExerciseAbstractAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(ExerciseAbstract... exerciseAbstracts) {
            exerciseAbstractDao.update(exerciseAbstracts[0]);
            return null;
        }
    }
    private static class DeleteExerciseAbstractAsyncTask extends AsyncTask<ExerciseAbstract, Void, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private DeleteExerciseAbstractAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(ExerciseAbstract... exerciseAbstracts) {
            exerciseAbstractDao.delete(exerciseAbstracts[0]);
            return null;
        }
    }
    private static class DeleteAllExerciseAbstractAsyncTask extends AsyncTask<Void, Void, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private DeleteAllExerciseAbstractAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            exerciseAbstractDao.deleteAllexerciseAbstracts();
            return null;
        }
    }

    private static class GetExerciseAbsFromIdAsyncTask extends AsyncTask<Integer, Integer, ExerciseAbstract>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbsFromIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected ExerciseAbstract doInBackground(Integer... values) {
            return exerciseAbstractDao.getExerciseAbsFromId(values[0]);
        }

    }
    private static class GetMusclesAsyncTask extends AsyncTask<Void, Void, List<String>>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetMusclesAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return exerciseAbstractDao.getMuscles();
        }

    }

    // NEW - AsyncTasks

}
