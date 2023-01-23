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
    private LinearLayout parentLinearLayout;
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

        parentLinearLayout = findViewById(R.id.parent_linear_layout);
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
            String callingClassName = getCallingActivity().getShortClassName();
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

            //New Workout that was picked from the list of workouts in current routine or id
            // of unfinished workout to reload.
            if(callingClassName.equals(".MainActivity") && intent.getAction()==null){
                prevWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId())));
                currentWorkout = constructNewWorkout(prevWorkout); // create workout

            }

            //Existing Workout - finished workout from history
            else if(callingClassName.equals(".History.ViewPracticalWorkouts") || intent.getAction()==ACTION_FINISH_WORKOUT){
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



/*        // ListView on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
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
            newWorkout.getExercises().get(i).setExerciseAbstract(
                    exerciseAbstractViewModel.getExerciseAbsFromId(newWorkout.getExercises().get(i).getId_exerciseabs())
            );
        }

        // To this point, i have a workout object, with a list of Exercises and each Exercise
        //  holds an ExerciseAbstract object

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



/*    private void prepareListDataPrev() {
        exerciseListHeader = new ArrayList<>();
        prevSetsListDataChild = new HashMap<>();
        Iterator<Exercise> iterator = currentWorkout.getExercises().iterator();
        String exerciseName;// = new String();
        List<Set> childList;
        Exercise exercise;

        //Constructing exercise names for adapter
        while (iterator.hasNext()) {
            exercise = iterator.next();
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).getName();
            exerciseListHeader.add(exerciseName);

            if (isExerciseInExerciseList(exercise,prevExercises)){//(currentExercise with prevExerciseList)
                childList = setViewModel.getSetsForExercise(exercise.getId());
                prevSetsListDataChild.put(exerciseName, childList);
            }
            //else, the exercise will be there but with no sets
            //this will happen if exercise list changed for abs workout
            //from the last practical workout made

        }
    }*/


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




/*    private void prepareListData2() {
        exerciseListHeader = new ArrayList<String>();
        prevSetsListDataChild = new HashMap<String, List<String>>();
        Iterator<Exercise> iterator = currentExercises.iterator();
        String exerciseName;
        List<String> childList = new ArrayList<String>();
        while(iterator.hasNext()) {
            //System.out.println(iterator.next());
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(iterator.next().getId_exerciseabs()).getName();
            exerciseListHeader.add(exerciseName);
            childList = new ArrayList<String>();
            for(int i = 0; i < 3; i++){
                childList.add("Hello");
            }
            prevSetsListDataChild.put(exerciseName,childList);
        }
    }*/

//--------------------New too
/*private void prepareListData() {
    exerciseListHeader = new ArrayList<String>();
    prevSetsListDataChild = new HashMap<String, List<String>>();

    // Adding child data
    exerciseListHeader.add("Top 250");
    exerciseListHeader.add("Now Showing");
    exerciseListHeader.add("Coming Soon..");

    // Adding child data
    List<String> top250 = new ArrayList<String>();
    top250.add("The Shawshank Redemption");
    top250.add("The Godfather");
    top250.add("The Godfather: Part II");
    top250.add("Pulp Fiction");
    top250.add("The Good, the Bad and the Ugly");
    top250.add("The Dark Knight");
    top250.add("12 Angry Men");

    List<String> nowShowing = new ArrayList<String>();
    nowShowing.add("The Conjuring");
    nowShowing.add("Despicable Me 2");
    nowShowing.add("Turbo");
    nowShowing.add("Grown Ups 2");
    nowShowing.add("Red 2");
    nowShowing.add("The Wolverine");

    List<String> comingSoon = new ArrayList<String>();
    comingSoon.add("2 Guns");
    comingSoon.add("The Smurfs 2");
    comingSoon.add("The Spectacular Now");
    comingSoon.add("The Canyons");
    comingSoon.add("Europa Report");

    prevSetsListDataChild.put(exerciseListHeader.get(0), top250); // Header, Child data
    prevSetsListDataChild.put(exerciseListHeader.get(1), nowShowing);
    prevSetsListDataChild.put(exerciseListHeader.get(2), comingSoon);
}*/

/*

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = inflater.inflate(R.layout.field, null);
        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }
    //expandable ListView
    //https://www.journaldev.com/9942/android-expandablelistview-example-tutorial
*/


