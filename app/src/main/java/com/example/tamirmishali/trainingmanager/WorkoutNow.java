package com.example.tamirmishali.trainingmanager;

import static com.example.tamirmishali.trainingmanager.MainActivity.ACTION_FINISH_WORKOUT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class WorkoutNow extends AppCompatActivity {

    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";


    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private SetViewModel setViewModel;
    private Workout currentWorkout;
    private Workout prevWorkout;
    private Workout refWorkout;
    private int sourceWorkoutID;

    private static final String TAG = WorkoutNow.class.getName();

    private RecyclerView exerciseRecyclerView;
    private WorkoutRecyclerAdapter workoutRecyclerAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workoutnow_layout);

        TextView textViewRoutineName = findViewById(R.id.workoutnow_current_routine);
        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        TextView textViewWorkoutDate = findViewById(R.id.workoutnow_workout_date);

        // ViewModels
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);

        // Handle incoming intent and load workouts
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
            String callingClassName = getCallingActivity().getClassName();

            if (callingClassName.equals("com.example.tamirmishali.trainingmanager.MainActivity") && intent.getAction() == null) {
                prevWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId()), true));
                currentWorkout = constructNewWorkout(prevWorkout);
                refWorkout = constructRefWorkout(prevWorkout, currentWorkout);
            } else if (callingClassName.equals("com.example.tamirmishali.trainingmanager.History.ViewPracticalWorkouts") || Objects.equals(intent.getAction(), ACTION_FINISH_WORKOUT)) {
                currentWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
                currentWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(currentWorkout.getId()), true));
                prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
                if (prevWorkout == null) {
                    prevWorkout = workoutViewModel.getAbstractWorkoutFromPractical(currentWorkout.getId_routine(), currentWorkout.getWorkoutName());
                }
                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId()), true));
                refWorkout = constructRefWorkout(prevWorkout, currentWorkout);
            }
        } else {
            setResult(RESULT_CANCELED, intent);
            finish();
            return;
        }

        // Set header info
        try {
            textViewRoutineName.setText("Routine: " + routineViewModel.getRoutine(currentWorkout.getId_routine()).getRoutineName());
            textViewWorkoutName.setText("Workout: " + currentWorkout.getWorkoutName());
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
            Date d = new Date(currentWorkout.getDate().getTime());
            textViewWorkoutDate.setText(formatter.format(d));
        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }

        // Setup RecyclerView
        exerciseRecyclerView = findViewById(R.id.exercise_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        exerciseRecyclerView.setLayoutManager(layoutManager);

        workoutRecyclerAdapter = new WorkoutRecyclerAdapter(
                this,
                currentWorkout,
                refWorkout,
                setViewModel,
                exerciseViewModel,
                exerciseAbstractViewModel
        );

        exerciseRecyclerView.setAdapter(workoutRecyclerAdapter);
        workoutRecyclerAdapter.expandAll(); // Or conditionally based on filled sets

        // Attach drag-to-reorder helper
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new WorkoutItemTouchCallback(workoutRecyclerAdapter));
        itemTouchHelper.attachToRecyclerView(exerciseRecyclerView);
    }

//    @SuppressLint("SetTextI18n")
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.workoutnow_layout);
//
//        TextView textViewRoutineName = findViewById(R.id.workoutnow_current_routine);
//        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
//        TextView textViewWorkoutDate = findViewById(R.id.workoutnow_workout_date);
//
//        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
//        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
//        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
//        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
//        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);
//
//
//
//        //Get workout ID for viewing the relevant workout
//        Intent intent = getIntent();
//        if (intent.hasExtra(EXTRA_WORKOUT_ID)){
//            String callingClassName = getCallingActivity().getClassName(); //.getShortClassName();
//            Log.d(TAG, "Class name= " + callingClassName);
//            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
//
//            //New Workout that was picked from the list of workouts in current routine or id
//            // of unfinished workout to reload.
//            if(callingClassName.equals("com.example.tamirmishali.trainingmanager.MainActivity") && intent.getAction()==null){ //.toString().equals(ACTION_FINISH_WORKOUT)){
//                // Too: create prevWorkout such as it is not literally the previous workout, but it
//                //  contains the exercises that are in currentWorkout, but filled with the most
//                //  recent Set reps and weight data:
//                prevWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
//                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId()), true));
//
//                // Create reference workout and new empty workout with the right number of Sets:
//                currentWorkout = constructNewWorkout(prevWorkout);
//                refWorkout = constructRefWorkout(prevWorkout, currentWorkout);
//
//            }
//
//            //Existing Workout - finished workout from history
//            else if(callingClassName.equals("com.example.tamirmishali.trainingmanager.History.ViewPracticalWorkouts") || Objects.equals(intent.getAction(), ACTION_FINISH_WORKOUT)){
//                currentWorkout = workoutViewModel.getWorkout(sourceWorkoutID);
//                currentWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(currentWorkout.getId()), true));
//
//                prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
//                if (prevWorkout == null){
//                    prevWorkout = workoutViewModel.getAbstractWorkoutFromPractical(
//                            currentWorkout.getId_routine(),currentWorkout.getWorkoutName());
//                }
//
//                prevWorkout.setExercises(fillExerciseData(exerciseViewModel.getExercisesForWorkout(prevWorkout.getId()), true));
//                refWorkout = constructRefWorkout(prevWorkout, currentWorkout);
//            }
//        }
//        //im on WorkoutNow Activity
//        else{
//            setResult(RESULT_CANCELED,intent);
//            finish();
//            return;
//        }
//
//        // Insert names of routine, workout and date at the top of the screen
//        try {
//            textViewRoutineName.setText("Routine: " + routineViewModel.getRoutine(currentWorkout.getId_routine()).getRoutineName());
//            textViewWorkoutName.setText("Workout: " + currentWorkout.getWorkoutName());
//
//            //https://stackabuse.com/how-to-get-current-date-and-time-in-java/
//            SimpleDateFormat formatter= new SimpleDateFormat("dd-MM-yyyy 'at' HH:mm");
//            Date d = new Date(currentWorkout.getDate().getTime());
//            textViewWorkoutDate.setText(formatter.format(d));
//
//        } catch (Exception e) {
//            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
//        }
//
//
//        // get the listview
//        expListView = findViewById(R.id.lvExp);
//
//        listAdapter = new ExpandableListAdapter(this, currentWorkout, refWorkout,
//                setViewModel,exerciseViewModel,exerciseAbstractViewModel);
//        // setting list adapter
//        expListView.setAdapter(listAdapter);
//        // MY SAVIOUR!!!! Fixed the text jumps!!! <3<3<3 (06.12.2022)
//        // https://stackoverflow.com/questions/18632084/expandablelistview-child-items-edittext-cant-keep-focus
//        expListView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
//
//        if (!currentWorkout.getExercises().get(0).areAllSetsFilled()) {
//            int count = listAdapter.getGroupCount();
//            for (int i = 0; i < count; i++)
//                expListView.expandGroup(i);
//            // expListView.expandGroup(0);
//        }
//
//        // found the command that makes the keyboard go as closest to be under all header children:
//        // https://developer.android.com/develop/ui/views/touch-and-input/keyboard-input/visibility
//
//        // Try to change the focus to the ExpandableListView so when i click an element inside it,
//        //  it will already be focused, and the focus jumps inside it will be less noticeable.
//        //  current state: when clicking the "Reps" EditText, the focus changes to the
//        //  ExpandableListView first, then to the relevant child, and then to the first element in
//        //  the child's linear layout, which is the "Weight" EditText. I believe that if the
//        //  ExpandableListView element is programmatically focused first, it will solve it.
//        expListView.requestFocus();
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    private List<Exercise> fillExerciseData(List<Exercise> exerciseList, Boolean fillSets){
        for(int i=0; i<exerciseList.size(); i++){
            Exercise exercise = exerciseList.get(i);
            exercise.setExerciseAbstract(exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()));

            if (fillSets) {
                exercise.setSets(setViewModel.getSetsForExercise(exercise.getId()));
                if (exercise.getSets() == null) {
                    List<Set> sets = new ArrayList<>();
                    exercise.setSets(sets);
                }
            }
        }
        return exerciseList;
    }



    // This Workout is just a reference Workout that contains the most updated Exercises (with Sets)
    //   needed for Current workout. New Workout number of sets will be built
    // This function retrieve all the Exercises that needs to be in currentWorkout, according to
    // its Abstract Workout. then, each Exercise Sets data is retrieved according to last time
    // this exercise was executed in the current routine.
    private Workout constructRefWorkout(Workout prevWorkout, Workout newWorkout) {

        // Get AbstractWorkout in order to obtain its exercises:
        Workout absWorkout = workoutViewModel.getAbstractWorkoutFromPractical(prevWorkout.getId_routine(), prevWorkout.getWorkoutName());
        // populate Workout Exercises:
        absWorkout.setExercises(exerciseViewModel.getExercisesForWorkout(absWorkout.getId()));
        // and populate each Exercise ExerciseAbstract data: (abstract Workout doesn't have Sets)
        absWorkout.setExercises(fillExerciseData(absWorkout.getExercises(), false));

        // Init a list of the most recent Exercises in the DB that correspond to the current workout
        //  im starting right now:
        List<Exercise> mostRecentExercises = new ArrayList<>();

        // Get most updated absWorkout.exercise(i) record in DB from all workouts in a specific routine:
        // explanation: if i do calves raises in 2 workouts, A and B, i want to get the most
        //              recent data (reps and weights):
        for(int i=0; i<absWorkout.getExercises().size(); i++){
            // Exclude the one from current newWorkout cause we don't want it to reference itself.
            Exercise mostRecentExercise = exerciseViewModel.getMostRecentExerciseFromAllWorkoutsInRoutine(
                    absWorkout.getId_routine(),
                    newWorkout.getId(),
                    absWorkout.getExercises().get(i).getId_exerciseabs());

            // if record exists in DB, fill its sets
            if (mostRecentExercise != null){
                mostRecentExercise.setSets(setViewModel.getSetsForExercise(mostRecentExercise.getId()));

                // --- If N_Sets_ref and N_Sets_prev are different, i need to handle what happens in refWorkout:
                // Ref = 4, prev = 2 => new = 2. => remove 2 first sets from Ref (leave last 2)
                // Ref = 2, prev = 4 => new = 4. => fill 2 first sets with zeros in Ref
                int N_sets_ref = mostRecentExercise.getSets().size();
                int N_sets_prev = prevWorkout.getExercises().get(i).getSets().size();
                List<Set> adaptedSets = new ArrayList<>();

                if (N_sets_ref > N_sets_prev){
                    adaptedSets = mostRecentExercise.getSets().subList(N_sets_ref-N_sets_prev, N_sets_ref);
                }
                else{
                    // This is only for display, doesn't need any id's
                    Set newSet = new Set(0, 0, -1, -1);

                    // insert missing N_sets at the start of the array:
                    for (int j=0; j<N_sets_prev-N_sets_ref; j++){
                        adaptedSets.add(newSet);
                    }
                    // add the existing data to the end.
                    adaptedSets.addAll(mostRecentExercise.getSets());
                }
                mostRecentExercise.setSets(adaptedSets);
                mostRecentExercises.add(mostRecentExercise);

            }
            // Todo: need to understand if the next commented section is actually needed
//            // some old logic that i didn't want to understand. original code is commented below
//            else {
//                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
//                currentExercise.setSets(setList);
//            }
        }

        Workout refWorkout = new Workout(absWorkout.getId_routine(), absWorkout.getName(), false);
        refWorkout.setExercises(mostRecentExercises);

        return refWorkout;
    }


    // This function retrieve all the classes instances (Workout, Exercises, Sets) and insert them
    //  into the DB. At the end of this process, we get a Workout filled with Exercises that are
    //  defined in the Abstract Workout record, with the number of sets as done at the prev workout
    //  this exercises was done.
    private Workout constructNewWorkout(Workout prevWorkout) {
        Workout newWorkout;

        // create and insert workout
        Workout workout = new Workout(prevWorkout.getId_routine(), prevWorkout.getName(), false);
        workoutViewModel.insert(workout);

        // get most recent workout with routine_id and workoutName:
        newWorkout = workoutViewModel.getNewestWorkout(workout.getId_routine(), workout.getName());

        // --- Insert exercises like they show in abstract workout to new workout ---
        // Get Exercises from abstract workout:
        //  1. get abstract Workout from practical workout: absWO
        //  2. get exercises from abstract workout using absWO.id: exercisesList
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


        // --- Now, i have a new Workout that contains list of Exercises.
        // ---  each Exercise assigned newWorkout's id and has ExerciseAbs id.

        // for each Exercise in workout, set its ExerciseAbstract object using Exercise.getId_exerciseabs
//        // New:
//        newWorkout.setExercises(fillExerciseData(newWorkout.getExercises(), false));
        // Old
        for(int i=0; i<newWorkout.getExercises().size(); i++){
            // get exercise abstract
            ExerciseAbstract exerciseAbstract = exerciseAbstractViewModel.getExerciseAbsFromId(
                    newWorkout.getExercises().get(i).getId_exerciseabs());
            // fill its String fields
            exerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractIdsToStrings(exerciseAbstract);
            // assign it to Exercise in newWorkout
            newWorkout.getExercises().get(i).setExerciseAbstract(exerciseAbstract);
        }

        // --- To this point, i have a workout object, with a list of Exercises and each Exercise
        // ---  holds an ExerciseAbstract object with all the Strings.

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

            // NOTE: I don't use the new code cause  i want the same number of Sets that was defined
            //       at first place for a specific workout.

//            // Get most updated exercise record in DB from all workouts in a specific routine:
//            // explanation: if i do calves raises in 2 workouts, A and B, i want to get the most
//            //              recent data (reps and weights):
//            Exercise mostRecentExercise = exerciseViewModel.getMostRecentExerciseFromAllWorkoutsInRoutine(
//                    currentWorkout.getId_routine(), currentExercise.getId_exerciseabs());
//
//
//            if (mostRecentExercise != null){
//                // Fill Set data:
//                mostRecentExercise.setSets(setViewModel.getSetsForExercise(mostRecentExercise.getId()));
//
//                setsNo = mostRecentExercise.getSets().size();
//
//                // insert 'setsNo' of empty Sets to DB with currentExercise id
//                for(int i=0 ; i<setsNo ; i++){
//                    Set set = new Set(currentExercise.getId(),-1.0,-1);
//                    setViewModel.insert(set);
//                }
//
//                // get all the Sets that were just inserted to DB and populate currentExercise with them:
//                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
//                if (!setList.isEmpty())
//                    currentExercise.setSets(setList);
//            }
//
//            // some old logic that i didn't want to understand. original code is commented below
//            if (mostRecentExercise == null){
//                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
//                currentExercise.setSets(setList);
//            }


            // Old Code for searching manually through data instead of running SQL query
            boolean foundFlag = Boolean.FALSE;

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

                    foundFlag = Boolean.TRUE;

                    break;
                }
            }

            // Past Tamir:
            // If exercise from abs was not found in prev, i still want to make it
            // It probably created from a change in mid routine.
            // Create one Set of it:

            // Present Tamir:
            // What past Tamir wrote does not make sense.
            // All i do here is assign an empty Set list:
            if (foundFlag == Boolean.FALSE){
                List<Set> setList = setViewModel.getSetsForExercise(currentExercise.getId());
                currentExercise.setSets(setList);
            }
        }

        return newWorkout;
    }

}



