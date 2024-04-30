package com.example.tamirmishali.trainingmanager.Set;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "set_table",
        foreignKeys = {
        @ForeignKey(
                entity = Exercise.class,
                parentColumns = "id_exercise",
                childColumns = "id_exercise",
                onDelete = CASCADE
        )})

public class Set {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_set")
    private int id_set;

    @ColumnInfo(name = "id_exercise")
    private int id_exercise;

    @ColumnInfo(name = "weight")
    private double weight;

    @ColumnInfo(name = "reps")
    private int reps;

    // ------------Constructors ---------------
    public Set(){

    }

    public Set(int exerciseId, double weight, int reps){
        this.id_set = 0;
        this.id_exercise = exerciseId;
        this.weight = weight;
        this.reps = reps;
    }

    public Set(int id_set , int exerciseId, double weight, int reps){
        this.id_set = id_set;
        this.id_exercise = exerciseId;
        this.weight = weight;
        this.reps = reps;
    }

    //---------------Getters & Setters---------------

    public int getId_set() {
        return id_set;
    }

    public void setId_set(int id_set) {
        this.id_set = id_set;
    }

    public int getId_exercise() {
        return id_exercise;
    }

    public void setId_exercise(int id_exercise) {
        this.id_exercise = id_exercise;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
