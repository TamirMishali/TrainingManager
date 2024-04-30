package com.example.tamirmishali.trainingmanager.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractInfo;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

@Dao
public interface ExerciseAbstractInfoDao {
    @Insert
    long insert(ExerciseAbstractInfo exerciseAbstractInfo);

    @Update
    void update(ExerciseAbstractInfo exerciseAbstractInfo);

    @Delete
    void delete(ExerciseAbstractInfo exerciseAbstractInfo);

}
