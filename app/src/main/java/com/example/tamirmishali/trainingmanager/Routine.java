package com.example.tamirmishali.trainingmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;



@Entity(tableName = "routine_table")
public class Routine {

    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "date")
    private java.sql.Date routineDate;

    //private ArrayList<workout> m_workoutsArrayList;
    @ColumnInfo(name = "exercise")
    private String exercise;



    //------------------------Constructor----------------------
    public Routine(String exercise, String date) {
        this.uid = 0;
        java.sql.Date d = java.sql.Date.valueOf(date);
        this.routineDate = d;
        this.exercise = exercise;
    }

    public Routine() {
    }

    //------------------------Getters and Setters----------------------
    public String getExercise() { return exercise; }
    public void setExercise(String exercise) { this.exercise = exercise; }

    public int getUid() { return uid; }
    public void setUid(int uid) {this.uid = uid; }

    public java.sql.Date getRoutineDate() {return routineDate; }
    public void setRoutineDate(java.sql.Date date) { this.routineDate = date; }

}



