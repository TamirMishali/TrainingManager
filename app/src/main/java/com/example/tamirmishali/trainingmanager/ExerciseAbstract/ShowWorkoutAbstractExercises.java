package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import static com.example.tamirmishali.trainingmanager.ExerciseAbstract.AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_ID;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.ListIterator;

// todo:
//  - (HIGH) add number of sets and reps range

// This class is showing exerciseAbstracts for a specific workout
// For reaching the list of all exercisesAbstracts, there is a button "buttonAddWorkout"
public class ShowWorkoutAbstractExercises extends AppCompatActivity {

    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_ID";
    public static final  String EXTRA_WORKOUT_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_NAME";
    public static final  String EXTRA_WORKOUT_DATE =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";

//    public static final int ADD_WORKOUT_REQUEST = 1;
//    public static final int EDIT_WORKOUT_REQUEST = 2;
    public static final int EDIT_EXERCISEABS_REQUEST = 2;
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

        //Get workout ID for saving the exerciseAbstract
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
            sourceWorkoutName = intent.getStringExtra(EXTRA_WORKOUT_NAME);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }

        //Title
        setTitle("Exercises of: " + sourceWorkoutName);

        //Floating Plus button declaration
        FloatingActionButton buttonAddWorkout = findViewById(R.id.button_add_exerciseabs);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                // Old code
//                Intent intent = new Intent(ShowWorkoutAbstractExercises.this, ShowAllAbstractExercises.class);
//                intent.putExtra(ShowAllAbstractExercises.EXTRA_WORKOUT_ID,sourceWorkoutID);
//                startActivityForResult(intent,ADD_EXERCISEABS_REQUEST);

                // New code after new exercise db change:
                Intent intent = new Intent(ShowWorkoutAbstractExercises.this, AddEditExerciseAbsActivity.class);
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_WORKOUT_ID,sourceWorkoutID);
                startActivityForResult(intent,ADD_EXERCISEABS_REQUEST);
            }
        });

        //Init
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_exerciseabs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //ExerciseAbstract Adapter Declaration
        final ExerciseAbstractAdapter exerciseAbstractAdapter = new ExerciseAbstractAdapter();
        recyclerView.setAdapter(exerciseAbstractAdapter);

        //ExerciseAbstractViewModel Declaration
        exerciseabstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
        exerciseabstractViewModel.getExerciseAbstractsForWorkout(sourceWorkoutID).observe( this, new Observer<List<ExerciseAbstract>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseAbstract> exerciseAbstracts) {
                for (ListIterator<ExerciseAbstract> iterator = exerciseAbstracts.listIterator(); iterator.hasNext(); ) {
                    ExerciseAbstract exerciseAbstract = iterator.next();
                    iterator.set(exerciseabstractViewModel.ExerciseAbstractIdsToStrings(exerciseAbstract));

                }
                exerciseAbstractAdapter.setExerciseAbstracts(exerciseAbstracts);
            }
        });

        //ExerciseViewModel Declaration
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
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
                AlertDialog.Builder alert = new AlertDialog.Builder(ShowWorkoutAbstractExercises.this);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_exerciseabstract_dialog);

                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ExerciseAbstract exerciseAbstract = (exerciseAbstractAdapter.getExerciseAbstractAt(viewHolder.getAdapterPosition()));
                        Exercise exercise = exerciseViewModel.getExerciseForWorkout(exerciseAbstract.getId(),sourceWorkoutID);

                        // Delete the exercise from exercise_table:
                        exerciseViewModel.delete(exercise);

                        // Delete the exercise from exerciseabs_table so it wont be a ghost without
                        //  a pointer in DB:
                        exerciseabstractViewModel.delete(exerciseAbstract);

                        Toast.makeText(ShowWorkoutAbstractExercises.this , "Exercise deleted" , Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();

                        //https://stackoverflow.com/questions/31787272/android-recyclerview-itemtouchhelper-revert-swipe-and-restore-view-holder
                        exerciseAbstractAdapter.notifyItemChanged(viewHolder.getAdapterPosition());

                    }
                });
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);




        //Edit Workout name and date
        exerciseAbstractAdapter.setOnItemLongClickListener(new ExerciseAbstractAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(ExerciseAbstract exerciseabstract) {
                Intent intent = new Intent(ShowWorkoutAbstractExercises.this, AddEditExerciseAbsActivity.class);
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_WORKOUT_ID, sourceWorkoutID);
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_ID, exerciseabstract.getId());
                startActivityForResult(intent, EDIT_EXERCISEABS_REQUEST);
/*

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
        exerciseAbstractAdapter.setOnItemClickListener(new ExerciseAbstractAdapter.OnItemClickListener() {
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

            Toast.makeText(this, "Exercise saved!", Toast.LENGTH_SHORT).show();
            /*
            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);

            Workout workout = new Workout(workoutName, workoutDate);//, workoutDate);
            workoutViewModel.insert(workout);
*/

            //Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();
        }
/*        else if(requestCode == ADD_WORKOUT_REQUEST && resultCode == RESULT_OK){
*//*            String workoutName = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_NAME);
            String workoutDate = data.getStringExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_DATE);
            Workout workout = new Workout(sourceWorkoutID,workoutName, Boolean.TRUE);//, workoutDate);
            //Workout workout = new Workout(sourceWorkoutID ,workoutName, workoutDate,Boolean.TRUE);//, workoutDate);
            exerciseabstractViewModel.insert(workout);
            Toast.makeText(this,"Workout saved", Toast.LENGTH_SHORT).show();*//*
        }*/
        else if(requestCode == EDIT_EXERCISEABS_REQUEST && resultCode == RESULT_OK){
            Toast.makeText(this, "Exercise edited!", Toast.LENGTH_SHORT).show();
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.editworkout_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.delete_all_exerciseabstract:
//                exerciseabstractViewModel.deleteAllExerciseAbstracts(sourceWorkoutID);
//                Toast.makeText(this, "All exercises deleted", Toast.LENGTH_SHORT).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

}
