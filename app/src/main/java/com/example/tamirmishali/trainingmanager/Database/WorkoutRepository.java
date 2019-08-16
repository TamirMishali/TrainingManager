package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.SetDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;



public class WorkoutRepository {
    private WorkoutDao workoutDao;
    private ExerciseDao exerciseDao;
    private ExerciseAbstractDao exerciseAbstractDao;
    private SetDao setDao;
    private LiveData<List<Workout>> allWorkouts;

    public WorkoutRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        workoutDao = database.workoutDao();
        exerciseDao = database.exerciseDao();
        exerciseAbstractDao = database.exerciseAbstractDao();
        setDao = database.setDao();
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
    public void deleteAllWorkouts(){
        new DeleteAllWorkoutAsyncTask(workoutDao).execute();
    }
    public LiveData<List<Workout>> getAllWorkouts(){
        return allWorkouts;
    }
    public LiveData<List<Workout>> getWorkoutsForRoutine(int routineId){
        return workoutDao.getWorkoutsForRoutine(routineId);
    }
    public LiveData<List<Workout>> getPracticalWorkoutsForRoutineLiveData(int routineId){
        return workoutDao.getPracticalWorkoutsForRoutineLiveData(routineId);
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
    public Workout getPrevWorkout(String workoutName, java.sql.Date workoutDate){
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
    public Workout getWorkout(int workoutId){
        Workout workout = new Workout();
        try {
            workout = new GetWorkoutAsyncTask(workoutDao,exerciseDao,exerciseAbstractDao,setDao).execute(workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }
    public Workout getLastWorkout(){
        Workout workout = new Workout();
        try {
            workout = new GetLastWorkoutAsyncTask(workoutDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }
    public Workout getPracticalWorkoutFromAbstract(int routineId, String workoutName){
        MyTaskParams2 params = new MyTaskParams2(routineId, workoutName);
        Workout workout = new Workout();
        try {
            workout = new GetPracticalWorkoutFromAbstractAsyncTask(workoutDao).execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }
    public Workout getAbstractWorkoutFromPractical(int routineId, String workoutName){
        MyTaskParams2 params = new MyTaskParams2(routineId, workoutName);
        Workout workout = new Workout();
        try {
            workout = new GetAbstractWorkoutFromPracticalAsyncTask(workoutDao).execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }
    public Workout getNewestWorkout(int routineId, String workoutName){
        MyTaskParams2 params = new MyTaskParams2(routineId, workoutName);
        Workout workout = new Workout();
        try {
            workout = new GetNewestWorkoutAsyncTask(workoutDao).execute(params).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workout;
    }

    public List<Workout> getLastPracticalWorkouts(){
        List<Workout> workoutList = new ArrayList<>();
        try {
            workoutList = new GetLastPracticalWorkoutsAsyncTask(workoutDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workoutList;
    }
    public List<Workout> getAbstractWorkoutsForRoutine(int routineId) {
        List<Workout> workoutList = new ArrayList<>();
        try {
            workoutList = new GetAbstractWorkoutsForRoutineAsyncTask(workoutDao).execute(routineId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workoutList;
    }
    public List<Workout> getPracticalWorkoutsForRoutine(int routineId){
        List<Workout> workoutList = new ArrayList<>();
        try {
            workoutList = new GetPracticalWorkoutsForRoutineAsyncTask(workoutDao).execute(routineId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return workoutList;
    }

    public int getNewestRoutineId(){
        int routineId = 0;
        try {
            routineId = new GetNewestRoutineIdAsyncTask(workoutDao).execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return routineId;
    }


    //Workout - AsyncTasks
    private static class InsertWorkoutAsyncTask extends AsyncTask<Workout, Void, Void>{
        private WorkoutDao workoutDao;

        private InsertWorkoutAsyncTask(WorkoutDao workoutDao ){
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
            workoutDao.deleteAllWorkouts();
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
    private static class GetNewestRoutineIdAsyncTask extends AsyncTask<Void, Void, Integer>{
        private WorkoutDao workoutDao;

        private GetNewestRoutineIdAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return workoutDao.getNewestRoutineId();
        }
    }

    //https://stackoverflow.com/questions/12069669/how-can-you-pass-multiple-primitive-parameters-to-asynctask
    private static class MyTaskParams {
        String workoutName;
        java.sql.Date workoutDate;

        MyTaskParams(String workoutName, java.sql.Date workoutDate) {
            this.workoutName = workoutName;
            this.workoutDate = workoutDate;
        }
    }
    private static class MyTaskParams2 {
        int routineId;
        String workoutName;

        MyTaskParams2(int routineId,String workoutName) {
            this.workoutName = workoutName;
            this.routineId = routineId;
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
            //Workout workout = workoutDao.getPrevWorkout(params[0].workoutName, params[0].workoutDate);
            //fillWorkout(workoutDao.getPrevWorkout(params[0].workoutName, params[0].workoutDate).getId(),workoutDao, exerciseDao,exerciseAbstractDao,setDao);
            return workoutDao.getPrevWorkout(params[0].workoutName, params[0].workoutDate);
        }
    }
    private static class GetPracticalWorkoutFromAbstractAsyncTask extends AsyncTask<MyTaskParams2, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetPracticalWorkoutFromAbstractAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(MyTaskParams2... params) {
            return workoutDao.getPracticalWorkoutFromAbstract(params[0].routineId, params[0].workoutName);
        }
    }
    private static class GetAbstractWorkoutFromPracticalAsyncTask extends AsyncTask<MyTaskParams2, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetAbstractWorkoutFromPracticalAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(MyTaskParams2... params) {
            return workoutDao.getAbstractWorkoutFromPractical(params[0].routineId, params[0].workoutName);
        }
    }
    private static class GetNewestWorkoutAsyncTask extends AsyncTask<MyTaskParams2, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetNewestWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(MyTaskParams2... params) {
            return workoutDao.getNewestWorkout(params[0].routineId, params[0].workoutName);
        }
    }

    private static class GetWorkoutAsyncTask extends AsyncTask<Integer, Void, Workout>{
        private WorkoutDao workoutDao;
        private ExerciseDao exerciseDao;
        private ExerciseAbstractDao exerciseAbstractDao;
        private SetDao setDao;

        private GetWorkoutAsyncTask(WorkoutDao workoutDao, ExerciseDao exerciseDao,
                                    ExerciseAbstractDao exerciseAbstractDao,
                                    SetDao setDao){
            this.workoutDao = workoutDao;
            this.exerciseDao = exerciseDao;
            this.exerciseAbstractDao = exerciseAbstractDao;
            this.setDao = setDao;
        }

        @Override
        protected Workout doInBackground(Integer... params) {
            return fillWorkout(params[0],workoutDao, exerciseDao,exerciseAbstractDao,setDao);
        }
    }
    private static class GetPracticalWorkoutsForRoutineAsyncTask extends AsyncTask<Integer, Void, List<Workout>>{
        private WorkoutDao workoutDao;

        private GetPracticalWorkoutsForRoutineAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected List<Workout> doInBackground(Integer... params) {
            return workoutDao.getPracticalWorkoutsForRoutine(params[0]);
        }
    }
    private static class GetAbstractWorkoutsForRoutineAsyncTask extends AsyncTask<Integer, Void, List<Workout>>{
        private WorkoutDao workoutDao;

        private GetAbstractWorkoutsForRoutineAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected List<Workout> doInBackground(Integer... params) {
            return workoutDao.getAbstractWorkoutsForRoutine(params[0]);
        }
    }

    private static class GetLastWorkoutAsyncTask extends AsyncTask<Void, Void, Workout>{
        private WorkoutDao workoutDao;

        private GetLastWorkoutAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected Workout doInBackground(Void... voids) {
            return workoutDao.getLastWorkout();
        }
    }
    private static class GetLastPracticalWorkoutsAsyncTask extends AsyncTask<Void, Void, List<Workout>>{
        private WorkoutDao workoutDao;

        private GetLastPracticalWorkoutsAsyncTask(WorkoutDao workoutDao){
            this.workoutDao = workoutDao;
        }

        @Override
        protected List<Workout> doInBackground(Void... voids) {
            return workoutDao.getLastPracticalWorkouts();
        }
    }


    private static Workout fillWorkout(int id,WorkoutDao workoutDao, ExerciseDao exerciseDao,
                             ExerciseAbstractDao exerciseAbstractDao,
                             SetDao setDao){
        Workout workout = workoutDao.getWorkout(id);
        List<Exercise> exercises = exerciseDao.getExercisesForWorkout(workout.getId());
        Iterator<Exercise> exerciseIterator = exercises.iterator();

        Exercise exercise;
        while (exerciseIterator.hasNext()) {
            exercise = exerciseIterator.next();
            exercise.setExerciseAbstract(exerciseAbstractDao.getExerciseAbsFromId(exercise.getId_exerciseabs()));
            exercise.setSets(setDao.getSetsForExercise(exercise.getId()));
        }
        workout.setExercises(exercises);

        return workout;
    }


}
