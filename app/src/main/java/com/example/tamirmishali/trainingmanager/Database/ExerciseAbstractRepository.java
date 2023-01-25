package com.example.tamirmishali.trainingmanager.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractNicknameDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractOperationDao;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractNickname;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
//import kotlinx.coroutines.scheduling.Task;

public class ExerciseAbstractRepository {
    private ExerciseAbstractDao  exerciseAbstractDao;
    private ExerciseAbstractOperationDao exerciseAbstractOperationDao;
    private ExerciseAbstractNicknameDao exerciseAbstractNicknameDao;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;

    private ExecutorService executor;

    // Amazing clear explanation about LiveData - don't use AsyncTask with livedata. livedata should
    // only be used to view the data, not manipulate it:
    //https://stackoverflow.com/questions/51601046/should-i-make-asynctask-member-of-livedata-or-repository-class-as-replacement

    public ExerciseAbstractRepository(Application application){
        TrainingManagerDatabase database = TrainingManagerDatabase.getInstance(application);
        exerciseAbstractDao = database.exerciseAbstractDao();
        exerciseAbstractOperationDao = database.exerciseAbstractOperationDao();
        exerciseAbstractNicknameDao = database.exerciseAbstractNicknameDao();

        allExerciseAbstracts = exerciseAbstractDao.getAllExercisesAbstract();
//        allNicknames = exerciseAbstractDao.getAllExerciseAbstractNicknames();
//        allOperations = exerciseAbstractDao.getAllExerciseAbstractOperations();
//        allInfoValues = exerciseAbstractDao.getAllExerciseAbstractInfoValues();

        this.executor = Executors.newSingleThreadExecutor();
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


    // ------------------------- NEW functions -------------------------------------
    // REMEMBER: livedata doesn't need AsyncTask
/*    public LiveData<List<ExerciseAbstractInfoValue>> getAllExerciseAbstractInfoValues() {
        return allInfoValues;
    }
    public LiveData<List<ExerciseAbstractOperation>> getAllExerciseAbstractOperations() {
        return allOperations;
    }
    public LiveData<List<ExerciseAbstractNickname>> getAllExerciseAbstractNicknames() {
        return allNicknames;
    }
    public LiveData<List<ExerciseAbstractInfo>> getAllExerciseAbstractInfo() {
        return allInfo;
    }*/

    public int getExerciseAbstractInfoValueId(String value){
        int id = 0;
        try {
            id = new GetExerciseAbstractInfoValueIdAsyncTask(exerciseAbstractDao).execute(value).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }
    public int getExerciseAbstractOperationId(String id_muscle, String operation){
        int id = 0;
        try {
            id = new GetExerciseAbstractOperationIdAsyncTask(exerciseAbstractDao).execute(id_muscle, operation).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }
    public int getExerciseAbstractNicknameId(String nickname){
        int id = 0;
        try {
            id = new GetExerciseAbstractNicknameIdAsyncTask(exerciseAbstractDao).execute(nickname).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    public String getExerciseAbstractInfoValueValue(int id){
        String value = new String();
        try {
            value = new GetExerciseAbstractInfoValueValueAsyncTask(exerciseAbstractDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return value;
    }
    public String getExerciseAbstractOperationOperation(int id){
        String operation = new String();
        try {
            operation = new GetExerciseAbstractOperationOperationAsyncTask(exerciseAbstractDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return operation;
    }
    public String getExerciseAbstractNicknameNickname(int id){
        String nickname = new String();
        try {
            nickname = new GetExerciseAbstractNicknameNicknameAsyncTask(exerciseAbstractDao).execute(id).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return nickname;
    }

    public List<String> getExerciseAbstractInfoValueValueByHeader(String info_header_name){
        List<String> values = new ArrayList<>();
        try {
            values = new GetExerciseAbstractInfoValueValueByHeaderAsyncTask(exerciseAbstractDao).execute(info_header_name).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return values;
    }
    public List<String> getExerciseAbstractOperationByMuscleId(int id_muscle){
        List<String> operations = new ArrayList<>();
        try {
            operations = new GetExerciseAbstractOperationByMuscleIdAsyncTask(exerciseAbstractDao).execute(id_muscle).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return operations;
    }
    public List<String> getExerciseAbstractNicknameByOperationId(int id_operation){
            List<String> nicknames = new ArrayList<>();
            try {
                nicknames = new GetExerciseAbstractNicknameByOperationIdAsyncTask(exerciseAbstractDao).execute(id_operation).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return nicknames;
    }

    public ExerciseAbstractNickname getExerciseAbstractNickname(int op_id, String nickname){
        Future<ExerciseAbstractNickname> future = executor.submit(new Callable<ExerciseAbstractNickname>() {
            @Override
            public ExerciseAbstractNickname call() throws Exception {
                return exerciseAbstractDao.getExerciseAbstractNickname(op_id, nickname);
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }

    }


    public int getExerciseAbstractId(ExerciseAbstract exerciseAbstract){
        int id = 0;
        try {
            id = new GetExerciseAbstractIdAsyncTask(exerciseAbstractDao).execute(exerciseAbstract).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return id;
    }

    // Operation:
    public void insert(ExerciseAbstractOperation exerciseAbstractOperation){
//        new InsertExerciseAbstractOperationAsyncTask(exerciseAbstractOperationDao, exerciseAbstractNicknameDao).execute(exerciseAbstractOperation);
        new InsertExerciseAbstractOperationAsyncTask(exerciseAbstractOperationDao).execute(exerciseAbstractOperation);
    }

    // nickname - this is not needed cuz i can put nickname as a variable inside operation - WRONG
    public void insert(ExerciseAbstractNickname exerciseAbstractNickname){
        new InsertExerciseAbstractNicknameAsyncTask(exerciseAbstractNicknameDao).execute(exerciseAbstractNickname);
    }

/*    public void insertOperation(String id_muscle, String operation){
        new InsertOperationAsyncTask(exerciseAbstractDao).execute(id_muscle, operation);
    }
    public void insertNickname(String id_operation, String nickname){
        new InsertNicknameAsyncTask(exerciseAbstractDao).execute(id_operation, nickname);
    }*/

    // --------------------------- ExerciseAbstract - AsyncTasks -----------------------------------
    private static class InsertExerciseAbstractAsyncTask extends AsyncTask<ExerciseAbstract, Void, Long>{
        private ExerciseAbstractDao exerciseAbstractDao;
        long insertedEAid;

        private InsertExerciseAbstractAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Long doInBackground(ExerciseAbstract... exerciseAbstracts) {
            this.insertedEAid = exerciseAbstractDao.insert(exerciseAbstracts[0]);
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



    // ------ NEW - AsyncTasks ------
    // Get Ids
    private static class GetExerciseAbstractInfoValueIdAsyncTask extends AsyncTask<String, Integer, Integer>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractInfoValueIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Integer doInBackground(String... params) {
            return exerciseAbstractDao.getExerciseAbstractInfoValueId(params[0]);
        }
    }
    private static class GetExerciseAbstractOperationIdAsyncTask extends AsyncTask<String, Integer, Integer>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractOperationIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Integer doInBackground(String... params) {
            return exerciseAbstractDao.getExerciseAbstractOperationId(params[0], params[1]);
        }
    }
    private static class GetExerciseAbstractNicknameIdAsyncTask extends AsyncTask<String, Integer, Integer>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractNicknameIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Integer doInBackground(String... params) {
            return exerciseAbstractDao.getExerciseAbstractNicknameId(params[0]);
        }
    }

    // Get String Values (infoValue, Operation, Nickname)
    private static class GetExerciseAbstractInfoValueValueAsyncTask extends AsyncTask<Integer, Integer, String>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractInfoValueValueAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected String doInBackground(Integer... params) {
            return exerciseAbstractDao.getExerciseAbstractInfoValueValue(params[0]);
        }
    }
    private static class GetExerciseAbstractOperationOperationAsyncTask extends AsyncTask<Integer, Integer, String>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractOperationOperationAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected String doInBackground(Integer... params) {
            return exerciseAbstractDao.getExerciseAbstractOperationOperation(params[0]);
        }
    }
    private static class GetExerciseAbstractNicknameNicknameAsyncTask extends AsyncTask<Integer, Integer, String>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractNicknameNicknameAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected String doInBackground(Integer... params) {
            return exerciseAbstractDao.getExerciseAbstractNicknameNickname(params[0]);
        }
    }

    // Get String array of values from tables (infoValue, Operation, Nickname)
    private static class GetExerciseAbstractInfoValueValueByHeaderAsyncTask extends AsyncTask<String, Integer, List<String>>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractInfoValueValueByHeaderAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected List<String> doInBackground(String... params) {
            return exerciseAbstractDao.getExerciseAbstractInfoValueValueByHeader(params[0]);
        }
    }
    private static class GetExerciseAbstractOperationByMuscleIdAsyncTask extends AsyncTask<Integer, Integer, List<String>>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractOperationByMuscleIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected List<String> doInBackground(Integer... params) {
            return exerciseAbstractDao.getExerciseAbstractOperationByMuscleId(params[0]);
        }
    }
    private static class GetExerciseAbstractNicknameByOperationIdAsyncTask extends AsyncTask<Integer, Integer, List<String>>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractNicknameByOperationIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected List<String> doInBackground(Integer... params) {
            return exerciseAbstractDao.getExerciseAbstractNicknameByOperationId(params[0]);
        }
    }

//    private static class GetExerciseAbstractNicknameAsyncTask extends

    private static class InsertExerciseAbstractOperationAsyncTask extends AsyncTask<ExerciseAbstractOperation, Void, Long>{
        private final ExerciseAbstractOperationDao exerciseAbstractOperationDao;

        private InsertExerciseAbstractOperationAsyncTask(ExerciseAbstractOperationDao exerciseAbstractOperationDao){
            this.exerciseAbstractOperationDao = exerciseAbstractOperationDao;
        }

        @Override
        protected Long doInBackground(ExerciseAbstractOperation... exerciseAbstractOperations) {
            exerciseAbstractOperationDao.insert(exerciseAbstractOperations[0]);
            return null;
        }

    }

    private static class InsertExerciseAbstractNicknameAsyncTask extends AsyncTask<ExerciseAbstractNickname, Void, Long>{
        private ExerciseAbstractNicknameDao exerciseAbstractNicknameDao;

        private InsertExerciseAbstractNicknameAsyncTask(ExerciseAbstractNicknameDao exerciseAbstractNicknameDao){
            this.exerciseAbstractNicknameDao = exerciseAbstractNicknameDao;
        }

        @Override
        protected Long doInBackground(ExerciseAbstractNickname... exerciseAbstractNicknames) {
            exerciseAbstractNicknameDao.insert(exerciseAbstractNicknames[0]);
            return null;
        }

    }

    private static class GetExerciseAbstractIdAsyncTask extends AsyncTask<ExerciseAbstract, Integer, Integer>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetExerciseAbstractIdAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Integer doInBackground(ExerciseAbstract... params) {
            ExerciseAbstract ea = params[0];
            if (ea.getId_nickname() != null) {
                return exerciseAbstractDao.getExerciseAbstractId_inner(
                    ea.getId_muscle(),
                    ea.getId_operation(),
                    ea.getId_nickname(),
                    ea.getId_load_type(),
                    ea.getId_position(),
                    ea.getId_angle(),
                    ea.getId_grip_width(),
                    ea.getId_thumbs_direction(),
                    ea.getId_separate_sides());
            }
            else{
                return exerciseAbstractDao.getExerciseAbstractId_inner(
                    ea.getId_muscle(),
                    ea.getId_operation(),
                    ea.getId_load_type(),
                    ea.getId_position(),
                    ea.getId_angle(),
                    ea.getId_grip_width(),
                    ea.getId_thumbs_direction(),
                    ea.getId_separate_sides());
            }
        }
    }


/*
    private static class InsertOperationAsyncTask extends AsyncTask<String, Integer, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private InsertOperationAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            return exerciseAbstractDao.insertOperation(params[0], params[1]);
        }
    }

    private static class InsertNicknameAsyncTask extends AsyncTask<String, Integer, Void>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private InsertNicknameAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected Void doInBackground(String... params) {
            return exerciseAbstractDao.insertNickname(params[0], params[1]);
        }
    }

*/

}


/*    private static class GetMusclesAsyncTask extends AsyncTask<Void, Void, List<String>>{
        private ExerciseAbstractDao exerciseAbstractDao;

        private GetMusclesAsyncTask(ExerciseAbstractDao exerciseAbstractDao){
            this.exerciseAbstractDao = exerciseAbstractDao;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            return exerciseAbstractDao.getMuscles();
        }

    }*/