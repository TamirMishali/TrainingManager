package com.example.tamirmishali.trainingmanager.Database.DAOs;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ExerciseAbstractDao {
    @Insert
    long insert(ExerciseAbstract exerciseAbstract);

    @Update
    void update(ExerciseAbstract exerciseAbstract);

    @Delete
    void delete(ExerciseAbstract exerciseAbstract);

    @Query("DELETE FROM exerciseabs_table")
    void deleteAllexerciseAbstracts();

//    @Query("SELECT * from exerciseabs_table Order by id_exerciseabs DESC")
    @Query("SELECT * from exerciseabs_table Order by muscleGroup, name")
    LiveData<List<ExerciseAbstract>> getAllExercisesAbstract();

    @Query("select EA.id_exerciseabs, EA.name, EA.description, EA.muscleGroup " +
            "from exerciseabs_table EA inner join exercise_table E ON EA.id_exerciseabs  = E.id_exerciseabs " +
            "where E.id_workout IN (:workoutId)")
    LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int workoutId);

    @Query("select * from exerciseabs_table where id_exerciseabs=:exerciseAbsId")
    ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId);

    @Query("select distinct muscleGroup from exerciseabs_table order by muscleGroup asc")
    List<String> getMuscles();

/*    @Query("SELECT * FROM exerciseabs_table WHERE id_workout=:id_workout")
    LiveData<List<ExerciseAbstract>> getExercisesForWorkout(final int id_workout);*/

}
