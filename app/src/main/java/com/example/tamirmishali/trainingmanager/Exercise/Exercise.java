package com.example.tamirmishali.trainingmanager.Exercise;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.List;

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
    private long id;

    @ColumnInfo(name = "id_exerciseabs")
    private long id_exerciseabs;

    @ColumnInfo(name = "id_workout")
    private long id_workout;

    @ColumnInfo(name = "comment")
    private String comment;

    @Ignore
    ExerciseAbstract exerciseAbstract;

    @Ignore
    List<Set> sets;


/*    @ColumnInfo(name = "data")
    private String data;*/

    // ------------Constructors ---------------
    public Exercise(){

    }

    public Exercise(Exercise exercise){
        this.id = 0;
        this.id_exerciseabs = exercise.id_exerciseabs;
        this.id_workout = exercise.id_workout;
        this.comment = exercise.comment;
    }

    public Exercise(long idExerciseAbs, long idWorkout, String comment/*, String data*/){
        this.id = 0;
        this.id_exerciseabs = idExerciseAbs;
        this.id_workout = idWorkout;
        this.comment = comment;
    }

    //---------------Getters & Setters---------------
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_exerciseabs() {
        return id_exerciseabs;
    }

    public void setId_exerciseabs(long id_exerciseAbs) {
        this.id_exerciseabs = id_exerciseAbs;
    }

    public long getId_workout() {
        return id_workout;
    }

    public void setId_workout(long id_workout) {
        this.id_workout = id_workout;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ExerciseAbstract getExerciseAbstract() {
        return exerciseAbstract;
    }

    public void setExerciseAbstract(ExerciseAbstract exerciseAbstract) {
        this.exerciseAbstract = exerciseAbstract;
    }

    public List<Set> getSets() {
        return sets;
    }

    public void setSets(List<Set> sets) {
        this.sets = sets;
    }

}
