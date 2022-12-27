package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "exerciseabs_nickname")
public class ExerciseAbstractNickname {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id_exerciseabs_operation")
    private int id_exerciseabs_operation;

    @ColumnInfo(name = "nickname")
    private String nickname;


    // ------------Constructors ---------------
    public ExerciseAbstractNickname(int id_exerciseabs_operation, String nickname) {
        this.id = 0;
        this.id_exerciseabs_operation = id_exerciseabs_operation;
        this.nickname = nickname;
    }

    public ExerciseAbstractNickname(int id, int id_exerciseabs_operation, String nickname) {
        this.id = id;
        this.id_exerciseabs_operation = id_exerciseabs_operation;
        this.nickname = nickname;
    }

    public ExerciseAbstractNickname(){

    }


    //---------------------Getters & Setters---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_exerciseabs_operation() {
        return id_exerciseabs_operation;
    }

    public void setId_exerciseabs_operation(int id_exerciseabs_operation) {
        this.id_exerciseabs_operation = id_exerciseabs_operation;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    //---------------------------Find functions-----------------------------
    public ExerciseAbstractNickname findByNickname(
            String target_nickname, List<ExerciseAbstractNickname> nicknames) {

        for (ExerciseAbstractNickname nickname : nicknames) {
            if (nickname.getNickname().equals(target_nickname)) {
                return nickname;
            }
        }
        return null;
    }
}

