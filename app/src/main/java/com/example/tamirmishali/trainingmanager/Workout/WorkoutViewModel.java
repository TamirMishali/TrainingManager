package com.example.tamirmishali.trainingmanager.Workout;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Database.WorkoutRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        repository.deleteAllWorkouts();
    }
    public LiveData<List<Workout>> getAllWorkouts(){
        return allWorkouts;
    }
    public LiveData<List<Workout>> getWorkoutsForRoutine(int id){ return repository.getWorkoutsForRoutine(id); }
    public List<String> getMusselsInWorkout(int id) {return repository.getMusselsInWorkout(id);}
    public Workout getCurrentWorkout() { return repository.getCurrentWorkout();}
    public Workout getPrevWorkout(String workoutName, java.sql.Date workoutDate){return repository.getPrevWorkout(workoutName,workoutDate);}
    public Workout getWorkout(int workoutId){return repository.getWorkout(workoutId);}
    public Workout getLastWorkout(){return repository.getLastWorkout();}
    public Workout getPracticalWorkoutFromAbstract(int routineId, String workoutName) {
        return repository.getPracticalWorkoutFromAbstract(routineId,workoutName);}
    public Workout getAbstractWorkoutFromPractical(int routineId, String workoutName) {
        return repository.getAbstractWorkoutFromPractical(routineId,workoutName);}
    public Workout getNewestWorkout(int routineId, String workoutName){return repository.getNewestWorkout(routineId,workoutName);}

    public int getNewestRoutineId(){return repository.getNewestRoutineId();}

    public List<Workout> getLastPracticalWorkouts(){return repository.getLastPracticalWorkouts();}
    public List<Workout> getAbstractWorkoutsForRoutine(int routineId){return repository.getAbstractWorkoutsForRoutine(routineId);}

    public LiveData<List<Workout>> getPracticalWorkoutsForRoutineLiveData(int routineId){return repository.getPracticalWorkoutsForRoutineLiveData(routineId);}
    public List<Workout> getPracticalWorkoutsForRoutine(int routineId){return repository.getPracticalWorkoutsForRoutine(routineId);}
    public List<Workout> getWorkoutsForDialog(int routineId){
        List<Workout> result = new ArrayList<>();
        List<Workout> abstractWorkouts= getAbstractWorkoutsForRoutine(routineId);
        if (abstractWorkouts.isEmpty() || abstractWorkouts == null){
            return null;
        }

        for(int i = 0; i<abstractWorkouts.size(); i++){
            Workout workout = getPracticalWorkoutFromAbstract(routineId,abstractWorkouts.get(i).getWorkoutName());
            if(workout != null)
                result.add(workout);
            else
                result.add(abstractWorkouts.get(i));
        }

        Collections.sort(result,new SortByDate());

        return result;
    }

    class SortByDate implements Comparator<Workout>
    {
        // Used for sorting in ascending order of
        // roll number
        public int compare(Workout a, Workout b)
        {
            if((a.getDate() == null ||  a.getDate().toString() == "")||
                    (b.getDate() == null || b.getDate().toString()==""))
                return 0;
            else if(a.getDate().after(b.getDate()))
                return 1;
            else
                return -1;
        }
    }

}
