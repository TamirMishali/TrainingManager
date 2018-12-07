package com.example.tamirmishali.trainingmanager.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "tableworkouts")
public class TableWorkouts {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "id_main")
    private int m_main;

    @ColumnInfo(name = "exerciseNum")
    private int m_exerciseNum;

    @ColumnInfo(name = "date")
    private Date m_date;

}
