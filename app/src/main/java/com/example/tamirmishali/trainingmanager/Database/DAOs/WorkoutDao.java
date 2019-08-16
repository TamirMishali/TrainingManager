package com.example.tamirmishali.trainingmanager.Database.DAOs;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;

@Dao
public interface WorkoutDao {
    @Insert
    long insert(Workout workout);

    @Update
    void update(Workout workout);

    @Delete
    void delete(Workout workout);

    @Query("DELETE FROM workout_table")
    void deleteAllWorkouts();

    @Query("SELECT * from workout_table Order by date DESC")
    LiveData<List<Workout>> getAllWorkouts();

    @Query("SELECT * FROM workout_table WHERE (id_routine=:id_routine and date is NULL)")
    LiveData<List<Workout>> getWorkoutsForRoutine(final int id_routine);

    @Query("select EA.muscleGroup " +
            "from exerciseabs_table EA inner join exercise_table E ON EA.id_exerciseabs  = E.id_exerciseabs " +
            "where E.id_workout IN (:workoutId)")
    List<String> getMusselsInWorkout(final int workoutId);

    @Query("select * from( " +
            "   select * from workout_table " +
            "   where id_routine IN " +
            "       (select id " +
            "       from routine_table " +
            "       Order BY date desc " +
            "       limit 1) " +
            "   and not (date is NULL) " +
            "   ORDER by date desc " +
            "   limit (SELECT count (name) FROM workout_table where (date is null) and (id_routine IN (select id from routine_table Order BY date desc limit 1)))) " +
            "ORDER by date ASC " +
            "LIMIT 1 ")
    Workout getCurrentWorkout();

    @Query("select * from workout_table where (name = :workoutName) and (date < :workoutDate) order by date desc limit 1")
    Workout getPrevWorkout(String workoutName, java.sql.Date workoutDate);

    @Query("select * from workout_table where id_workout=:workoutId")
    Workout getWorkout(int workoutId);

    @Query("SELECT * FROM workout_table WHERE id_routine in (SELECT id FROM routine_table ORDER BY date DESC LIMIT 1) AND date not NULL ORDER By date DESC LIMIT 1")
    Workout getLastWorkout();

    @Query("SELECT * from workout_table where not(date is null) order by date DESC " +
            "limit (SELECT count (name) FROM workout_table where (date is null) and (id_routine IN (select id from routine_table Order BY date asc limit 1)))")
    List<Workout> getLastPracticalWorkouts();

    @Query("SELECT * FROM workout_table WHERE id_routine=:routineId and date is NOT NULL ORDER BY date DESC")
    LiveData<List<Workout>> getPracticalWorkoutsForRoutineLiveData(int routineId);

    @Query("SELECT * from workout_table WHERE (id_routine=:routineId) and (date is null)")
    List<Workout> getAbstractWorkoutsForRoutine(int routineId);

    @Query("select * from workout_table where (id_routine=:routineId) and not(date is null)")
    List<Workout> getPracticalWorkoutsForRoutine(int routineId);

    @Query("select * from workout_table where (id_routine=:routineId) and (name=:workoutName) and not(date is null) order By date DESC limit 1")
    Workout getPracticalWorkoutFromAbstract(int routineId, String workoutName);

    @Query("select * from workout_table where (id_routine=:routineId) and (name=:workoutName) and (date is null) limit 1")
    Workout getAbstractWorkoutFromPractical(int routineId, String workoutName);

    @Query("Select id from routine_table Order By date asc limit 1")
    int getNewestRoutineId();

    @Query("select * from workout_table where (id_routine=:routineId) and (name=:workoutName) and not(date is null) order by date desc limit 1")
    Workout getNewestWorkout(int routineId, String workoutName);


}
