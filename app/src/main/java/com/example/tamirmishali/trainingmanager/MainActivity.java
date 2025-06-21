package com.example.tamirmishali.trainingmanager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tamirmishali.trainingmanager.Exercise.Exercise;
import com.example.tamirmishali.trainingmanager.Exercise.ExerciseViewModel;
import com.example.tamirmishali.trainingmanager.History.History;
import com.example.tamirmishali.trainingmanager.Routine.ShowRoutines;
import com.example.tamirmishali.trainingmanager.Routine.Routine;
import com.example.tamirmishali.trainingmanager.Routine.RoutineViewModel;
import com.example.tamirmishali.trainingmanager.Set.Set;
import com.example.tamirmishali.trainingmanager.Set.SetViewModel;
import com.example.tamirmishali.trainingmanager.Workout.Workout;
import com.example.tamirmishali.trainingmanager.Workout.WorkoutViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.widget.Toast.*;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;


/*TODO
 * - (MEDIUM) Deep change in DB 2: add the option to do super sets/drop sets
 * - (LOW) add a rolling dumbbell falling and rolling on main screen when opening the app
* */

/*TODO 08.02.2024
 * - (HIGH) Make the option to change the order of exercises appearance.
 * - (MEDIUM) When adding new exercise, open automatically the "Main muscle" scroll-down.
 * - (MEDIUM) When inserting exercises to a new workout, add the option to insert number of sets.
 * - (MEDIUM) Add attribute to exercise, "Contraction type": concentric, eccentric, static
 * - (LOW) When choosing cables, add the option for the instrument that will be attached to is.
 * */

public class MainActivity extends AppCompatActivity {
    public static final int EDIT_LAST_WORKOUT = 1;
    public static final int GET_DB_PATH_REQUEST_CODE = 2;
    protected static final String ACTION_FINISH_WORKOUT = "finish_workout";

    protected static String DATABASE_NAME = "training_manager_database";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    // was a to_do. if there is no problem with all the commenting, remove all "routine" lines in
    // code (25.01.2023): (LOW) why there is a liveData object in main activity? remove it.
//    LiveData<List<Routine>> routines;

    private RoutineViewModel routineViewModel;
    private WorkoutViewModel workoutViewModel;
    private ExerciseViewModel exerciseViewModel;
    private SetViewModel setViewModel;

    private ActivityResultLauncher<Intent> importDbLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        importDbLauncher = registerForActivityResult(
                new androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        if (uri != null) {
                            try {
                                importDatabase(uri);
                                Toast.makeText(this, "Database restored successfully", Toast.LENGTH_SHORT).show();
                                restartApp(); // Optional but useful
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(this, "Failed to restore database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );


        setContentView(R.layout.activity_main);

        // new AndroidX thing:
        // https://stackoverflow.com/questions/57534730/as-viewmodelproviders-of-is-deprecated-how-should-i-create-object-of-viewmode
        workoutViewModel = new ViewModelProvider(this).get(WorkoutViewModel.class);
        routineViewModel = new ViewModelProvider(this).get(RoutineViewModel.class);
        exerciseViewModel = new ViewModelProvider(this).get(ExerciseViewModel.class);
        setViewModel = new ViewModelProvider(this).get(SetViewModel.class);

        // WorkoutNow
        LinearLayout button_WorkoutNow = findViewById(R.id.linearLayout_workout_now);
//        TextView textView = findViewById(R.id.linearLayout_workout_now_tv);

        // History
        LinearLayout button_History = findViewById(R.id.linearLayout_history);
        TextView historyTextView = findViewById(R.id.linearLayout_history_tv);
        historyTextView.setPaintFlags(historyTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // EditWorkouts
        LinearLayout button_EditWorkout = findViewById(R.id.linearLayout_edit_workouts);
        TextView editWorkoutsTextView = findViewById(R.id.linearLayout_edit_workouts_tv);
        editWorkoutsTextView.setPaintFlags(historyTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        // Update textview to "start new workout" or "continue prev workout":
        update_text_workout_now_layout();

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
                    // LastWorkout is the most recent workout with date!=null from the most recent routine.
                    Workout workout = workoutViewModel.getLastWorkout();
                    List<Set> sets = new ArrayList<>();
                    // if last workout is null, it means that there are only abstract workouts in DB for most recent routine.
                    if (workout == null) {
                        // open dialog to chose what workout you want to start:
                        open_dialog(v);
                        return;
                    }
                    else // get all sets that are not filled from last workout
                        sets = setViewModel.getUnfilledSetsForWorkout(workout.getId());
                    if (sets.isEmpty()) { // if last workout filled properly - there are no unfinished sets
                        //Open Dialog of relevant workouts and their dates
                        open_dialog(v);
                    } else { // if missing data in sets of last workout
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
                Intent intent = new Intent(v.getContext(), ShowRoutines.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        // Update textview to "start new workout" or "continue prev workout":
        update_text_workout_now_layout();
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
        else if (requestCode == GET_DB_PATH_REQUEST_CODE && resultCode == RESULT_OK){
            if (data == null){
                return;
            }
            Uri uri_db = data.getData();
            Toast.makeText(this, uri_db.getPath(), LENGTH_SHORT).show();
        }

        else {
            //Toast.makeText(this,"Nothing and nothing", Toast.LENGTH_SHORT).show();
        }
    }


//    private Boolean dataValidation() {
//        //check for existing routines
//        routines = routineViewModel.getAllRoutines();
//        if (routines.getValue().isEmpty()) {
//            Toast.makeText(this,"No routines",Toast.LENGTH_SHORT).show();
//            return Boolean.FALSE;
//        }
//
//        //check for existing workouts
//
//
//        //check for at least one exercise exists in workout
//        //check for full sets fields
//
//        return Boolean.TRUE;
//    }

    public void update_text_workout_now_layout(){
        // Some tests to see what to write in the workout now button

//        LinearLayout button_WorkoutNow = findViewById(R.id.linearLayout_workout_now);
        TextView textView = findViewById(R.id.linearLayout_workout_now_tv);
        TextView textView_info = findViewById(R.id.linearLayout_workout_now_tv_info);
        ImageView imageView = findViewById(R.id.iv_new_or_edit_workout);

        String layoutStatus = "";
        String workout_name = "";

        Routine routine = routineViewModel.getFirstRoutine();
        if (routine == null) {
            layoutStatus = "no_routines";

        } else {
            layoutStatus = "new_workout";

            // get most recent workout from most recent routine
            Workout most_recent_workout = workoutViewModel.getLastWorkout();

            if (most_recent_workout != null){
                workout_name = most_recent_workout.getWorkoutName();
                List<Set> sets = setViewModel.getUnfilledSetsForWorkout(most_recent_workout.getId());

                // If last workout filled properly, set text to "start new workout now". else set
                // text to the current workout and routine name.
                if (!(sets.isEmpty() || sets == null)) {
                    layoutStatus = "continue_workout";

                }
            }
        }


//        https://proandroiddev.com/android-developer-beginner-faq-2-gravity-vs-layout-gravity-3c461bdeae12
//        Gravity and layout_gravity explanations

        switch(layoutStatus){
            case "no_routines":
                textView.setText("No Routines available");
                textView_info.setVisibility(View.GONE);
                imageView.setVisibility(View.GONE);
                break;

            case "new_workout":
                textView.setText(getString(R.string.const_Start_new_workout_now));
                imageView.setImageResource(R.drawable.ic_main_activity_new_workout);
                textView_info.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                break;

            case "continue_workout":
                textView.setText(getString(R.string.const_continue_current_workout));
                imageView.setImageResource(R.drawable.ic_main_activity_edit_pencil);

                // Construct Bold routine and workout and their normal information
                // Bold "Routine: ", and then add non-bold routine name + new line
                // https://stackoverflow.com/questions/16961796/android-textview-in-bold-and-normal-text
                SpannableStringBuilder s = new SpannableStringBuilder("Routine: ");
                s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
                textView_info.setText(s);
                textView_info.append(routine.getRoutineName()+"\n");


                // Bold "Workout: ", and then add non-bold workout name
                s = new SpannableStringBuilder("Workout: ");
                s.setSpan(new StyleSpan(Typeface.BOLD), 0, s.length(), 0);
                textView_info.append(s);
                textView_info.append(workout_name);

                textView_info.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
                break;
        }

        // https://stackoverflow.com/questions/2394935/can-i-underline-text-in-an-android-layout
        // awsleiman answer
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

    public void open_dialog(View v){
        // get all last practical workouts using the most updated routine:
        List<Workout> lastPracticalWorkouts;

        // Get desired routine from preferences
        int mainRoutineId = com.example.tamirmishali.trainingmanager.PreferenceUtils.getMainRoutineId(this);
        if (mainRoutineId == -1){
            lastPracticalWorkouts = workoutViewModel.getWorkoutsForDialog(workoutViewModel.getNewestRoutineId());
        }
        else{
            lastPracticalWorkouts = workoutViewModel.getWorkoutsForDialog(mainRoutineId);
        }


        // build dialog and its view and show it:
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.workoutnow_select_workout_listview,null);
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
                // get selected workout id
                Workout selectedWorkout = finalLastPracticalWorkouts.get(position);

                // make sure the abstract Workout of the selected Workout has exercises in it:
                Workout abstractWorkoutOfSelectedWorkout = workoutViewModel.getAbstractWorkoutFromPractical(
                        workoutViewModel.getNewestRoutineId(),
                        selectedWorkout.getWorkoutName());
                List<Exercise> exerciseList = (exerciseViewModel.getExercisesForWorkout(abstractWorkoutOfSelectedWorkout.getId()));

                if (exerciseList.isEmpty()){
                    Toast.makeText(MainActivity.this,"Selected workout has no exercises",Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                    dialog.cancel();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                    Intent intent = new Intent(MainActivity.this, WorkoutNow.class);
                    intent.putExtra(WorkoutNow.EXTRA_WORKOUT_ID, selectedWorkout.getId());
                    startActivityForResult(intent, EDIT_LAST_WORKOUT);
                }
            }
        });

    }


    // https://stackoverflow.com/questions/51030963/add-three-dots-in-toolbar#:~:text=Right%2Dclick%20res%20folder%20and,give%20you%20the%203%20dots.
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.mainactivity_menu,menu);
        return true;
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        importDbLauncher.launch(intent);
    }

    private void importDatabase(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);

        File dbFile = getDatabasePath(DATABASE_NAME + ".db"); // use your correct .db file name
        OutputStream outputStream = new FileOutputStream(dbFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    private void restartApp() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }


    // https://stackoverflow.com/questions/6540906/simple-export-and-import-of-a-sqlite-database-on-android
    private boolean exportDB() {
        boolean operationStatus = false;
        try {
            File DirectoryName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File dbFile = new File(this.getDatabasePath(DATABASE_NAME).getAbsolutePath());
            Log.d("MainActivity.exportDB()", "database absolut file path is:\n " + String.valueOf(dbFile));
            FileInputStream fis = new FileInputStream(dbFile + ".db");

            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            String outFileName = DirectoryName.getPath() + File.separator +
                    DATABASE_NAME + "_" + currentDate + ".db";

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            // Close the streams
            output.flush();
            output.close();
            fis.close();
            operationStatus = true;
            Log.e("dbBackup:", "DB exported successfully");

        } catch (IOException e) {
            Log.e("dbBackup:", e.getMessage());
            operationStatus = false;
        }
        return operationStatus;
    }

    // This is the 3 dots options in main activity.
    // It triggers the openFileChooser function that opens the data storage to pick a file to backup from
    // https://www.youtube.com/watch?v=go5BdWCKLFk&ab_channel=SWIKbyMirTahaAli
    private static final int IMPORT_DB_REQUEST_CODE = 1001;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_db:
                openFileChooser();
                return true;

            case R.id.export_db:
                if (exportDB()) {
                    Toast.makeText(this, "DB export file can be found in download folder", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "DB export failed", Toast.LENGTH_SHORT).show();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

