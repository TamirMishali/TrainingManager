package com.example.tamirmishali.trainingmanager.ExerciseAbstract;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

/*TODO 26.12.2022:
    - (MEDIUM) make operation and nickname autoCompleteTextViews show the options with zero chars written.
    - (LOW) if "main_muscle" is null/empty/whatever, make all other views unreachable.
    - (LOW) add a small 'i' button for information near operation_name to view all possible operations.
    - In the far future:
        - (MEDIUM) add a "plus" button near each field (except sep_hands) to add
          new attributes to the sql table.
        - (MEDIUM) add menu buttons for deleting "operation_name"s, "nickname"s (not all, but from a list)
        - (LOW) add menu button for editing the load_type, position, angle etc...
    */

public class AddEditExerciseAbsActivity extends AppCompatActivity {
    public static final String EXTRA_EXERCISEABS_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_EXERCISEABS_ID";
    public static final  String EXTRA_WORKOUT_ID =
            "com.example.tamirmishali.trainingmanager.EXTRA_WORKOUT_ID";

    // new
    private static final String TAG = "AddEditExerciseAbsActivity";
//    private ExerciseAbstract currentExerciseAbstract = new ExerciseAbstract();
    int workoutID;
    int exerciseAbstractID;
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

        exerciseAbstractViewModel = new ViewModelProvider(this).get(ExerciseAbstractViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);

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

            // Handle workout id
            workoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
            exerciseAbstractID = intent.getIntExtra(EXTRA_EXERCISEABS_ID, -1);

            // Handle exerciseAbstract
            ExerciseAbstract currentExerciseAbstract = exerciseAbstractViewModel.getExerciseAbsFromId(
                                                            exerciseAbstractID);

            setExerciseAbstractToView(currentExerciseAbstract);
            operationAdapter.notifyDataSetChanged();
            nicknameAdapter.notifyDataSetChanged();
            setExerciseabsNameEditText();


        } else if (intent.hasExtra(EXTRA_WORKOUT_ID)) {
            setTitle("Add new exercise");
            workoutID = intent.getIntExtra(EXTRA_WORKOUT_ID, -1);
            exerciseAbstractID = 0;
            // todo: (MEDIUM) fix the default value. with the comment out code, it makes "No" the only option.
//            autoCompleteTextView_SeparateHands.setText("No");
        }else{
            // todo: (MEDIUM) make the cancelation go back to list of exercises, and not list of Workouts.
            setResult(RESULT_CANCELED,intent);
            finish();
        }

        autoCompleteTextView_Muscle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // clear current value in operation and nickname:
                autoCompleteTextView_Operation.setText("");
                autoCompleteTextView_Nickname.setText("");
//                Log.d(TAG, "autoCompleteTextView_Muscle.setOnItemClickListener Triggered");

                int current_muscle_id = exerciseAbstractViewModel.getExerciseAbstractInfoValueId(adapterView.getItemAtPosition(position).toString());
                operationValues = new ArrayList<>(exerciseAbstractViewModel.getExerciseAbstractOperationByMuscleId(current_muscle_id));
                operationAdapter.clear();
                operationAdapter.addAll(operationValues);
                operationAdapter.notifyDataSetChanged();
            }
        });

        autoCompleteTextView_Operation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // remove the keyboard that stays on when moving to other view:
                    // https://stackoverflow.com/questions/15412943/hide-soft-keyboard-on-losing-focus#:~:text=Best%20way%20is%20to%20set,when%20the%20EditText%20loses%20focus.
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

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
                    nicknameAdapter.clear();
                    nicknameAdapter.addAll(nicknameValues);
                    nicknameAdapter.notifyDataSetChanged();
                }

            }
        });

        autoCompleteTextView_Nickname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    // remove the keyboard that stays on when moving to other view:
                    // https://stackoverflow.com/questions/15412943/hide-soft-keyboard-on-losing-focus#:~:text=Best%20way%20is%20to%20set,when%20the%20EditText%20loses%20focus.
                    InputMethodManager imm =  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    }

    private void saveExerciseAbs(){
        Log.d(TAG, "Starting to save exercise abstract");
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

        if (current_operation_id == 0) {
            ExerciseAbstractOperation exerciseAbstractOperation = new ExerciseAbstractOperation(current_muscle_id, opName);
            exerciseAbstractViewModel.insertOperation(exerciseAbstractOperation);
            Log.d(TAG, "Insertion of new operation: " + opName);
        }

        // if nickname is not empty, get the new inserted operation id and insert nickname as well
        // todo: (16.01.2023) i don't check if nickname exists in db before inserting new one. do it.
        if (!autoCompleteTextView_Nickname.getText().toString().isEmpty()){
            // Check if nickname exists for current exercise first

            current_operation_id = exerciseAbstractViewModel.getExerciseAbstractOperationId(
                    String.valueOf(current_muscle_id),
                    opName);

            new ExerciseAbstractNickname();
            ExerciseAbstractNickname exerciseAbstractNickname1;
            exerciseAbstractNickname1 = exerciseAbstractViewModel.getExerciseAbstractNickname(current_operation_id, autoCompleteTextView_Nickname.getText().toString());

            if (exerciseAbstractNickname1 == null){
                ExerciseAbstractNickname exerciseAbstractNickname = new ExerciseAbstractNickname();
                exerciseAbstractNickname.setNickname(autoCompleteTextView_Nickname.getText().toString());
                exerciseAbstractNickname.setId_exerciseabs_operation(current_operation_id);
                exerciseAbstractViewModel.insertNickname(exerciseAbstractNickname);
                Log.d(TAG, "Insertion of new nickname: " + autoCompleteTextView_Nickname.getText().toString());
            }


        }

        // this means old exerciseAbstract to edit, and not a new one:
//        int exerciseAbstractID = currentExerciseAbstract.getId();

        // Set global "currentExerciseAbstract" string using view's values:
        // This line deletes the old id that was inside, therefor, back it up and insert back as
        //   finishing loading view to object
        ExerciseAbstract currentExerciseAbstract = constructExerciseAbstractStringsFromView();
        currentExerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractStringsToIds(currentExerciseAbstract);

        // INIT to be INSERTED exercise:
        Exercise exercise = new Exercise();
        exercise.setId_workout(workoutID);
        exercise.setComment("");

        // If EA already exists in database, get its id:
        currentExerciseAbstract.setId(exerciseAbstractViewModel.getExerciseAbstractId(currentExerciseAbstract));

        // ------ This if/else is handling the ExerciseAbstract ------
        // Check if the new EA already exists in exerciseAbstract_table in DB:
        if (currentExerciseAbstract.getId() != 0){ // if it exists in DB
            // use the already exist one, don't create and insert a new EA:
            exercise.setId_exerciseabs(currentExerciseAbstract.getId());
            Log.d(TAG, "ExerciseAbstract already exist in DB with id = " + currentExerciseAbstract.getId());
        }
        else{ // if it doesn't exists in DB
            // insert it to DB:
            exerciseAbstractViewModel.insert(currentExerciseAbstract);
            int insertedExerciseAbstractId = exerciseAbstractViewModel.getExerciseAbstractId(currentExerciseAbstract);
            currentExerciseAbstract.setId(insertedExerciseAbstractId);
            Log.d(TAG, "New ExerciseAbstract have been inserted to DB with id = " + currentExerciseAbstract.getId());
            exercise.setId_exerciseabs(currentExerciseAbstract.getId());
        }

        // Now i have a full EA with a valid id. new one or one obtained from DB.
        // And a full Exercise object with all fields except id.

        // ------ This if/else is handling the add/edit Exercise ------
        // We are on "new exercise" activity:
        if (exerciseAbstractID == 0) {
            // Check if exercise exist in current workout already:
            Exercise exercise1 = exerciseViewModel.getExerciseForWorkout(
                    currentExerciseAbstract.getId(), workoutID);
            // if doesn't exist in current workout:
            if (exercise1 == null) {
                // Insert new exercise to DB:
                exerciseViewModel.insert(exercise);
                Log.d(TAG, "Inserted new Exercise to DB");
            }
            else{
                Toast.makeText(this, "Exercise already exists in this workout", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        // We are on "edit exercise" activity:
        // Conclusion: if im editing an EA, i need to check if its old version is used in any other
        //  exercise in DB. if so, leave it be. if not, delete it so if wont stay up in DB with no parent.
        else{
            // get edited Exercise:
            Exercise exercise1 = exerciseViewModel.getExerciseForWorkout(
                    exerciseAbstractID, workoutID);

            // insert its id to the new exercise with the updated/relevant EA we just created:
            exercise.setId((exercise1.getId()));

            // UPDATED exercise with new/old EA for that workout:
            exerciseViewModel.update(exercise);
            Log.d(TAG, "Updated Exercise to DB with an updated ExerciseAbstract");

            // now try to delete the old exerciseAbstract that we started editing in the first place.
            // if some other Exercise is linked to it, it will still be deleted, so i must check it before:
            try {
                List<Exercise> exerciseList = exerciseViewModel.getExercisesForEA(exerciseAbstractID);
                if (exerciseList.isEmpty()) {
                    exerciseAbstractViewModel.delete(exerciseAbstractViewModel.getExerciseAbsFromId(exerciseAbstractID));
                    Log.d(TAG, "Deleted old unlinked ExerciseAbstract id: " + exerciseAbstractID);
                }
            } finally {
                Log.d(TAG, "Could not delete old ExerciseAbstract because it is linked in db. id = " + exerciseAbstractID);
            }
        }

        setResult(RESULT_OK);
        finish();

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

    private ExerciseAbstract constructExerciseAbstractStringsFromView(){
        ExerciseAbstract exerciseAbstract = new ExerciseAbstract();
        exerciseAbstract.setMuscle(autoCompleteTextView_Muscle.getText().toString());
        exerciseAbstract.setOperation(autoCompleteTextView_Operation.getText().toString());
        exerciseAbstract.setNickname(autoCompleteTextView_Nickname.getText().toString());
        exerciseAbstract.setLoad_type(autoCompleteTextView_LoadType.getText().toString());
        exerciseAbstract.setPosition(autoCompleteTextView_Position.getText().toString());
        exerciseAbstract.setAngle(autoCompleteTextView_Angle.getText().toString());
        exerciseAbstract.setGrip_width(autoCompleteTextView_GripWidth.getText().toString());
        exerciseAbstract.setThumbs_direction(autoCompleteTextView_ThumbsDirection.getText().toString());
        exerciseAbstract.setSeparate_sides(autoCompleteTextView_SeparateHands.getText().toString());

//        exerciseAbstract = exerciseAbstractViewModel.ExerciseAbstractStringsToIds(exerciseAbstract);

        return exerciseAbstract;
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
        textInputEditText_exName.setText(exerciseAbstractNameTitle.strip());
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

