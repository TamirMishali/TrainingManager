package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.R;
import com.example.tamirmishali.trainingmanager.Workout.AddEditWorkoutActivity;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutAdapter;

import java.util.List;

// This class is showing exerciseAbstracts for a specific workout
// For reaching the list of all exercisesAbstracts, there is a button "buttonAddWorkout"
public class EditExercisesAbstract extends AppCompatActivity {

    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_ID";
    public static final  String EXTRA_WORKOUT_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_NAME";
    public static final  String EXTRA_WORKOUT_DATE =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";

    public static final int ADD_WORKOUT_REQUEST = 1;
    public static final int EDIT_WORKOUT_REQUEST = 2;
    public static final int ADD_EXERCISEABS_REQUEST = 3;
    private int sourceWorkoutID;
    private String sourceWorkoutName;
    //private WorkoutViewModel workoutViewModel;
    private ExerciseAbstractViewModel exerciseabstractViewModel;
    private ExerciseViewModel exerciseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editexerciseabs_layout);

        //Get workout ID for saving the exerciseabstract
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
            sourceWorkoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }

        //Title
        setTitle(sourceWorkoutName);

        //Floating Plus button declaration
        FloatingActionButton buttonAddWorkout = findViewById(R.id.button_add_exerciseabs);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditExercisesAbstract.this, AddExerciseAbstractToWorkoutActivity.class);
                intent.putExtra(AddExerciseAbstractToWorkoutActivity.EXTRA_WORKOUT_ID,sourceWorkoutID);
                startActivityForResult(intent,ADD_EXERCISEABS_REQUEST);
            }
        });

        //Init
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_exerciseabs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ExerciseAbstract Adapter Declaration
        final ExerciseAbstractAdapter adapter = new ExerciseAbstractAdapter();
        recyclerView.setAdapter(adapter);

        //ExerciseAbstractViewModel Declaration
        exerciseabstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);
        exerciseabstractViewModel.getExerciseAbstractsForWorkout(sourceWorkoutID).observe(this, new Observer<List<ExerciseAbstract>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseAbstract> exerciseAbstracts) {
                adapter.setExerciseAbstracts(exerciseAbstracts);
            }
        });

        //ExerciseViewModel Declaration
        exerciseViewModel = ViewModelProviders.of(this).get(ExerciseViewModel.class);
        exerciseViewModel.getAllExercises();

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
                AlertDialog.Builder alert = new AlertDialog.Builder(EditExercisesAbstract.this);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_exerciseabstract_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ExerciseAbstract exerciseAbstract = (adapter.getExerciseAbstractAt(viewHolder.getAdapterPosition()));
                        Exercise exercise = exerciseViewModel.getExerciseForWorkout(exerciseAbstract.getId(),sourceWorkoutID);
                        exerciseViewModel.delete(exercise);
                        Toast.makeText(EditExercisesAbstract.this , "Exercise deleted" , Toast.LENGTH_SHORT).show();
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
        adapter.setOnItemLongClickListener(new ExerciseAbstractAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ExerciseAbstract exerciseabstract) {
/*                Intent intent = new Intent(EditExercisesAbstract.this, AddEditExerciseAbstractActivity.class);
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID,exerciseabstract.getId());
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME, exerciseabstract.getName());
                if(workout.getWorkoutDate() != null){
                    intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE, exerciseabstract.getWorkoutDate().toString());
                }
                else{
                    intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE, "");
                }

                startActivityForResult(intent, EDIT_WORKOUT_REQUEST);*/
            }
        });

        //Add Exercises to that workout
        adapter.setOnItemClickListener(new ExerciseAbstractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ExerciseAbstract exerciseabstract) {
/*                Intent intent = new Intent(EditExercisesAbstract.this, AddExerciseAbstractToWorkoutActivity.class); //EditWorkouts
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID,exerciseabstract.getId());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_NAME, workout.getWorkoutName());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_DATE, workout.getWorkoutDate().toString());
                startActivityForResult(intent, ADD_EXERCISEABS_REQUEST);*/
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == ADD_EXERCISEABS_REQUEST && resultCode == RESULT_OK){
/*
            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);

            Workout workout = new Workout(workoutName, workoutDate);//, workoutDate);
            workoutViewModel.insert(workout);
*/

            //Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == ADD_WORKOUT_REQUEST && resultCode == RESULT_OK){
/*            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);
            Workout workout = new Workout(sourceWorkoutID,workoutName, Boolean.TRUE);//, workoutDate);
            //Workout workout = new Workout(sourceWorkoutID ,workoutName, workoutDate,Boolean.TRUE);//, workoutDate);
            exerciseabstractViewModel.insert(workout);
            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();*/
        }
        else if(requestCode == EDIT_WORKOUT_REQUEST && resultCode == RESULT_OK){
/*            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);
            Workout workout = new Workout(sourceWorkoutID,workoutName, workoutDate,Boolean.TRUE);//, workoutDate);
            exerciseabstractViewModel.update(workout);
            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();*/
        }
/*        else {
            Toast.makeText(this,"Exercise NOT saved", Toast.LENGTH_SHORT).show();
        }*/

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
            case R.id.delete_all_exerciseabstract:
                exerciseabstractViewModel.deleteAllExerciseAbstracts();
                Toast.makeText(this, "All exercises deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
