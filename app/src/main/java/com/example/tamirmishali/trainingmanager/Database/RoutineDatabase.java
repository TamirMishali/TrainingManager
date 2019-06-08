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
import com.example.tamirmishali.trainingmanager.Database.DAOs.SetDao;
import com.example.tamirmishali.trainingmanager.Database.DAOs.WorkoutDao;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import static java.lang.Math.toIntExact;

@Database(version = 11,entities = {Routine.class, Workout.class, Exercise.class, ExerciseAbstract.class, Set.class})
@TypeConverters({Converters.class})

public abstract class RoutineDatabase extends RoomDatabase {

    //DAO's Declarations
    public abstract RoutineDao routineDao();
    public abstract WorkoutDao workoutDao();
    public abstract ExerciseDao exerciseDao();
    public abstract ExerciseAbstractDao exerciseAbstractDao();
    public abstract SetDao setDao();

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
        private SetDao setDao;

        private PopulateDbAsyncTask(RoutineDatabase db) {
            routineDao = db.routineDao();
            workoutDao = db.workoutDao();
            exerciseDao = db.exerciseDao();
            exerciseAbstractDao = db.exerciseAbstractDao();
            setDao = db.setDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Routine routine = new Routine("Mass","2019-02-25");//, "20-02-2019");

            long longId = routineDao.insert(routine);
            int routineId = toIntExact(longId);

            //Abstract workouts
            Workout workout = new Workout(routineId,"Chest", Boolean.TRUE);
            longId = workoutDao.insert(workout);
            int workoutAbsOneId = toIntExact(longId);

            workout = new Workout(routineId, "Back", Boolean.TRUE);
            longId = workoutDao.insert(workout);
            int workoutAbsTwoId = toIntExact(longId);


            //Practical workouts
/*            workout = new Workout(routineId,"Chest","2019-04-01",Boolean.FALSE);
            longId = workoutDao.insert(workout);
            int workoutOneId = toIntExact(longId);*/

/*            workout = new Workout(routineId, "Back","2019-04-02",Boolean.FALSE);
            longId = workoutDao.insert(workout);
            int workoutTwoId = toIntExact(longId);

            workout = new Workout(routineId,"Chest","2019-04-03",Boolean.FALSE);
            longId = workoutDao.insert(workout);
            int workoutThreeId = toIntExact(longId);

            workout = new Workout(routineId, "Back","2019-04-04",Boolean.FALSE);
            longId = workoutDao.insert(workout);
            int workoutFourId = toIntExact(longId);*/

            populateExerciseAbstractDatabase(exerciseAbstractDao);
            //---------------------------------------Exercises and Sets-----------------------------
            //first abstract workout
            Set set;
            Exercise exercise = new Exercise(1,workoutAbsOneId,"");
            longId =  exerciseDao.insert(exercise);
            exercise = new Exercise(2,workoutAbsOneId,"");
            longId =  exerciseDao.insert(exercise);
            exercise = new Exercise(3,workoutAbsOneId,"");
            longId =  exerciseDao.insert(exercise);

            //second abstract workout
            exercise = new Exercise(14,workoutAbsTwoId,"");
            longId =  exerciseDao.insert(exercise);
            exercise = new Exercise(15,workoutAbsTwoId,"");
            longId =  exerciseDao.insert(exercise);
            exercise = new Exercise(16,workoutAbsTwoId,"");
            longId =  exerciseDao.insert(exercise);





            /*//Practical Workouts
            //workout One
            exercise = new Exercise(1,workoutOneId,"Workout One Ex 1");
            longId = exerciseDao.insert(exercise);
            int exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,7.5,10);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(2,workoutOneId,"Workout One Ex 2");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,3.75,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(3,workoutOneId,"Workout One Ex 3");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,3.75,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);


            //workout Three like One
            exercise = new Exercise(1,workoutThreeId,"Workout Three Ex 1");
            longId = exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,7.5,10);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(2,workoutThreeId,"Workout Three Ex 2");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,3.75,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(3,workoutThreeId,"Workout Three Ex 3");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,3.75,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);





            //Workout Two
            exercise = new Exercise(14,workoutTwoId,"Workout Two Ex 1");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,10,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(15,workoutTwoId,"Workout Two Ex 2");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,5,10);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(16,workoutTwoId,"Workout Two Ex 3");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,2.5,6);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);

            //Workout Four like Two
            exercise = new Exercise(14,workoutFourId,"Workout Four Ex 1");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,10,8);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(15,workoutFourId,"Workout Four Ex 2");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,5,10);
            setDao.insert(set);
            setDao.insert(set);

            exercise = new Exercise(16,workoutFourId,"Workout Four Ex 3");
            longId =  exerciseDao.insert(exercise);
            exerciseOneId = toIntExact(longId);
            set = new Set(exerciseOneId,2.5,6);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);
            setDao.insert(set);*/

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

