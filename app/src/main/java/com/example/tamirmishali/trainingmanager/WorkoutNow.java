package com.example.tamirmishali.trainingmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static com.example.tamirmishali.trainingmanager.MainActivity.ACTION_FINISH_WORKOUT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class WorkoutNow extends AppCompatActivity {

    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";


    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private SetViewModel setViewModel;
//    private LinearLayout parentLinearLayout;
    private LinearLayout extendedEAInfo;
    private Workout currentWorkout;
    private Workout prevWorkout;
    private int sourceWorkoutID;

    private static final String TAG = WorkoutNow.class.getName();

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workoutnow_layout);

//        parentLinearLayout = findViewById(R.id.parent_linear_layout);

        TextView textViewRoutineName = findViewById(R.id.workoutnow_current_routine);
        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        TextView textViewWorkoutDate = findViewById(R.id.workoutnow_workout_date);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);



        //Get workout ID for viewing the relevant workout
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)){
            String callingClassName = getCallingActivity().getClassName(); //.getShortClassName();
            Log.d(TAG, "Class name= " + callingClassName);
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

            //New Workout that was picked from the list of workouts in current routine or id
            // of unfinished workout to reload.
            if(callingClassName.equals("com.example.tamirmishali.trainingmanager.MainActivity") && intent.getAction()==null){
                prevWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId())));
                currentWorkout = constructNewWorkout(prevWorkout); // create workout

            }

            //Existing Workout - finished workout from history
            else if(callingClassName.equals("com.example.tamirmishali.trainingmanager.History.ViewPracticalWorkouts") || intent.getAction()==ACTION_FINISH_WORKOUT){
                currentWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                currentWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(currentWorkout.getId())));

                prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
                if (prevWorkout == null){
                    prevWorkout = workoutViewModel.getAbstractWorkoutFromPractical(
                            currentWorkout.getId_routine(),currentWorkout.getWorkoutName());
                }

                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId())));
            }
        }
        //im on WorkoutNow Activity
        else{
            setResult(RESULT_CANCELED,intent);
            finish();
            return;
        }

        // Insert names of routine, workout and date at the top of the screen
        try {
            textViewRoutineName.setText("Routine: " +
                    routineViewModel.getRoutine(currentWorkout.getId_routine()).getRoutineName());
            textViewWorkoutName.setText("Workout: " + currentWorkout.getWorkoutName());

            //https://stackabuse.com/how-to-get-current-date-and-time-in-java/
            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
            Date d = new Date(currentWorkout.getDate().getTime());
            textViewWorkoutDate.setText(formatter.format(d));

        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }


        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        listAdapter = new ExpandableListAdapter(this, currentWorkout, prevWorkout,
                setViewModel,exerciseViewModel,exerciseAbstractViewModel);
        // setting list adapter
        expListView.setAdapter(listAdapter);
        // MY SAVIOUR!!!! Fixed the text jumps!!! <3<3<3 (06.12.2022)
        // https://stackoverflow.com/questions/18632084/expandablelistview-child-items-edittext-cant-keep-focus
        expListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);

        expListView.expandGroup(0);
        // todo (25.01.2023): find the command that makes the keyboard go under all header children, and not only under the one getting focused.


/*        // ListView on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                *//*Toast.makeText(
                        getApplicationContext(),
                        exerciseListHeader.get(groupPosition)
                                + " : "
                                + prevSetsListDataChild.get(
                                exerciseListHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();*//*
                return false;
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private List<Exercise> fillExerciseData(List<Exercise> exerciseList){
        for(int i=0; i<exerciseList.size(); i++){
            Exercise exercise = exerciseList.get(i);
            exercise.setExerciseAbstract(exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()));
            exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));

            if (exercise.getSets() == null){
                List<Set> sets = new ArrayList<>();
                exercise.setSets(sets);
            }
        }
        return exerciseList;

    }

    //if exercise from last Workout exists in the next workout, then add it
    private HashMap<String, List<Set>> getPrevSetsListDataChild(List<Exercise> exerciseList, List<String> exerciseTitles){
        if(exerciseList == null || exerciseList.isEmpty())
            return null;

        //DEBUG and try to understand what happens when there are no sets
        //if it returns null or empty list


        HashMap<String, List<Set>> result = new HashMap<>();

        Iterator<Exercise> iterator = exerciseList.iterator();
        String exerciseName;
        Exercise exercise;

        while (iterator.hasNext()) {
            exercise = iterator.next();
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).generateExerciseAbstractName();
            if (exerciseTitles.contains(exerciseName)){
                result.put(exerciseName,setViewModel.getSetsForExercise(exercise.getId()));
            }
        }

        return result;
    }

    //takes the data from the abstract Workout
    private List<String> prepareListHeader(List<Exercise> exerciseList){
        if(exerciseList == null || exerciseList.isEmpty()){
            Log.d(TAG,"Current exercise list is empty or null");
            return null;
        }
        List<String> listHeader = new ArrayList<>();

        Iterator<Exercise> iterator = exerciseList.iterator();
        String exerciseName;
        Exercise exercise;

        //Constructing exercise names for adapter
        while (iterator.hasNext()) {
            exercise = iterator.next();
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).generateExerciseAbstractName();
            listHeader.add(exerciseName);
        }
        return listHeader;
    }

    private Workout constructNewWorkout(Workout prevWorkout) {
        Workout newWorkout;

        // create and insert workout
        Workout workout = new Workout(prevWorkout.getId_routine(), prevWorkout.getName(), false);
        workoutViewModel.insert(workout);

        // get most recent workout with routine_id and workoutName:
        newWorkout = workoutViewModel.getNewestWorkout(workout.getId_routine(), workout.getName());

        // --- Insert exercises like they show in abstract workout to new workout ---
        // Get Exercises from abstract workout
        List<Exercise> exercisesList;
        exercisesList = exerciseViewModel.getExercisesForWorkout(
                workoutViewModel.getAbstractWorkoutFromPractical(newWorkout.getId_routine(), newWorkout.getName()).getId());

        Iterator<Exercise> iteratorCurrentExercise = exercisesList.iterator();

        // iterate over the list of exercises from the abstract workout and create new exercise with
        // the new workout id and save to DB:
        while (iteratorCurrentExercise.hasNext()) {
            // get Exercise from iterator (already has workout_id and exerciseAbs_id of prevWorkout)
            Exercise exercise = new Exercise(iteratorCurrentExercise.next());
            //exercise.setId(0); not necessary  cuz in constructor already
            // Change the workout_id to current workout and insert to DB:
            exercise.setId_workout(newWorkout.getId());
            exerciseViewModel.insert(exercise);
        }

        // get all inserted Exercises and assign them to current workout (now with valid id in DB)
        newWorkout.setExercises(exerciseViewModel.getExercisesForWorkout(newWorkout.getId()));

        // for each Exercise in workout, set its ExerciseAbstract object using Exercise.getId_exerciseabs
        for(int i=0; i<newWorkout.getExercises().size(); i++){
            // get exercise abstract
            ExerciseAbstract exerciseAbstract = exerciseAbstractViewModel.getExerciseAbsFromId(
                    newWorkout.getExercises().get(i).getId_exerciseabs());
            // fill its String fields
            exerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractIdsToStrings(exerciseAbstract);
            // assign it to Exercise in newWorkout
            newWorkout.getExercises().get(i).setExerciseAbstract(exerciseAbstract);
        }

        // To this point, i have a workout object, with a list of Exercises and each Exercise
        //  holds an ExerciseAbstract object with all the Strings.

        // --- Sets ---
        // find each Exercise in prev workout and if there are sets there,
        // take the amount and create that number of sets in current Exercise
        // if not exist, don't create. let the adapter do it
        iteratorCurrentExercise = newWorkout.getExercises().iterator();
        Iterator<Exercise> prevIterator;
        int setsNo;
        Exercise prevExercise;

        // find same ExerciseAbs in both currentWorkout and prevWorkout
        // don't forget that currentExercise contains the Exercise from absWorkout
        while(iteratorCurrentExercise.hasNext()) {
            Exercise currentExercise = iteratorCurrentExercise.next();
            Boolean foundFlag = Boolean.FALSE;

            //iterate over all exercises in prevWorkout and search for currentExercise
            prevIterator = prevWorkout.getExercises().iterator();

            while(prevIterator.hasNext()) {
                prevExercise = prevIterator.next();

                //if exists, create the same number of sets like prevExercise
                if(currentExercise.getId_exerciseabs() == prevExercise.getId_exerciseabs()){
                    setsNo = prevExercise.getSets().size();

                    for(int i=0 ; i<setsNo ; i++){
                        Set set = new Set(currentExercise.getId(),-1.0,-1);
                        setViewModel.insert(set);
                    }

                    // get all the Sets that were just inserted to DB:
                    List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
                    if (!setList.isEmpty())
                        currentExercise.setSets(setList);
                        //currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(currentExercise.getId_exerciseabs()).getName(),setList);

                    foundFlag = Boolean.TRUE;
                    /*else
                        currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId()).getName(),null);*/
                    break;
                }
            }

            // Part Tamir:
            // If exercise from abs was not found in prev, i still want to make it
            // It probably created from a change in mid routine.
            // Create one Set of it:

            // Present Tamir:
            // What part Tamir wrote does not make sense.
            // All i do here is assign an empty Set list:
            if (foundFlag == Boolean.FALSE){
/*                Set set = new Set(currentExercise.getId(),-1.0,-1);
                setViewModel.insert(set);*/
                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
                currentExercise.setSets(setList);
                //currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(currentExercise.getId_exerciseabs()).getName(),setList);
            }
        }

        return newWorkout;
    }


    @NonNull//this is a marker, does nothing but telling is can never be null
    private Boolean isExerciseInExerciseList(Exercise exercise, List<Exercise> exerciseList){
        Iterator<Exercise> iterator = exerciseList.iterator();
        //Exercise currentExercise;

        while (iterator.hasNext()) {
            //currentExercise = iterator.next();
            if (exercise.getId_exerciseabs() == iterator.next().getId_exerciseabs()/*currentExercise.getId_exerciseabs()*/)
                return true;
        }
        return false;
    }
}



