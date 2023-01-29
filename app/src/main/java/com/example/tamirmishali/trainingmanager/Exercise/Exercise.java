package com.example.tamirmishali.trainingmanager.Exercise;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.ArrayList;
import java.util.List;

import static androidx.room.ForeignKey.CASCADE;
import static androidx.room.ForeignKey.NO_ACTION;

@Entity(tableName = "exercise_table",
        foreignKeys = {
        @ForeignKey(
                entity = ExerciseAbstract.class,
                parentColumns = "id_exerciseabs",
                childColumns = "id_exerciseabs",
                onDelete = NO_ACTION
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

    @Ignore
    ExerciseAbstract exerciseAbstract;

    @Ignore
    List<Set> sets = new ArrayList<>();


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

    public Exercise(int idExerciseAbs, int idWorkout, String comment/*, String data*/){
        this.id = 0;
        this.id_exerciseabs = idExerciseAbs;
        this.id_workout = idWorkout;
        this.comment = comment;
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

    public void setId_exerciseabs(int id_exerciseAbs) {
        this.id_exerciseabs = id_exerciseAbs;
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


    // Custom functions:

    /**
     * Check if weight and reps values in each Set are filled and valid.
     * If one of them is smaller than zero, return false.
     * This is due to a rule that -1 means unfilled value
     * value = -2 means N/A value but this is for view only.
     *
     * @return      Boolean. true for filled properly, false otherwise.
     * @see         Set
     */
    public Boolean areAllSetsFilled(){
        for (Set set_i : this.sets) {
            if (set_i.getReps() < 0 || set_i.getWeight() < 0)
                return false;
        }
        return true;
    }

    /**
     * Check if weight and reps values in each Set are filled and valid.
     * If one of them is smaller than zero, return false.
     * This is due to a rule that -1 means unfilled value
     * value = -2 means N/A value but this is for view only.
     *
     * @param       setList is an external List of Sets
     * @return      Boolean. true for filled properly, false otherwise.
     * @see         Set
     */
    public Boolean areAllSetsFilled(List<Set> setList){
        for (Set set_i : setList) {
            if (set_i.getReps() < 0 || set_i.getWeight() < 0)
                return false;
        }
        return true;
    }

}
