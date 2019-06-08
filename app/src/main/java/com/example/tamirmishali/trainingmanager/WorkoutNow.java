package com.example.tamirmishali.trainingmanager;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

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
    private List<Exercise> currentExercises;
    private List<Exercise> prevExercises;
    private ArrayList<Set> currentSets;
    private List<Set> prevSets;
    private static final String TAG = WorkoutNow.class.getName();

    //-------------------------New
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> exerciseListHeader;
    HashMap<String, List<Set>> prevSetsListDataChild;
    HashMap<String, List<Set>> currentSetsListDataChild;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workoutnow_layout);

        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        TextView textViewWorkoutDate = findViewById(R.id.workoutnow_workout_date);
        //getActionBar().setDisplayHomeAsUpEnabled(false);

        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);
        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        setViewModel = ViewModelProviders.of(this).get(SetViewModel.class);



        //Get workout ID for viewing the relevant workout
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)){
            String callingClassName = getCallingActivity().getShortClassName();
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

            //New Workout
            if(callingClassName.equals(".MainActivity")){
                prevWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                prevExercises = exerciseViewModel.getExercisesForWorkout(prevWorkout.getId());
                exerciseListHeader = prepareListHeader(prevExercises);

                //if this is not an abstract workout
                if(prevWorkout.getDate() != null /*|| prevWorkout.getDate().toString() != ""*/){
                    //take sets from it
                    prevSetsListDataChild = getPrevSetsListDataChild(prevExercises,exerciseListHeader);
                }
                //else - this is an Abstract workout, prevSets = NONE: cuz there's only abstract workout
                else{
                    prevSetsListDataChild = getPrevSetsListDataChild(prevExercises,exerciseListHeader);/*new HashMap<String>()*/
                }


                currentSetsListDataChild = new HashMap<>();
                constructNewWorkout(); // create workout

                //---------------------prevSets???----------------
            }

            //Existing Workout
            else if(callingClassName.equals(".ViewPracticalWorkouts")){
                currentWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                currentExercises = exerciseViewModel.getExercisesForWorkout(currentWorkout.getId());

                prevWorkout = workoutViewModel.getPrevWorkout(
                        currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
                prevExercises = exerciseViewModel.getExercisesForWorkout(prevWorkout.getId());
            }
        }
        //im on WorkoutNow Activity
        else{
            setResult(RESULT_CANCELED,intent);
            finish();
            //currentWorkout = workoutViewModel.getCurrentWorkout();
        }

        try {
            //currentWorkout = workoutViewModel.getCurrentWorkout();
            //prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
            //currentExercises = exerciseViewModel.getExercisesForWorkout(currentWorkout.getId());
            //prevExercises = exerciseViewModel.getExercisesForWorkout(prevWorkout.getId());
            /*if (currentExercises != null && prevExercises != null) {
                Log.d(TAG, currentExercises.get(0).getComment());
                Log.d(TAG, prevExercises.get(0).getComment());
            }*/
            //prevSets = setViewModel.getSetsForWorkout(prevWorkout.getId());
            textViewWorkoutName.setText("Workout: " + currentWorkout.getWorkoutName());
            textViewWorkoutDate.setText(currentWorkout.getDate().toString());

        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }


        //---------------------------New
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        //prepareListData2();
        /*if ()*/
        prepareListDataPrev();

        listAdapter = new ExpandableListAdapter(this, exerciseListHeader, prevSetsListDataChild, currentSetsListDataChild,
                setViewModel,exerciseViewModel,exerciseAbstractViewModel);

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // ListView Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + exerciseListHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // ListView Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        exerciseListHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // ListView Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        exerciseListHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // ListView on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        exerciseListHeader.get(groupPosition)
                                + " : "
                                + prevSetsListDataChild.get(
                                exerciseListHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
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
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).getName();
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
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).getName();
            listHeader.add(exerciseName);
        }
        return listHeader;
    }

    private void constructNewWorkout() {
        //insert workout
        Workout workout = new Workout(prevWorkout.getId_routine(), prevWorkout.getName(), false);
        workoutViewModel.insert(workout);
        currentWorkout = workoutViewModel.getNewestWorkout(workout.getId_routine(), workout.getName());


        //insert exercises as like in abstract
        List<Exercise> exercisesList;
        exercisesList = exerciseViewModel.getExercisesForWorkout(
                workoutViewModel.getAbstractWorkoutFromPractical(currentWorkout.getId_routine(), currentWorkout.getName()).getId());

        Iterator<Exercise> iteratorCurrentExercise = exercisesList.iterator();
        //Exercise exercise;
        while (iteratorCurrentExercise.hasNext()) {
            Exercise exercise = new Exercise(iteratorCurrentExercise.next());
            //exercise.setId(0); not necessary  cuz in constructor already
            exercise.setId_workout(currentWorkout.getId());
            exerciseViewModel.insert(exercise);
        }
        currentExercises = exerciseViewModel.getExercisesForWorkout(currentWorkout.getId());


        //sets
        //find each exercise in prev workout and if there are sets there,
        //take the amount and create that number of sets in current exercise
        //if not exist, don't create. let the adapter do it
        iteratorCurrentExercise = currentExercises.iterator();
        Iterator<Exercise> prevIterator;
        /*List<Set> prevSetsForCount*/
        int setsNo;
        //Set set;
        Exercise prevExercise;
        //find same ExerciseAbs in both currentWorkout and prevWorkout
        //don't forget that currentExercise contains the ex from absWorkout
        while(iteratorCurrentExercise.hasNext()) {
            Exercise currentExercise = iteratorCurrentExercise.next();
            Boolean foundFlag = Boolean.FALSE;
            //iterate over all exercises in prevWorkout and search for currentExercise
            prevIterator = prevExercises.iterator();

            while(prevIterator.hasNext()) {
                prevExercise = prevIterator.next();

                //if exists, create the same number of sets like prevExercise
                if(currentExercise.getId_exerciseabs() == prevExercise.getId_exerciseabs()){
                    setsNo = setViewModel.getSetsForExercise(prevExercise.getId()).size();

/*                    //no sets at all, its cuz abs workout
                    //put one set in it so it can start
                    if (setsNo == 0){
                        setsNo = 1;
                    }*/

                    for(int i=0 ; i<setsNo ; i++){
                        Set set = new Set(currentExercise.getId(),-1.0,-1);
                        setViewModel.insert(set);
                    }

                    List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
                    if (!setList.isEmpty() || setList != null)
                        currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(currentExercise.getId_exerciseabs()).getName(),setList);

                    foundFlag = Boolean.TRUE;
                    /*else
                        currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId()).getName(),null);*/
                    break;
                }
            }

            //if exercise from abs was not found in prev, i still want to make it
            //it probably created from a change in mid routine.
            //create one set of it.
            if (foundFlag == Boolean.FALSE){
/*                Set set = new Set(currentExercise.getId(),-1.0,-1);
                setViewModel.insert(set);*/
                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
                currentSetsListDataChild.put(exerciseAbstractViewModel.getExerciseAbsFromId(currentExercise.getId_exerciseabs()).getName(),setList);
            }
        }
    }



    private void prepareListDataPrev() {
        exerciseListHeader = new ArrayList<>();
        prevSetsListDataChild = new HashMap<>();
        Iterator<Exercise> iterator = currentExercises.iterator();
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


