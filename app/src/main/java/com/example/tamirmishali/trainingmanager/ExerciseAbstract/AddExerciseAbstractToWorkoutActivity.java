package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.R;

import java.util.List;

public class AddExerciseAbstractToWorkoutActivity extends AppCompatActivity {
    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_ROUTINE_ID";

    public static final int ADD_NEW_EXERCISEABS_REQUEST = 1;
    public static final int EDIT_NEW_EXERCISEABS_REQUEST = 3;

    private int sourceWorkoutID;
    private ExerciseAbstractViewModel exerciseabstractViewModel;
    private ExerciseViewModel exerciseViewModel;
    final ExerciseAbstractAdapter adapter = new ExerciseAbstractAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editallexerciseabs_layout);
        setTitle("All Exercises");

        //Get workout ID for saving the exerciseabstract
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }

        //Floating Plus button declaration
        FloatingActionButton buttonAddWorkout = findViewById(R.id.button_add_allexerciseabs);
        buttonAddWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddExerciseAbstractToWorkoutActivity.this, AddEditExerciseAbsActivity.class);
                startActivityForResult(intent,ADD_NEW_EXERCISEABS_REQUEST);
            }
        });

        //Init
        final RecyclerView recyclerView = findViewById(R.id.recycler_view_allexerciseabs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        /*recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));*/

        //WorkoutAdapter Declaration
        //final ExerciseAbstractAdapter adapter = new ExerciseAbstractAdapter();
        recyclerView.setAdapter(adapter);

        //ExerciseAbstractViewModel Declaration
        exerciseabstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);
        exerciseabstractViewModel.getAllExerciseAbstracts().observe(this, new Observer<List<ExerciseAbstract>>() {
            @Override
            public void onChanged(@Nullable List<ExerciseAbstract> exerciseAbstracts) {
                adapter.setExerciseAbstracts(exerciseAbstracts);
            }
        });

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
                AlertDialog.Builder alert = new AlertDialog.Builder(AddExerciseAbstractToWorkoutActivity.this);
                alert.setTitle(R.string.delete_entry_dialog_title);
                alert.setMessage(R.string.delete_exerciseabstract_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        ExerciseAbstract exerciseAbstract = (adapter.getExerciseAbstractAt(viewHolder.getAdapterPosition()));
                        exerciseabstractViewModel.delete(exerciseAbstract);

                        Toast.makeText(AddExerciseAbstractToWorkoutActivity.this , "Exercise deleted" , Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(AddExerciseAbstractToWorkoutActivity.this, AddEditExerciseAbsActivity.class);
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_ID, exerciseabstract.getId());
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_NAME, exerciseabstract.getName());
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_DESCRIPTION, exerciseabstract.getDescription());
                intent.putExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_MUSCLE, exerciseabstract.getMuscleGroup());
                startActivityForResult(intent, EDIT_NEW_EXERCISEABS_REQUEST);
            }
        });

        //Add Exercises to that workout
        adapter.setOnItemClickListener(new ExerciseAbstractAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final ExerciseAbstract exerciseabstract) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AddExerciseAbstractToWorkoutActivity.this);
                alert.setTitle(R.string.add_entry_dialog_title);
                alert.setMessage(R.string.add_exercise_dialog);
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with add
                        if (exerciseViewModel.getExerciseForWorkout(exerciseabstract.getId(),sourceWorkoutID) == null){ //doesn't exist in workout
                            Exercise exercise = new Exercise(exerciseabstract.getId(),sourceWorkoutID,"");
                            exerciseViewModel.insert(exercise);
                            Toast.makeText(AddExerciseAbstractToWorkoutActivity.this , "Exercise Added" , Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(AddExerciseAbstractToWorkoutActivity.this , "Exercise Exists in this workout" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();
            }



/*                Intent intent = new Intent(EditExercisesAbstract.this, AddExerciseAbstractToWorkoutActivity.class); //EditWorkouts
                intent.putExtra(AddEditWorkoutActivity.EXTRA_WORKOUT_ID,exerciseabstract.getId());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_NAME, workout.getWorkoutName());
                //intent.putExtra(AddEditWorkoutActivity.EXTRA_ROUTINE_DATE, workout.getWorkoutDate().toString());
                startActivityForResult(intent, ADD_EXERCISEABS_REQUEST);*/
            });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == ADD_NEW_EXERCISEABS_REQUEST && resultCode == RESULT_OK){
            String exerciseAbsName = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_NAME);
            String exerciseAbsDescription = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_DESCRIPTION);
            String exerciseAbsMuscle = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_MUSCLE);
            ExerciseAbstract exerciseAbstract = new ExerciseAbstract(exerciseAbsName, exerciseAbsDescription,exerciseAbsMuscle);
            try{
                exerciseabstractViewModel.insert(exerciseAbstract);
                Toast.makeText(this,"Exercise saved", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){
                Toast.makeText(this,"Error Saving Exercise", Toast.LENGTH_SHORT).show();
            }

        }
        else if (requestCode == EDIT_NEW_EXERCISEABS_REQUEST && resultCode == RESULT_OK) {
            int exerciseAbsId = data.getIntExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_ID,-1);
            String exerciseAbsName = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_NAME);
            String exerciseAbsDescription = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_DESCRIPTION);
            String exerciseAbsMuscle = data.getStringExtra(AddEditExerciseAbsActivity.EXTRA_EXERCISEABS_MUSCLE);
            ExerciseAbstract exerciseAbstract = new ExerciseAbstract(exerciseAbsId,exerciseAbsName, exerciseAbsDescription,exerciseAbsMuscle);
            if (exerciseAbsId != -1){
                try{
                    exerciseabstractViewModel.update(exerciseAbstract);
                    Toast.makeText(this,"Exercise Edited", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(this,"Error Editing Exercise", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            //Toast.makeText(this,"Exercise NOT saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.exerciseabstract_all_search,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        android.support.v7.widget.SearchView searchView = (android.support.v7.widget.SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_exerciseabstract:
                //exerciseabstractViewModel.deleteAllExerciseAbstracts();
                exerciseViewModel.deleteAllExercises(sourceWorkoutID);
                Toast.makeText(this, "All exercises deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

}
