package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseDao;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExerciseRepository {
    private ExerciseDao  exerciseDao;
    private LiveData<List<Exercise>> allExercises;
    private static Exercise exercise = new Exercise();

    private ExecutorService executor;

    public ExerciseRepository(Application application){
        TrainingManagerDatabase database = TrainingManagerDatabase.getInstance(application);
        exerciseDao = database.exerciseDao();
        allExercises = exerciseDao.getAllExercises();

        this.executor = Executors.newSingleThreadExecutor();
    }

    //-----Exercises-----
    public void insert(Exercise exercise){
        new InsertExerciseAsyncTask(exerciseDao).execute(exercise);
    }
    public void update(Exercise exercise){
        new UpdateExerciseAsyncTask(exerciseDao).execute(exercise);
    }
    public void delete(Exercise exercise){
        new DeleteExerciseAsyncTask(exerciseDao).execute(exercise);
    }
    public void deleteAllExercises(int workout_id){
        new DeleteAllExerciseAsyncTask(exerciseDao).execute(workout_id);
    }
    public Exercise getExerciseForWorkout(int exerciseAbstractId, int workoutId){
        try {
            exercise = new GetExerciseForWorkoutAsyncTask(exerciseDao).execute(exerciseAbstractId,workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return exercise;
    }
    public LiveData<List<Exercise>> getAllExercises(){
        return allExercises;
    }
    public LiveData<List<Exercise>> getExercisesForWorkout_O(int workoutId){
        return exerciseDao.getExercisesForWorkout_O(workoutId);
    }
    public List<Exercise> getExercisesForWorkout(int workoutId){
        List<Exercise> exercises = new ArrayList<>();
        try {
            exercises = new GetExercisesForWorkoutAsyncTask(exerciseDao).execute(workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return exercises;
    }
    public List<Exercise> getExercisesForEA(int EA_ID){
        Future<List<Exercise>> future = executor.submit(new Callable<List<Exercise>>() {
            @Override
            public List<Exercise> call() throws Exception {
                return exerciseDao.getExercisesForEA(EA_ID);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }

    //Exercise - AsyncTasks
    private static class InsertExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        private InsertExerciseAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.insert(exercises[0]);
            return null;
        }
    }
    private static class UpdateExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        private UpdateExerciseAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.update(exercises[0]);
            return null;
        }
    }
    private static class DeleteExerciseAsyncTask extends AsyncTask<Exercise, Void, Void>{
        private ExerciseDao exerciseDao;

        private DeleteExerciseAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Exercise... exercises) {
            exerciseDao.delete(exercises[0]);
            return null;
        }
    }
    private static class DeleteAllExerciseAsyncTask extends AsyncTask<Integer, Void, Void> {
        private ExerciseDao exerciseDao;

        private DeleteAllExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Integer... values) {
            exerciseDao.deleteAllExercises(values[0]);
            return null;
        }
    }

    //https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
    private static class GetExerciseForWorkoutAsyncTask extends AsyncTask<Integer, Integer, Exercise>{
        private ExerciseDao exerciseDao;

        private GetExerciseForWorkoutAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Exercise doInBackground(Integer... values) {
            return exerciseDao.getExerciseForWorkout(values[0],values[1]);
        }

    }
    private static class GetExercisesForWorkoutAsyncTask extends AsyncTask<Integer, Integer, List<Exercise>>{
        private ExerciseDao exerciseDao;

        private GetExercisesForWorkoutAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }
        protected List<Exercise> doInBackground(Integer... values) {
            return exerciseDao.getExercisesForWorkout(values[0]);
        }

    }


}
