package com.example.tamirmishali.trainingmanager.Routine;

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
    @ColumnInfo(name = "name") //change to name next time changing the DB
    private String routineName;



    //------------------------Constructor----------------------
    public Routine(String routineName, String date) {
        this.uid = 0;
        java.sql.Date d = java.sql.Date.valueOf(date);
        this.routineDate = d;
        this.routineName = routineName;
    }

    public Routine() {
    }

    //------------------------Getters and Setters----------------------
    public String getRoutineName() { return routineName; }
    public void setRoutineName(String routineName) { this.routineName = routineName; }

    public int getUid() { return uid; }
    public void setUid(int uid) {this.uid = uid; }

    public java.sql.Date getRoutineDate() {return routineDate; }
    public void setRoutineDate(java.sql.Date date) { this.routineDate = date; }

}



