package com.example.tamirmishali.trainingmanager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import com.example.tamirmishali.trainingmanager.Routine.EditRoutines;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    /*private RoutineViewModel routineViewModel;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Init
/*        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        routineViewModel.getAllRoutines().observe(this, new Observer<List<Routine>>() {
            @Override
            public void onChanged(@Nullable List<Routine> routines) {
                //update RecyclerView
            }
        });*/

        ImageButton button_Workout = findViewById(R.id.imageButton_WorkoutActivity);
        ImageButton button_EditWorkout = findViewById(R.id.imageButton_EditActivity);


        //Main
        button_Workout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), WorkoutNow.class);
                startActivity(intent);
                }
        });

        button_EditWorkout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditRoutines.class);
                startActivity(intent);
            }
        });


    }
}
