package com.example.tamirmishali.trainingmanager.Database.DAOs;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;

import java.util.List;

@Dao
public interface ExerciseDao {
    @Insert
    long insert(Exercise exercise);

    @Update
    void update(Exercise exercise);

    @Delete
    void delete(Exercise exercise);

    @Query("DELETE FROM exercise_table where id_workout=:id_workout")
    void deleteAllExercises(int id_workout);

    @Query("SELECT * from exercise_table where (id_exerciseabs =:id_exerciseabs) and (id_workout =:id_workout)")
    Exercise getExerciseForWorkout(final int id_exerciseabs,final int id_workout);

    @Query("SELECT * from exercise_table Order by id_exercise DESC")
    LiveData<List<Exercise>> getAllExercises();

    @Query("SELECT * FROM exercise_table WHERE id_workout=:id_workout")
    LiveData<List<Exercise>> getExercisesForWorkout_O(final int id_workout);

    @Query("SELECT * FROM exercise_table WHERE id_workout=:id_workout")
    List<Exercise> getExercisesForWorkout(final int id_workout);

    @Query("SELECT * FROM exercise_table WHERE id_exerciseabs=:EA_ID")
    List<Exercise> getExercisesForEA(final int EA_ID);

    //get (most recent)/(newest) exercise from all workouts in a routine.
    // This is for building the right number of sets and get the most recent number of reps in an exercise in a new workout.
    // This query resulted from the need to get most recent time i did an exercise. if i have ex1
    //  in workout1 and workout2, the following time i do the ex1, i want the app to get the most
    //  recent data. This means the it need to get the data from most recent workout i did ex1.
    // This replace taking the data from same prev workout, and not from the recent of all workouts.
    @Query("select * " +
            "from exercise_table " +
            "where exercise_table.id_exercise = " +
            "    (select id_exercise " +
            "    from workout_table " +
            "    join exercise_table ON workout_table.id_workout = exercise_table.id_workout " +
            "    where (id_routine=:id_routine) and (date is NOT NULL) and (exercise_table.id_exerciseabs=:id_exerciseabs) and (workout_table.date < " +
            "       (select wot1.date " +
            "        from workout_table as wot1 " +
            "        where wot1.id_workout=:id_workout)" +
            "       ) " +
            "ORDER BY date DESC " +
            "limit 1) ")
    Exercise getMostRecentExerciseFromAllWorkoutsInRoutine(final int id_routine, final int id_workout, final int id_exerciseabs);


/*    SELECT exerciseabs_table.id_exerciseabs, eaiv1.value as muscle, exerciseabs_operation.operation ,exerciseabs_nickname.nickname,
eaiv2.value as load_type, eaiv3.value as position, eaiv4.value as angle ,eaiv5.value as grip_width ,eaiv6.value as thumbs_direction ,eaiv7.value as separate_sides
FROM exerciseabs_table
	LEFT JOIN exerciseabs_info_value eaiv1 ON eaiv1.id = exerciseabs_table.id_muscle
	JOIN exerciseabs_operation ON exerciseabs_operation.id = exerciseabs_table.id_operation
	LEFT JOIN exerciseabs_nickname ON exerciseabs_nickname.id = exerciseabs_table.id_nickname
	LEFT JOIN exerciseabs_info_value eaiv2 ON eaiv2.id = exerciseabs_table.id_load_type
	LEFT JOIN exerciseabs_info_value eaiv3 ON eaiv3.id = exerciseabs_table.id_position
	LEFT JOIN exerciseabs_info_value eaiv4 ON eaiv4.id = exerciseabs_table.id_angle
	LEFT JOIN exerciseabs_info_value eaiv5 ON eaiv5.id = exerciseabs_table.id_grip_width
	LEFT JOIN exerciseabs_info_value eaiv6 ON eaiv6.id = exerciseabs_table.id_thumbs_direction
	LEFT JOIN exerciseabs_info_value eaiv7 ON eaiv7.id = exerciseabs_table.id_separate_sides */

}
