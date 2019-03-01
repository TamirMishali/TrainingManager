package com.example.tamirmishali.trainingmanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import java.sql.Date;
import java.util.Calendar;
import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "workout_table",
        foreignKeys = @ForeignKey(entity = Routine.class,
            parentColumns = "id",
            childColumns = "id_routine",
            onDelete = CASCADE))

public class Workout {

    @NonNull
    @ColumnInfo(name = "id_workout")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_routine")
    private int id_routine;

    @ColumnInfo(name = "type")
    private String type; //=routineDate_Char(A/B/C)

    @ColumnInfo(name = "date")
    private Date date;

    public Workout(){//-----------------------------------complete later

    }

    /*When Abstract workout, Date=null. if real workout Date=currentDate */
    public Workout(int id_routine, String type, boolean abstractWorkout) {
        this.id_routine = id_routine;
        this.type = type;
        if (abstractWorkout){
            this.date = null; }
        else {
            Calendar calendar = Calendar.getInstance();
            this.date  = new java.sql.Date(calendar.getTime().getTime());
        }

    }


    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getId_routine() { return id_routine; }

    public void setId_routine(int id_routine) { this.id_routine = id_routine; }

    public String getType() { return type; }

    public void setType(String type) { this.type = type; }

    public Date getDate() { return date; }

    public void setDate(Date date) { this.date = date; }


//ArrayList<Exercise> exercises; //-------------for now, late we will try the adapterJson thing
}


