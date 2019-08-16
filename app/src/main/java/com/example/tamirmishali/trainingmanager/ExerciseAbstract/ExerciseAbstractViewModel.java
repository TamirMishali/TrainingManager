package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Database.ExerciseAbstractRepository;

import java.util.ArrayList;
import java.util.List;

public class ExerciseAbstractViewModel extends AndroidViewModel {
    private ExerciseAbstractRepository repository;
    private LiveData<List<ExerciseAbstract>> allExerciseAbstracts;


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

}
