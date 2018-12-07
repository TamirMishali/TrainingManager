package com.example.tamirmishali.trainingmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "routine_table")
public class Routine {

    @PrimaryKey
    private int uid;

    @ColumnInfo(name = "date")
    private String routineDate;

    //private ArrayList<workout> m_workoutsArrayList;
    @ColumnInfo(name = "exercise")
    private String exercise;


    //------------------------Constructor----------------------


    public Routine(String routineDate, String exercise) {
        this.routineDate = routineDate;
        this.exercise = exercise;
    }

    //------------------------Getters and Setters----------------------
    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getRoutineDate() {
        return routineDate;
    }

    public void setRoutineDate(String date) {
        this.routineDate = date;
    }

}



