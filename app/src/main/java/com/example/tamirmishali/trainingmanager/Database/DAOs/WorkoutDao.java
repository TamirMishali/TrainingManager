package com.example.tamirmishali.trainingmanager.Database.DAOs;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout_table")
    void deleteAllworkouts();

    @Query("SELECT * from workout_table Order by date DESC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workout_table WHERE id_routine=:id_routine")
    LiveData<List<Workout>> getWorkoutsForRoutine(final int id_routine);

    @Query("select EA.muscleGroup " +
            "from exerciseabs_table EA inner join exercise_table E ON EA.id_exerciseabs  = E.id_exerciseabs " +
            "where E.id_workout IN (:workoutid)")
    List<String> getMusselsInWorkout(final int workoutid);

    @Query("select W.* " +
            "from workout_table W inner join routine_table R ON W.id_routine = R.id " +
            "where R.id IN (select id " +
            "               from routine_table " +
            "               Order BY date desc " +
            "               limit 1) " +
            "limit 1")
    Workout getCurrentWorkout();



}
