package com.example.tamirmishali.trainingmanager.ExerciseAbstract;


import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.tamirmishali.trainingmanager.R;

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

    private EditText editTextExerciseAbsName;
    private EditText editTextExerciseAbsDescription;
    private EditText editTextExerciseAbsMuscle;
    private Spinner spinnerMuscleGroup;

    private ExerciseAbstractViewModel exerciseAbstractViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exerciseabs);


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

        //check why we got here: Edit/Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_EXERCISEABS_ID)) {
            setTitle("Edit Exercise Info");
            editTextExerciseAbsName.setText(intent.getStringExtra(EXTRA_EXERCISEABS_NAME));
            editTextExerciseAbsDescription.setText(intent.getStringExtra(EXTRA_EXERCISEABS_DESCRIPTION));
            spinnerMuscleGroup.setSelection(spinnerValues.indexOf(intent.getStringExtra(EXTRA_EXERCISEABS_MUSCLE)));



        }else{
            setTitle("Add Exercise Info");
        }
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

