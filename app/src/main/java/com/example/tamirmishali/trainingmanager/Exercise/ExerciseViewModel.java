package com.example.tamirmishali.trainingmanager.Exercise;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Database.ExerciseRepository;
import com.example.tamirmishali.trainingmanager.Database.ExerciseRepository;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import java.util.List;

public class ExerciseViewModel extends AndroidViewModel {
    private ExerciseRepository repository;
    private LiveData<List<Exercise>> allExercises;


    public ExerciseViewModel(@NonNull Application application) {
        super(application);
        repository = new ExerciseRepository(application);
//        allRoutines = repository.getAllRoutines();
        allExercises = repository.getAllExercises();
    }
    public void insert(Exercise exercise){
        repository.insert(exercise);
    }
    public void update(Exercise exercise){
        repository.update(exercise);
    }
    public void delete(Exercise exercise){
        repository.delete(exercise);
    }
    public void deleteAllExercises(int id_workout){
        repository.deleteAllExercises(id_workout);
    }
    public Exercise getExerciseForWorkout(int idExerciseAbstract, int workoutId) {
        return repository.getExerciseForWorkout(idExerciseAbstract,workoutId);
    }
    public LiveData<List<Exercise>> getAllExercises(){
        return allExercises;
    }
    public LiveData<List<Exercise>> getExercisesForWorkout(int id){ return repository.getExercisesForWorkout(id); }

}
