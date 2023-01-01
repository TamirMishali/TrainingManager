package com.example.tamirmishali.trainingmanager.Workout;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Routine.Routine;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;


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
    private java.sql.Date date;

    @Ignore
    List<Exercise> exercises;

    public Workout(){//-----------------------------------complete later

    }

    /*When Abstract workout, Date=null. if real workout Date=currentDate */
    public Workout(int id, int id_routine, String name, String date, boolean abstractWorkout) {
        this.id = id;
        this.id_routine = id_routine;
        this.name = name;
        if (abstractWorkout){
            this.date = null; }
        else {
            java.sql.Date d = java.sql.Date.valueOf(date);
            this.date  = d;
        }
    }

    public Workout(int id, int id_routine, String name, boolean abstractWorkout) {
        this.id = id;
        this.id_routine = id_routine;
        this.name = name;
        if (abstractWorkout){
            this.date = null; }
        else {
            Calendar calendar = Calendar.getInstance();
            this.date  = new java.sql.Date(calendar.getTime().getTime());
        }
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

    public Workout(Workout workout){
        this.id = workout.getId();
        this.id_routine = workout.getId_routine();
        this.name = workout.getName();
        this.date = workout.getDate();
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

    public List<Exercise> getExercises(){return exercises;}

    public void setExercises(List<Exercise> exercises) {this.exercises = exercises;}

    //ArrayList<Exercise> exercises; //-------------for now, late we will try the adapterJson thing
}


