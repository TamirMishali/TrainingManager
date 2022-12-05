package com.example.tamirmishali.trainingmanager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.History.History;
import com.example.tamirmishali.trainingmanager.Routine.EditRoutines;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import static android.widget.Toast.*;


/*TODO
*
* */

public class MainActivity extends AppCompatActivity /*implements WorkoutNow_DialogListViewAdapter.CustomDialogListener*/{
    public static final int EDIT_LAST_WORKOUT = 1;
    protected static final String ACTION_FINISH_WORKOUT = "finish_workout";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    LiveData<List<Routine>> routines;

    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private SetViewModel setViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        routineViewModel = ViewModelProviders.of(this).get(RoutineViewModel.class);
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        setViewModel = ViewModelProviders.of(this).get(SetViewModel.class);

        ImageButton button_WorkoutNow = findViewById(R.id.imageButton_WorkoutNowActivity);
        ImageButton button_History = findViewById(R.id.imageButton_History);
        ImageButton button_EditWorkout = findViewById(R.id.imageButton_EditActivity);




        //Main
        // When clicking on the main menu "Workout":
        // if no routines exists, raise notification
        // else, open the last unfinished workout...
        button_WorkoutNow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (routineViewModel.getFirstRoutine() == null) {
                    Toast.makeText(MainActivity.this, "No Routines!", Toast.LENGTH_SHORT).show();
                } else {
                    Workout workout = workoutViewModel.getLastWorkout();
                    List<Set> sets = new ArrayList<>();
                    if (workout == null)
                        open_dialog(v);
                    else
                        sets = setViewModel.getUnfilledSetsForWorkout(workout.getId());
                    if (sets.isEmpty() || sets == null) { //last workout filled properly
                        //Open Dialog of relevant workouts and their dates
                        open_dialog(v);
                    } else {//missing data in sets of last workout
                        Intent intent = new Intent(v.getContext(), WorkoutNow.class);
                        intent.putExtra(WorkoutNow.EXTRA_WORKOUT_ID, workout.getId());
                        intent.setAction(ACTION_FINISH_WORKOUT);
                        Toast.makeText(MainActivity.this, "Last Workout still in progress", Toast.LENGTH_SHORT).show();
                        startActivityForResult(intent, EDIT_LAST_WORKOUT);
                    }
                }
            }
        });

        button_History.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), History.class);
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


    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == EDIT_LAST_WORKOUT && resultCode == RESULT_OK){
/*            String routineName = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_NAME);
            String routineDate = data.getStringExtra(AddEditRoutineActivity.EXTRA_ROUTINE_DATE);
            Routine routine = new Routine(routineName, routineDate);//, routineDate);
            routineViewModel.insert(routine);*/
            //Toast.makeText(this,"Nothing and nothing", Toast.LENGTH_SHORT).show();
        }

        else {
            //Toast.makeText(this,"Nothing and nothing", Toast.LENGTH_SHORT).show();
        }
    }

    private Boolean dataValidation() {
        //check for existing routines
        routines = routineViewModel.getAllRoutines();
        if (routines.getValue().isEmpty()) {
            Toast.makeText(this,"No routines",Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }

        //check for existing workouts


        //check for at least one exercise exists in workout
        //check for full sets fields

        return Boolean.TRUE;
    }

    public void open_dialog(View v){
        List<Workout> lastPracticalWorkouts;
        lastPracticalWorkouts = workoutViewModel.getWorkoutsForDialog(workoutViewModel.getNewestRoutineId());
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.workoutnow_custom_listview,null);
        final ListView listView = (ListView)row.findViewById((R.id.workoutnow_listview));
        WorkoutNow_DialogListViewAdapter workoutNow_dialogListViewAdapter = new WorkoutNow_DialogListViewAdapter(this,lastPracticalWorkouts);
        listView.setAdapter(workoutNow_dialogListViewAdapter);

        builder.setView(row);


        final AlertDialog dialog = builder.create();
        dialog.show();

        final List<Workout> finalLastPracticalWorkouts = lastPracticalWorkouts;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                dialog.cancel();
                if (dialog.isShowing()){
                    dialog.dismiss();
                    dialog.cancel();
                }
                int workoutId = finalLastPracticalWorkouts.get(position).getId();
                Intent intent = new Intent(MainActivity.this, WorkoutNow.class);
                intent.putExtra(WorkoutNow.EXTRA_WORKOUT_ID,workoutId);
                startActivityForResult(intent, EDIT_LAST_WORKOUT);

            }
        });

    }
}
