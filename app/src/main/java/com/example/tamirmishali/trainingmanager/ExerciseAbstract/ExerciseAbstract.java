package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Comparator;

@Entity(tableName = "exerciseabs_table")
public class ExerciseAbstract{

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


    //https://beginnersbook.com/2013/12/java-arraylist-of-object-sort-example-comparable-and-comparator/
    public static Comparator<ExerciseAbstract> MuscleGroupComparator = new Comparator<ExerciseAbstract>() {

        public int compare(ExerciseAbstract ea1, ExerciseAbstract ea2) {
            String StudentName1 = ea1.getMuscleGroup().toUpperCase();
            String StudentName2 = ea2.getMuscleGroup().toUpperCase();

            //ascending order
            return StudentName1.compareTo(StudentName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};




}
