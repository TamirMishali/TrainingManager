package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "exerciseabs_table")
public class ExerciseAbstract {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_exerciseabs")
    private int id;

    @ColumnInfo(name ="name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "muscleGroup")
    private String muscleGroup;


    // ------------Constructors ---------------
    //For new ExerciseAbs
    public ExerciseAbstract(String name, String description, String mussleGroup) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.muscleGroup = mussleGroup;
    }

    //For existing ExerciseAbs
    public ExerciseAbstract(int id, String name, String description, String mussleGroup) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.muscleGroup = mussleGroup;
    }

    public ExerciseAbstract(){

    }

    //---------------------Getters & Setters---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMuscleGroup() {
        return muscleGroup;
    }

    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
/*    @ColumnInfo(name = "reps")
    int[] reps = new int[5]; //maximum 5 sets

    @ColumnInfo(name = "isoriginal")
    Boolean isOriginal;*/

}
