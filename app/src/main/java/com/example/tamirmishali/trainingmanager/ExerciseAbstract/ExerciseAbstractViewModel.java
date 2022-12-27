package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Database.ExerciseAbstractRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExerciseAbstractViewModel extends AndroidViewModel {
    private ExerciseAbstractRepository repository;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;

    // new
    private LiveData<List<ExerciseAbstractNickname>> nicknames;
    private LiveData<List<ExerciseAbstractOperation>> operations;
    private LiveData<List<ExerciseAbstractInfoValue>> info_values;

    public ExerciseAbstractViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseAbstractRepository(application);
//        allRoutines = repository.getAllRoutines();
        allExerciseAbstracts = repository.getAllExerciseAbstracts();
    }
    public void insert(ExerciseAbstract exerciseabstract){
        repository.insert(exerciseabstract);
    }
    public void update(ExerciseAbstract exerciseabstract){
        repository.update(exerciseabstract);
    }
    public void delete(ExerciseAbstract exerciseabstract){
        repository.delete(exerciseabstract);
    }
    public void deleteAllExerciseAbstracts(){
        repository.deleteAllExerciseAbstracts();
    }
    public LiveData<List<ExerciseAbstract>> getAllExerciseAbstracts(){
        return allExerciseAbstracts;
    }
    public LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int id){ return repository.getExerciseAbstractsForWorkout(id); }


    public ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId){return repository.getExerciseAbsFromId(exerciseAbsId);}
    public List<String> getMuscles(){return repository.getMuscles();}


    // new
    public LiveData<List<ExerciseAbstractInfoValue>> getAllExerciseAbstractInfoValues(){ return repository.getAllExerciseAbstractInfoValues();}
    public LiveData<List<ExerciseAbstractNickname>> getAllExerciseAbstractNicknames(){ return repository.getAllExerciseAbstractNicknames();}
    public LiveData<List<ExerciseAbstractOperation>> getAllExerciseAbstractOperations(){ return repository.getAllExerciseAbstractOperations();}

    public List<ExerciseAbstractInfo> getExerciseAbstractsInfo(int id){ return repository.getExerciseAbstractsInfo(id); }
    public List<ExerciseAbstractInfoValue> getExerciseAbstractsInfoValue(int id){ return repository.getExerciseAbstractsInfoValue(id); }
    public List<ExerciseAbstractOperation> getExerciseAbstractsOperations(int id){return repository.getExerciseAbstractsOperations(id);}
    public List<ExerciseAbstractNickname> getExerciseAbstractsOperationNicknames(int id){return repository.getExerciseAbstractsOperationNicknames(id);}
    /*TODO:
        - create ExerciseAbstractInfo class and ExerciseAbstractInfoValue
        - Insert functions to ExerciseAbstractDao and all the repository class
        -
*/

    private ExerciseAbstract FillExerciseAbstractIDs(ExerciseAbstract exerciseAbstract){
//        if (exerciseAbstract.getMuscle() == null){
//            Exception exception = new Exception("Muscle is missing");
//
//        }

        exerciseAbstract.setId_muscle(exerciseAbstract.getMuscle());

        return exerciseAbstract;
    }

}
