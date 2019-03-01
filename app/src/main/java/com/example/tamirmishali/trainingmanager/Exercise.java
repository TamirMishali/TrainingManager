package com.example.tamirmishali.trainingmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_exercise")
    private int id;

    @ColumnInfo(name ="name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "mussleGroup")
    private String mussleGroup;

    @ColumnInfo(name = "reps")
    int[] reps = new int[5]; //maximum 5 sets

    @ColumnInfo(name = "isoriginal")
    Boolean isOriginal;

}
