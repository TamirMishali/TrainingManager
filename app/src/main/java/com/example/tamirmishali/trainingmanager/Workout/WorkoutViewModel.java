package com.example.tamirmishali.trainingmanager.Workout;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Database.WorkoutRepository;

import java.util.List;

public class WorkoutViewModel extends AndroidViewModel {
    private WorkoutRepository repository;
    private LiveData<List<Workout>> allWorkouts;


    public WorkoutViewModel(@NonNull Application application) {
        super(application);
        repository = new WorkoutRepository(application);
//        allRoutines = repository.getAllRoutines();
        allWorkouts = repository.getAllWorkouts();
    }

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
    public LiveData<List<Workout>> getWorkoutsForRoutine(int id){ return repository.getWorkoutsForRoutine(id); }

}
