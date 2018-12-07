package com.example.tamirmishali.trainingmanager;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import com.example.tamirmishali.trainingmanager.Database.RoutineRepository;
import java.util.List;

public class RoutineViewModel extends AndroidViewModel {
    private RoutineRepository repository;
    private LiveData<List<Routine>> allRoutines;


    public RoutineViewModel(@NonNull Application application) {
        super(application);
        repository = new RoutineRepository(application);
        allRoutines = repository.getAllRoutines();
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

    public void deleteAll(){
        repository.deleteAll();
    }

    public LiveData<List<Routine>> getAllRoutines(){
        return allRoutines;
    }

}
