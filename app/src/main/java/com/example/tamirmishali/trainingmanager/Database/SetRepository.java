package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import com.example.tamirmishali.trainingmanager.Database.DAOs.SetDao;
import com.example.tamirmishali.trainingmanager.Set.Set;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SetRepository {
    private SetDao setDao;
    private LiveData<List<Set>> allSets;
    //private static Set set = new Set();

    public SetRepository(Application application){
        RoutineDatabase database = RoutineDatabase.getInstance(application);
        setDao = database.setDao();
        allSets = setDao.getAllSets();
    }

    //-----Sets-----
    public void insert(Set set){
        new InsertSetAsyncTask(setDao).execute(set);
    }
    public void update(Set set){
        new UpdateSetAsyncTask(setDao).execute(set);
    }
    public void delete(Set set){
        new DeleteSetAsyncTask(setDao).execute(set);
    }
/*    public void deleteAllSets(int workout_id){
        new DeleteAllSetAsyncTask(setDao).execute(workout_id);
    }
    public Set getSetForExercise(int setAbstractId, int workoutId){
        try {
            set = new GetSetForWorkoutAsyncTask(setDao).execute(setAbstractId,workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return set;
    }

*/
    public LiveData<List<Set>> getAllSets(){
        return allSets;
    }
/*    public LiveData<List<Set>> getSetsForExercise(int exerciseId){
        return setDao.getSetsForExercise(exerciseId);
    }*/
/*    public List<Set> getSetsForExercises(int workoutId){
        List<Set> sets = new ArrayList<>();
        try {
            sets = new GetSetsForWorkoutAsyncTask(setDao).execute(workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sets;
    }*/
    public List<Set> getSetsForWorkout(int workoutId){
        List<Set> sets = new ArrayList<>();
        try {
            sets = new GetSetsForWorkoutAsyncTask(setDao).execute(workoutId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sets;
    }

    public List<Set> getSetsForExercise(int exerciseId){
        List<Set> sets = new ArrayList<>();
        try {
            sets = new GetSetsForExerciseAsyncTask(setDao).execute(exerciseId).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return sets;
    }

    //Set - AsyncTasks
    private static class InsertSetAsyncTask extends AsyncTask<Set, Void, Void>{
        private SetDao setDao;

        private InsertSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Set... sets) {
            setDao.insert(sets[0]);
            return null;
        }
    }
    private static class UpdateSetAsyncTask extends AsyncTask<Set, Void, Void>{
        private SetDao setDao;

        private UpdateSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Set... sets) {
            setDao.update(sets[0]);
            return null;
        }
    }
    private static class DeleteSetAsyncTask extends AsyncTask<Set, Void, Void>{
        private SetDao setDao;

        private DeleteSetAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Set... sets) {
            setDao.delete(sets[0]);
            return null;
        }
    }
    private static class GetSetsForWorkoutAsyncTask extends AsyncTask<Integer, Integer, List<Set>>{
        private SetDao setDao;

        private GetSetsForWorkoutAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected List<Set> doInBackground(Integer... values) {
            return setDao.getSetsForWorkout(values[0]);
        }

    }
    private static class GetSetsForExerciseAsyncTask extends AsyncTask<Integer, Integer, List<Set>>{
        private SetDao setDao;

        private GetSetsForExerciseAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected List<Set> doInBackground(Integer... values) {
            return setDao.getSetsForExercise(values[0]);
        }

    }

/*    private static class DeleteAllSetAsyncTask extends AsyncTask<Integer, Void, Void> {
        private SetDao setDao;

        private DeleteAllSetAsyncTask(SetDao setDao) {
            this.setDao = setDao;
        }

        @Override
        protected Void doInBackground(Integer... values) {
            setDao.deleteAllsets(values[0]);
            return null;
        }
    }*/

    //https://stackoverflow.com/questions/6053602/what-arguments-are-passed-into-asynctaskarg1-arg2-arg3
/*    private static class GetSetForWorkoutAsyncTask extends AsyncTask<Integer, Integer, Set>{
        private SetDao setDao;

        private GetSetForWorkoutAsyncTask(SetDao setDao){
            this.setDao = setDao;
        }

        @Override
        protected Set doInBackground(Integer... values) {
            return setDao.getSetForWorkout(values[0],values[1]);
        }

    }*/



}
