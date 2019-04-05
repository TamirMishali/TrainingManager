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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstract;
import com.example.tamirmishali.trainingmanager.ExerciseAbstract.ExerciseAbstractViewModel;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkoutNow extends AppCompatActivity {

    private RecyclerView recyclerView;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private LinearLayout parentLinearLayout;
    private Workout workout;
    private List<Exercise> exercises;
    private static final String TAG = WorkoutNow.class.getName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout./*activity_workoutnow*/workoutnow_layout);
        parentLinearLayout = /*(LinearLayout)*/ findViewById(R.id.parent_linear_layout);

        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        exerciseAbstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);

        try {
            workout = workoutViewModel.getCurrentWorkout();
            textViewWorkoutName.setText("Workout: " +workout.getWorkoutName());
        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_workoutNow);
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
        });
    }




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

}
