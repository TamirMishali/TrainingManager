package com.example.tamirmishali.trainingmanager.Routine;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Database.RoutineRepository;

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

    public LiveData<List<Routine>> getObservableProduct() {
        return allRoutines;
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
        if (allRoutines == null){
            allRoutines = repository.getAllRoutines();
        }
        return allRoutines;
    }

    public Routine getRoutine(int routineId) {return repository.getRoutine(routineId);}
    public Routine getFirstRoutine() {return repository.getFirstRoutine();}

}
