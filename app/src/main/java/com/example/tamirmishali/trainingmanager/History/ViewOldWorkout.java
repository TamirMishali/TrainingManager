package com.example.tamirmishali.trainingmanager.History;

//import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.ExpandableListAdapter;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;
import com.example.tamirmishali.trainingmanager.WorkoutNow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class ViewOldWorkout extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private SetViewModel setViewModel;
    private LinearLayout parentLinearLayout;
    private Workout currentWorkout;
    private Workout prevWorkout;
    private List<Exercise> currentExercises;
    private List<Exercise> prevExercises;
    private ArrayList<Set> currentSets;
    private List<Set> prevSets;
    private static final String TAG = WorkoutNow.class.getName();

    //-------------------------New
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<Set>> listDataChild;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.workoutnow_layout);
        parentLinearLayout = findViewById(R.id.parent_linear_layout);
        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);

        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);


        try {
            currentWorkout = workoutViewModel.getCurrentWorkout();
            prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(), currentWorkout.getWorkoutDate());
            currentExercises = exerciseViewModel.getExercisesForWorkout(currentWorkout.getId());
            prevExercises = exerciseViewModel.getExercisesForWorkout(prevWorkout.getId());
            if (currentExercises != null && prevExercises != null) {
                Log.i(TAG, currentExercises.get(0).getComment());
                Log.i(TAG, prevExercises.get(0).getComment());
            }
            prevSets = setViewModel.getSetsForWorkout(prevWorkout.getId());
            textViewWorkoutName.setText("Workout: " + currentWorkout.getWorkoutName());

        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }


        //---------------------------New
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        //prepareListData2();
        prepareListDataPrev();

        listAdapter = new ExpandableListAdapter(this, currentWorkout, prevWorkout,setViewModel,exerciseViewModel,exerciseAbstractViewModel); //FIX HERE THE SECOND listDataChild

        // setting list adapter
        expListView.setAdapter(listAdapter);


        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview Group collapsed listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });


    }





    private void prepareListDataPrev() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();
        Iterator<Exercise> iterator = currentExercises.iterator();
        String exerciseName = new String();
        List<Set> childList;
        Exercise exercise;

        while (iterator.hasNext()) {
            //System.out.println(iterator.next());
            exercise = iterator.next();
            exerciseName = exerciseAbstractViewModel.getExerciseAbsFromId(exercise.getId_exerciseabs()).generateExerciseAbstractName();
            listDataHeader.add(exerciseName);
            childList = setViewModel.getSetsForExercise(exercise.getId());

            listDataChild.put(exerciseName, childList);
        }
    }
}



