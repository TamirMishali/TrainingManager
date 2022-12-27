package com.example.tamirmishali.trainingmanager.ExerciseAbstract;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.tamirmishali.trainingmanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddEditExerciseAbsActivity extends AppCompatActivity {
    public static final String EXTRA_EXERCISEABS_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_EXERCISEABS_ID";
    public static final String EXTRA_EXERCISEABS_NAME =
            "com.example.tamirmishali.trainingmanager.EXTRA_EXERCISEABS_NAME";
    public static final String EXTRA_EXERCISEABS_DESCRIPTION =
            "com.example.tamirmishali.trainingmanager.EXTRA_EXERCISEABS_DESCRIPTION";
    public static final String EXTRA_EXERCISEABS_MUSCLE =
            "com.example.tamirmishali.trainingmanager.EXTRA_EXERCISEABS_MUSCLE";
    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_ID";

    // new
    int sourceWorkoutID;
    private TextInputEditText textInputEditText_exName;
    private AutoCompleteTextView autoCompleteTextView_Muscle;
    private AutoCompleteTextView autoCompleteTextView_LoadType;
    private AutoCompleteTextView autoCompleteTextView_Position;
    private AutoCompleteTextView autoCompleteTextView_Angle;
    private AutoCompleteTextView autoCompleteTextView_GripWidth;
    private AutoCompleteTextView autoCompleteTextView_ThumbsDirection;
    private AutoCompleteTextView autoCompleteTextView_SeparateHands;

    // old
    private EditText editTextExerciseAbsName;
    private EditText editTextExerciseAbsDescription;
    private EditText editTextExerciseAbsMuscle;
    private Spinner spinnerMuscleGroup;
    private ExerciseAbstractViewModel exerciseAbstractViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exerciseabs_new);

        // new
        textInputEditText_exName = findViewById(R.id.textInputEditText_exName);
        autoCompleteTextView_Muscle = findViewById(R.id.autoCompleteTextView_Muscle);
        autoCompleteTextView_LoadType = findViewById(R.id.autoCompleteTextView_LoadType);
        autoCompleteTextView_Position = findViewById(R.id.autoCompleteTextView_Position);
        autoCompleteTextView_Angle = findViewById(R.id.autoCompleteTextView_Angle);
        autoCompleteTextView_GripWidth = findViewById(R.id.autoCompleteTextView_GripWidth);
        autoCompleteTextView_ThumbsDirection = findViewById(R.id.autoCompleteTextView_ThumbsDirection);
        autoCompleteTextView_SeparateHands = findViewById(R.id.autoCompleteTextView_SeparateHands);


        //new - Get workout ID for saving the exerciseabstract
        //      check why we got here: Edit/Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID) && (intent.hasExtra(EXTRA_EXERCISEABS_ID))){
            setTitle("Edit exercise");
            // Todo: set all relevant items and views
            editTextExerciseAbsName.setText(intent.getStringExtra(EXTRA_EXERCISEABS_NAME));
            editTextExerciseAbsDescription.setText(intent.getStringExtra(EXTRA_EXERCISEABS_DESCRIPTION));
            spinnerMuscleGroup.setSelection(spinnerValues.indexOf(intent.getStringExtra(EXTRA_EXERCISEABS_MUSCLE)));
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

        } else if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            setTitle("Add exercise");
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }


        /*TODO 26.12.2022:
                * add in the exerciseabs "save" button the following logics:
                  - Operation name: check if inserted operation_name exists for the following muscle.
                    if not, add it to the SQL table.
                  - Nickname: same as operation_name.
                * if "main_muscle" is null/empty/whatever, make all other views unreachable.
                * add a small 'i' button for information near operation_name to view all possible operations.
                * In the far future:
                    - add a "plus" button near each field (except sep_hands) to add
                      new attributes to the sql table.
                    - add menu buttons for deleting "operation_name"s, "nickname"s (not all, but from a list)
                    - add menu button for editing the load_type, position, angle etc...
                */
        exerciseAbstractViewModel = ViewModelProviders.of(this).get(ExerciseAbstractViewModel.class);

        editTextExerciseAbsName = findViewById(R.id.edit_text_Exerciseabs_name);
        editTextExerciseAbsDescription = findViewById(R.id.edit_text_Exerciseabs_description);
        //editTextExerciseAbsMuscle = findViewById(R.id.edit_text_Exerciseabs_muscle);
        spinnerMuscleGroup = findViewById(R.id.spinner_muscle_group);

        //spinner thing
        List<String> spinnerValues = new ArrayList<>();
        spinnerValues.add("");
        spinnerValues.addAll(exerciseAbstractViewModel.getMuscles());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, spinnerValues); //

        spinnerMuscleGroup.setAdapter(spinnerAdapter);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMuscleGroup.setAdapter(spinnerAdapter);

        spinnerMuscleGroup.setSelection(0, false);

        // Old
//        //check why we got here: Edit/Add
//        Intent intent = getIntent();
//        if (intent.hasExtra(EXTRA_EXERCISEABS_ID)) {
//            setTitle("Edit Exercise Info");
//            editTextExerciseAbsName.setText(intent.getStringExtra(EXTRA_EXERCISEABS_NAME));
//            editTextExerciseAbsDescription.setText(intent.getStringExtra(EXTRA_EXERCISEABS_DESCRIPTION));
//            spinnerMuscleGroup.setSelection(spinnerValues.indexOf(intent.getStringExtra(EXTRA_EXERCISEABS_MUSCLE)));
//        }else{
//            setTitle("Add Exercise Info");
//        }
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

    }

    private void saveExerciseAbs(){
        String exerciseAbsName = editTextExerciseAbsName.getText().toString();
        String exerciseAbsDescription = editTextExerciseAbsDescription.getText().toString();
        //String exerciseAbsMuscleGroup = editTextExerciseAbsMuscle.getText().toString();
        String exerciseAbsMuscleGroup = spinnerMuscleGroup.getSelectedItem().toString();

        if (exerciseAbsName.trim().isEmpty() || exerciseAbsMuscleGroup.trim().isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if (exerciseAbsDescription.trim().isEmpty()){
            exerciseAbsDescription = "";
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_EXERCISEABS_NAME,exerciseAbsName);
        data.putExtra(EXTRA_EXERCISEABS_DESCRIPTION,exerciseAbsDescription);
        data.putExtra(EXTRA_EXERCISEABS_MUSCLE,exerciseAbsMuscleGroup);

        int id = getIntent().getIntExtra(EXTRA_EXERCISEABS_ID, -1);
        if (id != -1) {
            data.putExtra(EXTRA_EXERCISEABS_ID, id);
        }
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_all_exerciseabstract_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_exerciseabs:
                saveExerciseAbs();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

