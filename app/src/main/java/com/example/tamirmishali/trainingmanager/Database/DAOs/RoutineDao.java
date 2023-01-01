package com.example.tamirmishali.trainingmanager.Database.DAOs;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Routine.Routine;

import java.util.List;

@Dao
public interface RoutineDao{
    //@Insert
    //void insert(Routine routine);

    @Insert
    long insert(Routine routine);

    @Update
    void update(Routine routine);

    @Delete
    void delete(Routine routine);

    @Query("DELETE FROM routine_table")
    void deleteAll();

    @Query("SELECT * from routine_table Order by date DESC")
    LiveData<List<Routine>> getAllRoutines();

    @Query("select * from routine_table where id = :id_routine")
    Routine getRoutine(int id_routine);

    @Query("select * from routine_table order by date desc limit 1")
    Routine getFirstRoutine();

    /*@Query("SELECT * from routine_table Order by date DESC")
    List<Routine> getAllRoutinesNLD();*/

/*    @Query("SELECT * from routine_table Order by date DESC")
    Routine getCurrentRoutine();*/

}

