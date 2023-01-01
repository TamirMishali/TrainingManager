package com.example.tamirmishali.trainingmanager.Database.DAOs;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Set.Set;


import java.util.List;

@Dao
public interface SetDao {
    @Insert
    long insert(Set set);

    @Update
    void update(Set set);

    @Delete
    void delete(Set set);

    @Query("SELECT * from set_table Order by id_set DESC")
    LiveData<List<Set>> getAllSets();


    @Query("SELECT * FROM set_table WHERE id_exercise IN (select id_exercise from exercise_table where id_workout=:workoutId)")
    List<Set> getSetsForWorkout(int workoutId);

    @Query("SELECT *" +
            "FROM set_table " +
            "WHERE id_exercise in( " +
            "   SELECT id_exercise " +
            "   from exercise_table " +
            "   where id_workout=:workoutId)" +
            "   and (reps = '-1')")
    List<Set> getUnfilledSetsFromWorkout(int workoutId);

    @Query("SELECT * FROM set_table WHERE id_exercise=:id_exercise")
    List<Set> getSetsForExercise(final int id_exercise);



}
