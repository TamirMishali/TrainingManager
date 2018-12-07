package com.example.tamirmishali.trainingmanager.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.example.tamirmishali.trainingmanager.Database.DAOs.RoutineDao;
import com.example.tamirmishali.trainingmanager.Routine;


@Database(version = 2,entities = {Routine.class,})// TableSetsHistory.class, TableWorkouts.class, TableWorkoutTypesHistory.class})
public abstract class RoutineDatabase extends RoomDatabase {

    // DAO's Declarations
    public abstract RoutineDao routineDao();

    // Prevention of opening the same database to RAM twice.
    private static final String DATABASE_NAME = "routines_database";
    private static volatile RoutineDatabase INSTANCE;

    public static synchronized RoutineDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    RoutineDatabase.class, DATABASE_NAME).fallbackToDestructiveMigration()
                    .addCallback(roomcallback)
                    .build();
        }
        return INSTANCE;
    }


    //callback to the current database. when the app is opened for the first time
    //it create that database. we want to access the same database over time
    //and not creating a new database every time we open the app.
    private static RoomDatabase.Callback roomcallback = new RoomDatabase.Callback() {
                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    new PopulateDbAsyncTask(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private RoutineDao routineDao;

        private PopulateDbAsyncTask(RoutineDatabase db) {
            routineDao = db.routineDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            routineDao.insert(new Routine("10.12.15","Back Biceps"));
            //routineDao.insert(new Routine("12.12.15","Chest Triceps"));
            //routineDao.insert(new Routine("13.12.15","Legs Shoulders"));
            return null;
        }
    }
}

