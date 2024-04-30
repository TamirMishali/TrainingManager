package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "exerciseabs_operation",
        foreignKeys = {
            @ForeignKey(
                    entity = ExerciseAbstractInfoValue.class,
                    parentColumns = "id",
                    childColumns = "id_exerciseabs_info_value",
                    onDelete = NO_ACTION
            )})
public class ExerciseAbstractOperation {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "id_exerciseabs_info_value")
    private int id_exerciseabs_info_value;

    @ColumnInfo(name = "operation")
    private String operation;

    @Ignore
    ExerciseAbstractNickname exerciseAbstractNickname;


    // ------------Constructors ---------------
    public ExerciseAbstractOperation(int id_exerciseabs_info_value, String operation) {
        this.id = 0;
        this.id_exerciseabs_info_value = id_exerciseabs_info_value;
        this.operation = operation;
    }

    public ExerciseAbstractOperation(int id_exerciseabs_info_value, String operation, ExerciseAbstractNickname exerciseAbstractNickname) {
        this.id = 0;
        this.id_exerciseabs_info_value = id_exerciseabs_info_value;
        this.operation = operation;
        this.exerciseAbstractNickname = exerciseAbstractNickname;
    }

    public ExerciseAbstractOperation(int id, int id_exerciseabs_info_value, String operation) {
        this.id = id;
        this.id_exerciseabs_info_value = id_exerciseabs_info_value;
        this.operation = operation;
    }

    public ExerciseAbstractOperation(int id, int id_exerciseabs_info_value, String operation, ExerciseAbstractNickname exerciseAbstractNickname) {
        this.id = id;
        this.id_exerciseabs_info_value = id_exerciseabs_info_value;
        this.operation = operation;
        this.exerciseAbstractNickname = exerciseAbstractNickname;
    }

    public ExerciseAbstractOperation(){

    }


    //---------------------Getters & Setters---------------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_exerciseabs_info_value() {
        return id_exerciseabs_info_value;
    }

    public void setId_exerciseabs_info_value(int id_exerciseabs_info_value) {
        this.id_exerciseabs_info_value = id_exerciseabs_info_value;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public ExerciseAbstractNickname getExerciseAbstractNickname() {
        return exerciseAbstractNickname;
    }

    public void setExerciseAbstractNickname(ExerciseAbstractNickname exerciseAbstractNickname) {
        this.exerciseAbstractNickname = exerciseAbstractNickname;
    }

    //---------------------------Find functions-----------------------------
    public ExerciseAbstractOperation findByOperation(
            String target_operation, List<ExerciseAbstractOperation> operations) {

        for (ExerciseAbstractOperation operation : operations) {
            if (operation.getOperation().equals(target_operation)) {
                return operation;
            }
        }
        return null;
    }
}

