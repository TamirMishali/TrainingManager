package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "exerciseabs_info_value")
public class ExerciseAbstractInfoValue {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id_exerciseabs_info")
    private int id_exerciseabs_info;

    @ColumnInfo(name = "value")
    private String value;


    // ------------Constructors ---------------
    public ExerciseAbstractInfoValue(int id, int id_exerciseabs_info, String value) {
        this.id = id;
        this.id_exerciseabs_info = id_exerciseabs_info;
        this.value = value;
    }

    public ExerciseAbstractInfoValue(int id_exerciseabs_info, String value) {
        this.id = 0;
        this.id_exerciseabs_info = id_exerciseabs_info;
        this.value = value;
    }

    public ExerciseAbstractInfoValue() {

    }


    //---------------------Getters & Setters---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_exerciseabs_info() {
        return id_exerciseabs_info;
    }

    public void setId_exerciseabs_info(int id_exerciseabs_info) {
        this.id_exerciseabs_info = id_exerciseabs_info;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

