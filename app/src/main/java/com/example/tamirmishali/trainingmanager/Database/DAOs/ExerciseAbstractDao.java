package com.example.tamirmishali.trainingmanager.Database.DAOs;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import androidx.lifecycle.LiveData;

import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractInfoValue;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractNickname;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractOperation;

import java.util.List;

@Dao
public interface ExerciseAbstractDao {
    @Insert
    long insert(ExerciseAbstract exerciseAbstract);

    @Update
    void update(ExerciseAbstract exerciseAbstract);

    @Delete
    void delete(ExerciseAbstract exerciseAbstract);

    @Query("DELETE FROM exerciseabs_table")
    void deleteAllexerciseAbstracts();

    @Query("SELECT * from exerciseabs_table Order by id_muscle, id_operation")
    LiveData<List<ExerciseAbstract>> getAllExercisesAbstract();

    @Query("select * " +
            "from exerciseabs_table EA inner join exercise_table E ON EA.id_exerciseabs  = E.id_exerciseabs " +
            "where E.id_workout IN (:workoutId)")
    LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int workoutId);

    @Query("select * from exerciseabs_table where id_exerciseabs=:exerciseAbsId")
    ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId);



    // NEW:
    // TODO: Understand if i must add new tables DAO's so the auto implemented
    //  code will be created or maybe there might be another way to avoid it - IDK

    // ------------------ exerciseabs_info_value ----------------------
    @Query("select * from exerciseabs_info_value")
    LiveData<List<ExerciseAbstractInfoValue>> getAllExerciseAbstractInfoValues();

    @Query("SELECT id FROM exerciseabs_info_value WHERE value=:value")
    int getExerciseAbstractInfoValueId(String value);

    @Query("SELECT value FROM exerciseabs_info_value WHERE id=:id")
    String getExerciseAbstractInfoValueValue(int id);

    @Query("SELECT value FROM exerciseabs_info_value " +
            "JOIN exerciseabs_info ON " +
            "exerciseabs_info_value.id_exerciseabs_info=exerciseabs_info.id " +
            "WHERE info_header_name = :info_header_name1" )
    List<String> getExerciseAbstractInfoValueValueByHeader(String info_header_name1);



    // ------------------ exerciseabs_operation ----------------------
/*    @Query("INSERT INTO exerciseabs_operation VALUES(:id_muscle, :operation)")
    Void insertOperation(String id_muscle, String operation);*/

    @Query("select * from exerciseabs_operation ")
    LiveData<List<ExerciseAbstractOperation>> getAllExerciseAbstractOperations();

    @Query("SELECT id FROM exerciseabs_operation " +
            "WHERE id_exerciseabs_info_value=:id_muscle and operation=:operation")
    int getExerciseAbstractOperationId(String id_muscle, String operation);

    @Query("SELECT operation FROM exerciseabs_operation WHERE id=:id")
    String getExerciseAbstractOperationOperation(int id);

    @Query("SELECT * FROM exerciseabs_info_value " +
            "JOIN exerciseabs_info ON " +
            "exerciseabs_info_value.id_exerciseabs_info=exerciseabs_info.id " +
            "WHERE info_header_name = :info_header_name1" )
    int getAllExerciseAbstractInfoValues(String info_header_name1);

    @Query("SELECT operation FROM exerciseabs_operation WHERE id_exerciseabs_info_value=:id_muscle")
    List<String> getExerciseAbstractOperationByMuscleId(int id_muscle);



    // ------------------ exerciseabs_nickname ----------------------
/*    @Query("INSERT INTO exerciseabs_nickname VALUES(:id_operation, :nickname)")
    Void insertNickname(String id_operation, String nickname);*/

    @Query("select * from exerciseabs_nickname ")
    LiveData<List<ExerciseAbstractNickname>> getAllExerciseAbstractNicknames();

    @Query("SELECT id FROM exerciseabs_nickname WHERE nickname=:nickname")
    int getExerciseAbstractNicknameId(String nickname);

    @Query("SELECT nickname FROM exerciseabs_nickname WHERE id=:id")
    String getExerciseAbstractNicknameNickname(int id);

    @Query("SELECT nickname FROM exerciseabs_nickname WHERE id_exerciseabs_operation=:id_operation")
    List<String> getExerciseAbstractNicknameByOperationId(int id_operation);



}





// OLD

//package com.example.tamirmishali.trainingmanager.Database.DAOs;
//
//
//import android.arch.lifecycle.LiveData;
//import android.arch.persistence.room.Dao;
//import android.arch.persistence.room.Delete;
//import android.arch.persistence.room.Insert;
//import android.arch.persistence.room.Query;
//import android.arch.persistence.room.Update;
//
//import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Dao
//public interface ExerciseAbstractDao {
//    @Insert
//    long insert(ExerciseAbstract exerciseAbstract);
//
//    @Update
//    void update(ExerciseAbstract exerciseAbstract);
//
//    @Delete
//    void delete(ExerciseAbstract exerciseAbstract);
//
//    @Query("DELETE FROM exerciseabs_table")
//    void deleteAllexerciseAbstracts();
//
////    @Query("SELECT * from exerciseabs_table Order by id_exerciseabs DESC")
//    @Query("SELECT * from exerciseabs_table Order by muscleGroup, name")
//    LiveData<List<ExerciseAbstract>> getAllExercisesAbstract();
//
//    @Query("select EA.id_exerciseabs, EA.name, EA.description, EA.muscleGroup " +
//            "from exerciseabs_table EA inner join exercise_table E ON EA.id_exerciseabs  = E.id_exerciseabs " +
//            "where E.id_workout IN (:workoutId)")
//    LiveData<List<ExerciseAbstract>> getExerciseAbstractsForWorkout(int workoutId);
//
//    @Query("select * from exerciseabs_table where id_exerciseabs=:exerciseAbsId")
//    ExerciseAbstract getExerciseAbsFromId(int exerciseAbsId);
//
//    @Query("select distinct muscleGroup from exerciseabs_table order by muscleGroup asc")
//    List<String> getMuscles();
//
///*    @Query("SELECT * FROM exerciseabs_table WHERE id_workout=:id_workout")
//    LiveData<List<ExerciseAbstract>> getExercisesForWorkout(final int id_workout);*/
