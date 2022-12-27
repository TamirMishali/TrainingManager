package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "exerciseabs_info")
public class ExerciseAbstractInfo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "info_header_name")
    private String info_header_name;


    // ------------Constructors ---------------

    public ExerciseAbstractInfo(int id, String info_header_name) {
        this.id = id;
        this.info_header_name = info_header_name;
    }

    public ExerciseAbstractInfo(String info_header_name) {
        this.id = 0;
        this.info_header_name = info_header_name;
    }

    public ExerciseAbstractInfo() {

    }


    //---------------------Getters & Setters---------------------


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getInfo_header_name() {
        return info_header_name;
    }

    public void setInfo_header_name(String info_header_name) {
        this.info_header_name = info_header_name;
    }

}

