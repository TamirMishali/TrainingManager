package com.example.tamirmishali.trainingmanager.Exercise;

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

    @ColumnInfo(name = "mussleGroup")
    private String mussleGroup;


    // ------------Constructors ---------------
    public ExerciseAbstract(String name, String description, String mussleGroup) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.mussleGroup = mussleGroup;
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

    public String getMussleGroup() {
        return mussleGroup;
    }

    public void setMussleGroup(String mussleGroup) {
        this.mussleGroup = mussleGroup;
    }
/*    @ColumnInfo(name = "reps")
    int[] reps = new int[5]; //maximum 5 sets

    @ColumnInfo(name = "isoriginal")
    Boolean isOriginal;*/

}
