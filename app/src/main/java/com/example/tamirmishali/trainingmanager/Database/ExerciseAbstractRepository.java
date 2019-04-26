package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractDao;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseAbstractRepository {
    private ExerciseAbstractDao  exerciseAbstractDao;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;

    public ExerciseAbstractRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        exerciseAbstractDao = database.exerciseAbstractDao();
        allExerciseAbstracts = exerciseAbstractDao.getAllExercisesAbstract();
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

}
