package com.example.tamirmishali.trainingmanager.Database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.example.tamirmishali.trainingmanager.Converters;
import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseAbstractDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.ExerciseDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.RoutineDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import static java.lang.Math.toIntExact;

@Database(version = 6,entities = {Routine.class, Workout.class, Exercise.class, ExerciseAbstract.class})
@TypeConverters({Converters.class})

public abstract class RoutineDatabase extends RoomDatabase {

    //DAO's Declarations
    public abstract RoutineDao routineDao();
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseAbstractDao exerciseAbstractDao();

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
        private WorkoutDao workoutDao;
        private ExerciseDao exerciseDao;
        private ExerciseAbstractDao exerciseAbstractDao;

        private PopulateDbAsyncTask(RoutineDatabase db) {
            routineDao = db.routineDao();
            workoutDao = db.workoutDao();
            exerciseDao = db.exerciseDao();
            exerciseAbstractDao = db.exerciseAbstractDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Routine routine = new Routine("Back Biceps","2019-02-25");//, "20-02-2019");
            //routineDao.insert(routine);

            //routine = new Routine(0, "Chest Triceps");
            long longId = routineDao.insert(routine);
            int routineId = toIntExact(longId);

            Workout workout = new Workout(routineId,"A", Boolean.TRUE);
            longId = workoutDao.insert(workout);
            int workoutId = toIntExact(longId);

            populateExerciseAbstractDatabase(exerciseAbstractDao);
            /*ExerciseAbstract exerciseAbstract = new ExerciseAbstract("Olympic bench chest press", "press of chest on bench and shit", "Chest");
            longId = exerciseAbstractDao.insert(exerciseAbstract);
            int exerciseAbsId = toIntExact(longId);

            Exercise exercise = new Exercise(exerciseAbsId, workoutId, "", "7.5,8|7.5,8|7.5,10");
            exerciseDao.insert(exercise);*/
            //workoutDao.insert(new Workout(routine.getUid(),"B", Boolean.TRUE));
            //workoutDao.insert(new Workout(routine.getUid(),"C", Boolean.TRUE));
            //routineDao.insert(new Routine("12.12.15","Chest Triceps"));
            //routineDao.insert(new Routine("13.12.15","Legs Shoulders"));
            return null;
        }
    }

    private static void populateExerciseAbstractDatabase(ExerciseAbstractDao dao){
        dao.insert(new ExerciseAbstract("Bench press - Barbell", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Bench press - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Incline bench press - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Incline bench press - Barbell", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Decline bench press - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Decline bench press - Barbell", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Chest butterfly - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Chest butterfly - Cables", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Incline butterfly - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Incline butterfly - Cables", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Decline butterfly - Dumbbells", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Decline butterfly - Cables", "press of chest on bench and shit", "Chest"));
        dao.insert(new ExerciseAbstract("Chest dips", "Dips", "Chest"));
        dao.insert(new ExerciseAbstract("Barbell row", "Holding a barbell with a pronated grip (palms facing down), bend your knees slightly and bring your torso forward, by bending at the waist, while keeping the back straight until it is almost parallel to the floor. ", "Back"));
        dao.insert(new ExerciseAbstract("Dumbbell row", "Body Parallel to the floor. Pull dumbbell to stomach", "Back"));
        dao.insert(new ExerciseAbstract("Lat pulldown machine", "The movement is initiated by pulling the elbows down and back, lowering the bar to the upper chest", "Back"));
        dao.insert(new ExerciseAbstract("Row machine", "Pull handle to stomach. Elbows near the body. straight back and tight stomach", "Back"));
        dao.insert(new ExerciseAbstract("Romanian Deadlift", "Don't do that without instructor", "Back"));
        dao.insert(new ExerciseAbstract("Pull up", "Body weight pull up", "Back"));
        dao.insert(new ExerciseAbstract("Military press ", "Barbell press above head", "Shoulders"));
        dao.insert(new ExerciseAbstract("Biceps curl", "forearm directed up", "Biceps"));
        dao.insert(new ExerciseAbstract("Biceps hammer curl", "forearm directed to the side", "Biceps"));
        dao.insert(new ExerciseAbstract("Triceps pushdown with cable", "Elbows close to body. Push down the cable", "Triceps"));
        dao.insert(new ExerciseAbstract("Lying triceps extensions - Dumbbells", "Lye on back. Parallel elbows above head. Lower dumbbells behind head", "Triceps"));
        dao.insert(new ExerciseAbstract("Lying triceps extensions - W Barbell", "Lye on back. Parallel elbows above head. Lower barbell behind head", "Triceps"));
        dao.insert(new ExerciseAbstract("Squat", "Fuck", "Legs"));
        dao.insert(new ExerciseAbstract("Bulgarian Split Squat", "Leg on bench. good luck", "Legs"));
        dao.insert(new ExerciseAbstract("Deadlift", "Romanian Deadlift", "Legs"));
    }
}

