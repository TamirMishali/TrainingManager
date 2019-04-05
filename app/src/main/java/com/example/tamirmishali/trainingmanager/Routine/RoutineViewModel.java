package com.example.tamirmishali.trainingmanager.Routine;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.tamirmishali.trainingmanager.Database.RoutineRepository;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;

public class RoutineViewModel extends AndroidViewModel {
    private RoutineRepository repository;
    private LiveData<List<Routine>> allRoutines;
//    private LiveData<List<Workout>> allWorkouts;


    public RoutineViewModel(@NonNull Application application) {
        super(application);
        repository = new RoutineRepository(application);
        allRoutines = repository.getAllRoutines();
 //       allWorkouts = repository.getAllWorkouts();
    }

    public void insert(Routine routine){
        repository.insert(routine);
    }
    public void update(Routine routine){
        repository.update(routine);
    }
    public void delete(Routine routine){
        repository.delete(routine);
    }
    public void deleteAllRoutines(){
        repository.deleteAll();
    }
    public LiveData<List<Routine>> getAllRoutines(){
        return allRoutines;
    }
    /*public Routine getCurrentRoutine() {repository.getCurrentRoutine();}*/

/*

    public void insert(Workout workout){
        repository.insert(workout);
    }
    public void update(Workout workout){
        repository.update(workout);
    }
    public void delete(Workout workout){
        repository.delete(workout);
    }
    public void deleteAllWorkouts(){
        repository.deleteAllworkouts();
    }
    public LiveData<List<Workout>> getAllWorkouts(){
        return allWorkouts;
    }
    public LiveData<List<Workout>> getWorkoutsForRoutine(int id){ return repository.getWorkoutsForRoutine(id); }*/

}
