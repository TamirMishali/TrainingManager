package com.example.tamirmishali.trainingmanager.Set;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Database.SetRepository;
import java.util.List;

public class SetViewModel extends AndroidViewModel {
    private SetRepository repository;
    private LiveData<List<Set>> allSets;


    public SetViewModel(@NonNull Application application) {
        super(application);
        repository = new SetRepository(application);
//        allRoutines = repository.getAllRoutines();
        allSets = repository.getAllSets();
    }
    public void insert(Set set){
        repository.insert(set);
    }
    public void update(Set set){
        repository.update(set);
    }
    public void delete(Set set){
        repository.delete(set);
    }
/*    public void deleteAllSets(int id_workout){
        repository.deleteAllSets(id_workout);
    }*/
/*    public Set getSetForWorkout(int idSetAbstract, int workoutId) {
        return repository.getSetForWorkout(idSetAbstract,workoutId);
    }*/
    public LiveData<List<Set>> getAllSets(){return allSets; }
    //public LiveData<List<Set>> getSetsForExercise(int exercise_id){ return repository.getSetsForExercise(exercise_id); }
    public List<Set> getSetsForWorkout(int workoutId){ return repository.getSetsForWorkout(workoutId);}
    public List<Set> getUnfilledSetsForWorkout(int workoutId){return repository.getUnfilledSetsFromWorkout(workoutId);}
    public List<Set> getSetsForExercise(int exerciseId){return repository.getSetsForExercise(exerciseId);}

}
