package com.example.tamirmishali.trainingmanager.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractInfoValue;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

@Dao
public interface ExerciseAbstractOperationDao {
    @Insert
    long insert(ExerciseAbstractOperation exerciseAbstractOperation);

    @Update
    void update(ExerciseAbstractOperation exerciseAbstractOperation);

    @Delete
    void delete(ExerciseAbstractOperation exerciseAbstractOperation);

}
