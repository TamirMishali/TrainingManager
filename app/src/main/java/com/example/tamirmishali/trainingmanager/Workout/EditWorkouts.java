package com.example.tamirmishali.trainingmanager.Workout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Dialogs;
import com.example.tamirmishali.trainingmanager.R;

import java.util.List;

public class EditWorkouts extends AppCompatActivity {

    public static final  String EXTRA_ROUTINE_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";
    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";
    public static final  String EXTRA_WORKOUT_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";
    public static final  String EXTRA_WORKOUT_DATE =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";

    public static final int ADD_WORKOUT_REQUEST = 1;
    public static final int EDIT_WORKOUT_REQUEST = 2;
    public static final int ADD_EXERCISE_REQUEST = 3;
    private int sourceRoutineID;
    private WorkoutViewModel workoutViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editworkouts_layout);
        setTitle("Workouts");

        //Get routine ID for saving the workout
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ROUTINE_ID)) {
            sourceRoutineID = intent.getIntExtra(EXTRA_ROUTINE_ID, -1);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }


        //Floating Plus button decleration
        FloatingActionButton buttonAddWorkout = findViewById(R.id.button_add_workout);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditWorkouts.this,AddEditWorkoutActivity.class); //AddEditWorkoutActivity
                startActivityForResult(intent,ADD_WORKOUT_REQUEST);
            }
        });

        //Init
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_workout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //WorkoutAdapter Decleration
        final WorkoutAdapter adapter = new WorkoutAdapter();
        recyclerView.setAdapter(adapter);

        //WorkoutViewModel Decleration
        workoutViewModel = ViewModelProviders.of(this).get(WorkoutViewModel.class);
        workoutViewModel.getWorkoutsForRoutine(sourceRoutineID).observe(this, new Observer<List<Workout>>() {
            @Override
            public void onChanged(@Nullable List<Workout> workouts) {
                adapter.setWorkouts(workouts);
            }
        });

        //---------------------------------ACTIONS--------------------------------
        //Delete workout
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder alert = new AlertDialog.Builder(EditWorkouts.this);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_workout_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        workoutViewModel.delete(adapter.getWorkoutAt(viewHolder.getAdapterPosition()));
                        Toast.makeText(EditWorkouts.this , "Workout deleted" , Toast.LENGTH_SHORT).show();

                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();

                        //https://stackoverflow.com/questions/31787272/android-recyclerview-itemtouchhelper-revert-swipe-and-restore-view-holder
                        adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);




        //Edit Workout name and date
        adapter.setOnItemLongClickListener(new WorkoutAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Workout workout) {
                Intent intent = new Intent(EditWorkouts.this, AddEditWorkoutActivity.class);
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID,workout.getId());
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME, workout.getWorkoutName());
                if(workout.getWorkoutDate() != null){
                    intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE, workout.getWorkoutDate().toString());
                }
                else{
                    intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE, "");
                }

                startActivityForResult(intent, EDIT_WORKOUT_REQUEST);
            }
        });

        //Add Exercises to that workout
        adapter.setOnItemClickListener(new WorkoutAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Workout workout) {
                Intent intent = new Intent(EditWorkouts.this, AddEditWorkoutActivity.class); //EditWorkouts
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID,workout.getId());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_NAME, workout.getWorkoutName());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_DATE, workout.getWorkoutDate().toString());
                startActivityForResult(intent, ADD_EXERCISE_REQUEST);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == ADD_EXERCISE_REQUEST && resultCode == RESULT_OK){
/*
            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);

            Workout workout = new Workout(workoutName, workoutDate);//, workoutDate);
            workoutViewModel.insert(workout);
*/

            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ADD_WORKOUT_REQUEST && resultCode == RESULT_OK){
            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);
            Workout workout = new Workout(sourceRoutineID ,workoutName, Boolean.TRUE);//, workoutDate);
            //Workout workout = new Workout(sourceRoutineID ,workoutName, workoutDate,Boolean.TRUE);//, workoutDate);
            workoutViewModel.insert(workout);
            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_WORKOUT_REQUEST && resultCode == RESULT_OK){
            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);
            Workout workout = new Workout(sourceRoutineID ,workoutName, workoutDate,Boolean.TRUE);//, workoutDate);
            workoutViewModel.update(workout);
            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this,"Workout NOT saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.editworkout_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_workouts:
                //workoutViewModel.deleteAllWorkouts();
                Toast.makeText(this, "All workouts deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
