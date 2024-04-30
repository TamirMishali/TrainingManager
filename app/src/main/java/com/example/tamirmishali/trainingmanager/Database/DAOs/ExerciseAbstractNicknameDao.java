package com.example.tamirmishali.trainingmanager.Database.DAOs;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractNickname;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

@Dao
public interface ExerciseAbstractNicknameDao {
    @Insert
    long insert(ExerciseAbstractNickname exerciseAbstractNickname);

    @Update
    void update(ExerciseAbstractNickname exerciseAbstractNickname);

    @Delete
    void delete(ExerciseAbstractNickname exerciseAbstractNickname);

}
