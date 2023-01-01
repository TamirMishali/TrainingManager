package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.List;
import java.util.OptionalInt;

@Entity(tableName = "exerciseabs_info_value",
        foreignKeys = {
        @ForeignKey(
                entity = ExerciseAbstractInfo.class,
                parentColumns = "id",
                childColumns = "id_exerciseabs_info",
                onDelete = NO_ACTION
        )})
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


    // --------------------- Functions ----------------------------
    public int getIndexByProperty(List<ExerciseAbstractInfoValue> exivList, String value){
        OptionalInt index = exivList.stream()
                .mapToInt(exivList::indexOf)
                .filter(i -> exivList.get(i).getValue().equals(value))
                .findFirst();

        if (!(index.isPresent())) {
            System.out.println(value + " not found in the list");
            return -1;
        }
        else{
            return index.getAsInt();
        }
    }
}

