package com.example.tamirmishali.trainingmanager.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import java.sql.Date;

@Entity(tableName = "routine_table")
public class TableRoutine {
    @PrimaryKey(autoGenerate = true)
    private int id;

/*    @ColumnInfo(name = "id_routine")
    private int m_idroutine;

    @ColumnInfo(name = "workout_char")
    private char m_workoutChar;*/

    @ColumnInfo(name = "date")
    private Date m_date;

    public TableRoutine() {
//        this.m_idroutine = id_routine;
//        this.m_workoutChar = c;
        this.m_date = new java.sql.Date(new java.util.Date().getTime());//PrepStmt.setTimestamp(1, date);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getM_date() {
        return m_date;
    }

    public void setM_date(Date m_date) {
        this.m_date = m_date;
    }
}



