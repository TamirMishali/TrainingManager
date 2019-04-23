package com.example.tamirmishali.trainingmanager;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WorkoutNow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private LinearLayout parentLinearLayout;
    private Workout currentWorkout;
    private Workout prevWorkout;
    private List<Exercise> exercises;
    private static final String TAG = WorkoutNow.class.getName();

    //-------------------------New
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private List<Workout> workouts;
    private List<Routine> routines;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout./*activity_workoutnow*/workoutnow_layout);
        parentLinearLayout = /*(LinearLayout)*/ findViewById(R.id.parent_linear_layout);



        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);
        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);


/*        //routineViewModel.getAllRoutines().observe(this, new Observer<List<Routine>>() {
        routineViewModel.getAllRoutines().observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(@Nullable List<Routine> routiness) {
                if(routiness == null || routiness.size() == 0) {
                    // No data in your database, call your api for data
                } else {
                    routines = routiness;
                    Routine r = routines.get(0);
                    workouts = workoutViewModel.getWorkoutsForRoutine(r.getUid()).getValue();
                    // One or more items retrieved, no need to call your api for data.
                }
            }
        });*/

        try {
            currentWorkout = workoutViewModel.getCurrentWorkout();
            prevWorkout = workoutViewModel.getPrevWorkout(currentWorkout.getWorkoutName(),currentWorkout.getWorkoutDate());
            textViewWorkoutName.setText("Workout: " +currentWorkout.getWorkoutName());
            /*routines = routineViewModel.getAllRoutines().getValue();
            workouts = workoutViewModel.getWorkoutsForRoutine(routines.get(0).getUid()).getValue();*/

        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }

/*        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_workoutNow);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        final ExpandableRecyclerAdapter adapter = new ExpandableRecyclerAdapter();
        recyclerView.setAdapter(adapter);


        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getExercisesForWorkout(workout.getId()).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                adapter.setExercises(exercises);
            }
        });*/




        //---------------------------New
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

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
/*                final TextView textViewPrev = findViewById(R.id.ListHeaderPrev);
                final TextView textViewNow = findViewById(R.id.ListHeaderNow);
                textViewNow.setVisibility(View.VISIBLE);
                textViewPrev.setVisibility(View.VISIBLE);*/

            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();
/*                final TextView textViewPrev = findViewById(R.id.ListHeaderPrev);
                final TextView textViewNow = findViewById(R.id.ListHeaderNow);
                textViewNow.setVisibility(View.GONE);
                textViewPrev.setVisibility(View.GONE);*/

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

    private void vissibilityOfAllGroups(){

    }
    private void observeExercises() {
        exerciseViewModel.getExercisesForWorkout(currentWorkout.getId()).observe(this, new Observer<List<Exercise>>() {
            @Override
            public void onChanged(@Nullable List<Exercise> exercises) {
                Toast.makeText(WorkoutNow.this, "something", Toast.LENGTH_SHORT).show();
            }
        });
    }

//--------------------New too
private void prepareListData() {
    listDataHeader = new ArrayList<String>();
    listDataChild = new HashMap<String, List<String>>();

    // Adding child data
    listDataHeader.add("Top 250");
    listDataHeader.add("Now Showing");
    listDataHeader.add("Coming Soon..");

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

    listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
    listDataChild.put(listDataHeader.get(1), nowShowing);
    listDataChild.put(listDataHeader.get(2), comingSoon);
}

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

}
