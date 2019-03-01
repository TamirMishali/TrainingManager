package com.example.tamirmishali.trainingmanager.Database.DAOs;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.tamirmishali.trainingmanager.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    void insert(Workout workout);

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

}
