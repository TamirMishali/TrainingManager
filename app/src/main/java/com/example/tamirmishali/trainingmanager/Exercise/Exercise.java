package com.example.tamirmishali.trainingmanager.Exercise;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "exercise_table",
        foreignKeys = {
        @ForeignKey(
                entity = ExerciseAbstract.class,
                parentColumns = "id_exerciseabs",
                childColumns = "id_exerciseabs",
                onDelete = CASCADE
        ),
        @ForeignKey(
                entity = Workout.class,
                parentColumns = "id_workout",
                childColumns = "id_workout",
                onDelete = CASCADE)})

public class Exercise {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_exercise")
    private int id;

    @ColumnInfo(name = "id_exerciseabs")
    private int id_exerciseabs;

    @ColumnInfo(name = "id_workout")
    private int id_workout;

    @ColumnInfo(name = "comment")
    private String comment;

/*    @ColumnInfo(name = "data")
    private String data;*/

    // ------------Constructors ---------------
    public Exercise(){

    }
    public Exercise(int idExerciseAbs, int idWorkout, String comment/*, String data*/){
        this.id = 0;
        this.id_exerciseabs = idExerciseAbs;
        this.id_workout = idWorkout;
        this.comment = comment;
/*        this.data = data;*/
    }

    //---------------Getters & Setters---------------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_exerciseabs() {
        return id_exerciseabs;
    }

    public void setId_exerciseabs(int id_exerciseabs) {
        this.id_exerciseabs = id_exerciseabs;
    }

    public int getId_workout() {
        return id_workout;
    }

    public void setId_workout(int id_workout) {
        this.id_workout = id_workout;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

/*    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }*/
/*    @ColumnInfo(name = "reps")
    int[] reps = new int[5]; //maximum 5 sets

    @ColumnInfo(name = "isoriginal")
    Boolean isOriginal;*/

}
