package com.example.tamirmishali.trainingmanager;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

public class WorkoutNow extends AppCompatActivity {

    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workoutnow);

        TextView textViewWorkoutName = findViewById(R.id.workoutnow_current_workout);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);

        try {
            Workout workout = workoutViewModel.getCurrentWorkout();
            textViewWorkoutName.setText(workout.getWorkoutName());
        } catch (Exception e) {
            Toast.makeText(this, "couldn't load last workout", Toast.LENGTH_SHORT).show();
        }
    }
}
