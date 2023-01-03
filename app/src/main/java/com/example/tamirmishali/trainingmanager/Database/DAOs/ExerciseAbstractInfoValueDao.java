package com.example.tamirmishali.trainingmanager.Database.DAOs;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractInfoValue;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractNickname;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

import java.util.List;
@Dao
public interface ExerciseAbstractInfoValueDao {
    @Insert
    long insert(ExerciseAbstractInfoValue exerciseAbstractInfoValue);

    @Update
    void update(ExerciseAbstractInfoValue exerciseAbstractInfoValue);

    @Delete
    void delete(ExerciseAbstractInfoValue exerciseAbstractInfoValue);



}
