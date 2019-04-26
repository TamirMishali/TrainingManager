package com.example.tamirmishali.trainingmanager.Database.DAOs;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("DELETE FROM exercise_table where id_workout=:id_workout")
    void deleteAllexercises(int id_workout);

    @Query("SELECT * from exercise_table where (id_exerciseabs =:id_exerciseabs) and (id_workout =:id_workout)")
    Exercise getExerciseForWorkout(final int id_exerciseabs,final int id_workout);

    @Query("SELECT * from exercise_table Order by id_exercise DESC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercise_table WHERE id_workout=:id_workout")
    LiveData<List<Exercise>> getExercisesForWorkout_O(final int id_workout);

    @Query("SELECT * FROM exercise_table WHERE id_workout=:id_workout")
    List<Exercise> getExercisesForWorkout(final int id_workout);



}
