package com.example.tamirmishali.trainingmanager.Workout;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.example.tamirmishali.trainingmanager.Routine.Routine;

import java.sql.Date;
import java.util.Calendar;
import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "workout_table",
        foreignKeys = @ForeignKey(entity = Routine.class,
            parentColumns = "id",
            childColumns = "id_routine",
            onDelete = CASCADE))

public class Workout {

    @NonNull
    @ColumnInfo(name = "id_workout")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_routine")
    private int id_routine;

    @ColumnInfo(name = "name")
    private String name; //=routineDate_Char(A/B/C)

    @ColumnInfo(name = "date")
    private Date date;

    public Workout(){//-----------------------------------complete later

    }

    /*When Abstract workout, Date=null. if real workout Date=currentDate */
    public Workout(int id_routine, String name, String date, boolean abstractWorkout) {
        this.id = 0;
        this.id_routine = id_routine;
        this.name = name;
        if (abstractWorkout){
            this.date = null; }
        else {
            java.sql.Date d = java.sql.Date.valueOf(date);
            this.date  = d;
        }


    }
    public Workout(int id_routine, String name, boolean abstractWorkout) {
        this.id = 0;
        this.id_routine = id_routine;
        this.name = name;
        if (abstractWorkout){
            this.date = null; }
        else {
            Calendar calendar = Calendar.getInstance();
            this.date  = new java.sql.Date(calendar.getTime().getTime());
        }
    }

    public int getId() {return id;}

    public void setId(int id) { this.id = id; }

    public int getId_routine() { return id_routine; }

    public void setId_routine(int id_routine) { this.id_routine = id_routine; }

    public String getWorkoutName() { return name; }

    public void setWorkoutName(String name) { this.name = name; }

    public Date getWorkoutDate() { return date; }

    public void setWorkoutDate(Date date) { this.date = date; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    //ArrayList<Exercise> exercises; //-------------for now, late we will try the adapterJson thing
}

