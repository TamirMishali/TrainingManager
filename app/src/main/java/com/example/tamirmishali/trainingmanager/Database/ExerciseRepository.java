package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseDao;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ExerciseRepository {
    private ExerciseDao  exerciseDao;
    private LiveData<List<Exercise>> allExercises;
    private static Exercise exercise = new Exercise();

    public ExerciseRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        exerciseDao = database.exerciseDao();
        allExercises = exerciseDao.getAllExercises();
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
    public void deleteAllExercises(){
        new DeleteAllExerciseAsyncTask(exerciseDao).execute();
    }
    public Exercise getExerciseForWorkout(int exerciseastractid, int workoutid){
        //Exercise exercise =
        //return new GetExerciseForWorkoutAsyncTask(exerciseDao).execute(exerciseastractid,workoutid).get();
        try {
            exercise = new GetExerciseForWorkoutAsyncTask(exerciseDao).execute(exerciseastractid,workoutid).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //new GetExerciseForWorkoutAsyncTask(exerciseDao).execute();
        //return exercise;

        //return GetExerciseForWorkoutAsyncTask
        //return exerciseDao.getExerciseForWorkout(exerciseastractid, workoutid);
        return exercise;
    }
    public LiveData<List<Exercise>> getAllExercises(){
        return allExercises;
    }
    public LiveData<List<Exercise>> getExercisesForWorkout(int workoutid){
        return exerciseDao.getExercisesForWorkout(workoutid);
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
    private static class DeleteAllExerciseAsyncTask extends AsyncTask<Void, Void, Void> {
        private ExerciseDao exerciseDao;

        private DeleteAllExerciseAsyncTask(ExerciseDao exerciseDao) {
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            exerciseDao.deleteAllexercises();
            return null;
        }
    }

    private static class GetExerciseForWorkoutAsyncTask extends AsyncTask<Integer, Integer, Exercise>{
        private ExerciseDao exerciseDao;

        private GetExerciseForWorkoutAsyncTask(ExerciseDao exerciseDao){
            this.exerciseDao = exerciseDao;
        }

        @Override
        protected Exercise doInBackground(Integer... values) {
            //return exerciseDao.getExerciseForWorkout(values[0],values[1]);
            return exerciseDao.getExerciseForWorkout(values[0],values[1]);
            //return null;
        }

    }

}
