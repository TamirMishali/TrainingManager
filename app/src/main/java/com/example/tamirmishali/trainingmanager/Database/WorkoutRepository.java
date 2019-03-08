package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;

public class WorkoutRepository {
    private WorkoutDao  workoutDao;
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
    public LiveData<List<Workout>> getWorkoutsForRoutine(int routineid){
        return workoutDao.getWorkoutsForRoutine(routineid);
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
}
