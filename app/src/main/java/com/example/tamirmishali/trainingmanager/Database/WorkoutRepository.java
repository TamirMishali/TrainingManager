package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class WorkoutRepository {
    private WorkoutDao workoutDao;
    private LiveData<List<Workout>> allWorkouts;

    public WorkoutRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        workoutDao = database.workoutDao();
        allWorkouts = workoutDao.getAllWorkouts();
    }

    //-----Workouts-----
    public void insert(Workout workout){
        new InsertWorkoutAsyncTask(workoutDao).execute(workout);
    }
    public void update(Workout workout){
        new UpdateWorkoutAsyncTask(workoutDao).execute(workout);
    }
    public void delete(Workout workout){
        new DeleteWorkoutAsyncTask(workoutDao).execute(workout);
    }
    public void deleteAllworkouts(){
        new DeleteAllWorkoutAsyncTask(workoutDao).execute();
    }
    public LiveData<List<Workout>> getAllWorkouts(){
        return allWorkouts;
    }
    public LiveData<List<Workout>> getWorkoutsForRoutine(int routineId){
        return workoutDao.getWorkoutsForRoutine(routineId);
    }
    public List<String> getMusselsInWorkout(int workout_id){
        List<String> mussles = new ArrayList<>();
        try {
            mussles = new GetMusselsInWorkoutAsyncTask(workoutDao).execute(workout_id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return mussles;
    }
    public Workout getCurrentWorkout(){
        Workout workout = new Workout();
        try {
            workout = new GetCurrentWorkoutAsyncTask(workoutDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }
    public Workout getPrevWorkout(String workoutName, int workoutDate){
        MyTaskParams params = new MyTaskParams(workoutName, workoutDate);
        Workout workout = new Workout();
        try {
            workout = new GetPrevWorkoutAsyncTask(workoutDao).execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }

    //Workout - AsyncTasks
    private static class InsertWorkoutAsyncTask extends AsyncTask<Workout, Void, Void>{
        private WorkoutDao workoutDao;

        private InsertWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.insert(workouts[0]);
            return null;
        }
    }
    private static class UpdateWorkoutAsyncTask extends AsyncTask<Workout, Void, Void>{
        private WorkoutDao workoutDao;

        private UpdateWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.update(workouts[0]);
            return null;
        }
    }
    private static class DeleteWorkoutAsyncTask extends AsyncTask<Workout, Void, Void>{
        private WorkoutDao workoutDao;

        private DeleteWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Workout... workouts) {
            workoutDao.delete(workouts[0]);
            return null;
        }
    }
    private static class DeleteAllWorkoutAsyncTask extends AsyncTask<Void, Void, Void>{
        private WorkoutDao workoutDao;

        private DeleteAllWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            workoutDao.deleteAllworkouts();
            return null;
        }
    }

    //https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
    private static class GetMusselsInWorkoutAsyncTask extends AsyncTask<Integer, Integer, List<String>>{
        private WorkoutDao workoutDao;

        private GetMusselsInWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected List<String> doInBackground(Integer... values) {
            return workoutDao.getMusselsInWorkout(values[0]);
        }
    }

    //https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
    private static class GetCurrentWorkoutAsyncTask extends AsyncTask<Void, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetCurrentWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(Void... voids) {
            return workoutDao.getCurrentWorkout();
        }
    }


    //https://stackoverflow.com/questions/12069669/how-can-you-pass-multiple-primitive-parameters-to-asynctask
    private static class MyTaskParams {
        String workoutName;
        int workoutDate;

        MyTaskParams(String workoutName, int workoutDate) {
            this.workoutName = workoutName;
            this.workoutDate = workoutDate;
        }
    }
    //https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
    private static class GetPrevWorkoutAsyncTask extends AsyncTask<MyTaskParams, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetPrevWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(MyTaskParams... params) {
            return workoutDao.getPrevWorkout(params[0].workoutName, params[0].workoutDate);
        }
    }
}
