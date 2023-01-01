package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

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
            * Not sure about it but if needed:
              add an observer over ExerciseAbstract Nickname/Operation/InfoValue to activity and use
              The "LifeCycle" option in order to kill the liveData object from keep updating even
              after activity closed. The observer
              https://developer.android.com/topic/libraries/architecture/livedata#observe_livedata_objects
            */

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
    private ExerciseAbstract currentExerciseAbstract = new ExerciseAbstract();
    int sourceWorkoutID;
    private TextInputEditText textInputEditText_exName;
    private AutoCompleteTextView autoCompleteTextView_Operation;
    private AutoCompleteTextView autoCompleteTextView_Nickname;
    private AutoCompleteTextView autoCompleteTextView_Muscle;
    private AutoCompleteTextView autoCompleteTextView_LoadType;
    private AutoCompleteTextView autoCompleteTextView_Position;
    private AutoCompleteTextView autoCompleteTextView_Angle;
    private AutoCompleteTextView autoCompleteTextView_GripWidth;
    private AutoCompleteTextView autoCompleteTextView_ThumbsDirection;
    private AutoCompleteTextView autoCompleteTextView_SeparateHands;


    private ExerciseAbstractViewModel exerciseAbstractViewModel;
    private ExerciseViewModel exerciseViewModel;

    private List<String> operationValues = new ArrayList<>();
    private List<String> nicknameValues = new ArrayList<>();

    // todo: in order to query again the load_type, position, etc according to usage frequency, ill
    //  need to define their arraylist<String> here in the class global variables.

    // old
//    private EditText editTextExerciseAbsName;
//    private EditText editTextExerciseAbsDescription;
//    private EditText editTextExerciseAbsMuscle;
//    private Spinner spinnerMuscleGroup;
//    private ExerciseAbstractViewModel exerciseAbstractViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_exerciseabs);

        // new
        textInputEditText_exName = findViewById(R.id.textInputEditText_exName);
        autoCompleteTextView_Operation = findViewById(R.id.autoCompleteTextView_operationName);
        autoCompleteTextView_Nickname = findViewById(R.id.autoCompleteTextView_Nickname);
        autoCompleteTextView_Muscle = findViewById(R.id.autoCompleteTextView_Muscle);
        autoCompleteTextView_LoadType = findViewById(R.id.autoCompleteTextView_LoadType);
        autoCompleteTextView_Position = findViewById(R.id.autoCompleteTextView_Position);
        autoCompleteTextView_Angle = findViewById(R.id.autoCompleteTextView_Angle);
        autoCompleteTextView_GripWidth = findViewById(R.id.autoCompleteTextView_GripWidth);
        autoCompleteTextView_ThumbsDirection = findViewById(R.id.autoCompleteTextView_ThumbsDirection);
        autoCompleteTextView_SeparateHands = findViewById(R.id.autoCompleteTextView_SeparateHands);


        // Free text views:
        ArrayAdapter<String> operationAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, operationValues);
        autoCompleteTextView_Operation.setAdapter(operationAdapter);

        ArrayAdapter<String> nicknameAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, nicknameValues);
        autoCompleteTextView_Nickname.setAdapter(nicknameAdapter);


        // DropDowns views:
        List<String> muscleValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("muscle"));
        ArrayAdapter<String> muscleAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, muscleValues);
        autoCompleteTextView_Muscle.setAdapter(muscleAdapter);

        List<String> loadTypeValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("load_type"));
        ArrayAdapter<String> loadTypeAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, loadTypeValues);
        autoCompleteTextView_LoadType.setAdapter(loadTypeAdapter);

        List<String> positionValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("position"));
        ArrayAdapter<String> positionAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, positionValues);
        autoCompleteTextView_Position.setAdapter(positionAdapter);

        List<String> angleValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("angle"));
        ArrayAdapter<String> angleAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, angleValues);
        autoCompleteTextView_Angle.setAdapter(angleAdapter);

        List<String> gripWidthValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("grip_width"));
        ArrayAdapter<String> gripWidthAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, gripWidthValues);
        autoCompleteTextView_GripWidth.setAdapter(gripWidthAdapter);

        List<String> thumbsDirectionValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("thumbs_direction"));
        ArrayAdapter<String> thumbsDirectionAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, thumbsDirectionValues);
        autoCompleteTextView_ThumbsDirection.setAdapter(thumbsDirectionAdapter);

        List<String> SeparateHandsValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractInfoValueValueByHeader("separate_hands"));
        ArrayAdapter<String> SeparateHandsAdapter = new ArrayAdapter<>(this, R.layout.dropdown_item, SeparateHandsValues);
        autoCompleteTextView_SeparateHands.setAdapter(SeparateHandsAdapter);



        //new - Get workout ID for saving the exerciseAbstract
        //      check why we got here: Edit/Add
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_WORKOUT_ID) && (intent.hasExtra(EXTRA_EXERCISEABS_ID))){
            setTitle("Edit exercise");
            // Todo: set all relevant items and views
            // New
            // Handle workout id
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

            // Handle exerciseAbstract
            currentExerciseAbstract = exerciseAbstractViewModel.getExerciseAbsFromId(
                    intent.getIntExtra(EXTRA_EXERCISEABS_ID, -1));
            setExerciseAbstractToView(currentExerciseAbstract);
            operationAdapter.notifyDataSetChanged();
            nicknameAdapter.notifyDataSetChanged();
            setExerciseabsNameEditText();



            //Old
//            editTextExerciseAbsName.setText(intent.getStringExtra(EXTRA_EXERCISEABS_NAME));
//            editTextExerciseAbsDescription.setText(intent.getStringExtra(EXTRA_EXERCISEABS_DESCRIPTION));
//            spinnerMuscleGroup.setSelection(spinnerValues.indexOf(intent.getStringExtra(EXTRA_EXERCISEABS_MUSCLE)));
//            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);

        } else if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            setTitle("Add exercise");
            sourceWorkoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
        }else{
            setResult(RESULT_CANCELED,intent);
            finish();
        }

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

        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);

        // Explanation for how to access operationValues from inner method:
        // https://stackoverflow.com/questions/14425826/variable-is-accessed-within-inner-class-needs-to-be-declared-final
        // Retrieve relevant Operation data after muscle was chosen:
        autoCompleteTextView_Muscle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // clear current value in operation and nickname:
                autoCompleteTextView_Operation.clearListSelection();
                autoCompleteTextView_Nickname.clearListSelection();

                int current_muscle_id = exerciseAbstractViewModel.getExerciseAbstractInfoValueId(adapterView.getItemAtPosition(i).toString());
                operationValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractOperationByMuscleId(current_muscle_id));
                operationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autoCompleteTextView_Operation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // get muscle id in order to get operation id
                    int current_muscle_id = exerciseAbstractViewModel.getExerciseAbstractInfoValueId( autoCompleteTextView_Muscle.getText().toString());
                    int current_operation_id = exerciseAbstractViewModel.getExerciseAbstractOperationId(
                            String.valueOf(current_muscle_id),
                            autoCompleteTextView_Operation.getText().toString());

                    // if operation exist in db, update nickname string array to the matched operation:
                    if (current_operation_id != -1) {
                        nicknameValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractNicknameByOperationId(current_operation_id));
                    }
                    else{
                        nicknameValues = new ArrayList<>();
                    }
                    nicknameAdapter.notifyDataSetChanged();
                }

            }
        });

        autoCompleteTextView_Operation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setExerciseabsNameEditText();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        autoCompleteTextView_Nickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                setExerciseabsNameEditText();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Observer for detecting the recent exerciseAbstract id that was inserted (after pressing
        // save in this activity). after we get its id, we can save it to exercise_table with the
        // workout id.

        // Create the observer which updates the UI.
        final Observer<Long> exerciseAbstractInsertedIdObserver = new Observer<Long>() {
            @Override
            public void onChanged(@Nullable final Long idExerciseAbs) {
                Exercise exercise = new Exercise(idExerciseAbs.intValue(), sourceWorkoutID, "");
                exerciseViewModel.insert(exercise);
            }
        };
        exerciseAbstractViewModel.getInsertedExerciseAbstractId().observe(this, exerciseAbstractInsertedIdObserver );

    }

    private void saveExerciseAbs(){

        // Set global "currentExerciseAbstract" string using view's values:
        constructExerciseAbstractStringsFromView();

        // Make sure mandatory fields are not empty:
        String muscle = autoCompleteTextView_Muscle.getText().toString();
        String opName = autoCompleteTextView_Operation.getText().toString();
        if (muscle.trim().isEmpty() || opName.trim().isEmpty()){
            Toast.makeText(this, "Please fill mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // if operation doesn't exist in db, add it with nickname (if nickname not empty)
        // get muscle id in order to get operation id
        int current_muscle_id = exerciseAbstractViewModel.getExerciseAbstractInfoValueId(muscle);
        int current_operation_id = exerciseAbstractViewModel.getExerciseAbstractOperationId(
                String.valueOf(current_muscle_id),
                opName);

        if (current_operation_id == -1){
//            exerciseAbstractViewModel.insertOperation(
//                    String.valueOf(current_muscle_id),
//                    String.valueOf(current_operation_id));

            // if nickname is not empty, get the new inserted operation id and insert nickname as well
            if (!currentExerciseAbstract.getNickname().isEmpty()){
                current_operation_id = exerciseAbstractViewModel.getExerciseAbstractOperationId(
                        String.valueOf(current_muscle_id),
                        opName);
//                exerciseAbstractViewModel.insertNickname(
//                        String.valueOf(current_operation_id),
//                        String.valueOf(currentExerciseAbstract.getNickname()));
            }
        }


        // insert the new exerciseAbstract. inside insert, it fills the missing id's:
        exerciseAbstractViewModel.insert(currentExerciseAbstract);





        // old part:

/*        String exerciseAbsName = editTextExerciseAbsName.getText().toString();
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
        finish();*/
    }


    // after retrieving exercise abstract from db, get all text attributes from database using id's
    //  and set them to views:
    private void setExerciseAbstractToView(ExerciseAbstract exerciseAbstract){
        exerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractIdsToStrings(exerciseAbstract);
        autoCompleteTextView_Muscle.setText(exerciseAbstract.getMuscle());
        autoCompleteTextView_Operation.setText(exerciseAbstract.getOperation());
        autoCompleteTextView_Nickname.setText(exerciseAbstract.getNickname());
        autoCompleteTextView_LoadType.setText(exerciseAbstract.getLoad_type());
        autoCompleteTextView_Position.setText(exerciseAbstract.getPosition());
        autoCompleteTextView_Angle.setText(exerciseAbstract.getAngle());
        autoCompleteTextView_GripWidth.setText(exerciseAbstract.getGrip_width());
        autoCompleteTextView_ThumbsDirection.setText(exerciseAbstract.getThumbs_direction());
        autoCompleteTextView_SeparateHands.setText(exerciseAbstract.getSeparate_sides());
    }

    private void constructExerciseAbstractStringsFromView(){
        currentExerciseAbstract.setMuscle(autoCompleteTextView_Muscle.getText().toString());
        currentExerciseAbstract.setOperation(autoCompleteTextView_Operation.getText().toString());
        currentExerciseAbstract.setNickname(autoCompleteTextView_Nickname.getText().toString());
        currentExerciseAbstract.setLoad_type(autoCompleteTextView_LoadType.getText().toString());
        currentExerciseAbstract.setPosition(autoCompleteTextView_Position.getText().toString());
        currentExerciseAbstract.setAngle(autoCompleteTextView_Angle.getText().toString());
        currentExerciseAbstract.setGrip_width(autoCompleteTextView_GripWidth.getText().toString());
        currentExerciseAbstract.setThumbs_direction(autoCompleteTextView_ThumbsDirection.getText().toString());
        currentExerciseAbstract.setSeparate_sides(autoCompleteTextView_SeparateHands.getText().toString());

        currentExerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractStringsToIds(currentExerciseAbstract);
    }

    private void setExerciseabsNameEditText(){
        boolean existsAndValidEAMuscle = (autoCompleteTextView_Muscle.getText().toString() != null) &&
                                (!autoCompleteTextView_Muscle.getText().toString().isEmpty());
        boolean existsAndValidEAOp = (autoCompleteTextView_Operation.getText().toString() != null) &&
                                (!autoCompleteTextView_Operation.getText().toString().isEmpty());
        boolean existsAndValidEANickname = (autoCompleteTextView_Nickname.getText().toString() != null) &&
                                (!autoCompleteTextView_Nickname.getText().toString().isEmpty());

        String exerciseAbstractNameTitle = "";
        if (existsAndValidEAMuscle && existsAndValidEAOp){
            // if operation exists and nickname is empty, eAName is muscle+operation
            if(!(existsAndValidEANickname)){
                exerciseAbstractNameTitle = autoCompleteTextView_Muscle.getText().toString() + " " +
                        autoCompleteTextView_Operation.getText().toString();
            }
            // else if operation and nickname exists, eAName is nickname+operation
            else {
                exerciseAbstractNameTitle = autoCompleteTextView_Nickname.getText().toString() + " " +
                        autoCompleteTextView_Operation.getText().toString();
            }
        }
        textInputEditText_exName.setText(exerciseAbstractNameTitle);
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

